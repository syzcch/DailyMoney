package com.project.syz.account_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.project.syz.account_management.database.DBHelper;
import com.project.syz.account_management.mainfunc.AboutActivity;
import com.project.syz.account_management.mainfunc.AddActivity;
import com.project.syz.account_management.mainfunc.BudgetActivity;
import com.project.syz.account_management.mainfunc.EditActivity;
import com.project.syz.account_management.mainfunc.MonthStatisticActivity;
import com.project.syz.account_management.mainfunc.YearStatisticActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    private String texts[] = null;
    private int images[] = null;
    private long exitTime = 0;
    private static File dir,file;
    private static final String DATABASE_NAME = "account.db3";
    private static DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDatabase();
        setGridView();
    }

    private void loadDatabase(){
 //       dbhelper = new DBHelper(this,DATABASE_NAME);
        dbhelper = DBHelper.getInstance();
        dir = new File("data/data/" + getPackageName() + "/databases");
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        file = new File(dir, DATABASE_NAME);

        try {
            if (!file.exists()) {
                dbhelper.CreateDB(file);
                dbhelper.CreateTable();
            }
            else{
                dbhelper.CreateDB(file);
            }

            /*
            if (!file.exists()) {
                dbhelper.CreateDB();
                dbhelper.CreateTable();
            }
            */
        } catch (Exception e) {
            Log.e("Database", "Creating database error!");
            e.printStackTrace();
        }
    }

    private void setGridView() {
        images=new int[]{R.drawable.btn, R.drawable.btn_edit,
                R.drawable.btn_check, R.drawable.btn_monthstatistics,
                R.drawable.btn_yearstatistics,R.drawable.btn_finance,
                R.drawable.btn_about,R.drawable.btn_exit};
        texts = new String[]{ getString(R.string.add), getString(R.string.deledit),
                getString(R.string.check), getString(R.string.monthstatistics),getString(R.string.yearstatistics),
                getString(R.string.finance), getString(R.string.about),
                getString(R.string.exit)};

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < texts.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", images[i]);
            map.put("itemText", texts[i]);
            lstImageItem.add(map);
        }

        SimpleAdapter saImageItems = new SimpleAdapter(this,
                lstImageItem,// 数据源
                R.layout.night_item,// 显示布局
                new String[] { "itemImage", "itemText" },
                new int[] { R.id.itemImage, R.id.itemText });
        gridview.setAdapter(saImageItems);
        gridview.setOnItemClickListener(new ItemClickListener());
    }

    class ItemClickListener implements AdapterView.OnItemClickListener {
        /**
         * 点击项时触发事件
         *
         * @param parent  发生点击动作的AdapterView
         * @param view 在AdapterView中被点击的视图(它是由adapter提供的一个视图)。
         * @param position 视图在adapter中的位置。
         * @param rowid 被点击元素的行id。
         */
        public void onItemClick(AdapterView<?> parent, View view, int position, long rowid) {
            HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
            //获取数据源的属性值
            String itemText=(String)item.get("itemText");
            Object object=item.get("itemImage");
 //           Toast.makeText(GvActivity.this, itemText, Toast.LENGTH_LONG).show();

            //根据图片进行相应的跳转
            switch (images[position]) {
                case R.drawable.btn:
                    /*
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
//                    intent.setClass(MainActivity.this, AddActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dbhelper", dbhelper);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    */
                  startActivity(new Intent(MainActivity.this, AddActivity.class));//启动另一个Activity
 //                   finish();//结束此Activity，可回收
                    break;
                case R.drawable.btn_edit:
                    /*
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
//                    intent.setClass(MainActivity.this, AddActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dbhelper", dbhelper);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    */
                    startActivity(new Intent(MainActivity.this, EditActivity.class));//启动另一个Activity
                    //                   finish();//结束此Activity，可回收
                    break;
                case R.drawable.btn_check:
                    startActivity(new Intent(MainActivity.this, BudgetActivity.class));//启动另一个Activity
                    break;
                case R.drawable.btn_monthstatistics:
                    startActivity(new Intent(MainActivity.this, MonthStatisticActivity.class));//启动另一个Activity
                    break;
                case R.drawable.btn_yearstatistics:
                    startActivity(new Intent(MainActivity.this, YearStatisticActivity.class));//启动另一个Activity
                    break;
                case R.drawable.btn_finance:
                    break;
                case R.drawable.btn_about:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));//启动另一个Activity
                    break;
                case R.drawable.btn_exit:
                    finish();
                    System.exit(0);
                    break;
            }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_gone) {
            finish();
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else{
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
