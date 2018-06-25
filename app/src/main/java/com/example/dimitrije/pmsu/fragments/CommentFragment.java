package com.example.dimitrije.pmsu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dimitrije.pmsu.LoginActivity;
import com.example.dimitrije.pmsu.R;
import com.example.dimitrije.pmsu.adapters.CommentAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.example.dimitrije.pmsu.model.Comment;
import com.example.dimitrije.pmsu.model.Post;
import com.example.dimitrije.pmsu.model.Tag;
import com.example.dimitrije.pmsu.model.User;
import com.example.dimitrije.pmsu.service.CommentService;
import com.example.dimitrije.pmsu.service.PostService;
import com.example.dimitrije.pmsu.service.ServiceUtils;
import com.example.dimitrije.pmsu.service.UserService;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dimitrije on 5/8/2018.
 */

public class CommentFragment extends Fragment {

    private View view;
    private CommentService commentService;
    private UserService userService;
    private List<Comment> comments = new ArrayList<>();
    private Post post;
    private User user;
    private ListView listView;
    private SharedPreferences sharedPreferences;
    private Button addButton;
    private EditText titleEdit;
    private EditText descriptionEdit;
    private boolean sortCommentsByDate;
    private boolean sortCommentsByPopularity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.comment_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*
        c1.setDescription("Opis komentara 1");
        c1.setDate(new Date(2018-1900, 03-01, 19));
        u1.setUsername("pera");
        c1.setAuthor(u1);
        c1.setLikes(2);
        c1.setDislikes(1);

        c2.setDescription("Opis komentara 2");
        c2.setDate(new Date(2018-1900, 07-01, 24));
        u2.setUsername("mika");
        c2.setAuthor(u2);
        c2.setLikes(8);
        c2.setDislikes(9);

        comments.add(c1);
        comments.add(c2);
*/



        String json = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null){
            json = extras.getString("Post");
        }

        post = new Gson().fromJson(json, Post.class);
        System.out.println("NEKII POST + " + post.getId());
        listView = (ListView) view.findViewById(R.id.listViewComment);
        titleEdit = view.findViewById(R.id.titleCommentAdd);
        descriptionEdit = view.findViewById(R.id.descriptionCommentAdd);
        addButton = view.findViewById(R.id.addComment);

        commentService = ServiceUtils.commentService;
        Call<List<Comment>> call = commentService.getCommentPost(post.getId());

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                    if(response.isSuccessful()){
                        comments = response.body();
                        CommentAdapter commentAdapter = new CommentAdapter(getContext(), comments);

                        listView.setAdapter(commentAdapter);

                        consultPreferences();
                    }

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });


        userService = ServiceUtils.userService;

        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);
        final String userPrefe = sharedPreferences.getString(LoginActivity.Username, "");

        Call<User> callUser = userService.getByUsername(userPrefe);

        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userPrefe.equals("")){
                    Toast.makeText(getContext(), "You cant add comment", Toast.LENGTH_SHORT).show();
                }else{
                    addComment();
                    titleEdit.setText("");
                    descriptionEdit.setText("");
                    FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
                    t.setAllowOptimization(true);
                    t.detach(CommentFragment.this).attach(CommentFragment.this).commitAllowingStateLoss();
                }
            }
        });

    }


    private void addComment(){
        Comment comment = new Comment();
        String title = titleEdit.getText().toString();
        comment.setTitle(title);
        String description = descriptionEdit.getText().toString();
        comment.setDescription(description);
        Date date = new Date();
        comment.setDate(date);
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setLikes(0);
        comment.setDislikes(0);

        Call<Comment> call = commentService.addComment(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Toast.makeText(getContext(), "Added comment", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Comment > call, Throwable t) {

            }
        });
    }


    private void consultPreferences(){
        sortCommentsByDate = sharedPreferences.getBoolean(getString(R.string.sortCommentByDate),false);
        sortCommentsByPopularity = sharedPreferences.getBoolean(getString(R.string.sortCommentByPopularity),false);

        if(sortCommentsByDate == true){
            sortByDate();
        }

        if(sortCommentsByPopularity == true){
            sortByPopularity();
        }
    }

    public void sortByDate(){
        Call<List<Comment>> callComment = commentService.sortComment();

        callComment.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments = response.body();
                CommentAdapter commentAdapter = new CommentAdapter(getContext(), comments);

                listView.setAdapter(commentAdapter);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }

    public void sortByPopularity(){
        Call<List<Comment>> call = commentService.sortCommentByLike();

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments = response.body();
                CommentAdapter commentAdapter = new CommentAdapter(getContext(), comments);

                listView.setAdapter(commentAdapter);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }
}
