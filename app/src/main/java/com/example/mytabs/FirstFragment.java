package com.example.mytabs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FirstFragment extends Fragment {
    Long billNo_generated = 0L;
    final int DEF_MAX = 25;
    String vendor_name = "Rohit";
    int coldrinkFinalTotal = 0;
    int total = 0;
    int total1 = 0;
    int orangeQty = 0;
    int orangeTotal = 0;
    int kokamTotal = 0;
    int lemonTotal = 0;
    int sarbatTotal = 0;
    int lSoda = 15;
    int lSodaTotal = 0;
    int ssrbt = 20;
    int ssrbtTotal = 0;
    int pachakTotal = 0;
    int finalTotal = 0;
    int walaTotal = 0;
    int lorangeTotal = 0;
    int lorange = 20;
    int LlemonTotal = 0;
    int orange,lemon,kokam,sarbat,Ssrbt,lsoda,lassi,pachak,wala,water,Lorange,Lemon,Llemon,sSoda;
    int jeeraTotal = 0;
    int sSodaTotal = 0;
    int waterTotal = 0;
    int lassiTotal = 0;
    TextView tcf;
    TextView responseTV;
    Map<String, Item> items = new HashMap<>();
    SeekBar seek;
    SeekBar seek2;
    SeekBar seek3;
    SeekBar seek4;
    SeekBar seek5;
    SeekBar seek6;
    SeekBar seek7;
    SeekBar seek8;
    SeekBar seek9;
    SeekBar seek10;
    SeekBar seek12;
    SeekBar seek11;
    SeekBar seek13;
    SeekBar seek14;
    int otherAmt = 0;
    EditText custName;
    EditText etOther;
    private static final String dbname = "Bill.db";
    Context context;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    int jeera;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Map<String,Integer> itemsBill;
    int custItems=0;
    StringBuilder sbTemp;

    public FirstFragment() {
        // Required empty public constructor
    }
    public void getDetails(){
        Toast.makeText(context, "ggg", Toast.LENGTH_SHORT).show();
    }
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        prefs = getLayoutInflater().getContext().getSharedPreferences("d",MODE_PRIVATE);
        editor = prefs.edit();
        orange = prefs.getInt("orange",15);
        kokam = prefs.getInt("kokam",20);
        lemon = prefs.getInt("lemon",15);
        sarbat = prefs.getInt("sarbat",15);
        pachak = prefs.getInt("pachak",20);
        wala = prefs.getInt("wala",20);
        lSoda = prefs.getInt("lsoda",15);
        ssrbt = prefs.getInt("ssrbt",20);
        lorange = prefs.getInt("lorange",20);
        Llemon = prefs.getInt("llemon",20);
        jeera = prefs.getInt("jsoda",15);
        sSoda = prefs.getInt("ssoda",15);
        water = prefs.getInt("water",10);
        lassi = prefs.getInt("lassi",20);

        Map<String,?> map = prefs.getAll();
        for(Map.Entry<String,?> e : map.entrySet()){
            System.out.println(""+e.getKey()+" "+e.getValue());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        context = getActivity();
        ((MainActivity) getActivity()).f1 = this;

                seek = (SeekBar) view.findViewById(R.id.seekBar);
        custName = view.findViewById(R.id.editTextTextPersonName);
//        Button btn = view.findViewById(R.id.button);
        tcf = view.findViewById(R.id.textView3);
        TextView tprogress = view.findViewById(R.id.textView4);
        seek2 = view.findViewById(R.id.seekBar2);
        TextView tseek2 = view.findViewById(R.id.textView5);
        seek3 = view.findViewById(R.id.seekBar3);
        TextView tseek9 = view.findViewById(R.id.textView9);
        seek5 = view.findViewById(R.id.seekBar5);
        TextView tseek5 = view.findViewById(R.id.textView2);
//        Button btnPostAPI = view.findViewById(R.id.button2);
//        responseTV = view.findViewById(R.id.textView10);
        Button btn3 = view.findViewById(R.id.button3);
        Button reset_seq = view.findViewById(R.id.button5);

        seek4 = view.findViewById(R.id.seekBar4);
        TextView tv12 = view.findViewById(R.id.textView12);

        seek6 = view.findViewById(R.id.seekBar6);
        TextView tv19 = view.findViewById(R.id.textView19);

        seek9 = view.findViewById(R.id.seekBar9);
        TextView tv15 = view.findViewById(R.id.textView15);

        seek7 = view.findViewById(R.id.seekBar7);
        TextView tv14 = view.findViewById(R.id.textView14);

        seek8 = view.findViewById(R.id.seekBar8);
        TextView tv17 = view.findViewById(R.id.textView17);

        seek10 = view.findViewById(R.id.seekBar10);
        TextView tv22 = view.findViewById(R.id.textView22);

        seek12 = view.findViewById(R.id.seekBar12);
        TextView tv23 = view.findViewById(R.id.textView23);

        seek11 = view.findViewById(R.id.seekBar11);
        TextView tv25 = view.findViewById(R.id.textView25);

        seek13 = view.findViewById(R.id.seekBar13);
        TextView tv27 = view.findViewById(R.id.textView27);

        seek14 = view.findViewById(R.id.seekBar14);
        TextView tv29 = view.findViewById(R.id.textView29);

        etOther = view.findViewById(R.id.editTextPhone);

        etOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("Add/Remove Items");
                final View customItems = getLayoutInflater().inflate(R.layout.custom_items,null);
                ImageButton btnAdd = customItems.findViewById(R.id.button7);
                ImageButton btnDel = customItems.findViewById(R.id.button8);
                EditText etAddItemName = customItems.findViewById(R.id.editTextTextPersonName2);
                EditText etAddValue = customItems.findViewById(R.id.editTextNumber2);
                Spinner spinner = customItems.findViewById(R.id.spinner2);
                TextView textView33 = customItems.findViewById(R.id.textView33);
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
                spinner.setSelection(Adapter.NO_SELECTION);
                spinnerAdapter.add("Select Item");
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
//                spinnerAdapter.remove((String) spinner.getSelectedItem());
                itemsBill = new HashMap<>();

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println(spinnerAdapter.getItem(position));
                        for(Map.Entry<String,Integer> m : itemsBill.entrySet()){
                            if(spinner.getSelectedItem().equals(m.getKey())){
                                etAddItemName.setText(String.valueOf(m.getKey()));
                                etAddValue.setText(String.valueOf(m.getValue()));
                                break;
                            }else{
                                etAddItemName.setText(String.valueOf(""));
                                etAddValue.setText(String.valueOf(""));
                            }
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!etAddItemName.getText().toString().isEmpty() && !etAddValue.getText().toString().isEmpty()){
                            String itemName = etAddItemName.getText().toString();
                            Integer itemVal = Integer.parseInt(etAddValue.getText().toString());
                            itemsBill.put(itemName,itemVal);
                            System.out.println(itemsBill.entrySet());
                            spinnerAdapter.clear();
                            for(Map.Entry<String,Integer> m : itemsBill.entrySet()){
                                spinnerAdapter.add(m.getKey());
                            }
                            etAddItemName.setText("");
                            etAddValue.setText("");
                        }
                        StringBuilder sb = new StringBuilder();
                        int z = 0;
                        for(Map.Entry<String,Integer> i : itemsBill.entrySet()){
                            sb.append(i.getKey()+" "+i.getValue()+"\n");
                            z = z + i.getValue();
                        }
                        sb.append("---------\nTotal:"+z);
                        textView33.setText(sb);
                    }
                });

                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String selectedItem = (String)spinner.getSelectedItem();
                        spinnerAdapter.remove(selectedItem);
                        itemsBill.remove(selectedItem);
                        if(itemsBill.containsKey(selectedItem)){
                            itemsBill.remove(selectedItem);
                        }else{
                            etAddItemName.setText("");
                            etAddValue.setText("");
                        }
                        StringBuilder sb = new StringBuilder();
                        int f=0;
                        for(Map.Entry<String,Integer> i : itemsBill.entrySet()){
                            sb.append(i.getKey()+"->"+i.getValue()+"\n");
                            f = f + i.getValue();
                        }
                        sb.append("---------\nTotal:"+f);
                        textView33.setText(sb);
                    }
                });
                builder.setView(customItems);
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        custItems = 0;
                        for(Map.Entry<String,Integer> m : itemsBill.entrySet()){
                            if(itemsBill.size()>0){
                                custItems = custItems + m.getValue();
                            }else{
                                custItems = 0;
                            }
                        }
                        System.out.println(custItems);
                        if(itemsBill.size()>0){
                            sbTemp = new StringBuilder();
                            sbTemp.append("[");
                            for(Map.Entry<String,Integer> i : itemsBill.entrySet()){
                                sbTemp.append(i.getKey()+"="+i.getValue()+" ");
                            }
                            sbTemp.append("]");
                        }
                        popUpTotal(custItems);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemsBill.clear();
                        etOther.setText("");
                        spinnerAdapter.clear();
                        etAddItemName.setText("");
                        etAddValue.setText("");
                    }
                });


                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        etOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!etOther.getText().toString().isEmpty() && !etOther.getText().toString().equals("0")){
                    otherAmt = Integer.parseInt(String.valueOf(etOther.getText().toString()));
                    refreshTotal();
                }else{
                    otherAmt = 0;
                    refreshTotal();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        tprogress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Orange Current Rate:"+orange);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("orange",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Orange rate updated:"+prefs.getInt("orange",15), Toast.LENGTH_SHORT).show();
                            seek.setProgress(0);
                        }else{
                            orange = prefs.getInt("orange",15);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        reset_seq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_resetSeq();
//                List<Bill> list = getAllData();
//                for(Bill itr : list){
//                    System.out.println(itr.getBillno()+" "+itr.getOrange()+" "+itr.getKokam()+" "+itr.getLemon()+" "+itr.getSarbat()+"|"+itr.getTotal());
//                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbManager db = new DbManager(getActivity().getApplicationContext());
                if (orangeTotal == 0 && kokamTotal == 0 && lemonTotal == 0 && sarbatTotal == 0 && pachakTotal == 0 && walaTotal == 0 && lSodaTotal == 0 && ssrbtTotal == 0 && lorangeTotal == 0 && LlemonTotal == 0 && jeeraTotal ==0 && sSodaTotal == 0 && waterTotal == 0 && lassiTotal == 0) {
                    if(etOther.getText().toString().isEmpty() || etOther.getText().toString().equals("0")){
                        Toast.makeText(getActivity().getApplicationContext(), "Please choose QTY", Toast.LENGTH_SHORT).show();
                    }
                    if(orangeTotal == 0 ){
                        items.remove("orange");
                    }
                    if (kokamTotal == 0) {
                        items.remove("kokam");
                    }
                    if (lemonTotal == 0) {
                        items.remove("lemon");
                    }
                    if (sarbatTotal == 0) {
                        items.remove("sarbat");
                    }
                    if (pachakTotal == 0) {
                        items.remove("pachak");
                    }
                    if (walaTotal == 0) {
                        items.remove("wala");
                    }
                    if (lSodaTotal == 0) {
                        items.remove("lsoda");
                    }
                    if (ssrbtTotal == 0) {
                        items.remove("ssrbt");
                    }
                    if (lorangeTotal == 0) {
                        items.remove("lorange");
                    }
                    if (LlemonTotal == 0) {
                        items.remove("llemon");
                    }
                    if(jeeraTotal == 0 ){
                        items.remove("jsoda");
                    }
                    if(sSodaTotal == 0 ){
                        items.remove("sSoda");
                    }
                    if(waterTotal == 0 ){
                        items.remove("water");
                    }
                    if(lassiTotal == 0 ){
                        items.remove("lassi");
                    }
//                    finalTotal=0;
//                    coldrinkFinalTotal=0;
//                    items.clear();
                    if(!etOther.getText().toString().isEmpty() && !etOther.getText().toString().equals("0")){
                        otherAmt = Integer.valueOf(etOther.getText().toString());
                        items.put("other",new Item(0,0,otherAmt));
//                        finalTotal = finalTotal + otherAmt;
                        coldrinkFinalTotal = finalTotal;
                        Toast.makeText(getContext(), "Items added:\n"+items.keySet(), Toast.LENGTH_SHORT).show();
                        System.out.println("coldrinkFinalTotal:>>>>>>>>>>>"+coldrinkFinalTotal);
                    }else{
                        items.remove("other");
//                        finalTotal = finalTotal - otherAmt;
                        coldrinkFinalTotal = finalTotal;
                        System.out.println("coldrinkFinalTotal:>>>>>>>>>>>"+coldrinkFinalTotal);

                    }
//                    MainActivity m = (MainActivity) getActivity();
//                    m.items1.clear();
//                    m.coldrinkFinalTotal=0;
//                    m.coldTotal=0;
                } else {
//                    billNo_generated = generateSeq();
//                    Date now = new Date();
//                    long timestamp = now.getTime();
//                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy  hh:mm:ss a");
//                    String dateStr = sdf.format(timestamp);
//                    tcf.setText("Orange: "+orangeTotal+"\nKokam: "+kokamTotal+"\nLemon: "+lemonTotal+"\nSarbat: "+sarbatTotal+"\n----------------\nTotal: "+finalTotal+"\nDate & Time: "+dateStr);
                    Item orangeObj = null;
                    if (orangeTotal != 0) {
                        orangeObj = new Item(orange, orangeTotal / orange, orangeTotal);
                        items.put("orange", orangeObj);
                    } else {
                        items.remove("orange");
                    }
                    Item kokamObj = null;
                    if (kokamTotal != 0) {
                        kokamObj = new Item(kokam, kokamTotal / kokam, kokamTotal);
                        items.put("kokam", kokamObj);
                    } else {
                        items.remove("kokam");
                    }
                    Item lemonObj = null;
                    if (lemonTotal != 0) {
                        lemonObj = new Item(lemon, lemonTotal / lemon, lemonTotal);
                        items.put("lemon", lemonObj);
                    } else {
                        items.remove("lemon");
                    }
                    Item sarbatObj = null;
                    if (sarbatTotal != 0) {
                        sarbatObj = new Item(sarbat, sarbatTotal / sarbat, sarbatTotal);
                        items.put("sarbat", sarbatObj);
                    } else {
                        items.remove("sarbat");
                    }
                    Item pachakObj = null;
                    if (pachakTotal != 0) {
                        pachakObj = new Item(pachak, pachakTotal / pachak, pachakTotal);
                        items.put("pachak", pachakObj);
                    } else {
                        items.remove("pachak");
                    }
                    Item walaObj = null;
                    if (walaTotal != 0) {
                        walaObj = new Item(wala, walaTotal / wala, walaTotal);
                        items.put("wala", walaObj);
                    } else {
                        items.remove("wala");
                    }
                    Item lsodaObj = null;
                    if (lSodaTotal != 0) {
                        lsodaObj = new Item(lSoda, lSodaTotal / lSoda, lSodaTotal);
                        items.put("lsoda", lsodaObj);
                    } else {
                        items.remove("lsoda");
                    }
                    Item ssrbtObj = null;
                    if (ssrbtTotal != 0) {
                        ssrbtObj = new Item(ssrbt, ssrbtTotal / ssrbt, ssrbtTotal);
                        items.put("ssrbt", ssrbtObj);
                    } else {
                        items.remove("ssrbt");
                    }
                    Item lorangeObj = null;
                    if (lorangeTotal != 0) {
                        lorangeObj = new Item(lorange, lorangeTotal / lorange, lorangeTotal);
                        items.put("lorange", lorangeObj);
                    } else {
                        items.remove("lorange");
                    }
                    Item llemonObj = null;
                    if (LlemonTotal != 0) {
                        llemonObj = new Item(Llemon, LlemonTotal / Llemon, LlemonTotal);
                        items.put("llemon", llemonObj);
                    } else {
                        items.remove("llemon");
                    }
                    Item jsodaObj = null;
                    if (jeeraTotal != 0) {
                        jsodaObj = new Item(jeera, jeeraTotal / jeera, jeeraTotal);
                        items.put("jsoda", jsodaObj);
                    } else {
                        items.remove("jsoda");
                    }
                    Item sSodaObj = null;
                    if (sSodaTotal != 0) {
                        sSodaObj = new Item(sSoda, sSodaTotal / sSoda, sSodaTotal);
                        items.put("sSoda", sSodaObj);
                    } else {
                        items.remove("sSoda");
                    }
                    Item waterObj = null;
                    if (waterTotal != 0) {
                        waterObj = new Item(water, waterTotal / water, waterTotal);
                        items.put("water", waterObj);
                    } else {
                        items.remove("water");
                    }
                    Item lassiObj = null;
                    if (lassiTotal != 0) {
                        lassiObj = new Item(lassi, lassiTotal / lassi, lassiTotal);
                        items.put("lassi", lassiObj);
                    } else {
                        items.remove("lassi");
                    }
                    if (finalTotal != 0) {
                        if(!etOther.getText().toString().isEmpty() && !etOther.getText().equals("0")){
                            otherAmt = Integer.valueOf(String.valueOf(etOther.getText()));
//                            coldrinkFinalTotal = finalTotal + otherAmt;
                            items.put("other",new Item(0,0,otherAmt));
                            coldrinkFinalTotal = finalTotal;
                            System.out.println("coldrinkFinalTotal:>>>>>"+coldrinkFinalTotal);
                        }else{
                            coldrinkFinalTotal = finalTotal;
                            items.remove("other");
                            System.out.println("coldrinkFinalTotal:>>>>"+coldrinkFinalTotal);
                        }
                    }else{
                        if(etOther.getText().toString().isEmpty() || etOther.getText().equals("0")){
                            items.remove("other");
                            coldrinkFinalTotal = finalTotal - otherAmt;
                        }
                    }
                    for (Map.Entry<String, Item> itr : items.entrySet()) {
                        Item t = itr.getValue();
                        System.out.println("\n" + itr.getKey() + "--" + t.getTotal());
                    }
                    Toast.makeText(getActivity().getApplicationContext(), "Items added:\n"+items.keySet(), Toast.LENGTH_LONG).show();
//                    String res = db.addRecord(billNo_generated,orangeTotal,kokamTotal,lemonTotal,sarbatTotal,pachakTotal,walaTotal,lSodaTotal,ssrbtTotal,lorangeTotal,LlemonTotal,finalTotal,dateStr);
                    //generate pdf invoice
//                    printInvoice(billNo_generated,orange,kokam,lemon,sarbat,pachak,wala,lsoda,ssrbt,lorange,llemon,finalTotal);
//                    Toast.makeText(MainActivity.this, res, Toast.LENGTH_SHORT).show();
                }
//                MainActivity m = (MainActivity) getActivity();
//                m.getMap1(items);
//                m.getColdTotal(coldrinkFinalTotal);
//                seek.setProgress(0);
//                seek2.setProgress(0);
//                seek3.setProgress(0);
//                seek4.setProgress(0);
//                seek5.setProgress(0);
//                seek6.setProgress(0);
//                seek7.setProgress(0);
//                seek8.setProgress(0);
//                seek9.setProgress(0);
//                seek10.setProgress(0);

            }
        });

        tv27.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Water Current Rate:"+water);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("water",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Water rate updated:"+prefs.getInt("water",10), Toast.LENGTH_SHORT).show();
                            seek13.setProgress(0);
                        }else{
                            water = prefs.getInt("water",10);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek13.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek13.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        tv29.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Lassi Current Rate:"+lassi);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("lassi",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Lassi rate updated:"+prefs.getInt("lassi",20), Toast.LENGTH_SHORT).show();
                            seek14.setProgress(0);

                        }else{
                            lassi = prefs.getInt("lassi",20);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek14.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek14.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        tv25.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Strwbry Soda:"+sSoda);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("ssoda",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "StrwBerry Soda rate updated:"+prefs.getInt("ssoda",15), Toast.LENGTH_SHORT).show();
                            seek11.setProgress(0);
                        }else{
                            sSoda = prefs.getInt("ssoda",15);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek11.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek11.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        tv23.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Jeera Current Rate:"+jeera);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("jsoda",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Jeera Soda rate updated:"+prefs.getInt("jsoda",15), Toast.LENGTH_SHORT).show();
                            seek12.setProgress(0);

                        }else{
                            jeera = prefs.getInt("jsoda",15);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek12.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek12.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        tv22.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Limbu Lemon curr rate:"+Llemon);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("llemon",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Limbu Lemon rate updated:"+prefs.getInt("llemon",20), Toast.LENGTH_SHORT).show();
                            seek10.setProgress(0);
                        }else{
                            Llemon = prefs.getInt("llemon",20);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek10.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek10.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        seek13.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv27.setText(String.valueOf(seekBar.getProgress()));
                water = prefs.getInt("water",10);
                waterTotal = Integer.parseInt(tv27.getText().toString()) * water;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek14.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv29.setText(String.valueOf(seekBar.getProgress()));
                lassi = prefs.getInt("lassi",20);
                lassiTotal = Integer.parseInt(tv29.getText().toString()) * lassi;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek11.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv25.setText(String.valueOf(seekBar.getProgress()));
                sSoda = prefs.getInt("ssoda",15);
                sSodaTotal = Integer.parseInt(tv25.getText().toString()) * sSoda;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek12.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv23.setText(String.valueOf(seekBar.getProgress()));
                jeera = prefs.getInt("jsoda",15);
                jeeraTotal = Integer.parseInt(tv23.getText().toString()) * jeera;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek10.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv22.setText(String.valueOf(seekBar.getProgress()));
                Llemon = prefs.getInt("llemon",20);
                LlemonTotal = Integer.parseInt(tv22.getText().toString()) * Llemon;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        tv17.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Limbu Orange Curr Rate:"+lorange);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("lorange",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Limbu orange rate updated:"+prefs.getInt("lorange",20), Toast.LENGTH_SHORT).show();
                            seek8.setProgress(0);
                        }else{
                            lorange = prefs.getInt("lorange",20);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek8.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek8.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        seek8.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv17.setText(String.valueOf(seekBar.getProgress()));
                lorange = prefs.getInt("lorange",20);
                lorangeTotal = Integer.parseInt(tv17.getText().toString()) * lorange;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tv14.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Soda Sarbat curr Rate:"+ssrbt);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("ssrbt",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Soda Srbt rate updated:"+prefs.getInt("ssrbt",20), Toast.LENGTH_SHORT).show();
                            seek7.setProgress(0);

                        }else{
                            ssrbt = prefs.getInt("ssrbt",20);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek7.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek7.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        seek7.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv14.setText(String.valueOf(seekBar.getProgress()));
                ssrbt = prefs.getInt("ssrbt",20);
                ssrbtTotal = Integer.parseInt(tv14.getText().toString()) * ssrbt;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tv15.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Limbu Soda Curr Rate:"+lSoda);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("lSoda",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Limbu Soda rate updated:"+prefs.getInt("lSoda",15), Toast.LENGTH_SHORT).show();
                            seek9.setProgress(0);
                        }else{
                            lSoda = prefs.getInt("lSoda",15);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek9.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek9.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        seek9.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv15.setText(String.valueOf(seekBar.getProgress()));
                lSoda = prefs.getInt("lSoda",15);
                lSodaTotal = Integer.parseInt(tv15.getText().toString()) * lSoda;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tv19.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Wala Current Rate:"+wala);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("wala",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Wala rate updated:"+prefs.getInt("wala",15), Toast.LENGTH_SHORT).show();
                            seek6.setProgress(0);

                        }else{
                            wala = prefs.getInt("wala",15);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek6.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek6.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        seek6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv19.setText(String.valueOf(seekBar.getProgress()));
                wala = prefs.getInt("wala",15);
                walaTotal = Integer.parseInt(tv19.getText().toString()) * wala;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tv12.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Pachak Curr Rate:"+pachak);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("pachak",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Pachak rate updated:"+prefs.getInt("pachak",20), Toast.LENGTH_SHORT).show();
                            seek4.setProgress(0);
                        }else{
                            pachak = prefs.getInt("pachak",20);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek4.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek4.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        seek4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv12.setText(String.valueOf(seekBar.getProgress()));
                pachak = prefs.getInt("pachak",20);
                pachakTotal = Integer.parseInt(tv12.getText().toString()) * pachak;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tseek5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Sarbat Curr Rate:"+sarbat);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("sarbat",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Sarbat rate updated:"+prefs.getInt("sarbat",15), Toast.LENGTH_SHORT).show();
                            seek5.setProgress(0);
                        }else{
                            sarbat = prefs.getInt("sarbat",15);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek5.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek5.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        seek5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                tseek5.setText(String.valueOf(seekBar.getProgress()));
                sarbat = prefs.getInt("sarbat",15);
                sarbatTotal = Integer.parseInt(tseek5.getText().toString()) * sarbat;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tseek9.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Lemon Current Rate:"+lemon);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("lemon",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Lemon rate updated:"+prefs.getInt("lemon",15), Toast.LENGTH_SHORT).show();
                            seek3.setProgress(0);
                        }else{
                            lemon = prefs.getInt("lemon",15);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek3.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek3.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        seek3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tseek9.setText(String.valueOf(seekBar.getProgress()));
                lemon = prefs.getInt("lemon",15);
                lemonTotal = Integer.parseInt(tseek9.getText().toString()) * lemon;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tseek2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Kokam\t Current Rate:"+kokam);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("kokam",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Kokam rate updated:"+prefs.getInt("kokam",20), Toast.LENGTH_SHORT).show();
                            seek2.setProgress(0);
                        }else{
                            kokam = prefs.getInt("kokam",20);
                        }
                        if(!et1.getText().toString().equals("")){
                            seek2.setMax(Integer.parseInt(et1.getText().toString()));
                        }else{
                            seek2.setMax(DEF_MAX);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        seek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tseek2.setText(String.valueOf(seekBar.getProgress()));
                kokam = prefs.getInt("kokam",20);
                kokamTotal = Integer.parseInt(tseek2.getText().toString()) * kokam;
//                tcf.setText("Total: " + refreshTotal());
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Toast.makeText(getApplicationContext(), ""+seekBar.getProgress()+" progress:"+progress, Toast.LENGTH_SHORT).show();

                if (seekBar.isEnabled() == false) {
                    tprogress.setText(String.valueOf(0));
                }
                orangeQty = seekBar.getProgress();
                tprogress.setText(String.valueOf(seekBar.getProgress()));
                orange = prefs.getInt("orange",15);
                orangeTotal = Integer.parseInt(tprogress.getText().toString()) * orange;
                refreshTotal();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    private void popUpTotal(int custItems) {
        if(custItems != 0){
            etOther.setText(String.valueOf(custItems));
            System.out.println(itemsBill.entrySet());
            etOther.clearFocus();
        }else{
            etOther.setText(String.valueOf(""));
            etOther.clearFocus();
        }
    }

    private void do_resetSeq() {
        SharedPreferences sp = getActivity().getSharedPreferences("key_code", MODE_PRIVATE);
//        int code = sp.getInt("code",0);
        sp.edit().putInt("code", 0).commit();
//        Toast.makeText(this, "Reset done:"+code, Toast.LENGTH_SHORT).show();
    }

    private void refreshTotal() {
        finalTotal = 0;
        if (sSodaTotal != 0) {
            finalTotal += sSodaTotal;
        } else {
            finalTotal -= sSodaTotal;
        }
        if (jeeraTotal != 0) {
            finalTotal += jeeraTotal;
        } else {
            finalTotal -= jeeraTotal;
        }
        if (LlemonTotal != 0) {
            finalTotal += LlemonTotal;
        } else {
            finalTotal -= LlemonTotal;
        }
        if (lorangeTotal != 0) {
            finalTotal += lorangeTotal;
        } else {
            finalTotal -= lorangeTotal;
        }
        if (walaTotal != 0) {
            finalTotal += walaTotal;
        } else {
            finalTotal -= walaTotal;
        }
        if (ssrbtTotal != 0) {
            finalTotal += ssrbtTotal;
        } else {
            finalTotal -= ssrbtTotal;
        }
        if (lSodaTotal != 0) {
            finalTotal += lSodaTotal;
        } else {
            finalTotal -= lSodaTotal;
        }
        if (pachakTotal != 0) {
            finalTotal += pachakTotal;
        } else {
            finalTotal -= pachakTotal;
        }
        if (orangeTotal != 0) {
            finalTotal += orangeTotal;
        } else {
            finalTotal -= orangeTotal;
        }
        if (kokamTotal != 0) {
            finalTotal += kokamTotal;
        } else {
            finalTotal -= kokamTotal;
        }
        if (lemonTotal != 0) {
            finalTotal += lemonTotal;
        } else {
            finalTotal -= lemonTotal;
        }
        if (sarbatTotal != 0) {
            finalTotal += sarbatTotal;
        } else {
            finalTotal -= sarbatTotal;
        }
        if (waterTotal != 0) {
            finalTotal += waterTotal;
        } else {
            finalTotal -= waterTotal;
        }
        if (lassiTotal != 0) {
            finalTotal += lassiTotal;
        } else {
            finalTotal -= lassiTotal;
        }
        if (otherAmt != 0) {
            finalTotal += otherAmt;
        } else {
            finalTotal -= otherAmt;
        }
        Date now = new Date();
        long timestamp = now.getTime();
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("MM/dd/yyyy  hh:mm:ss a");
        }
        String dateStr = sdf.format(timestamp);
        tcf.setText("Orange: " + orangeTotal + "\nKokam: " + kokamTotal + "\nLemon: " + lemonTotal + "\nSarbat: " + sarbatTotal + "\nPachak:" + pachakTotal + "\nWala:" + walaTotal + "\nLsoda:" + lSodaTotal + "\nSsrbt:" + ssrbtTotal + "\nLorange:" + lorangeTotal + "\nLlemon:" + LlemonTotal + "\nJsoda:"+jeeraTotal+"\nsSoda:" + sSodaTotal + "\nWater:"+waterTotal+"\nLassi:"+lassiTotal+"\nOther:"+otherAmt+"\n----------------\nTotal: " + finalTotal + "\nDate & Time: " + dateStr);
    }


    public void resetAllseeks1() {
        finalTotal=0;
        coldrinkFinalTotal=0;
        if(seek11.getProgress() != 0){
            seek11.setProgress(0);
        }
        if(seek12.getProgress() != 0){
            seek12.setProgress(0);
        }
        if(seek13.getProgress() != 0){
            seek13.setProgress(0);
        }
        if(seek14.getProgress() != 0){
            seek14.setProgress(0);
        }
        if(seek.getProgress() != 0){
            seek.setProgress(0);
        }
        if(seek2.getProgress() != 0){
            seek2.setProgress(0);
        }
        if(seek3.getProgress() != 0){
            seek3.setProgress(0);
        }
        if(seek4.getProgress() != 0){
            seek4.setProgress(0);
        }
        if(seek5.getProgress() != 0){
            seek5.setProgress(0);
        }
        if(seek6.getProgress() != 0){
            seek6.setProgress(0);
        }
        if(seek7.getProgress() != 0){
            seek7.setProgress(0);
        }
        if(seek8.getProgress() != 0){
            seek8.setProgress(0);
        }
        if(seek9.getProgress() != 0){
            seek9.setProgress(0);
        }
        if(seek10.getProgress() != 0){
            seek10.setProgress(0);
        }
    }

    public void makeClear1() {
        Toast.makeText(context, "make 1", Toast.LENGTH_SHORT).show();
        coldrinkFinalTotal=0;
        custName.setText("");
        finalTotal = 0;
        if (!items.isEmpty()) {
            resetAllseeks1();
        }
        coldrinkFinalTotal = 0;
        items.clear();
    }
}