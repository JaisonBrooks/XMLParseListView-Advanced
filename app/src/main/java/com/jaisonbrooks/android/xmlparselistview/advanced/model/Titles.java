package com.jaisonbrooks.android.xmlparselistview.advanced.model;

import java.io.Serializable;

/**
 * Created by jbrooks on 7/25/14.
 */
public class Titles implements Serializable {

    private String title;
    private String subtitle;
    private String description;

    public Titles() {
    }

    public Titles(String title, String subtitle, String description) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
    }
    public String getTitle() {
        return title;
    }
    public String getSubtitle() {
        return subtitle;
    }
    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}