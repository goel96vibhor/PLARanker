package Entities;

import Utils.URLDetailFetcher;

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
    private String viewId;
    private List<Product> ads;
    private URLBean urlBean;

    public View(String viewId, List<Product> ads, String publisherUrl) {
        this.viewId = viewId;
        this.ads = ads;
        this.publisherUrl = publisherUrl;
//        try {
//            if (publisherUrl!=null)
//                this.publisherUrl = URLDecoder.decode(publisherUrl.replaceAll("%(?![0-9a-fA-F]{2})", "%25"), "UTF-8");
//        } catch (UnsupportedEncodingException ex) {
//            // Can be safely ignored because UTF-8 is always supported
//        }
        this.urlBean = URLDetailFetcher.getURLBean(publisherUrl);
    }

    public URLBean getUrlBean() {
        return urlBean;
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
        urlBean.calculateTermVecs();
        for(Product product:ads)
        {
            //System.out.println(query);
            product.calculateFeatures(urlBean);
        }
    }

    public RankList getRankListforView()
    {
        return new RankList(ads,viewId);
    }



}
