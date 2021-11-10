@echo off

echo [!] Checking docker
set "dockerErr=0"
call docker -v || set dockerErr=1
if "%dockerErr%" == "0" echo [+] Docker found && goto checkdockerid

echo [-] Maven executable (mvn) could not be found
echo [!] Please add mvn executable to your PATH variable
echo [-] Script cannot continue
goto end

:checkdockerid

if not "%1" == "" echo [!] Creating images for %1 && goto execute

echo [-] DockerID could not be found
echo [!] Please start the script passing your DockerID as arguments
echo [-] Script cannot continue
goto end

:execute

@rem Configuration Server
echo [!] Imaging Configuration Server
cd ../configuration-server/
set "dockerErr=0"
call docker build . --tag %1/configuration-server --force-rm=true || set dockerErr=1
if "%dockerErr%" == "0" echo [+] Configuration Server successfully imaged
if "%dockerErr%" == "1" echo [-] Error imaging Configuration Server. Script cannot continue && goto end

@rem Discovery Service
echo [!] Imaging Discovery Service
cd ../discovery-service/
set "dockerErr=0"
call docker build . --tag %1/discovery-service --force-rm=true || set dockerErr=1
if "%dockerErr%" == "0" echo [+] Discovery Service successfully imaged
if "%dockerErr%" == "1" echo [-] Error imaging Discovery Service. Script cannot continue && goto end

@rem Gateway Service
echo [!] Imaging Gateway Service
cd ../gateway-service/
set "dockerErr=0"
call docker build . --tag %1/gateway-service --force-rm=true || set dockerErr=1
if "%dockerErr%" == "0" echo [+] Gateway Service successfully imaged
if "%dockerErr%" == "1" echo [-] Error imaging Gateway Service. Script cannot continue && goto end

@rem Users Service
echo [!] Imaging Users Service
cd ../users-service/
set "dockerErr=0"
call docker build . --tag %1/users-service --force-rm=true || set dockerErr=1
if "%dockerErr%" == "0" echo [+] Users Service successfully imaged
if "%dockerErr%" == "1" echo [-] Error imaging Users Service. Script cannot continue && goto end

@rem Products Service
echo [!] Imaging Products Service
cd ../products-service/
set "dockerErr=0"
call docker build . --tag %1/products-service --force-rm=true || set dockerErr=1
if "%dockerErr%" == "0" echo [+] Products Service successfully imaged
if "%dockerErr%" == "1" echo [-] Error imaging Products Service. Script cannot continue && goto end

@rem Comments Service
echo [!] Imaging Comments Service
cd ../comments-service/
set "dockerErr=0"
call docker build . --tag %1/comments-service --force-rm=true || set dockerErr=1
if "%dockerErr%" == "0" echo [+] Comments Service successfully imaged
if "%dockerErr%" == "1" echo [-] Error imaging Comments Service. Script cannot continue && goto end

:end
cd ../scripts/
