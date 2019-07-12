package com.example.husseinjehadalhroub.englishwords.MyAdapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.husseinjehadalhroub.englishwords.Activities.ReadActivity;
import com.example.husseinjehadalhroub.englishwords.DataTypes.WordMeaning;
import com.example.husseinjehadalhroub.englishwords.MyTools.Config;
import com.example.husseinjehadalhroub.englishwords.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterHome extends RecyclerView.Adapter<MyAdapterHome.MyViewHolder> {
    private ArrayList<WordMeaning> list;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // each data item is just a string in this case
        public TextView word;
        public TextView def;

        public MyViewHolder(View v) {
            super(v);
            word = v.findViewById(R.id.wordView);
            def = v.findViewById(R.id.defView);
            v.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.add(this.getAdapterPosition(), v.getId(), 0, "Edit");//groupId, itemId, order, title

        }


    }

    public MyAdapterHome(ArrayList<WordMeaning> list) {
        this.list = list;
    }


    public MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                           int viewType) {
        // create a new view
        final Context context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.home_rec_view_design, parent, false);
        final MyViewHolder vh = new MyViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = vh.getAdapterPosition();
                Intent intent = new Intent(context, ReadActivity.class);

                intent.putExtra(Config.INTENT_WORD_ID, list.get(position).getWordId());


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Activity activity = (Activity) context;
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                            Pair.<View, String>create(vh.word, "textView"),
                            Pair.<View, String>create(vh.def, "textView2"));

                    context.startActivity(intent, options.toBundle());

            }
        }


        });


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.word.setText(list.get(position).getWordMeaning());
        holder.def.setText(list.get(position).getWordDefinition());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateData(ArrayList<WordMeaning> list) {
        this.list = list;
    }


}
