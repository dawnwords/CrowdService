mkdir %bundles_path%
copy ..\interfaces\*.jar %bundles_path%
copy ..\out\%bundles_path%\*.jar %bundles_path%

for /f "delims=|" %%i in ('dir /b %bundles_path%') do (
	set jar=%bundles_path%\%%i
	call jar2dex.bat
)

set bindex="org.osgi.impl.bundle.bindex-2.2.0.jar"
set repoxml=%bundles_path%_repo.xml
set url=http://10.131.252.156:8080/obr/
set remote=\\10.131.252.156\share\tomcat7\webapps\obr\

echo change %bundles_path% repository url
java -cp %bindex% -jar %bindex% -n %repo_name% -q -r %repoxml% %bundles_path%
fart %repoxml% uri=' uri='%url%

rmdir /s /q %remote%%bundles_path%
del /q %remote%%repoxml%

echo deploy %bundles_path%
mkdir %remote%%bundles_path%
copy %bundles_path%\* %remote%%bundles_path%
move %repoxml% %remote%%repoxml%
rmdir /s /q %bundles_path%