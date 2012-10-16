import java.io.*;
import java.util.Arrays;
import java.util.List;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class MorphologicalAnalysis  {

    public static void main(String[] args) {

        if( args.length == 0 ){
            return;
        }

        try{
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            String line, text = null;
            while( (line = br.readLine()) != null ){
                if( line.indexOf("http") == -1 ) 
                    text += line;
            }
            br.close();

            Tokenizer tokenizer = Tokenizer.builder().build();
            List<Token> tokens = tokenizer.tokenize(text);

            String[] path = args[0].split("/");
            BufferedWriter bw = new BufferedWriter(new FileWriter("morphol/"+path[path.length-1]));
            for (Token token : tokens) {
                if( token.getAllFeatures().indexOf("名詞") != -1 ){
                    bw.write(token.getSurfaceForm()+"\n"); 
                }
                /*
                   System.out.println("==================================================");
                   System.out.println("allFeatures : " + token.getAllFeatures());
                   System.out.println("partOfSpeech : " + token.getPartOfSpeech());
                   System.out.println("position : " + token.getPosition());
                   System.out.println("reading : " + token.getReading());
                   System.out.println("surfaceFrom : " + token.getSurfaceForm());
                   System.out.println("allFeaturesArray : " + Arrays.asList(token.getAllFeaturesArray()));
                   System.out.println("辞書にある言葉? : " + token.isKnown());
                   System.out.println("未知語? : " + token.isUnknown());
                   System.out.println("ユーザ定義? : " + token.isUser());
                   */
            }
            bw.close();
        }catch( Exception e ){
            return;
        }
    }
}
