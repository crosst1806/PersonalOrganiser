package uk.co.thomas_cross.personalorganiser.model;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import uk.co.thomas_cross.personalorganiser.LocationAssociated;
import uk.co.thomas_cross.personalorganiser.adapters.ActivityArrayAdapter;
import uk.co.thomas_cross.personalorganiser.adapters.ActivityIterationArrayAdapter;
import uk.co.thomas_cross.personalorganiser.adapters.ActivitySequenceArrayAdapter;
import uk.co.thomas_cross.personalorganiser.adapters.ActivitySequenceListItemArrayAdapter;
import uk.co.thomas_cross.personalorganiser.adapters.DataSensitivityArrayAdapter;
import uk.co.thomas_cross.personalorganiser.adapters.DiaryEntryArrayAdapter;
import uk.co.thomas_cross.personalorganiser.adapters.LocationsArrayAdapter;
import uk.co.thomas_cross.personalorganiser.adapters.PlannedActivityArrayAdapter;
import uk.co.thomas_cross.personalorganiser.adapters.RoleArrayAdapter;
import uk.co.thomas_cross.personalorganiser.adapters.ToDoArrayAdapter;
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.ActivityIteration;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceList;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceListItem;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.DiaryEntry;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.Person;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.RoleAssociated;
import uk.co.thomas_cross.personalorganiser.entities.ToDo;
import uk.co.thomas_cross.personalorganiser.entities.UserId;
import uk.co.thomas_cross.personalorganiser.entities.tor.ActivitySequenceListItemTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.ActivitySequenceListTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.ActivityTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.DataSensitivityTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.DiaryEntryTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.LocationTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.MedianMinuteTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.PlannedActivityTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.RoleTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.ToDoTor;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

/**
 * Created by root on 26/12/17.
 */

public class POModel {

    private static POModel sInstance;

