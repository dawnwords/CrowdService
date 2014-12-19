echo jar2dex: %jar%
call dx.bat --dex --debug --output=classes.dex %jar% 
aapt add %jar% classes.dex
del /q /f classes.dex