package ProductPreprocess;

import java.util.ArrayList;
import java.util.List;
import Utils.Stemmer;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import java.io.StringReader;
/**
 * Created by vibhor.go on 10/14/16.
 */

public class WordRetrieval{
    public static Stemmer stemmer;
    private static Logger logger = Logger.getLogger(WordRetrieval.class.getName());

    static {
        stemmer= new Stemmer();
        StopWordHash.readStopWords();
    }

    public static List<String> retrieveProcessedWords(String text)
    {
        return getStemmedTokens(tokenize(text));
    }

    public static List<String> tokenize(String text)
    {
        List<String> tokens=new ArrayList<String>();
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        TokenStream tokenStream
                = analyzer.tokenStream("contents",
                new StringReader(text));
        CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);

        try{
        while(tokenStream.incrementToken()) {
           tokens.add(attr.toString());
        }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        //System.out.println(tokens);
        return tokens;
    }

    public static List<String> getStemmedTokens(List<String> tokens)
    {
        List<String> stemmedTokens= new ArrayList<String>();
        for(String token: tokens)
        {
            String stemmedToken=getStemmedWord(token);
            if(stemmedToken!=null&&stemmedToken!=""&&stemmedToken.matches(".*\\w.*")&&!StopWordHash.stopWordHash.contains(stemmedToken))stemmedTokens.add(stemmedToken);
        }
        return stemmedTokens;
    }

    public static String getStemmedWord(String token)
    {

        stemmer.add(token.toCharArray(),token.length());
        stemmer.stem();
        String stemmedToken= stemmer.toString();
        return stemmedToken;
    }

//    public static void main(String args[])
//    {
//
//        String text="It is a long established fact  TH5-hisT H-250 that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).";
//        List<String> tokens=tokenize(text);
//        List<String> stemmedTokens= new ArrayList<String>();
//        for(String token: tokens)
//        {
//
//            String u=getStemmedWord(token);
//            if(u!=null&&u!=""&&u.matches(".*\\w.*"))stemmedTokens.add(u);
//
//        }
//
//        //String  u = new String(stemmer.getResultBuffer(), 0, stemmer.getResultLength());
//        System.out.println(stemmedTokens);
//
//    }


}