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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.syz.account_management.R;
import com.project.syz.account_management.database.DBHelper;
import com.project.syz.account_management.database.DBItem;

public class DetailActivity extends ActionBarActivity {

    private TextView datetype,recordDate;
    private String typeStr;
    private Spinner categorySpin;
    private EditText moneyEdit,memoEdit;
    private String memoString,typeString;
    private float money;
    private static DBHelper dbhelper;
    private int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        datetype = (TextView) findViewById(R.id.datetype);
        recordDate = (TextView) findViewById(R.id.recordDate);
        DBItem dbItem = (DBItem)getIntent().getParcelableExtra("DBItem");
        typeStr = (String)getIntent().getStringExtra("typeStr");
        categorySpin = (Spinner) findViewById(R.id.categorySpin);
        moneyEdit = (EditText)findViewById(R.id.editMoney);
        memoEdit = (EditText)findViewById(R.id.editMemo);
        dbhelper = DBHelper.getInstance();
        setDetailInfo(dbItem);
    }

    private void setDetailInfo(DBItem dbItem){
        datetype.setText(typeStr);
        recordDate.setText(dbItem.getTime());
        moneyEdit.setText(String.valueOf(dbItem.getMoney()));
        setMoneyFilter();
        memoEdit.setText(dbItem.getMemo());
        _id = dbItem.get_id();
        setCateSpin(dbItem.getType());

//        setDateInfo();

    }

    public void submit(View view) {
        typeString = categorySpin.getSelectedItem().toString();
        if(moneyEdit.getText().toString().isEmpty()){
            Toast.makeText(this, "Please input money to record info", Toast.LENGTH_LONG).show();
            return;
        }
        money = Float.parseFloat(moneyEdit.getText().toString());
        money = (float)(Math.round(money*100))/100;
        memoString = memoEdit.getText().toString();
        dbhelper.UpdateItem(typeStr,money,memoString,typeString,_id);
        finish();
    }

    public void delete(View view) {
        dbhelper.DeleteItem(typeStr,_id);
        finish();
    }

    private void setCateSpin(String type) {
        int position = 0;
        // 建立数据源
        String[] mItems_expend = getResources().getStringArray(R.array.expend);
        String[] mItems_income = getResources().getStringArray(R.array.income);

        MyAdapter _MyAdapter=new MyAdapter(this, mItems_expend, mItems_income);
        //绑定Adapter
        categorySpin.setAdapter(_MyAdapter);
        if(typeStr.equals("income")){
            for(int i=0; i<mItems_income.length; i++){
                if(mItems_income[i].equals(type)){
                    position = i;
                    break;
                }
            }
        }
        else{
            for(int i=0; i<mItems_expend.length; i++){
                if(mItems_expend[i].equals(type)){
                    position = i;
                    break;
                }
            }
        }
        categorySpin.setSelection(position);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
