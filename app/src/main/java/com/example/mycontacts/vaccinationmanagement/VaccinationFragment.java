package com.example.mycontacts.vaccinationmanagement;


import com.example.mycontacts.vaccinationmanagement.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontacts.R;
import com.example.mycontacts.vaccinationmanagement.Database.DataBaseHandler;
import com.example.mycontacts.vaccinationmanagement.Database.VaccinationDataItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.mycontacts.*;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.mycontacts.R.*;
import static com.example.mycontacts.vaccinationmanagement.AddEvent.FROM_ADD_TO_MAIN;
import static com.example.mycontacts.vaccinationmanagement.AddEvent.RESULT_CODE_FROM_ADD_TO_MAIN;
import static com.example.mycontacts.vaccinationmanagement.CustomListAdapter.DISPLAY_DATA;
import static com.example.mycontacts.vaccinationmanagement.CustomListAdapter.RETURN_FROM_ADD;
import static com.google.android.material.color.MaterialColors.getColor;


public class  VaccinationFragment extends Fragment {

    public static final String ADD_DATE="add_intent";
    private static final int ADD_INTENT =1;
    private static final String DATE_ON_ROTATION ="date_on_rotation" ;
   // private ActivityResultLauncher<Intent> someActivityResultLauncher;


    RecyclerView recyclerView;
    FloatingActionButton addEventFAB;
    CustomListAdapter adapter;
    DataBaseHandler db;
    SimpleDateFormat formatter;
    String date;
    Calendar calendar;
    MaterialCalendarView calendarView;
    EventDecorator decorator;

    public VaccinationFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Log.i(contants.LOG_CAT,"onCreate running");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(layout.calendar_main,container,false);

        Log.i(contants.LOG_CAT,"onCreateView running");
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(contants.LOG_CAT,"onViewCreated running");


        formatter=new SimpleDateFormat("dd/MM/yyyy");
        db=new DataBaseHandler(view.getContext());
        //super.onCreate(savedInstanceState);
        // initializations
        recyclerView =view.findViewById(id.recycler_view);
        addEventFAB=(FloatingActionButton) view.findViewById(id.add_event_fab);
        calendarView=(MaterialCalendarView) view.findViewById(id.calendarView);
        EventDecorator.setgreenDates(db.getDates(0),calendarView, ContextCompat.getColor(getContext(), color.dark_green));
        EventDecorator.setRedDates(db.getDates(1),calendarView,ContextCompat.getColor(getContext(),color.red ));
        Intent intent =getActivity().getIntent();
        calendar = Calendar.getInstance();
        Date date_obj = calendar.getTime();
        date = formatter.format(date_obj);
        calendarView.setCurrentDate(date_obj);


        if(savedInstanceState!=null){
            date=savedInstanceState.getString(DATE_ON_ROTATION);
            try {
                calendar.setTime(formatter.parse(date));
                calendarView.setCurrentDate(calendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        //recyclerview setup
        adapter =new CustomListAdapter(getList(formatter.format(calendar.getTime())),someActivityResultLauncher);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay dat, boolean selected) {
                date=formatter.format(dat.getDate());
                adapter.setVaccinationData(db.getAllContactsData(date));
                adapter.notifyDataSetChanged();
            }
        }) ;

        addEventFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEvent.class);
                intent.putExtra(ADD_DATE,date);
                //((AppCompatActivity)getActivity()).startActivityForResult(intent,RETURN_FROM_ADD);
                someActivityResultLauncher.launch(intent);
            }

           //ActivityResultLauncher<Intent>
        });

    }


/*
        addEventFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ActivityMain.this, AddEvent.class);
                intent.putExtra(ADD_DATE,date);
                startActivityForResult(intent,RETURN_FROM_ADD);
            }
        });

 */

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(DATE_ON_ROTATION,date);
        super.onSaveInstanceState(outState);
        Log.i(contants.LOG_CAT,"onSaveInstanceState running");

    }

    private List<VaccinationDataItem> getList(String date) {
        return db.getAllContactsData(date);
    }



    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {


                    if (result.getResultCode() ==RESULT_CODE_FROM_ADD_TO_MAIN) {
                        // There are no request codes
                        Intent data = result.getData();
                        date = data.getStringExtra(FROM_ADD_TO_MAIN);
                        try {
                            Date date_obj = formatter.parse(date);
                            calendar.setTime(date_obj);
                            calendarView.setCurrentDate(date_obj);
                            // calendarView.setDate(date_obj.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        adapter.setVaccinationData(db.getAllContactsData(date));
                        adapter.notifyDataSetChanged();
                        EventDecorator.setgreenDates(db.getDates(0), calendarView, ContextCompat.getColor(getView().getContext(),color.dark_green));
                        EventDecorator.setRedDates(db.getDates(1), calendarView, ContextCompat.getColor( getView().getContext(),color.red));
                        Log.i(contants.LOG_CAT,"onActivity running sa");
                    }
                }
            });


    //public void openSomeActivityForResult() {
      //  Intent intent = new Intent(getActivity(), AddEvent.class);
        //someActivityResultLauncher.launch(intent);
    //}

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RETURN_FROM_ADD&& resultCode==RESULT_CODE_FROM_ADD_TO_MAIN) {
            date = data.getStringExtra(FROM_ADD_TO_MAIN);
            try {
                Date date_obj = formatter.parse(date);
                calendar.setTime(date_obj);
                calendarView.setCurrentDate(date_obj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            adapter.setVaccinationData(db.getAllContactsData(date));
            adapter.notifyDataSetChanged();
            EventDecorator.setgreenDates(db.getDates(0),calendarView,ContextCompat.getColor(getView().getContext(),R.color.dark_green));
            EventDecorator.setRedDates(db.getDates(1),calendarView,ContextCompat.getColor(getView().getContext(),R.color.red));

            Log.i(contants.LOG_CAT,"onActivity running");
        }
    }


     */





}

