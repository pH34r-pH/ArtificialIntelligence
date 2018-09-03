param([String]$file, [String]$maze)
javac -cp ".;lib/PacSimLib.jar" ($file+".java")
jar cfm ($file+".jar") MANIFEST.MF *.class *.java
java -jar ($file+".jar") $maze
jar -tvf ($file+".jar")