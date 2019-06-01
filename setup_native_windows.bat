SET PATH=%JAVA_HOME%;%PATH%

git clone https://bitbucket.org/chromiumembedded/java-cef.git jcef
cd jcef
git checkout 1fda5d8f948670d08ef86bc4e8637b8581995ce9

echo "Running cmake..."
mkdir jcef_build
cd jcef_build
cmake -G "%generatorName%" ..

echo "Building the JCEF Java classes..."
cd c:\projects\journey\jcef\tools\
call compile.bat %buildType%