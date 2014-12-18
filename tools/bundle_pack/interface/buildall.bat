@echo off
for /f "delims=|" %%i in ('dir /b') do (
	if exist %%i\* (
		echo build %%i
		cd %%i
		call build.bat
	)
)