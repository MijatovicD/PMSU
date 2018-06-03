package com.example.dimitrije.pmsu.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimitrije.pmsu.EditPostActivity;
import com.example.dimitrije.pmsu.LoginActivity;
import com.example.dimitrije.pmsu.PostsActivity;
import com.example.dimitrije.pmsu.R;
import com.example.dimitrije.pmsu.ReadPostActivity;
import com.example.dimitrije.pmsu.model.Post;
import com.example.dimitrije.pmsu.model.Tag;
import com.example.dimitrije.pmsu.service.PostService;
import com.example.dimitrije.pmsu.service.ServiceUtils;
import com.example.dimitrije.pmsu.service.TagService;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dimitrije on 4/17/2018.
 */

public class ReadPostFragment extends Fragment {

    private View view;
    private Post post;
    private Post post1;
    private TagService tagService;
    private List<Tag> tags;
    private ImageButton likeButton;
    private ImageButton dislikeButton;
    private int countLike;
    private int countDislike;
    private PostService postService;

    public ReadPostFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.read_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_post, menu);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String json = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null){
            json = extras.getString("Post");
        }
        post = new Gson().fromJson(json, Post.class);

        post.getId();

        TextView title = view.findViewById(R.id.title_Read);
        title.setText(post.getTitle());

        TextView date = view.findViewById(R.id.date_Read);
        String datePost = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(post.getDate());
        date.setText(datePost);

        TextView description = view.findViewById(R.id.description_Read);
        description.setText(post.getDescription());

        TextView user = view.findViewById(R.id.user_Read);
        user.setText(post.getAuthor().getUsername());

        TextView like = view.findViewById(R.id.countLikee);
        like.setText(Integer.toString(post.getLikes()));

        TextView dislike = view.findViewById(R.id.countDislikee);
        dislike.setText(String.valueOf(post.getDislikes()));

        TextView latitude = view.findViewById(R.id.latitude);
        latitude.setText(Double.toString(post.getLatitude()));

        TextView longitude = view.findViewById(R.id.longitude);
        longitude.setText(Double.toString(post.getLongitude()));

        final TextView tag = view.findViewById(R.id.tag_Read);

        tagService = ServiceUtils.tagService;
        Call<List<Tag>> call = tagService.getTagByPost(post.getId());

        call.enqueue(new Callback<List<Tag>>() {
        @Override
        public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {

            tags = response.body();

            for(Tag t : tags){
                tag.setText(t.getName());
            }

        }

        @Override
        public void onFailure(Call<List<Tag>> call, Throwable t) {

        }
    });

    postService = ServiceUtils.postService;
    likeButton = view.findViewById(R.id.like_Read);
    dislikeButton = view.findViewById(R.id.dislike_Read);


    likeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            countLike = post.getLikes();
            post.setLikes(countLike+1);

            Call<Post> call = postService.likeDislike(post, post.getId());

            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    Toast.makeText(getContext(), "Liked", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    Toast.makeText(getContext(), "Greska", Toast.LENGTH_SHORT).show();
                }
            });

        }
    });

    dislikeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            countDislike = post.getDislikes();
            post.setDislikes(countDislike);

            Call<Post> call = postService.likeDislike(post, post.getId());

            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    Toast.makeText(getContext(), "Disliked", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {

                }
            });
        }
    });
    }

}
