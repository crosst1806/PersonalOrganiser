package uk.co.thomas_cross.personalorganiser;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import uk.co.thomas_cross.personalorganiser.adapters.TabPagerAdapter;
import uk.co.thomas_cross.personalorganiser.entities.DiaryEntry;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.entities.ToDo;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.Person;
import uk.co.thomas_cross.personalorganiser.entities.UserId;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        InitialSetupDialog.InitialSetUpDialogListener {

    private static final String TAG = "PersonalOrganiser";

    private static final int SHOW_TIME_TABLE = 0;
    private static final int SHOW_DIARY = 1;
    private static final int SHOW_TO_DOS = 2;

    private int currentDisplay = SHOW_TIME_TABLE;

    private SimpleDateFormat timeStampStorageFormat
            = new SimpleDateFormat("yyyyMMddHHmmsss");

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 101;

    SimpleDateFormat diaryEntryCreationDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
    private FloatingActionButton fab;

    private TabPagerAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // We need permission to create and use the personalOrganiser.db in external storage

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String s = "Permission to create the Personal Organiser Database is required!";
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

        final TabLayout tabLayout =
                (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Timetable"));
        tabLayout.addTab(tabLayout.newTab().setText("Diary"));
        tabLayout.addTab(tabLayout.newTab().setText("To Dos"));

        final ViewPager viewPager =
                (ViewPager) findViewById(R.id.pager);

        adapter =  new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());


        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        );


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                currentDisplay = tab.getPosition();

                viewPager.setCurrentItem(currentDisplay);

                switch (currentDisplay) {
                    case SHOW_TIME_TABLE:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                PlannedActivity pa = generateNewPlannedActivity();
                                showPlannedActivityCrudDialog(PlannedActivityCrudDialog.CREATE_MODE, pa);
                            }
                        });
                        break;
                    case SHOW_DIARY:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DiaryEntry de = new DiaryEntry();
                                de.setOwner(1);
                                de.setOwnerType(Ownable.PERSON);
                                de.setDateTime(diaryEntryCreationDateFormat.format(new Date()));
                                de.setTextEntry("");
                                de.setTimeStamp(timeStampStorageFormat.format(new Date()));
                                de.setLastModifiedBy(1);

                                showDiaryEntryCrudDialog(DiaryEntryCrudDialog.CREATE_MODE, de);
                            }
                        });
                        break;
                    case SHOW_TO_DOS:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ToDo newToDo = new ToDo();
                                newToDo.setOwner(1);
                                newToDo.setOwnerType(Ownable.PERSON);
                                newToDo.setTimeStamp(timeStampStorageFormat.format(new Date()));
                                newToDo.setLastModifiedBy(1);
                                showToDoCrudDialog(ToDoCrudDialog.CREATE_MODE, newToDo);
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

                PlannedActivity pa = generateNewPlannedActivity();
                showPlannedActivityCrudDialog(PlannedActivityCrudDialog.CREATE_MODE, pa);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void showInitialSetUpDialog() {
        FragmentManager fm = getSupportFragmentManager();
        InitialSetupDialog dialogFragment = new InitialSetupDialog();
        dialogFragment.show(fm, "initial_setup");
    }

    private int peopleCount() {
        POModel personsPOModel = POModel.getInstance(this);
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

        if (id == R.id.action_filter_by_role) {
            showFilterByRoleDialog();
            return true;
        }

        if ( id == R.id.action_filter_by_location){
            showFilterByLocationDialog();
            return true;
        }

        if (id == R.id.action_select_date) {
            DatePickerDialog dialog = initialiaseCalendarDialog();
            dialog.show();
            return true;
        }

        if (id == R.id.action_exit) {
            finish();
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

            Intent intent = new Intent(this, DisplayRoles.class);
            startActivity(intent);

        } else if (id == R.id.data_sensitivitys) {

            Intent intent = new Intent(this, DisplayDataSensitivitys.class);
            startActivity(intent);

        } else if (id == R.id.locations) {

            Intent intent = new Intent(this, DisplayLocations.class);
            startActivity(intent);

        } else if (id == R.id.activitys) {

            Intent intent = new Intent(this, DisplayActivitys.class);
            startActivity(intent);

        } else if ( id == R.id.activity_iterations ){

            Intent intent = new Intent(this, DisplayActivityIterations.class);
            startActivity(intent);

        } else if ( id == R.id.activity_sequences ){

            Intent intent = new Intent(this, DisplayActivitySequences.class);
            startActivity(intent);

        } else if ( id == R.id.sequence_iterations ){

//            Intent intent = new Intent(this, DisplayActivityIterations.class);
//            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        POModel poModel = POModel.getInstance(this);

        // Now add the person details we have got to date.
        // The first person in the model is the owner of the
        // personal organiser.

//        person.setMiddleNames("santa"); NULL PROBLEM ?
        final int personId = (int) poModel.addPerson(person);
//        Log.i(TAG, "personId is " + personId);

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
        userId.setOwner(person.getDatabaseRecordNo());
        userId.setOwnerType(Ownable.PERSON);
        userId.setLastModifiedBy(userid);   // self referential
        poModel.updateUserId(userId);

        // Now update the
    }

    @Override
    public void onDialogNegativeClick(Person p, UserId userId) {
//        Log.i(TAG, "onDialogNegativeClick()");
        finish();
    }

    private void showToDoCrudDialog(int mode, ToDo toDo) {
        FragmentManager fm = getSupportFragmentManager();
        ToDoCrudDialog dialogFragment = ToDoCrudDialog.newInstance(mode, toDo);
        dialogFragment.show(fm, "crud_form_to_do");
    }

    private void showPlannedActivityCrudDialog(int mode, PlannedActivity pa) {
        FragmentManager fm = getSupportFragmentManager();
        PlannedActivityCrudDialog dialogFragment = PlannedActivityCrudDialog.newInstance(mode, pa);
        dialogFragment.show(fm, "crud_form_planned_activity");
    }

    private void showDiaryEntryCrudDialog(int mode, DiaryEntry de) {
        FragmentManager fm = getSupportFragmentManager();
        DiaryEntryCrudDialog dialogFragment = DiaryEntryCrudDialog.newInstance(mode, de);
        dialogFragment.show(fm, "crud_form_diary_entry");
    }

    private void showFilterByRoleDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterRolesDialog filterRolesDialog = FilterRolesDialog.newInstance();
        filterRolesDialog.show(fm, "filter_roles");
    }

    private void showFilterByLocationDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterByLocationDialog dialog = FilterByLocationDialog.newInstance();
        dialog.show(fm,"tag");
    }


    private Calendar currentDate = Calendar.getInstance();

    private DatePickerDialog initialiaseCalendarDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,

                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        currentDate.set(year,monthOfYear,dayOfMonth);
                        doSomething(currentDate);
                        adapter.refresh(currentDate);
                    }

                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                                                        currentDate.get(Calendar.DAY_OF_MONTH));

        return datePickerDialog;
    }

    private void doSomething(Calendar date){
        POModel model = POModel.getInstance(this);
        model.setCurrentDate(date);

    }


    private SimpleDateFormat endDateStorageFormat = new SimpleDateFormat("dd-MMM-yyyy  HH:mm");
    private SimpleDateFormat startDateStorageFormat = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat startTimeStorageFormat = new SimpleDateFormat("HH:mm");

    /**
     * Generates a newly created and blank planned activity for the fab
     * button.
     *
     * @return the newly generated planned activity
     */
    private PlannedActivity generateNewPlannedActivity() {
        PlannedActivity pa = new PlannedActivity();
        pa.setOwner(1);
        pa.setOwnerType(Ownable.PERSON);
        pa.setPriority(3);
        Calendar calendar = Calendar.getInstance();
        pa.setStartDate(startDateStorageFormat.format(calendar.getTime()));
        pa.setStartTime(startTimeStorageFormat.format(calendar.getTime()));
        calendar.add(Calendar.MINUTE, 60);
        pa.setEndDateTime(endDateStorageFormat.format(calendar.getTime()));
        pa.setLastModifiedBy(1);
        return pa;
    }

}
