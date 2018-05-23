package com.example.dimitrije.pmsu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.example.dimitrije.pmsu.fragments.CommentFragment;
import com.example.dimitrije.pmsu.fragments.ReadPostFragment;
import com.example.dimitrije.pmsu.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import com.example.dimitrije.pmsu.model.NavItem;
import com.example.dimitrije.pmsu.model.Post;
import com.example.dimitrije.pmsu.model.User;
import com.example.dimitrije.pmsu.service.PostService;
import com.example.dimitrije.pmsu.service.ServiceUtils;
import com.example.dimitrije.pmsu.service.UserService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReadPostActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayoutCreate;
    private ListView mDrawerListCreate;
    private ActionBarDrawerToggle mDrawerToggleCreate;
    private RelativeLayout mDrawerPaneCreate;
    private CharSequence mDrawerTittleCreate;
    private CharSequence mTitleCreate;
    private ArrayList<NavItem> mNavItemCreate = new ArrayList<>();
    private AlertDialog dialog;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Post post = new Post();
    private User user = new User();
    private List<Post> posts = new ArrayList<Post>();
    private PostService postService;
    private UserService userService;
    String userPreferences;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);
/*
        TextView titleText = findViewById(R.id.title_Read);
        titleText.setText("Naslov posta");
        TextView descriptionnText = findViewById(R.id.description_Read);
        descriptionnText.setText("Opis posta");
        TextView userText = findViewById(R.id.user_Read);
        userText.setText("pera");
        TextView dateText = findViewById(R.id.date_Read);
        dateText.setText("26-3-2018");
        TextView locationText = findViewById(R.id.location_Read);
        locationText.setText("Novi Sad");
        TextView likeText = findViewById(R.id.countLike);
        likeText.setText("Likes 8");
        TextView dislikeText = findViewById(R.id.countDislike);
        dislikeText.setText("Dislikes 4");
        TextView tagText = findViewById(R.id.tag_Read);
        tagText.setText("#hashtag");
*/
        prepareMenu(mNavItemCreate);

        mTitleCreate = mDrawerTittleCreate = getTitle();
        mDrawerLayoutCreate = (DrawerLayout) findViewById(R.id.drawerLayoutPost);
        mDrawerListCreate = (ListView) findViewById(R.id.navListPost);

        mDrawerPaneCreate = (RelativeLayout) findViewById(R.id.drawerPaneRead);
        DrawerListAdapter adapter = new DrawerListAdapter( this, mNavItemCreate);
        mDrawerListCreate.setAdapter(adapter);

        mDrawerListCreate.setOnItemClickListener(new ReadPostActivity.DrawerItemClickListenerCreate());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ReadPostFragment(), "Read post");
        viewPagerAdapter.addFragment(new CommentFragment(), "Comments");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPost);
        setSupportActionBar(toolbar);

        final android.support.v7.app.ActionBar actionBarCreate = getSupportActionBar();

        if (actionBarCreate != null){
            actionBarCreate.setDisplayHomeAsUpEnabled(true);
            actionBarCreate.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBarCreate.setHomeButtonEnabled(true);
        }

        mDrawerToggleCreate = new ActionBarDrawerToggle(this, mDrawerLayoutCreate, toolbar,
                R.string.drawer_open, R.string.drawer_close){

            public void onDrawerClosed(View view){
                getSupportActionBar().setTitle(mTitleCreate);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                getSupportActionBar().setTitle("MyNews");
                invalidateOptionsMenu();
            }
        };

        mDrawerLayoutCreate.addDrawerListener(mDrawerToggleCreate);
        mDrawerToggleCreate.syncState();


        String json = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            json = extras.getString("Post");
        }
        post = new Gson().fromJson(json, Post.class);
        postService = ServiceUtils.postService;

//        userPreferences = sharedPreferences.getString(LoginActivity.Username, "");

        userService = ServiceUtils.userService;

        Call<User> call = userService.getByUsername(userPreferences);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }


    private void prepareMenu(ArrayList<NavItem> mNavItems){
        mNavItems.add(new NavItem("Posts", "Postovi", R.drawable.ic_action_post));
        mNavItems.add(new NavItem("Settigs", "Podesavanja", R.drawable.ic_action_settings));
        mNavItems.add(new NavItem("Logout", "Odajva", R.drawable.ic_action_logout));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuSetting:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.menuDelete:
                postService = ServiceUtils.postService;

                Call<Post> call = postService.deletePost(post.getId());

                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {

                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(this, "Deleted", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_read, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class DrawerItemClickListenerCreate implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            selectItemFromDrawerCreate(position);
        }
    }

    private void selectItemFromDrawerCreate(int position){
        if (position == 0){
            Intent post = new Intent(this, PostsActivity.class);
            startActivity(post);
        }
        if (position == 1){
            Intent setting = new Intent(this, SettingsActivity.class);
            startActivity(setting);
        }  if (position == 2){
            Intent logout = new Intent(this, LoginActivity.class);
            startActivity(logout);
        }

        mDrawerListCreate.setItemChecked(position, true);
        setTitle(mNavItemCreate.get(position).getmTitle());
        mDrawerLayoutCreate.closeDrawer(mDrawerPaneCreate);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleCreate = title;
        getSupportActionBar().setTitle(mTitleCreate);
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
