@echo off

set ERROR_CODE=0

@REM set local scope for the variables
@setlocal

set CMD_LINE_ARGS=%*
set PRGDIR=%~dp0

if "%JAVACMD%"=="" set JAVACMD=java

%JAVACMD% %JAVA_OPTS% -jar %PRGDIR%\katapult.jar %CMD_LINE_ARGS%
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error

@endlocal
set ERROR_CODE=%ERRORLEVEL%

:end

@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal

:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%