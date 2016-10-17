package ProductPreprocess;


import java.util.HashSet;
import Entities.Product;

/**
 * Created by vibhor.go on 10/14/16.
 */

public class ProductHash{

    public static HashSet<Long> productsList;

    public ProductHash()
    {
        productsList= new HashSet<Long>();
    }

    public static void addProducttoHash(Product product)
    {
        productsList.add(product.getAd_id());
    }

    public static boolean productinHash(Product product)
    {
        if(productsList.contains(product.getAd_id()))return true;
        else return false;
    }

}