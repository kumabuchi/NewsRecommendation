import java.io.*;
import java.util.Arrays;
import java.util.List;

public class NewsExtractor  {

    public static void main(String[] args) {

        try{
            BufferedReader br = new BufferedReader(new FileReader("article.raw"));
            BufferedWriter bw = null;
	    int contentsFlag = 0;
	    int titleFlag = 0;
	    int counter = 0;
            String line;
            while( (line = br.readLine()) != null ){
		line = line.trim();
		line = line.replaceAll("　","");
		if( contentsFlag == 0 && line.indexOf("Full Text Rss") != -1 )
			contentsFlag = 1;
		else if( contentsFlag == 1 && line.indexOf("&copy; fullrss.net 2012 All Right Reserved") != -1 ){
			contentsFlag = 0;
			titleFlag = 0;
		}else if( contentsFlag == 1 && titleFlag == 0 && !line.equals("") ){
			++counter;
			bw = new BufferedWriter(new FileWriter("new/"+line.replace(" ","")));
			titleFlag = 1;	
                }else if( contentsFlag == 1 && titleFlag == 1 && line.indexOf("http") == 0 ) 
			bw.write("#"+line+"\n");
		else if( line.indexOf("fullrss.net") != -1 ){
			if( bw != null )
				bw.close();
			titleFlag = 0;
		}else if( contentsFlag == 1 && titleFlag == 1 && !line.equals("") )
			bw.write(line+"\n");
            }
            br.close();
	    //System.out.println("news counter : "+counter);
        }catch( Exception e ){
	    e.printStackTrace();
            return;
        }
    }
}
