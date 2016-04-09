package com.project.syz.account_management.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.sql.Date;

/**
 * Created by 2 on 2016/3/16.
 */
public class DBHelper{
//    private static final long serialVersionUID = -7060210544600464481L;
    private static final String TAG = "DBHelper";// 调试标签
    private static DBHelper singleDBHelper = null;
/*
    private static  String DATABASE_NAME;// 数据库名
    private SQLiteDatabase db;
    Context context;//应用环境上下文   Activity 是其子类
    private static File dir,file;
*/
//    private   String DATABASE_NAME;// 数据库名
    private static SQLiteDatabase db;
//    Context context;//应用环境上下文   Activity 是其子类
//    private  File dir,file;
/*
    public DBHelper(Context _context, String dbname) {
        context = _context;
        DATABASE_NAME = dbname;
    }
*/
    public synchronized  static DBHelper getInstance() {
        if (singleDBHelper == null) {
            singleDBHelper = new DBHelper();
        }
        return singleDBHelper;
    }

    public void CreateDB(File file){
        db = SQLiteDatabase.openOrCreateDatabase(file,null);
//        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE,null);
        Log.d(TAG, "Create DB  ok");
    }

    public void CreateTable(){
        try {
            db.execSQL("CREATE TABLE expenditure (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "type  varchar(20),"
                    + "money float,"
                    + " memo varchar(100),"
                    + " time date"
                    + ");");
            db.execSQL("CREATE TABLE income (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "type  varchar(20),"
                    + "money float,"
                    + " memo varchar(100),"
                    + " time date"
                    + ");");
            db.execSQL("CREATE TABLE budget (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "money float,"
                    + " time date"
                    + ");");
            db.execSQL("CREATE TABLE month (" +
                    "month string"
                    + ");");
            for(int i = 1;i < 13; i++) {
                db.execSQL("INSERT INTO month values (?)", new Object[]{i});
            }
            Log.d(TAG, "Create Table expenditure ok");
        } catch (Exception e) {
            Log.d(TAG, "Create Table expenditure err,table exists.");
            e.printStackTrace();
        }
    }

    public void InsertIncome(String type,float money, String meno, Date date){
//        String sql = "INSERT INTO income values ( " + type + "," + money + "," + meno + "," + date + ")";
        try {
            db.execSQL("INSERT INTO income values (null,?,?,?,?)",new Object[]{type,money,meno,date});
            Log.d(TAG, "insert into Table income ok");
        } catch (Exception e) {
            Log.d(TAG, "insert into Table income err!");
//            e.printStackTrace();
        }
    }

    // not use then
    public void InsertMonth(int month){
//        String sql = "INSERT INTO income values ( " + type + "," + money + "," + meno + "," + date + ")";
        try {
            db.execSQL("INSERT INTO month values (?)",new Object[]{month});
            Log.d(TAG, "insert into Table month ok");
        } catch (Exception e) {
            Log.d(TAG, "insert into Table month err!");
//            e.printStackTrace();
        }
    }

    public void DeleteItem(String tb_name,int _id){
        String sql = "delete from " + tb_name + " where _id = " + _id;
        try {
            db.execSQL(sql);
            Log.d(TAG, "DeleteItem ok");
        } catch (Exception e) {
            Log.d(TAG, "DeleteItem err!");
            e.printStackTrace();
        }
    }

    public void UpdateItem(String tb_name,float money, String memo, String typeString, int _id){
//        String sql = "update " + tb_name + " set money= " + money + " and memo = '" + memo + "' where _id= " + _id;
        ContentValues cv = new ContentValues();
        cv.put("money", money);
        cv.put("memo", memo);
        cv.put("type",typeString);
//        cv.put("content", content);
//        cv.put("tag", tag);
        String[] args = {String.valueOf(_id)};

        try {
//            db.execSQL(sql);
            db.update(tb_name, cv, "_id=?",args);
            Log.d(TAG, "UpdateItem ok");
        } catch (Exception e) {
            Log.d(TAG, "UpdateItem err!");
            e.printStackTrace();
        }
    }

    public void InsertExpenditure(String type,float money, String meno, Date date){
    //    String sql = "INSERT INTO income values ( " + type + "," + money + "," + meno + "," + date + ")";
        try {
            db.execSQL("INSERT INTO expenditure values (null,?,?,?,?)",new Object[]{type,money,meno,date});
    //        db.execSQL(sql);
            Log.d(TAG, "insert into Table expenditure ok");
        } catch (Exception e) {
            Log.d(TAG, "insert into Table expenditure err!");
        }
    }

    public Cursor QueryBySql(String sql){
        Cursor res = null;
        try {
            res = db.rawQuery(sql ,null);
            Log.d(TAG, "QueryBySql search ok");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "QueryBySql search err!");
        }
        return res;
    }

    public float CalDayBalance(String date){
        //    String sql = "INSERT INTO income values ( " + type + "," + money + "," + meno + "," + date + ")";
        Cursor cIncom = null;
        Cursor cExpend = null;
        float fIncome = 0;
        float fExpend = 0;
        try {
            cExpend = db.rawQuery("select sum(money) from expenditure where time = '"+date+"'" ,null);
            cIncom = db.rawQuery("select sum(money) from income where time  = '"+date+"'" ,null);
//            cIncom = db.rawQuery("select sum(money) from income where date =  ?",new String[]{date});
//            cExpend = db.rawQuery("select sum(money) from expenditure where time = ?",new String[]{date});
            while(cExpend.moveToNext()){
                fExpend = cExpend.getFloat(0);
            }
            while(cIncom.moveToNext()){
                fIncome = cIncom.getFloat(0);
            }
            Log.d(TAG, "balance search ok");
        } catch (Exception e) {
            Log.d(TAG, "balance search err!");
        }
        return fIncome - fExpend;
    }

    public String search(String type){
        String sql = "select * from " + type;
        String res = "";
        Cursor c = null;

 //       String sql = "INSERT INTO income values ( " + type + "," + money + "," + meno + "," + date + ")";
        try {
            c = db.rawQuery(sql, null);
            while(c.moveToNext()){
                res += c.getString(0);
                res += c.getFloat(1);
                res += c.getString(2);
                res += c.getString(3);
                res += "/n";
            }
            Log.d(TAG, "search Table ok");
        } catch (Exception e) {
            Log.d(TAG, "search Table err!");
        }
        return res;
    }
}
