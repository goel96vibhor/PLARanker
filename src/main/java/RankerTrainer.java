import DataIO.DatabaseRepository;
import Entities.Product;
import Entities.RankList;
import Entities.View;
import NeuralNet.MRRScorer;
import NeuralNet.NDCGScorer;
import NeuralNet.RankNet;
import ProductPreprocess.IDFCalculator;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by vibhor.go on 11/18/16.
 */

public class RankerTrainer
{
    //List<RankList> viewRankList=null;
    public static Random random= new Random();
    List<RankList> trainSamples=null;
    List<RankList> validationSamples=null;
    List<RankList> testSamples=null;
    private static Logger logger = Logger.getLogger(RankerTrainer.class.getName());
    public void prepareData()
    {
        DatabaseRepository databaseRepository= new DatabaseRepository();
        System.out.println(DatabaseRepository.getProductDetails().size());
//        for(Product product:DatabaseRepository.getProductDetails().values())
//        {
//            IDFCalculator.updateIdf(product);
//        }
//        IDFCalculator.updateAll();
//        deserializeIDF();
//        IDFCalculator.calculateIdfs();
        deserializeIDF();
        System.out.println("Completed calculation of idfs");
        List<View> verticalViews= DatabaseRepository.getPageViews();
        System.out.println("Completed addition of views to map");
        trainSamples= new ArrayList<RankList>();
        validationSamples=new ArrayList<RankList>();
        testSamples= new ArrayList<RankList>();
        for(View view:verticalViews)
        {
            //if(!view.getViewId().equalsIgnoreCase("B28EE0B31C03D224A3ECE63F1BEBC096"))continue;
            view.calculateFeaturesforView();
            logger.info("calculated features for view: "+view.getViewId());
            RankList viewRankList= view.getRankListforView();
            int count=0;
            for(int targetValue:viewRankList.targetValues)count+=targetValue;
            if(count==0)continue;;
            if(random.nextDouble()<0.5)trainSamples.add(viewRankList);
            else if(random.nextDouble()<0.4)validationSamples.add(viewRankList);
            else testSamples.add(viewRankList);
            logger.info(viewRankList.targetValues);
            for(int i=0;i<viewRankList.listFeatures.size();i++)
            logger.info(viewRankList.listFeatures.get(i));
        }
        System.out.println("completed calculating features");
        System.out.println("trainsamples: "+trainSamples.size()+" validationSamples: "+validationSamples.size()+" testsamples: "+testSamples.size());
    }

    public void trainModel()
    {
        RankNet rankNet= new RankNet(trainSamples,validationSamples,testSamples,trainSamples.get(0).listFeatures.get(0).size(),1);
        rankNet.setScorer(new MRRScorer());
        rankNet.initialize();
        rankNet.train();
    }

    public void serializeIdf()
    {
        System.out.println(IDFCalculator.productCount);
        System.out.println(IDFCalculator.titleIDFs.size());
        System.out.println(IDFCalculator.attributeIDFs.size());
        System.out.println(IDFCalculator.descriptionIDFs.size());
        System.out.println(IDFCalculator.wholeDocIDFs.size());
        System.out.println(IDFCalculator.productSet.size());
        System.out.println(IDFCalculator.avgTitleLength);
        System.out.println(IDFCalculator.avgAttributeLength);
        System.out.println(IDFCalculator.avgDescriptionLength);
        System.out.println(IDFCalculator.avgwholeDocLength);

        try{
        FileOutputStream fos = new FileOutputStream("idf.map");

        ObjectOutputStream outputStream = new ObjectOutputStream(fos);
        outputStream.writeObject(new IDFCalculator());
        outputStream.close();
        }
        catch (IOException ioex)
        {
            ioex.printStackTrace();
        }
    }

    public void deserializeIDF()
    {
        try{
        FileInputStream fis = new FileInputStream("idf.map");
        ObjectInputStream inputStream = new ObjectInputStream(fis);
        IDFCalculator idfCalculator= new IDFCalculator();
        idfCalculator= (IDFCalculator) inputStream.readObject();
        inputStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        System.out.println(IDFCalculator.productCount);
        System.out.println(IDFCalculator.titleIDFs.size());
        System.out.println(IDFCalculator.attributeIDFs.size());
        System.out.println(IDFCalculator.descriptionIDFs.size());
        System.out.println(IDFCalculator.wholeDocIDFs.size());
        System.out.println(IDFCalculator.productSet.size());
        System.out.println(IDFCalculator.avgTitleLength);
        System.out.println(IDFCalculator.avgAttributeLength);
        System.out.println(IDFCalculator.avgDescriptionLength);
        System.out.println(IDFCalculator.avgwholeDocLength);
    }
}