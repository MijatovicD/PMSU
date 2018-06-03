package com.example.dimitrije.pmsu;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dimitrije.pmsu.model.Post;
import com.example.dimitrije.pmsu.service.PostService;
import com.example.dimitrije.pmsu.service.ServiceUtils;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostActivity extends AppCompatActivity {

    private Post post = new Post();
    private Button button;
    private EditText editTitle;
    private EditText editDescription;
    private PostService postService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        sharedPreferences = getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);

        int postId = sharedPreferences.getInt("Post", post.getId());

        editTitle = findViewById(R.id.editTitlePost);
        editDescription = findViewById(R.id.editDescriptionPost);

        postService = ServiceUtils.postService;

        Call<Post> call = postService.getPost(postId);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                post = response.body();

                EditText title = findViewById(R.id.editTitlePost);
                title.setText(post.getTitle());

                EditText description = findViewById(R.id.editDescriptionPost);
                description.setText(post.getDescription());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });

        button = findViewById(R.id.btnEditPost);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePost();
            }
        });

    }

    public void updatePost(){

        postService = ServiceUtils.postService;

        Call<Post> call = postService.updatePost(post, post.getId());


        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                String titlePost =  editTitle.getText().toString();
                String descriptionPost = editDescription.getText().toString();
                post.setTitle(titlePost);
                post.setDescription(descriptionPost);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

}
