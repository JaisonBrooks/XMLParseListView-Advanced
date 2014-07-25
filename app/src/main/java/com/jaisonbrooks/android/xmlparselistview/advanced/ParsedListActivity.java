package com.jaisonbrooks.android.xmlparselistview.advanced;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jaisonbrooks.android.xmlparselistview.R;
import com.jaisonbrooks.android.xmlparselistview.advanced.adapter.ListAdapter;
import com.jaisonbrooks.android.xmlparselistview.advanced.model.DataFeed;
import com.jaisonbrooks.android.xmlparselistview.advanced.utils.XmlParser;

import java.util.ArrayList;

/**
 * Created by jbrooks on 7/25/2014
 */


public class ParsedListActivity extends Activity {

    Context context;
    XmlParser parser;
    ArrayList<DataFeed> mFeedList;
    ListView _lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_parsed_list);
        setupListView();

    }
    public void setupListView() {
        _lv = (ListView) findViewById(R.id.listView);
        new DoRssFeedTask().execute();
        _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Hello " + mFeedList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class DoRssFeedTask extends AsyncTask<String, Void, ArrayList<DataFeed>> {
        ProgressDialog prog;
        String jsonStr = null;
        Handler innerHandler;

        @Override
        protected void onPreExecute() {
            prog = new ProgressDialog(context);
            prog.setMessage("Loading....");
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
                 ListAdapter listAdapter = new ListAdapter(context, result);
                _lv.setAdapter(listAdapter);
                }
            });
        }
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
