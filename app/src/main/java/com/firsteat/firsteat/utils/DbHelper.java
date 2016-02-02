package com.firsteat.firsteat.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firsteat.firsteat.models.Locations;
import com.firsteat.firsteat.models.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by touchmagics on 12/3/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String TAG_LOCATION_ID = "location_id";
    public static final String TAG_FLAG_CREATE_TABLE_MENUITEM = "create_table_menuitem";
    private static final String TAG_FLAG_CREATE_TABLE_OFFERS = "create table offers";
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor prefEditor;
    private String TAG=DbHelper.class.getSimpleName();

    private static SQLiteDatabase database;
    private static String tablename;
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="eatfirst.db";

    /*Table Locations to store locations for service*/
    private static final String TABLE_LOCATIONS="locations";
    private static final String COLUMN_LOCATIONS_ID="id";//id of the location
    private static final String COLUMN_LOCATIONS_NAME="name";//name of the location
    private static final String COLUMN_LOCATIONS_LAT="lat";//latitude value
    private static final String COLUMN_LOCATIONS_LONG="lon";//longitude value
    private static final String COLUMN_LOCATIONS_STATUS="status";//status

    /*Table MenuItems*/
    private static final String TABLE_MENUITEMS="menuitems";
    private static final String COLUMN_MENUITEMS_STATUS="status";//status
    private static final String COLUMN_MENUITEMS_TOKEN="token";//token
    private static final String COLUMN_MENUITEMS_ID="id";//id of menu item
    private static final String COLUMN_MENUITEMS_NAME="name";//menuitem name
    private static final String COLUMN_MENUITEMS_LOCATION_ID="location_id";//location id
    private static final String COLUMN_MENUITEMS_DESCRIPTION="description";//description
    private static final String COLUMN_MENUITEMS_CATEGORY="category";//category of item
    private static final String COLUMN_MENUITEMS_PRICE="price";//price of item
    private static final String COLUMN_MENUITEMS_MOL="mol";//MAX ORDER LIMIT
    private static final String COLUMN_MENUITEMS_IMG_URL="img_url";//img url of item
    private static final String COLUMN_MENUITEMS_IN_STOCK="in_stock";//in stock status
    private static final String COLUMN_MENUITEMS_ACTIVE="active";//avtive status
    private static final String COLUMN_MENUITEMS_PROTEINS="proteins";//proteins value
    private static final String COLUMN_MENUITEMS_CARBOHYDRATES="carbohydrates";//carbohydrates
    private static final String COLUMN_MENUITEMS_FAT="fat";//fat
    private static final String COLUMN_MENUITEMS_FIBER="fibers";//fibers
    private static final String COLUMN_MENUITEMS_CALORIES="calories";//calories
    private int location_id;

    /*Table Offers*/
    private static final String TABLE_OFFERS="offers";
    private static final String COLUMN_OFFER_ID="offer_id";//id of the offer
    private static final String COLUMN_OFFER_NAME="offers_name";//name of offer
    private static final String COLUMN_OFFER_DESCRIPTION="offers_description";//description of offer
    private static final String COLUMN_OFFER_QUANTITY="offers_quantitiy";//quantity of offer
    private static final String COLUMN_OFFER_PRICE="offers_price";//price of offer
    private static final String COLUMN_OFFER_MENU_ID="offers_menu_id";//menu id with which offer is attached
    private static final String COLUMN_OFFER_STATUS="offers_status";//status of offer
    private static final String COLUMN_OFFER_START_DATE="offers_date";//start date of offer


    public void upgradeTable(String tablename,int previous_version,int latest_version)
    {
        DbHelper.tablename=tablename;
        onUpgrade(database, previous_version, latest_version);
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        prefEditor=prefs.edit();

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        this.database=db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String querry="DROP TABLE IF EXISTS "+tablename;
        db.execSQL(querry);
    }

    /*
    * Location Table
    * */
    public void createTableLocations() {
        this.database=this.getWritableDatabase();
        String query_create_location_table="create table "+TABLE_LOCATIONS+"("+
                COLUMN_LOCATIONS_ID+" integer not null,"+
                COLUMN_LOCATIONS_NAME+" text not null,"+
                COLUMN_LOCATIONS_LAT+" text,"+
                COLUMN_LOCATIONS_LONG+" text,"+
                COLUMN_LOCATIONS_STATUS+" integer not null"+
                ")";
        this.database.execSQL(query_create_location_table);
        database.close();
    }

    public void insertLocationValues(Locations locations) {
        database=this.getWritableDatabase();
        List<Locations.DataEntity> data = locations.getData();
        ContentValues values=new ContentValues();
        for(Locations.DataEntity entity:data){
            values.put(COLUMN_LOCATIONS_ID,entity.getId());
            values.put(COLUMN_LOCATIONS_NAME,entity.getLocation());
            values.put(COLUMN_LOCATIONS_LAT,entity.getLatitude());
            values.put(COLUMN_LOCATIONS_LONG,entity.getLongitude());
            values.put(COLUMN_LOCATIONS_STATUS,entity.getActive());
            location_id=entity.getId();
            database.insert(TABLE_LOCATIONS,null,values);
        }
        prefEditor.putInt(TAG_LOCATION_ID,1).commit();
        database.close();
    }

    public ArrayList<String> fetchLocationValues(){
        ArrayList<String> data=new ArrayList<>();
        String loc;
        database=this.getReadableDatabase();
        String querry="select * from "+TABLE_LOCATIONS;
        Cursor cursor=database.rawQuery(querry,null);

        if(cursor.moveToFirst())
        {
            do {
                loc=cursor.getString(1);
                data.add(loc);
            }while (cursor.moveToNext());
        }
        data.trimToSize();
        return data;
    }

    /*
    * Menu Table
    * */
    public void createTableMenu() {
        this.database=DbHelper.this.getWritableDatabase();
        String query_create_location_table="create table "+TABLE_MENUITEMS+"("+
                COLUMN_MENUITEMS_ID+" integer not null,"+
                COLUMN_MENUITEMS_NAME+" text,"+
                COLUMN_MENUITEMS_LOCATION_ID+" integer,"+
                COLUMN_MENUITEMS_DESCRIPTION+" text,"+
                COLUMN_MENUITEMS_CATEGORY+"  text,"+
                COLUMN_MENUITEMS_PRICE+"  integer,"+
                COLUMN_MENUITEMS_MOL+"  text,"+
                COLUMN_MENUITEMS_IMG_URL+"  text,"+
                COLUMN_MENUITEMS_IN_STOCK+"  text,"+
                COLUMN_MENUITEMS_ACTIVE+"  text,"+
                COLUMN_MENUITEMS_PROTEINS+"  integer,"+
                COLUMN_MENUITEMS_CARBOHYDRATES+"  integer,"+
                COLUMN_MENUITEMS_FAT+"  integer,"+
                COLUMN_MENUITEMS_FIBER+"  integer,"+
                COLUMN_MENUITEMS_CALORIES+"  integer"+
                ")";
        this.database.execSQL(query_create_location_table);
        database.close();
        prefEditor.putBoolean(TAG_FLAG_CREATE_TABLE_MENUITEM,true).commit();
    }

    public void insertMenuItemsToMenu(Menu menu) {
        database=this.getWritableDatabase();
        List<Menu.DataEntity> data = menu.getData();
        ContentValues values=new ContentValues();
        for(Menu.DataEntity entity:data){
            values.put(COLUMN_MENUITEMS_ID,entity.getId());
            values.put(COLUMN_MENUITEMS_NAME,entity.getName());
            values.put(COLUMN_MENUITEMS_LOCATION_ID,entity.getLocation_id());
            values.put(COLUMN_MENUITEMS_DESCRIPTION,entity.getDescription());
            values.put(COLUMN_MENUITEMS_CATEGORY,entity.getCategory());
            values.put(COLUMN_MENUITEMS_PRICE,entity.getPrice());
            values.put(COLUMN_MENUITEMS_MOL,entity.getMax_order_limit());
            values.put(COLUMN_MENUITEMS_IMG_URL,entity.getImg_url());
            values.put(COLUMN_MENUITEMS_IN_STOCK,entity.getIn_stock());
            values.put(COLUMN_MENUITEMS_ACTIVE,entity.getActive());
            values.put(COLUMN_MENUITEMS_PROTEINS,entity.getFood_details().getProteins());
            values.put(COLUMN_MENUITEMS_CARBOHYDRATES,entity.getFood_details().getCarbohydrates());
            values.put(COLUMN_MENUITEMS_FAT,entity.getFood_details().getFats());
            values.put(COLUMN_MENUITEMS_FIBER,entity.getFood_details().getFibre());
            values.put(COLUMN_MENUITEMS_CALORIES,entity.getFood_details().getCalories());
            database.insert(TABLE_MENUITEMS,null,values);
        }
        database.close();
    }

    public Menu fetchMenu(int location) {
        Menu menu=new Menu();
        Menu.DataEntity menuitem;
        ArrayList<Menu.DataEntity> dummymenu= new ArrayList<>();
        Menu.DataEntity.FoodDetailsEntity food;
        String loc;
        database=this.getReadableDatabase();
        String querry="select * from "+TABLE_MENUITEMS;
        Cursor cursor=database.rawQuery(querry,null);

        if(cursor.moveToFirst())
        {
            do {
                if(cursor.getInt(2)==location){
                    menuitem=new Menu.DataEntity();
                    food=new Menu.DataEntity.FoodDetailsEntity();

                    Log.d(TAG, "menu item at location");
                    menuitem.setId(cursor.getInt(0));
                    menuitem.setName(cursor.getString(1));
                    menuitem.setLocation_id(cursor.getInt(2));
                    menuitem.setDescription(cursor.getString(3));
                    menuitem.setCategory(cursor.getString(4));
                    menuitem.setPrice(cursor.getInt(5));
                    menuitem.setMax_order_limit(cursor.getInt(6));
                    menuitem.setImg_url(cursor.getString(7));
                    menuitem.setIn_stock(cursor.getString(8));
                    menuitem.setActive(cursor.getString(9));

                    food.setProteins(cursor.getInt(10));
                    food.setCarbohydrates(cursor.getInt(11));
                    food.setFats(cursor.getInt(12));
                    food.setFibre(cursor.getInt(13));
                    food.setCalories(cursor.getInt(14));

                    menuitem.setFood_details(food);

                    dummymenu.add(menuitem);
                }

            }while (cursor.moveToNext());
            dummymenu.trimToSize();
            menu.setData(dummymenu);
            Log.d(TAG,"menu size "+menu.getData().size());
        }
        return menu;
    }

    public Menu fetchFavMenu(ArrayList<String> favMenuIds) {
        Menu menu=new Menu();
        Menu.DataEntity menuitem;
        ArrayList<Menu.DataEntity> dummymenu= new ArrayList<>();
        Menu.DataEntity.FoodDetailsEntity food;
        String loc;
        database=this.getReadableDatabase();
        String querry="select * from "+TABLE_MENUITEMS;
        Cursor cursor=database.rawQuery(querry,null);

        if(cursor.moveToFirst())
        {
            do {
                for(int i=0;i<favMenuIds.size();i++){
                    if(favMenuIds.get(i).equalsIgnoreCase(String.valueOf(cursor.getInt(0)))){

                        menuitem=new Menu.DataEntity();
                        food=new Menu.DataEntity.FoodDetailsEntity();

                        Log.d(TAG, "menu item at location");
                        menuitem.setId(cursor.getInt(0));
                        menuitem.setName(cursor.getString(1));
                        menuitem.setLocation_id(cursor.getInt(2));
                        menuitem.setDescription(cursor.getString(3));
                        menuitem.setCategory(cursor.getString(4));
                        menuitem.setPrice(cursor.getInt(5));
                        menuitem.setMax_order_limit(cursor.getInt(6));
                        menuitem.setImg_url(cursor.getString(7));
                        menuitem.setIn_stock(cursor.getString(8));
                        menuitem.setActive(cursor.getString(9));

                        food.setProteins(cursor.getInt(10));
                        food.setCarbohydrates(cursor.getInt(11));
                        food.setFats(cursor.getInt(12));
                        food.setFibre(cursor.getInt(13));
                        food.setCalories(cursor.getInt(14));

                        menuitem.setFood_details(food);

                        dummymenu.add(menuitem);
                    }
                }


            }while (cursor.moveToNext());
            dummymenu.trimToSize();
            menu.setData(dummymenu);
            Log.d(TAG,"menu size "+menu.getData().size());
        }
        return menu;
    }


    /*
    * offers related stuff
    * */
    public void createTableOffers() {
        this.database=DbHelper.this.getWritableDatabase();
        String query_create_location_table="create table "+TABLE_OFFERS+"("+
                COLUMN_OFFER_ID+" integer not null,"+
                COLUMN_OFFER_NAME+" text,"+
                COLUMN_OFFER_DESCRIPTION+" text,"+
                COLUMN_OFFER_QUANTITY+" integer,"+
                COLUMN_OFFER_PRICE+"  integer,"+
                COLUMN_OFFER_MENU_ID+"  integer,"+
                COLUMN_OFFER_STATUS+"  integer,"+
                COLUMN_OFFER_START_DATE+"  text"+
                ")";
        this.database.execSQL(query_create_location_table);
        database.close();
        prefEditor.putBoolean(TAG_FLAG_CREATE_TABLE_OFFERS,true).commit();
        if(prefs.getBoolean(TAG_FLAG_CREATE_TABLE_OFFERS,false))
            Log.d(TAG,"offer table created");
    }

    public void insertIntoOffers(Menu menu) {
        database=this.getWritableDatabase();
        List<Menu.DataEntity> data = menu.getData();
        ContentValues values=new ContentValues();

        for (int i = 0; i < data.size(); i++) {
            for(int j=0;j<data.get(i).getFood_details().getOffers().size();j++){
                values.put(COLUMN_OFFER_ID, data.get(i).getFood_details().getOffers().get(j).getId());
                values.put(COLUMN_OFFER_NAME,data.get(i).getFood_details().getOffers().get(j).getOffer_name());
                values.put(COLUMN_OFFER_DESCRIPTION,data.get(i).getFood_details().getOffers().get(j).getDescription());
                values.put(COLUMN_OFFER_QUANTITY,data.get(i).getFood_details().getOffers().get(j).getQty());
                values.put(COLUMN_OFFER_PRICE,data.get(i).getFood_details().getOffers().get(j).getPrice());
                values.put(COLUMN_OFFER_MENU_ID,data.get(i).getFood_details().getOffers().get(j).getMenu_id());
                values.put(COLUMN_OFFER_STATUS,data.get(i).getFood_details().getOffers().get(j).getStatus());
                values.put(COLUMN_OFFER_START_DATE,data.get(i).getFood_details().getOffers().get(j).getStart_date());
                database.insert(TABLE_OFFERS, null, values);
            }
        }

        database.close();
        Log.d(TAG, "values inserted into offer table");
    }

    public List<Menu.DataEntity.FoodDetailsEntity.OffersEntity> fetchOffers(int item_id) {

        List<Menu.DataEntity.FoodDetailsEntity.OffersEntity> offers = new ArrayList<>();
        Menu.DataEntity.FoodDetailsEntity.OffersEntity offerenty;
        database=this.getReadableDatabase();

        String querry="select * from "+TABLE_OFFERS;
        Cursor cursor=database.rawQuery(querry,null);


        if(cursor.moveToFirst())
        {
            do {
                if(cursor.getInt(5)==item_id)//get menu id of the offer to compare with parameter
                {
                    offerenty=new Menu.DataEntity.FoodDetailsEntity.OffersEntity();
                    offerenty.setId(cursor.getInt(0));
                    offerenty.setOffer_name(cursor.getString(1));
                    offerenty.setDescription(cursor.getString(2));
                    offerenty.setQty(cursor.getInt(3));
                    offerenty.setPrice(cursor.getInt(4));
                    offerenty.setMenu_id(cursor.getInt(5));
                    offerenty.setStatus(cursor.getInt(6));
                    offerenty.setStart_date(cursor.getString(7));
                    offers.add(offerenty);

                }

            }while (cursor.moveToNext());
        }
        return offers;
    }
}
