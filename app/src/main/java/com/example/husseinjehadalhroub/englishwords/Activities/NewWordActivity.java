package com.example.husseinjehadalhroub.englishwords.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.husseinjehadalhroub.englishwords.MyAdapters.MyAdapterWord;
import com.example.husseinjehadalhroub.englishwords.MyTools.Config;
import com.example.husseinjehadalhroub.englishwords.MyTools.StringTools;
import com.example.husseinjehadalhroub.englishwords.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewWordActivity extends AppCompatActivity {


    ArrayList<String> list;
    //    RecyclerView listView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        list = new ArrayList<>();
        list.add("");
//
//        //instantiate custom adapter
//        adapter = new MyCustomeArrayAdapter(list, this);
//
//        //handle listview and assign adapter
        recyclerView = findViewById(R.id.listView);
//
//        listView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapterWord(list);
        recyclerView.setAdapter(mAdapter);


    }

    public void addExample(View v) {

        list.add("");
        int location = list.size() - 1;
        mAdapter.notifyItemInserted(location);
        recyclerView.smoothScrollToPosition(location);


    }

    public void save(View v) {
        v.setEnabled(false);
        if(!isNetworkConnected())
        {
            Toast.makeText(this, "Can't save without Internet connection!", Toast.LENGTH_LONG).show();
            v.setEnabled(true);
            return;
        }
        String word = ((EditText) findViewById(R.id.addWord)).getText().toString();
        String searchWord = StringTools.convertToSearchExpression(word);
        String arabicM = ((EditText) findViewById(R.id.addWordMeaning)).getText().toString();
        String def = ((EditText) findViewById(R.id.addWordDef)).getText().toString();

        if (word.isEmpty() || arabicM.isEmpty() || def.isEmpty()) {
            Toast.makeText(this, "Please fill the first three inputs...", Toast.LENGTH_SHORT).show();
            v.setEnabled(true);
            return;
        }

        def = StringTools.formatDot(def);


        ArrayList<String> examples = new ArrayList<>();
        int length = list.size();
        for (int i = 0; i < length; i++) {
            String example = list.get(i);
            if (example.isEmpty())
                continue;
            example = StringTools.captilizeFirstLatter(example);
            examples.add(StringTools.formatDot(example));

        }

        ParseObject parseObject = new ParseObject(Config.CLASS_NAME);
        parseObject.put(Config.WORD, word);
        parseObject.put(Config.WORD_SEARCH, searchWord);
        parseObject.put(Config.WORD_ARABIC, arabicM);
        parseObject.put(Config.WORD_DEF, def);
        parseObject.put(Config.WORD_EXAMPLES, examples);
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });



//        parseObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null)
//                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//            }
//        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
