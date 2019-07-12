package com.example.husseinjehadalhroub.englishwords.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.husseinjehadalhroub.englishwords.MyTools.Config;
import com.example.husseinjehadalhroub.englishwords.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ReadActivity extends AppCompatActivity {
    ArrayAdapter arrayAdapter;
    TextView wordEnglish;
    TextView wordArabic;
    TextView def;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        init();


        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Config.CLASS_NAME);


        parseQuery.fromLocalDatastore();
        parseQuery.whereEqualTo(Config.WORD_ID, getIntent().getStringExtra(Config.INTENT_WORD_ID)).
                getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            String word = object.getString(Config.WORD);
                            String arabicM = object.getString(Config.WORD_ARABIC);
                            String definition = object.getString(Config.WORD_DEF);
                            ArrayList<String> examples = (ArrayList<String>) object.get(Config.WORD_EXAMPLES);

                            wordEnglish.setText(word);
                            wordArabic.setText(arabicM);
                            def.setText(definition);
                            ListView listView = findViewById(R.id.listView);
                            arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, examples);
                            listView.setAdapter(arrayAdapter);


                        }
                    }
                });

    }

    private void init() {

        wordEnglish = findViewById(R.id.wordEnglish);
        wordArabic = findViewById(R.id.wordArabic);
        def = findViewById(R.id.def);

    }





}
