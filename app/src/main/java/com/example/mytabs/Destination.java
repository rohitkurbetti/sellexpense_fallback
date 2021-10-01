package com.example.mytabs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Destination extends AppCompatActivity {
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    MainActivity m = new MainActivity();
    private static final int JOB_ID= 101;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    int expense= 0;
    int todayTotal = 0;
    int bkpPrevBal = 0;

    String ss;
    TextView tt;
    TextView tt1;
    int availBal=0;
    TextView tvPrevBal;
    TextView tvSelling;
    String prevDateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        prefs = getSharedPreferences("bill",MODE_PRIVATE);
        editor = prefs.edit();
        tt = findViewById(R.id.tt);
        tt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editor.putInt("prevBalance",0).apply();
                Toast.makeText(Destination.this, ""+String.valueOf(prefs.getInt("prevBalance",0)), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        tvPrevBal = findViewById(R.id.tvPrevBal);
        tvSelling = findViewById(R.id.tvSelling);
        EditText etExp = findViewById(R.id.etExp);
        TextView tvProfit = findViewById(R.id.tvProfit);
        Button btnCommit = findViewById(R.id.btnCommit);
        tvPrevBal.setText(String.valueOf(prefs.getInt("prevBalance",0)));

        ComponentName componentName = new ComponentName(this,MJobScheduler.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,componentName);
        builder.setPeriodic(2000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);

        jobInfo = builder.build();
        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        tvProfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        a();
        DbManager db = new DbManager(getApplicationContext());
//        Calendar calendar = Calendar.getInstance();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//
//        Date d = calendar.getTime();
//        Cursor res = db.findRange(sdf.format(d.getTime()),sdf.format(d.getTime()));
//        while(res.moveToNext()){
//            todayTotal += res.getInt(27);
//        }
//        System.out.println(String.valueOf("todaysTotal:"+todayTotal));
//        tvSelling.setText(String.valueOf(todayTotal));


        tvSelling.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etExp.setText("0");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etExp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!etExp.getText().toString().isEmpty()){
                    expense = Integer.parseInt(etExp.getText().toString());
                    tvProfit.setText(String.valueOf(todayTotal-(expense != 0 && !etExp.getText().toString().isEmpty() ? expense : 0)));
                }else{
                    tvProfit.setText(String.valueOf(todayTotal+(expense != 0 && !etExp.getText().toString().isEmpty() ? expense : 0)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnCommit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Cursor res = db.getAllExp();
                final View customView = getLayoutInflater().inflate(R.layout.expense, null);
                TableLayout tableLayout = customView.findViewById(R.id.table_layout);
                boolean isDarkThemeOn = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

                TextView tb0 = customView.findViewById(R.id.billno);
                TextView cname = customView.findViewById(R.id.custname);
                TextView tb1 = customView.findViewById(R.id.orange);
                TextView tb2 = customView.findViewById(R.id.kokam);
                TextView tb3 = customView.findViewById(R.id.lemon);
                TextView tb4 = customView.findViewById(R.id.sarbat);
                TextView tb5 = customView.findViewById(R.id.ssarbat);
                AlertDialog.Builder builder = new AlertDialog.Builder(Destination.this);
                builder.setTitle("Expense Data");
                builder.setView(customView);
                if(res.getCount()>0) {
                    while (res.moveToNext()) {
//                    sb.append(res.getInt(1)+"  "+res.getInt(18)+"  "+res.getString(19)+"\n");
//                    total += res.getInt(27);
                        TableRow tableRow = new TableRow(customView.getContext());
                        tableRow.setPadding(10, 10, 10, 10);

                        TextView tv1 = new TextView(customView.getContext());
                        tv1.setText(String.valueOf(res.getInt(0)));
                        tv1.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView cname1 = new TextView(customView.getContext());
                        cname1.setText(String.valueOf(res.getInt(1)));
                        cname1.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv2 = new TextView(customView.getContext());
                        tv2.setText(String.valueOf(res.getInt(2)));
                        tv2.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv3 = new TextView(customView.getContext());
                        tv3.setText(String.valueOf(res.getInt(3)));
                        tv3.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv4 = new TextView(customView.getContext());
                        tv4.setText(String.valueOf(res.getInt(4)));
                        tv4.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv5 = new TextView(customView.getContext());
                        tv5.setText(String.valueOf(res.getString(5)));
                        tv5.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv6 = new TextView(customView.getContext());
                        tv6.setText(String.valueOf(res.getString(6)));
                        tv6.setGravity(Gravity.CENTER_HORIZONTAL);


                        if (isDarkThemeOn) {
                            cname.setTextColor(Color.WHITE);
                            tb0.setTextColor(Color.WHITE);
                            tb1.setTextColor(Color.WHITE);
                            tb2.setTextColor(Color.WHITE);
                            tb3.setTextColor(Color.WHITE);
                            tb4.setTextColor(Color.WHITE);
                            tb5.setTextColor(Color.WHITE);

                        } else {
                            cname.setTextColor(Color.BLACK);
                            tb0.setTextColor(Color.BLACK);
                            tb1.setTextColor(Color.BLACK);
                            tb2.setTextColor(Color.BLACK);
                            tb3.setTextColor(Color.BLACK);
                            tb4.setTextColor(Color.BLACK);
                            tb5.setTextColor(Color.BLACK);
                        }
                        tableRow.addView(tv1);
                        tableRow.addView(cname1);
                        tableRow.addView(tv2);
                        tableRow.addView(tv3);
                        tableRow.addView(tv4);
                        tableRow.addView(tv5);
                        tableRow.addView(tv6);
//                    }
                        tableLayout.addView(tableRow);
                    }
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(Destination.this, "No data found", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvProfit.getText().toString().contentEquals("0")){
                    etExp.setText(String.valueOf("0"));
                }
                if(tt.getText().toString().contentEquals("Select Date") ){
                    callDatePicker();

                }else{
                    int id = db.checkEntryExists(ss);
                    int getProfitVal = db.getProfitVal(ss);
                    Toast.makeText(Destination.this, "record id :"+id, Toast.LENGTH_SHORT).show();
                    System.out.println("get progit:"+getProfitVal);
                    bkpPrevBal = prefs.getInt("prevBalance",0);
                    int currTodayBal = 0;
                    if(id == 0){

                        currTodayBal = bkpPrevBal + Integer.parseInt(String.valueOf(tvProfit.getText().toString().contentEquals("0")?"0":String.valueOf(tvProfit.getText().toString())));

                        editor.putInt("prevBalance",currTodayBal).apply();
                    }else{
                        Toast.makeText(Destination.this, "bkpPrevBal:"+bkpPrevBal, Toast.LENGTH_SHORT).show();
                        bkpPrevBal = prefs.getInt("prevBalance",0) - getProfitVal;
//                        currTodayBal = currTodayBal - prefs.getInt("prevBalance",0);

                        editor.putInt("prevBalance",bkpPrevBal).apply();
                        bkpPrevBal = prefs.getInt("prevBalance",0);
                        currTodayBal = bkpPrevBal + Integer.parseInt(String.valueOf(tvProfit.getText().toString().contentEquals("0")?"0":String.valueOf(tvProfit.getText().toString())));
                        editor.putInt("prevBalance",currTodayBal).apply();
                        Toast.makeText(Destination.this, "Curr bal update:"+currTodayBal, Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("PrevBal:"+bkpPrevBal+" CurrBlanace:"+currTodayBal+" Selling:"+tvSelling.getText()+" exp:"+expense+" profit"+tvProfit.getText()+" Date:"+ss);
                    int profitToday = Integer.parseInt(tvProfit.getText().toString());
                    Toast.makeText(Destination.this, "Id returned:"+id, Toast.LENGTH_SHORT).show();
                    if(id != 0){
                        System.out.println("currentTodalBal:>>"+currTodayBal);
                        editor.putInt("prevBalance",currTodayBal).apply();
                        db.updateExpOne(id,bkpPrevBal,currTodayBal,todayTotal,expense,profitToday,ss);
                    }else{
                        String res = db.saveOne(bkpPrevBal,currTodayBal,todayTotal,expense,profitToday,ss);
                        Toast.makeText(Destination.this, "Row added "+res, Toast.LENGTH_SHORT).show();
                    }

                }

//                AlertDialog.Builder builder = new AlertDialog.Builder(Destination.this);
//                builder.setTitle("Expense data");
//                Cursor res = db.getAllExp();
//                while (res.moveToNext()) {
//                    tt1.setText(res.getInt(0)+" "+res.getInt(1)+" "+res.getInt(2)+" "+res.getInt(3)+" "+res.getInt(4)+" "+res.getInt(5)+" "+res.getInt(6)+"\n");
//                }


            }
        });


        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobScheduler.cancel(JOB_ID);
                Toast.makeText(getApplicationContext(), "Job Cancelled...", Toast.LENGTH_SHORT).show();
            }
        });

        tt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                getTodaysSeeling();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void getAvailBalTillDate(String prevDateStr) {
        DbManager db = new DbManager(getApplicationContext());
        Cursor cRes = db.getAvailBalTillDate(prevDateStr);
        if(cRes.getCount()>0){
            while(cRes.moveToNext()){
                availBal += Integer.parseInt(String.valueOf(cRes.getInt(5)));
            }
            tvPrevBal.setText(String.valueOf(availBal));
            availBal = 0;
        }else{
            tvPrevBal.setText(String.valueOf(0));
            availBal = 0;
        }
    }

    private void getTodaysSeeling() {
        DbManager db = new DbManager(getApplicationContext());
        Cursor res = db.findRange(tt.getText().toString(),tt.getText().toString());
        if(res.getCount()>0){
            while(res.moveToNext()) {
                todayTotal += Integer.parseInt(String.valueOf(res.getInt(27)));
            }
        }else{
            todayTotal = 0;
        }
        tvSelling.setText(String.valueOf(todayTotal));
    }

    private void callDatePicker() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(Destination.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String day1, month1;
                if (view.getDayOfMonth() < 10) {
                    day1 = "0" + view.getDayOfMonth();
                } else {
                    day1 = "" + view.getDayOfMonth();
                }
                if ((view.getMonth() + 1) < 10) {
                    month1 = "0" + (view.getMonth() + 1);
                } else {
                    month1 = "" + (view.getMonth() + 1);
                }
                ss = view.getYear()+"/"+ month1 + "/" + day1;
                System.out.println(ss);
                try {
                    view.setMaxDate(c.getTimeInMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tt.setText(String.valueOf(ss));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    Date d = sdf.parse(ss);
                    Calendar cc = Calendar.getInstance();
                    cc.setTime(d);
                    cc.add(Calendar.DATE,-1);
                    Date prevDate = cc.getTime();
                    prevDateStr = sdf.format(prevDate);
                    System.out.println("Previous Date:"+prevDateStr);
                    getAvailBalTillDate(prevDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, mYear, mMonth, mDay);
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void a(){
        jobScheduler.schedule(jobInfo);
        Toast.makeText(this, "Job scheduled...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

}
