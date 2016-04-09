package com.project.syz.account_management.mainfunc;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.syz.account_management.R;
import com.project.syz.account_management.database.DBHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class AddActivity extends ActionBarActivity {

    private TextView dateInfo,balanceMoney;
    private Spinner categorySpin;
    private RadioGroup radioType;
    private String typeStr = "expenditure";
    private static DBHelper dbhelper;
    private Date date;
    private String typeString,memoString;
    private float money;
    private EditText moneyEdit,memoEdit;
    private String  dateStr;
    private float balance=0;
//    private RunBalance aTask;

    //for test
    private TextView searchres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Intent intent = this.getIntent();
//        dbhelper=(DBHelper)intent.getSerializableExtra("dbhelper");
        dbhelper = DBHelper.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        dateInfo = (TextView) findViewById(R.id.dateInfo);
        categorySpin = (Spinner) findViewById(R.id.categorySpin);
//        incomeSpin = (Spinner) findViewById(R.id.incomeSpin);
        radioType = (RadioGroup) findViewById(R.id.radioType);
        moneyEdit = (EditText)findViewById(R.id.editMoney);
        memoEdit = (EditText)findViewById(R.id.editMemo);
//        searchres = (TextView)findViewById(R.id.searchres);
        balanceMoney  = (TextView) findViewById(R.id.balanceMoney);

//        aTask = new RunBalance();
//        aTask.execute("");
        setAddInfo();
    }

    // re-caculate balance
    protected void onResume(Bundle savedInstanceState) {
        super.onResume();
        setBalance();
    }

    private void setAddInfo(){
        setMoneyFilter();
        setDateInfo();
        setCateSpin();
        setBalance();
    }

    private void setBalance(){
        balance = dbhelper.CalDayBalance(dateStr);
        balance = (float)(Math.round(balance*100))/100;
        balanceMoney.setText(String.valueOf(balance));
    }

    private void setMoneyFilter(){
        moneyEdit.addTextChangedListener(new TextWatcher() {
            private boolean isChanged = false;
/*
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
            }
*/
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        moneyEdit.setText(s);
                        moneyEdit.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    moneyEdit.setText(s);
                    moneyEdit.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        moneyEdit.setText(s.subSequence(0, 1));
                        moneyEdit.setSelection(1);
                        return;
                    }
                }
            }
        });
    }

    private void setCateSpin() {
        // get type here
        radioType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                typeStr=(id == R.id.income?"income":"expenditure");
            }

        });
        /*  another method
        for(int i=0; i<radioType.getChildCount(); i++){
            RadioButton r = (RadioButton)radioType.getChildAt(i);
            if(r.isChecked()){
                typeStr = r.getText().toString();
                break;
            }
        }
        */

        // 建立数据源
        String[] mItems_expend = getResources().getStringArray(R.array.expend);
        String[] mItems_income = getResources().getStringArray(R.array.income);
        // 建立Adapter并且绑定数据源
//        ArrayAdapter<String> _Adapter_expend=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems_expend);
//        ArrayAdapter<String> _Adapter_income=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems_income);
        //绑定 Adapter到控件
//        categorySpin.setAdapter(_Adapter_expend);
//        incomeSpin.setAdapter(_Adapter_income);
/*
        if(typeStr.isEmpty() && typeStr.equals("Income")){
            categorySpin.invalidate();
        }
        else if(typeStr.isEmpty() && typeStr.equals("expenditure")){
            incomeSpin.invalidate();
        }
*/
        MyAdapter _MyAdapter=new MyAdapter(this, mItems_expend, mItems_income);
        //绑定Adapter
        categorySpin.setAdapter(_MyAdapter);;
    }

    class MyAdapter extends BaseAdapter {
        private String[] mItems_expend;
        private String[] mItems_income;
        private Context mContext;

        public MyAdapter(Context pContext, String[] mItems_expend, String[] mItems_income) {

            this.mContext = pContext;
            this.mItems_expend = mItems_expend;
            this.mItems_income = mItems_income;
        }

        @Override
        public int getCount() {
            if(!typeStr.isEmpty() && typeStr.equals("income")) {
                return mItems_income.length;
            }
            else if(!typeStr.isEmpty() && typeStr.equals("expenditure")){
                return mItems_expend.length;
            }
            return mItems_expend.length;
        }

        @Override
        public Object getItem(int position) {
            if(!typeStr.isEmpty() && typeStr.equals("income")) {
                return mItems_income[position];
            }
            else if(!typeStr.isEmpty() && typeStr.equals("expenditure")){
                return mItems_expend[position];
            }
            return mItems_expend[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        /**
         * 下面是重要代码
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
            convertView=_LayoutInflater.inflate(android.R.layout.simple_spinner_item, null);
            if(convertView!=null)
            {
                if(!typeStr.isEmpty() && typeStr.equals("income")) {
                    ((TextView)convertView).setText(mItems_income[position]);
                }
                else if(!typeStr.isEmpty() && typeStr.equals("expenditure")){
                    ((TextView)convertView).setText(mItems_expend[position]);
                }
            }
            return convertView;
        }
    }
/*
    private class RunBalance extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            // params comes from the execute() call: params[0] is the url.
            try {
                balance = dbhelper.CalDayBalance(dateStr);
                return "Sucessful!";
            } catch (Exception e) {
                Log.e("Database Loading", "Error in loading database");
                e.printStackTrace();
                return "Error in loading database";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            balanceMoney.setText(String.valueOf(balance));
//            btLoad.setText("閲嶆柊鍔犺浇");
        }
    }
*/
    public void submit(View view) {
        typeString = categorySpin.getSelectedItem().toString();

        if(moneyEdit.getText().toString().isEmpty()){
            Toast.makeText(this, "Please input money to record info", Toast.LENGTH_LONG).show();
            return;
        }

        money = Float.parseFloat(moneyEdit.getText().toString());
        money = (float)(Math.round(money*100))/100;
        memoString = memoEdit.getText().toString();

        if (typeStr.equals("income")){
            dbhelper.InsertIncome(typeString, money, memoString, date);
        }
        // expenditure
        else{
            dbhelper.InsertExpenditure(typeString, money, memoString, date);
        }
        moneyEdit.setText("");
        memoEdit.setText("");
        setBalance();
 //       aTask.execute("");
    }

    public void search(View view) {

        String res = dbhelper.search("expenditure");
        searchres.setText(res);

    }

    private void setDateInfo(){
        SimpleDateFormat sDateFormat = new  SimpleDateFormat("yyyy-MM-dd");
        date = new Date(System.currentTimeMillis());
        dateStr =  sDateFormat.format(date);
        dateInfo.setText(dateStr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
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
