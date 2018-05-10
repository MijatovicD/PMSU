package com.example.dimitrije.pmsu;

import android.content.Intent;
import android.content.res.Configuration;
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

import java.util.ArrayList;
import java.util.Set;

import model.NavItem;

public class CreatePostsActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayoutCreate;
    private ListView mDrawerListCreate;
    private ActionBarDrawerToggle mDrawerToggleCreate;
    private RelativeLayout mDrawerPaneCreate;
    private CharSequence mDrawerTittleCreate;
    private CharSequence mTitleCreate;
    private ArrayList<NavItem> mNavItemCreate = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_posts);


        prepareMenu(mNavItemCreate);

        mTitleCreate = mDrawerTittleCreate = getTitle();
        mDrawerLayoutCreate = (DrawerLayout) findViewById(R.id.drawerLayoutCreate);
        mDrawerListCreate = (ListView) findViewById(R.id.navListCreate);

        mDrawerPaneCreate = (RelativeLayout) findViewById(R.id.drawerPaneCreate);
        DrawerListAdapter drawerListAdapter = new DrawerListAdapter( this, mNavItemCreate);
        mDrawerListCreate.setAdapter(drawerListAdapter);

        mDrawerListCreate.setOnItemClickListener(new CreatePostsActivity.DrawerItemClickListenerCreate());

        Toolbar toolbarCreate = (Toolbar) findViewById(R.id.toolbarCreate);
        setSupportActionBar(toolbarCreate);

        final android.support.v7.app.ActionBar actionBarCreate = getSupportActionBar();

        if (actionBarCreate != null){
            actionBarCreate.setDisplayHomeAsUpEnabled(true);
            actionBarCreate.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBarCreate.setHomeButtonEnabled(true);
        }

        mDrawerToggleCreate = new ActionBarDrawerToggle(this, mDrawerLayoutCreate, toolbarCreate,
                R.string.drawer_open, R.string.drawer_close){

            public void onDrawerClosed(View view){
                getActionBar().setTitle(mTitleCreate);
                getSupportActionBar().setTitle(mTitleCreate);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                getSupportActionBar().setTitle("MyNews");
                invalidateOptionsMenu();
            }
        };
    }

    private void prepareMenu(ArrayList<NavItem> mNavItems){
        mNavItems.add(new NavItem("Posts", "Postovi", R.drawable.ic_action_post));
        mNavItems.add(new NavItem("Settigs", "Podesavanja", R.drawable.ic_action_settings));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuSetting:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.shareMenu:
                Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
