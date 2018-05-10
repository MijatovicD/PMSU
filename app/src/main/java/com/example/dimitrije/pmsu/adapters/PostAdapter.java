package com.example.dimitrije.pmsu.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dimitrije.pmsu.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.Post;

/**
 * Created by Dimitrije on 4/18/2018.
 */

public class PostAdapter extends ArrayAdapter<Post> {

    public PostAdapter(Context context, ArrayList<Post> posts){
        super(context, 0, posts);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Post post = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_list, viewGroup, false);
        }

        TextView titleView = convertView.findViewById(R.id.titlePostList);
        TextView dateView = convertView.findViewById(R.id.subTitleList);
        ImageView imageView = convertView.findViewById(R.id.iconPostList);

        titleView.setText(post.getTitle());

        String date = new SimpleDateFormat("yyyy.MM.dd").format(post.getDate());

        dateView.setText(date);

        imageView.setImageResource(R.drawable.ic_action_post);

        return convertView;
    }

}
