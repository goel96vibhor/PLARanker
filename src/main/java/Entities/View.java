package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman on 10/11/16.
 */
public class View {
    private String publisherUrl;
    private String query;
    private String viewId;
    private List<Product> ads;

    public View(String viewId, List<Product> ads) {
        this.viewId = viewId;
        this.ads = ads;
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
}
