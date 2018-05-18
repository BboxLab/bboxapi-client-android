package fr.lab.bbox.bboxapirunner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import fr.lab.bbox.bboxapirunner.application.ApplicationFragment;
import fr.lab.bbox.bboxapirunner.media.MediaFragment;
import fr.lab.bbox.bboxapirunner.notification.NotificationFragment;
import fr.lab.bbox.bboxapirunner.security.SecurityFragment;
import fr.lab.bbox.bboxapirunner.ui.UserInterfaceFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        };
        ;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.replace(R.id.container, new BboxFragment()).commit();

        BboxIpAddrDefault();

        View header = navigationView.getHeaderView(0);
        TextView id = (TextView) header.findViewById(R.id.app_id);
        id.setText("app_id : " + getString(R.string.APP_ID));
        TextView secret = (TextView) header.findViewById(R.id.app_secret);
        secret.setText("app_secret : " + getString(R.string.APP_SECRET));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        switch (item.getItemId()) {
            case R.id.nav_security:
                mFragmentTransaction.replace(R.id.container, new SecurityFragment());
                break;

            case R.id.nav_application:
                mFragmentTransaction.replace(R.id.container, new ApplicationFragment());
                break;

            case R.id.nav_media:
                mFragmentTransaction.replace(R.id.container, new MediaFragment());
                break;

            case R.id.nav_userinterface:
                mFragmentTransaction.replace(R.id.container, new UserInterfaceFragment());
                break;

            case R.id.nav_notification:
                mFragmentTransaction.replace(R.id.container, new NotificationFragment());
                break;

            case R.id.nav_bbox:
                mFragmentTransaction.replace(R.id.container, new BboxFragment());
                break;
        }

        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void BboxIpAddrDefault() {
        String bboxip = "127.0.0.1";
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("bboxip", bboxip);
        editor.commit();
    }
}
