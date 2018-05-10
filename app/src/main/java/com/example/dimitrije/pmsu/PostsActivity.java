package com.example.dimitrije.pmsu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.dimitrije.pmsu.adapters.DrawerListAdapter;
import com.example.dimitrije.pmsu.adapters.PostAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


import model.NavItem;
import model.Post;
import model.User;

public class PostsActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTittle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItem = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private SharedPreferences sharedPreferences;
    private boolean sortPostByDate;
    private boolean sortPostByPopularity;


    private Post post = new Post();
    private Post post1 = new Post();
    private User user1 = new User();
    private User user2 = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        prepareMenu(mNavItem);

        mTitle = mDrawerTittle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutPost);
        mDrawerList = (ListView) findViewById(R.id.navListPost);

        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPanePost);
        DrawerListAdapter adapter = new DrawerListAdapter( this, mNavItem);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new PostsActivity.DrawerItemClickListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPost);
        setSupportActionBar(toolbar);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
        }


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close){

            public void onDrawerClosed(View view){
                getActionBar().setTitle(mTitle);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                getSupportActionBar().setTitle("MyNews");
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        post.setTitle("Mark Zucerberg prodao Facebook");
        post.setDate(new Date(2018-1900, 04-01, 23));
        post.setDescription("Opis za post broj 1");
        user1.setUsername("pera");
        post.setAuthor(user1);
        post.setLikes(4);
        post.setDislikes(2);


        post1.setTitle("Insagram kupio Facebook");
        post1.setDate(new Date(2018-1900, 05-01, 30));
        post1.setDescription("Opis za post broj 2");
        user2.setUsername("mika");
        post1.setAuthor(user2);
        post1.setLikes(8);
        post1.setDislikes(4);


        posts.add(post);
        posts.add(post1);

        PostAdapter postAdapter = new PostAdapter(this, posts);
        ListView listView = findViewById(R.id.postList);

        listView.setAdapter(postAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Post post = posts.get(i);


                Intent intent = new Intent(PostsActivity.this,ReadPostActivity.class);
                intent.putExtra("title",post.getTitle());
                String formatedDate = new SimpleDateFormat("yyyy.MM.dd").format(post.getDate());
                intent.putExtra("date",formatedDate);
                intent.putExtra("author",post.getAuthor().getUsername());
                intent.putExtra("description",post.getDescription());
                intent.putExtra("likes",String.valueOf(post.getLikes()));
                intent.putExtra("dislikes",String.valueOf(post.getDislikes()));

                startActivity(intent);
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        consultPreferences();
    }


    private void consultPreferences(){
        sortPostByDate = sharedPreferences.getBoolean(getString(R.string.sortPostByDate),false);
        sortPostByPopularity = sharedPreferences.getBoolean(getString(R.string.sortPostByPopularity),false);

        if(sortPostByDate == true) {
            sortDate();
        }

        if(sortPostByPopularity == true){
            sortPopularity();
        }

    }

    private void sortDate(){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post posts1) {
                return posts1.getDate().compareTo(post.getDate());
            }
        });
    }

    private void sortPopularity(){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post posts1) {
                return posts1.getLikes() - post.getLikes();
            }
        });
    }


    private void prepareMenu(ArrayList<NavItem> mNavItems){
        mNavItems.add(new NavItem("Posts", "Postovi", R.drawable.ic_action_post));
        mNavItems.add(new NavItem("Settigs", "Podesavanja", R.drawable.ic_action_settings));
        mNavItems.add(new NavItem("Create post", "Kreiraj post", R.drawable.ic_action_add));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuSetting:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuCreate:
                Toast.makeText(this, "Create", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            selectItemFromDrawer(position);
        }
    }

    private void selectItemFromDrawer(int position){
        if (position == 0){

        }
        else if (position == 1){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        else if (position == 2){
            Intent i = new Intent(this, CreatePostsActivity.class);
            startActivity(i);
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItem.get(position).getmTitle());
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
