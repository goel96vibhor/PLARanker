package ProductPreprocess;

import java.util.HashMap;
import java.util.List;

import Entities.Product;
/**
        * Created by vibhor.go on 10/14/16.
*/

public class IDFCalculator{

    public static int productCount=0;
    public static HashMap<String, Double> titleIDFs ;
    public static HashMap<String, Double> attributeIDFs ;
    public static HashMap<String, Double> descriptionIDFs ;

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
                ProductHash.addProducttoHash(product);
                productCount+=1;
            }
        }
    }

    public static void updateTitleIdf(Product product)
    {
        String title=product.getTitle();
        List<String> titleWords= WordRetrieval.retrieveProcessedWords(title);
        for(String word: titleWords)
        {
            if(!titleIDFs.containsKey(word))
            {
                titleIDFs.put(word,(double)(0));
                attributeIDFs.put(word,(double)(0));
                descriptionIDFs.put(word,(double)(0));
            }
            titleIDFs.put(word,(titleIDFs.get(word)*productCount+1)/(productCount+1));
        }
    }

    public static void updateAttributeIdf(Product product)
    {
        String attributes=product.getAttributes();
        List<String> attributeWords= WordRetrieval.retrieveProcessedWords(attributes);
        for(String word: attributeWords)
        {
            if(!attributeIDFs.containsKey(word))
            {
                titleIDFs.put(word,(double)(0));
                attributeIDFs.put(word,(double)(0));
                descriptionIDFs.put(word,(double)(0));
            }
            attributeIDFs.put(word,(attributeIDFs.get(word)*productCount+1)/(productCount+1));
        }
    }

    public static void updateDescriptionIdf(Product product)
    {
        String description=product.getDescription();
        List<String> descriptionWords= WordRetrieval.retrieveProcessedWords(description);
        for(String word: descriptionWords)
        {
            if(!descriptionIDFs.containsKey(word))
            {
                titleIDFs.put(word,(double)(0));
                attributeIDFs.put(word,(double)(0));
                descriptionIDFs.put(word,(double)(0));
            }
            descriptionIDFs.put(word,(descriptionIDFs.get(word)*productCount+1)/(productCount+1));
        }
    }




}