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
import Entities.URLBean;
import Entities.View;
import Utils.ApplicationProperties;
import org.apache.log4j.Logger;

import javax.print.attribute.IntegerSyntax;

/**
        * Created by vibhor.go on 10/14/16.
*/

public class IDFCalculator implements Externalizable{

    private static int documentCount;
    private static HashMap<String, Double> titleIDFs ;
    private static HashMap<String, Double> wholeDocIDFs;
    private static HashSet<Long> productSet;
    private static HashSet<String> urlSet;
    private static Logger logger = Logger.getLogger(IDFCalculator.class.getName());
    private static Double avgTitleLength=0.0;
    private static Double avgwholeDocLength=0.0;


    static
    {
        titleIDFs= new HashMap<String, Double>();
        wholeDocIDFs=new HashMap<String, Double>();
        documentCount=0;
        productSet = new HashSet<Long>();
        urlSet= new HashSet<String>();
    }

    public static int getDocumentCount() {
        return documentCount;
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
        System.out.println("documentCount" + documentCount);
        updateAll();
    }

    public static void updateAll() {
        avgTitleLength=avgwholeDocLength=0.0;
        for(String key : titleIDFs.keySet())
        {
                titleIDFs.put(key, documentCount / titleIDFs.get(key));
                avgTitleLength+=1.0/titleIDFs.get(key);
        }
        for(String key : wholeDocIDFs.keySet())
        {
            wholeDocIDFs.put(key, documentCount / wholeDocIDFs.get(key));
            avgwholeDocLength+=1.0/wholeDocIDFs.get(key);
        }
    }

    public static void updateIdftoCount()
    {
        for(String key : titleIDFs.keySet())
        {
            titleIDFs.put(key,documentCount/titleIDFs.get(key));
        }
        for(String key : wholeDocIDFs.keySet())
        {
            wholeDocIDFs.put(key,documentCount/wholeDocIDFs.get(key));
        }
    }

    public static void updateIdf(Product product)
    {
        if(!productSet.contains(product.getAd_id())) {
            productSet.add(product.getAd_id());
            updateTitleIdf(product.getTitle());
            updateWholeDocIdf(product.getTitle()+product.getAttributes()+product.getDescription());
            documentCount+=1;
            
            //System.out.println(documentCount);
            logger.info("updated idfs for product with ad_id: "+product.getAd_id());
//            logger.info("added product with ad_id: "+product.getAd_id()+" to product hash.");
        }
    }
    
    public static void updateIdf(URLBean urlBean)
    {
        if(!urlSet.contains(urlBean.getUrl())){
            urlSet.add(urlBean.getUrl());
            updateTitleIdf(urlBean.getTitle());
            updateWholeDocIdf(urlBean.getContent());
            documentCount+=1;
            logger.info("updated idfs for url: "+urlBean.getUrl());
        }
    }

    public static void updateTitleIdf(String title)
    {
        List<String> titleWords= WordRetrieval.retrieveProcessedWords(title);
        for(String word: titleWords)
        {
            if(!titleIDFs.containsKey(word))
                titleIDFs.put(word,(double)(0));
            titleIDFs.put(word,titleIDFs.get(word)+1.0);
        }
    }
    
    public static void updateWholeDocIdf(String content)
    {
        
        List<String> contentWords= WordRetrieval.retrieveProcessedWords(content);
        for(String word: contentWords)
        {
            if(!wholeDocIDFs.containsKey(word))
                wholeDocIDFs.put(word,(double)(0));
            wholeDocIDFs.put(word,wholeDocIDFs.get(word)+1.0); 
        }
    }
    
    public static HashMap<String, Double> getDocTypeIdfs(int docType)
    {
        HashMap<String, Double> docTypeIdfs= null;
        if(docType==0)docTypeIdfs= IDFCalculator.titleIDFs;
        else docTypeIdfs=IDFCalculator.wholeDocIDFs;
        return docTypeIdfs;
    }

    public static Double getavgDocTypeLength(int docType)
    {
        if(docType==0)return IDFCalculator.avgTitleLength;
        else return IDFCalculator.avgwholeDocLength;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(documentCount);
        out.writeObject(titleIDFs);
        out.writeObject(wholeDocIDFs);
        out.writeObject(productSet);
        out.writeObject(urlSet);
        out.writeDouble(avgTitleLength);
        out.writeDouble(avgwholeDocLength);
    }

    public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
        documentCount=in.readInt();
        titleIDFs=(HashMap<String ,Double>)in.readObject();
        wholeDocIDFs=(HashMap<String ,Double>)in.readObject();
        productSet=(HashSet<Long>)in.readObject();
        urlSet=(HashSet<String>)in.readObject();
        avgTitleLength=in.readDouble();
        avgwholeDocLength=in.readDouble();
    }

    public void updateIDFforViews(List <View> views)
    {
       updateIdftoCount();
       for(View view:views)
       {
          for(Product product:view.getAds())
          {
              updateIdf(product);
          }
           updateIdf(view.getUrlBean());
       }
       updateAll();
    }

    public static HashMap<String, Double> getTitleIDFs() {
        return titleIDFs;
    }

    public static HashMap<String, Double> getWholeDocIDFs() {
        return wholeDocIDFs;
    }

    public static HashSet<Long> getProductSet() {
        return productSet;
    }

    public static HashSet<String> getUrlSet() {
        return urlSet;
    }

    public static Double getAvgTitleLength() {
        return avgTitleLength;
    }

    public static Double getAvgwholeDocLength() {
        return avgwholeDocLength;
    }
}