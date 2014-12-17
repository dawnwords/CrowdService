@echo off
set bindex="org.osgi.impl.bundle.bindex-2.2.0.jar"
set bundles_path="bundles"
java -cp /%bindex% -jar %bindex% -n IGB -q -r repository.xml %bundles_path%
pause