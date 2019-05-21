echo "Making distribution..."
cd c:\projects\journey\jcef\tools\
call make_distrib.bat %buildType%
cd ..\binary_distrib
7z a jcef-distrib-%fullBuildType%.zip %buildType%