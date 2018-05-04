package com.example.daisy.hellomyworld;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    //180324
    private List<CostBean> mCostBeanList;
    private DatebaseHelper mDatebaseHelper;
    private CostListAdapter mAdapter;

    private int lastPress=0;
    private boolean delState=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //180324
        mDatebaseHelper=new DatebaseHelper(this);
        mCostBeanList=new ArrayList<>();
        ListView costList=(ListView)findViewById(R.id.lv_main);
        initCostData();
        mAdapter = new CostListAdapter(this, mCostBeanList);
        costList.setAdapter(mAdapter);


        //press to delete list item
//        costList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(delState){
//                    if(lastPress<parent.getCount()){
//                        View delview=parent.getChildAt(lastPress).findViewById(R.id.ln_del);
//                        if(null!=delview){
//                            delview.setVisibility(View.GONE);
//                        }
//                    }
//                    delState=false;
//                    return;
//                }else {
//                    Log.d("click:",position+" ");
//                }
////                lastPress=position;
//            }
//        });
        costList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            private View delview;
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (lastPress < parent.getCount()) {
                    delview = parent.getChildAt(lastPress).findViewById(R.id.ln_del);
                    if (null != delview) {
                        delview.setVisibility(View.GONE);
                    }
                }

                delview = view.findViewById(R.id.ln_del);
                delview.setVisibility(View.VISIBLE);

                delview.findViewById(R.id.tv_ldel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delview.setVisibility(View.GONE);
                        mDatebaseHelper.delete(mCostBeanList,position);
                        mCostBeanList.remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                delview.findViewById(R.id.tv_lcancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delview.setVisibility(View.GONE);
                    }
                });

                lastPress = position;
                delState = true;
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //180324
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflate=LayoutInflater.from(MainActivity.this);
                View viewDialog=inflate.inflate(R.layout.new_cost_daily,null);

                final EditText title=(EditText)viewDialog.findViewById(R.id.et_cost_title);
                final EditText money=(EditText)viewDialog.findViewById(R.id.et_cost_money);
                final DatePicker date=(DatePicker) viewDialog.findViewById(R.id.dp_cost_date);
                builder.setView(viewDialog);
                builder.setTitle("记 一 笔");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CostBean costBean=new CostBean();
                        if(money.getText().toString().length()==0||title.getText().toString().length()==0){
//                            System.out.print("the input is empty");
                        }else{
                            costBean.costTitle=title.getText().toString();
                            costBean.costMoney=money.getText().toString();
                            costBean.costDate=date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDayOfMonth();
                            mDatebaseHelper.insertCost(costBean);
                            mCostBeanList.add(costBean);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.create().show();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void initCostData() {//180324

//        mDatebaseHelper.deleteAllDate();
//        for (int i=0;i<3;i++) {
//            CostBean costBean = new CostBean();
//            costBean.costTitle=i+"fruits";
//            costBean.costDate="2018-3-23";
//            costBean.costMoney="20";
//            mDatebaseHelper.insertCost(costBean);
//        }
        Cursor cursor=mDatebaseHelper.getAllCostDate();
        if(cursor!=null){
            while (cursor.moveToNext()){
                CostBean costBean=new CostBean();
                costBean.costTitle=cursor.getString(cursor.getColumnIndex("cost_title"));
                costBean.costDate=cursor.getString(cursor.getColumnIndex("cost_date"));
                costBean.costMoney=cursor.getString(cursor.getColumnIndex("cost_money"));
                mCostBeanList.add(costBean);
            }
            cursor.close();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (id == R.id.action_clr) {
//            mDatebaseHelper.deleteAllDate();
//            mCostBeanList.clear();
//            mAdapter.notifyDataSetChanged();
//            return true;
//        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_chart) {
            Intent intent=new Intent(MainActivity.this,ChartsActivity.class);
            intent.putExtra("cost_list",(Serializable)mCostBeanList);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);


    }


}
