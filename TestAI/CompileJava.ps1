param([String]$file)
del *.jar
del *.class
javac -cp ".;lib/PacSimLib.jar" ($file+".java")
jar cfm ($file+".jar") MANIFEST.MF *.class *.java
jar -tvf ($file+".jar")