package com.example.mytabs;

import static android.content.Context.MODE_PRIVATE;

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

public class SecondFragment extends Fragment {
    StringBuilder sbTemp1;
    Long billNo_generated = 0L;
    final int DEF_MAX = 25;
    int finalTotal = 0;
    int IceCreamFinalTotal = 0;
    String vendor_name = "Rohit";
    int vanilla,pista,sbry,mango,kulfi,cbar,btrSch,fpack;
    int vanillaTotal = 0;
    int pistaTotal = 0;
    int sbryTotal = 0;
    int mangoTotal = 0;
    int btrSchTotal = 0;
    int kulfiTotal = 0;
    int cbarTotal = 0;
    int otherAmt1 = 0;
    int fpackTotal = 0;
    TextView tcf;
    TextView responseTV;
    EditText etOther1;
    Map<String, Item> items2 = new HashMap<>();
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
    private static final String dbname = "Bill.db";
    Context context;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    HashMap<String, Integer> itemsBill2;
    int custItems1;

    public SecondFragment() {
        // Required empty public constructor
    }
    public void getDetails(){
        Toast.makeText(context, "ggasdasdg", Toast.LENGTH_SHORT).show();
    }
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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
        vanilla = prefs.getInt("vanilla",10);
        mango = prefs.getInt("mango",20);
        cbar = prefs.getInt("cbar",10);
        sbry = prefs.getInt("sbry",10);
        btrSch = prefs.getInt("btrsch",20);
        kulfi = prefs.getInt("kulfi",10);
        pista = prefs.getInt("pista",15);
        fpack = prefs.getInt("fpack",100);
//        ssrbt = prefs.getInt("ssrbt",20);
//        lorange = prefs.getInt("lorange",20);
//        Llemon = prefs.getInt("llemon",20);
//        jeera = prefs.getInt("jsoda",15);
//        sSoda = prefs.getInt("ssoda",15);
//        water = prefs.getInt("water",10);
//        lassi = prefs.getInt("lassi",20);

        Map<String,?> map = prefs.getAll();
        for(Map.Entry<String,?> e : map.entrySet()){
            System.out.println(""+e.getKey()+" "+e.getValue());
        }
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        context = getActivity();
        ((MainActivity)getActivity()).f2 = this;
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        seek = (SeekBar) view.findViewById(R.id.seekBar);
        TextView tprogress = view.findViewById(R.id.textView4);

//        Button btn = view.findViewById(R.id.button);
        tcf = view.findViewById(R.id.textView3);

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

        etOther1 = view.findViewById(R.id.editTextPhone2);

