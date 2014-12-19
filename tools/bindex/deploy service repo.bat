@echo off
set bundles_path=service
copy ..\bundle_pack\interface\*.jar bundles_path
copy ..\bundle_pack\service\*.jar bundles_path
call buildAndDeploy.bat