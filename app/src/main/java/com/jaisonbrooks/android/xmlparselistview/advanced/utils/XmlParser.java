package com.jaisonbrooks.android.xmlparselistview.advanced.utils;

import android.content.Context;
import android.util.Log;

import com.jaisonbrooks.android.xmlparselistview.advanced.model.DataFeed;
import com.jaisonbrooks.android.xmlparselistview.advanced.model.Titles;

import org.apache.http.ParseException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jbrooks on 7/25/2014
 */

public class XmlParser {

    private XmlPullParserFactory factory;
    private XmlPullParser parser;

    private ArrayList<DataFeed> feedList;
    public ArrayList<Titles> titlesList;
    private DataFeed feed;
    private Titles feed_titles;

    private String tagName;
    private Context context;

    public static final String ITEM = "item";
    public static final String CHANNEL = "items";
    public static final String TITLE = "title";
    public static final String SUBTITLE = "subtitle";
    public static final String DESCRIPTION = "description";
    public static final String ID = "id";

    public static final String TITLES= "titles";

    public String title = "";
    public String subtitle = "";
    public String description = "";
    public int id = 0;

    public XmlParser(Context context) {
        this.context = context;
    }

    public ArrayList<DataFeed> parse() {
        try {
            int count = 0;
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            String URL = "http://greenbot.org/jaisonbrooks/jones.xml";
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int response = connection.getResponseCode();
            Log.d("debug", "The response is: " + response);
            InputStream is = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            parser.setInput(inputStreamReader);

            int eventType = parser.getEventType();
            boolean done = false;
            feedList = new ArrayList<DataFeed>();
            titlesList = new ArrayList<Titles>();
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:

                        if (tagName.equals(ITEM)) {
                            feed = new DataFeed();
                            id = Integer.parseInt(parser.getAttributeValue(null, ID));
                        }
                        if (tagName.equals("titles")) {
                            feed_titles = new Titles();
                            if (tagName.equals(TITLE)) {
                                feed_titles.setTitle(parser.nextText().toString());
                            }
                            if (tagName.equals(SUBTITLE)) {
                                feed_titles.setSubtitle(parser.nextText().toString());
                            }
                            if (tagName.equals(DESCRIPTION)) {
                                feed_titles.setDescription(parser.nextText().toString());
                            }
                        }

                        if (tagName.equals(TITLE)) {
                            title = parser.nextText().toString();
                        }
                        if (tagName.equals(SUBTITLE)) {
                            subtitle = parser.nextText().toString();
                        }
                        if (tagName.equals(DESCRIPTION)) {
                            description = parser.nextText().toString();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equals(CHANNEL)) {
                            done = true;
                        } else if (tagName.equals(ITEM)) {
                            feed = new DataFeed(title, subtitle, description, id);
                            feedList.add(feed);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    return feedList;
    }

    public ArrayList<Titles> getTitlesList() {
        return titlesList;
    }
}
