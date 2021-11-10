package com.example.mytabs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytabs.ui.main.SectionsPagerAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private static final int ACTIVITY_CHOOSE_FILE = 123;
    private static final String CHANNEL_ID = "exampleservicechannel";
    private int ACTIVITY_CHOOSE_ANYFILE = 23;
    private long mBackPressed;
    private String[] storagePermissions;
    private static final int STORAGE_REQUEST_CODE_EXPORT = 1;
    private static final int STORAGE_REQUEST_CODE_IMPORT = 2;
    FirebaseFirestore db;
    ProgressDialog pd;
    int r = 189, g = 189, b = 189;
    public FirstFragment f1;
    public SecondFragment f2;
    public ThirdFragment f3;
    Long billNo_generated = 0L;
    int masterFinal = 0;
    Context context;
    String sb;
    boolean isValid;
    String validCustName;
    int IceCreamFinalTotal;
    int coldrinkFinalTotal;
    Map<String, Item> map = new HashMap<>();
    //    Map<String,Item> items1 = new HashMap<>();
//    Map<String,Item> items2 = new HashMap<>();
    int iceTotal;
    int coldTotal;
    Long maxBillFire = 0L;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    ListView listView;
    String[] str_list;

//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

    private void showSnackIfOffline(){
        final boolean online = isOnline1();
        runOnUiThread(new TimerTask() { //must run on main thread to update UI (show Snackbar), can be used only in Activity (FragmentActivity, AppCompatActivity...)
            @Override
            public void run() {
                if(!online)
                    Snackbar.make(findViewById(android.R.id.content), "Sorry, you're offline", Snackbar.LENGTH_LONG)
                        .setTextColor(Color.WHITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showSnackIfOffline();
                        }
                    })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_purple ))
                        .show();
                else{
                    getLastestBillNoFromFireStore();
                }
            }
        });
    }

    private boolean isOnline(){
        try {
            return Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8").waitFor() == 0; //  "8.8.8.8" is the server to ping
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isOnline1(){
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;
            }
            else
                connected = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return connected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_restore) {
            if (checkStoragePermission()) {
                //permission allowed
//                class MyImport extends AsyncTask<String,Void,String>{
//                    @Override
//                    protected String doInBackground(String... strings) {
//                        importCSV();
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(String s) {
//                        super.onPostExecute(s);
//                        Toast.makeText(context, "Done Importing", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                new MyImport().execute();

                Intent serIntent = new Intent(this, ExampleIntentService.class);
                serIntent.putExtra("inputExtra", "import");
                ContextCompat.startForegroundService(this, serIntent);

            } else {
                requestStoragePermissionImport();
//                int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//
//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    // We don't have permission so prompt the user
//                    ActivityCompat.requestPermissions(
//                            this,
//                            storagePermissions,
//                            STORAGE_REQUEST_CODE_IMPORT
//                    );
//                }
            }
        }
        if (id == R.id.action_backup) {
            if (checkStoragePermission()) {
                //permission allowed
//                class MyTask extends AsyncTask<String,Void,String>{
//
//                    @Override
//                    protected String doInBackground(String... strings) {
//                        exportCSV();
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(String s) {
//                        super.onPostExecute(s);
//                        Toast.makeText(context, "Done Exporting", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                new MyTask().execute();

                Intent serIntent = new Intent(this, ExampleIntentService.class);
                serIntent.putExtra("inputExtra", "export");
                ContextCompat.startForegroundService(this, serIntent);

            } else {
                requestStoragePermissionExport();
//                int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    // We don't have permission so prompt the user
//                    ActivityCompat.requestPermissions(
//                            this,
//                            storagePermissions,
//                            STORAGE_REQUEST_CODE_EXPORT
//                    );
//                }
            }
        }
        if (id == R.id.action_sendMail) {
            if (checkStoragePermission()) {
                //permission allowed
                saveToMail();
            } else {
                requestStoragePermissionImport();
//                int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    // We don't have permission so prompt the user
//                    ActivityCompat.requestPermissions(
//                            this,
//                            storagePermissions,
//                            STORAGE_REQUEST_CODE_EXPORT
//                    );
//                }
            }
        }
        if (id == R.id.action_openDoc) {

            if (checkStoragePermission()) {
                //permission allowed
//                saveToMail();
                Intent chooseFile;
                Intent intent;
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFile.setType("application/csv");
                intent = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);

            } else {
                requestStoragePermissionImport();
//                int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    // We don't have permission so prompt the user
//                    ActivityCompat.requestPermissions(
//                            this,
//                            storagePermissions,
//                            STORAGE_REQUEST_CODE_EXPORT
//                    );
//                }
            }
        }
        if (id == R.id.action_viewDoc) {

            if (checkStoragePermission()) {
                //permission allowed
//                saveToMail();
                Intent chooseFile;
                Intent intent;
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFile.setType("application/*");
                intent = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(intent, ACTIVITY_CHOOSE_ANYFILE);

            } else {
                requestStoragePermissionImport();
//                int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    // We don't have permission so prompt the user
//                    ActivityCompat.requestPermissions(
//                            this,
//                            storagePermissions,
//                            STORAGE_REQUEST_CODE_EXPORT
//                    );
//                }
            }
        }
        if (id == R.id.action_getDocFireStore) {
            showSnackIfOffline();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        String path = "";
        if (requestCode == ACTIVITY_CHOOSE_ANYFILE) {
            Uri uri = data.getData();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, getContentResolver().getType(uri));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (requestCode == ACTIVITY_CHOOSE_FILE) {
            Uri uri = data.getData();
            String fileExtStr = uri.getPath().substring(uri.getPath().lastIndexOf("."));
            System.out.println(fileExtStr);
            if (fileExtStr.contentEquals(".csv")) {
                String FilePath = getRealPathFromURI(uri); // should the path be here in this string
                File file = new File(FilePath);

                importFromSelectedCSV(file, FilePath);

//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            Uri data1 = null;
//            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
//                data1 = FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID+".provider",file);
//            }else{
//                data1 = Uri.fromFile(file);
//            }
//            intent.setDataAndType(data1, getContentResolver().getType(data1));
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            startActivity(intent);
            } else {
                Toast.makeText(context, "Invalid File\nPlease choose CSV File", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Do you want to Browse for the file?");
                builder.setCancelable(false);
                builder.setPositiveButton("Browse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (checkStoragePermission()) {
                            //permission allowed
//                saveToMail();
                            Intent chooseFile;
                            Intent intent;
                            chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                            chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                            chooseFile.setType("application/csv");
                            intent = Intent.createChooser(chooseFile, "Choose a file");
                            startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);

                        } else {
                            requestStoragePermissionImport();
//                int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    // We don't have permission so prompt the user
//                    ActivityCompat.requestPermissions(
//                            this,
//                            storagePermissions,
//                            STORAGE_REQUEST_CODE_EXPORT
//                    );
//                }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private void importFromSelectedCSV(File file, String FilePath) {
        File csvFile1 = null, csvFile = null;
        DbManager db = new DbManager(MainActivity.this);
        if (FilePath.contains("SQLite_BillBackup.csv")) {
            csvFile1 = file;

            if (csvFile1.exists()) {
                //backup exists
                System.out.println("abs path1:" + csvFile1.getAbsolutePath());
                try {
                    CSVReader csvReader1 = new CSVReader(new FileReader(csvFile1.getAbsolutePath()));
                    String[] nexLine1;
                    if (csvFile1.length() > 0) {
                        db.clearBill();
                    }
//                if(csvFile1.exists() && csvReader1.readNext() != null){
//                    db.clearBill();
//                }
                    while ((nexLine1 = csvReader1.readNext()) != null) {
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
                        int other = Integer.parseInt(nexLine1[24]);
                        int other1 = Integer.parseInt(nexLine1[25]);
                        int total = Integer.parseInt(nexLine1[26]);
                        String addedDateTime = String.valueOf(nexLine1[27]);
                        String date = String.valueOf(nexLine1[28]);
                        String otherItems = String.valueOf(nexLine1[29]);
                        String otherItems1 = String.valueOf(nexLine1[30]);
                        db.addFromCSV1(billNo, custName, orange, kokam, lemon, sarbat, pachak, wala, lsoda, ssrbt, lorange, llemon, jsoda, sSoda, water, lassi, vanilla, pista, stwbry, mango, btrsch, kulfi, cbar, fpack, other, other1, total, addedDateTime, date, otherItems, otherItems1);
//                    db.updateExpOne(id,selling,expense,profit,date,expenses);
                    }
                    Toast.makeText(context, "Backup Restored Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    System.out.println(e);
                    Toast.makeText(context, e.getMessage() + "cought herer", Toast.LENGTH_SHORT).show();
                }
            } else {
                //backup doesnt exist
                Toast.makeText(context, "No Backup Found", Toast.LENGTH_SHORT).show();
            }
        } else {
            csvFile = file;

            if (csvFile.exists()) {
                //backup exists
                System.out.println("abs path:" + csvFile.getAbsolutePath());
                try {
                    CSVReader csvReader = new CSVReader(new FileReader(csvFile.getAbsolutePath()));
                    String[] nexLine;
                    while ((nexLine = csvReader.readNext()) != null) {
                        int id = Integer.parseInt(nexLine[0]);
                        int selling = Integer.parseInt(nexLine[1]);
                        int expense = Integer.parseInt(nexLine[2]);
                        int profit = Integer.parseInt(nexLine[3]);
                        String date = nexLine[4];
                        String expenses = nexLine[5];
                        db.addFromCSV(id, selling, expense, profit, date, expenses);
//                    db.updateExpOne(id,selling,expense,profit,date,expenses);
                    }
                    Toast.makeText(context, "Backup Restored Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                //backup doesnt exist
                Toast.makeText(context, "No Backup Found", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private String getRealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Downloads.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
//        while(cursor.moveToNext()){
//            System.out.println(cursor);
//        }
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATA);
        cursor.moveToFirst();
        System.out.println("cindent:" + cursor.getString(column_index));
        return cursor.getString(column_index);
    }

    private void requestStoragePermissionImport() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE_IMPORT);
    }

    private void requestStoragePermissionExport() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE_EXPORT);
    }


    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void saveToMail() {
        String pathAndFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/SQLiteBackup/Expense_Backup/" + "SQLite_ExpBackup.csv";
        String pathAndFileName1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/SQLiteBackup/Billing_Backup/" + "SQLite_BillBackup.csv";
        System.out.println(pathAndFileName);
        File csvFile = new File(pathAndFileName);
        File csvFile1 = new File(pathAndFileName1);
        if (csvFile.exists() && csvFile1.exists()) {
            ArrayList<Uri> files = new ArrayList<>();
            ArrayList<String> filesToSend = new ArrayList<>();
            filesToSend.add(pathAndFileName);
            filesToSend.add(pathAndFileName1);
            for (String path : filesToSend /* List of the files you want to send */) {
                File file = new File(path);
                Uri uri11 = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri11 = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                } else {
                    uri11 = Uri.fromFile(file);
                }
                files.add(uri11);
            }
            if (files.size() > 0) {
                Intent sendEmail = new Intent(Intent.ACTION_SEND_MULTIPLE);
                sendEmail.setType("application/csv");
//                sendEmail.putExtra(Intent.EXTRA_EMAIL,new String[]{USER_EMAIL});
//                sendEmail.putExtra(Intent.EXTRA_SUBJECT,EMAIL_SUB);
//                sendEmail.putExtra(Intent.EXTRA_TEXT,EMAIL_BODY);
                //        sendEmail.putExtra(Intent.EXTRA_STREAM, uri1);
                sendEmail.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                startActivity(Intent.createChooser(sendEmail, "Email:"));
            } else {
                Toast.makeText(context, "Files not Found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Files not found", Toast.LENGTH_SHORT).show();
        }

    }


    public void importCSV() {
        DbManager db = new DbManager(MainActivity.this);
        String pathAndFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/SQLiteBackup/Expense_Backup/" + "SQLite_ExpBackup.csv";
        String pathAndFileName1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/SQLiteBackup/Billing_Backup/" + "SQLite_BillBackup.csv";
        System.out.println(pathAndFileName);
        File csvFile = new File(pathAndFileName);
        File csvFile1 = new File(pathAndFileName1);

        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", csvFile);

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
        Uri uri1 = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", filePath);

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


        if (csvFile.exists()) {
            //backup exists
            System.out.println("abs path:" + csvFile.getAbsolutePath());
            try {
                CSVReader csvReader = new CSVReader(new FileReader(csvFile.getAbsolutePath()));
                String[] nexLine;
                while ((nexLine = csvReader.readNext()) != null) {
                    int id = Integer.parseInt(nexLine[0]);
                    int selling = Integer.parseInt(nexLine[1]);
                    int expense = Integer.parseInt(nexLine[2]);
                    int profit = Integer.parseInt(nexLine[3]);
                    String date = nexLine[4];
                    String expenses = nexLine[5];
                    System.out.println("see me>>>" + expenses);
                    db.addFromCSV(id, selling, expense, profit, date, expenses);
//                    db.updateExpOne(id,selling,expense,profit,date,expenses);
                }
//                Toast.makeText(context, "Backup Restored Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        } else {
            //backup doesnt exist
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "No Backup Found", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (csvFile1.exists()) {
            //backup exists
            System.out.println("abs path1:" + csvFile1.getAbsolutePath());
            try {
                CSVReader csvReader1 = new CSVReader(new FileReader(csvFile1.getAbsolutePath()));
                String[] nexLine1;

                if (csvFile1.length() > 0) {
                    db.clearBill();
                }
                while ((nexLine1 = csvReader1.readNext()) != null) {
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
                    int other = Integer.parseInt(nexLine1[24]);
                    int other1 = Integer.parseInt(nexLine1[25]);
                    int total = Integer.parseInt(nexLine1[26]);
                    String addedDateTime = String.valueOf(nexLine1[27]);
                    String date = String.valueOf(nexLine1[28]);
                    String otherItems = String.valueOf(nexLine1[28]);
                    String otherItems1 = String.valueOf(nexLine1[28]);
                    db.addFromCSV1(billNo, custName, orange, kokam, lemon, sarbat, pachak, wala, lsoda, ssrbt, lorange, llemon, jsoda, sSoda, water, lassi, vanilla, pista, stwbry, mango, btrsch, kulfi, cbar, fpack, other, other1, total, addedDateTime, date, otherItems, otherItems1);
//                    db.updateExpOne(id,selling,expense,profit,date,expenses);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Backup Restored Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                System.out.println(e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, e.getMessage() + "cought herer", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            //backup doesnt exist
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "No Backup Found", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void exportCSV() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/SQLiteBackup/Expense_Backup").toString();
        String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/SQLiteBackup/Billing_Backup").toString();
        File folder = new File(path);
        File folder1 = new File(path1);
        boolean isFolderCreated = false;
        boolean isFolderCreated1 = false;
        if (!folder.exists()) {
            //do mkdir
            isFolderCreated = folder.mkdirs(); //create folder if not exists
        }
        if (!folder1.exists()) {
            //do mkdir
            isFolderCreated1 = folder1.mkdirs(); //create folder if not exists
        }
        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HHmmss");
//        String dateFmt = sdf.format(c.getTime());
        String csvFileName = "SQLite_ExpBackup.csv";
        String csvFileName1 = "SQLite_BillBackup.csv";
        //file path and name
        String filepathAndName = folder.toString() + "/" + csvFileName;
        String filepathAndName1 = folder1.toString() + "/" + csvFileName1;
        System.out.println(filepathAndName);
        System.out.println(filepathAndName1);
        DbManager db = new DbManager(MainActivity.this);
        Cursor res = db.getAllExp();
        Cursor res1 = db.getData();
        try {
            FileWriter fw = new FileWriter(filepathAndName);
            while (res.moveToNext()) {
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
            while (res1.moveToNext()) {
                fw1.append(String.valueOf(res1.getInt(1)));
                fw1.append(",");
                fw1.append(String.valueOf(res1.getString(2).isEmpty() ? "-" : res1.getString(2)));
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
                fw1.append(String.valueOf(res1.getString(28)));
                fw1.append(",");
                fw1.append(String.valueOf(res1.getString(29)));
                fw1.append("\n");
            }
            fw1.flush();
            fw1.close();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "BackedUp:" + filepathAndName + "\t" + filepathAndName1, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotifChannel();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        setContentView(R.layout.activity_main);
        pd = new ProgressDialog(this);
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        context = getApplicationContext();
//        FirstFragment f1 = new FirstFragment();
//        FirstFragment f1 = (FirstFragment)getSupportFragmentManager().findFragmentById(R.id.frag1);
//        SecondFragment f2 = new SecondFragment();
//        SecondFragment f2 = (SecondFragment)getSupportFragmentManager().findFragmentById(R.id.frag2);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Reset Bill Series");
                builder.setMessage("Do you want to reset Billno Series?");
                builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        do_resetSeq();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                final View customView = getLayoutInflater().inflate(R.layout.custom_table, null);
                boolean isDarkThemeOn = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
                try {
                    String str = f1.custName.getText().toString();
                    if (!str.equals("") || str != null) {
//                        isValid = Pattern.compile("^[a-zA-Z0-9 _@#$&*.]{2,}$").matcher(str).matches();
                        isValid = true;
                        if (isValid) {
                            validCustName = str;
                        } else if (!str.equals("")) {
                            Toast.makeText(context, "Only Alphanumeric characters allowed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        validCustName = "";
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                DbManager db = new DbManager(getApplicationContext());
//                map.putAll(items1);
//                map.putAll(items2);
                for (Map.Entry<String, Item> itr : map.entrySet()) {
                    System.out.println(itr.getKey() + "   ::   " + itr.getValue().getTotal());
                }
//                System.out.println("1st Map size:" + f1.items.size());
//                System.out.println("2nd map size:" + f2.items2.size());
                map.putAll(f1.items);
                map.putAll(f2.items2);
                System.out.println("map sizeeeee:" + map.size());
                TableLayout tableLayout = customView.findViewById(R.id.table_layout);
                TextView tb1 = customView.findViewById(R.id.item);
                TextView tb2 = customView.findViewById(R.id.qty);
                TextView tb3 = customView.findViewById(R.id.rate);
                TextView tb4 = customView.findViewById(R.id.total);
                tableLayout.setWeightSum(1);
                for (Map.Entry<String, Item> i : map.entrySet()) {
                    Item item = i.getValue();
                    TableRow tableRow = new TableRow(customView.getContext());
//                    tableRow.setPadding(110, 50, 110, 10);
//                    for(int j = 0; j < 3 ;j++){
                    TextView tv1 = new TextView(customView.getContext());
                    tv1.setText(i.getKey());
                    tv1.setGravity(Gravity.CENTER_HORIZONTAL);
//                    tv1.setTextColor(Color.BLACK);
//                    tv1.setPadding(70,3,70,3);
                    TextView tv2 = new TextView(customView.getContext());
                    tv2.setText(String.valueOf(item.getQty()));
                    tv2.setGravity(Gravity.CENTER_HORIZONTAL);
//                    tv2.setTextColor(Color.BLACK);
//                    tv2.setPadding(50, 3, 50, 3);
                    TextView tv3 = new TextView(customView.getContext());
                    tv3.setGravity(Gravity.CENTER_HORIZONTAL);
//                    tv3.setTextColor(Color.BLACK);
                    tv3.setText(String.valueOf(item.getRate()));
//                    tv3.setPadding(50, 3, 50, 3);
                    TextView tv4 = new TextView(customView.getContext());
                    tv4.setText(String.valueOf(item.getTotal()));
//                    tv4.setPadding(50, 3, 50, 3);
                    tv4.setGravity(Gravity.CENTER_HORIZONTAL);
//                    tv4.setTextColor(Color.BLACK);
                    if (isDarkThemeOn) {
                        tb1.setTextColor(Color.WHITE);
                        tb2.setTextColor(Color.WHITE);
                        tb3.setTextColor(Color.WHITE);
                        tb4.setTextColor(Color.WHITE);
                        tv1.setTextColor(Color.WHITE);
                        tv2.setTextColor(Color.WHITE);
                        tv3.setTextColor(Color.WHITE);
                        tv4.setTextColor(Color.WHITE);
                    } else {
                        tb1.setTextColor(Color.BLACK);
                        tb2.setTextColor(Color.BLACK);
                        tb3.setTextColor(Color.BLACK);
                        tb4.setTextColor(Color.BLACK);
                        tv1.setTextColor(Color.BLACK);
                        tv2.setTextColor(Color.BLACK);
                        tv3.setTextColor(Color.BLACK);
                        tv4.setTextColor(Color.BLACK);
                    }
                    tableRow.addView(tv1);
                    tableRow.addView(tv2);
                    tableRow.addView(tv3);
                    tableRow.addView(tv4);
//                    }
                    tableLayout.addView(tableRow);
                }
                TextView tvFinal = new TextView(customView.getContext());
                int total = f1.coldrinkFinalTotal + f2.IceCreamFinalTotal;
                tvFinal.setText("\nTotal: " + total);
                tvFinal.setPadding(10, 10, 40, 10);
                tvFinal.setGravity(Gravity.END);
                if (isDarkThemeOn) {
                    tvFinal.setTextColor(Color.WHITE);
                } else {
                    tvFinal.setTextColor(Color.BLACK);
                }
                tableLayout.addView(tvFinal);

//                if (f1.items.size() == 0 && f2.items2.size() == 0) {
//                    map.clear();
//                }
                for (Map.Entry<String, Item> i : f1.items.entrySet()) {
                    if (map.containsKey(i.getKey())) {
                        map.put(i.getKey(), i.getValue());
                        System.out.println("added:::::->" + i.getKey());
                    } else {
                        System.out.println("remved:::::->" + i.getKey());
                        map.remove(i.getKey());
                    }
                }
                for (Map.Entry<String, Item> i : f2.items2.entrySet()) {
                    if (map.containsKey(i.getKey())) {
                        map.put(i.getKey(), i.getValue());
                        System.out.println("added:::::->" + i.getKey());
                    } else {
                        System.out.println("remved:::::->" + i.getKey());
                        map.remove(i.getKey());
                    }
                }
                if (map.size() != 0) {
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(getApplicationContext(), "Billing", Toast.LENGTH_SHORT).show();
                            masterFinal = f1.coldrinkFinalTotal + f2.IceCreamFinalTotal;
//                            Toast.makeText(MainActivity.this, "FinalTotal:"+masterFinal, Toast.LENGTH_SHORT).show();
                            int latestBillNo = db.getLatestBillNo();
                            if (latestBillNo != 0) {
                                SharedPreferences sp = getSharedPreferences("key_code", MODE_PRIVATE);
//                                int code = sp.getInt("code", 0);
                                sp.edit().putInt("code", latestBillNo).commit();
                                System.out.println("latest BillNo: " + latestBillNo);
                            }
                            billNo_generated = generateSeq();
                            Date now = new Date();
                            long timestamp = now.getTime();
                            SimpleDateFormat sdf = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                            }
                            String dateStr = sdf.format(timestamp);
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
                            String currDate = sdf1.format(now.getTime());
                            //tcf.setText("Orange: "+f1.orangeTotal+"\nKokam: "+f1.kokamTotal+"\nLemon: "+f1.lemonTotal+"\nSarbat: "+f1.sarbatTotal+"\n----------------\nTotal: "+f1.finalTotal+"\nDate & Time: "+dateStr);
                            String res = db.addRecord(billNo_generated, validCustName, f1.orangeTotal, f1.kokamTotal, f1.lemonTotal, f1.sarbatTotal, f1.pachakTotal, f1.walaTotal, f1.lSodaTotal, f1.ssrbtTotal, f1.lorangeTotal, f1.LlemonTotal, f1.jeeraTotal, f1.sSodaTotal, f1.waterTotal, f1.lassiTotal, f2.vanillaTotal, f2.pistaTotal, f2.sbryTotal, f2.mangoTotal, f2.btrSchTotal, f2.kulfiTotal, f2.cbarTotal, f2.fpackTotal, f1.otherAmt, f2.otherAmt1, masterFinal, dateStr, currDate, f1.sbTemp != null ? String.valueOf(f1.sbTemp) : "", f2.sbTemp1 != null ? String.valueOf(f2.sbTemp1) : "");
                            //upload on FireStore
//                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
//                            builder1.setCancelable(false);
//                            builder1.setTitle("Choose");
//                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                }
//                            });
//                            View view_list = getLayoutInflater().inflate(R.layout.custom_list,null);
//                            builder1.setView(view_list);
//                            listView=view_list.findViewById(R.id.listView);
//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
//                                    android.R.layout.simple_list_item_1, new String[]{"Bill_temp","Bill_super"});
//                            listView.setAdapter(adapter);
//                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                                    String value=adapter.getItem(position);
                                    uploadToFireStore(billNo_generated, validCustName, dateStr, currDate);
//                                }
//                            });
//                            pd.dismiss();
//                            AlertDialog dialog1 = builder1.create();
//                            dialog1.show();
                            if (checkStoragePermission()) {
                                //permission allowed
//                                printInvoice(billNo_generated, map, masterFinal, null, null);
                                try {
                                    printInvoice1(billNo_generated, map, masterFinal, null, validCustName);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //permission denied
                                requestStoragePermissionExport();
                            }
                            Toast.makeText(getApplicationContext(), "Bill generated successfully "+billNo_generated, Toast.LENGTH_SHORT).show();
                            System.out.println("=====================data is going to be print======================");
                            System.out.println("BillNo: " + billNo_generated);
                            System.out.println("Time: " + dateStr);
                            System.out.println("FinalTotal:" + masterFinal);
                            f1.coldrinkFinalTotal = 0;
                            f2.IceCreamFinalTotal = 0;
                            f1.finalTotal = 0;
                            f2.finalTotal = 0;
                            f1.otherAmt = 0;
                            f2.otherAmt1 = 0;
                            validCustName = "";
                            clearAll();
                            map.clear();
                            if (f1.sbTemp != null) {
                                f1.sbTemp.delete(0, f1.sbTemp.length());
                            }

                            if (f2.sbTemp1 != null) {
                                f2.sbTemp1.delete(0, f2.sbTemp1.length());
                            }
                            if (f1.itemsBill != null) {
                                f1.itemsBill.clear();
                            }
                            if (f2.itemsBill2 != null) {
                                f2.itemsBill2.clear();
                            }
                        }

                        private void clearAll() {
                            f1.finalTotal = 0;
                            f2.finalTotal = 0;
                            if (!f1.items.isEmpty()) {
                                f1.resetAllseeks1();
                            }
                            if (!f2.items2.isEmpty()) {
                                f2.resetAllseeks2();
                            }
                            f1.coldrinkFinalTotal = 0;
                            f2.IceCreamFinalTotal = 0;
                            f1.etOther.setText("");
                            f2.etOther1.setText("");
                            validCustName = "";
                            f1.items.clear();
                            f2.items2.clear();
                            f1.custName.setText(validCustName);
                            map.clear();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                            map.clear();
                        }
                    });
                    builder.setCancelable(false);
                    builder.setIcon(R.drawable.ic_print);
                    builder.setTitle("Preview And Print");

                    if (isValid) {
                        sb = "Customer Name: " + validCustName;
                    } else {
                        sb = "Customer Name: ";
                    }
                    sb = sb + "\nItems Selected:";
                    builder.setView(customView);
//                    for (Map.Entry<String, Item> itr : map.entrySet()) {
//                        sb.append(itr.getKey() + "   " + itr.getValue().getQty() + "   " + itr.getValue().getTotal() + "\n");
//                    }
//                    sb.append("----------------------\nTotal: " + Integer.parseInt(String.valueOf((Integer.parseInt(String.valueOf(f1.coldrinkFinalTotal))) + (Integer.parseInt(String.valueOf(f2.IceCreamFinalTotal))))) + "\n");
                    builder.setMessage(sb);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
//                    map.clear();
                    Toast.makeText(MainActivity.this, "Please select Items", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getLastestBillNoFromFireStore() {
        pd.show();
        pd.setMessage("Fetching Data... Please wait");
        FirebaseFirestore dbRes = FirebaseFirestore.getInstance();
        CollectionReference billCollectionRef = dbRes.collection("temp");
        billCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                View customView = getLayoutInflater().inflate(R.layout.custom_table_ext, null);
                TableLayout tableLayout = customView.findViewById(R.id.table_layout);
                boolean isDarkThemeOn = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
                TextView tb0 = customView.findViewById(R.id.billno);
                TextView cname = customView.findViewById(R.id.custname);
                TextView tb1 = customView.findViewById(R.id.orange);
                TextView tb2 = customView.findViewById(R.id.kokam);
                TextView tb3 = customView.findViewById(R.id.lemon);
                TextView tb4 = customView.findViewById(R.id.sarbat);
                TextView tb5 = customView.findViewById(R.id.pachak);
                TextView tb6 = customView.findViewById(R.id.wala);
                TextView tb7 = customView.findViewById(R.id.lsoda);
                TextView tb8 = customView.findViewById(R.id.ssrbt);
                TextView tb9 = customView.findViewById(R.id.lorange);
                TextView tb10 = customView.findViewById(R.id.llemon);
                TextView tbJsoda = customView.findViewById(R.id.jsoda);
                TextView tbSsoda = customView.findViewById(R.id.sSoda);
                TextView tbWater = customView.findViewById(R.id.water);
                TextView tbLassi = customView.findViewById(R.id.lassi);
                TextView tb11 = customView.findViewById(R.id.vanilla);
                TextView tb12 = customView.findViewById(R.id.pista);
                TextView tb13 = customView.findViewById(R.id.stwbry);
                TextView tb14 = customView.findViewById(R.id.mango);
                TextView tb15 = customView.findViewById(R.id.btrsch);
                TextView tbKulfi = customView.findViewById(R.id.kulfi);
                TextView tbCbar = customView.findViewById(R.id.cbar);
                TextView tbFpack = customView.findViewById(R.id.fpack);
                TextView tbOther = customView.findViewById(R.id.other);
                TextView tbOther1 = customView.findViewById(R.id.other1);
                TextView tb16 = customView.findViewById(R.id.total);
                TextView tb17 = customView.findViewById(R.id.date1);
                tableLayout.setWeightSum(1);

                int total = 0;
                int billCount = 0;

                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.exists()) {
//                        sb.append("Billno=" + document.getData().get("billNo") + "\nTotal=" + document.getData().get("total") + "\nDate=" + document.getData().get("date") + "\n--------------------------------------------------------\n");
                        total += document.getData().containsKey("total")? (Long) document.getData().get("total") :0;
                        billCount += 1;
                        TableRow tableRow = new TableRow(customView.getContext());
                        tableRow.setPadding(10, 10, 10, 10);
                        TextView tv1 = new TextView(customView.getContext());
                        tv1.setText(String.valueOf(document.getData().containsKey("billNo")? Integer.parseInt(String.valueOf(document.getData().get("billNo"))) :0));
                        tv1.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView cname1 = new TextView(customView.getContext());
                        cname1.setText(String.valueOf(document.getData().containsKey("custName")? document.getData().get("custName") :""));
                        cname1.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv2 = new TextView(customView.getContext());
                        tv2.setText(String.valueOf(document.getData().containsKey("orange")? Integer.parseInt(String.valueOf(document.getData().get("orange"))) :0));
                        tv2.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv3 = new TextView(customView.getContext());
                        tv3.setText(String.valueOf(document.getData().containsKey("kokam")? Integer.parseInt(String.valueOf(document.getData().get("kokam"))) :0));
                        tv3.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv4 = new TextView(customView.getContext());
                        tv4.setText(String.valueOf(document.getData().containsKey("lemon")? Integer.parseInt(String.valueOf(document.getData().get("lemon"))) :0));
                        tv4.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv5 = new TextView(customView.getContext());
                        tv5.setText(String.valueOf(document.getData().containsKey("sarbat")? Integer.parseInt(String.valueOf(document.getData().get("sarbat"))) :0));
                        tv5.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv6 = new TextView(customView.getContext());
                        tv6.setText(String.valueOf(document.getData().containsKey("pachak")? Integer.parseInt(String.valueOf(document.getData().get("pachak"))) :0));
                        tv6.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv7 = new TextView(customView.getContext());
                        tv7.setText(String.valueOf(document.getData().containsKey("ssrbt")? Integer.parseInt(String.valueOf(document.getData().get("ssrbt"))) :0));
                        tv7.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv8 = new TextView(customView.getContext());
                        tv8.setText(String.valueOf(document.getData().containsKey("llemon")? Integer.parseInt(String.valueOf(document.getData().get("llemon"))) :0));
                        tv8.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv9 = new TextView(customView.getContext());
                        tv9.setText(String.valueOf(document.getData().containsKey("jsoda")? Integer.parseInt(String.valueOf(document.getData().get("jsoda"))) :0));
                        tv9.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv10 = new TextView(customView.getContext());
                        tv10.setText(String.valueOf(document.getData().containsKey("sSoda")? Integer.parseInt(String.valueOf(document.getData().get("sSoda"))) :0));
                        tv10.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv11 = new TextView(customView.getContext());
                        tv11.setText(String.valueOf(document.getData().containsKey("water")? Integer.parseInt(String.valueOf(document.getData().get("water"))) :0));
                        tv11.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvJsoda = new TextView(customView.getContext());
                        tvJsoda.setText(String.valueOf(document.getData().containsKey("jsoda")? Integer.parseInt(String.valueOf(document.getData().get("jsoda"))) :0));
                        tvJsoda.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvSsoda = new TextView(customView.getContext());
                        tvSsoda.setText(String.valueOf(document.getData().containsKey("sSoda")? Integer.parseInt(String.valueOf(document.getData().get("sSoda"))) :0));
                        tvSsoda.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvWater = new TextView(customView.getContext());
                        tvWater.setText(String.valueOf(document.getData().containsKey("water")? Integer.parseInt(String.valueOf(document.getData().get("water"))) :0));
                        tvWater.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvLassi = new TextView(customView.getContext());
                        tvLassi.setText(String.valueOf(document.getData().containsKey("lassi")? Integer.parseInt(String.valueOf(document.getData().get("lassi"))) :0));
                        tvLassi.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv12 = new TextView(customView.getContext());
                        tv12.setText(String.valueOf(document.getData().containsKey("vanilla")? Integer.parseInt(String.valueOf(document.getData().get("vanilla"))):0));
                        tv12.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv13 = new TextView(customView.getContext());
                        tv13.setText(String.valueOf(document.getData().containsKey("pista")? Integer.parseInt(String.valueOf(document.getData().get("pista"))) :0));
                        tv13.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv14 = new TextView(customView.getContext());
                        tv14.setText(String.valueOf(document.getData().containsKey("stwbry")? Integer.parseInt(String.valueOf(document.getData().get("stwbry"))) :0));
                        tv14.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv15 = new TextView(customView.getContext());
                        tv15.setText(String.valueOf(document.getData().containsKey("mango")? Integer.parseInt(String.valueOf(document.getData().get("mango"))) :0));
                        tv15.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv16 = new TextView(customView.getContext());
                        tv16.setText(String.valueOf(document.getData().containsKey("btrsch")? Integer.parseInt(String.valueOf(document.getData().get("btrsch"))) :0));
                        tv16.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvKulfi = new TextView(customView.getContext());
                        tvKulfi.setText(String.valueOf(document.getData().containsKey("kulfi")? Integer.parseInt(String.valueOf( document.getData().get("kulfi"))) :0));
                        tvKulfi.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvCbar = new TextView(customView.getContext());
                        tvCbar.setText(String.valueOf(document.getData().containsKey("cbar")? Integer.parseInt(String.valueOf(document.getData().get("cbar"))) :0));
                        tvCbar.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvFpack = new TextView(customView.getContext());
                        tvFpack.setText(String.valueOf(document.getData().containsKey("fpack")? Integer.parseInt(String.valueOf(document.getData().get("fpack"))) :0));
                        tvFpack.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvOther = new TextView(customView.getContext());
                        tvOther.setText(String.valueOf(document.getData().containsKey("otherAmt")?  Integer.parseInt(String.valueOf( document.getData().get("otherAmt"))) :0));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            tvOther.setTooltipText(String.valueOf(document.getData().containsKey("otherItems")? (String) document.getData().get("otherItems") :""));
                        }
                        tvOther.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvOther1 = new TextView(customView.getContext());
                        tvOther1.setText(String.valueOf(document.getData().containsKey("otherAmt1")? Integer.parseInt(String.valueOf(document.getData().get("otherAmt1"))) :0));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            tvOther1.setTooltipText(String.valueOf(document.getData().containsKey("otherItems1")? (String) document.getData().get("otherItems1") :""));
                        }
                        tvOther1.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv17 = new TextView(customView.getContext());
                        tv17.setText(String.valueOf(document.getData().containsKey("total")? Integer.parseInt(String.valueOf(document.getData().get("total"))) :0));
                        tv17.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv18 = new TextView(customView.getContext());
                        tv18.setText(String.valueOf(document.getData().containsKey("date")? String.valueOf(document.getData().get("date")) :""));
                        tv18.setGravity(Gravity.CENTER_HORIZONTAL);


                        if (isDarkThemeOn) {
                            cname.setTextColor(Color.WHITE);
                            tb0.setTextColor(Color.WHITE);
                            tb1.setTextColor(Color.WHITE);
                            tb2.setTextColor(Color.WHITE);
                            tb3.setTextColor(Color.WHITE);
                            tb4.setTextColor(Color.WHITE);
                            tb5.setTextColor(Color.WHITE);
                            tb6.setTextColor(Color.WHITE);
                            tb7.setTextColor(Color.WHITE);
                            tb8.setTextColor(Color.WHITE);
                            tb9.setTextColor(Color.WHITE);
                            tb10.setTextColor(Color.WHITE);
                            tb11.setTextColor(Color.WHITE);
                            tb12.setTextColor(Color.WHITE);
                            tb13.setTextColor(Color.WHITE);
                            tb14.setTextColor(Color.WHITE);
                            tb15.setTextColor(Color.WHITE);
                            tb16.setTextColor(Color.WHITE);
                            tb17.setTextColor(Color.WHITE);
                            tbJsoda.setTextColor(Color.WHITE);
                            tbSsoda.setTextColor(Color.WHITE);
                            tbKulfi.setTextColor(Color.WHITE);
                            tbCbar.setTextColor(Color.WHITE);
                            tbFpack.setTextColor(Color.WHITE);
                            tbWater.setTextColor(Color.WHITE);
                            tbLassi.setTextColor(Color.WHITE);
                            tbOther.setTextColor(Color.WHITE);
                            tbOther1.setTextColor(Color.WHITE);
                        } else {
                            cname.setTextColor(Color.BLACK);
                            tb0.setTextColor(Color.BLACK);
                            tb1.setTextColor(Color.BLACK);
                            tb2.setTextColor(Color.BLACK);
                            tb3.setTextColor(Color.BLACK);
                            tb4.setTextColor(Color.BLACK);
                            tb5.setTextColor(Color.BLACK);
                            tb6.setTextColor(Color.BLACK);
                            tb7.setTextColor(Color.BLACK);
                            tb8.setTextColor(Color.BLACK);
                            tb9.setTextColor(Color.BLACK);
                            tb10.setTextColor(Color.BLACK);
                            tb11.setTextColor(Color.BLACK);
                            tb12.setTextColor(Color.BLACK);
                            tb13.setTextColor(Color.BLACK);
                            tb14.setTextColor(Color.BLACK);
                            tb15.setTextColor(Color.BLACK);
                            tb16.setTextColor(Color.BLACK);
                            tb17.setTextColor(Color.BLACK);
                            tbJsoda.setTextColor(Color.BLACK);
                            tbSsoda.setTextColor(Color.BLACK);
                            tbKulfi.setTextColor(Color.BLACK);
                            tbCbar.setTextColor(Color.BLACK);
                            tbFpack.setTextColor(Color.BLACK);
                            tbWater.setTextColor(Color.BLACK);
                            tbLassi.setTextColor(Color.BLACK);
                            tbOther.setTextColor(Color.BLACK);
                            tbOther1.setTextColor(Color.BLACK);
                        }
                        tableRow.addView(tv1);
                        tableRow.addView(cname1);
                        tableRow.addView(tv2);
                        tableRow.addView(tv3);
                        tableRow.addView(tv4);
                        tableRow.addView(tv5);
                        tableRow.addView(tv6);
                        tableRow.addView(tv7);
                        tableRow.addView(tv8);
                        tableRow.addView(tv9);
                        tableRow.addView(tv10);
                        tableRow.addView(tv11);
                        tableRow.addView(tvJsoda);
                        tableRow.addView(tvSsoda);
                        tableRow.addView(tvWater);
                        tableRow.addView(tvLassi);
                        tableRow.addView(tv12);
                        tableRow.addView(tv13);
                        tableRow.addView(tv14);
                        tableRow.addView(tv15);
                        tableRow.addView(tv16);
                        tableRow.addView(tvKulfi);
                        tableRow.addView(tvCbar);
                        tableRow.addView(tvFpack);
                        tableRow.addView(tvOther);
                        tableRow.addView(tvOther1);
                        tableRow.addView(tv17);
                        tableRow.addView(tv18);

                        tableLayout.addView(tableRow);
                    } else {
                        TableRow tableRow = new TableRow(customView.getContext());
                        tableRow.setPadding(10, 10, 10, 10);

                        TextView tvNthing = new TextView(customView.getContext());
                        tvNthing.setText(String.valueOf("No data found"));
                        tvNthing.setGravity(Gravity.CENTER_HORIZONTAL);

                        tableRow.addView(tvNthing);
                        tableLayout.addView(tableRow);
                        return;
                    }
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setTitle("Cloud Data");
                builder1.setView(customView);
                builder1.setCancelable(false);
                builder1.setMessage("Bill Count: "+billCount+"\nTotal: "+total);
                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                pd.dismiss();
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadToFireStore(Long billNo_generated, String validCustName, String dateStr, String currDate) {
        db = FirebaseFirestore.getInstance();
        pd.setMessage("Uploading data to FireStore");
        pd.show();
        Map<String, Object> map_temp = new HashMap<>();
        if (billNo_generated != 0L) {
            map_temp.put("billNo", billNo_generated);
        }
        if (validCustName != null) {
            map_temp.put("custName", validCustName);
        }
        if (f1.orangeTotal != 0) {
            map_temp.put("orange", f1.orangeTotal);
        }
        if (f1.kokamTotal != 0) {
            map_temp.put("kokam", f1.kokamTotal);
        }
        if (f1.lemonTotal != 0) {
            map_temp.put("lemon", f1.lemonTotal);
        }
        if (f1.sarbatTotal != 0) {
            map_temp.put("sarbat", f1.sarbatTotal);
        }
        if (f1.pachakTotal != 0) {
            map_temp.put("pachak", f1.pachakTotal);
        }
        if (f1.walaTotal != 0) {
            map_temp.put("wala", f1.walaTotal);
        }
        if (f1.lSodaTotal != 0) {
            map_temp.put("lsoda", f1.lSodaTotal);
        }
        if (f1.ssrbtTotal != 0) {
            map_temp.put("ssrbt", f1.ssrbtTotal);
        }
        if (f1.lorangeTotal != 0) {
            map_temp.put("lorange", f1.lorangeTotal);
        }
        if (f1.LlemonTotal != 0) {
            map_temp.put("llemon", f1.LlemonTotal);
        }
        if (f1.jeeraTotal != 0) {
            map_temp.put("jsoda", f1.jeeraTotal);
        }
        if (f1.sSodaTotal != 0) {
            map_temp.put("ssoda", f1.sSodaTotal);
        }
        if (f1.waterTotal != 0) {
            map_temp.put("water", f1.waterTotal);
        }
        if (f1.lassiTotal != 0) {
            map_temp.put("lassi", f1.lassiTotal);
        }

        if (f2.vanillaTotal != 0) {
            map_temp.put("vanilla", f2.vanillaTotal);
        }
        if (f2.pistaTotal != 0) {
            map_temp.put("pista", f2.pistaTotal);
        }
        if (f2.sbryTotal != 0) {
            map_temp.put("stwbry", f2.sbryTotal);
        }
        if (f2.mangoTotal != 0) {
            map_temp.put("mango", f2.mangoTotal);
        }
        if (f2.btrSchTotal != 0) {
            map_temp.put("btrsch", f2.btrSchTotal);
        }
        if (f2.kulfiTotal != 0) {
            map_temp.put("kulfi", f2.kulfiTotal);
        }
        if (f2.cbarTotal != 0) {
            map_temp.put("cbar", f2.cbarTotal);
        }
        if (f2.fpackTotal != 0) {
            map_temp.put("fpack", f2.fpackTotal);
        }
        if (f1.otherAmt != 0) {
            map_temp.put("otherAmt", f1.otherAmt);
        }
        if (f2.otherAmt1 != 0) {
            map_temp.put("otherAmt1", f2.otherAmt1);
        }
        if (masterFinal != 0) {
            map_temp.put("total", masterFinal);
        }
        if (!dateStr.isEmpty()) {
            map_temp.put("date", dateStr);
        }
        if (!currDate.isEmpty()) {
            map_temp.put("currDate", currDate);
        }
        if (f1.sbTemp != null) {
            map_temp.put("otherItems", String.valueOf(f1.sbTemp));
        }
        if (f2.sbTemp1 != null) {
            map_temp.put("otherItems1", String.valueOf(f2.sbTemp1));
        }

        db.collection("temp").document(String.valueOf(map_temp.get("billNo"))).set(map_temp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Uploaded on FireStore Billno " + String.valueOf(map_temp.get("billNo")), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to upload on FireStore Billno " + String.valueOf(map_temp.get("billNo")), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void printInvoice1(Long billNo_generated, Map<String, Item> map, int masterFinal, Object o, Object o1) throws FileNotFoundException {
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("d", MODE_PRIVATE);
        editor = prefs.edit();
        boolean isReprint = prefs.getBoolean("isReprint", false);
        Date date = new Date();
        String datePattern = "dd/MM/yyyy";
        String fileSaveDatePattern = "yyMMddhhmmss";
        SimpleDateFormat datFormatForFileSave = new SimpleDateFormat(fileSaveDatePattern);
        String fileExt = datFormatForFileSave.format(date.getTime());
        SimpleDateFormat datePatternFormat = new SimpleDateFormat(datePattern);
        String timePattern = "hh:mm:ss a";
        SimpleDateFormat timePatternFormat = new SimpleDateFormat(timePattern);

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/Bills").toString();
        File f = new File(path);
        if(!f.exists()){
            f.mkdirs();
        }
        File file = new File(f, "GajananColdrink_" + fileExt + "_" + billNo_generated + ".pdf");
        PdfWriter pdfWriter = new PdfWriter(path + "/GajananColdrink_Bill_" + fileExt + "_" + billNo_generated + ".pdf");
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        pdfDocument.setDefaultPageSize(PageSize.A7);
        document.setMargins(10f, 10f, 10f, 10f);
        document.setFontSize(9);
        float col = 100f;
        float columnWidth[] = {col, col};
        Table table = new Table(columnWidth);
        table.setFontSize(6);
        table.setBackgroundColor(new DeviceRgb(r, g, b));
        table.addCell(new Cell().add(new Paragraph("Invoice").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setMarginTop(10f).setFontSize(13f).setMarginBottom(10f)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Gajanan Coldrink House\nOpp. Nagar Palika,Main Road,Gadhinglaj")
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(10f)
                .setMarginBottom(10f)
                .setMarginRight(10f)
        ).setBorder(Border.NO_BORDER));

        float colWidth[] = {40, 80, 70, 80};

        Table custInfoTable = new Table(colWidth).setBorder(Border.NO_BORDER);
        custInfoTable.setFontSize(6);
        custInfoTable.addCell(new Cell(0, 4).add(new Paragraph("Customer Information").setBold()).setBorder(Border.NO_BORDER));

        custInfoTable.addCell(new Cell().add(new Paragraph("Name")).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));
        custInfoTable.addCell(new Cell().add(new Paragraph(String.valueOf(o1.toString()))).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));
        custInfoTable.addCell(new Cell().add(new Paragraph("Invoice No")).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));
        custInfoTable.addCell(new Cell().add(new Paragraph(String.valueOf(billNo_generated))).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));
        custInfoTable.addCell(new Cell().add(new Paragraph("Date")).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));
        custInfoTable.addCell(new Cell().add(new Paragraph(String.valueOf(datePatternFormat.format(date.getTime())))).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));
        custInfoTable.addCell(new Cell().add(new Paragraph("Time")).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));
        custInfoTable.addCell(new Cell().add(new Paragraph(String.valueOf(timePatternFormat.format(date.getTime())))).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));


        float colWidth1[] = {100, 50, 50, 80};

        Table itemInfoTable = new Table(colWidth1).setBorder(Border.NO_BORDER);
        itemInfoTable.setFontSize(6);
        itemInfoTable.addCell(new Cell().add(new Paragraph("Item")).setTextAlignment(TextAlignment.LEFT).setBackgroundColor(new DeviceRgb(r, g, b)).setBorder(Border.NO_BORDER));
        itemInfoTable.addCell(new Cell().add(new Paragraph("Qty")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceRgb(r, g, b)).setBorder(Border.NO_BORDER));
        itemInfoTable.addCell(new Cell().add(new Paragraph("Rate(Rs.)")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceRgb(r, g, b)).setBorder(Border.NO_BORDER));
        itemInfoTable.addCell(new Cell().add(new Paragraph("Amount")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceRgb(r, g, b)).setBorder(Border.NO_BORDER));

//        itemInfoTable.addCell(new Cell().add(new Paragraph("App Creation")).setTextAlignment(TextAlignment.LEFT));
//        itemInfoTable.addCell(new Cell().add(new Paragraph("20")).setTextAlignment(TextAlignment.CENTER));
//        itemInfoTable.addCell(new Cell().add(new Paragraph("200")).setTextAlignment(TextAlignment.CENTER));
//        itemInfoTable.addCell(new Cell().add(new Paragraph("4000")).setTextAlignment(TextAlignment.CENTER));
//
//        itemInfoTable.addCell(new Cell().add(new Paragraph("WebApp Creation")).setTextAlignment(TextAlignment.LEFT));
//        itemInfoTable.addCell(new Cell().add(new Paragraph("15")).setTextAlignment(TextAlignment.CENTER));
//        itemInfoTable.addCell(new Cell().add(new Paragraph("150")).setTextAlignment(TextAlignment.CENTER));
//        itemInfoTable.addCell(new Cell().add(new Paragraph("3000")).setTextAlignment(TextAlignment.CENTER));
//
//        itemInfoTable.addCell(new Cell().add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceRgb(r,g,b)).setBorder(Border.NO_BORDER));
//        itemInfoTable.addCell(new Cell().add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceRgb(r,g,b)).setBorder(Border.NO_BORDER));
//        itemInfoTable.addCell(new Cell().add(new Paragraph("Total")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceRgb(r,g,b)).setBorder(Border.NO_BORDER));
//        itemInfoTable.addCell(new Cell().add(new Paragraph("4500")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceRgb(r,g,b)).setBorder(Border.NO_BORDER));

        for (Map.Entry<String, Item> itr : map.entrySet()) {
            itemInfoTable.addCell(new Cell().add(new Paragraph(itr.getKey())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            itemInfoTable.addCell(new Cell().add(new Paragraph(String.valueOf(itr.getValue().getQty()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            itemInfoTable.addCell(new Cell().add(new Paragraph(String.valueOf(itr.getValue().getRate()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            itemInfoTable.addCell(new Cell().add(new Paragraph(String.valueOf(itr.getValue().getTotal()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
        }

        itemInfoTable.addCell(new Cell().add(new Paragraph("Total Items:")).setTextAlignment(TextAlignment.LEFT).setBackgroundColor(new DeviceRgb(r, g, b)).setBorder(Border.NO_BORDER));
        itemInfoTable.addCell(new Cell().add(new Paragraph(String.valueOf(map.size()))).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceRgb(r, g, b)).setBorder(Border.NO_BORDER));
        itemInfoTable.addCell(new Cell().add(new Paragraph("Total(Rs.)")).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceRgb(r, g, b)).setBorder(Border.NO_BORDER));
        itemInfoTable.addCell(new Cell().add(new Paragraph(String.valueOf(masterFinal))).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(new DeviceRgb(r, g, b)).setBorder(Border.NO_BORDER));

        document.add(table);
        document.add(custInfoTable);
        document.add(itemInfoTable);

        document.close();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void printInvoice(Long billNo, Map<String, Item> map, Integer finalTotal, String dateandtime, String cname) {
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("d", MODE_PRIVATE);
        editor = prefs.edit();
        boolean isReprint = prefs.getBoolean("isReprint", false);
        PdfDocument mypdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        Date date = new Date();
        String datePattern = "dd/MM/yyyy";
        String fileSaveDatePattern = "yyMMddhhmmss";
        SimpleDateFormat datFormatForFileSave = new SimpleDateFormat(fileSaveDatePattern);
        String fileExt = datFormatForFileSave.format(date.getTime());
        SimpleDateFormat datePatternFormat = new SimpleDateFormat(datePattern);
        String timePattern = "hh:mm:ss a";
        SimpleDateFormat timePatternFormat = new SimpleDateFormat(timePattern);
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1000, 1400, 1).create();
        PdfDocument.Page myPage = mypdfDocument.startPage(myPageInfo);
        System.out.println("Height:" + myPageInfo.getPageHeight());
        Canvas canvas = myPage.getCanvas();

        myPaint.setTextSize(50);
        canvas.drawText("Gajanan Coldrink House", 30, 80, myPaint);

        myPaint.setTextSize(30);
        canvas.drawText("O/P Municipal Corporation Gadhinglaj, Gadhinglaj", 30, 120, myPaint);

        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Invoice No", canvas.getWidth() - 40, 40, myPaint);
        canvas.drawText(String.valueOf(billNo), canvas.getWidth() - 40, 80, myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);

        canvas.drawRect(30, 150, canvas.getWidth() - 30, 160, myPaint);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Date", 50, 200, myPaint);
        if (isReprint) {
            try {
                Date dd = datePatternFormat.parse(dateandtime);
                canvas.drawText(datePatternFormat.format(dd.getTime()), 250, 200, myPaint);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            canvas.drawText(datePatternFormat.format(date.getTime()), 250, 200, myPaint);
        }

        canvas.drawText("Time", 620, 200, myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        if (isReprint) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                Date dd = sdf.parse(dateandtime);
                canvas.drawText(timePatternFormat.format(dd.getTime()), canvas.getWidth() - 40, 200, myPaint);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            canvas.drawText(timePatternFormat.format(date.getTime()), canvas.getWidth() - 40, 200, myPaint);
        }


        myPaint.setTextAlign(Paint.Align.LEFT);

        myPaint.setColor(Color.rgb(150, 150, 150));
        canvas.drawRect(30, 250, 270, 300, myPaint);

        myPaint.setColor(Color.WHITE);
        canvas.drawText("Bill Description", 50, 285, myPaint);

        myPaint.setColor(Color.BLACK);

        canvas.drawText("Customer Name", 30, 350, myPaint);
        if (isReprint) {
            canvas.drawText(cname.equals("") ? "" : cname, 280, 350, myPaint);
        } else {
            if (validCustName != null && !validCustName.equals("")) {
                canvas.drawText(validCustName, 280, 350, myPaint);
            } else {
//            canvas.drawText("Vendor Name", 30, 350, myPaint);
//            canvas.drawText("Rohit", 280, 350, myPaint);
            }
        }

        myPaint.setColor(Color.rgb(150, 150, 150));
        canvas.drawRect(30, 400, canvas.getWidth() - 30, 450, myPaint);

        myPaint.setColor(Color.WHITE);

        canvas.drawText("Item", 50, 435, myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Qty", 380, 435, myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Rate(Rs.)", 520, 435, myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Amount", canvas.getWidth() - 40, 435, myPaint);
        int height = 480;
//        int width =580;
        int qty = 0;
        height = height + 5;
        for (Map.Entry<String, Item> itr : map.entrySet()) {
            Item obj = itr.getValue();
            myPaint.setTextAlign(Paint.Align.LEFT);
            myPaint.setColor(Color.BLACK);
            canvas.drawText(String.valueOf(itr.getKey()), 50, height, myPaint);//itemname
            myPaint.setTextAlign(Paint.Align.RIGHT);
//            if(itr.getKey()=="Kokam"){
//                qty = itr.getValue()/20;
//            }else{
//                qty = itr.getValue()/15;
//            }
            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(String.valueOf(obj.getQty()), 385, height, myPaint);//qty
            myPaint.setTextAlign(Paint.Align.RIGHT);

            canvas.drawText(String.valueOf(obj.getRate()), 580, height, myPaint);//rate
//            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(obj.getTotal()), canvas.getWidth() - 40, height, myPaint);//total
            myPaint.setTextAlign(Paint.Align.RIGHT);
            height = height + 40;
//            width= width + 100;
        }

        height = height + 40;
        myPaint.setColor(Color.rgb(150, 150, 150));
        int tempHeight = height;
        tempHeight = tempHeight - 50;
        canvas.drawRect(30, tempHeight, canvas.getWidth() - 30, height, myPaint);
//        height = height + 50;
        myPaint.setColor(Color.WHITE);
        canvas.drawText("TotalQty:", 550, height - 15, myPaint);
        canvas.drawText(String.valueOf(map.size()), 950, height - 15, myPaint);
        height = height + 45;
        myPaint.setColor(Color.BLACK);
        canvas.drawText("SubTotal", 550, height, myPaint);

        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        height = height + 40;
        canvas.drawText("Total", 550, height, myPaint);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        height = height - 80;
        myPaint.setTextAlign(Paint.Align.RIGHT);
        height = height + 40;
        canvas.drawText(String.valueOf(finalTotal), 960, height, myPaint);
//        canvas.drawText(String.valueOf((finalTotal * 0) / 100), 970, height, myPaint);

        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        height = height + 40;
        canvas.drawText(String.valueOf("₹ " + ((finalTotal) + (finalTotal * 0) / 100)), 960, height, myPaint);

        height = height + 80;
        myPaint.setTextAlign(Paint.Align.RIGHT);

        Double str = Double.parseDouble(String.valueOf(finalTotal));
        String language = "en";
        String Country = "IN";

        String toWords = convertIntoWords(str, language, Country);
        canvas.drawText("Amt in words: ₹ " + toWords + " only", canvas.getWidth() - 40, height, myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);

        height = height + 100;
        canvas.drawText("Make ALL checks payable to the \"Vendor\"", 30, height, myPaint);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        height = height + 40;
        canvas.drawText("Thank You Visit Again...", 30, height, myPaint);
        mypdfDocument.finishPage(myPage);
        File file;
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/GajananColdrinks");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (isReprint) {
            file = new File(folder, "GajananColdrinks_" + fileExt + "_" + billNo + "_updated.pdf");
        } else {
            file = new File(folder, "GajananColdrinks_" + fileExt + "_" + this.billNo_generated + ".pdf");
        }
        String pdfFilePath = folder.toString();
//                +"/GajananColdrinks_"+ fileExt +"_" + this.billNo_generated + ".pdf";
//        String pdfFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Billing/"+"GajananColdrinks_"+ fileExt +"_" + this.billNo_generated + ".pdf";
        System.out.println(pdfFilePath);
        try {
            mypdfDocument.writeTo(new FileOutputStream(file));
            System.out.println("File Generated Successfully");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Invoice");
            builder.setIcon(R.drawable.ic_invoice);
            builder.setCancelable(false);
            builder.setMessage("Do you want to preview generated Invoice?");
            builder.setPositiveButton("Preview", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        File file1 = null;
                        if (isReprint) {
                            file1 = new File(pdfFilePath, "/GajananColdrinks_" + fileExt + "_" + billNo + "_updated.pdf");
                        } else {
                            file1 = new File(pdfFilePath, "/GajananColdrinks_" + fileExt + "_" + billNo_generated + ".pdf");
                        }

                        Uri uri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file1);
                        } else {
                            uri = Uri.fromFile(file1);
                        }
                        intent.setDataAndType(uri, "application/pdf");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "Open With .."));
                    } catch (Exception ex) {
                        Toast.makeText(context, String.valueOf(ex), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception ex) {
            System.out.println("File unable to Generate");
            ex.printStackTrace();
        }
    }

    private String convertIntoWords(Double str, String language, String Country) {
        Locale local = new Locale(language, Country);
        RuleBasedNumberFormat ruleBasedNumberFormat = new RuleBasedNumberFormat(local, RuleBasedNumberFormat.SPELLOUT);
        return ruleBasedNumberFormat.format(str);
    }

    private void do_resetSeq() {
        SharedPreferences sp = getSharedPreferences("key_code", MODE_PRIVATE);
//        int code = sp.getInt("code",0);
        sp.edit().putInt("code", 0).commit();
        Toast.makeText(this, "Reset done..", Toast.LENGTH_SHORT).show();
    }

    private Long generateSeq() {
        SharedPreferences sp = getSharedPreferences("key_code", MODE_PRIVATE);

        int code = sp.getInt("code",0);

        if (code <= 0) {
            code = 1; //--set default start value--
        } else {
            code++; //--or just increment it--
        }

        sp.edit().putInt("code", code).apply(); //--save new value--
        String num = String.format("%05d", code); // var is "001"
//        String newKey = "13-T0_" + code;
        Long billNo = Long.valueOf(num);
        System.out.println("----------------" + billNo + "----------------");
        return billNo;
    }

//    public void getMap1(Map<String, Item> filteredmap1) {
//        Toast.makeText(context, "gemMap1 called", Toast.LENGTH_SHORT).show();
//        items1.putAll(filteredmap1);
//        for(Map.Entry<String,Item> i : items1.entrySet()){
//            if(filteredmap1.containsKey(i.getKey())){
//                items1.put(i.getKey(),i.getValue());
//                Toast.makeText(context, "Added::::>>"+i.getKey(), Toast.LENGTH_SHORT).show();
//            }else{
//                items1.remove(i.getKey());
//                Toast.makeText(context, "Removed::::>>"+i.getKey(), Toast.LENGTH_SHORT).show();
//            }
//        }
//        Toast.makeText(context, "Total map size11: "+map.size(), Toast.LENGTH_SHORT).show();
//    }

//    public void getMap2(Map<String, Item> filteredMap2) {
//        items2.putAll(filteredMap2);
//        for(Map.Entry<String,Item> i : items2.entrySet()){
//            if(filteredMap2.containsKey(i.getKey())){
//                items2.put(i.getKey(),i.getValue());
//                Toast.makeText(context, "Added::::>>"+i.getKey(), Toast.LENGTH_SHORT).show();
//            }else{
//                items2.remove(i.getKey());
//                Toast.makeText(context, "Removed::::>>"+i.getKey(), Toast.LENGTH_SHORT).show();
//            }
//        }
//        for(Map.Entry<String,Item> i : filteredMap2.entrySet()){
//            if(items2.containsKey(i.getKey())){
//                items2.put(i.getKey(),i.getValue());
//            }else{
//                items2.remove(i.getKey());
//            }
//        }
//        Toast.makeText(context, "Total map size22: "+map.size(), Toast.LENGTH_SHORT).show();
//    }

//    public void getColdTotal(int coldrinkFinalTotal1) {
//        Toast.makeText(context, "getTotal:"+coldrinkFinalTotal1, Toast.LENGTH_SHORT).show();
//        if(coldrinkFinalTotal1 != 0){
//            coldTotal = coldrinkFinalTotal1;
//        }else{
//            coldTotal = 0;
//        }
//    }

//    public void getIceTotal(int IceCreamFinalTotal1) {
//        if(IceCreamFinalTotal1 != 0){
//            iceTotal = IceCreamFinalTotal1;
//        }else{
//            iceTotal = 0;
//        }
//    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}