package com.example.daisy.hellomyworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DatebaseHelper extends SQLiteOpenHelper {


    public static final String COST_MONEY = "cost_money";
    public static final String COST_DATE = "cost_date";
    public static final String COST_TITLE = "cost_title";
    public static final String MY_WORLD_COST = "myWorld_cost";

    public DatebaseHelper(Context context) {
        super(context, "myworld_daily", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists myWorld_cost("
            +"id int primary key,"
            +"cost_title VARCHAR,"
            +"cost_date VARCHAR,"
            +"cost_money VARCHAR)");
    }


    public void delete(List<CostBean> CostBeanList, int position){
        String delText=CostBeanList.get(position).costTitle;
        String delDate=CostBeanList.get(position).costDate;
        String delMon=CostBeanList.get(position).costMoney;
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL("DELETE FROM " + MY_WORLD_COST + " WHERE cost_title='"
                            + delText+"'"+" AND cost_date='"+delDate+"'"+" AND cost_money='"+delMon+"'");
        database.close();
    }

    public void insertCost(CostBean costBean){
        SQLiteDatabase database=getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(COST_TITLE,costBean.costTitle);
        cv.put(COST_DATE,costBean.costDate);
        cv.put(COST_MONEY,costBean.costMoney);
        database.insert(MY_WORLD_COST,null,cv);
        database.close();
    }
    public Cursor getAllCostDate(){
        SQLiteDatabase database=getWritableDatabase();
        return database.query(MY_WORLD_COST,null,null,
                null,null,null,COST_DATE+" ASC");
    }
    public void deleteAllDate(){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(MY_WORLD_COST,null,null);
        database.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
