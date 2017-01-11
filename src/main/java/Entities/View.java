package Entities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman on 10/11/16.
 */
public class View {
    private String publisherUrl=null;
    private String query=null;
    private String viewId;
    private List<Product> ads;
    private URLBean urlBean;

    public View(String viewId, List<Product> ads, String publisherUrl) {
        this.viewId = viewId;
        this.ads = ads;
        try {
            if (publisherUrl!=null)
                this.publisherUrl = URLDecoder.decode(publisherUrl.replaceAll("%(?![0-9a-fA-F]{2})", "%25"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // Can be safely ignored because UTF-8 is always supported
        }
        if (this.publisherUrl!=null)query=parseQuery(this.publisherUrl);
    }

    public String getQuery() {
        return query;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public List<Product> getAds() {
        return ads;
    }

    public void setAds(List<Product> ads) {
        this.ads = ads;
    }

    public void calculateFeaturesforView()
    {
        for(Product product:ads)
        {
            //System.out.println(query);
            product.calculateFeatures(query);
        }
    }

    public RankList getRankListforView()
    {
        return new RankList(ads,viewId);
    }

    public String parseQuery(String publisherUrl)
    {
        String result=null;
        int startIndex,endIndex;
        if(publisherUrl.contains("search/"))
        {
            startIndex=publisherUrl.indexOf("search/")+7;
            endIndex=publisherUrl.indexOf('?',startIndex);
            if(endIndex!=-1)result=publisherUrl.substring(startIndex,endIndex);
            else result=publisherUrl.substring(startIndex);
        }
        else if (publisherUrl.contains("index/"))
        {
            startIndex=publisherUrl.indexOf("index/")+6;
            endIndex=publisherUrl.indexOf('?',startIndex);
            if(endIndex!=-1)result=publisherUrl.substring(startIndex,endIndex);
            else result=publisherUrl.substring(startIndex);
        }
        else if(publisherUrl.contains("topic/20/"))
        {
            startIndex=publisherUrl.indexOf("topic/20/")+9;
            endIndex=publisherUrl.indexOf('?',startIndex);
            if(endIndex!=-1)result=publisherUrl.substring(startIndex,endIndex);
            else result=publisherUrl.substring(startIndex);
        }
        else if(publisherUrl.contains("cat/20/"))
        {
            startIndex=publisherUrl.indexOf("cat/20/")+7;
            endIndex=publisherUrl.indexOf('?',startIndex);
            if(endIndex!=-1)result=publisherUrl.substring(startIndex,endIndex);
            else result=publisherUrl.substring(startIndex);
        }
        if (result!=null && result.matches(".*[a-z].*")){
            return result;
        }
        return null;
    }

}
