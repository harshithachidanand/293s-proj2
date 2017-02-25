import java.io.IOException;
import java.util.Iterator;
import java.util.HashSet;
import java.lang.StringBuffer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TrecInvertedIndex {

	public static class InvertedIndexMapper extends Mapper<LongWritable, Text, Text, Text>{
		private Text word = new Text();
		private final static Text document = new Text();

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			// format is ID,TITLE,BODY separated by tabs
			String[] splitDoc = value.toString().split("\t");
			String docID = splitDoc[0];
			String docTitle = "";
			String docBody = "";

			if(splitDoc.length == 3){
				docTitle = splitDoc[1];
				docBody = splitDoc[2];
			}
			else if(splitDoc.length == 2){
				docTitle = splitDoc[1];
			}

			document.set(docID);
			
			String[] titleWords = docTitle.split(" ");
			for(String s : titleWords){
				word.set(s);
				context.write(word,document);
			}

			String[] bodyWords = docBody.split(" ");
			for(String s : bodyWords){
				word.set(s);
				context.write(word,document);
			}
		}
	}

	public static class InvertedIndexReducer extends Reducer<Text,Text,Text,Text> {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			StringBuffer buf = new StringBuffer();
			HashSet<String> unique = new HashSet<>();
			for (Text val : values) {
				String docID = val.toString();
				if(!unique.contains(docID)){
					unique.add(docID);
					if(buf.length() != 0){
						buf.append(", ");
					}
					buf.append(docID);
				}
			}
			Text docList = new Text();
			docList.set(buf.toString());
			context.write(key, docList);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "inverted index");
		job.setJarByClass(TrecInvertedIndex.class);
		job.setMapperClass(InvertedIndexMapper.class);
		job.setCombinerClass(InvertedIndexReducer.class);
		job.setReducerClass(InvertedIndexReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}