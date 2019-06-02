SET PATH=%JAVA_HOME%;%PATH%

git clone https://bitbucket.org/chromiumembedded/java-cef.git jcef
cd jcef
git checkout 045302f591e53057a011b4b1df1d26fbee15e35d

echo "Running cmake..."
mkdir jcef_build
cd jcef_build
cmake -G "%generatorName%" ..

echo "Building the JCEF Java classes..."
cd c:\projects\journey\jcef\tools\
call compile.bat %buildType%