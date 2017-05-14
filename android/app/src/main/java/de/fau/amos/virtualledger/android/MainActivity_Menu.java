package de.fau.amos.virtualledger.android;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.fau.amos.virtualledger.R;
import de.fau.amos.virtualledger.android.api.Restapi;
import de.fau.amos.virtualledger.android.api.model.LogoutApiModel;
import de.fau.amos.virtualledger.android.api.model.StringApiModel;
import de.fau.amos.virtualledger.android.menu.adapter.MenuAdapter;
import de.fau.amos.virtualledger.android.menu.model.ItemSlidingMenu;
import de.fau.amos.virtualledger.android.model.UserCredential;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.util.Log;

/**
 * Created by Simon on 13.05.2017.
 */

public class MainActivity_Menu extends AppCompatActivity {

    /**
     *
     */
    @Inject
    Retrofit retrofit;


    private List<ItemSlidingMenu> slidingItems;
    private MenuAdapter menuAdapter;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private RelativeLayout content;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        //init
        init();

        //items for slide list
        configureItemsForMenu();

        //set Menu-Icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //title -- use if a title is necessary
        //setTitle(slidingItems.get(0).getImageTitle());

        //select
        listView.setItemChecked(0, true);

        //Close sliding menu
        drawerLayout.closeDrawer(listView);

        //starting fragment -- todo: if necessary add the start fragment here
        //replaceFragment(0);

        //click on items
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> root, View view, int pos, long id) {
                        //title
                        setTitle(slidingItems.get(pos).getImageTitle());
                        //items selected
                        listView.setItemChecked(pos, true);

                        replaceFragment(pos);

                        //Close
                        drawerLayout.closeDrawer(listView);
                    }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {

            /**
             *
             * @param drawerView
             */
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            /**
             *
             * @param drawerView
             */
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        // add the Toggle as Listener
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    /**
     *@methodtype initialization
     */
    private void init() {
        listView = (ListView) findViewById(R.id.sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        content = (RelativeLayout)findViewById(R.id.content);
        slidingItems = new ArrayList<>();
    }

    /**
     *@methodtype initialization
     */
    private void configureItemsForMenu() {
        slidingItems.add(new ItemSlidingMenu(R.drawable.icon_logout, "Logout"));
        menuAdapter = new MenuAdapter(this, slidingItems);
        listView.setAdapter(menuAdapter);
    }

    /**
     *
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    /**
     *
     * @param pos
     */
    private void replaceFragment(int pos) {
        Fragment fragment = null;
        switch (pos) {
            case 0:
                executeNextActivity();
                break;
            //new Fragments can be added her
            case 1:
                fragment = new Fragment();
                openFragment(fragment);
            default:
                fragment = new Fragment();
                openFragment(fragment);
                break;
        }

    }

    /**
     *
     * @param fragment
     */
    private void openFragment(Fragment fragment) {
        if(null!=fragment) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     *
     */
    public void executeNextActivity(){
        logout();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     *
     */
    public void logout() {
        String email = ((EditText) findViewById(R.id.Email)).getText().toString();
        retrofit2.Call<StringApiModel> responseMessage = retrofit.create(Restapi.class).logout(new LogoutApiModel(email));


        responseMessage.enqueue(new Callback<StringApiModel>() {
            @Override
            public void onResponse(retrofit2.Call<StringApiModel> call, Response<StringApiModel> response) {
                if(response.isSuccessful()) {
                    //Todo: change to Login Screen
                    /*Intent intent = new Intent(RegisterActivity.this, MainActivity_Menu.class);
                    startActivity(intent);*/

                } else if(response.code() == 400)
                { // code for sent data were wrong
                    Log.v("Error!","Logout not successful, wrong data sent");
                }
            }


            @Override
            public void onFailure(retrofit2.Call<StringApiModel> call, Throwable t) {

                Log.v("Error!","Can't connect to Endpoint");
            }
        });
    }

}
