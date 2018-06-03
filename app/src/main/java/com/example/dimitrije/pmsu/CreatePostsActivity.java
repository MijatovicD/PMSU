package com.example.dimitrije.pmsu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimitrije.pmsu.adapters.DrawerListAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.dimitrije.pmsu.dialogs.LocationDialog;
import com.example.dimitrije.pmsu.fragments.MapFragment;
import com.example.dimitrije.pmsu.model.Comment;
import com.example.dimitrije.pmsu.model.NavItem;
import com.example.dimitrije.pmsu.model.Post;
import com.example.dimitrije.pmsu.model.Tag;
import com.example.dimitrije.pmsu.model.User;
import com.example.dimitrije.pmsu.service.PostService;
import com.example.dimitrije.pmsu.service.ServiceUtils;
import com.example.dimitrije.pmsu.service.TagService;
import com.example.dimitrije.pmsu.service.UserService;
import com.example.dimitrije.pmsu.tools.FragmentTransition;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostsActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayoutCreate;
    private ListView mDrawerListCreate;
    private ActionBarDrawerToggle mDrawerToggleCreate;
    private RelativeLayout mDrawerPaneCreate;
    private CharSequence mDrawerTittleCreate;
    private CharSequence mTitleCreate;
    private ArrayList<NavItem> mNavItemCreate = new ArrayList<>();

    private PostService postService;
    private TagService tagService;
    private static Post postBody;
    private List<Post> posts = new ArrayList<>();
    private EditText titleText;
    private EditText descriptionText;
    private EditText tagText;
    private ImageButton btnPhoto;
    private static User user;
    private Comment comment = new Comment();
    private static Tag tag = new Tag();
    private static Tag tagBody = new Tag();
    private SharedPreferences sharedPreferences;
    private SharedPreferences map;
    private UserService userService;
    private AlertDialog dialog;
    private Bitmap bitmap;
    private TextView locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_posts);


        prepareMenu(mNavItemCreate);

        mTitleCreate = mDrawerTittleCreate = getTitle();
        mDrawerLayoutCreate = (DrawerLayout) findViewById(R.id.drawerLayoutCreate);
        mDrawerListCreate = (ListView) findViewById(R.id.navListCreate);

        mDrawerPaneCreate = (RelativeLayout) findViewById(R.id.drawerPaneCreate);
        DrawerListAdapter drawerListAdapter = new DrawerListAdapter(this, mNavItemCreate);
        mDrawerListCreate.setAdapter(drawerListAdapter);

        mDrawerListCreate.setOnItemClickListener(new CreatePostsActivity.DrawerItemClickListenerCreate());

        Toolbar toolbarCreate = (Toolbar) findViewById(R.id.toolbarCreate);
        setSupportActionBar(toolbarCreate);

        titleText = findViewById(R.id.titleCreate);
        descriptionText = findViewById(R.id.descriptionCreate);
        tagText = findViewById(R.id.tagCreate);
        locationText = findViewById(R.id.longitude);

        btnPhoto = findViewById(R.id.photoCreate);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
            }
        });


        final android.support.v7.app.ActionBar actionBarCreate = getSupportActionBar();

        if (actionBarCreate != null) {
            actionBarCreate.setDisplayHomeAsUpEnabled(true);
            actionBarCreate.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBarCreate.setHomeButtonEnabled(true);
        }

        mDrawerToggleCreate = new ActionBarDrawerToggle(this, mDrawerLayoutCreate, toolbarCreate,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitleCreate);
                getSupportActionBar().setTitle(mTitleCreate);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("MyNews");
                invalidateOptionsMenu();
            }
        };


        postService = ServiceUtils.postService;
        userService = ServiceUtils.userService;
        tagService = ServiceUtils.tagService;

        sharedPreferences = getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);
        map = getSharedPreferences(MapFragment.Prefe, Context.MODE_PRIVATE);

        String userPref = sharedPreferences.getString(LoginActivity.Username, "");
        System.out.println("fJLKFSKLJSFSDFJKLFWKJL" + userPref);
        Call<User> call = userService.getByUsername(userPref);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                System.out.println("USER GEEET " + user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void prepareMenu(ArrayList<NavItem> mNavItems) {
        mNavItems.add(new NavItem("Posts", "Postovi", R.drawable.ic_action_post));
        mNavItems.add(new NavItem("Settigs", "Podesavanja", R.drawable.ic_action_settings));
        mNavItems.add(new NavItem("Location", "Map", R.drawable.ic_action_map));
        mNavItems.add(new NavItem("Logout", "Odjava", R.drawable.ic_action_logout));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSetting:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.shareMenu:
                    addPost();
                    Intent intent = new Intent(this, PostsActivity.class);
                    startActivity(intent);
                    finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addPost() {
        final Post post = new Post();

        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();

        post.setTitle(title);
        post.setDescription(description);
        System.out.println("USERNAME + " + user.getUsername());
        post.setAuthor(user);
        post.setLikes(0);
        post.setDislikes(0);
        Date date = new Date();
        post.setDate(date);

        Float latitude = map.getFloat(MapFragment.Latitude, 0);
        Float longitude = map.getFloat(MapFragment.Longitude, 0);
        post.setLatitude(latitude);
        post.setLongitude(longitude);

        Call<Post> call = postService.addPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                postBody = response.body();
                addTag();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Greska", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void showLocationDialog(){
        if(dialog == null){
            dialog = new LocationDialog(this).prepareDialog();
        }else{
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
        dialog.show();
    }

    public boolean validate(){
        if(titleText.equals("") || titleText == null){
            Toast.makeText(this, "Morate uneti title", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(descriptionText.equals("") || descriptionText == null) {
            Toast.makeText(this, "Morate uneti description", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }


    public void addTag(){
        String tagString = tagText.getText().toString().trim();
        String[] separator = tagString.split("#");

        List<String> tagFilter = Arrays.asList(separator);
        for (String tagStrings : tagFilter.subList(1, tagFilter.size())){
            tag.setName("#" + tagString);

            Call<Tag> call = tagService.addTag(tag);
            call.enqueue(new Callback<Tag>() {
                @Override
                public void onResponse(Call<Tag> call, Response<Tag> response) {
                    tagBody = response.body();
                    addTagInPost(postBody.getId(), tagBody.getId());
                }

                @Override
                public void onFailure(Call<Tag> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Greska", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void addImage(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.id.imageContainer);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
    }

    public void addTagInPost(int postId, int tagId){
        Call<Post> call = postService.addTagInPost(postBody.getId(), tagBody.getId());

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {


            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri image = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                ImageView view = findViewById(R.id.imageContainer);
                view.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create, menu);
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
        }
        if (position == 2){
            FragmentTransition.to(MapFragment.newInstance(), this, false);
        }
        if (position == 3){
            Intent logout = new Intent(this, LoginActivity.class);
            sharedPreferences.edit().clear().commit();
            startActivity(logout);
            finish();
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
