import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Iterator;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class NewsRecommendation  {

	public static final String OUTPUT_PATH_OF_TERM = "./";
	public static final String OUTPUT_PATH_OF_OUT = "./";
	public static final String OUTPUT_PATH_OF_MOR = "./morphol/";
	public static final String INPUT_PATH_OF_PAST = "./past/";
	public static final String INPUT_PATH_OF_NEW = "./new/";

	private String favoriteFile = null;
	private int numOfTerms = 0;

	private HashMap<String, Integer> favTerms = null;
	private HashMap<String, ArrayList<String>> dictionary = null;
	private ArrayList<String> morFileList = null;
	private ArrayList<TfIdf> scores = null;


	public NewsRecommendation(){
		// default constructor
	}

	public NewsRecommendation(String favoriteFile){
		this.favoriteFile = favoriteFile;
	}

	public void setFavoriteFile(String favoriteFile){
		this.favoriteFile = favoriteFile;
	}

	public void run(){
		readFavorite();
		readNew();
		calcTfIdf();
		output();
	}

	private void readFavorite(){

		if( this.favoriteFile == null ){
			System.err.println("[ERROR] : <favorite_article_list_file> is not set to variable in class.");
			System.exit(-1);
		}

		favTerms = new HashMap<String, Integer>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(favoriteFile));
		} catch (FileNotFoundException e) {
			System.err.println("[ERROR] : <favorite_article_list_file> of arguments is not found.");
			System.exit(-1);
		}
		String line = null;
		try{
			while( (line = br.readLine()) != null ){
				BufferedReader br_past = new BufferedReader(new FileReader(INPUT_PATH_OF_PAST+line));
				String pastLine, text = null;
				while( (pastLine = br_past.readLine()) != null ){
					if( pastLine.indexOf("#") != 0 )
						text += pastLine;
				}
				br_past.close();
				Tokenizer tokenizer = Tokenizer.builder().build();
				List<Token> tokens = tokenizer.tokenize(text);
				for (Token token : tokens) {
					if( token.getAllFeatures().indexOf("名詞") != -1 ){
						++numOfTerms;
						int cnt = 1;
						if( favTerms.containsKey(token.getSurfaceForm()) )
							cnt = favTerms.get(token.getSurfaceForm())+1;
						favTerms.put(token.getSurfaceForm(), cnt);
					}
				}
			}
			br.close();
		}catch(IOException e){
			System.err.println("[ERROR] : IOException while processing file -> "+line);
			System.exit(-1);
		}
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_PATH_OF_TERM+favoriteFile+".term"));
			bw.write(numOfTerms+"\n");
			for (Iterator<Entry<String, Integer>> it = favTerms.entrySet().iterator(); it.hasNext();) {
				Entry<String, Integer> entry = it.next();
				String key = (String) entry.getKey();
				Integer value = (Integer) entry.getValue();
				bw.write(key+"|"+value+"\n");
			}
			bw.close();
		}catch(IOException e){
			System.err.println("[ERROR] : IOException while output "+favoriteFile+".terms file.");
			System.exit(-1);
		}
	}

	private void readNew(){
		dictionary = new HashMap<String, ArrayList<String>>();
		morFileList = new ArrayList<String>();
		File dir = new File(INPUT_PATH_OF_NEW);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try{

				BufferedReader br_new = new BufferedReader(new FileReader(file));
				String newLine, text = null;
				while( (newLine = br_new.readLine()) != null ){
					if( newLine.indexOf("#") != 0 )
						text += newLine;
				}
				br_new.close();

				HashMap<String, Integer> terms = new HashMap<String, Integer>();
				int termCnt = 0;
				Tokenizer tokenizer = Tokenizer.builder().build();
				List<Token> tokens = tokenizer.tokenize(text);
				for (Token token : tokens) {
					if( token.getAllFeatures().indexOf("名詞") != -1 ){
						++termCnt;
						int cnt = 1;
						if( terms.containsKey(token.getSurfaceForm()) )
							cnt = terms.get(token.getSurfaceForm())+1;
						terms.put(token.getSurfaceForm(), cnt);
					}
				}

				String[] spPath = file.toString().split("/");
				morFileList.add(OUTPUT_PATH_OF_MOR+spPath[spPath.length-1]+".mor");
				BufferedWriter bw_new = new BufferedWriter(new FileWriter(OUTPUT_PATH_OF_MOR+spPath[spPath.length-1]+".mor"));
				bw_new.write(termCnt+"\n");
				for (Iterator<Entry<String, Integer>> it = terms.entrySet().iterator(); it.hasNext();) {
					Entry<String, Integer> entry = it.next();
					String key = (String) entry.getKey();
					Integer value = (Integer) entry.getValue();
					bw_new.write(key+"|"+value+"\n");
					ArrayList<String> list = dictionary.get(key);
					if( list == null )
						list = new ArrayList<String>();
					if( !list.contains(spPath[spPath.length-1]))
						list.add(spPath[spPath.length-1]);
					dictionary.put(key, list);
				}
				bw_new.close();
			}catch(Exception e){
				System.err.println("[ERROR] : IOException while processing file -> "+file);
				e.printStackTrace();
				System.exit(-1);
			}
		}
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_PATH_OF_MOR+"terms.txt"));
			for (Iterator<Entry<String, ArrayList<String>>> it = dictionary.entrySet().iterator(); it.hasNext();) {
				Entry<String, ArrayList<String>> entry = it.next();
				String key = (String) entry.getKey();
				ArrayList<String> value = (ArrayList<String>) entry.getValue();
				for( int j=0; j<value.size(); j++ )
					bw.write(key+"|"+value.get(j)+"\n");
			}
			bw.close();
		}catch(IOException e){
			System.err.println("[ERROR] : IOException while output terms.txt file.");
			System.exit(-1);
		}
	}

	private void calcTfIdf(){
		scores = new ArrayList<TfIdf>();
		for( int i=0; i<morFileList.size(); i++ ){
			String file = morFileList.get(i);
			String[] spPath = file.split("/");
			String fileName = spPath[spPath.length-1].replace(".mor","");
			try{
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				BufferedReader br = new BufferedReader(new FileReader(file));
				int numTerms = Integer.parseInt(br.readLine());
				String line = null;
				while( (line = br.readLine()) != null ){
					String[] spLine = line.split("\\|");
					map.put(spLine[0], Integer.parseInt(spLine[spLine.length-1]));
				}
				br.close();
				double tfIdf = 0.0;
				for (Iterator<Entry<String, Integer>> it = favTerms.entrySet().iterator(); it.hasNext();) {
					Entry<String, Integer> entry = it.next();
					String key = (String) entry.getKey();
					Integer value = (Integer) entry.getValue();
					if( map.containsKey(key) ){
						/* //debug
						System.out.println(value);
						System.out.println(numOfTerms);
						System.out.println(map.get(key));
						System.out.println(numTerms);
						System.out.println(morFileList.size());
						System.out.println(dictionary.get(key).size());
						System.out.println("====================");		
						*/
						tfIdf += ((double)map.get(key)/(double)numTerms)*Math.log10(morFileList.size()/dictionary.get(key).size());
					}
				}
				scores.add(new TfIdf(fileName, tfIdf));
			}catch( Exception e ){
				System.err.println("[ERROR] : Exception while processing file => "+file);
				e.printStackTrace();
				System.exit(-1);
			}
		}
		Collections.sort(scores, new Comparator<Object>(){
				public int compare(Object o1, Object o2){
				return (-1)*(int) (((((TfIdf)o1).getScore()-((TfIdf)o2).getScore()))*1e+7);
				}
				});
	}

	private void output(){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_PATH_OF_OUT+favoriteFile+".out"));
			for( int i=0; i<scores.size(); i++ ){
				bw.write(scores.get(i).getScore()+"\t"+scores.get(i).getTitle()+"\n");
			}
			bw.close();
		}catch( IOException e ){
			System.err.println("[ERROR] : IOException while outputting results.");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void main(String[] args) {

		if( args.length < 1 ){
			System.out.println("USAGE : java NewRecommendation <favorite_article_list_file>");
			System.exit(-1);
		}

		NewsRecommendation nr = new NewsRecommendation(args[0]);
		System.out.println("[Info] Favorite List File : "+args[0]);
		nr.run();
		System.out.println("[Finish] Complete News Recommendation!");

	}
}

class TfIdf {

	private String title;
	private double score;

	public TfIdf(String title, double score){
		this.title = title;
		this.score = score;
	}

	public String getTitle(){
		return this.title;
	}

	public double getScore(){
		return this.score;
	}

}
