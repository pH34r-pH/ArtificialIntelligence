param([String]$file)
javac -cp ".;lib/PacSimLib.jar" ($file+".java")
jar cfm ($file+".jar") MANIFEST.MF *.class *.java
jar -tvf ($file+".jar")