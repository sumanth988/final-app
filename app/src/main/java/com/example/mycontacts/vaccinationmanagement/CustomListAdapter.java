package com.example.mycontacts.vaccinationmanagement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mycontacts.R;
import com.example.mycontacts.vaccinationmanagement.Database.VaccinationDataItem;
import com.example.mycontacts.vaccinationmanagement.VaccinationFragment;

import java.util.List;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.MyViewHolder> {
    public static final String DISPLAY_DATA="display data";
    private List<VaccinationDataItem>  localData;
    public static final int RETURN_FROM_ADD=3;
    private static ActivityResultLauncher<Intent> activityResultLauncher;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view) ;
    }

    public CustomListAdapter(List<VaccinationDataItem> vaccinationDataItemList, ActivityResultLauncher<Intent> context) {
        localData=vaccinationDataItemList;
        activityResultLauncher=context;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setDate(localData.get(position).getVaccinationDate());
        holder.setPetName(localData.get(position).getPetName());
        holder.setVaccinationDone(localData.get(position).getVaccinationDone());
        holder.setVaccineName(localData.get(position).getVaccineName());
        holder.setEventId(localData.get(position).getEventId());
        holder.setClickListner();
    }

    @Override
    public int getItemCount() {
        return localData.size();
    }

    public void setVaccinationData(List<VaccinationDataItem> allContacts) {
        localData=allContacts;
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{
        private TextView date;
        private int eventId;
        private TextView vaccineName;
        private TextView petName;
        private CheckBox vaccinationDone;
        public void setEventId(int eventId) {
            this.eventId = eventId;
        }
        public void setDate(String date){
            this.date.setText(date);
        }
        public void setVaccineName(String vaccineName) {
            this.vaccineName.setText( vaccineName);
        }

        public void setPetName(String petName) {
            this.petName.setText(petName);
        }

        public void setVaccinationDone(int vaccinationDone) {
            this.vaccinationDone.setChecked(vaccinationDone == 0);

        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=(TextView) itemView.findViewById(R.id.date);
            vaccinationDone=(CheckBox) itemView.findViewById(R.id.checkBox);
            vaccineName=(TextView) itemView.findViewById(R.id.vaccine_name);
            petName=(TextView)itemView.findViewById(R.id.pet_name);

        }
        public void setClickListner(){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(itemView.getContext(),AddEvent.class);
                    intent.putExtra(DISPLAY_DATA,eventId);

                    activityResultLauncher.launch(intent);

                }
            });
        }
    }
}
