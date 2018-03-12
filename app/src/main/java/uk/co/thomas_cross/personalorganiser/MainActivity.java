package uk.co.thomas_cross.personalorganiser;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.database.PersonsDBFrontEnd;
import uk.co.thomas_cross.personalorganiser.entities.Person;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InitialSetupDialogFragment.InitialSetUpDialogListener {

    private static final String TAG = "PersonalOrganiser";

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 101;
    private static final int REQUEST_ROLES_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout =
                (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Daily Schedule"));
        tabLayout.addTab(tabLayout.newTab().setText("Page A Day Diary"));

        final ViewPager viewPager =
                (ViewPager) findViewById(R.id.pager);

        final PagerAdapter adapter =
                new TabPagerAdapter(getSupportFragmentManager(),
                        tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        );


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // We need permission to create and use the personalOrganiser.db in external storage

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            Log.i(TAG, "Permission to write to external storage denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Log.i(TAG, "shouldShowRationale()");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String s = "Permission to create the Personal Organiser database is required!";
                builder.setMessage(s);
                builder.setTitle("Permission Required");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        makeRequest();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                makeRequest();
                Log.i(TAG, "shouldNotShowRationale()");

            }

        } else {

            Log.i(TAG, "Permission to write to external storage granted");
//            makeRequest();

        }

        // The first and only record in the persons table will be the owner of
        // this personal organiser. If the record does not exist then we
        // need to request creation of this record by the user.

        if (peopleCount() == 0) {
            Log.i(TAG, "people count is " + peopleCount());
            showInitialSetUpDialog();
        }
        Log.i(TAG, "people count is " + peopleCount());


    }

    private void showInitialSetUpDialog() {
        FragmentManager fm = getSupportFragmentManager();
        InitialSetupDialogFragment dialogFragment = new InitialSetupDialogFragment();
        dialogFragment.show(fm, "initial_setup");
    }

    private int peopleCount() {
        PersonsDBFrontEnd personsDBFrontEnd = new PersonsDBFrontEnd(this);
        ArrayList<Person> people = personsDBFrontEnd.getPersons();
        return people.size();
    }

    protected void makeRequest() {

//        Log.i(TAG,"makeRequest()");

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        String message = "empty";
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    message = "Personal Organiser database creation denied.";
                } else {
                    message = "Personal Organiser database creation allowed.";
                }
                Snackbar.make(this.getCurrentFocus(), message, Snackbar.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.roles) {

            Intent intent = new Intent(this, DisplayRolesActivity.class);
            startActivityForResult(intent, REQUEST_ROLES_CODE);

            // Handle the camera action
        } else if (id == R.id.data_sensitivitys) {

        } else if (id == R.id.locations) {

        } else if (id == R.id.activitys) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int request_code,
                                    int result_code,
                                    Intent data) {
        switch (request_code) {
            case REQUEST_ROLES_CODE:
                if (result_code == RESULT_OK) {
                    Snackbar.make(this.getCurrentFocus(), "Everything OK", Snackbar.LENGTH_LONG).setAction("Undo", null).show();
                } else {
                    Snackbar.make(this.getCurrentFocus(), "Everything not OK", Snackbar.LENGTH_LONG)
                            .setAction("Undo", null).show();
                }
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(Person person) {

        if (person.getFirstName().length() == 0) {
            Log.i(TAG, "onDialogPositiveClick() - no firstName");
            return;
        }
        if (person.getLastName().length() == 0) {
            Log.i(TAG, "onDialogPositiveClick() - no lastName");
            return;
        }
        PersonsDBFrontEnd dbFrontEnd = new PersonsDBFrontEnd(this);
        dbFrontEnd.addPerson(person);
        Log.i(TAG, "onDialogPositiveClick() " + person.toString() + " has been added!");

    }

    @Override
    public void onDialogNegativeClick() {
        Log.i(TAG, "onDialogNegativeClick()");
        finish();
    }
}
