mkdir classes
javac *.java -cp ..\..\lib\* -d classes
jar cfm %jar% MANIFEST.MF -C classes .
call dx.bat --dex --debug --output=classes.dex %jar% 
aapt add %jar% classes.dex
rmdir /q /s classes
del /q /f classes.dex
move %jar% ..