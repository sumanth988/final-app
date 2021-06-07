package com.example.mycontacts;

import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);
    }
*/


@SuppressWarnings("ALL")
public class EditViewActivity extends AppCompatActivity {

    int from_Where_I_Am_Coming = 0;
    private DBHelper mydb ;

    ImageView ivImage;
    TextView name ;
    TextView breed;
    TextView vaccine;
    TextView weight;
    TextView age;
    String name_To_Update ="";
    String tsk="view_profile";




    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageButton btnSelect;
    private String userChoosenTask;


    static File destination;
    static FileOutputStream fo;
    static Bitmap bm=null;
    static Uri uri;
    static String path="";
    private String s="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        ivImage = (ImageView)findViewById(R.id.editImage);
        name = (TextView) findViewById(R.id.editPetName);
        breed = (TextView) findViewById(R.id.editPetBreed);
        vaccine = (TextView) findViewById(R.id.editVaccination);
        weight = (TextView) findViewById(R.id.editWeight);
        age = (TextView) findViewById(R.id.editAge);
        btnSelect = (ImageButton) findViewById(R.id.Imb1);



        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            String task = extras.getString("task");
            String name_intent=extras.getString("name");

            if(task.equals("view_profile")){
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(name_intent);
                name_To_Update = name_intent;
                rs.moveToFirst();

                String image_vie=rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PATH));
                String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                String bree = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_BREED));
                String vaccin= rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_VACCINE));
                String weigh = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_WEIGHT));
                String ag = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_AGE));

                s=image_vie;

                if (!rs.isClosed())  {
                    rs.close();
                }
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);

                if(image_vie!="")
                {
                    File dir=new File(getExternalFilesDir(null).getAbsolutePath()+"/petApp/",image_vie);
                    //File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/petApp/",image_vie);
                    Bitmap bitmap= BitmapFactory.decodeFile(String.valueOf(dir));
                    ivImage.setImageBitmap(bitmap);
                }


                btnSelect.setVisibility(View.INVISIBLE);


                name.setText((CharSequence)nam);
                name.setFocusable(false);
                name.setClickable(false);

                breed.setText((CharSequence)bree);
                breed.setFocusable(false);
                breed.setClickable(false);

                vaccine.setText((CharSequence)vaccin);
                vaccine.setFocusable(false);
                vaccine.setClickable(false);

                weight.setText((CharSequence)weigh);
                weight.setFocusable(false);
                weight.setClickable(false);

                age.setText((CharSequence)ag);
                age.setFocusable(false);
                age.setClickable(false);



            }else if(task.equals("add_new"))
            {
                String backgroundImageName = String.valueOf(ivImage.getTag())+".jpg";
                s=backgroundImageName;

            }
        }




    }


    public  File getMyFile(String imgName)
    {
        return new File(getExternalFilesDir(null).getAbsolutePath()+"/petApp/",imgName);
    }

    public  void imageHandlerFun(View view)
    {
        /*
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        */


        selectImage();


//        Bitmap bitmap=BitmapFactory.decodeFile(String.valueOf(destination));
//        ivImage.setImageBitmap(bitmap);

        File dir=new File(getExternalFilesDir(null).getAbsolutePath()+"/petApp/",s);
        Bitmap bitmap=BitmapFactory.decodeFile(String.valueOf(dir));
        ivImage.setImageBitmap(bitmap);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(EditViewActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(EditViewActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        bm= (Bitmap) data.getExtras().get("data");




        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        s=System.currentTimeMillis() + ".jpg";
        destination=new File(getExternalFilesDir(null).getAbsolutePath()+"/petApp/",s);
        //path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/petApp/";


        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(bm);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                s=System.currentTimeMillis() + ".jpg";
                // path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/petApp/";
                destination=new File(getExternalFilesDir(null).getAbsolutePath()+"/petApp/",s);
                //destination = new File(path,s);



                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
    }














    /////





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();


        if(extras !=null) {
            String Value = extras.getString("task");
            if(Value.equals("view_profile")){
                getMenuInflater().inflate(R.menu.display_contact, menu);
            } else{
                getMenuInflater().inflate(R.menu.menu_main, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.Edit_Contact:
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);

                btnSelect = (ImageButton) findViewById(R.id.Imb1);
                btnSelect.setVisibility(View.VISIBLE);

                name.setEnabled(false);
                name.setFocusableInTouchMode(false);
                name.setClickable(false);


                breed.setEnabled(true);
                breed.setFocusableInTouchMode(true);
                breed.setClickable(true);

                vaccine.setEnabled(true);
                vaccine.setFocusableInTouchMode(true);
                vaccine.setClickable(true);

                weight.setEnabled(true);
                weight.setFocusableInTouchMode(true);
                weight.setClickable(true);

                age.setEnabled(true);
                age.setFocusableInTouchMode(true);
                age.setClickable(true);

                return true;
            case R.id.Delete_Contact:

                Bundle extra=getIntent().getExtras();
                String to_update=extra.getString("name");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteProfile)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteContact(to_update);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });


                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            String Value = extras.getString("task");
            if(Value.equals("view_profile")){
                if(mydb.updateContact(s,name.getText().toString(),
                        breed.getText().toString(), vaccine.getText().toString(),
                        weight.getText().toString(), age.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),HomePage.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }


            } else if(Value.equals("add_new")){
                if(mydb.insertContact(s,name.getText().toString(), breed.getText().toString(),
                        vaccine.getText().toString(), weight.getText().toString(),
                        age.getText().toString())){
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                startActivity(intent);
            }
        }
    }
}

