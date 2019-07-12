package com.example.husseinjehadalhroub.englishwords.MyAdapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.husseinjehadalhroub.englishwords.DataTypes.DataWord;
import com.example.husseinjehadalhroub.englishwords.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterWord extends RecyclerView.Adapter<MyAdapterWord.MyViewHolder> {
    protected ArrayList<String> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public int counterTest = 0;
        public EditText editText;
        public ImageView imageView;

        public MyViewHolder(View v) {
            super(v);
            editText = v.findViewById(R.id.editText2);
            imageView = v.findViewById(R.id.deleteImage);


            final TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

//                    list.get(getAdapterPosition()).(s.toString());
                    list.set(getAdapterPosition(), s.toString());
//                    Log.i("change TEXT ", counterTest++ + "      " + getAdapterPosition());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
            editText.addTextChangedListener(watcher);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position == -1)
                        return;
                    counterTest = 0;
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, list.size());
//                    Log.i("deleted", "" + position);
                }
            });


        }


    }


    public MyAdapterWord(ArrayList<String> list) {
        this.list = list;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_custome, parent, false);

//        LayoutParams params = v.getLayoutParams();
//        params.height = 200;
//        v.setLayoutParams(params);
        MyViewHolder vh = new MyViewHolder(v);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        holder.editText.setText(list.get(position).getFieldText());
        holder.editText.setText(list.get(position));
        System.out.println("for position " + position + " the holder.edittext = " + list.get(position));

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


}
