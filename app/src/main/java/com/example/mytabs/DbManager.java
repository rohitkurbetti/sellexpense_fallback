package com.example.mytabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.Date;

public class DbManager extends SQLiteOpenHelper {

    private static final String dbname = "Bill.db";
    SQLiteDatabase db = this.getWritableDatabase();
    public DbManager(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String str = "create table t_bill (id integer primary key autoincrement,billNo integer,custName text,orange integer ,kokam integer,lemon integer,sarbat integer,pachak integer,wala integer,lsoda integer,ssrbt integer,lorange integer,llemon integer,jsoda integer, sSoda integer,water integer, lassi integer ,vanilla integer,pista integer,stwbry integer,mango integer,btrsch integer,kulfi integer,cbar integer,fpack integer,other integer,other1 integer,total integer,addedondatetime Date,date Date)";
        db.execSQL(str);
        String str1 = "create table t_expense (id integer primary key autoincrement,selling integer, expense integer,profit integer,date Date,expenses text)";
        db.execSQL(str1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists t_bill");
        db.execSQL("drop table if exists t_expense");
        onCreate(db);
    }
    public String addRecord(Long billNo, String custName, int orange, int kokam, int lemon, int sarbat, int pachak, int walaTotal, int lSodaTotal, int ssrbtTotal, int lorange, int llemon,int jsoda, int sSoda,int water, int lassi ,int vanilla , int pista , int stwbry, int mango, int btrsch,int kulfi,int cbar,int fpack,int other,int other1,int finalTotal, String addedDate, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("billNo",billNo);
        cv.put("custName",custName);
        cv.put("orange",orange);
        cv.put("kokam",kokam);
        cv.put("lemon",lemon);
        cv.put("sarbat",sarbat);
        cv.put("pachak",pachak);
        cv.put("wala",walaTotal);
        cv.put("lsoda",lSodaTotal);
        cv.put("Ssrbt",ssrbtTotal);
        cv.put("lorange",lorange);
        cv.put("llemon",llemon);
        cv.put("jsoda",jsoda);
        cv.put("sSoda",sSoda);
        cv.put("water",water);
        cv.put("lassi",lassi);
        cv.put("vanilla",vanilla);
        cv.put("pista",pista);
        cv.put("stwbry",stwbry);
        cv.put("mango",mango);
        cv.put("btrsch",btrsch);
        cv.put("kulfi",kulfi);
        cv.put("cbar",cbar);
        cv.put("fpack",fpack);
        cv.put("other",other);
        cv.put("other1",other1);
        cv.put("total",finalTotal);
        cv.put("addedondatetime", String.valueOf(addedDate));
        cv.put("date", String.valueOf(date));
        System.out.println(cv);
        long res = db.insert("t_bill",null,cv);
        if(res==-1){
            return "Failed";
        }else{
            return "Success: "+cv;
        }
    }
    public Cursor getData(){

        Cursor getAlldata = db.rawQuery("select * from t_bill",null);
        return getAlldata;
    }

    public Cursor getOne(int billNo){
        Cursor cursor = db.rawQuery("select * from t_bill where billNo= '"+billNo+"'",null);
        return cursor;
    }

    public Cursor findRange(CharSequence fromDate, CharSequence toDate){
        String str = "select * from t_bill where date between \'"+fromDate+"\' and \'"+toDate+"\'";
        System.out.println(str);
        Cursor cursor = db.rawQuery(str,null);
        return cursor;
    }

    public Cursor getOneByName(String name) {
        String str ="select * from t_bill where custName like '%"+name+"%'";
        Cursor c = db.rawQuery(str,null);
        System.out.println(str);
        return c;
    }

    public void updateOne(String cname, int parseInt, int parseInt1, int parseInt2, int parseInt3, int parseInt4, int parseInt5, int parseInt6, int parseInt7, int parseInt8, int parseInt9, int parseInt10, int parseInt11, int parseInt12, int parseInt13,int parseInt14,int parseInt15,int parseInt16,int parseInt17,int parseInt18,int parseInt19, int parseInt20,int parseInt21,int parseInt22,int parseInt23,int total,int billNo) {
        ContentValues c = new ContentValues();
        c.put("custName",cname);
        c.put("orange",parseInt);
        c.put("kokam",parseInt1);
        c.put("lemon",parseInt2);
        c.put("sarbat",parseInt3);
        c.put("pachak",parseInt4);
        c.put("wala",parseInt5);
        c.put("lsoda",parseInt6);
        c.put("Ssrbt",parseInt7);
        c.put("lorange",parseInt8);
        c.put("llemon",parseInt9);
        c.put("jsoda",parseInt10);
        c.put("sSoda",parseInt11);
        c.put("water",parseInt12);
        c.put("lassi",parseInt13);

        c.put("vanilla",parseInt14);
        c.put("pista",parseInt15);
        c.put("stwbry",parseInt16);
        c.put("mango",parseInt17);
        c.put("btrsch",parseInt18);
        c.put("kulfi",parseInt19);
        c.put("cbar",parseInt20);
        c.put("fpack",parseInt21);
        c.put("other",parseInt22);
        c.put("other1",parseInt23);
        c.put("total",total);
        db.update("t_bill",c,"billNo = ?",new String[]{String.valueOf(billNo)});
        System.out.println("row updated");
    }

    public int saveOne(int todayTotal, int expense, int profitToday, String ss,String expMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("selling", String.valueOf(todayTotal));
        cv.put("expense", String.valueOf(expense));
        cv.put("profit", String.valueOf(profitToday));
        cv.put("date", String.valueOf(ss));
        cv.put("expenses", String.valueOf(expMap));
        System.out.println(cv);
        long res = db.insert("t_expense",null,cv);
        if(res==-1){
            return -1;
        }else{
            return 1;
        }
    }

    public int checkEntryExists(String ss) {
        int id = 0;
        String str = "select * from t_expense where date like '%"+ss+"%'";
        Cursor c = db.rawQuery(str,null);
        System.out.println(str);
        if(c.getCount() > 0){
            while (c.moveToNext()){
                id = c.getInt(0);
            }
            return id;
        }else{
            return 0;
        }
    }

    public void updateExpOne(int id,int todayTotal, int expense, int profitToday, String ss,String expMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("selling", String.valueOf(todayTotal));
        cv.put("expense", String.valueOf(expense));
        cv.put("profit", String.valueOf(profitToday));
        cv.put("date", String.valueOf(ss));
        cv.put("expenses", String.valueOf(expMap));

        System.out.println(cv);
        db.update("t_expense",cv,"id = ?",new String[]{String.valueOf(id)});
        System.out.println("row has been updated");
    }

    public int getProfitVal(String ss) {
        int profit=0;
        String str = "select * from t_expense where date like '%"+ss+"%' order by id desc";
        Cursor c = db.rawQuery(str,null);
        System.out.println(str);
        if(c.getCount() > 0){
            while (c.moveToNext()){
                profit = c.getInt(3);
            }
            return profit;
        }else{
            return 0;
        }
    }

    public Cursor getAllExp() {
        String str = "select * from t_expense order by date desc";
        Cursor c = db.rawQuery(str,null);
        return c;
    }

    public String deleteByBillNo(int billNo) {
        long res = db.delete("t_bill"," billNo = ?",new String[]{String.valueOf(billNo)});
        if(res==-1){
            return "BillNo "+billNo+" Deletion Failed";
        }else{
            return "BillNo "+ billNo +" deleted successfully";
        }
    }

    public Cursor getAvailBalTillDate(String prevDateStr) {
        String str = "select * from t_expense where date <= \'"+prevDateStr+"\'";
        System.out.println(str);
        Cursor c = db.rawQuery(str,null);
        return c;
    }

    public String deleteExpByBillNo(int billno) {
        long res = db.delete("t_expense"," id = ?",new String[]{String.valueOf(billno)});
        if(res==-1){
            return "Expense Deletion Failed: "+billno;
        }else{
            return "Expense deleted successfully: "+billno;
        }
    }

    public Cursor getOneExp(int id) {
        Cursor cursor = db.rawQuery("select * from t_expense where id = '"+id+"'",null);
        return cursor;
    }

    public void addFromCSV(int id, int selling, int expense, int profit, String date, String expenses) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", String.valueOf(id));
        cv.put("selling", String.valueOf(selling));
        cv.put("expense", String.valueOf(expense));
        cv.put("profit", String.valueOf(profit));
        cv.put("date", String.valueOf(date));
        cv.put("expenses", String.valueOf(expenses));
        System.out.println(cv);
        db.insert("t_expense",null,cv);
    }

