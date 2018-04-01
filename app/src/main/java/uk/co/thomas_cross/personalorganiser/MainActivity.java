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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.Person;
import uk.co.thomas_cross.personalorganiser.entities.UserId;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InitialSetupDialogFragment.InitialSetUpDialogListener {

    private static final String TAG = "PersonalOrganiser";

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 101;
    private static final int REQUEST_ROLES_CODE = 102;
    private static final int REQUEST_DATA_SENSITIVITYS_CODE = 103;
    private static final int REQUEST_LOCATIONS_CODE = 104;
    private static final int REQUEST_TO_DOS_CODE = 105;

    SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:sss");
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout =
                (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Timetable"));
        tabLayout.addTab(tabLayout.newTab().setText("Diary"));
        tabLayout.addTab(tabLayout.newTab().setText("To Dos"));

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
                switch (tab.getPosition()) {
                    case 0:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar.make(view, "Add new Planned Activity", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
                        break;
                    case 1:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar.make(view, "Add new Diary Entry", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
                        break;
                    case 2:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar.make(view, "Add new To Do", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new Planned Activity", Snackbar.LENGTH_LONG)
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

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String s = "Permission to create the Personal Organiser model is required!";
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

            }

        } else {

            // The first and only record in the persons table will be the owner of
            // this personal organiser. If the record does not exist then we
            // need to request creation of this record by the user.

            if (peopleCount() == 0) {
                showInitialSetUpDialog();
            }

        }


    }

    private void showInitialSetUpDialog() {
        FragmentManager fm = getSupportFragmentManager();
        InitialSetupDialogFragment dialogFragment = new InitialSetupDialogFragment();
        dialogFragment.show(fm, "initial_setup");
    }

    private int peopleCount() {
        POModel personsPOModel = new POModel(this);
        ArrayList<Person> people = personsPOModel.getPersons();
        return people.size();
    }


    protected void makeRequest() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        String message = "empty";
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    message = "Personal Organiser model creation denied.";
                } else {
                    message = "Personal Organiser model creation allowed.";
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

            Intent intent = new Intent(this, DisplayDataSensitivitysActivity.class);
            startActivityForResult(intent, REQUEST_DATA_SENSITIVITYS_CODE);

        } else if (id == R.id.locations) {

            Intent intent = new Intent(this, DisplayLocationsActivity.class);
            startActivityForResult(intent, REQUEST_LOCATIONS_CODE);

        } else if (id == R.id.to_dos) {

            Intent intent = new Intent(this, DisplayToDosActivity.class);
            startActivityForResult(intent, REQUEST_TO_DOS_CODE);

        } else if (id == R.id.activitys) {

            Intent intent = new Intent(this, DisplayActivitysActivity.class);
            startActivity(intent);

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
            case REQUEST_DATA_SENSITIVITYS_CODE:
                if (result_code == RESULT_OK) {
                    Snackbar.make(this.getCurrentFocus(), "Everything OK", Snackbar.LENGTH_LONG).setAction("Undo", null).show();
                } else {
                    Snackbar.make(this.getCurrentFocus(), "Everything not OK", Snackbar.LENGTH_LONG)
                            .setAction("Undo", null).show();
                }
                break;
            case REQUEST_LOCATIONS_CODE:
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
    public void onDialogPositiveClick(Person person, UserId userId) {

        if (person.getFirstName().length() == 0) {
            Snackbar.make(this.getCurrentFocus(), "No First Name", Snackbar.LENGTH_LONG).setAction("Undo", null).show();
            return;
        }
        if (person.getLastName().length() == 0) {
            Snackbar.make(this.getCurrentFocus(), "No Last Name", Snackbar.LENGTH_LONG).setAction("Undo", null).show();
            return;
        }
        if (userId.getUserName().length() < 5) {
            Log.i(TAG, "User Name must be at least 5 characters long.");
            return;
        }
        if (userId.getPassword().length() < 6) {
            Log.i(TAG, "Password must be at least 6 characters long.");
            return;
        }
        POModel poModel = new POModel(this);

        // Now add the person details we have got to date.
        // The first person in the model is the owner of the
        // personal organiser.

//        person.setMiddleNames("santa"); NULL PROBLEM ?
        final int personId = (int) poModel.addPerson(person);
        Log.i(TAG, "personId is " + personId);

        // add the user id details that we have
        userId.setOwner((int) personId);
        userId.setOwnerType(Ownable.PERSON);
        userId.setDataSensitivity(0);
        userId.setLastModifiedBy(1);
        final int userid = (int) poModel.addUserId(userId);
        Log.i(TAG, "user id is " + userid);

        // fetch back the person record that now has an id
        person = poModel.getPerson(personId);
        person.setOwner(person.getDatabaseRecordNo()); // person owns itself
        person.setOwnerType(Ownable.PERSON);
        person.setUserId(userid);       // user id is the one we just added
        person.setLastModifiedBy(userid);
        poModel.updatePerson(person);

        // Now fetch back the user id to update details
        userId = poModel.getUserId(userid);
        Log.i(TAG, "User Id is " + userId.toString());
        userId.setOwner(person.getDatabaseRecordNo());
        userId.setOwnerType(Ownable.PERSON);
        userId.setLastModifiedBy(userid);   // self referential
        poModel.updateUserId(userId);

        // Now update the
    }

    @Override
    public void onDialogNegativeClick(Person p, UserId userId) {
        Log.i(TAG, "onDialogNegativeClick()");
        finish();
    }

}
