set abt="D:\Development\Android\sdk\build-tools\android-4.4W\"
rmdir /q out
mkdir out
for %%i in (*.jar) do (
	copy /y %%i out\%%i
	%abt%dx --dex --output=classes.dex %%i
	%abt%aapt add out\%%i classes.dex
	del /q /f classes.dex
)
pause