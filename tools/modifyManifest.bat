jar xf %jar% META-INF/MANIFEST.MF
cd META-INF
echo Require-Capability:  >> MANIFEST.MF.new
findstr /v Require MANIFEST.MF >> MANIFEST.MF.new
move MANIFEST.MF.new MANIFEST.MF
cd ..
jar -ufm %jar% META-INF/MANIFEST.MF
rmdir /s /q META-INF