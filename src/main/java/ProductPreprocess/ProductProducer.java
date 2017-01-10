package ProductPreprocess;

import DataIO.DatabaseRepository;
import DataIO.ResultSetConnectionPair;
import DataIO.ResultSetParser;
import Entities.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by aman on 11/17/16.
 */
public class ProductProducer implements Runnable {
    private BlockingQueue<Product> productQueue;
    private List<String> verticals;
    private int providerId;

    public ProductProducer(BlockingQueue<Product> productQueue, List<String> verticals, int providerId) {
        this.productQueue = productQueue;
        this.verticals = verticals;
        this.providerId = providerId;
    }

    public void run() {
        for (String vertical : verticals) {
            ResultSetConnectionPair resultSetConnectionPair = DatabaseRepository.getInstance().getProductsForVertical(vertical, providerId);
            try {
                ResultSet resultSet = resultSetConnectionPair.getResultSet();
                int count = 0;
                while (resultSet.next()) {
                    Product product = ResultSetParser.getNextProduct(resultSet);
                    count++;
                    System.out.println(count+ " count");
                    productQueue.add(product);

                   if(count==10000)
                   {
                       count=0;
                       try {
                           Thread.sleep(1000);
                       }
                       catch (InterruptedException iex)
                       {
                            iex.printStackTrace();
                       }
                   }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                System.out.println("closing rspair");
                resultSetConnectionPair.closeBoth();
                System.out.println("closed");
            }
        }
        Product product = new Product();
        product.setAd_id(Long.MIN_VALUE);
        productQueue.add(product);
    }
}
