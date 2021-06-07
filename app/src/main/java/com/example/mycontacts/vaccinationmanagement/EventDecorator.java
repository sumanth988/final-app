package com.example.mycontacts.vaccinationmanagement;

import android.graphics.Color;

import com.example.mycontacts.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Date;

public class EventDecorator implements DayViewDecorator {

    private static EventDecorator redDecorator;
    private static EventDecorator greenDecorator;
    private  int color;
    private  ArrayList<CalendarDay> dates;

    private EventDecorator(int color, ArrayList<Date> dates) {
        this.color = color;
        this.dates=new ArrayList<CalendarDay>();
        for(int i=0;i<dates.size();i++)
        this.dates.add(CalendarDay.from(dates.get(i)));
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(6,color));
    }


    public static  void setgreenDates(ArrayList<Date> add, MaterialCalendarView calendarView,int green) {
        if(greenDecorator!=null){
            calendarView.removeDecorator(greenDecorator);
        }
        greenDecorator=new EventDecorator(green,add);
        calendarView.addDecorator(greenDecorator);
    }
    public static  void setRedDates(ArrayList<Date> add, MaterialCalendarView calendarView, int color) {
        if(redDecorator!=null){
            calendarView.removeDecorator(redDecorator);
        }
        redDecorator=new EventDecorator(color,add);
        calendarView.addDecorator(redDecorator);
    }
}
