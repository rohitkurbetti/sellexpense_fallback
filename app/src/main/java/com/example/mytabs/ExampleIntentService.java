package com.example.mytabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExampleIntentService extends IntentService {
    public static ThirdFragment t1;
    private static final String TAG = "ExampleIntentService";
    private static final String CHANNEL_ID = "exampleservicechannel";

    private PowerManager.WakeLock wakeLock;
    Dialog nDialog;


    public ExampleIntentService() {
        super("ExampleIntentService");
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");


        PowerManager poweerManger = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = poweerManger.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"ExampleApp:WakeLock");
        wakeLock.acquire();
        Log.d(TAG,"WakeLock acquired");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            Notification notification = new Notification.Builder(this,CHANNEL_ID)
                    .setContentTitle("Example IntentService")
                    .setContentText("Running...")
                    .setSmallIcon(R.drawable.ic_action_name)
                    .build();
            startForeground(1,notification);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG,"onHandleIntent");
        String input = intent.getStringExtra("inputExtra");
        if(input.equals("import")){
            DbManager db = new DbManager(getApplicationContext());
            String pathAndFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/SQLiteBackup/Expense_Backup/" + "SQLite_ExpBackup.csv";
            String pathAndFileName1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/SQLiteBackup/Billing_Backup/" + "SQLite_BillBackup.csv";
            System.out.println(pathAndFileName);
            File csvFile = new File(pathAndFileName);
            File csvFile1 = new File(pathAndFileName1);

            Uri uri = FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID+".provider",csvFile);

//        Intent i = new Intent(Intent.ACTION_SENDTO);
//        i.setData(uri.parse("mailto:"));
//        i.putExtra(Intent.EXTRA_EMAIL, new String[]{USER_EMAIL});
//        i.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUB);
//        i.putExtra(Intent.EXTRA_TEXT, EMAIL_BODY);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.putExtra(Intent.EXTRA_STREAM, uri);
//        //i.type = "image/png"
//        this.startActivity(Intent.createChooser(i, null));

            File filePath = new File(pathAndFileName1);
//        Intent intentShareFile = new Intent(Intent.ACTION_VIEW);
//        File fileWithinMyDir = new File(pathAndFileName);
            Uri uri1 = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID+".provider",filePath);

