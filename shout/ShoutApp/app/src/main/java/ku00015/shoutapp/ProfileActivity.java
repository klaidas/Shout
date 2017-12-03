package ku00015.shoutapp;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ProfileActivity extends AppCompatActivity {

    static final String session = "LoginSession";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(R.layout.add_activity_actionbar);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setElevation(0);
        }
        setContentView(R.layout.activity_profile);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        TabPagerAdapter tabPagerAdapter;
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        if(tabLayout != null && viewPager != null) {
            tabLayout.addTab(tabLayout.newTab().setText("Posts"));
            tabLayout.addTab(tabLayout.newTab().setText("Comments"));

            tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(tabPagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) { }

                @Override
                public void onTabReselected(TabLayout.Tab tab) { }
            });
        }
    }

    /******************************************************
     *                   Action Bar Menu
     ******************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                return true;
            case R.id.action_logout:
                SharedPreferences sharedPreferences = getSharedPreferences(session, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                finishAfterTransition();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }


}
