import Utils.ApplicationProperties;

import java.util.logging.Logger;

/**
 * Created by aman on 9/27/16.
 */
public class PLARanker {
    private static Logger logger = Logger.getLogger(PLARanker.class.getName());

    static {
        try {
            ApplicationProperties.loadProperties(PLARanker.class.getResourceAsStream("/application.properties"), true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        RankerTrainer rankerTrainer= new RankerTrainer();
//        rankerTrainer.prepareData();
        //rankerTrainer.trainModel();


    }
}
