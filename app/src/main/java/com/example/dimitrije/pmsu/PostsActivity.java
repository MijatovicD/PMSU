package com.example.dimitrije.pmsu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimitrije.pmsu.adapters.DrawerListAdapter;
import com.example.dimitrije.pmsu.adapters.PostAdapter;
import com.example.dimitrije.pmsu.model.Tag;
import com.example.dimitrije.pmsu.service.TagService;
import com.example.dimitrije.pmsu.service.UserService;
import com.example.dimitrije.pmsu.tools.FragmentTransition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


import com.example.dimitrije.pmsu.fragments.MapFragment;
import com.example.dimitrije.pmsu.model.NavItem;
import com.example.dimitrije.pmsu.model.Post;
import com.example.dimitrije.pmsu.model.User;
import com.example.dimitrije.pmsu.service.PostService;
import com.example.dimitrije.pmsu.service.ServiceUtils;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTittle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItem = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private boolean sortPostByDate;
    private boolean sortPostByPopularity;
    private PostService postService;
    private UserService userService;
    private TagService tagService;
    private PostAdapter postAdapter;
    private Button editUser;

    private ListView listView;

    private Post post = new Post();
    private User user = new User();
    private Tag tag = new Tag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        prepareMenu(mNavItem);

        mTitle = mDrawerTittle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutPost);
        mDrawerList = (ListView) findViewById(R.id.navListPost);
        listView = (ListView) findViewById(R.id.postList);

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
//                getActionBar().setTitle(mTitle);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                getSupportActionBar().setTitle("MyNews");
                invalidateOptionsMenu();
            }
        };


        TextView userText = findViewById(R.id.userName);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        sharedPreferences = getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(LoginActivity.Username)){
            userText.setText(sharedPreferences.getString(LoginActivity.Name, ""));
        }

        postService = ServiceUtils.postService;
        userService = ServiceUtils.userService;
        tagService = ServiceUtils.tagService;
        Call call = postService.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(response.isSuccessful()){
                    posts = response.body();
                    listView.setAdapter(new PostAdapter(PostsActivity.this, posts));
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        Call callTags = tagService.getTags();

        callTags.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {

                if(response.isSuccessful()){
                    tags = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        Call userCall = userService.getUsers();

        userCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if(response.isSuccessful()){
                    users = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        editUser = findViewById(R.id.editProfile);

        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPref = sharedPreferences.getString(LoginActivity.Username, "");

                Call<User> call = userService.getByUsername(userPref);

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        System.out.println("USERNAME" + user.getUsername());
                        Intent intent = new Intent(PostsActivity.this, EditUsersActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });

/*
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

        listView.setAdapter(postAdapter);
*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                post = posts.get(i);

                postService = ServiceUtils.postService;
                Call<Post> call = postService.getPost(post.getId());

                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {

                        if (response.isSuccessful()){
                            post = response.body();
                            Intent intent = new Intent(PostsActivity.this,ReadPostActivity.class);
                            intent.putExtra("Post", new Gson().toJson(post));

                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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
        Call<List<Post>> callPost = postService.sortPosts();

        callPost.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                posts = response.body();

                PostAdapter adapter = new PostAdapter(PostsActivity.this, posts);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }

    private void sortPopularity(){
       Call<List<Post>> callLike = postService.sortPostsByLike();

       callLike.enqueue(new Callback<List<Post>>() {
           @Override
           public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
               posts = response.body();

               PostAdapter adapter = new PostAdapter(PostsActivity.this, posts);
               listView.setAdapter(adapter);
           }

           @Override
           public void onFailure(Call<List<Post>> call, Throwable t) {

           }
       });
    }


    private void prepareMenu(ArrayList<NavItem> mNavItems){
        mNavItems.add(new NavItem("Posts", "Postovi", R.drawable.ic_action_post));
        mNavItems.add(new NavItem("Settigs", "Podesavanja", R.drawable.ic_action_settings));
        mNavItems.add(new NavItem("Create post", "Kreiraj post", R.drawable.ic_action_add));
        mNavItems.add(new NavItem("Location", "Map", R.drawable.ic_action_map));
        mNavItems.add(new NavItem("Edit user", "Izmeni", R.drawable.ic_action_edit));
        mNavItems.add(new NavItem("Log out", "Odjava", R.drawable.ic_action_logout));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuSetting:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuCreate:
                Intent create = new Intent(this, CreatePostsActivity.class);
                startActivity(create);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
             /*   ArrayList<Post> postList = new ArrayList<>();

                for (Post p : posts){
                    if (p.getAuthor().getUsername().contains(s.toLowerCase())){
                        postList.add(p);
                    }
                }

                PostAdapter postAdapter = new PostAdapter(PostsActivity.this, postList);
                listView.setAdapter(postAdapter);
*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
              /*List<Post> postList = new ArrayList<>();


               for (Tag t : tags){
                   if (t.getName().contains(s.toLowerCase())){
                       t.getPosts().addAll(postList);
                   }
               }

               PostAdapter postAdapter = new PostAdapter(PostsActivity.this, postList);
               listView.setAdapter(postAdapter);*/
                ArrayList<Post> postList = new ArrayList<>();

                for (Post p : posts){
                    if (p.getAuthor().getUsername().contains(s.toLowerCase())){
                        postList.add(p);
                    }
                }

                PostAdapter postAdapter = new PostAdapter(PostsActivity.this, postList);
                listView.setAdapter(postAdapter);


             return true;
            }
        });
        return true;
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
        else if (position == 3){
            FragmentTransition.to(MapFragment.newInstance(), this, false);
        }
        else if (position == 4){
            Intent edit = new Intent(this, EditUserActivity.class);
            startActivity(edit);
        }
        else if (position == 5){
            Intent ite = new Intent(this, LoginActivity.class);
            sharedPreferences.edit().clear().commit();
            startActivity(ite);
            finish();
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
