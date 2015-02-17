rem set remote=\\10.131.252.156\share\tomcat7\webapps\obr\
set remote=D:\Development\Workspace\OBR\obr\
set jar=%1\%2

mkdir %1
copy ..\out\%jar% %1

call modifyManifest.bat
call jar2dex.bat
copy %jar% %remote%%1
rmdir /s /q %1