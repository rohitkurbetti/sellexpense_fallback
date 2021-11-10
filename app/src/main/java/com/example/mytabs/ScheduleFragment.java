package com.example.mytabs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ScheduleFragment extends Fragment {
    SharedPreferences prefs;
    FirebaseFirestore db;
    ProgressDialog pd;
    SharedPreferences.Editor editor;
    int expense = 0;
    int todayTotal = 0;
    String ss;
    TextView tt;
    int availBal = 0;
    TextView tvPrevBal;
    TextView tvSelling;
    String prevDateStr;
    EditText etExp;
    EditText etSelling,etExpense,etProfit;
    TextView etDate;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ImageButton btnEdit;
    ImageButton btnDelete;
    int custItems=0;
    Map<String,Integer> items = new HashMap<>();

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        prefs = getActivity().getSharedPreferences("bill", MODE_PRIVATE);
        editor = prefs.edit();
        pd = new ProgressDialog(v.getContext());
        tt = v.findViewById(R.id.tt);
        btnEdit = v.findViewById(R.id.button4);
        btnDelete = v.findViewById(R.id.button6);
        tvPrevBal = v.findViewById(R.id.tvPrevBal);
        tvSelling = v.findViewById(R.id.tvSelling);
        etExp = v.findViewById(R.id.etExp);
        TextView tvProfit = v.findViewById(R.id.tvProfit);
        Button btnCommit = v.findViewById(R.id.btnCommit);
        tvPrevBal.setText(String.valueOf("0"));
        EditText etExpBill = v.findViewById(R.id.editTextNumber);
        DbManager db = new DbManager(getContext());


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etExpBill.getText().toString().isEmpty() && !etExpBill.getText().toString().contentEquals("0")) {
                    int billno = Integer.parseInt(String.valueOf(etExpBill.getText().toString()));
                    Cursor res = db.getOneExp(billno);
                    if(res.getCount() > 0){
                        final View custExpEditView = getLayoutInflater().inflate(R.layout.edit_exp_view, null);
                        etSelling = custExpEditView.findViewById(R.id.expSelling);
                        etExpense = custExpEditView.findViewById(R.id.expExpense);
                        etProfit = custExpEditView.findViewById(R.id.expProfit);
                        etDate = custExpEditView.findViewById(R.id.expDate);
                        while(res.moveToNext()){
                            etSelling.setText(String.valueOf(res.getInt(1)));
                            etExpense.setText(String.valueOf(res.getInt(2)));
                            etProfit.setText(String.valueOf(res.getInt(3)));
                            etDate.setText(String.valueOf(res.getString(4)));
                        }
                        etSelling.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                int f = Integer.parseInt(etSelling.getText().toString().isEmpty()?"0":etSelling.getText().toString()) - Integer.parseInt(etExpense.getText().toString().isEmpty()?"0":etExpense.getText().toString());
                                etProfit.setText(String.valueOf(f));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        etExpense.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                int f = Integer.parseInt(etSelling.getText().toString().isEmpty()?"0":etSelling.getText().toString()) - Integer.parseInt(etExpense.getText().toString().isEmpty()?"0":etExpense.getText().toString());
                                etProfit.setText(String.valueOf(f));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        etDate = custExpEditView.findViewById(R.id.expDate);
                        TextView finalEtDate = etDate;
                        etDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar c = Calendar.getInstance();
                                int mYear = c.get(Calendar.YEAR);
                                int mMonth = c.get(Calendar.MONTH);
                                int mDay = c.get(Calendar.DAY_OF_MONTH);
                                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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
                                        String min = day1 + "-" + month1 + "-" + view.getYear();
//                                        etDate.setText(day1 + "/" + month1 + "/" + view.getYear());
                                        finalEtDate.setText(view.getYear()+"/"+ month1 + "/" + day1);
                                        System.out.println(finalEtDate);
                                    }
                                }, mYear, mMonth, mDay);
                                datePickerDialog.show();
                            }
                        });

