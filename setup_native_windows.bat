SET PATH=%JAVA_HOME%;%PATH%

git clone https://bitbucket.org/chromiumembedded/java-cef.git jcef
cd jcef
git checkout d348788e3347fa4d2a421773463f7dd62da60991

echo "Running cmake..."
mkdir jcef_build
cd jcef_build
cmake -G "%generatorName%" ..

echo "Building the JCEF Java classes..."
cd c:\projects\journey\jcef\tools\
call compile.bat %buildType%