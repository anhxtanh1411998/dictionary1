package com.studentproject.dictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class CustomAdapter extends ArrayAdapter<translate> {
    private Context context;
    private ArrayList<translate> list;
    private int resource;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<translate> list) {
        super(context, resource, list);
        this.context =context;
        this.list = list;
        this.resource=resource;

    }

    public void addItemCustomAdapter(ArrayList<translate> translates){
        list.addAll(translates);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Viewhold viewHold;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.display_iteam_listview, parent, false);

            viewHold = new Viewhold();
            viewHold.content = convertView.findViewById(R.id.tv_content);
            viewHold.word = convertView.findViewById(R.id.tv_word);
            convertView.setTag(viewHold);
        } else {
            viewHold = (Viewhold) convertView.getTag();
        }

        translate translate =list.get(position);
        viewHold.content.setText(translate.getContent());
        viewHold.word.setText(translate.getWord());
        return convertView;
    }

    static class Viewhold{
        TextView word;
        TextView content;
    }
}
