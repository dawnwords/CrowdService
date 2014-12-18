set bindex="org.osgi.impl.bundle.bindex-2.2.0.jar"
set repoxml=%bundles_path%_repo.xml
set url=http://10.131.252.156:8080/obr/
set remote=\\10.131.252.156\share\tomcat7\webapps\obr\

java -cp %bindex% -jar %bindex% -n IGB -q -r %repoxml% %bundles_path%
fart %repoxml% uri=' uri='%url%

rmdir /q %remote%%bundles_path%
del /q %remote%%repoxml%

mkdir %remote%%bundles_path%
copy %bundles_path%\* %remote%%bundles_path%
copy %repoxml% %remote%%repoxml%

pause