        etOther1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add/Remove Items");
                builder.setCancelable(false);
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
                itemsBill2 = new HashMap<>();

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println(spinnerAdapter.getItem(position));
                        for(Map.Entry<String,Integer> m : itemsBill2.entrySet()){
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
                            itemsBill2.put(itemName,itemVal);
                            System.out.println(itemsBill2.entrySet());
                            spinnerAdapter.clear();
                            for(Map.Entry<String,Integer> m : itemsBill2.entrySet()){
                                spinnerAdapter.add(m.getKey());
                            }
                            etAddItemName.setText("");
                            etAddValue.setText("");
                        }
                        StringBuilder sb = new StringBuilder();
                        int z = 0;
                        for(Map.Entry<String,Integer> i : itemsBill2.entrySet()){
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
                        itemsBill2.remove(selectedItem);
                        if(itemsBill2.containsKey(selectedItem)){
                            itemsBill2.remove(selectedItem);
                        }else{
                            etAddItemName.setText("");
                            etAddValue.setText("");
                        }
                        StringBuilder sb = new StringBuilder();
                        int f=0;
                        for(Map.Entry<String,Integer> i : itemsBill2.entrySet()){
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
                        custItems1 = 0;
                        for(Map.Entry<String,Integer> m : itemsBill2.entrySet()){
                            if(itemsBill2.size()>0){
                                custItems1 = custItems1 + m.getValue();
                            }else{
                                custItems1 = 0;
                            }
                        }
                        System.out.println(custItems1);
                        if(itemsBill2.size()>0){
                            sbTemp1 = new StringBuilder();
                            sbTemp1.append("[");
                            for(Map.Entry<String,Integer> i : itemsBill2.entrySet()){
                                sbTemp1.append(i.getKey()+"="+i.getValue()+" ");
                            }
                            sbTemp1.append("]");
                        }
                        popUpTotal(custItems1);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemsBill2.clear();
                        etOther1.setText("");
                        spinnerAdapter.clear();
                        etAddItemName.setText("");
                        etAddValue.setText("");
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        etOther1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!etOther1.getText().toString().isEmpty() && !etOther1.getText().toString().equals("0")){
                    otherAmt1 = Integer.parseInt(String.valueOf(etOther1.getText().toString()));
                    refreshTotal();
                }else{
                    otherAmt1 = 0;
                    refreshTotal();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DbManager db = new DbManager(getActivity().getApplicationContext());
                Map<String,Item> filteredMap2 = new HashMap<>();
                if (vanillaTotal == 0 && pistaTotal == 0 && sbryTotal == 0 && mangoTotal == 0 && btrSchTotal==0 && kulfiTotal == 0 && cbarTotal == 0 && fpackTotal == 0) {
                    if(etOther1.getText().toString().isEmpty() || etOther1.getText().equals("0")){
                        Toast.makeText(getActivity().getApplicationContext(), "Please choose QTY", Toast.LENGTH_SHORT).show();
                    }
                    if (vanillaTotal == 0) {
                        items2.remove("vanilla");
                    }
                    if (pistaTotal == 0) {
                        items2.remove("pista");
                    }
                    if (sbryTotal == 0) {
                        items2.remove("stwbry");
                    }
                    if (mangoTotal == 0) {
                        items2.remove("mango");
                    }
                    if (btrSchTotal == 0) {
                        items2.remove("btrsch");
                    }
                    if (kulfiTotal == 0) {
                        items2.remove("kulfi");
                    }
                    if (cbarTotal == 0) {
                        items2.remove("cBar");
                    }
                    if (fpackTotal == 0) {
                        items2.remove("fpack");
                    }
//                    if (finalTotal == 0) {
//                        IceCreamFinalTotal = finalTotal;
//                    }
//                    finalTotal=0;
//                    IceCreamFinalTotal=0;
//                    items2.clear();

                    if(!etOther1.getText().toString().isEmpty() && !etOther1.getText().toString().equals("0")){
                        otherAmt1 = Integer.valueOf(etOther1.getText().toString());
                        items2.put("other1",new Item(0,0,otherAmt1));
//                        finalTotal = finalTotal + otherAmt1;
                        IceCreamFinalTotal = finalTotal;
                        Toast.makeText(getContext(), "Items added:\n"+items2.keySet(), Toast.LENGTH_SHORT).show();
                        System.out.println("Icecreamtotal:>>>>>>>>>>>"+IceCreamFinalTotal);
                    }else{
                        items2.remove("other1");
//                        finalTotal = finalTotal - otherAmt1;
                        IceCreamFinalTotal = finalTotal;
                        System.out.println("Icecreamtotal:>>>>>>>>>>>"+IceCreamFinalTotal);
//                        otherAmt1=0;
                    }

//                    MainActivity m = (MainActivity) getActivity();
//                    m.items2.clear();
//                    m.IceCreamFinalTotal=0;
//                    m.iceTotal=0;
                } else {
//                    billNo_generated = generateSeq();
//                    Date now = new Date();
//                    long timestamp = now.getTime();
//                    SimpleDateFormat sdf = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                        sdf = new SimpleDateFormat("MM/dd/yyyy  hh:mm:ss a");
//                    }
//                    String dateStr = sdf.format(timestamp);
//                    tcf.setText("Vanilla: "+vanillaTotal+"\nPista: "+pistaTotal+"\nStrBry: "+sbryTotal+"\nmango: "+mangoTotal+"\nBtrSch:"+btrSchTotal+"\n----------------\nTotal: "+finalTotal+"\nDate & Time: "+dateStr);
                    Item vanillaObj = null;
                    if (vanillaTotal != 0) {
                        vanillaObj = new Item(vanilla, vanillaTotal / vanilla, vanillaTotal);
                        items2.put("vanilla", vanillaObj);
                    } else {
                        items2.remove("vanilla");
                    }
                    Item pistaObj = null;
                    if (pistaTotal != 0) {
                        pistaObj = new Item(pista, pistaTotal / pista, pistaTotal);
                        items2.put("pista", pistaObj);
                    } else {
                        items2.remove("pista");
                    }
                    Item sbryObj = null;
                    if (sbryTotal != 0) {
                        sbryObj = new Item(sbry, sbryTotal / sbry, sbryTotal);
                        items2.put("stwbry", sbryObj);
                    } else {
                        items2.remove("stwbry");
                    }
                    Item mangoObj = null;
                    if (mangoTotal != 0) {
                        mangoObj = new Item(mango, mangoTotal / mango, mangoTotal);
                        items2.put("mango", mangoObj);
                    } else {
                        items2.remove("mango");
                    }
                    Item btrschObj = null;
                    if (btrSchTotal != 0) {
                        btrschObj = new Item(btrSch, btrSchTotal / btrSch, btrSchTotal);
                        items2.put("btrsch", btrschObj);
                    } else {
                        items2.remove("btrsch");
                    }
                    Item kulfiObj = null;
                    if (kulfiTotal != 0) {
                        kulfiObj = new Item(kulfi, kulfiTotal / kulfi, kulfiTotal);
                        items2.put("kulfi", kulfiObj);
                    } else {
                        items2.remove("kulfi");
                    }
                    Item cBarObj = null;
                    if (cbarTotal != 0) {
                        cBarObj = new Item(cbar, cbarTotal / cbar, cbarTotal);
                        items2.put("cBar", cBarObj);
                    } else {
                        items2.remove("cBar");
                    }
                    Item fpackObj = null;
                    if (fpackTotal != 0) {
                        fpackObj = new Item(fpack, fpackTotal / fpack, fpackTotal);
                        items2.put("fpack", fpackObj);
                    } else {
                        items2.remove("fpack");
                    }
                    if (finalTotal != 0) {
                        if(!etOther1.getText().toString().isEmpty() && !etOther1.getText().equals("0")){
                            otherAmt1 = Integer.valueOf(String.valueOf(etOther1.getText()));
//                            IceCreamFinalTotal = finalTotal + otherAmt1;
                            items2.put("other1",new Item(0,0,otherAmt1));
                            IceCreamFinalTotal = finalTotal;
                            System.out.println("IceCreamFinalTotal:>>>>>"+IceCreamFinalTotal);
                        }else{
                            IceCreamFinalTotal = finalTotal;
                            items2.remove("other1");
                            System.out.println("IceCreamFinalTotal:>>>>"+IceCreamFinalTotal);
                        }
                    }else{
                        if(etOther1.getText().toString().isEmpty() || etOther1.getText().equals("0")){
                            items2.remove("other1");
                            IceCreamFinalTotal = finalTotal - otherAmt1;
                        }else{
                            IceCreamFinalTotal = finalTotal;
                        }
                    }
                    StringBuilder var1 =new StringBuilder();
                    for (Map.Entry<String, Item> itr : items2.entrySet()) {
                        Item t = itr.getValue();
                        System.out.println("\n" + itr.getKey() + "--" + t.getTotal());
                        var1.append("\n"+ itr.getKey() + "--" + t.getTotal());
                    }
                    Toast.makeText(getActivity().getApplicationContext(), "Items added:\n"+items2.keySet(), Toast.LENGTH_LONG).show();

//                    m.getMap2(filteredMap2);
//                    String res = db.addRecord(billNo_generated,orangeTotal,kokamTotal,lemonTotal,sarbatTotal,pachakTotal,walaTotal,lSodaTotal,ssrbtTotal,lorangeTotal,LlemonTotal,finalTotal,dateStr);
                    //generate pdf invoice
//                    printInvoice(billNo_generated,orange,kokam,lemon,sarbat,pachak,wala,lsoda,ssrbt,lorange,llemon,finalTotal);
//                    Toast.makeText(MainActivity.this, res, Toast.LENGTH_SHORT).show();
//                    seek.setProgress(0);
//                    seek2.setProgress(0);
//                    seek3.setProgress(0);
//                    seek4.setProgress(0);
//                    seek5.setProgress(0);
//                    seek6.setProgress(0);
//                    seek7.setProgress(0);

//                    seek9.setProgress(0);
//                    seek8.setProgress(0);
//                    seek10.setProgress(0);
                }
//                MainActivity m = (MainActivity) getActivity();
//                m.getMap2(items2);
//                m.getIceTotal(IceCreamFinalTotal);
//                Toast.makeText(m, "putted:"+filteredMap2.size()+" items", Toast.LENGTH_SHORT).show();
            }
        });

