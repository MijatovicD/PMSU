package com.example.dimitrije.pmsu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dimitrije.pmsu.adapters.CommentAdapter;
import com.example.dimitrije.pmsu.model.Comment;
import com.example.dimitrije.pmsu.service.CommentService;
import com.example.dimitrije.pmsu.service.ServiceUtils;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCommentActivity extends AppCompatActivity {

    private CommentService commentService;
    private EditText titleEdit;
    private EditText descriptionEdit;
    private Button btnEdit;
    private Comment comment = new Comment();
    private int commentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);

        titleEdit = findViewById(R.id.titleCommentEdit);
        descriptionEdit = findViewById(R.id.descriptionCommentEdit);
        btnEdit = findViewById(R.id.btnEditComment);


        commentService = ServiceUtils.commentService;

        commentId = getIntent().getIntExtra("commentId", 0);

        Call<Comment> call = commentService.getCommentId(commentId);

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                comment = response.body();

                if (comment != null){
                    titleEdit.setText(comment.getTitle());
                    descriptionEdit.setText(comment.getDescription());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateComment();
            }
        });
    }


    public void updateComment(){

        commentService = ServiceUtils.commentService;

        Call<Comment> callComment = commentService.updateComment(comment, commentId);

        callComment.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                comment = response.body();
                String title = titleEdit.getText().toString();
                String description = descriptionEdit.getText().toString();

                comment.setTitle(title);
                comment.setDescription(description);
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });
    }
}
