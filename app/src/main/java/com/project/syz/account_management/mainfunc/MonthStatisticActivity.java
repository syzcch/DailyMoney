package com.project.syz.account_management.mainfunc;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.syz.account_management.R;
import com.project.syz.account_management.database.DBHelper;
import com.project.syz.account_management.database.GraphicItem;
import com.project.syz.account_management.graphic.ExpendGraphicFragment;
import com.project.syz.account_management.graphic.IncomeGraphicFragment;
import com.project.syz.account_management.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;

//import org.achartengine.ChartFactory;
//import org.achartengine.GraphicalView;

public class MonthStatisticActivity extends ActionBarActivity {

    private Spinner spyear,spmonth;
    private ArrayAdapter<String> adapterSpYear;
    private ArrayAdapter<String> adapterSpMonth;
    private ArrayList<String> dataYear = new ArrayList<String>();
    private ArrayList<String> dataMonth = new ArrayList<String>();
    private String yearmonthStr;
    private static DBHelper dbhelper;

    private TextView income,expenditure,balance;

    private float incomeMonth = 0;
    private float expenditureMonth = 0;
    private float balanceMonth = 0;

//    GraphicalView graphicalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_statistic);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        spyear = (Spinner) findViewById(R.id.spyear);
        spmonth = (Spinner) findViewById(R.id.spmonth);
        dbhelper = DBHelper.getInstance();

        income = (TextView) findViewById(R.id.income);
        expenditure = (TextView) findViewById(R.id.expenditure);
        balance = (TextView) findViewById(R.id.balance);

        setStatisticInfo();
    }

    private void setStatisticInfo(){
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

    public void detailIncome(View view){
        String[] mItems_income = getResources().getStringArray(R.array.income);
        float[] fmoney = new float[mItems_income.length];
        String sql;
        Cursor cursor;
        GraphicItem newItem;

        yearmonthStr = spyear.getSelectedItem().toString() + "-" + spmonth.getSelectedItem().toString();

        for(int i = 0; i < mItems_income.length; i++) {
            sql = "select sum(money) from income where substr(time,1,7) = '" + yearmonthStr + "' and type = '" + mItems_income[i] + "'";
            cursor = dbhelper.QueryBySql(sql);
            if(cursor.getCount() == 0){
                Toast.makeText(this, "No result in the date you selected", Toast.LENGTH_LONG).show();
                return;
            }

            while(cursor.moveToNext()) {
                fmoney[i] = cursor.getFloat(0);
            }
        }

        newItem = new GraphicItem(mItems_income,fmoney);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, IncomeGraphicFragment.newInstance(newItem)).commit();
    }

    public void detailExpenditure(View view){
        String[] mItems_expend = getResources().getStringArray(R.array.expend);
        float[] fmoney = new float[mItems_expend.length];
        String sql;
        Cursor cursor;
        GraphicItem newItem;

        yearmonthStr = spyear.getSelectedItem().toString() + "-" + spmonth.getSelectedItem().toString();

        for(int i = 0; i < mItems_expend.length; i++) {
            sql = "select sum(money) from expenditure where substr(time,1,7) = '" + yearmonthStr + "' and type = '" + mItems_expend[i] + "'";
            cursor = dbhelper.QueryBySql(sql);
            if(cursor.getCount() == 0){
                Toast.makeText(this, "No result in the date you selected", Toast.LENGTH_LONG).show();
                return;
            }

            while(cursor.moveToNext()) {
                fmoney[i] = cursor.getFloat(0);
            }
        }

        newItem = new GraphicItem(mItems_expend,fmoney);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ExpendGraphicFragment.newInstance(newItem)).commit();
    }

    public void search(View view){

        String sql;
        Cursor cursor_income;
        Cursor cursor_expenditure;
        yearmonthStr = spyear.getSelectedItem().toString() + "-" + spmonth.getSelectedItem().toString();
        sql = "select sum(money) from income where substr(time,1,7) = '" + yearmonthStr +"'";
        cursor_income = dbhelper.QueryBySql(sql);
        while(cursor_income.moveToNext()) {
            incomeMonth = cursor_income.getFloat(0);
        }
        incomeMonth = (float)(Math.round(incomeMonth*100))/100;

        sql = "select sum(money) from expenditure where substr(time,1,7) = '" + yearmonthStr +"'";
        cursor_expenditure = dbhelper.QueryBySql(sql);
        while(cursor_expenditure.moveToNext()) {
            expenditureMonth = cursor_expenditure.getFloat(0);
        }
        expenditureMonth = (float)(Math.round(expenditureMonth*100))/100;

        balanceMonth = incomeMonth - expenditureMonth;
        balanceMonth = (float)(Math.round(balanceMonth*100))/100;

        income.setText( String.valueOf(incomeMonth));
        income.setTextColor(android.graphics.Color.RED);
        expenditure.setText(String.valueOf(expenditureMonth));
        expenditure.setTextColor(android.graphics.Color.RED);
        balance.setText(String.valueOf(balanceMonth));
        balance.setTextColor(android.graphics.Color.RED);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_month_statistic, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_month_statistic, container, false);
            return rootView;
        }
    }
}
