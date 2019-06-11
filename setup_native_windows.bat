SET PATH=%JAVA_HOME%;%PATH%

git clone https://bitbucket.org/chromiumembedded/java-cef.git jcef
call gradlew.bat checkoutJCEF

echo "Running cmake..."
cd jcef
mkdir jcef_build
cd jcef_build
cmake -G "%generatorName%" ..

echo "Building the JCEF Java classes..."
cd c:\projects\journey\jcef\tools\
call compile.bat %buildType%