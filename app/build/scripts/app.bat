@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

<<<<<<< HEAD
@if "%DEBUG%"=="" @echo off
=======
@if "%DEBUG%" == "" @echo off
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
@rem ##########################################################################
@rem
@rem  app startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
<<<<<<< HEAD
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
=======
if "%DIRNAME%" == "" set DIRNAME=.
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and APP_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
<<<<<<< HEAD
if %ERRORLEVEL% equ 0 goto execute
=======
if "%ERRORLEVEL%" == "0" goto execute
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\app.jar;%APP_HOME%\lib\javafx-fxml-20.0.1.jar;%APP_HOME%\lib\javafx-controls-20.0.1-mac.jar;%APP_HOME%\lib\javafx-controls-20.0.1.jar;%APP_HOME%\lib\javafx-graphics-20.0.1-mac.jar;%APP_HOME%\lib\javafx-graphics-20.0.1.jar;%APP_HOME%\lib\javafx-base-20.0.1-mac.jar;%APP_HOME%\lib\javafx-base-20.0.1.jar;%APP_HOME%\lib\guava-30.1.1-jre.jar;%APP_HOME%\lib\mysql-connector-java-8.0.28.jar;%APP_HOME%\lib\html2pdf-3.0.2.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-3.8.0.jar;%APP_HOME%\lib\error_prone_annotations-2.5.1.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\protobuf-java-3.11.4.jar;%APP_HOME%\lib\barcodes-7.1.16.jar;%APP_HOME%\lib\font-asian-7.1.16.jar;%APP_HOME%\lib\sign-7.1.16.jar;%APP_HOME%\lib\forms-7.1.16.jar;%APP_HOME%\lib\hyph-7.1.16.jar;%APP_HOME%\lib\svg-7.1.16.jar;%APP_HOME%\lib\styled-xml-parser-7.1.16.jar;%APP_HOME%\lib\layout-7.1.16.jar;%APP_HOME%\lib\pdfa-7.1.16.jar;%APP_HOME%\lib\kernel-7.1.16.jar;%APP_HOME%\lib\io-7.1.16.jar;%APP_HOME%\lib\slf4j-api-1.7.30.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\bcpkix-jdk15on-1.68.jar;%APP_HOME%\lib\bcprov-jdk15on-1.68.jar


@rem Execute app
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %APP_OPTS%  -classpath "%CLASSPATH%" cookbook.Cookbook %*

:end
@rem End local scope for the variables with windows NT shell
<<<<<<< HEAD
if %ERRORLEVEL% equ 0 goto mainEnd
=======
if "%ERRORLEVEL%"=="0" goto mainEnd
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae

:fail
rem Set variable APP_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
<<<<<<< HEAD
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%APP_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%
=======
if  not "" == "%APP_EXIT_CONSOLE%" exit 1
exit /b 1
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
