package ProductPreprocess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import Entities.Product;
import Utils.ApplicationProperties;
import org.apache.log4j.Logger;

import javax.print.attribute.IntegerSyntax;

/**
        * Created by vibhor.go on 10/14/16.
*/

public class IDFCalculator{

    public static int productCount;
    public static HashMap<String, Double> titleIDFs ;
    public static HashMap<String, Double> attributeIDFs ;
    public static HashMap<String, Double> descriptionIDFs ;
    public static HashSet<Long> productSet;
    private static Logger logger = Logger.getLogger(IDFCalculator.class.getName());

    public IDFCalculator()
    {
        titleIDFs= new HashMap<String, Double>();
        attributeIDFs= new HashMap<String, Double>();
        descriptionIDFs= new HashMap<String, Double>();
        productCount=0;
        productSet = new HashSet<Long>();
    }

    public static void calculateIdfs() {
        List<String> verticals = Arrays.asList(ApplicationProperties.getProperty("VERTICALS").split("~"));
        int providerId = Integer.parseInt(ApplicationProperties.getProperty("PROVIDER_ID"));
        BlockingQueue<Product> productQueue = new ArrayBlockingQueue<Product>(10000);
        new Thread(new ProductProducer(productQueue, verticals, providerId)).start();
        try {
            while (true) {
                Product product = productQueue.take();
                System.out.println(product.getTitle());
                System.out.println(product.getAd_id());
                if(product.getAd_id()==Long.MIN_VALUE)
                    break;
                updateIdf(product);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("productCount" + productCount);
        updateAll();
    }

    public static void updateAll() {
        for(String key : titleIDFs.keySet())
            titleIDFs.put(key, productCount / titleIDFs.get(key));
        for(String key : attributeIDFs.keySet())
            attributeIDFs.put(key, productCount / attributeIDFs.get(key));
        for(String key : descriptionIDFs.keySet())
            descriptionIDFs.put(key, productCount / descriptionIDFs.get(key));
    }

    public static void updateIdf(Product product)
    {
        if(!productSet.contains(product.getAd_id())) {
            productSet.add(product.getAd_id());
            updateTitleIdf(product);
            updateAttributeIdf(product);
            updateDescriptionIdf(product);
            productCount+=1;
//            logger.info("updated idfs for product with ad_id: "+product.getAd_id());
//            logger.info("added product with ad_id: "+product.getAd_id()+" to product hash.");
        }
    }

    public static void updateTitleIdf(Product product)
    {
        String title=product.getTitle();
        List<String> titleWords= WordRetrieval.retrieveProcessedWords(title);
        logger.info("retrieved processed description words for product with id: "+product.getAd_id());
        for(String word: titleWords)
        {
            if(!titleIDFs.containsKey(word))
                titleIDFs.put(word,(double)(0));
            titleIDFs.put(word,titleIDFs.get(word)+1.0);
        }
    }

    public static void updateAttributeIdf(Product product)
    {
        String attributes=product.getAttributes();
        List<String> attributeWords= WordRetrieval.retrieveProcessedWords(attributes);
        logger.info("retrieved processed attribute words for product with id: "+product.getAd_id());
        for(String word: attributeWords)
        {
            if(!attributeIDFs.containsKey(word))
                attributeIDFs.put(word,(double)(0));
            attributeIDFs.put(word,attributeIDFs.get(word)+1.0);
        }
    }

    public static void updateDescriptionIdf(Product product)
    {
        String description=product.getDescription();
        List<String> descriptionWords= WordRetrieval.retrieveProcessedWords(description);
        logger.info("retrieved processed description words for product with id: "+product.getAd_id());
        for(String word: descriptionWords)
        {
            if(!descriptionIDFs.containsKey(word))
                descriptionIDFs.put(word,(double)(0));
            descriptionIDFs.put(word,descriptionIDFs.get(word)+1.0);
        }
    }
}