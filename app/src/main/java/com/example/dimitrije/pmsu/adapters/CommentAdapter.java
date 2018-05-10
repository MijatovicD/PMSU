package com.example.dimitrije.pmsu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dimitrije.pmsu.R;

import model.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Dimitrije on 5/8/2018.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {

    public CommentAdapter(Context context, ArrayList<Comment> comments){
        super(context,0,comments);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        Comment comment = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_layout,viewGroup,false);
        }

        TextView title_view = view.findViewById(R.id.titleComentList);
        TextView author_view = view.findViewById(R.id.authorCommentList);
        TextView comment_view = view.findViewById(R.id.comment_view);
        TextView date_view = view.findViewById(R.id.dateCommentList);
        String date = new SimpleDateFormat("yyyy.MM.dd").format(comment.getDate());
        date_view.setText(date);
        TextView like_view = view.findViewById(R.id.likeCommentListText);
        TextView dislike_view = view.findViewById(R.id.dislike_comment_text);


        title_view.setText(comment.getTitle());
        author_view.setText(comment.getAuthor().getUsername());
        comment_view.setText(comment.getDescription());
        like_view.setText(String.valueOf(comment.getLikes()));
        dislike_view.setText(String.valueOf(comment.getDislikes()));

        return view;
    }
}