//                        etExpense.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                if(!etExpense.getText().toString().isEmpty()){
//                                    if(etSelling.getText().toString().isEmpty()){
//                                        etSelling.setText(String.valueOf("0"));
//                                        int res = calculateProfit(Integer.parseInt(etSelling.getText().toString()),Integer.parseInt(etExpense.getText().toString()));
//                                        tvProfit.setText(String.valueOf(res));
//                                    }
//                                }else{
//                                    etExpense.setText(String.valueOf("0"));
//                                }
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable s) {
//
//                            }
//                        });
                        etExpense.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Add/Remove Items");
                                builder.setCancelable(false);
                                final View expEditView = getLayoutInflater().inflate(R.layout.custom_items,null);
                                ImageButton btnAdd = expEditView.findViewById(R.id.button7);
                                ImageButton btnDel = expEditView.findViewById(R.id.button8);
                                EditText etAddItemName = expEditView.findViewById(R.id.editTextTextPersonName2);
                                EditText etAddValue = expEditView.findViewById(R.id.editTextNumber2);
                                Spinner spinner = expEditView.findViewById(R.id.spinner2);
                                TextView textView33 = expEditView.findViewById(R.id.textView33);
                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
                                spinner.setSelection(Adapter.NO_SELECTION);
                                spinnerAdapter.add("Select Item");
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
//                spinnerAdapter.remove((String) spinner.getSelectedItem());
                                items = new HashMap<>();


                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        System.out.println(spinnerAdapter.getItem(position));
                                        for(Map.Entry<String,Integer> m : items.entrySet()){
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
                                            items.put(itemName,itemVal);
                                            System.out.println(items.entrySet());
                                            spinnerAdapter.clear();
                                            for(Map.Entry<String,Integer> m : items.entrySet()){
                                                spinnerAdapter.add(m.getKey());
                                            }
                                            etAddItemName.setText("");
                                            etAddValue.setText("");
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        int z = 0;
                                        for(Map.Entry<String,Integer> i : items.entrySet()){
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
                                        items.remove(selectedItem);
                                        if(items.containsKey(selectedItem)){
                                            items.remove(selectedItem);
                                        }else{
                                            etAddItemName.setText("");
                                            etAddValue.setText("");
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        int f=0;
                                        for(Map.Entry<String,Integer> i : items.entrySet()){
                                            sb.append(i.getKey()+"->"+i.getValue()+"\n");
                                            f = f + i.getValue();
                                        }
                                        sb.append("---------\nTotal:"+f);
                                        textView33.setText(sb);
                                    }
                                });
                                builder.setView(expEditView);

                                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        custItems = 0;
                                        for(Map.Entry<String,Integer> m : items.entrySet()){
                                            if(items.size()>0){
                                                custItems = custItems + m.getValue();
                                            }else{
                                                custItems = 0;
                                            }
                                        }
                                        System.out.println(items.entrySet());
                                        popUpTotalForEditExp(custItems);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        items.clear();
                                        spinnerAdapter.clear();
                                        etAddItemName.setText("");
                                        etAddValue.setText("");
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Editing Expense No: "+billno);
                        builder.setCancelable(false);
                        builder.setView(custExpEditView);
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!etSelling.getText().toString().isEmpty() && !etSelling.getText().toString().isEmpty() && !etProfit.getText().toString().isEmpty() && !etDate.getText().toString().contentEquals("select Date")){
                                    int sell,profit,exp;
                                    String dateStr;
                                    sell = Integer.parseInt(etSelling.getText().toString().isEmpty()?"0":etSelling.getText().toString());
                                    exp = Integer.parseInt(etExpense.getText().toString().isEmpty()?"0":etExpense.getText().toString());
                                    profit = Integer.parseInt(etProfit.getText().toString().isEmpty()?"0":etProfit.getText().toString());
                                    dateStr = String.valueOf(etDate.getText().toString());
                                    db.updateExpOne(billno,sell,exp,profit,dateStr,String.valueOf(items.entrySet()));
                                    Toast.makeText(getContext(), "Expense no has been updated: "+billno, Toast.LENGTH_SHORT).show();
                                    etExpBill.clearFocus();
                                }else{
                                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                items.clear();
                                etExp.setText("");
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else{
                        Toast.makeText(getContext(), "Expense not found", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Please Enter Expense No.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etExpBill.getText().toString().isEmpty() && !etExpBill.getText().toString().contentEquals("0")) {
                    int billno = Integer.parseInt(String.valueOf(etExpBill.getText().toString()));
                    Cursor res = db.getOneExp(billno);
                    if(res.getCount() > 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Delete");
                        builder.setMessage("Do you want to delete Expense No " + billno + "?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String resMsg = db.deleteExpByBillNo(billno);
                                Toast.makeText(getContext(), resMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else{
                        Toast.makeText(getContext(), "Expense Not Found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Please Enter Expense No.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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


        etExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                items = new HashMap<>();

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println(spinnerAdapter.getItem(position));
                        for(Map.Entry<String,Integer> m : items.entrySet()){
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
                            items.put(itemName,itemVal);
                            System.out.println(items.entrySet());
                            spinnerAdapter.clear();
                            for(Map.Entry<String,Integer> m : items.entrySet()){
                                spinnerAdapter.add(m.getKey());
                            }
                            etAddItemName.setText("");
                            etAddValue.setText("");
                        }
                        StringBuilder sb = new StringBuilder();
                        int z = 0;
                        for(Map.Entry<String,Integer> i : items.entrySet()){
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
                        items.remove(selectedItem);
                        if(items.containsKey(selectedItem)){
                            items.remove(selectedItem);
                        }else{
                            etAddItemName.setText("");
                            etAddValue.setText("");
                        }
                        StringBuilder sb = new StringBuilder();
                        int f=0;
                        for(Map.Entry<String,Integer> i : items.entrySet()){
                            sb.append(i.getKey()+"->"+i.getValue()+"\n");
                            f = f + i.getValue();
                        }
                        sb.append("---------\nTotal:"+f);
                        textView33.setText(sb);
                    }
                });
                builder.setView(customItems);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        custItems = 0;
                        for(Map.Entry<String,Integer> m : items.entrySet()){
                            if(items.size()>0){
                                custItems = custItems + m.getValue();
                            }else{
                                custItems = 0;
                            }
                        }
                        System.out.println(custItems);
                        popUpTotal(custItems);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        items.clear();
                        spinnerAdapter.clear();
                        etAddItemName.setText("");
                        etAddValue.setText("");
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        etExp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!etExp.getText().toString().isEmpty()) {
                    expense = Integer.parseInt(etExp.getText().toString());
                    tvProfit.setText(String.valueOf(todayTotal - (expense != 0 && !etExp.getText().toString().isEmpty() ? expense : 0)));
                } else {
                    tvProfit.setText(String.valueOf(todayTotal + (expense != 0 && !etExp.getText().toString().isEmpty() ? expense : 0)));
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
                TextView tb1 = customView.findViewById(R.id.kokam);
                TextView tb2 = customView.findViewById(R.id.lemon);
                TextView tb3 = customView.findViewById(R.id.sarbat);
                TextView tb4 = customView.findViewById(R.id.ssarbat);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Expense Data");
                builder.setView(customView);
                if (res.getCount() > 0) {
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            tv2.setTooltipText(String.valueOf(res.getString(5)));
                        }
                        tv2.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv3 = new TextView(customView.getContext());
                        tv3.setText(String.valueOf(res.getInt(3)));
                        tv3.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv4 = new TextView(customView.getContext());
                        tv4.setText(String.valueOf(res.getString(4)));
                        tv4.setGravity(Gravity.CENTER_HORIZONTAL);


                        if (isDarkThemeOn) {
                            cname1.setTextColor(Color.WHITE);
                            tb0.setTextColor(Color.WHITE);
                            tb1.setTextColor(Color.WHITE);
                            tb2.setTextColor(Color.WHITE);
                            tb3.setTextColor(Color.WHITE);
                            tb4.setTextColor(Color.WHITE);

                        } else {
                            cname1.setTextColor(Color.BLACK);
                            tb0.setTextColor(Color.BLACK);
                            tb1.setTextColor(Color.BLACK);
                            tb2.setTextColor(Color.BLACK);
                            tb3.setTextColor(Color.BLACK);
                            tb4.setTextColor(Color.BLACK);
                        }
                        tableRow.addView(tv1);
                        tableRow.addView(cname1);
                        tableRow.addView(tv2);
                        tableRow.addView(tv3);
                        tableRow.addView(tv4);
//                    }
                        tableLayout.addView(tableRow);
                    }
                } else {
//                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                    TextView tvNoData = new TextView(customView.getContext());
                    tvNoData.setText(String.valueOf("No data found"));
                    tvNoData.setGravity(Gravity.CENTER_HORIZONTAL);

                    TableRow tableRow = new TableRow(customView.getContext());
                    tableRow.setPadding(10, 10, 10, 10);
                    tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
                    tableRow.addView(tvNoData);

                    tableLayout.addView(tableRow);
                }
                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });


        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvProfit.getText().toString().contentEquals("0")) {
                    etExp.setText(String.valueOf("0"));
                }
                if (tt.getText().toString().contentEquals("Select Date")) {
                    callDatePicker();

                } else {
                    int id = db.checkEntryExists(ss);
                    int getProfitVal = db.getProfitVal(ss);
                    int currTodayBal = 0;
                    System.out.println("Selling:" + tvSelling.getText() + " exp:" + expense + " profit" + tvProfit.getText() + " Date:" + ss);
                    int profitToday = Integer.parseInt(tvProfit.getText().toString());
//                    Toast.makeText(getContext(), "Id returned:" + id, Toast.LENGTH_SHORT).show();
                    if (id != 0) {
                        System.out.println("currentTodalBal:>>" + currTodayBal);
                        StringBuilder sb1 = new StringBuilder();
                        if(items.size()>0){
                            items.entrySet().toString();

                            sb1.append("[");
                            for(Map.Entry<String,Integer> itr : items.entrySet()){
                                sb1.append(itr.getKey()+"="+itr.getValue()+" ");
                            }
                            sb1.append("]");
                            System.out.println(sb1);
                        }else{
                            sb1.append("");
                        }
                        db.updateExpOne(id, todayTotal, expense, profitToday, ss,String.valueOf(sb1));
                        try {
                            savetoFireStore(todayTotal, expense, profitToday, ss,String.valueOf(sb1));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(), "Expense updated successfully: "+id, Toast.LENGTH_SHORT).show();
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append(items.entrySet());
                        System.out.println(sb);
                        int res = db.saveOne(todayTotal, expense, profitToday, ss,String.valueOf(sb));
                        try {
                            savetoFireStore(todayTotal, expense, profitToday, ss,String.valueOf(sb));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(res==1){
                            Toast.makeText(getContext(), "Expense saved successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Expense could not be saved!", Toast.LENGTH_SHORT).show();
                        }

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
//                jobScheduler.cancel(JOB_ID);
//                Toast.makeText(getApplicationContext(), "Job Cancelled...", Toast.LENGTH_SHORT).show();
                callDatePicker();
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
        return v;
    }


    private void savetoFireStore(int todayTotal, int expense, int profitToday, String ss, String s) throws ParseException {
        db = FirebaseFirestore.getInstance();
        pd.setMessage("Uploading data to FireStore");
        pd.show();
        Map<String,Object> m = new HashMap<>();
        m.put("selling",todayTotal);
        m.put("expense",expense);
        m.put("profit",profitToday);
        m.put("date",ss);
        m.put("expenses",String.valueOf(s));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date d = sdf.parse(ss);
        SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
        db.collection("Expenses_temp").document(String.valueOf(sdff.format(d.getTime()))).set(m)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Expense Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Expense Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void popUpTotalForEditExp(int custItems) {
        if(custItems != 0){
            etExpense.setText(String.valueOf(custItems));
        }else{
            etExpense.setText(String.valueOf("0"));
        }
    }

    private void popUpTotal(int custItems) {
        if(custItems != 0){
            etExp.setText(String.valueOf(custItems));
        }else{
            etExp.setText(String.valueOf("0"));
        }
    }

    private int calculateProfit(int selling, int expense) {
        int finalTotal = 0;
        finalTotal = selling + expense;
        return finalTotal;
    }

    private void getAvailBalTillDate(String prevDateStr) {
        DbManager db = new DbManager(getContext());
        Cursor cRes = db.getAvailBalTillDate(prevDateStr);
        if (cRes.getCount() > 0) {
            while (cRes.moveToNext()) {
                availBal += Integer.parseInt(String.valueOf(cRes.getInt(3)));
            }
            tvPrevBal.setText(String.valueOf(availBal));
            availBal = 0;
        } else {
            tvPrevBal.setText(String.valueOf(0));
            availBal = 0;
        }
    }

    private void getTodaysSeeling() {
        todayTotal=0;
        DbManager db = new DbManager(getContext());
        Cursor res = db.findRange(tt.getText().toString(), tt.getText().toString());
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                todayTotal += Integer.parseInt(String.valueOf(res.getInt(27)));
            }
        } else {
            todayTotal = 0;
        }
        tvSelling.setText(String.valueOf(todayTotal));
    }


    private void callDatePicker() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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
                ss = view.getYear() + "/" + month1 + "/" + day1;
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
//                    cc.add(Calendar.DATE,-1);  for getting previous date
                    Date prevDate = cc.getTime();
                    prevDateStr = sdf.format(prevDate);
                    System.out.println("Previous Date:" + prevDateStr);
                    getAvailBalTillDate(prevDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, mYear, mMonth, mDay);
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}