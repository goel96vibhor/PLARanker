package Utils;

import Entities.URLBean;
import org.json.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import ProductPreprocess.WordRetrieval;

/**
 * Created by vibhor.go on 01/05/16.
 */

public class URLDetailFetcher
{
    private final static int CURL_CONNECT_TIMEOUT_MS = 2000;
    private final static  int CURL_READ_TIMEOUT_MS = 2000;
    private final static String xtractorApiUrl="http://xtractor.reports.mn/api?a=unique&url=";
    public static String getUrlExtractedJson(String publisherUrl)
    {
        publisherUrl = cleanSourceUrl(xtractorApiUrl+publisherUrl) ;
        InputStream inputStream = null;
        try {
            URL url = new URL(publisherUrl);
            long start = System.currentTimeMillis();
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(CURL_CONNECT_TIMEOUT_MS);
            httpURLConnection.setReadTimeout(CURL_READ_TIMEOUT_MS);
//            OutputStream os = httpURLConnection.getOutputStream();
//            os.write(jsonData.getBytes());
//            os.flush();
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

    public static String getTitlefromJson(JSONObject urlJSON)
    {
        String title=null;
        String property=null;
        JSONArray metaTagsArray= urlJSON.getJSONArray("meta_tags");
        JSONObject metaTagJson;
        for (int i = 0; i < metaTagsArray.length(); i++)
        {
            metaTagJson=metaTagsArray.getJSONObject(i);
            if(metaTagJson.has("property"))
            {
                property= metaTagJson.getString("property");
                if(property.contains("og:title"))
                    title=metaTagJson.getString("content");

            }
            else if(metaTagJson.has("name"))
            {
                property= metaTagJson.getString("name");
                if(property.contains("title"))
                    title=metaTagJson.getString("content");

            }

        }
        if(title!=null)
        {
            JSONArray uniqueJsonArray= urlJSON.getJSONArray("unique");
            JSONObject uniqueTagJson;
            for (int i = 0; i < uniqueJsonArray.length(); i++)
            {
                uniqueTagJson= uniqueJsonArray.getJSONObject(i);
                if(uniqueTagJson.has("tag")&&uniqueTagJson.getString("tag").contains("title"))
                {
                    title=uniqueTagJson.getString("text");
                    break;
                }
            }
        }
        if(title!=null)formatText(title);
        return title;
    }

    public static String getHeadingsfromJson(JSONObject urlJSON)
    {
        String property=null;
        StringBuilder headings= new StringBuilder();
        JSONArray uniqueJsonArray= urlJSON.getJSONArray("unique");
        JSONObject uniqueTagJson;
        for (int i = 0; i < uniqueJsonArray.length(); i++)
        {
            uniqueTagJson= uniqueJsonArray.getJSONObject(i);
            if(uniqueTagJson.has("tag")&&uniqueTagJson.getString("tag").matches("h[0-6]"))
            {
                headings.append(formatText(uniqueTagJson.getString("text")));
                headings.append(" ");
            }
        }
        return headings.toString();
    }

    public static String getContentfromJson(JSONObject urlJSON)
    {
        StringBuilder content= new StringBuilder();
//        if(urlJSON.getString("snacktory_output").matches(".*[a-zA-Z]+.*"))
//        {
//            return urlJSON.getString("snacktory_output");
//        }

        {

            JSONArray uniqueJsonArray= urlJSON.getJSONArray("unique");
            JSONObject uniqueTagJson;
            for (int i = 0; i < uniqueJsonArray.length(); i++)
            {
                uniqueTagJson= uniqueJsonArray.getJSONObject(i);
                if(uniqueTagJson.has("tag")&&uniqueTagJson.has("text")&&!uniqueTagJson.getString("tag").contains("title"))
                {
                    String text= formatText(uniqueTagJson.getString("text"));
                    content.append(text);
                    content.append(" ");
                }
            }
        }
        return content.toString();
    }

    public static String formatText(String text)
    {
        return text.replaceAll("&quot;","\"")
            .replaceAll("&amp;","&")
            .replaceAll("&#8217;","'")
            .replaceAll("&rsquo;","’");
    }

    public static URLBean getURLBean(String publisherUrl)
    {
        URLBean urlBean= new URLBean();
        String jsonOutput= getUrlExtractedJson(publisherUrl);
        JSONObject urlJSON = new JSONObject(jsonOutput);
        urlBean.setTitle(getTitlefromJson(urlJSON));
        urlBean.setHeading(getHeadingsfromJson(urlJSON));
        urlBean.setContent(getContentfromJson(urlJSON));
        return urlBean;
    }

    public static void main(String args[])
    {
        URLBean urlBean= getURLBean("http://www.healthyandnaturalworld.com/how-long-does-food-poisoning-last");
        System.out.println(WordRetrieval.retrieveProcessedWords(urlBean.getTitle()).toString());
        System.out.println(WordRetrieval.retrieveProcessedWords(urlBean.getHeading()).toString());
        System.out.println(WordRetrieval.retrieveProcessedWords(urlBean.getContent()).toString());
    }
}
