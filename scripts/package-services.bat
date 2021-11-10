@echo off

echo [!] Checking maven
set "mvnErr=0"
call mvn -v || set mvnErr=1
if "%mvnErr%" == "0" echo [+] Maven found && goto execute

echo [-] Maven executable (mvn) could not be found
echo [!] Please add mvn executable to your PATH variable
echo [-] Script cannot continue
goto end

:execute

@rem Configuration Server
echo [!] Packaging Configuration Server
cd ../configuration-server/
set "mvnErr=0"
call mvn clean package install "-Dmaven.test.skip=true" || set mvnErr=1
if "%mvnErr%" == "0" echo [+] Configuration Server successfully packaged
if "%mvnErr%" == "1" echo [-] Error packaging Configuration Server. Script cannot continue && goto end

@rem Discovery Service
echo [!] Packaging Discovery Service
cd ../discovery-service/
set "mvnErr=0"
call mvn clean package install "-Dmaven.test.skip=true" || set mvnErr=1
if "%mvnErr%" == "0" echo [+] Discovery Service successfully packaged
if "%mvnErr%" == "1" echo [-] Error packaging Discovery Service. Script cannot continue && goto end

@rem Gateway Service
echo [!] Packaging Gateway Service
cd ../gateway-service/
set "mvnErr=0"
call mvn clean package install "-Dmaven.test.skip=true" || set mvnErr=1
if "%mvnErr%" == "0" echo [+] Gateway Service successfully packaged
if "%mvnErr%" == "1" echo [-] Error packaging Gateway Service. Script cannot continue && goto end

@rem Users Service
echo [!] Packaging Users Service
cd ../users-service/
set "mvnErr=0"
call mvn clean package install "-Dmaven.test.skip=true" || set mvnErr=1
if "%mvnErr%" == "0" echo [+] Users Service successfully packaged
if "%mvnErr%" == "1" echo [-] Error packaging Users Service. Script cannot continue && goto end

@rem Products Service
echo [!] Packaging Products Service
cd ../products-service/
set "mvnErr=0"
call mvn clean package install "-Dmaven.test.skip=true" || set mvnErr=1
if "%mvnErr%" == "0" echo [+] Products Service successfully packaged
if "%mvnErr%" == "1" echo [-] Error packaging Products Service. Script cannot continue && goto end

@rem Comments Service
echo [!] Packaging Comments Service
cd ../comments-service/
set "mvnErr=0"
call mvn clean package install "-Dmaven.test.skip=true" || set mvnErr=1
if "%mvnErr%" == "0" echo [+] Comments Service successfully packaged
if "%mvnErr%" == "1" echo [-] Error packaging Comments Service. Script cannot continue && goto end

:end
cd ../scripts/
