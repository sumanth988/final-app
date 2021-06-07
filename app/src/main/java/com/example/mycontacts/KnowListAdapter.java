package com.example.mycontacts;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class KnowListAdapter extends RecyclerView.Adapter<KnowListAdapter.MyViewHolder> {
    private KnowListData[] listdata;

    public KnowListAdapter(KnowListData[] youTubeLinks) {
        listdata=youTubeLinks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        return new MyViewHolder(layoutInflater.inflate(R.layout.recycler_item_know_your_pet,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setTitleTextView(listdata[position].title);
        holder.setListner(listdata[position].link);

    }

    @Override
    public int getItemCount() {
        //return listdata.length;
        return listdata.length;
    }

    public void setList(KnowListData[] youTubeLinks) {
        listdata=youTubeLinks;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
       // private ImageView thumbNail;
        private TextView titleTextView;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            //thumbNail=itemView.findViewById(R.id.thumb_nail);
            titleTextView=itemView.findViewById(R.id.title_text_view);

        }

        public void setTitleTextView(String title) {
            this.titleTextView.setText(title);
        }
        public void setListner(String link){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(link));
                    intent.setPackage("com.google.android.youtube");
                    titleTextView.getContext().startActivity(intent);

                }
            });
        }
    }
}
