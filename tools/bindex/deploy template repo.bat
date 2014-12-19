@echo off
set bundles_path=template
copy ..\bundle_pack\interface\*.jar bundles_path
copy ..\bundle_pack\template\*.jar bundles_path
call buildAndDeploy.bat