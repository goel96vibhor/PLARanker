package ProductPreprocess;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
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

public class IDFCalculator implements Externalizable{

    public static int productCount;
    public static HashMap<String, Double> titleIDFs ;
    public static HashMap<String, Double> attributeIDFs ;
    public static HashMap<String, Double> descriptionIDFs ;
    public static HashMap<String, Double> wholeDocIDFs;
    public static HashSet<Long> productSet;
    private static Logger logger = Logger.getLogger(IDFCalculator.class.getName());
    public static Double avgTitleLength=0.0;
    public static Double avgAttributeLength=0.0;
    public static Double avgDescriptionLength=0.0;
    public static Double avgwholeDocLength=0.0;


    static
    {
        titleIDFs= new HashMap<String, Double>();
        attributeIDFs= new HashMap<String, Double>();
        descriptionIDFs= new HashMap<String, Double>();
        wholeDocIDFs=new HashMap<String, Double>();
        productCount=0;
        productSet = new HashSet<Long>();
    }

    public static void calculateIdfs() {
        List<String> verticals = Arrays.asList(ApplicationProperties.getProperty("VERTICALS").split("~"));
        int providerId = Integer.parseInt(ApplicationProperties.getProperty("PROVIDER_ID"));
        BlockingQueue<Product> productQueue = new ArrayBlockingQueue<Product>(100000);
        new Thread(new ProductProducer(productQueue, verticals, providerId)).start();
        int count=0;
        try {
            while (true) {
                Product product = productQueue.take();
//                System.out.print(product.getTitle()+" ");
//                System.out.println(product.getAd_id());
                count++;
                System.out.println(count);
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
        avgAttributeLength=avgDescriptionLength=avgTitleLength=avgwholeDocLength=0.0;
        for(String key : titleIDFs.keySet())
        {
                titleIDFs.put(key, productCount / titleIDFs.get(key));
                avgTitleLength+=1.0/titleIDFs.get(key);
        }
        for(String key : attributeIDFs.keySet())
        {
            attributeIDFs.put(key, productCount / attributeIDFs.get(key));
            avgAttributeLength+=1.0/attributeIDFs.get(key);
        }

        for(String key : descriptionIDFs.keySet())
        {
            descriptionIDFs.put(key, productCount / descriptionIDFs.get(key));
            avgDescriptionLength+=1.0/descriptionIDFs.get(key);
        }
        for(String key : wholeDocIDFs.keySet())
        {
            wholeDocIDFs.put(key, productCount / wholeDocIDFs.get(key));
            avgwholeDocLength+=1.0/wholeDocIDFs.get(key);
        }
    }

    public static void updateIdf(Product product)
    {
        if(!productSet.contains(product.getAd_id())) {
            productSet.add(product.getAd_id());
            updateTitleIdf(product);
            updateAttributeIdf(product);
            updateDescriptionIdf(product);
            productCount+=1;
            //System.out.println(productCount);
//            logger.info("updated idfs for product with ad_id: "+product.getAd_id());
//            logger.info("added product with ad_id: "+product.getAd_id()+" to product hash.");
        }
    }

    public static void updateTitleIdf(Product product)
    {
        String title=product.getTitle();
        List<String> titleWords= WordRetrieval.retrieveProcessedWords(title);
        logger.info("retrieved processed title words for product with id: "+product.getAd_id());
        for(String word: titleWords)
        {
            if(!titleIDFs.containsKey(word))
                titleIDFs.put(word,(double)(0));
            if(!wholeDocIDFs.containsKey(word))
                wholeDocIDFs.put(word,(double)(0));
            wholeDocIDFs.put(word,wholeDocIDFs.get(word)+1.0);
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
            if(!wholeDocIDFs.containsKey(word))
                wholeDocIDFs.put(word,(double)(0));
            wholeDocIDFs.put(word,wholeDocIDFs.get(word)+1.0);
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
            if(!wholeDocIDFs.containsKey(word))
                wholeDocIDFs.put(word,(double)(0));
            wholeDocIDFs.put(word,wholeDocIDFs.get(word)+1.0);
        }
    }

    public static HashMap<String, Double> getDocTypeIdfs(int docType)
    {
        HashMap<String, Double> docTypeIdfs= null;
        if(docType==0)docTypeIdfs= IDFCalculator.titleIDFs;
        else if (docType==1)docTypeIdfs=IDFCalculator.attributeIDFs;
        else if (docType==2)docTypeIdfs=IDFCalculator.descriptionIDFs;
        else docTypeIdfs=IDFCalculator.wholeDocIDFs;
        return docTypeIdfs;
    }

    public static Double getavgDocTypeLength(int docType)
    {
        if(docType==0)return IDFCalculator.avgTitleLength;
        else if (docType==1)return IDFCalculator.avgAttributeLength;
        else if (docType==2)return IDFCalculator.avgDescriptionLength;
        else return IDFCalculator.avgwholeDocLength;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(productCount);
        out.writeObject(titleIDFs);
        out.writeObject(attributeIDFs);
        out.writeObject(descriptionIDFs);
        out.writeObject(wholeDocIDFs);
        out.writeObject(productSet);
        out.writeDouble(avgTitleLength);
        out.writeDouble(avgAttributeLength);
        out.writeDouble(avgDescriptionLength);
        out.writeDouble(avgwholeDocLength);
    }

    public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
        productCount=in.readInt();
        titleIDFs=(HashMap<String ,Double>)in.readObject();
        attributeIDFs=(HashMap<String ,Double>)in.readObject();
        descriptionIDFs=(HashMap<String ,Double>)in.readObject();
        wholeDocIDFs=(HashMap<String ,Double>)in.readObject();
        productSet=(HashSet<Long>)in.readObject();
        avgTitleLength=in.readDouble();
        avgAttributeLength=in.readDouble();
        avgDescriptionLength=in.readDouble();
        avgwholeDocLength=in.readDouble();
    }

}