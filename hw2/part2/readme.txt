run 'brew install hadoop' to get commands and src files in /usr/local/Cellar

copied a bunch of jar files from /usr/local/Cellar to /alljars/ to make it easier to set classpath

followed this:
https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html

compile issue solved from this:
http://stackoverflow.com/questions/15188042/where-are-hadoop-jar-files-in-hadoop-2



WORD COUNT JOB:

export CLASSPATH=".:alljars/*"
javac TrecWordCount.java
jar cf wc.jar TrecWordCount*.class
hadoop jar wc.jar TrecWordCount input output

notes:
/input/ should contain lines-trec.txt
/output will be created


INVERTED INDEX JOB:

mkdir input2
python parse.py (assuming you have lines-trec.txt inside /input directory)

export CLASSPATH=".:alljars/*"
javac TrecInvertedIndex.java
jar cf iv.jar TrecInvertedIndex*.class
hadoop jar iv.jar TrecInvertedIndex input2 output2

notes:
program will run out of heap space, so I included an input3 directory which only has 10 documents
