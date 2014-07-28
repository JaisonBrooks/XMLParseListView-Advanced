package com.jaisonbrooks.android.xmlparselistview.advanced;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaisonbrooks.android.xmlparselistview.advanced.adapter.ListAdapter;
import com.jaisonbrooks.android.xmlparselistview.advanced.model.DataFeed;
import com.jaisonbrooks.android.xmlparselistview.advanced.utils.XmlParser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jbrooks on 7/25/2014
 */


public class ParsedListActivity extends Activity {

    Context context;
    XmlParser parser;
    ArrayList<DataFeed> mFeedList;
    ListView _lv;
    ListAdapter listAdapter;
    Set<DataFeed> data_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_parsed_list);
        setupListView();
        setupActionbar();

    }

    private void setupActionbar() {
        ActionBar ab = getActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
        }
    }

    public void setupListView() {
        _lv = (ListView) findViewById(R.id.listView);
        new DoRssFeedTask().execute();
    }

    public class DoRssFeedTask extends AsyncTask<String, Void, ArrayList<DataFeed>> {
        ProgressDialog prog;
        String jsonStr = null;
        Handler innerHandler;

        @Override
        protected void onPreExecute() {
            prog = new ProgressDialog(context);
            prog.setMessage("Loading....");
            prog.setCancelable(false);
            prog.show();
        }

        @Override
        protected ArrayList<DataFeed> doInBackground(String... params) {
            parser = new XmlParser(context);
            mFeedList = parser.parse();
            return mFeedList;
        }

        @Override
        protected void onPostExecute(final ArrayList<DataFeed> result) {
            prog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                 listAdapter = new ListAdapter(context, mFeedList);
                    int count = listAdapter.getCount();
                    if (count !=0 && listAdapter !=null) {
                        _lv.setAdapter(listAdapter);
                        setupListViewItemClick();
                    }
                }
            });
        }
    }

    private void setupListViewItemClick() {
        _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Hello " + listAdapter.getItem(i).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        _lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Removed " + listAdapter.getItem(i).getTitle(), Toast.LENGTH_SHORT).show();
                listAdapter.remove(listAdapter.getItem(i));
                listAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parsed_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            new DoRssFeedTask().execute();
            return true;
        }
        if (id == R.id.action_clear) {
            listAdapter.clear();
            return true;
        }
        if (id == R.id.action_sort) {
            listAdapter.sort(new Comparator<DataFeed>() {
                @Override
                public int compare(DataFeed dataFeed, DataFeed dataFeed2) {
                    return dataFeed.getTitle().compareTo(dataFeed2.getTitle());
                }
            });
        }

        if (id == R.id.action_remove_all_dupes) {

            // mFeedList // ArrayList<DataFeed>
            // listAdapter // ListAdapter
            // _lv // ListView

            ArrayList<DataFeed> list_non_dupes = new ArrayList<DataFeed>();
            for (int i=0; i < mFeedList.size(); i++) {
                DataFeed obj = mFeedList.get(i);
                String TITLE = obj.getTitle();
                int TITLE_ID = obj.getId();
                for (int j = 0; j < mFeedList.size(); j++) {
                    DataFeed obj_to_comparse = mFeedList.get(j);
                    String TITLE_TO_COMPARE = obj_to_comparse.getTitle();
                    int TITLE_TO_COMPARE_ID = obj_to_comparse.getId();

                    if (!TITLE.matches(TITLE_TO_COMPARE)) {
                        if (TITLE_ID != TITLE_TO_COMPARE_ID) {
                            if (!list_non_dupes.contains(obj_to_comparse)) {
                                list_non_dupes.add(obj_to_comparse);
                            }
                        }
                    } else if (TITLE_ID != TITLE_TO_COMPARE_ID) {
                        if (list_non_dupes.contains(obj_to_comparse)) {
                            list_non_dupes.remove(obj_to_comparse);
                        } else {
                            list_non_dupes.add(obj_to_comparse);
                        }
                    }
                }
            }

            if (!list_non_dupes.isEmpty()) {
                listAdapter = new ListAdapter(context, list_non_dupes);
                listAdapter.sort(new Comparator<DataFeed>() {
                    @Override
                    public int compare(DataFeed dataFeed, DataFeed dataFeed2) {
                        return dataFeed.getTitle().compareTo(dataFeed2.getTitle());
                    }
                });
                _lv.setAdapter(listAdapter);
                setupListViewItemClick();
            }


        }

         if (id == R.id.action_show_dupes) {

             // mFeedList // ArrayList<DataFeed>
             // listAdapter // ListAdapter
             // _lv // ListView

             ArrayList<DataFeed> list_dupes = new ArrayList<DataFeed>();

             for (int i=0; i < mFeedList.size(); i++) {
                 DataFeed obj = mFeedList.get(i);
                 String TITLE = obj.getTitle();
                 int TITLE_ID = obj.getId();
                 for (int j = 0; j < mFeedList.size(); j++) {
                     DataFeed obj_to_comparse = mFeedList.get(j);
                     String TITLE_TO_COMPARE = obj_to_comparse.getTitle();
                     int TITLE_TO_COMPARE_ID = obj_to_comparse.getId();

                     if (TITLE.matches(TITLE_TO_COMPARE)) {
                         if (TITLE_ID != TITLE_TO_COMPARE_ID) {
                             if (!list_dupes.contains(obj_to_comparse)) {
                                 list_dupes.add(obj_to_comparse);
                             }
                         }
                     }
                 }
             }

             if (!list_dupes.isEmpty()) {
                 listAdapter = new ListAdapter(context, list_dupes);
                 listAdapter.sort(new Comparator<DataFeed>() {
                     @Override
                     public int compare(DataFeed dataFeed, DataFeed dataFeed2) {
                         return dataFeed.getTitle().compareTo(dataFeed2.getTitle());
                     }
                 });
                 _lv.setAdapter(listAdapter);
                 setupListViewItemClick();
             }
         }

        return super.onOptionsItemSelected(item);
    }

}
