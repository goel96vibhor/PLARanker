package Utils;

import DataIO.Database;
import DataIO.DatabaseManager;
import DataIO.ResultSetConnectionPair;
import Entities.Product;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aman on 11/24/16.
 */
public class ProductDetailFetcher {
    private final static int CURL_CONNECT_TIMEOUT_MS = 2000;
    private final static  int CURL_READ_TIMEOUT_MS = 2000;
    public static final String ESApiCall = "http://172.19.59.5:9877/items_ecn_alias/_search";

    static {
        try {
            ApplicationProperties.loadProperties(ProductDetailFetcher.class.getResourceAsStream("/application.properties"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static String getHttpUrlContent(String sourceUrl)
//    {
//        return getHttpUrlContent(sourceUrl, true);
//    }
//
//    public static String getHttpUrlContent(String sourceUrl , boolean timeout)
//    {
//        sourceUrl = cleanSourceUrl(sourceUrl) ;
//        InputStream inputStream = null;
//        try {
//            URL url = new URL(sourceUrl);
//            long start = System.currentTimeMillis();
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.setRequestMethod("GET");
//            /**
//             *If the call is not from the Live Thread , there is no need for timeout
//             *If it's the live thread then timeout decides if timeouts should be set
//             *If timeout parameter is not set in the call it is automatically taken to be true
//             */
//            httpURLConnection.setConnectTimeout(CURL_CONNECT_TIMEOUT_MS);
//            httpURLConnection.setReadTimeout(CURL_READ_TIMEOUT_MS);
//
//            inputStream = httpURLConnection.getInputStream();
//            if(inputStream == null)
//                throw new Exception("No input Stream Received");
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            return stringBuilder.toString();
//        }catch(Exception e) {
//
//            return null;
//        }finally{
//            try {
//                if (inputStream != null)
//                    inputStream.close();
//            }catch(IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }

    public static String postHttpUrlContent(String sourceUrl, String jsonData)
    {
        sourceUrl = cleanSourceUrl(sourceUrl) ;
        InputStream inputStream = null;
        try {
            URL url = new URL(sourceUrl);
            long start = System.currentTimeMillis();
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(CURL_CONNECT_TIMEOUT_MS);
            httpURLConnection.setReadTimeout(CURL_READ_TIMEOUT_MS);
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(jsonData.getBytes());
            os.flush();
            inputStream = httpURLConnection.getInputStream();
            if(inputStream == null)
                throw new Exception("No input Stream Received");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static String cleanSourceUrl(String sourceUrl)
    {
        return sourceUrl.replace(" ","%20");
    }

    private static ResultSetConnectionPair getProductIds() {
        Database database = DatabaseManager.getAdMaster();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = database.getPreparedStatement("select * from compare_online_stats_final_1(nolock)");
            return new ResultSetConnectionPair(preparedStatement.executeQuery(), database);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getQuery(List<Long> adIds) {
        JSONObject j[] = new JSONObject[adIds.size()];
        int i=0;
        for(Long adId : adIds) {
            j[i]= new JSONObject();

            JSONObject jAd = new JSONObject();
            jAd.put("ad_id", String.valueOf(adId));
            j[i].put("match", jAd);
            i++;
        }
        JSONObject jShould = new JSONObject();
        jShould.put("should",j);
        JSONObject jBool = new JSONObject();
        jBool.put("bool", jShould);
        JSONObject jQuery = new JSONObject();
        jQuery.put("query", jBool);
        jQuery.put("size", adIds.size());
        return jQuery.toString();
    }

    public static List<Long> getAdIds() {
        List<Long> adIds = new ArrayList<Long>();
        ResultSetConnectionPair resultSetConnectionPair = getProductIds();
        ResultSet resultSet = resultSetConnectionPair.getResultSet();
        try {
            while (resultSet.next()) {
                adIds.add(resultSet.getLong("article_id"));
            }
            System.out.println(adIds.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            resultSetConnectionPair.closeBoth();
        }
        return adIds;
    }

    public static void main(String[] args) {

        List<Long> adIds = getAdIds();

        List<String> esIndices = new ArrayList<String>();
        esIndices.add("items_ecn_20161125034002");
        esIndices.add("items_ecn_20161128121001");
        esIndices.add("items_ecn_20161123110323");
        esIndices.add("items_ecn_20161127095002");
        esIndices.add("items_ecn_20161124035001");

        List<Long> idsFound = new ArrayList<Long>();
        HashMap<Long, String> productMap = new HashMap<>();
        int total = 0;
        int scrollSize = 100;
        for(String index : esIndices) {
            String esApi = "http://172.19.59.5:9877/" + index + "/_search";
            for (int i = 0; i < adIds.size(); i += scrollSize) {
                String esQuery = getQuery(adIds.subList(i, Math.min(i + scrollSize, adIds.size())));
                String result = postHttpUrlContent(esApi, esQuery);
                if (result == null) {
                    System.out.println("Result NULL");
                    break;
                }
                JSONObject resultJson = new JSONObject(result);
                int hits = Integer.parseInt(resultJson.getJSONObject("hits").get("total").toString());
                System.out.println("Hits : " + hits);
                JSONArray productArray = resultJson.getJSONObject("hits").getJSONArray("hits");
                for(int j=0;j<productArray.length();j++) {
                    Long productId = Long.parseLong(productArray.getJSONObject(j).get("_id").toString());
                    idsFound.add(productId);
                    JSONObject productJson = productArray.getJSONObject(j).getJSONObject("_source");
                    JSONObject product = new JSONObject();
                    product.put("ad_id", productJson.get("ad_id"));
                    product.put("title", productJson.get("title"));
                    product.put("price", productJson.get("price"));
                    product.put("original_price", productJson.get("original_price"));
                    product.put("description", productJson.get("description"));
                    product.put("brand", productJson.get("brand"));
                    product.put("attribute_text", productJson.get("attribute_text"));
                    product.put("seller_rating", productJson.get("seller_rating"));
                    product.put("expected_rpm", productJson.get("expected_rpm"));
                    productMap.put(productId, productJson.toString());
                }
                total += hits;
            }
            adIds.removeAll(idsFound);
            System.out.println("Total Found: " + total);
            System.out.println("Remaining : " + adIds.size());
        }
        HashMap<Long,String> deserializedMap = null;
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("product.map"));
            outputStream.writeObject(productMap);
            outputStream.close();
//            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("product.map"));
//            deserializedMap = (HashMap<Long,String>) inputStream.readObject();
//            inputStream.close();
//            System.out.println("Deserialized Map Size : " + deserializedMap.size());
//            for(Long adId : deserializedMap.keySet()) {
//                JSONObject productJson = new JSONObject(deserializedMap.get(adId));
//                System.out.println(productJson.getLong("ad_id"));
//                System.out.println(productJson.getString("title"));
//                System.out.println(productJson.getDouble("original_price"));
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
