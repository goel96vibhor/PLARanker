package ProductPreprocess;
import Utils.ApplicationProperties;
import java.util.HashSet;


/**
 * Created by vibhor.go on 10/14/16.
 */

public class StopWordHash{

    public static HashSet<String> stopWordHash;

    public static void readStopWords()
    {
         String stopWords= ApplicationProperties.getProperty("STOP_WORDS");
         String stopWordList[]= stopWords.split("~");
         stopWordHash= new HashSet<String>();
         for(String word:stopWordList)
         {
             stopWordHash.add(WordRetrieval.getStemmedWord(word));
         }
    }


}