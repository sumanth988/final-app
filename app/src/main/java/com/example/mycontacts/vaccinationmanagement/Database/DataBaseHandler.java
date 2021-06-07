package com.example.mycontacts.vaccinationmanagement.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    public static final String TABLE_VACCINATION_DATA="vaccination_table";
    public static final String EVENT_ID="event_id";
    public static final String PET_NAME ="petName";
    public static final String VACCINATION_DATE ="vaccinationdate";
    public static final String VACCINE_NAME ="vaccinename";
    public static final String VACCINATION_DONE ="vaccinenationDone";

    public DataBaseHandler(@Nullable Context context) {
        super(context,TABLE_VACCINATION_DATA, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_VACCINATION_DATA+ "("
                + EVENT_ID + " INTEGER PRIMARY KEY," + PET_NAME + " TEXT,"
                + VACCINATION_DATE+ " TEXT," + VACCINE_NAME+" TEXT,"+VACCINATION_DONE+" INTEGER"+");";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VACCINATION_DATA);
        onCreate(db);
    }

    public void deleteContact(Long eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VACCINATION_DATA, EVENT_ID + " = ?",
                new String[] { String.valueOf(eventId) });
        db.close();
    }
    public void updateEvent(VaccinationDataItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = getcv(item);

        // updating row
        db.update(TABLE_VACCINATION_DATA, values, EVENT_ID + " = ?",
                new String[] { String.valueOf(item.getEventId()) });
    }
    public List<VaccinationDataItem> getAllContactsData(String date) {
        List<VaccinationDataItem> vaccinationList = new ArrayList<VaccinationDataItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_VACCINATION_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        String[] str={EVENT_ID,PET_NAME,VACCINATION_DATE,VACCINE_NAME,VACCINATION_DONE};
        Cursor cursor = db.query(TABLE_VACCINATION_DATA,str,VACCINATION_DATE+" = ? ",new String[]{date},null,null,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                VaccinationDataItem vaccinationDataItem = new VaccinationDataItem( cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4) );

                // Adding contact to list
                vaccinationList.add(vaccinationDataItem);
            } while (cursor.moveToNext());
        }

        // return contact list
        return vaccinationList;
    }
    public void addVaccinationData(VaccinationDataItem vaccinationDataItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = getcv(vaccinationDataItem);
        db.insert(TABLE_VACCINATION_DATA, null, values);
        db.close();
    }
    private ContentValues getcv(VaccinationDataItem vaccinationDataItem) {
        ContentValues values = new ContentValues();
        values.put(EVENT_ID, vaccinationDataItem.getEventId());
        values.put(PET_NAME, vaccinationDataItem.getPetName());
        values.put(VACCINATION_DATE, vaccinationDataItem.getVaccinationDate());
        values.put(VACCINE_NAME, vaccinationDataItem.getVaccineName());
        values.put(VACCINATION_DONE, vaccinationDataItem.getVaccinationDone());
        return values;
    }
    public void deleteContact(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VACCINATION_DATA,EVENT_ID+" = ?",new String[]{String.valueOf(eventId)});
        db.close();
    }

    public VaccinationDataItem getVaccineData(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr=db.query(TABLE_VACCINATION_DATA,new String[]{EVENT_ID,PET_NAME,VACCINATION_DATE,VACCINE_NAME,VACCINATION_DONE},EVENT_ID+" = ?",new String[]{String.valueOf(eventId)},null,null,null);
        if (cr != null)
            cr.moveToFirst();
        return new VaccinationDataItem(cr.getInt(0),cr.getString(1),cr.getString(2),cr.getString(3),cr.getInt(4));
    }
    public ArrayList<Date> getDates(int vaccinationDone){
        ArrayList<Date> dates=new ArrayList<>();
        Cursor cursor=this.getReadableDatabase().query(true,TABLE_VACCINATION_DATA
                ,new String[]{VACCINATION_DATE},VACCINATION_DONE+" = ?",new String[]{String.valueOf(vaccinationDone)},null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date=formatter.parse(cursor.getString(0));
                    dates.add(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        return dates;
    }
}
