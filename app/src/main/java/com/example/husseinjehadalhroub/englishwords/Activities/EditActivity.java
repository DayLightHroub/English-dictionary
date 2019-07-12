package com.example.husseinjehadalhroub.englishwords.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.husseinjehadalhroub.englishwords.MyAdapters.MyAdapterWord;
import com.example.husseinjehadalhroub.englishwords.MyTools.Config;
import com.example.husseinjehadalhroub.englishwords.MyTools.StringTools;
import com.example.husseinjehadalhroub.englishwords.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditActivity extends AppCompatActivity {

    private String wordId;
    EditText textWord;
    EditText textArabic;
    EditText defininition;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        init();

        wordId = getIntent().getStringExtra(Config.INTENT_WORD_ID);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Config.CLASS_NAME);
        query.fromLocalDatastore().whereEqualTo(Config.WORD_ID, wordId).getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    textWord.setText(object.getString(Config.WORD));
                    textArabic.setText(object.getString(Config.WORD_ARABIC));
                    defininition.setText(object.getString(Config.WORD_DEF));
                    list = (ArrayList<String>) object.get(Config.WORD_EXAMPLES);
                    mAdapter = new MyAdapterWord(list);
                    recyclerView.setAdapter(mAdapter);
//                    int length = examplesArray.size();
//                    for (int i = 0; i < length; i++)
//                        list.add(new DataWord(examplesArray.get(i)));


                }
            }
        });

//        String word = ((EditText) (findViewById(R.id.editWord))).getText().toString();
//        String wordArabic = ((EditText) (findViewById(R.id.editArabic))).getText().toString();
//        String def = ((EditText) (findViewById(R.id.editDef))).getText().toString();

    }

    private void init() {

//        list = new ArrayList<>();
//        list.add("");
        recyclerView = findViewById(R.id.editListView);
//
//        listView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


//        mAdapter = new MyAdapterWord(list);
//        recyclerView.setAdapter(mAdapter);

        textWord = findViewById(R.id.editWord);
        textArabic = findViewById(R.id.editArabic);
        defininition = findViewById(R.id.editDef);
    }

    public void addExample(View v) {
        System.out.println("i am here");
        list.add("");
        int location = list.size() - 1;
        mAdapter.notifyItemInserted(location);
        recyclerView.smoothScrollToPosition(location);

    }

    public void saveEdit(View v) {
        v.setEnabled(false);
        if (!isNetworkConnected()) {
            Toast.makeText(this, "Can't save without Internet connection!", Toast.LENGTH_LONG).show();
            v.setEnabled(true);
            return;
        }
        final String word = textWord.getText().toString();
        final String searchWord = StringTools.convertToSearchExpression(word);
        final String arabicM = textArabic.getText().toString();
        String def = defininition.getText().toString();
        def = StringTools.formatDot(def);
        final ArrayList<String> examples = new ArrayList<>();
        int length = list.size();
        for (int i = 0; i < length; i++) {
            String example = list.get(i);
            if (example.isEmpty())
                continue;
            example = StringTools.captilizeFirstLatter(example);
            examples.add(StringTools.formatDot(example));

        }


        ParseQuery<ParseObject> query = ParseQuery.getQuery(Config.CLASS_NAME);

// Retrieve the object by id
        final String finalDef = def;
        query.fromLocalDatastore().getInBackground(wordId, new GetCallback<ParseObject>() {
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put(Config.WORD, word);
                    parseObject.put(Config.WORD_SEARCH, searchWord);
                    parseObject.put(Config.WORD_ARABIC, arabicM);
                    parseObject.put(Config.WORD_DEF, finalDef);
                    parseObject.put(Config.WORD_EXAMPLES, examples);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                    });

                }
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
