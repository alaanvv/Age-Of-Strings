rm -rf bin
rm -rf bin_test

(cd src && javac -d ../bin Programa.java)

javac -d bin_test -cp "bin:junit.jar" src/persistencia/PersistenteTest.java

java -cp "junit.jar:bin:bin_test" org.junit.platform.console.ConsoleLauncher execute --scan-class-path
