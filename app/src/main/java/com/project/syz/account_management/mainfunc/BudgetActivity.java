package com.project.syz.account_management.mainfunc;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.project.syz.account_management.R;
import com.project.syz.account_management.database.DBHelper;
import com.project.syz.account_management.database.DateText;
import com.project.syz.account_management.utils.DateAdapter;
import com.project.syz.account_management.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BudgetActivity extends ActionBarActivity {

    private Spinner spyear,spmonth;
    private static DBHelper dbhelper;
    private ArrayAdapter<String> adapterSpYear;
    private ArrayAdapter<String> adapterSpMonth;
    private ArrayList<String> dataYear = new ArrayList<String>();
    private ArrayList<String> dataMonth = new ArrayList<String>();
    private String yearmonthStr;

    private List<DateText> dxlist;

    private ListView lvList;
    private DateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        spyear = (Spinner) findViewById(R.id.spyear);
        spmonth = (Spinner) findViewById(R.id.spmonth);
        dbhelper = DBHelper.getInstance();
        lvList = (ListView) findViewById(R.id.lv_list);
        dxlist = new ArrayList<DateText>();

        setBudgetInfo();
    }

    private void setBudgetInfo(){
        setDateInfo();
    }

    private void setDateInfo(){
        Calendar cal = Calendar.getInstance();
        // edit and delete at most 5 years info
        for (int i = 0; i <= 5; i++) {
            dataYear.add("" + (cal.get(Calendar.YEAR) - 5 + i));
        }
        adapterSpYear = new ArrayAdapter<String>(this, R.layout.spinner_item, dataYear);
        adapterSpYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spyear.setAdapter(adapterSpYear);
        spyear.setSelection(5);// 2016 default

        // 12 months
        for (int i = 1; i <= 12; i++) {
            dataMonth.add("" + (i < 10 ? "0" + i : i));
        }
        adapterSpMonth = new ArrayAdapter<String>(this, R.layout.spinner_item, dataMonth);
        adapterSpMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spmonth.setAdapter(adapterSpMonth);
/*
        adapterSpDay = new ArrayAdapter<String>(this, R.layout.spinner_item, dataDay);
        adapterSpDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spday.setAdapter(adapterSpDay);
*/
        spmonth.setSelection(DateTimeUtil.getCurrentMonth()-1);
        spmonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //               dataDay.clear();
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.valueOf(spyear.getSelectedItem().toString()));
                cal.set(Calendar.MONTH, arg2);
//                int dayofm = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                /*
                for (int i = 1; i <= dayofm; i++) {
                    dataDay.add("" + (i < 10 ? "0" + i : i));
                }
                adapterSpDay.notifyDataSetChanged();
                */
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    public void search(View view){
        String sql;
        Cursor cursor_income;
        float money;
        String time,type,dxStr;

        yearmonthStr = spyear.getSelectedItem().toString() + "-" + spmonth.getSelectedItem().toString();
        sql = "select time,money,type from expenditure where substr(time,1,7) = '" + yearmonthStr +"' order by time";
        cursor_income = dbhelper.QueryBySql(sql);
        while(cursor_income.moveToNext()) {
            time = cursor_income.getString(0);
            money = cursor_income.getFloat(1);
            type = cursor_income.getString(2);
            dxStr = "spending " + String.valueOf(money) + "$  type is " + type;
            DateText dt = new DateText(time,dxStr);
            dxlist.add(dt);
        }
        adapter = new DateAdapter(this, dxlist);
        lvList.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
