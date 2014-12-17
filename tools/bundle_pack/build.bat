mkdir classes
javac *.java -cp ..\..\lib\* -d classes
cd classes
jar cfm ../%jar% ../MANIFEST.MF .
cd ..
rmdir /q /s classes
move %jar% ..
cd ..