    public void addFromCSV1(int billNo, String custName, int orange, int kokam, int lemon, int sarbat, int pachak, int wala, int lsoda, int ssrbt, int lorange, int llemon, int jsoda, int sSoda, int water, int lassi, int vanilla, int pista, int stwbry, int mango, int btrsch, int kulfi, int cbar, int fpack, int other, int other1, int total, String addedDateTime, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from t_bill");
        ContentValues cv = new ContentValues();
        cv.put("billNo",billNo);
        cv.put("custName",String.valueOf(custName));
        cv.put("orange",orange);
        cv.put("kokam",kokam);
        cv.put("lemon",lemon);
        cv.put("sarbat",sarbat);
        cv.put("pachak",pachak);
        cv.put("wala",wala);
        cv.put("lsoda",lsoda);
        cv.put("Ssrbt",ssrbt);
        cv.put("lorange",lorange);
        cv.put("llemon",llemon);
        cv.put("jsoda",jsoda);
        cv.put("sSoda",sSoda);
        cv.put("water",water);
        cv.put("lassi",lassi);
        cv.put("vanilla",vanilla);
        cv.put("pista",pista);
        cv.put("stwbry",stwbry);
        cv.put("mango",mango);
        cv.put("btrsch",btrsch);
        cv.put("kulfi",kulfi);
        cv.put("cbar",cbar);
        cv.put("fpack",fpack);
        cv.put("other",other);
        cv.put("other1",other1);
        cv.put("total",total);
        cv.put("addedondatetime", String.valueOf(addedDateTime));
        cv.put("date", String.valueOf(date));
        System.out.println(cv);
        db.insert("t_bill",null,cv);
    }

    public int getLatestBillNo() {
        int latestBillNo = 0;
        Cursor res = db.rawQuery("select MAX(billNo) from t_bill",null);
        if(res.getCount()>0){
            while(res.moveToNext()){
                latestBillNo = Integer.parseInt(String.valueOf(res.getInt(0)));
            }
        }else{
            latestBillNo = 0;
        }
        return latestBillNo;
    }

    public void clearBill() {
        db.execSQL("delete from t_bill");
    }

    public Cursor findRangeForExp(String fromStr, String toStr) {
        String str = "select * from t_expense where date between \'"+fromStr+"\' and \'"+toStr+"\'";
        Cursor res = db.rawQuery(str,null);
        return res;
    }
}

