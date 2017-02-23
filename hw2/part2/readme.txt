run 'brew install hadoop' to get commands and src files in /usr/local/Cellar

copied a bunch of jar files from /usr/local/Cellar to /alljars/ to make it easier to set classpath

followed this:
https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html

compile issue solved from this:
http://stackoverflow.com/questions/15188042/where-are-hadoop-jar-files-in-hadoop-2



TO COMPILE:
export CLASSPATH=".:alljars/*"
javac TrecWordCount.java

TO RUN:
jar cf wc.jar TrecWordCount*.class
hadoop jar wc.jar TrecWordCount input output

/input/ should contain the lines-trec.txt (not included in git cuz too big)
/output will be created

*** as of right now, code compiles but throws runtime error (java.lang.NoClassDefFoundError: org/apache/hadoop/yarn/util/Apps) 