package com.project.syz.account_management.mainfunc;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.syz.account_management.R;
import com.project.syz.account_management.database.DBHelper;
import com.project.syz.account_management.database.GraphicItem;
import com.project.syz.account_management.database.LineChartItem;
import com.project.syz.account_management.graphic.ExpendGraphicFragment;
import com.project.syz.account_management.graphic.IncomeGraphicFragment;
import com.project.syz.account_management.graphic.LineChartFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class YearStatisticActivity extends ActionBarActivity {

    private Spinner spyear;
    private ArrayAdapter<String> adapterSpYear;
    private ArrayList<String> dataYear = new ArrayList<String>();
    private String yearmonthStr;
    private static DBHelper dbhelper;
    private TextView income,expenditure,balance;

    private float incomeMonth = 0;
    private float expenditureMonth = 0;
    private float balanceMonth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_statistic);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MonthStatisticActivity.PlaceholderFragment())
                    .commit();
        }

        spyear = (Spinner) findViewById(R.id.spyear);
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
    }

    public void detailIncome(View view){
        String[] mItems_income = getResources().getStringArray(R.array.income);
        float[] fmoney = new float[mItems_income.length];
        String sql;
        Cursor cursor;
        GraphicItem newItem;

        yearmonthStr = spyear.getSelectedItem().toString();

        for(int i = 0; i < mItems_income.length; i++) {
            sql = "select sum(money) from income where substr(time,1,4) = '" + yearmonthStr + "' and type = '" + mItems_income[i] + "'";
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

        yearmonthStr = spyear.getSelectedItem().toString();

        for(int i = 0; i < mItems_expend.length; i++) {
            sql = "select sum(money) from expenditure where substr(time,1,4) = '" + yearmonthStr + "' and type = '" + mItems_expend[i] + "'";
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

    public void lineChart(View view){
        String sqlIncome,sqlExpend;
        int month = 0;
        String yearMonth;
        LineChartItem lChart;
        Cursor cursor_income,cursor_expenditure;
        float fIncome[] = new float[12];
        float fExpend[] = new float[12];
        yearmonthStr = spyear.getSelectedItem().toString();
        /*
        for(int i = 1; i < 13; i++){
            if(i < 10) {
                yearMonth = yearmonthStr + "-0" + i;
            }
            else{
                yearMonth = yearmonthStr + "-" + i;
            }
            sqlIncome = "select sum(money) from income where substr(time,1,7) = '" + yearMonth + "'";
            sqlExpend = "select sum(money) from expenditure where substr(time,1,7) = '" + yearMonth + "'";

            cursor_income = dbhelper.QueryBySql(sqlIncome);

        }
        */

//        sqlIncome = "select ifnull(sum(tt.money),0) from( select month, ifnull(time,month) n_time, money from (select  month, substr(time,6,2) time,money from month left outer join income on substr(time,6,2) = month.month where  substr(time,1,4)='" + yearmonthStr + "' or substr(time,1,4) is null) t) tt group by tt.n_time order by tt.month";
//        sqlExpend = "select ifnull(sum(tt.money),0) from( select month, ifnull(time,month) n_time, money from (select  month, substr(time,6,2) time,money from month left outer join expenditure on substr(time,6,2) = month.month where  substr(time,1,4)='" + yearmonthStr + "' or substr(time,1,4) is null) t) tt group by tt.n_time order by tt.month";
        sqlIncome = "select ifnull(sum(money),0) from month left outer join income on month.month=substr(time,6,2) and substr(time,1,4)='" + yearmonthStr + "' group by month.month order by month.month";
        sqlExpend = "select ifnull(sum(money),0) from month left outer join expenditure on month.month=substr(time,6,2) and substr(time,1,4)='" + yearmonthStr + "' group by month.month order by month.month";
        cursor_income = dbhelper.QueryBySql(sqlIncome);

        while(cursor_income.moveToNext()) {
            fIncome[month] = cursor_income.getFloat(0);
            fIncome[month] = (float)(Math.round(fIncome[month]*100))/100;
            month++;
        }

        cursor_expenditure = dbhelper.QueryBySql(sqlExpend);
        month = 0;
        while(cursor_expenditure.moveToNext()) {
            fExpend[month] = cursor_expenditure.getFloat(0);
            fExpend[month] = (float)(Math.round(fExpend[month]*100))/100;
            month++;
        }
        lChart = new LineChartItem(fIncome,fExpend);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, LineChartFragment.newInstance(lChart)).commit();

    }

    public void search(View view){

        String sql;
        Cursor cursor_income;
        Cursor cursor_expenditure;
        yearmonthStr = spyear.getSelectedItem().toString();
        sql = "select sum(money) from income where substr(time,1,4) = '" + yearmonthStr +"'";
        cursor_income = dbhelper.QueryBySql(sql);
        while(cursor_income.moveToNext()) {
            incomeMonth = cursor_income.getFloat(0);
        }
        incomeMonth = (float)(Math.round(incomeMonth*100))/100;

        sql = "select sum(money) from expenditure where substr(time,1,4) = '" + yearmonthStr +"'";
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
        getMenuInflater().inflate(R.menu.menu_year_statistic, menu);
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
