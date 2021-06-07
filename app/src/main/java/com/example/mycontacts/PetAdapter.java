package com.example.mycontacts;

        import android.content.Context;
        import android.content.Intent;
        import android.database.Observable;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;

        import java.io.File;
        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<PetShow> petShowList;
    private Context myContext;

    public PetAdapter(Context context, List<PetShow> petShowList) {
        this.petShowList = petShowList;
        this.myContext = context;
    }


    public class PetViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        ImageView thumbnail;

        public PetViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title1);
            subtitle = (TextView) view.findViewById(R.id.subtitle);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }



        public void setClickListner() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), EditViewActivity.class);

                    Bundle dataBundle = new Bundle();
                    String text=title.getText().toString();
                    dataBundle.putString("name",text);
                    dataBundle.putString("task","view_profile");
                    intent.putExtras(dataBundle);

                    ( thumbnail.getContext()).startActivity(intent);
                }
            });
        }

    }




    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_card, parent, false);

        return new PetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PetViewHolder holder, int position) {
        PetShow ps = petShowList.get(position);
        holder.title.setText(ps.getPet_name());
        holder.subtitle.setText(ps.getPet_type());

        String imgName=ps.getImg();
        // loading album cover using Glide library
        //Glide.with(myContext).load(ps.getImg()).into(holder.thumbnail);
        File dir=new File(myContext.getExternalFilesDir(null).getAbsolutePath()+"/petApp/",imgName);
        Bitmap bitmap= BitmapFactory.decodeFile(String.valueOf(dir));
        holder.thumbnail.setImageBitmap(bitmap);


        holder.setClickListner();


    }




    @Override
    public int getItemCount() {
        return petShowList.size();
    }
}
