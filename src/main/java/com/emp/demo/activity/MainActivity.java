package com.emp.demo.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;


import com.emp.demo.fragments.FragmentDrawer;
import com.emp.demo.R;
import com.emp.demo.adapters.PagerAdapter;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    public ViewPager viewPager;
    private Toolbar mToolbar;
    FrameLayout ly;
    private FragmentDrawer drawerFragment;

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//OneSignal.startInit(this).init();
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ly = (FrameLayout) findViewById(R.id.container_body);
        ly.setVisibility(View.GONE);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Featured"));
        tabLayout.addTab(tabLayout.newTab().setText("Channels"));
        tabLayout.addTab(tabLayout.newTab().setText("Series"));
        getSupportActionBar().setElevation(0);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.setVisibility(View.VISIBLE);
                ly.setVisibility(View.GONE);
                viewPager.setCurrentItem(tab.getPosition());
                //mToolbar.setTitle(tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        //displayView(0);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        loadSearchUi(menu);
        return true;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
        ly.setVisibility(View.GONE);
        viewPager.setCurrentItem(position);
        viewPager.setVisibility(View.VISIBLE);
    }

    private void displayView(int position) {
        switch (position) {
            case 0:
                loadAssetCarouselActivity("Movies", "/content/asset?fieldSet=ALL&publicationQuery=publications.products:EnigmaFVOD_enigma&includeUserData=true&pageNumber=1&sort=originalTitle&pageSize=100&onlyPublished=true&assetType=MOVIE");
                break;
            case 1:
                loadAssetCarouselActivity("Documentaries", "/content/asset?fieldSet=ALL&publicationQuery=publications.products:EnigmaFVOD_enigma&includeUserData=true&pageNumber=1&sort=originalTitle&pageSize=100&onlyPublished=true&assetType=MOVIE");
                break;
            case 2:
                loadAssetCarouselActivity("Kids", "/content/asset?fieldSet=ALL&publicationQuery=publications.products:kidsContent_enigma&includeUserData=true&pageNumber=1&sort=originalTitle&pageSize=100&onlyPublished=true&assetType=MOVIE");
                break;
            case 3:
                loadAssetCarouselActivity("Clips", "/content/asset?fieldSet=ALL&&includeUserData=true&pageNumber=1&sort=originalTitle&pageSize=100&onlyPublished=true&assetType=CLIP");
                break;
            case 4:
                loadAssetCarouselActivity("Recently Watched", "/userplayhistory/lastviewed?fieldSet=ALL");
                break;
            case 5:
                loadDownloadsActivity();
                break;
            case 6:
                rateme();
                break;
            case 7:
                share();
                break;
            case 8:
                logout();
                break;

            default:
                break;
        }
    }


    public void loadDownloadsActivity() {
        Intent intent = new Intent(this, MyDownloads.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void loadAssetCarouselActivity(String title, String endpoint) {
        Intent intent = new Intent(this, GenericAssetCarousel.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("title", title);
        intent.putExtra("endpoint", endpoint);
        startActivity(intent);
    }

    public void logout() {
        finish();
    }

    public void more() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=%1$s"));
        startActivity(intent);
        Log.i("Code2care ", "Cancel button Clicked!");
    }

    public void rateme() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=%1$s"));
        startActivity(intent);
        Log.i("Code2care ", "Cancel button Clicked!");
    }

    public void share() {
        String message = "https://play.google.com/store/apps/details?id=%1$s";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, "Share the link of News Point"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        //if (id == R.id.search) {
        //    showDdisclaimerdialog();
        //    return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mToolbar != null && mToolbar.getMenu() != null) {
            final MenuItem searchMenuItem = mToolbar.getMenu().findItem(R.id.search);
            if(searchMenuItem != null) {
                searchMenuItem.collapseActionView();
            }
        }
    }

    private void loadSearchUi(Menu menu) {
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconified(false);
        searchView.clearFocus();
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchView.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.setQuery("", false);
                searchView.clearFocus();
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchMenuItem.collapseActionView();
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMenuItem.collapseActionView();
                Intent intent = new Intent(getApplicationContext(), SearchResults.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }
}


