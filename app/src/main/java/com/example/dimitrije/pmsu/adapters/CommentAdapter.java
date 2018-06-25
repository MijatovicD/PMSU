package com.example.dimitrije.pmsu.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimitrije.pmsu.EditCommentActivity;
import com.example.dimitrije.pmsu.LoginActivity;
import com.example.dimitrije.pmsu.R;

import com.example.dimitrije.pmsu.model.Comment;
import com.example.dimitrije.pmsu.service.CommentService;
import com.example.dimitrije.pmsu.service.ServiceUtils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dimitrije on 5/8/2018.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {

    private CommentService commentService;
    private Comment comment;
    private ImageButton likeButton;
    private ImageButton dislikeButton;
    private ImageButton editButton;
    private int likeCount;
    private int dislikeCount;
    private SharedPreferences sharedPreferences;
    public static final String MyPreferences = "Prefs";

    public CommentAdapter(Context context, List<Comment> comments){
        super(context,0,comments);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        final Comment comment = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_layout,viewGroup,false);
        }
        sharedPreferences = getContext().getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);

        final String userPref = sharedPreferences.getString(LoginActivity.Username, "");

        TextView title_view = view.findViewById(R.id.titleComentList);
        TextView author_view = view.findViewById(R.id.authorCommentList);
        TextView description_view = view.findViewById(R.id.descriptionComment);
        TextView date_view = view.findViewById(R.id.dateCommentList);
        String date = new SimpleDateFormat("yyyy.MM.dd").format(comment.getDate());
        date_view.setText(date);
        TextView like_view = view.findViewById(R.id.likeCommentListText);
        TextView dislike_view = view.findViewById(R.id.dislikeCommentListText);
        ImageButton deleteButton = view.findViewById(R.id.deleteComment);

        likeButton = view.findViewById(R.id.likeCommentList);
        dislikeButton = view.findViewById(R.id.dislikeCommentList);
        editButton = view.findViewById(R.id.editComment);

        title_view.setText(comment.getTitle());
        author_view.setText(comment.getAuthor().getUsername());
        description_view.setText(comment.getDescription());
        like_view.setText(String.valueOf(comment.getLikes()));
        dislike_view.setText(String.valueOf(comment.getDislikes()));

        commentService = ServiceUtils.commentService;

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userPref.equals(comment.getAuthor().getUsername()) || userPref.equals("")){
                    Toast.makeText(getContext(), "You cant delete your comment", Toast.LENGTH_SHORT).show();
                }else{
                    deleteComment(comment.getId());
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });


        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!userPref.equals(comment.getAuthor().getUsername()) || userPref.equals("")){
                    likeCount = comment.getLikes();
                    comment.setLikes(likeCount + 1);
                    Call<Comment> call = commentService.likeDislike(comment, comment.getId());

                    call.enqueue(new Callback<Comment>() {
                        @Override
                        public void onResponse(Call<Comment> call, Response<Comment> response) {
                            Toast.makeText(getContext(), "Liked", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Comment> call, Throwable t) {

                        }
                    });
                }else{
                    Toast.makeText(getContext(), "You cant like your comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!userPref.equals(comment.getAuthor().getUsername()) || userPref.equals("")) {
                    dislikeCount = comment.getDislikes();
                    comment.setDislikes(dislikeCount + 1);

                    Call<Comment> call = commentService.likeDislike(comment, comment.getId());

                    call.enqueue(new Callback<Comment>() {
                        @Override
                        public void onResponse(Call<Comment> call, Response<Comment> response) {
                            Toast.makeText(getContext(), "Disliked", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Comment> call, Throwable t) {

                        }
                    });
                }else{
                    Toast.makeText(getContext(), "You cant dislike your comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SharedPreferences.Editor editor = sharedPreferences.edit();

               if(!userPref.equals(comment.getAuthor().getUsername()) || userPref.equals("")){
                   Toast.makeText(getContext(), "You dont have permision to edit this comment", Toast.LENGTH_SHORT).show();
               }else{
                   Intent intent = new Intent(getContext(), EditCommentActivity.class);
                   intent.putExtra("commentId",comment.getId());
                   getContext().startActivity(intent);

               }
            }
        });

        return view;

    }

    public void deleteComment(int id){
        Call<Comment> call = commentService.deleteComment(id);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });
        this.notifyDataSetChanged();
    }

}