    private DBFrontEnd dbFrontEnd = null;
    private SimpleDateFormat timeStampFormat =
            UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_TimeStamp_Storage);
    private SimpleDateFormat diaryEntryCreationDateFormat
            = new SimpleDateFormat("dd MMM yyyy HH:mm");

    private ToDoTor toDoTor = new ToDoTor();
    private DataSensitivityTor dataSensitivityTor = new DataSensitivityTor();
    private RoleTor roleTor = new RoleTor();
    private LocationTor locationTor = new LocationTor();
    private ActivityTor activityTor = new ActivityTor();
    private PlannedActivityTor plannedActivityTor = new PlannedActivityTor();
    private DiaryEntryTor diaryEntryTor = new DiaryEntryTor();
    private ActivitySequenceListTor activitySequenceListTor = new ActivitySequenceListTor();
    private ActivitySequenceListItemTor activitySequenceListItemTor = new ActivitySequenceListItemTor();

    private ArrayList<RoleStatus> filteredRoles = new ArrayList<RoleStatus>();
    private ArrayList<LocationStatus> filteredLocations = new ArrayList<LocationStatus>();

    private ActivityIterationArrayAdapter activityIterationArrayAdapter = null;
    private ActivityArrayAdapter activityArrayAdapter = null;
    private PlannedActivityArrayAdapter plannedActivityArrayAdapter = null;
    private DiaryEntryArrayAdapter diaryEntryArrayAdapter = null;
    private LocationsArrayAdapter locationsArrayAdapter = null;
    private ToDoArrayAdapter toDoArrayAdapter = null;
    private DataSensitivityArrayAdapter dataSensitivityArrayAdapter = null;
    private RoleArrayAdapter roleArrayAdapter = null;
    private ActivitySequenceArrayAdapter activitySequenceArrayAdapter = null;
    private ActivitySequenceListItemArrayAdapter activitySequenceListItemArrayAdapter = null;

    private Calendar currentDate = Calendar.getInstance();

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private POModel(Context context) {

        dbFrontEnd = DBFrontEnd.getInstance(context);
        ArrayList roles = getRoles();
        Collections.sort(roles, roleTor);
        for (int r = 0; r < roles.size(); r++) {
            Role role = (Role) roles.get(r);
            RoleStatus rs = new RoleStatus(role, true);
            filteredRoles.add(rs);
        }
        ArrayList locations = getLocations();
        Collections.sort(locations, locationTor);
        for (int l = 0; l < locations.size(); l++) {
            Location location = (Location) locations.get(l);
            LocationStatus ls = new LocationStatus(location, true);
            filteredLocations.add(ls);
        }

    }

    public static synchronized POModel getInstance(Context context) {
        // Use the application context, which will ensure you dont
        // accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new POModel(context);
        }
        return sInstance;
    }

    public Calendar getCurrentDate() {
        return this.currentDate;
    }

    public void setCurrentDate(Calendar calendar) {

        this.currentDate = calendar;
        refreshPlannedActivityArrayAdapter();
        refreshDiaryEntryArrayAdapter();
    }

    public String getUserName(int userId) {

        UserId userId1 = dbFrontEnd.getUserId(userId);
        if (userId1 == null) {
            return "Not Found";
        }
        return userId1.getUserName();
    }

    public String getUserTitle(int owner, int ownerType) {

        String title = "Owner Type Not Found";
        switch (ownerType) {
            case Ownable.NONE:
                title = "None";
                break;
            case Ownable.PERSON:
                Person p = dbFrontEnd.getPerson(owner);
                title = p.getFirstName() + " " + p.getLastName();
                break;
            case Ownable.GROUP:
                title = "Not Yet Implemented";
                break;
            case Ownable.TEAM:
                title = "Not yet Implemented";
                break;
            case Ownable.ORGANISATION:
                title = "Not Yet Implemented";
                break;
        }
        return title;
    }

    public String getOwnerType(int ownerType) {

        String title = "Owner Type Not Found";
        switch (ownerType) {
            case Ownable.NONE:
                title = "None";
                break;
            case Ownable.PERSON:
                title = "Person";
                break;
            case Ownable.GROUP:
                title = "Group";
                break;
            case Ownable.TEAM:
                title = "Team";
                break;
            case Ownable.ORGANISATION:
                title = "organisation";
                break;
        }
        return title;
    }

    public ArrayList<Person> getPersons() {
        return dbFrontEnd.getPersons();
    }

    public long addPerson(Person person) {
        return dbFrontEnd.addPerson(person);
    }

    public Person getPerson(int id) {
        return dbFrontEnd.getPerson(id);
    }

    public long updatePerson(Person person) {
        return dbFrontEnd.updatePerson(person);
    }

    public void deletePerson(int personID) {
        dbFrontEnd.deletePerson(personID);
    }

    public ArrayList<Role> getRoles() {

        ArrayList<Role> roles = dbFrontEnd.getRoles();

        Person p = dbFrontEnd.getPerson(1);
        Role personal = new Role();
        personal.setOwner(p.getDatabaseRecordNo());
        personal.setOwnerType(Ownable.PERSON);
        personal.setTitle(p.getFirstName() + " " + p.getLastName());
        personal.setTimeStamp(timeStampFormat.format(new Date()));
        personal.setLastModifiedBy(1);
        roles.add(personal);

        return roles;
    }

    public long addRole(Role role) {
        long id = dbFrontEnd.addRole(role);
        refreshRoleArrayAdapter();
        return id;
    }

    public Role getRole(int id) {

        if (id == 0) {

            Person p = getPerson(1);
            if (p == null) {
                p = new Person();
                p.setFirstName("Mickey");
                p.setLastName("Mouse");
            }

            Role personal = new Role();
            personal.setOwner(1);
            personal.setOwnerType(Ownable.PERSON);
            personal.setTitle(p.getFirstName() + " " + p.getLastName());
            personal.setTimeStamp(timeStampFormat.format(new Date()));
            personal.setLastModifiedBy(1);
            return personal;

        } else {

            return dbFrontEnd.getRole(id);

        }


    }

    public long updateRole(Role role) {

        long id = dbFrontEnd.updateRole(role);
        refreshRoleArrayAdapter();
        return id;
    }

    public void deleteRole(int roleID) {

        dbFrontEnd.deleteRole(roleID);
        refreshRoleArrayAdapter();
    }

    public UserId getUserId(int id) {
        return dbFrontEnd.getUserId(id);
    }

    public long addUserId(UserId userId) {
        return dbFrontEnd.addUserId(userId);
    }

    public long updateUserId(UserId userId) {
        return dbFrontEnd.updateUserId(userId);
    }

    public void deleteUserId(int userId) {
        dbFrontEnd.deleteUserId(userId);
    }

    public ArrayList<DataSensitivity> getDataSensitivitys() {

        ArrayList<DataSensitivity> dataSensitivities =
                dbFrontEnd.getDataSensitivitys();

        DataSensitivity privateDS = new DataSensitivity();
        privateDS.setOwner(1);
        privateDS.setOwnerType(Ownable.PERSON);
        privateDS.setTitle("Private");
        dataSensitivities.add(privateDS);

        return dataSensitivities;
    }

    public long addDataSensitivity(DataSensitivity dataSensitivity) {
        long id = dbFrontEnd.addDataSensitivity(dataSensitivity);
        refreshDataSensitivityArraAdapter();
        return id;
    }

    public DataSensitivity getDataSensitivity(int id) {

        DataSensitivity ds = null;

        if (id == 0) {

            DataSensitivity privateDS = new DataSensitivity();
            privateDS.setOwner(1);
            privateDS.setOwnerType(Ownable.PERSON);
            privateDS.setTitle("Private");
            ds = privateDS;

        } else {
            ds = dbFrontEnd.getDataSensitivity(id);
        }

        return ds;
    }

    public long updateDataSensitivity(DataSensitivity ds) {
        long id = dbFrontEnd.updateDataSensitivity(ds);
        refreshDataSensitivityArraAdapter();
        return id;
    }

    public void deleteDataSensitivity(int dsId) {
        dbFrontEnd.deleteDataSensitivity(dsId);
        refreshDataSensitivityArraAdapter();
    }

    public ArrayList<Location> getLocations() {

        ArrayList<Location> locations = dbFrontEnd.getLocations();

        Location noLocation = new Location();
        noLocation.setOwner(1);
        noLocation.setOwnerType(Ownable.PERSON);
        noLocation.setTitle("Non Specific");
        locations.add(noLocation);

        return locations;
    }

    public long addLocation(Location location) {
        long id = dbFrontEnd.addLocation(location);
        refreshLocationsArrayAdapter();
        return id;
    }

    public Location getLocation(int id) {

        Location location = null;
        if (id == 0) {

            Location noLocation = new Location();
            noLocation.setOwner(1);
            noLocation.setOwnerType(Ownable.PERSON);
            noLocation.setTitle("Non Specific");
            location = noLocation;

        } else {
            location = dbFrontEnd.getLocation(id);
        }
        return location;
    }

    public long updateLocation(Location location) {

        long id = dbFrontEnd.updateLocation(location);
        refreshLocationsArrayAdapter();
        return id;
    }

    public void deleteLocation(int id) {

        dbFrontEnd.deleteLocation(id);
        refreshLocationsArrayAdapter();
    }

    public ArrayList<ToDo> getToDos() {

        ArrayList<ToDo> toDos = dbFrontEnd.getToDos();

        ArrayList<RoleAssociated> ras = filterRoleAssociated(toDos);
        toDos.clear();
        for (RoleAssociated object : ras) {
            toDos.add((ToDo) object);
        }

        ArrayList<LocationAssociated> las = filterLocationAssociated(toDos);
        toDos.clear();
        for (LocationAssociated object : las) {
            toDos.add((ToDo) object);
        }

        return toDos;
    }

    public long addToDo(ToDo toDo) {
        long id = dbFrontEnd.addToDo(toDo);
        refreshToDoArrayAdapter();
        return id;
    }

    public ToDo getToDo(int id) {
        return dbFrontEnd.getToDo(id);
    }

    public long updateToDo(ToDo toDo) {
        long id = dbFrontEnd.updateToDo(toDo);
        refreshToDoArrayAdapter();
        return id;
    }

    public void deleteToDo(int id) {
        dbFrontEnd.deleteToDo(id);
        refreshToDoArrayAdapter();
    }

    public ArrayList<Activity> getActivitys() {

        ArrayList<Activity> activitys = dbFrontEnd.getActivitys();

        ArrayList<RoleAssociated> ras = filterRoleAssociated(activitys);
        activitys.clear();
        for (RoleAssociated object : ras) {
            activitys.add((Activity) object);
        }

        ArrayList<LocationAssociated> las = filterLocationAssociated(activitys);
        activitys.clear();
        for (LocationAssociated object : las) {
            activitys.add((Activity) object);
        }

        ActivityTor activityTor = new ActivityTor();
        Collections.sort(activitys, activityTor);
        return activitys;
    }

    public ArrayList<Activity> getActivitysByRole(int roleId) {

        Activity activity = new Activity();
        activity.setDescription("No Activity Selected");
        activity.setOwner(1);
        activity.setOwnerType(Ownable.PERSON);
        activity.setLastModifiedBy(1);
        ArrayList<Activity> activitys = dbFrontEnd.getActivitysByRole(roleId);
        activitys.add(activity);
        ArrayList<RoleAssociated> ras = filterRoleAssociated(activitys);
        activitys.clear();
        for (RoleAssociated object : ras) {
            activitys.add((Activity) object);
        }

        ArrayList<LocationAssociated> las = filterLocationAssociated(activitys);
        activitys.clear();
        for (LocationAssociated object : las) {
            activitys.add((Activity) object);
        }

        ActivityTor activityTor = new ActivityTor();
        Collections.sort(activitys, activityTor);
        return activitys;
    }

    // SPECIAL METHODS

    public ArrayList<Activity> getActivitysByRoleAndLocation(int roleId,
                                                             int locationId) {
        Activity activity = new Activity();
        activity.setOwner(1);
        activity.setOwnerType(Ownable.PERSON);
        activity.setDescription("             ");
        activity.setLastModifiedBy(1);

        ArrayList<Activity> activitys = dbFrontEnd.getActivitysByRole(roleId, locationId);

        activitys.add(activity);
        ArrayList<RoleAssociated> ras = filterRoleAssociated(activitys);
        activitys.clear();
        for (RoleAssociated object : ras) {
            activitys.add((Activity) object);
        }

        ArrayList<LocationAssociated> las = filterLocationAssociated(activitys);
        activitys.clear();
        for (LocationAssociated object : las) {
            activitys.add((Activity) object);
        }

        ActivityTor activityTor = new ActivityTor();
        Collections.sort(activitys, activityTor);
        return activitys;
    }

    public long addActivity(Activity activity) {

        long id = dbFrontEnd.addActivity(activity);
        refreshActivityArrayAdapter();
        return id;
    }

    public Activity getActivity(int id) {
        if (id == 0) {
            Activity activity = new Activity();
            activity.setOwner(1);
            activity.setOwnerType(Ownable.PERSON);
            activity.setDescription("             ");
            activity.setLastModifiedBy(1);
            return activity;
        }
        return dbFrontEnd.getActivity(id);
    }

    public long updateActivity(Activity activity) {

        long id = dbFrontEnd.updateActivity(activity);
        refreshActivityArrayAdapter();
        return id;
    }

    public void deleteActivity(int id) {
        dbFrontEnd.deleteActivity(id);
        refreshActivityArrayAdapter();
    }

    public ArrayList<DiaryEntry> getDiaryEntrys() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String startDate = sdf.format(currentDate.getTime());
        ArrayList<DiaryEntry> des = dbFrontEnd.getDiaryEntrys(startDate);

        ArrayList<RoleAssociated> ras = filterRoleAssociated(des);
        des.clear();
        for (RoleAssociated object : ras) {
            des.add((DiaryEntry) object);
        }

        ArrayList<LocationAssociated> las = filterLocationAssociated(des);
        des.clear();
        for (LocationAssociated object : las) {
            des.add((DiaryEntry) object);
        }

        return des;
    }

    public long addDiaryEntry(DiaryEntry diaryEntry) {

        diaryEntry.setDateTime(diaryEntryCreationDateFormat.format(new Date()));
        long id = dbFrontEnd.addDiaryEntry(diaryEntry);
        refreshDiaryEntryArrayAdapter();
        return id;

    }

    public DiaryEntry getDiaryEntry(int id) {

        return dbFrontEnd.getDiaryEntry(id);

    }

    public long updateDiary(DiaryEntry diaryEntry) {
        long id = dbFrontEnd.updateDiary(diaryEntry);
        refreshDiaryEntryArrayAdapter();
        return id;

    }

    public void deleteDiaryEntry(int id) {

        dbFrontEnd.deleteDiaryEntry(id);
        refreshDiaryEntryArrayAdapter();

    }

    public ArrayList<PlannedActivity> getPlannedActivitys() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String startDate = sdf.format(currentDate.getTime());

        ArrayList<PlannedActivity> pas = dbFrontEnd.getPlannedActivitys(startDate);

        ArrayList<RoleAssociated> ras = filterRoleAssociated(pas);
        pas.clear();
        for (RoleAssociated listObject : ras) {
            pas.add((PlannedActivity) listObject);
        }

        ArrayList<LocationAssociated> las = filterLocationAssociated(pas);
        pas.clear();
        for (LocationAssociated listObject : las) {
            pas.add((PlannedActivity) listObject);
        }


        Collections.sort(pas, plannedActivityTor);
        return pas;

    }

    public void enableActivityIteration(ActivityIteration iteration) {

        int increment = 0;
        switch (iteration.getFrequencyInterval()) {
            case ActivityIteration.MINUTES:
                increment = iteration.getFrequency();
                break;
            case ActivityIteration.HOURS:
                increment = iteration.getFrequency() * 60;
                break;
            case ActivityIteration.DAYS:
                increment = iteration.getFrequency() * 60 * 24;
                break;
            case ActivityIteration.WEEKS:
                increment = iteration.getFrequency() * 60 * 24 * 7;
                break;
            case ActivityIteration.MONTHS:
                increment = iteration.getFrequency() * 60 * 24 * 7 * 4;
                break;
            case ActivityIteration.YEARS:
                increment = iteration.getFrequency() * 60 * 24 * 7 * 4 * 12;
                break;
        }

        SimpleDateFormat yyyyMMdd = UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_yyyyMMdd);
        Calendar start = UtilityHelper.getCalendarFromStartDate(iteration.getStartDate());
        Calendar end = UtilityHelper.getCalendarFromStartDate(iteration.getEndDate());

        while (start.before(end)) {
            PlannedActivity pa = new PlannedActivity();
            pa.setOwner(iteration.getOwner());
            pa.setOwnerType(iteration.getOwnerType());
            pa.setRole(iteration.getRole());
            pa.setLocation(iteration.getLocation());
            pa.setActivity(iteration.getActivity());
            Activity a = getActivity(iteration.getActivity());
            pa.setDescription(a.getDescription());
            pa.setMedianMinutes(a.getMedianMinutes());
            pa.setPriority(iteration.getPriority());
            pa.setGeneratorType(PlannedActivity.ACTIVITY_ITERATION_CHILD);
            pa.setGeneratorId(iteration.getDatabaseRecordNo());
            pa.setStartDate(yyyyMMdd.format(start.getTime()));
            pa.setStartTime(iteration.getStartTime());
//            pa.setEndDateTime();
            pa.setStatus(PlannedActivity.PENDING);
            pa.setDataSensitivity(iteration.getDataSensitivity());
            pa.setLastModifiedBy(iteration.getLastModifiedBy());
            addPlannedActivity(pa);
            start.add(Calendar.MINUTE, increment);
        }

        refreshPlannedActivityArrayAdapter();

    }

    public void disableActivityIteration(ActivityIteration iteration){
        ArrayList<PlannedActivity> pas = dbFrontEnd.getPlannedActivitys(iteration);
        for (int i=0; i < pas.size(); i++ ){
            PlannedActivity pa = pas.get(i);
            if ( pa.getStatus() == PlannedActivity.PENDING )
                deletePlannedActivity(pa.getDatabaseRecordNo());
        }
    }

    public long addPlannedActivity(PlannedActivity plannedActivity) {
        long id = dbFrontEnd.addPlannedActivity(plannedActivity);
        refreshPlannedActivityArrayAdapter();
        return id;
    }

    public PlannedActivity getPlannedActivity(int id) {

        return dbFrontEnd.getPlannedActivity(id);

    }

    public long updatePlannedActivity(PlannedActivity plannedActivity) {
        long id = dbFrontEnd.updatePlannedActivity(plannedActivity);
        refreshPlannedActivityArrayAdapter();
        Activity activity = dbFrontEnd.getActivity(plannedActivity.getActivity());
        recalculateMedianMinutesForActivity(activity);
        return id;
    }

    public void deletePlannedActivity(int id) {

        PlannedActivity pa = dbFrontEnd.getPlannedActivity(id);
        Activity activity = dbFrontEnd.getActivity(pa.getActivity());
        dbFrontEnd.deletePlannedActivity(id);
        refreshPlannedActivityArrayAdapter();
        if (pa.getStatus() == PlannedActivity.COMPLETED)
            recalculateMedianMinutesForActivity(activity);
    }

    public ArrayList<ActivitySequenceList> getActivitySequenceLists(){
        ArrayList<ActivitySequenceList> sequences = dbFrontEnd.getActivitySequenceLists();
        return sequences;
    }

    public ActivitySequenceList getActivitySequenceList(int id){
        return dbFrontEnd.getActivitySequenceList(id);
    }

    public long addActivitySequenceList(ActivitySequenceList asl){
        long id = dbFrontEnd.addActivitySequenceList(asl);
        refreshActivitySequenceArrayAdapter();
        return id;
    }

    public long updateActivitySequenceList(ActivitySequenceList asl){
        long id = dbFrontEnd.updateActivitySequenceList(asl);
        refreshActivitySequenceArrayAdapter();
        return id;
    }

    public void deleteActivitySequenceList(int id){
        dbFrontEnd.deleteActivitySequenceList(id);
        refreshActivitySequenceArrayAdapter();
    }


    public ArrayList<ActivitySequenceListItem> getActivitySequenceListItems(){
        ArrayList<ActivitySequenceListItem> items = dbFrontEnd.getActivitySequenceListItems();
        return items;
    }

    public ActivitySequenceListItem getActivitySequenceListItem(int id){
        return dbFrontEnd.getActivitySequenceListItem(id);
    }

    public long addActivitySequenceListItem(ActivitySequenceListItem item){
        long id = dbFrontEnd.addActivitySequenceListItem(item);
        refreshActivitySequenceListItemArrayAdapter();
        return id;
    }

    public long updateActivitySequenceListItem(ActivitySequenceListItem item){
        long id = dbFrontEnd.updateActivitySequenceListItem(item);
        refreshActivitySequenceListItemArrayAdapter();
        return id;
    }

    public void deleteActivitySequenceListItem(int id){
        dbFrontEnd.deleteActivitySequenceListItem(id);
        refreshActivitySequenceListItemArrayAdapter();
    }


    private void recalculateMedianMinutesForActivity(Activity activity) {
        ArrayList pas = dbFrontEnd.getPlannedActivitys(activity);
        ArrayList<Integer> medianMinutes = new ArrayList<Integer>();
        for (int p = 0; p < pas.size(); p++) {
            PlannedActivity pa = (PlannedActivity) pas.get(p);
            if (pa.getStatus() == PlannedActivity.COMPLETED) {
                medianMinutes.add(new Integer(pa.getTimeTaken()));
            }

        }
        Collections.sort(medianMinutes, new MedianMinuteTor());
        int sampleSize = medianMinutes.size();
        Integer result = null;

        if (sampleSize == 0)
            return;

        if (sampleSize == 1) {
            result = medianMinutes.get(0);
        } else {
            result = medianMinutes.get(sampleSize / 2);
        }
        activity.setMedianMinutes(result.intValue());
        dbFrontEnd.updateActivity(activity);
    }

    public ToDoArrayAdapter getToDoArrayAdapter(Context context) {
        toDoArrayAdapter = new ToDoArrayAdapter(context, getToDos());
        toDoArrayAdapter.sort(toDoTor);
        return toDoArrayAdapter;
    }

    public void refreshToDoArrayAdapter() {
        if (toDoArrayAdapter == null)
            return;
        toDoArrayAdapter.clear();
        toDoArrayAdapter.addAll(getToDos());
        toDoArrayAdapter.sort(toDoTor);
        toDoArrayAdapter.notifyDataSetChanged();
    }

    public DataSensitivityArrayAdapter getDataSensitivityArrayAdapter(Context context) {
        dataSensitivityArrayAdapter = new DataSensitivityArrayAdapter(context, getDataSensitivitys());
        dataSensitivityArrayAdapter.sort(dataSensitivityTor);
        return dataSensitivityArrayAdapter;
    }

    private void refreshDataSensitivityArraAdapter() {
        if (dataSensitivityArrayAdapter == null)
            return;
        dataSensitivityArrayAdapter.clear();
        dataSensitivityArrayAdapter.addAll(getDataSensitivitys());
        dataSensitivityArrayAdapter.sort(dataSensitivityTor);
        dataSensitivityArrayAdapter.notifyDataSetChanged();
    }

    public RoleArrayAdapter getRoleArrayAdapter(Context context) {
        roleArrayAdapter = new RoleArrayAdapter(context, getRoles());
        roleArrayAdapter.sort(roleTor);
        return roleArrayAdapter;
    }

    public void refreshRoleArrayAdapter() {
        if (roleArrayAdapter == null)
            return;
        roleArrayAdapter.clear();
        roleArrayAdapter.addAll(getRoles());
        roleArrayAdapter.sort(roleTor);
        roleArrayAdapter.notifyDataSetChanged();
    }

    public LocationsArrayAdapter getLocationsArrayAdapter(Context context) {
        locationsArrayAdapter = new LocationsArrayAdapter(context, getLocations());
        locationsArrayAdapter.sort(locationTor);
        return locationsArrayAdapter;
    }

    public void refreshLocationsArrayAdapter() {
        if (locationsArrayAdapter == null)
            return;
        locationsArrayAdapter.clear();
        locationsArrayAdapter.addAll(getLocations());
        locationsArrayAdapter.sort(locationTor);
        locationsArrayAdapter.notifyDataSetChanged();
    }

    public ActivitySequenceArrayAdapter getActivitySequenceArrayAdapter(Context context){
        activitySequenceArrayAdapter = new ActivitySequenceArrayAdapter(context, getActivitySequenceLists());
        activitySequenceArrayAdapter.sort(activitySequenceListTor);
        return activitySequenceArrayAdapter;
    }


    public void refreshActivitySequenceArrayAdapter() {
        if (activitySequenceArrayAdapter == null)
            return;
        activitySequenceArrayAdapter.clear();
        activitySequenceArrayAdapter.addAll(getActivitySequenceLists());
        activitySequenceArrayAdapter.sort(activitySequenceListTor);
        activitySequenceArrayAdapter.notifyDataSetChanged();
    }

    public ActivitySequenceListItemArrayAdapter getActivitySequenceListItemArrayAdapter(Context context){
        activitySequenceListItemArrayAdapter = new ActivitySequenceListItemArrayAdapter(context, getActivitySequenceListItems());
        activitySequenceListItemArrayAdapter.sort(activitySequenceListItemTor);
        return activitySequenceListItemArrayAdapter;
    }


    public void refreshActivitySequenceListItemArrayAdapter() {
        if (activitySequenceListItemArrayAdapter == null)
            return;
        activitySequenceListItemArrayAdapter.clear();
        activitySequenceListItemArrayAdapter.addAll(getActivitySequenceListItems());
        activitySequenceListItemArrayAdapter.sort(activitySequenceListItemTor);
        activitySequenceListItemArrayAdapter.notifyDataSetChanged();
    }

    public ActivityArrayAdapter getActivityArrayAdapter(Context context) {
        activityArrayAdapter = new ActivityArrayAdapter(context, getActivitys());
        activityArrayAdapter.sort(activityTor);
        return activityArrayAdapter;
    }

    public void refreshActivityArrayAdapter() {
        if (activityArrayAdapter == null)
            return;
        activityArrayAdapter.clear();
        activityArrayAdapter.addAll(getActivitys());
        activityArrayAdapter.sort(activityTor);
        activityArrayAdapter.notifyDataSetChanged();
    }

    public PlannedActivityArrayAdapter getPlannedActivityArrayAdapter(Context context) {
        plannedActivityArrayAdapter = new PlannedActivityArrayAdapter(context, getPlannedActivitys());
        plannedActivityArrayAdapter.sort(plannedActivityTor);
        return plannedActivityArrayAdapter;
    }

    public void refreshPlannedActivityArrayAdapter() {
        if (plannedActivityArrayAdapter == null)
            return;
        plannedActivityArrayAdapter.clear();
        plannedActivityArrayAdapter.addAll(getPlannedActivitys());
        plannedActivityArrayAdapter.sort(plannedActivityTor);
        plannedActivityArrayAdapter.notifyDataSetChanged();
    }

    public DiaryEntryArrayAdapter getDiaryEntryArrayAdapter(Context context) {
        diaryEntryArrayAdapter = new DiaryEntryArrayAdapter(context, getDiaryEntrys());
        diaryEntryArrayAdapter.sort(diaryEntryTor);
        return diaryEntryArrayAdapter;
    }

    public void refreshDiaryEntryArrayAdapter() {
        if (diaryEntryArrayAdapter == null)
            return;
        diaryEntryArrayAdapter.clear();
        diaryEntryArrayAdapter.addAll(getDiaryEntrys());
        diaryEntryArrayAdapter.sort(diaryEntryTor);
        diaryEntryArrayAdapter.notifyDataSetChanged();
    }

    public ActivityIterationArrayAdapter getActivityIterationArrayAdapter(Context context) {
        activityIterationArrayAdapter = new ActivityIterationArrayAdapter(context, getActivityIterations());
//        activityIterationArrayAdapter.sort(activityITor);
        return activityIterationArrayAdapter;
    }

    public void refreshActivityIterationArrayAdapter() {
        if (activityIterationArrayAdapter == null)
            return;
        activityIterationArrayAdapter.clear();
        activityIterationArrayAdapter.addAll(getActivityIterations());
//        activityArrayAdapter.sort(activityTor);
        activityIterationArrayAdapter.notifyDataSetChanged();
    }


    public ArrayList<RoleStatus> getFilteredRoles() {

        return this.filteredRoles;
    }

    public ArrayList<RoleAssociated> filterRoleAssociated(ArrayList roleAssociated) {

        ArrayList<RoleAssociated> filtered = new ArrayList<RoleAssociated>();

        for (int i = 0; i < roleAssociated.size(); i++) {
            RoleAssociated ra = (RoleAssociated) roleAssociated.get(i);
            for (int j = 0; j < this.filteredRoles.size(); j++) {
                RoleStatus rs = (RoleStatus) filteredRoles.get(j);
                boolean available = rs.isAvailable();
                Role r = rs.getRole();
                if (available) {
                    if (r.getDatabaseRecordNo() == ra.getRole()) {
                        filtered.add(ra);
                    }
                }
            }
        }
        return filtered;
    }

    public ArrayList<LocationStatus> getFilteredLocations() {

        return this.filteredLocations;
    }

    public ArrayList<LocationAssociated> filterLocationAssociated(ArrayList locationAssociated) {

        ArrayList<LocationAssociated> filtered = new ArrayList<LocationAssociated>();

        for (int i = 0; i < locationAssociated.size(); i++) {
            LocationAssociated la = (LocationAssociated) locationAssociated.get(i);
            for (int j = 0; j < this.filteredLocations.size(); j++) {
                LocationStatus ls = (LocationStatus) filteredLocations.get(j);
                boolean available = ls.isAvailable();
                Location l = ls.getLocation();
                if (available) {
                    if (l.getDatabaseRecordNo() == la.getLocation()) {
                        filtered.add(la);
                    }
                }
            }
        }
        return filtered;
    }

    public String formatMedianMinutes(int minutes) {
        int hours = 0;
        StringBuffer sb = new StringBuffer();
        if (minutes == 0) {
            sb.append("0min");
        } else if (minutes > 59) {
            hours = minutes / 60;
            minutes -= hours * 60;
            sb.append(hours + "hr ");
        }
        if (minutes > 0)
            sb.append(minutes + "min");
        return sb.toString();
    }

    public ArrayList<ActivityIteration> getActivityIterations() {

        ArrayList<ActivityIteration> activityIterations = dbFrontEnd.getActivityIterations();

        return activityIterations;
    }

    public long addActivityIteration(ActivityIteration activityIteration) {

        long id = dbFrontEnd.addActivityIteration(activityIteration);
        refreshActivityIterationArrayAdapter();
        return id;
    }

    public ActivityIteration getActivityIteration(int id) {
        return dbFrontEnd.getActivityIteration(id);
    }

    public long updateActivityIteration(ActivityIteration activityIteration) {

        long id = dbFrontEnd.updateActivityIteration(activityIteration);
        refreshActivityIterationArrayAdapter();
        return id;
    }

    public void deleteActivityIteration(int id) {
        dbFrontEnd.deleteActivityIteration(id);
        refreshActivityIterationArrayAdapter();
    }

    public class RoleStatus {

        private Role role = null;
        private boolean available = false;

        public RoleStatus(Role role, boolean available) {
            this.role = role;
            this.available = available;
        }

        public Role getRole() {
            return this.role;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {

            this.available = available;

        }
    }

    public class LocationStatus {

        private Location location = null;
        private boolean available = false;

        public LocationStatus(Location location, boolean available) {
            this.location = location;
            this.available = available;
        }

        public Location getLocation() {
            return this.location;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {

            this.available = available;

        }
    }

    public ActivityIteration generateActivityIteration(){


        SimpleDateFormat storageFormat =
                UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_TimeStamp_Storage);

        SimpleDateFormat startDateFormat =
                UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_yyyyMMdd);

        SimpleDateFormat startTimeFormat =
                UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_21_59);

        ActivityIteration iteration = new ActivityIteration();

        iteration.setOwner(1);
        iteration.setOwnerType(Ownable.PERSON);
        Calendar now = Calendar.getInstance();
        iteration.setStartDate(startDateFormat.format(now.getTime()));
        iteration.setStartTime(startTimeFormat.format(now.getTime()));
        iteration.setEndDate(startDateFormat.format(now.getTime()));
        iteration.setEndTime(startTimeFormat.format(now.getTime()));
        iteration.setTimeStamp(storageFormat.format(now.getTime()));
        iteration.setLastModifiedBy(1);
        return iteration;

    }


}
