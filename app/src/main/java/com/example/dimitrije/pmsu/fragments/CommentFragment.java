package com.example.dimitrije.pmsu.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dimitrije.pmsu.R;
import com.example.dimitrije.pmsu.adapters.CommentAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import model.Comment;
import model.Tag;
import model.User;

/**
 * Created by Dimitrije on 5/8/2018.
 */

public class CommentFragment extends Fragment {

    private View view;

    private Comment c1 = new Comment();
    private Comment c2 = new Comment();

    private ArrayList<Comment> comments = new ArrayList<>();

    private CommentAdapter commentAdapter;

    private User u1 = new User();
    private User u2 = new User();

    private Tag t1 = new Tag();
    private Tag t2 = new Tag();


    private SharedPreferences sharedPreferences;

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

        commentAdapter = new CommentAdapter(getContext(), comments);

        ListView listView = view.findViewById(R.id.listViewComment);
        listView.setAdapter(commentAdapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        consultPreferences();

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
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment comment, Comment comment1) {
                return comment1.getDate().compareTo(comment.getDate());
            }
        });
    }

    public void sortByPopularity(){
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment comment, Comment comment1) {
                return comment1.getLikes() - comment.getLikes();
            }
        });
    }
}
