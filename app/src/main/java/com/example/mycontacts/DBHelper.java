package com.example.mycontacts;



import java.util.ArrayList;
        import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.DatabaseUtils;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_PATH="image_name";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_VACCINE = "vaccine";
    public static final String CONTACTS_COLUMN_WEIGHT = "weight";
    public static final String CONTACTS_COLUMN_AGE = "age";
    public static final String CONTACTS_COLUMN_BREED = "breed";
    private HashMap hp;
    private Object Context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

/*
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + CONTACTS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name TEXT NOT NULL, " +
                    " grade TEXT NOT NULL);";  */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "(id integer PRIMARY KEY AUTOINCREMENT ,image_name TEXT not null, name TEXT not null,breed TEXT not null,vaccine TEXT, weight TEXT,age TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContact (String image_name,String name, String breed, String vaccine, String weight,String age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("image_name",image_name);
        contentValues.put("name", name);
        contentValues.put("breed", breed);
        contentValues.put("vaccine", vaccine);
        contentValues.put("weight", weight);
        contentValues.put("age", age);
        db.insert("contacts", null, contentValues);
        return true;
    }

/*
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }
    */

    public  Cursor getData(String pp)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from contacts where name=?",new String[]{pp}
               , null);
        return res;
    }


    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (String image_name, String name, String breed, String vaccine, String weight,String age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("image_name",image_name);
        contentValues.put("name", name);
        contentValues.put("breed", breed);
        contentValues.put("vaccine", vaccine);
        contentValues.put("weight", weight);
        contentValues.put("age", age);
        db.update("contacts", contentValues, "name = ? ", new String[] { name } );
        return true;
    }

    public Integer deleteContact (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contacts",
                "name = ? ",
                new String[] { name });

        db.close();
        return 1;
    }

    public List<PetShow> getAllUser() {
        List<PetShow> pet_show_list = new ArrayList<PetShow>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from contacts",null);
        res.moveToFirst();

        if (res.moveToFirst()) {
            do {
                PetShow ps= new PetShow( res.getString(2),res.getString(3),res.getString(1));

                // Adding contact to list
                pet_show_list.add(ps);
            } while (res.moveToNext());
        }
        return pet_show_list;
    }



    public List<String> getAllUserName() {
        List<String> show_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts ", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            show_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));

            //res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();

        }

        return show_list;
    }
}