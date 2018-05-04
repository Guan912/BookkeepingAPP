package com.example.daisy.hellomyworld;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.security.PublicKey;
import java.util.List;

import javax.security.auth.callback.Callback;

public class CostListAdapter extends BaseAdapter{

    private List<CostBean> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;


    public CostListAdapter(Context context,List<CostBean> list){
        mContext=context;
        mList=list;
        mLayoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.list_item,null);

//            Button button=(Button)convertView.findViewById(R.id.bt_del);
//            button.setOnClickListener(new View.OnClickListener(){
//                public void onClick(View view){
//                    mCallBack.onClick(view);
//                }
//            });


            viewHolder.mTvCostTitle=(TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.mTvCostDate=(TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.mTvCostMoney=(TextView) convertView.findViewById(R.id.tv_cost);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }

        CostBean bean=mList.get(position);

        viewHolder.mTvCostTitle.setText(bean.costTitle);
        viewHolder.mTvCostDate.setText(bean.costDate);
        viewHolder.mTvCostMoney.setText(bean.costMoney);

        return convertView;
    }

    private static class ViewHolder{

        public TextView mTvCostTitle;
        public TextView mTvCostDate;
        public TextView mTvCostMoney;

    }


}
