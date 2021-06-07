package com.example.mycontacts.vaccinationmanagement;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;


import com.example.mycontacts.vaccinationmanagement.Database.VaccinationDataItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CalendarProviderDataBase {
    public static  void updateEventInCalendarProvider(VaccinationDataItem vaccinationDataItem, Context context) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        Uri updateUri;
        values.put(CalendarContract.Events.TITLE,vaccinationDataItem.getVaccineName() );
        values.put(CalendarContract.Events.DESCRIPTION,vaccinationDataItem.getVaccineName()+" vaccination for"+vaccinationDataItem.getPetName());
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,vaccinationDataItem.getEventId());
        int rows = cr.update(updateUri, values, null, null);

    }

    public static  VaccinationDataItem addEventToCalendarProvider(String date, String vaccine, String pet, boolean vacc_done, Context context) {


        DateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
        Date date_obj=new Date();
        try {
            date_obj=formatter.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date_obj);
        calendar.set(Calendar.HOUR_OF_DAY,7);


        long startMillis = 0;
        startMillis = calendar.getTimeInMillis();

        calendar.add(Calendar.HOUR_OF_DAY,1);
        Long endMilli=calendar.getTimeInMillis();


        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.TITLE,vaccine );
        values.put(CalendarContract.Events.DESCRIPTION,vaccine+" vaccination for"+pet);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, String.valueOf(TimeZone.getDefault()));
        Uri uri;
        values.put(CalendarContract.Events.DTEND,endMilli);
        VaccinationDataItem vaccinationDataItem;
        try {
            uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            int eventID =  Integer.parseInt(uri.getLastPathSegment());
            // Toast.makeText(this,"eventid is "+eventID,Toast.LENGTH_SHORT).show();
            vaccinationDataItem=new VaccinationDataItem(eventID,pet,date,vaccine,vacc_done?0:1);
            return vaccinationDataItem;
        }
        catch (Exception e){
            Toast.makeText(context,"an error occured",Toast.LENGTH_LONG).show();
        }
        return null;
    }
    public static void deletfromCalendarProvider(int eventId,Context context) {
        ContentResolver cr = context.getContentResolver();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
        int rows = cr.delete(deleteUri, null, null);

    }
}
