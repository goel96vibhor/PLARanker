package ProductPreprocess;

import java.util.HashMap;
import java.util.List;

import Entities.Product;
import org.apache.log4j.Logger;

/**
        * Created by vibhor.go on 10/14/16.
*/

public class IDFCalculator{

    public static int productCount=0;
    public static HashMap<String, Double> titleIDFs ;
    public static HashMap<String, Double> attributeIDFs ;
    public static HashMap<String, Double> descriptionIDFs ;
    private static Logger logger = Logger.getLogger(IDFCalculator.class.getName());

    public IDFCalculator()
    {
        titleIDFs= new HashMap<String, Double>();
        attributeIDFs= new HashMap<String, Double>();
        descriptionIDFs= new HashMap<String, Double>();
        productCount=0;

    }

    public static void updateIdfs(List<Product> productList)
    {
        for(Product product:productList)
        {

            if(!ProductHash.productinHash(product)){
                updateTitleIdf(product);
                updateAttributeIdf(product);
                updateDescriptionIdf(product);
                logger.info("updated idfs for product with ad_id: "+product.getAd_id());
                ProductHash.addProducttoHash(product);
                productCount+=1;
                logger.info("added product with ad_id: "+product.getAd_id()+" to product hash.");

            }
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
            {
                titleIDFs.put(word,(double)(0));
                attributeIDFs.put(word,(double)(0));
                descriptionIDFs.put(word,(double)(0));
            }
            titleIDFs.put(word,(titleIDFs.get(word)*productCount+1.0)/(productCount+1.0));
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
            {
                titleIDFs.put(word,(double)(0));
                attributeIDFs.put(word,(double)(0));
                descriptionIDFs.put(word,(double)(0));
            }
            attributeIDFs.put(word,(attributeIDFs.get(word)*productCount+1.0)/(productCount+1.0));
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
            {
                titleIDFs.put(word,(double)(0));
                attributeIDFs.put(word,(double)(0));
                descriptionIDFs.put(word,(double)(0));
            }
            descriptionIDFs.put(word,(descriptionIDFs.get(word)*productCount+1.0)/(productCount+1.0));
        }
    }




}