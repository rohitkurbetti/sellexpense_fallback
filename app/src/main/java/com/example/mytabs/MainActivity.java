package com.example.mytabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytabs.ui.main.SectionsPagerAdapter;
import com.ibm.icu.text.RuleBasedNumberFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MyResultReceiver{
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                do_resetSeq();
                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                final View customView = getLayoutInflater().inflate(R.layout.custom_table, null);
                boolean isDarkThemeOn = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)  == Configuration.UI_MODE_NIGHT_YES;
                try{
                    String str = f1.custName.getText().toString();
                    if(!str.equals("")||str != null){
//                        isValid = Pattern.compile("^[a-zA-Z0-9 _@#$&*.]{2,}$").matcher(str).matches();
                        isValid = true;
                        if(isValid){
                            validCustName = str;
                        }else if(!str.equals("")){
                            Toast.makeText(context, "Only Alphanumeric characters allowed", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        validCustName = "";
                    }
                }catch (Exception ex){
                    System.out.println(ex);
                }
                DbManager db = new DbManager(getApplicationContext());
//                map.putAll(items1);
//                map.putAll(items2);
                for(Map.Entry<String,Item> itr : map.entrySet()){
                    System.out.println(itr.getKey()+"   ::   "+itr.getValue().getTotal());
                }
//                System.out.println("1st Map size:" + f1.items.size());
//                System.out.println("2nd map size:" + f2.items2.size());
                map.putAll(f1.items);
                map.putAll(f2.items2);
                System.out.println("map sizeeeee:"+map.size());
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
                    if(isDarkThemeOn){
                        tb1.setTextColor(Color.WHITE);
                        tb2.setTextColor(Color.WHITE);
                        tb3.setTextColor(Color.WHITE);
                        tb4.setTextColor(Color.WHITE);
                        tv1.setTextColor(Color.WHITE);
                        tv2.setTextColor(Color.WHITE);
                        tv3.setTextColor(Color.WHITE);
                        tv4.setTextColor(Color.WHITE);
                    }else{
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
                tvFinal.setText("\nTotal: "+total);
                tvFinal.setPadding(10,10,40,10);
                tvFinal.setGravity(Gravity.END);
                if(isDarkThemeOn){
                    tvFinal.setTextColor(Color.WHITE);
                }else{
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
                            Toast.makeText(getApplicationContext(), "Billing", Toast.LENGTH_SHORT).show();
                            masterFinal = f1.coldrinkFinalTotal + f2.IceCreamFinalTotal;
//                            Toast.makeText(MainActivity.this, "FinalTotal:"+masterFinal, Toast.LENGTH_SHORT).show();
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
                            String res = db.addRecord(billNo_generated,validCustName, f1.orangeTotal, f1.kokamTotal, f1.lemonTotal, f1.sarbatTotal, f1.pachakTotal, f1.walaTotal, f1.lSodaTotal, f1.ssrbtTotal, f1.lorangeTotal, f1.LlemonTotal,f1.jeeraTotal, f1.sSodaTotal ,f1.waterTotal ,f1.lassiTotal ,f2.vanillaTotal, f2.pistaTotal, f2.sbryTotal, f2.mangoTotal, f2.btrSchTotal,f2.kulfiTotal,f2.cbarTotal,f2.fpackTotal,f1.otherAmt,f2.otherAmt1, masterFinal, dateStr,currDate);
                            printInvoice(billNo_generated, map, masterFinal,null,null);
                            Toast.makeText(getApplicationContext(), "Bill generated successfully", Toast.LENGTH_SHORT).show();
                            System.out.println("=====================data is going to be print======================");
                            System.out.println("BillNo: " + billNo_generated);
                            System.out.println("Time: " + dateStr);
                            System.out.println("FinalTotal:" + masterFinal);
                            f1.coldrinkFinalTotal=0;
                            f2.IceCreamFinalTotal=0;
                            f1.finalTotal=0;
                            f2.finalTotal=0;
                            f1.otherAmt=0;
                            f2.otherAmt1=0;
                            validCustName="";
                            clearAll();
                            map.clear();
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
                            validCustName="";
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

                    if(isValid){
                        sb = "Customer Name: "+validCustName;
                    }else {
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void printInvoice(Long billNo, Map<String, Item> map, Integer finalTotal,String dateandtime,String cname) {
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("d",MODE_PRIVATE);
        editor = prefs.edit();
        boolean isReprint = prefs.getBoolean("isReprint",false);
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
        if(isReprint){
            try {
                Date dd = datePatternFormat.parse(dateandtime);
                canvas.drawText(datePatternFormat.format(dd.getTime()), 250, 200, myPaint);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            canvas.drawText(datePatternFormat.format(date.getTime()), 250, 200, myPaint);
        }

        canvas.drawText("Time", 620, 200, myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        if(isReprint){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                Date dd = sdf.parse(dateandtime);
                canvas.drawText(timePatternFormat.format(dd.getTime()), canvas.getWidth() - 40, 200, myPaint);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            canvas.drawText(timePatternFormat.format(date.getTime()), canvas.getWidth() - 40, 200, myPaint);
        }


        myPaint.setTextAlign(Paint.Align.LEFT);

        myPaint.setColor(Color.rgb(150, 150, 150));
        canvas.drawRect(30, 250, 270, 300, myPaint);

        myPaint.setColor(Color.WHITE);
        canvas.drawText("Bill Description", 50, 285, myPaint);

        myPaint.setColor(Color.BLACK);

        canvas.drawText("Customer Name", 30, 350, myPaint);
        if(isReprint){
            canvas.drawText(cname.equals("")?"":cname, 280, 350, myPaint);
        }else{
            if(validCustName != null && !validCustName.equals("")) {
                canvas.drawText(validCustName, 280, 350, myPaint);
            }else {
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
        canvas.drawText("TotalQty:" + map.size(), 380, 435, myPaint);
        height = height + 50;
        myPaint.setColor(Color.BLACK);
        canvas.drawText("SubTotal", 550, height, myPaint);
        height = height + 40;
        canvas.drawText("Tax 0%", 550, height, myPaint);

        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        height = height + 40;
        canvas.drawText("Total", 550, height, myPaint);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        height = height - 80;
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(finalTotal), 970, height, myPaint);
        height = height + 40;
        canvas.drawText(String.valueOf((finalTotal * 0) / 100), 970, height, myPaint);

        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        height = height + 40;
        canvas.drawText(String.valueOf("₹ " + ((finalTotal) + (finalTotal * 0) / 100)), 970, height, myPaint);

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
        if(isReprint){
            file = new File(getExternalFilesDir("/"), "GajananColdrinks_" + fileExt + "_" + billNo + "_updated.pdf");
        }else{
            file = new File(getExternalFilesDir("/"), "GajananColdrinks_" + fileExt + "_" + this.billNo_generated + ".pdf");
        }
        String pdfFilePath = getExternalFilesDir("/").getAbsolutePath();
//                +"/GajananColdrinks_"+ fileExt +"_" + this.billNo_generated + ".pdf";
//        String pdfFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Billing/"+"GajananColdrinks_"+ fileExt +"_" + this.billNo_generated + ".pdf";
        System.out.println(pdfFilePath);
        try {
            mypdfDocument.writeTo(new FileOutputStream(file));
            System.out.println("File Generated Successfully");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Invoice");
            builder.setIcon(R.drawable.ic_invoice);
            builder.setMessage("Do you want to preview generated Invoice?");
            builder.setPositiveButton("Preview", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        File file1 = null;
                        if(isReprint){
                            file1 = new File(pdfFilePath,"/GajananColdrinks_"+fileExt+"_"+billNo+"_updated.pdf");
                        }else{
                            file1 = new File(pdfFilePath,"/GajananColdrinks_"+fileExt+"_"+billNo_generated+".pdf");
                        }

                        Uri uri;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            uri = FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID+".provider",file1);
                        }else{
                            uri = Uri.fromFile(file1);
                        }
                        intent.setDataAndType(uri,"application/pdf");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent,"Open With .."));
                    }catch (Exception ex){
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
        int code = sp.getInt("code", 0);

        if (code <= 0) {
            code = 1; //--set default start value--
        } else {
            code++; //--or just increment it--
        }

        sp.edit().putInt("code", code).commit(); //--save new value--
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
    public String getResult() {
        return "Got the interface method";
    }


//    public void getMap(Map<String, Item> filteredmap1) {
//        map.putAll(filteredmap1);
//        Toast.makeText(context, "size1>>"+map.size(), Toast.LENGTH_SHORT).show();
//    }
//
//    public void getMap2(Map<String, Item> filteredMap2) {
//        map.putAll(filteredMap2);
//        Toast.makeText(context, "size2>>"+map.size(), Toast.LENGTH_SHORT).show();
//    }

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