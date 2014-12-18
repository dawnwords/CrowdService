mkdir classes
javac -classpath ..\lib\* *.java -d classes
cd classes
jar cf ..\core.jar .
cd ..
rmdir /q /s classes
move core.jar ..\lib