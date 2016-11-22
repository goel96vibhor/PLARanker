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

    public ProductProducer(BlockingQueue<Product> productQueue, List<String> verticals) {
        this.productQueue = productQueue;
        this.verticals = verticals;
    }

    public void run() {
        for (String vertical : verticals) {
            ResultSetConnectionPair resultSetConnectionPair = DatabaseRepository.getInstance().getProductsForVertical(vertical);
            try {
                ResultSet resultSet = resultSetConnectionPair.getResultSet();
                while (resultSet.next()) {
                    Product product = ResultSetParser.getNextProduct(resultSet);
                    productQueue.add(product);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                resultSetConnectionPair.closeBoth();
            }
        }
        Product product = new Product();
        product.setAd_id(Long.MIN_VALUE);
        productQueue.add(product);
    }
}
