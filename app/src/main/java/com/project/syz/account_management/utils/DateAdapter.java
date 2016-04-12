package com.project.syz.account_management.utils;

/**
 * Created by 2 on 2016/4/8.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.project.syz.account_management.R;
import com.project.syz.account_management.database.DateText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DateAdapter extends BaseAdapter {
    private Context context;
    private List<DateText> list;
    List liste = new ArrayList<String>();
    String[] mItems_expend;

    public DateAdapter(Context context, List<DateText> list) {
        this.context = context;
        this.list = list;
        mItems_expend = context.getResources().getStringArray(R.array.expend);
        this.liste = Arrays.asList(mItems_expend);
    }

    public void setList(List<DateText> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int index = 0;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_time_line, parent, false);
            holder.date = (TextView) convertView
                    .findViewById(R.id.txt_date_time);
            holder.content = (TextView) convertView
                    .findViewById(R.id.txt_date_content);
            holder.line = (View) convertView.findViewById(R.id.v_line);
            holder.title = (RelativeLayout) convertView
                    .findViewById(R.id.rl_title);
            holder.icon = (ImageView) convertView.findViewById(R.id.v_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //时间轴竖线的layout
        LayoutParams params = (LayoutParams) holder.line.getLayoutParams();
        //第一条数据，肯定显示时间标题
        if (position == 0) {
            holder.title.setVisibility(View.VISIBLE);
//            holder.date.setText(TimeFormat.format("yyyy.MM.dd",
//                    list.get(position).getDate()));
            holder.date.setText(list.get(position).getDate());
            params.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_title);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.txt_date_content);
        } else { // 不是第一条数据
            // 本条数据和上一条数据的时间戳相同，时间标题不显示
            if (list.get(position).getDate()
                    .equals(list.get(position - 1).getDate())) {
                holder.title.setVisibility(View.GONE);
                params.addRule(RelativeLayout.ALIGN_TOP, R.id.txt_date_content);
                params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.txt_date_content);
            } else {
                //本条数据和上一条的数据的时间戳不同的时候，显示数据
                holder.title.setVisibility(View.VISIBLE);
 //               holder.date.setText(TimeFormat.format("yyyy.MM.dd",
 //                       list.get(position).getDate()));
                holder.date.setText(list.get(position).getDate());
                params.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_title);
                params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.txt_date_content);
            }
        }
        holder.line.setLayoutParams(params);
        holder.content.setText(list.get(position).getText());
        index= liste.indexOf(list.get(position).getType());
        if(index < 0){
            index = 0;
        }
        holder.icon.setImageResource(DateText.images[index]);
        return convertView;
    }

    public static class ViewHolder {
        RelativeLayout title;
        View line;
        TextView date;
        TextView content;
        ImageView icon;
    }
}