//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        emailIntent.setType("application/csv");
//        emailIntent.setDataAndType(uri1,context.getContentResolver().getType(uri1));
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, "asds");
//        emailIntent.putExtra(Intent.EXTRA_CC, "csa");
//        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "UllageReport (Pdf)");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "UllageReport");
//        emailIntent.putExtra(Intent.EXTRA_STREAM, uri1);
//        try{
//            if (emailIntent.resolveActivity(getPackageManager()) != null) {
//                startActivity(Intent.createChooser(emailIntent,"Send via"));
//            }
//        }
//        catch(android.content.ActivityNotFoundException e){
//            Toast.makeText(this, "No email client available", Toast.LENGTH_SHORT).show();
//        }


            if(csvFile.exists()){
                //backup exists
                System.out.println("abs path:"+csvFile.getAbsolutePath());
                try {
                    CSVReader csvReader = new CSVReader(new FileReader(csvFile.getAbsolutePath()));
                    String[] nexLine;
                    while((nexLine = csvReader.readNext()) != null){
                        int id = Integer.parseInt(nexLine[0]);
                        int selling = Integer.parseInt(nexLine[1]);
                        int expense = Integer.parseInt(nexLine[2]);
                        int profit = Integer.parseInt(nexLine[3]);
                        String date = nexLine[4];
                        String expenses = nexLine[5];
                        System.out.println("see me>>>"+expenses);
                        db.addFromCSV(id,selling,expense,profit,date,expenses);
//                    db.updateExpOne(id,selling,expense,profit,date,expenses);
                    }
//                Toast.makeText(context, "Backup Restored Successfully", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.getMessage(),  Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }else {
                //backup doesnt exist
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "No Backup Found", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if(csvFile1.exists()){
                //backup exists
                System.out.println("abs path1:"+csvFile1.getAbsolutePath());
                try {
                    CSVReader csvReader1 = new CSVReader(new FileReader(csvFile1.getAbsolutePath()));
                    String[] nexLine1;

                    if(csvFile1.length()>0){
                        db.clearBill();
                    }
                    while((nexLine1 = csvReader1.readNext()) != null){
//                    int id = Integer.parseInt(nexLine1[0]);
                        int billNo = Integer.parseInt(nexLine1[0]);
                        String custName = String.valueOf(nexLine1[1]);
                        int orange = Integer.parseInt(nexLine1[2]);
                        int kokam = Integer.parseInt(nexLine1[3]);
                        int lemon = Integer.parseInt(nexLine1[4]);
                        int sarbat = Integer.parseInt(nexLine1[5]);
                        int pachak = Integer.parseInt(nexLine1[6]);
                        int wala = Integer.parseInt(nexLine1[7]);
                        int lsoda = Integer.parseInt(nexLine1[8]);
                        int ssrbt = Integer.parseInt(nexLine1[9]);
                        int lorange = Integer.parseInt(nexLine1[10]);
                        int llemon = Integer.parseInt(nexLine1[11]);
                        int jsoda = Integer.parseInt(nexLine1[12]);
                        int sSoda = Integer.parseInt(nexLine1[13]);
                        int water = Integer.parseInt(nexLine1[14]);
                        int lassi = Integer.parseInt(nexLine1[15]);
                        int vanilla = Integer.parseInt(nexLine1[16]);
                        int pista = Integer.parseInt(nexLine1[17]);
                        int stwbry = Integer.parseInt(nexLine1[18]);
                        int mango = Integer.parseInt(nexLine1[19]);
                        int btrsch = Integer.parseInt(nexLine1[20]);
                        int kulfi = Integer.parseInt(nexLine1[21]);
                        int cbar = Integer.parseInt(nexLine1[22]);
                        int fpack = Integer.parseInt(nexLine1[23]);
                        int cones = Integer.parseInt(nexLine1[24]);
                        int coneb = Integer.parseInt(nexLine1[25]);
                        int other = Integer.parseInt(nexLine1[26]);
                        int other1 = Integer.parseInt(nexLine1[27]);
                        int total = Integer.parseInt(nexLine1[28]);
                        String addedDateTime = String.valueOf(nexLine1[29]);
                        String date = String.valueOf(nexLine1[30]);
                        String otherItems = String.valueOf(nexLine1[31]);
                        String otherItems1 = String.valueOf(nexLine1[32]);
                        db.addFromCSV1(billNo,custName,orange,kokam,lemon,sarbat,pachak,wala,lsoda,ssrbt,lorange,llemon,jsoda,sSoda,water,lassi,vanilla,pista,stwbry,mango,btrsch,kulfi,cbar,fpack,cones,coneb,other,other1,total,addedDateTime,date,otherItems,otherItems1);
//                    db.updateExpOne(id,selling,expense,profit,date,expenses);
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Backup Restored Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (Exception e){
                    System.out.println(e);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.getMessage()+"cought herer",  Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }else {
                //backup doesnt exist
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "No Backup Found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            Log.d(TAG,"Done Importing log f");

        } else if(input.equals("export")){
            //export code
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/SQLiteBackup/Expense_Backup").toString();
            String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/SQLiteBackup/Billing_Backup").toString();
            File folder = new File(path);
            File folder1 = new File(path1);
            boolean isFolderCreated = false;
            boolean isFolderCreated1 = false;
            if(!folder.exists()){
                //do mkdir
                isFolderCreated = folder.mkdirs(); //create folder if not exists
            }
            if(!folder1.exists()){
                //do mkdir
                isFolderCreated1 = folder1.mkdirs(); //create folder if not exists
            }
            Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HHmmss");
//        String dateFmt = sdf.format(c.getTime());
            String csvFileName = "SQLite_ExpBackup.csv";
            String csvFileName1 = "SQLite_BillBackup.csv";
            //file path and name
            String filepathAndName = folder.toString()+"/"+csvFileName;
            String filepathAndName1 = folder1.toString()+"/"+csvFileName1;
            System.out.println(filepathAndName);
            System.out.println(filepathAndName1);
            DbManager db = new DbManager(ExampleIntentService.this);
            Cursor res = db.getAllExp();
            Cursor res1 = db.getData();
            try {
                FileWriter fw = new FileWriter(filepathAndName);
                while(res.moveToNext()){
                    fw.append(String.valueOf(res.getInt(0)));
                    fw.append(",");
                    fw.append(String.valueOf(res.getInt(1)));
                    fw.append(",");
                    fw.append(String.valueOf(res.getInt(2)));
                    fw.append(",");
                    fw.append(String.valueOf(res.getInt(3)));
                    fw.append(",");
                    fw.append(String.valueOf(res.getString(4)));
                    fw.append(",");
                    fw.append(String.valueOf(res.getString(5)));
                    fw.append("\n");
                }
                fw.flush();
                fw.close();
                FileWriter fw1 = new FileWriter(filepathAndName1);
                while(res1.moveToNext()){
                    fw1.append(String.valueOf(res1.getInt(1)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getString(2).isEmpty()?"-":res1.getString(2)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(3)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(4)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(5)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(6)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(7)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(8)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(9)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(10)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(11)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(12)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(13)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(14)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(15)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(16)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(17)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(18)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(19)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(20)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(21)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(22)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(23)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(24)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(25)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(26)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(27)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(28)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getInt(29)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getString(30)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getString(31)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getString(32)));
                    fw1.append(",");
                    fw1.append(String.valueOf(res1.getString(33)));
                    fw1.append("\n");
                }
                fw1.flush();
                fw1.close();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "BackedUp:"+filepathAndName+"\t"+filepathAndName1, Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            Log.d(TAG,"Done Exporting log f");

        } else if (input.equals("fillTable1")){
//            t = v.findViewById(R.id.tbEstimation);
//            srno1 = v.findViewById(R.id.serialNo);
//            tvTotal =v.findViewById(R.id.total1);
//            currDate1 = v.findViewById(R.id.currDate);
            boolean isDarkThemeOn = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
            DbManager db = new DbManager(t1.getContext());
            Calendar calendar = Calendar.getInstance();
//            nDialog = new ProgressDialog(t1.getContext());
//            ((ProgressDialog) nDialog).setMessage("Loading..");
//            nDialog.setTitle("Get Data");
//            ((ProgressDialog) nDialog).setIndeterminate(false);
//            nDialog.setCancelable(true);
//            nDialog.show();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    t1.progressDialog.setVisibility(View.VISIBLE);
                    t1.t.setVisibility(View.GONE);
                }
            });
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            int todayTotal = 0;
            int totalAll = 0;

            Date d = calendar.getTime();
            Cursor res = db.findRange(sdf.format(d.getTime()),sdf.format(d.getTime()));
            int srno = 0;
            String currDate = "";
            if(res.getCount()>0){
//            TableRow row1 = new TableRow(getContext());
//            row1.setPadding(55,0,25,0);
//            TextView h1 = new TextView(getContext());
//            h1.setTextColor(Color.BLACK);
//            h1.setText(String.valueOf("No"));
//            TextView h2 = new TextView(getContext());
//            h2.setText(String.valueOf("Total"));
//            h2.setTextColor(Color.BLACK);
//            TextView h3 = new TextView(getContext());
//            h3.setText(String.valueOf("Date"));
//            h3.setTextColor(Color.BLACK);
//            row1.addView(h1);
//            row1.addView(h2);
//            row1.addView(h3);
//            t.addView(row1);
                if(isDarkThemeOn){
//                srno1 = v.findViewById(R.id.serialNo);
//                tvTotal = v.findViewById(R.id.total1);
//                currDate1 = v.findViewById(R.id.currDate);
                    t1.srno1.setTextColor(Color.WHITE);
                    t1.tvTotal.setTextColor(Color.WHITE);
                    t1.currDate1.setTextColor(Color.WHITE);
                }else{
                    t1.srno1.setTextColor(Color.BLACK);
                    t1.tvTotal.setTextColor(Color.BLACK);
                    t1.currDate1.setTextColor(Color.BLACK);
                }
                while(res.moveToNext()){
                    TableRow row = new TableRow(t1.getActivity());
                    row.setPadding(50,10,25,10);
                    srno = res.getInt(1);
                    todayTotal = res.getInt(29);
                    currDate = res.getString(30);

                    t1.srno1 = new TextView(t1.getActivity());
                    t1.srno1.setText(String.valueOf(srno));
                    t1.srno1.setPadding(80,0,25,0);

                    t1.tvTotal = new TextView(t1.getActivity());
                    t1.tvTotal.setText(String.valueOf(todayTotal));
                    Bundle b = new Bundle();

                    if(!String.valueOf(res.getInt(3)).equals("0")){
                        b.putInt("orange", Integer.parseInt(String.valueOf(res.getInt(3))));
                    }
                    if(!String.valueOf(res.getInt(4)).equals("0")){
                        b.putInt("kokam", Integer.parseInt(String.valueOf(res.getInt(4))));
                    }
                    if(!String.valueOf(res.getInt(5)).equals("0")){
                        b.putInt("lemon", Integer.parseInt(String.valueOf(res.getInt(5))));
                    }
                    if(!String.valueOf(res.getInt(6)).equals("0")) {
                        b.putInt("sarbat", Integer.parseInt(String.valueOf(res.getInt(6))));
                    }
                    if(!String.valueOf(res.getInt(7)).equals("0")) {
                        b.putInt("pachak", Integer.parseInt(String.valueOf(res.getInt(7))));
                    }
                    if(!String.valueOf(res.getInt(8)).equals("0")) {
                        b.putInt("wala", Integer.parseInt(String.valueOf(res.getInt(8))));
                    }
                    if(!String.valueOf(res.getInt(9)).equals("0")) {
                        b.putInt("lsoda", Integer.parseInt(String.valueOf(res.getInt(9))));
                    }
                    if(!String.valueOf(res.getInt(10)).equals("0")) {
                        b.putInt("ssrbt", Integer.parseInt(String.valueOf(res.getInt(10))));
                    }
                    if(!String.valueOf(res.getInt(11)).equals("0")) {
                        b.putInt("lorange", Integer.parseInt(String.valueOf(res.getInt(11))));
                    }
                    if(!String.valueOf(res.getInt(12)).equals("0")) {
                        b.putInt("llemon", Integer.parseInt(String.valueOf(res.getInt(12))));
                    }
                    if(!String.valueOf(res.getInt(13)).equals("0")) {
                        b.putInt("jsoda", Integer.parseInt(String.valueOf(res.getInt(13))));
                    }
                    if(!String.valueOf(res.getInt(14)).equals("0")) {
                        b.putInt("sSoda", Integer.parseInt(String.valueOf(res.getInt(14))));
                    }
                    if(!String.valueOf(res.getInt(15)).equals("0")) {
                        b.putInt("water", Integer.parseInt(String.valueOf(res.getInt(15))));
                    }
                    if(!String.valueOf(res.getInt(16)).equals("0")) {
                        b.putInt("lassi", Integer.parseInt(String.valueOf(res.getInt(16))));
                    }
                    if(!String.valueOf(res.getInt(17)).equals("0")) {
                        b.putInt("vanilla", Integer.parseInt(String.valueOf(res.getInt(17))));
                    }
                    if(!String.valueOf(res.getInt(18)).equals("0")) {
                        b.putInt("pista", Integer.parseInt(String.valueOf(res.getInt(18))));
                    }
                    if(!String.valueOf(res.getInt(19)).equals("0")) {
                        b.putInt("stwbry", Integer.parseInt(String.valueOf(res.getInt(19))));
                    }
                    if(!String.valueOf(res.getInt(20)).equals("0")) {
                        b.putInt("mango", Integer.parseInt(String.valueOf(res.getInt(20))));
                    }
                    if(!String.valueOf(res.getInt(21)).equals("0")) {
                        b.putInt("btrsch", Integer.parseInt(String.valueOf(res.getInt(21))));
                    }
                    if(!String.valueOf(res.getInt(22)).equals("0")) {
                        b.putInt("kulfi", Integer.parseInt(String.valueOf(res.getInt(22))));
                    }
                    if(!String.valueOf(res.getInt(23)).equals("0")) {
                        b.putInt("cbar", Integer.parseInt(String.valueOf(res.getInt(23))));
                    }
                    if(!String.valueOf(res.getInt(24)).equals("0")) {
                        b.putInt("fpack", Integer.parseInt(String.valueOf(res.getInt(24))));
                    }
                    if(!String.valueOf(res.getInt(25)).equals("0")) {
                        b.putInt("cones", Integer.parseInt(String.valueOf(res.getInt(25))));
                    }
                    if(!String.valueOf(res.getInt(26)).equals("0")) {
                        b.putInt("coneb", Integer.parseInt(String.valueOf(res.getInt(26))));
                    }
                    if(!String.valueOf(res.getInt(27)).equals("0")) {
                        b.putInt("other", Integer.parseInt(String.valueOf(res.getInt(27))));
                    }
                    if(!String.valueOf(res.getInt(28)).equals("0")) {
                        b.putInt("other1", Integer.parseInt(String.valueOf(res.getInt(28))));
                    }
//                if(!String.valueOf(res.getInt(28)).equals("0")) {
//                    b.putString("datetime", (String.valueOf(res.getString(28))));
//                }

                    Bundle bundle = new Bundle();
                    bundle.putAll(b);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        StringBuilder str = new StringBuilder();
                        if(bundle.getInt("orange") != 0){
                            str.append("orange:"+bundle.getInt("orange"));
                        }
                        if(bundle.getInt("kokam") != 0){
                            str.append(" kokam:"+bundle.getInt("kokam"));
                        }
                        if(bundle.getInt("lemon") != 0){
                            str.append(" lemon:"+bundle.getInt("lemon"));
                        }
                        if(bundle.getInt("sarbat") != 0){
                            str.append(" sarbat:"+bundle.getInt("sarbat"));
                        }
                        if(bundle.getInt("pachak") != 0){
                            str.append(" pachak:"+bundle.getInt("pachak"));
                        }
                        if(bundle.getInt("wala") != 0){
                            str.append(" wala:"+bundle.getInt("wala"));
                        }
                        if(bundle.getInt("lsoda") != 0){
                            str.append(" lsoda:"+bundle.getInt("lsoda"));
                        }
                        if(bundle.getInt("ssrbt") != 0){
                            str.append(" ssrbt:"+bundle.getInt("ssrbt"));
                        }
                        if(bundle.getInt("lorange") != 0){
                            str.append(" lorange:"+bundle.getInt("lorange"));
                        }
                        if(bundle.getInt("llemon") != 0){
                            str.append(" llemon:"+bundle.getInt("llemon"));
                        }
                        if(bundle.getInt("jsoda") != 0){
                            str.append(" jsoda:"+bundle.getInt("jsoda"));
                        }
                        if(bundle.getInt("sSoda") != 0){
                            str.append(" sSoda:"+bundle.getInt("sSoda"));
                        }
                        if(bundle.getInt("water") != 0){
                            str.append(" water:"+bundle.getInt("water"));
                        }
                        if(bundle.getInt("lassi") != 0){
                            str.append(" lassi:"+bundle.getInt("lassi"));
                        }
                        if(bundle.getInt("vanilla") != 0){
                            str.append(" vanilla:"+bundle.getInt("vanilla"));
                        }
                        if(bundle.getInt("pista") != 0){
                            str.append(" pista:"+bundle.getInt("pista"));
                        }
                        if(bundle.getInt("stwbry") != 0){
                            str.append(" stwbry:"+bundle.getInt("stwbry"));
                        }
                        if(bundle.getInt("mango") != 0){
                            str.append(" mango:"+bundle.getInt("mango"));
                        }
                        if(bundle.getInt("btrsch") != 0){
                            str.append(" btrsch:"+bundle.getInt("btrsch"));
                        }
                        if(bundle.getInt("kulfi") != 0){
                            str.append(" kulfi:"+bundle.getInt("kulfi"));
                        }
                        if(bundle.getInt("cbar") != 0){
                            str.append(" cbar:"+bundle.getInt("cbar"));
                        }
                        if(bundle.getInt("fpack") != 0){
                            str.append(" fpack:"+bundle.getInt("fpack"));
                        }
                        if(bundle.getInt("cones") != 0){
                            str.append(" cones:"+bundle.getInt("cones"));
                        }
                        if(bundle.getInt("coneb") != 0){
                            str.append(" coneb:"+bundle.getInt("coneb"));
                        }
                        if(bundle.getInt("other") != 0){
                            str.append(" other:"+bundle.getInt("other"));
                        }
                        if(bundle.getInt("other1") != 0){
                            str.append(" other1:"+bundle.getInt("other1"));
                        }
//                    if(bundle.getString("datetime") != ""){
//                        str.append(" datetime:"+bundle.getString("datetime"));
//                    }
//                    tvTotal.setTooltipText(String.valueOf("Orange:"+bundle.get("orange")+", Kokam:"+bundle.get("kokam")+", Lemon:"+bundle.get("lemon")+", Sarbat:"+bundle.get("sarbat")+", Pachak:"+bundle.getInt("pachak")+", Wala"+bundle.getInt("wala")+", Lsoda:"+bundle.getInt("lsoda")+", ssrbt:"+bundle.getInt("ssrbt")+", Lorange:"+bundle.getInt("lorange")+", Llemon:"+bundle.getInt("llemon")+", jSoda:"+bundle.getInt("jsoda")+", sSoda:"+bundle.getInt("sSoda")+", Water:"+bundle.getInt("water")+", Lassi:"+bundle.getInt("lassi")+", Vanilla:"+bundle.getInt("vanilla")+", Pista:"+bundle.getInt("pista")+", Stwbry:"+bundle.getInt("stwbry")+", Mango:"+bundle.getInt("mango")+", Btrsch:"+bundle.getInt("btrsch")+", kulfi:"+bundle.getInt("kulfi")+", Cbar:"+bundle.getInt("cbar")+", Fpack:"+bundle.getInt("fpack")+", Other:"+bundle.getInt("other")+", Other1:"+bundle.getInt("other1")+", Date:"+bundle.getString("datetime")));
                        t1.tvTotal.setTooltipText(str);
                    }
                    t1.tvTotal.setPadding(130,0,25,0);

                    t1.currDate1 = new TextView(t1.getActivity());
                    t1.currDate1.setText(String.valueOf(currDate));
                    t1.currDate1.setPadding(60,0,25,0);
                    totalAll += Integer.parseInt(String.valueOf(res.getInt(29)));
                    row.addView(t1.srno1);
                    row.addView(t1.tvTotal);
                    row.addView(t1.currDate1);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            t1.t.addView(row);
                            t1.t.setVisibility(View.GONE);
                        }
                    });
                }
                TableRow row = new TableRow(t1.getActivity());
                row.setHorizontalGravity(Gravity.START);
                TextView tvAll = new TextView(t1.getActivity());
                tvAll.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvAll.setPadding(50,0,25,0);
                tvAll.setText(String.valueOf("Total: "+totalAll));
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        row.addView(tvAll);
                            t1.t.addView(row);
                    }
                });
            }else{
                TableRow rowEmpty = new TableRow(t1.getActivity());
                rowEmpty.setPadding(50,10,25,5);
                rowEmpty.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

                TextView tvEmpty = new TextView(t1.getActivity());
                tvEmpty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvEmpty.setGravity(Gravity.CENTER_HORIZONTAL);

                tvEmpty.setText(String.valueOf("No data found"));
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        rowEmpty.addView(tvEmpty);
                            t1.t.addView(rowEmpty);
                    }
                });
            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    t1.progressDialog.setVisibility(View.GONE);
                    t1.t.setVisibility(View.VISIBLE);
                }
            });
            Log.d(TAG,"End fill table");

        } else if(input.equals("BillReportPdf")){
            //call ThirdFragment's doPrintReport();
            try {
                t1.doPrintReport();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        wakeLock.release();
        System.gc();
        Log.d(TAG,"wakelock released");
    }
}
