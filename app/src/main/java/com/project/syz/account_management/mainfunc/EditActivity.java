package com.project.syz.account_management.mainfunc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.project.syz.account_management.R;
import com.project.syz.account_management.database.DBHelper;
import com.project.syz.account_management.database.DBItem;
import com.project.syz.account_management.utils.DateTimeUtil;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditActivity extends ActionBarActivity {

    private Spinner spyear,spmonth;
    private ListView listView;
    private ArrayAdapter<String> adapterSpYear;
    private ArrayAdapter<String> adapterSpMonth;
    private ArrayList<String> dataYear = new ArrayList<String>();
    private ArrayList<String> dataMonth = new ArrayList<String>();

    private RadioGroup radioType;
    private String typeStr = "expenditure";

    private String yearmonthStr;
    private int monthInt;
    private Date date;

    private static DBHelper dbhelper;
    private Context context;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelper = DBHelper.getInstance();
        setContentView(R.layout.activity_edit);
        spyear = (Spinner) findViewById(R.id.spyear);
        spmonth = (Spinner) findViewById(R.id.spmonth);
        listView  = (ListView) findViewById(R.id.listView);
        radioType = (RadioGroup) findViewById(R.id.radioType);
        context = this;
        setEditInfo();
    }

    private void setEditInfo(){
        radioType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                typeStr=(id == R.id.income?"income":"expenditure");
            }

        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(id<0) {
                    return;
                }
                ListView listView = (ListView) parent;
                Cursor cTmp = (Cursor)listView.getItemAtPosition(position);
                int _id = cTmp.getInt(0);
                String type = cTmp.getString(1);
                float money = cTmp.getFloat(2);
                String time = cTmp.getString(3);
                String memo =  cTmp.getString(4);

                DBItem dbItem = new DBItem();
                dbItem.set_id(_id);
                dbItem.setType(type);
                dbItem.setMoney(money);
                dbItem.setTime(time);
                dbItem.setMemo(memo);
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("DBItem", dbItem);
                intent.putExtras(bundle);
                intent.putExtra("typeStr",typeStr);
                startActivityForResult(intent,1);
 //               startActivity(intent);
 //!               Toast.makeText(context, type + " , " + money + " , " + time  + " , "  + memo  + " , " + _id, Toast.LENGTH_LONG).show();
            }
        });
        setDefaultDateInfo();
        setDateInfo();
        setAdapterInfo();
//        setCateSpin();
//        setBalance();
    }

    // re-show infos for current month
    /*
    protected void onResume(Bundle savedInstanceState) {
        super.onResume();
//        yearmonthStr = spyear.getSelectedItem().toString() + "-" + spmonth.getSelectedItem().toString();
        setDefaultDateInfo();
        setAdapterInfo();
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setDefaultDateInfo();
        setAdapterInfo();
    }

    private void setAdapterInfo(){

        String sql;
        if (typeStr.equals("income")){
            sql = "select _id,type,money,time,memo from income where substr(time,1,7) = '" + yearmonthStr +"'";
        }
        else{
            sql = "select _id,type,money,time,memo from expenditure where substr(time,1,7) = '" + yearmonthStr +"'";
        }
        cursor = dbhelper.QueryBySql(sql);

        adapter = new SimpleCursorAdapter(context,
                R.layout.listview_item, cursor, new String[] { "type", "money",  "time", "_id"},
                new int[] { R.id.text1, R.id.text2, R.id.text3, R.id.text4 }, 0);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
    }

    private void setDefaultDateInfo(){
        SimpleDateFormat sDateFormat = new  SimpleDateFormat("yyyy-MM-dd");
        date = new Date(System.currentTimeMillis());
        yearmonthStr =  sDateFormat.format(date);
        monthInt = Integer.parseInt(yearmonthStr.substring(5,7));
        yearmonthStr = yearmonthStr.substring(0,7);
    }

    public void search(View view){
        yearmonthStr = spyear.getSelectedItem().toString() + "-" + spmonth.getSelectedItem().toString();
        setAdapterInfo();
    }

    private void setDateInfo(){
        Calendar cal = Calendar.getInstance();
        // edit and delete at most 5 years info
        for (int i = 0; i <= 5; i++) {
            dataYear.add("" + (cal.get(Calendar.YEAR) - 5 + i));
        }
        adapterSpYear = new ArrayAdapter<String>(context, R.layout.spinner_item, dataYear);
        adapterSpYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spyear.setAdapter(adapterSpYear);
        spyear.setSelection(5);// 2016 default

        // 12 months
        for (int i = 1; i <= 12; i++) {
            dataMonth.add("" + (i < 10 ? "0" + i : i));
        }
        adapterSpMonth = new ArrayAdapter<String>(context, R.layout.spinner_item, dataMonth);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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
