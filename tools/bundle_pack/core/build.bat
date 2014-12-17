mkdir classes
javac -classpath ..\lib\* *.java -d classes
cd classes
jar cf ..\core.jar .
cd ..
move core.jar ..\lib
rmdir /q /s classes
pause