//        seek10.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tv22.setText(String.valueOf(seekBar.getProgress()));
//                LlemonTotal = Integer.parseInt(tv22.getText().toString()) * Llemon;
//                refreshTotal();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//        seek8.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tv17.setText(String.valueOf(seekBar.getProgress()));
//                lorangeTotal = Integer.parseInt(tv17.getText().toString()) * lorange;
//                refreshTotal();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//        seek7.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tv14.setText(String.valueOf(seekBar.getProgress()));
//                ssrbtTotal = Integer.parseInt(tv14.getText().toString()) * ssrbt;
//                refreshTotal();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//
//

        tv14.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Kulfi Current Rate:"+kulfi);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("fpack",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Family pack rate updated:"+prefs.getInt("fpack",100), Toast.LENGTH_SHORT).show();
                            seek7.setProgress(0);
                        }else{
                            fpack = prefs.getInt("fpack",100);
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
                fpack = prefs.getInt("fpack",100);
                fpackTotal = Integer.parseInt(tv14.getText().toString()) * fpack;
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
                builder.setTitle("Kulfi Current Rate:"+kulfi);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("kulfi",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Kulfi rate updated:"+prefs.getInt("kulfi",10), Toast.LENGTH_SHORT).show();
                            seek6.setProgress(0);
                        }else{
                            kulfi = prefs.getInt("kulfi",10);
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


        seek9.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv15.setText(String.valueOf(seekBar.getProgress()));
                cbar = prefs.getInt("cbar",10);
                cbarTotal = Integer.parseInt(tv15.getText().toString()) * cbar;
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
                builder.setTitle("ChocoBar Curr Rate:"+cbar);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("cbar",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Chocobar rate updated:"+prefs.getInt("cbar",10), Toast.LENGTH_SHORT).show();
                            seek9.setProgress(0);
                        }else{
                            cbar = prefs.getInt("cbar",10);
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

        seek6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv19.setText(String.valueOf(seekBar.getProgress()));
                kulfi = prefs.getInt("kulfi",10);
                kulfiTotal = Integer.parseInt(tv19.getText().toString()) * kulfi;
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
                builder.setTitle("BtrScotch Curr Rate:"+btrSch);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("btrsch",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Butter Scotch rate updated:"+prefs.getInt("btrsch",20), Toast.LENGTH_SHORT).show();
                            seek4.setProgress(0);
                        }else{
                            btrSch = prefs.getInt("btrsch",20);
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
                btrSch = prefs.getInt("btrsch",20);
                btrSchTotal = Integer.parseInt(tv12.getText().toString()) * btrSch;
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
                builder.setTitle("Mango Curr Rate:"+mango);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("mango",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Mango rate updated:"+prefs.getInt("mango",20), Toast.LENGTH_SHORT).show();
                            seek5.setProgress(0);
                        }else{
                            mango = prefs.getInt("mango",20);
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
                mango = prefs.getInt("mango",20);
                mangoTotal = Integer.parseInt(tseek5.getText().toString()) * mango;
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
                builder.setTitle("StwBerry Curr Rate:"+sbry);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("sbry",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Strw Berry rate updated:"+prefs.getInt("sbry",10), Toast.LENGTH_SHORT).show();
                            seek3.setProgress(0);
                        }else{
                            sbry = prefs.getInt("sbry",10);
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
                sbry = prefs.getInt("sbry",10);
                sbryTotal = Integer.parseInt(tseek9.getText().toString()) * sbry;
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
                builder.setTitle("Pista Curr Rate:"+pista);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("pista",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Pista rate updated:"+prefs.getInt("pista",15), Toast.LENGTH_SHORT).show();
                            seek2.setProgress(0);

                        }else{
                            pista = prefs.getInt("pista",15);
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

//
                tseek2.setText(String.valueOf(seekBar.getProgress()));
                pista = prefs.getInt("pista",15);
                pistaTotal = Integer.parseInt(tseek2.getText().toString()) * pista;
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
        tprogress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout,null);
                builder.setView(customLayout);
                builder.setTitle("Vanilla Curr Rate:"+vanilla);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et = customLayout.findViewById(R.id.editText);
                        EditText et1 = customLayout.findViewById(R.id.editText1);
                        if(!et.getText().toString().equals("")){
                            editor.putInt("vanilla",Integer.parseInt(et.getText().toString())).apply();
                            Toast.makeText(getContext(), "Vanilla rate updated:"+prefs.getInt("vanilla",10), Toast.LENGTH_SHORT).show();
                            seek.setProgress(0);
                        }else{
                            vanilla = prefs.getInt("vanilla",10);
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
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Toast.makeText(getApplicationContext(), ""+seekBar.getProgress()+" progress:"+progress, Toast.LENGTH_SHORT).show();

//                vanilla = seekBar.getProgress();
                tprogress.setText(String.valueOf(seekBar.getProgress()));
                vanilla = prefs.getInt("vanilla",10);
                vanillaTotal = Integer.parseInt(tprogress.getText().toString()) * vanilla;
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

    private void popUpTotal(int custItems1) {
        if(custItems1 != 0){
            etOther1.setText(String.valueOf(custItems1));
            etOther1.clearFocus();
        }else{
            etOther1.setText(String.valueOf(""));
            etOther1.clearFocus();
        }
    }


    public void resetAllseeks2() {
        finalTotal=0;
        IceCreamFinalTotal=0;
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
        if(seek9.getProgress() != 0){
            seek9.setProgress(0);
        }
        if(seek7.getProgress() != 0){
            seek7.setProgress(0);
        }
    }


    private void refreshTotal() {
        finalTotal = 0;
        if (vanillaTotal != 0) {
            finalTotal += vanillaTotal;
        } else {
            finalTotal -= vanillaTotal;
        }
        if (pistaTotal != 0) {
            finalTotal += pistaTotal;
        } else {
            finalTotal -= pistaTotal;
        }
        if (mangoTotal != 0) {
            finalTotal += mangoTotal;
        } else {
            finalTotal -= mangoTotal;
        }
        if (sbryTotal != 0) {
            finalTotal += sbryTotal;
        } else {
            finalTotal -= sbryTotal;
        }
        if (btrSchTotal != 0) {
            finalTotal += btrSchTotal;
        } else {
            finalTotal -= btrSchTotal;
        }
        if (kulfiTotal != 0) {
            finalTotal += kulfiTotal;
        } else {
            finalTotal -= kulfiTotal;
        }
        if (cbarTotal != 0) {
            finalTotal += cbarTotal;
        } else {
            finalTotal -= cbarTotal;
        }
        if (fpackTotal != 0) {
            finalTotal += fpackTotal;
        } else {
            finalTotal -= fpackTotal;
        }
        if (otherAmt1 != 0) {
            finalTotal += otherAmt1;
        } else {
            finalTotal -= otherAmt1;
        }
        Date now = new Date();
        long timestamp = now.getTime();
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("MM/dd/yyyy  hh:mm:ss a");
        }
        String dateStr = sdf.format(timestamp);
        tcf.setText("Vanilla: " + vanillaTotal + "\nPista: " + pistaTotal + "\nStrBry: " + sbryTotal + "\nmango: " + mangoTotal + "\nBtrSch:" + btrSchTotal + "\nKulfi:"+kulfiTotal+"\nCbar:"+cbarTotal+"\nFamily Pack:"+fpackTotal+"\nOther1:"+otherAmt1+"\n----------------\nTotal: " + finalTotal + "\nDate & Time: " + dateStr);
    }

    public void makeClear2() {
        Toast.makeText(context, "make 2", Toast.LENGTH_SHORT).show();
        IceCreamFinalTotal=0;
        finalTotal = 0;
        if (!items2.isEmpty()) {
            resetAllseeks2();
        }
        IceCreamFinalTotal = 0;
        items2.clear();
    }

    public Map<? extends String,? extends Item> getItems2() {
        return items2;
    }
}