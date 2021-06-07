package com.example.mycontacts.vaccinationmanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.example.mycontacts.DBHelper;
import com.example.mycontacts.HomePage;
import com.example.mycontacts.R;
import com.example.mycontacts.vaccinationmanagement.Database.DataBaseHandler;
import com.example.mycontacts.vaccinationmanagement.Database.VaccinationDataItem;
import com.example.mycontacts.vaccinationmanagement.CustomListAdapter;

import java.util.List;

import static com.example.mycontacts.vaccinationmanagement.CalendarProviderDataBase.addEventToCalendarProvider;
import static com.example.mycontacts.vaccinationmanagement.CalendarProviderDataBase.deletfromCalendarProvider;
import static com.example.mycontacts.vaccinationmanagement.CalendarProviderDataBase.updateEventInCalendarProvider;


public class AddEvent extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 1;
    public static final String FROM_ADD_TO_MAIN = "return from add";
    static final int RESULT_CODE_FROM_ADD_TO_MAIN = 4;
    Spinner  petNamesSpinner;
    Spinner vaccineNamesSpinner;
    TextView selectDate;
    CheckBox VaccineDoneCHKBOX;
    Button addBtn;
    Button deleteBtn;

    String pet;
    String vaccine;
    String date;
    int eventId;

    DataBaseHandler dbHandler;
    Intent intent;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        intent =getIntent();


        //initializing the views
        VaccineDoneCHKBOX=(CheckBox) findViewById(R.id.done);
        deleteBtn=findViewById(R.id.delete);
        addBtn=findViewById(R.id.add);
        petNamesSpinner=(Spinner) findViewById(R.id.petname);
        selectDate =(TextView) findViewById(R.id.editTextDate);
        vaccineNamesSpinner=(Spinner) findViewById(R.id.vaccination);
        dbHandler=new DataBaseHandler(AddEvent.this);

        //setting data based on editing or adding
        eventId=intent.getIntExtra(CustomListAdapter.DISPLAY_DATA,-1);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventId==-1) {
                    VaccinationDataItem item=addEventToCalendarProvider(date, vaccine, pet, VaccineDoneCHKBOX.isChecked(), AddEvent.this);
                 dbHandler.addVaccinationData(item);
                }
                else updateEvent();
                Intent intent =new Intent(AddEvent.this, HomePage.class);
                intent.putExtra(FROM_ADD_TO_MAIN,date);
                setResult(RESULT_CODE_FROM_ADD_TO_MAIN,intent);
                finish();
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteContact(eventId);
                deletfromCalendarProvider(eventId,AddEvent.this);
                Intent intent =new Intent(AddEvent.this,HomePage.class);
                intent.putExtra(FROM_ADD_TO_MAIN,date);
                setResult(RESULT_CODE_FROM_ADD_TO_MAIN,intent);
                finish();
            }

        });

            AdapterView.OnItemSelectedListener vacccinationClickListner = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vaccine = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };



        AdapterView.OnItemSelectedListener petSpinnerClickListner = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pet=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        petNamesSpinner.setOnItemSelectedListener(petSpinnerClickListner);
        vaccineNamesSpinner.setOnItemSelectedListener(vacccinationClickListner);
        loadPetData();
        loadVaccineData();
        setData();
        mRequestPermisions();
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    private void mRequestPermisions() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CALENDAR) ==
                PackageManager.PERMISSION_GRANTED) {
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                        REQUEST_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Please restart app and click allow", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setData() {
        if(eventId==-1){
            deleteBtn.setVisibility(View.GONE);
            date=intent.getStringExtra(VaccinationFragment.ADD_DATE);
            selectDate.setText(date);
        }else
        {
            VaccinationDataItem item=dbHandler.getVaccineData(eventId);
            date=item.getVaccinationDate();
            selectDate.setText(item.getVaccinationDate());
            petNamesSpinner.setSelection(getPetNames().indexOf(item.getPetName()));
            vaccineNamesSpinner.setSelection(DummyDataBaseLogics.getVaccineData().indexOf(item.getVaccineName()));
            VaccineDoneCHKBOX.setChecked(item.getVaccinationDone()==0);
            addBtn.setText("save");
        }
    }

   /* private void init() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        calId=sharedPref.getLong(getString(R.string.CalenderId),-1);
        if(calId==-1){
            calId=getCalId();
        }
        Toast.makeText(this, ""+(calId),Toast.LENGTH_LONG).show();
    }

    public Long getCalId() {
         final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Calendars._ID                          // 0// 3
        };

// The indices for the projection array above.
         final int PROJECTION_ID_INDEX = 0;
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;


// Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        cur.moveToFirst();
            long calID = 0;


            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);


            // Do something with the values...


        return calId;
    }

    // inserting a calendar
    private long insertCalender() {
        ContentResolver cr=getContentResolver();
        ContentValues values =new ContentValues();

        values.put(CalendarContract.Calendars.VISIBLE, 0);
        values.put(CalendarContract.Calendars.SYNC_EVENTS,1);
        Uri uri=cr.insert(asSyncAdapter(),values);
        return Long.parseLong(uri.getLastPathSegment());
    }
    static Uri asSyncAdapter() {
        return CalendarContract.Calendars.CONTENT_URI.buildUpon()
                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "pet Care App")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL).build();
    }
*/

    private void updateEvent() {
        dbHandler.updateEvent(new VaccinationDataItem(eventId,pet,date,vaccine,VaccineDoneCHKBOX.isChecked()?0:1));
        updateEventInCalendarProvider(new VaccinationDataItem(eventId,pet,date,vaccine,VaccineDoneCHKBOX.isChecked()?0:1),AddEvent.this );
    }

    private void loadVaccineData() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, com.example.mycontacts.vaccinationmanagement.DummyDataBaseLogics.getVaccineData());
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccineNamesSpinner.setAdapter(dataAdapter);
    }

    public  List<String> getPetNames(){


        DBHelper dbh=new DBHelper(getApplicationContext());
        List<String> list=dbh.getAllUserName();

        return list;
    }


    private void loadPetData() {

        List<String> lables = getPetNames();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, lables);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petNamesSpinner.setAdapter(dataAdapter);
    }

}

