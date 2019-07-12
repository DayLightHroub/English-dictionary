package com.example.husseinjehadalhroub.englishwords.Activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.husseinjehadalhroub.englishwords.DataTypes.WordMeaning;
import com.example.husseinjehadalhroub.englishwords.MyAdapters.MyAdapterHome;
import com.example.husseinjehadalhroub.englishwords.MyTools.Config;
import com.example.husseinjehadalhroub.englishwords.MyTools.StringTools;
import com.example.husseinjehadalhroub.englishwords.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeActivity extends AppCompatActivity {


    ArrayList<WordMeaning> list;
    //    RecyclerView listView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private ArrayList<WordMeaning> listForSearch;
    MaterialSearchView searchView;
    private boolean isSearchEpty = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("mx568isOJGZ9pRElxyqm4D67TKQnEqapggRRHmkM")
                .clientKey("wVw3IKh83JZZoAXIrIgvpFguBy8mhkbXBW5KF7MO")
                .server("https://parseapi.back4app.com")
                .enableLocalDataStore()
                .build()
        );


        init();

        searchView = findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                resetRView();
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(final String newText) {

                if (!newText.isEmpty()) {
                    isSearchEpty = false;

                    if (StringTools.isEnglish(newText))
                        searchInEnglish(newText);
                    else
                        searchInArabic(newText);
                } else {
                    isSearchEpty = true;
                    resetRView();
                }
                return true;
            }
        });

        updateRecyclerView();

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateRecyclerView();
                pullToRefresh.setRefreshing(false);
            }
        });


    }

    private void searchInArabic(String newText) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Config.CLASS_NAME);
        parseQuery.whereContains(Config.WORD_ARABIC, newText);

        parseQuery.fromLocalDatastore();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && !isSearchEpty) {
                    listForSearch.clear();
                    for (ParseObject x : objects)
                        listForSearch.add(new WordMeaning(x.getString(Config.WORD), x.getString(Config.WORD_DEF), x.getObjectId()));
                    ((MyAdapterHome) mAdapter).updateData(listForSearch);
                    mAdapter.notifyDataSetChanged();
                } else
                    resetRView();


            }
        });
    }

    private void searchInEnglish(String newText) {
        String formattedText = StringTools.convertToSearchExpression(newText);
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Config.CLASS_NAME);
        parseQuery.whereContains(Config.WORD_SEARCH, formattedText);

        parseQuery.fromLocalDatastore();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && !isSearchEpty) {
                    listForSearch.clear();
                    for (ParseObject x : objects)
                        listForSearch.add(new WordMeaning(x.getString(Config.WORD), x.getString(Config.WORD_DEF), x.getObjectId()));
                    ((MyAdapterHome) mAdapter).updateData(listForSearch);
                    mAdapter.notifyDataSetChanged();
                } else
                    resetRView();


            }
        });
    }

    private void resetRView() {
        ((MyAdapterHome) mAdapter).updateData(list);
        mAdapter.notifyDataSetChanged();
    }

    private void init() {

        context = this;
        list = new ArrayList<>();
        listForSearch = new ArrayList<>();
        recyclerView = findViewById(R.id.recView);
//
//        listView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapterHome(list);


        recyclerView.setAdapter(mAdapter);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vocabulary");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

    }

    private void updateRecyclerView() {


        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Config.CLASS_NAME);

        if (isNetworkConnected()) {

            parseQuery.orderByAscending(Config.WORD).findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        ParseObject.unpinAllInBackground(Config.CLASS_NAME);
                        list.clear();
                        for (ParseObject x : objects)
                            list.add(new WordMeaning(x.getString(Config.WORD), x.getString(Config.WORD_DEF), x.getObjectId()));
                        mAdapter.notifyDataSetChanged();
                        ParseObject.pinAllInBackground(Config.CLASS_NAME, objects);
                    } else
                        System.out.println("filedaaaaaaaaaaaaaaa");


                }
            });
        } else {
            parseQuery.fromLocalDatastore().orderByAscending(Config.WORD).findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        list.clear();
                        for (ParseObject x : objects)
                            list.add(new WordMeaning(x.getString(Config.WORD), x.getString(Config.WORD_DEF), x.getObjectId()));
                        mAdapter.notifyDataSetChanged();

                    } else
                        System.out.println("filedaaaaaaaaaaaaaaa");
                }
            });
        }
    }


    public void addNewWord(View v) {
        Intent intent = new Intent(this, NewWordActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);

        searchView.setMenuItem(item);

        return true;
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(Config.INTENT_WORD_ID, list.get(item.getGroupId()).getWordId());
        startActivity(intent);
        return true;
    }
}
