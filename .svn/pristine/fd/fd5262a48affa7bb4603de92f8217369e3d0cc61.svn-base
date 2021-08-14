package com.vritti.sales.CounterBilling;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.Interface.AddItemReport;
import com.vritti.sales.activity.Sales_HomeSActivity;
import com.vritti.sales.adapters.ItemlistAdapter;
import com.vritti.sales.beans.AdditemBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sharvari on 3/18/2016.
 */
public class ItemwiseReportsFragment extends Fragment implements AddItemReport {

    private Context parent;
    EditText editText_itemCode, editText_Item_Name;
    Button btn_fromdate, btn_todate, btn_Cancel, btn_selectitem, btn_dailysale, btn_totalsale;
    static int year, month, day;
    private ListView listView;
    ItemlistAdapter itemlistAdapter;
    private ArrayList<AdditemBean> arrayList;
    public static ArrayList<AdditemBean> additemBeanslist;
    Button button_add, buttonDailySale, buttonTotalSale, buttonCancel;
    AdditemBean additemBean;
    String itemname = null;
    String itemid = "";
    Button buttonItem, buttonFdate, buttonTdate;

    String itemcode, item_name, item_qnty, item_unit, item_mrp, freeitemid, freeitem, freeitem_qnty;
    String radio_selection, userid;

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    String From_date = null, To_date = null;
    String TOVDIS = "T";
    SharedPreferences sharedpreferences;
    String FromDate, ToDate, toDate, fromdate;
    String res = "";

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    DatabaseHandlers dbhandler;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    public ItemwiseReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parent = getContext();

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(parent);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(parent, dabasename);
        sql_db = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        //mprogress=findViewById(R.id.toolbar_progress_App_bar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent = getActivity();
        View view = inflater.inflate(R.layout.tbuds_activity_sale, container, false);

       /* sharedpreferences = getActivity().getSharedPreferences(UserRegistrationActivity.MyPREFERENCES,
                Context.MODE_PRIVATE);*/
        userid = sharedpreferences.getString("userid", null);
     //   dbHandler = new DatabaseHandler(getActivity());

        btn_selectitem = (Button) view.findViewById(R.id.buttonItem);
        btn_fromdate = (Button) view.findViewById(R.id.buttonFdate);
        btn_todate = (Button) view.findViewById(R.id.buttonTdate);
       // btn_fromdate.setText(FromDate);
        btn_fromdate.setText("From Date");
      //  btn_todate.setText(ToDate);
        btn_todate.setText("To Date");
        btn_Cancel = (Button) view.findViewById(R.id.buttonCancel);
        btn_dailysale = (Button)view.findViewById(R.id.buttonDailySale);
        btn_totalsale = (Button)view.findViewById(R.id.buttonTotalSale);
        arrayList = new ArrayList<AdditemBean>();
        additemBeanslist = new ArrayList<AdditemBean>();

        setListener();
        return view;
    }

    private void setListener() {

        btn_selectitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additemBeanslist.clear();

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.tbuds_activity_add_items_report);
                dialog.setTitle("Item List");
                listView = (ListView) dialog.findViewById(R.id.listView_additemlist);
                button_add = (Button) dialog.findViewById(R.id.button_addItem);
                getDataFromDatabase();
                dialog.show();

                button_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (additemBeanslist.size() > 0) {
                            for (int i = 0; i < additemBeanslist.size(); i++) {

                                if (i == 0) {
                                    itemname = additemBeanslist.get(0).getItemname();
                                    itemid = additemBeanslist.get(0).getItemmasterid();
                                } else {
                                    itemname = itemname + " , " + additemBeanslist.get(i).getItemname();
                                    itemid = itemid + " , " + additemBeanslist.get(0).getItemmasterid();
                                }
                            }
                        }
                        if (itemname == null) {
                            btn_selectitem.setText("Select Item");
                        } else {
                            btn_selectitem.setText(itemname);
                        }
                        dialog.dismiss();
                    }
                });
                // buttonItem.setText(itemname);
            }
        });

        btn_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                btn_fromdate.setText("From Date");

                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                btn_fromdate.setText(dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year);

                                From_date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                      /*  fromdate = year + "-" + String.format("%02d", (monthOfYear + 1))
                                + "-" + dayOfMonth + " 00:00:01.000";*/

                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        btn_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                //  c.add(Calendar.DAY_OF_MONTH, 30);
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                btn_todate.setText("To Date");

                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                btn_todate.setText(dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year);

                                To_date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                //  toDate = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" +
                                // dayOfMonth + " 00:00:01.000";

                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        btn_dailysale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemname != null && From_date != null && To_date != null) {
                    Intent intent = new Intent(getContext(), DailySaleActivity.class);
                    intent.putExtra("Items", itemid);
                    intent.putExtra("FromDate", From_date);
                    intent.putExtra("ToDate", From_date);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(),
                            "Enter Item and select dates", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_totalsale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemname != null && From_date != null && To_date != null) {
                    Intent intent = new Intent(getContext(), TotalSaleActivity.class);
                    intent.putExtra("Items", itemid);
                    intent.putExtra("FromDate", From_date);
                    intent.putExtra("ToDate", To_date);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(),
                            "Enter Item and select dates", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);*/
                Intent intent = new Intent(getActivity(), Sales_HomeSActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private static void finish() {
    }

    public void getDataFromDatabase() {
      /*  sql += "SELECT * FROM " + TABLE_MARCHANT_ITEM_RUNI;
        sql += " WHERE ItemName LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY ItemMasterId DESC";
        ItemMasterId
        ItemName*/
        // TODO Auto-generated method stub
        arrayList.clear();
     //   DatabaseHandler db1 = new DatabaseHandler(getContext());
     //   SQLiteDatabase db = db1.getWritableDatabase();
      /*  + "(Itemid TEXT, Itemname TEXT, NMrpV TEXT, " +
                "QtyV TEXT,UnitV TEXT,IsUploaded TEXT)";*/
        try {
            Cursor c = sql_db.rawQuery("Select distinct Itemname,Itemid from "
                    + dbhandler.TABLE_ITEM_MRP_Runi, null);
            AdditemBean additemBean1 = new AdditemBean();
            additemBean1.setItemname("All Items");
            additemBean1.setItemmasterid("All Items");
            arrayList.add(additemBean1);

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    additemBean = new AdditemBean();
                    additemBean.setItemmasterid(c.getString(c.getColumnIndex("Itemid")));
                    // additemBean.setItemMrp(c.getString(c.getColumnIndex("NMrpV")));
                    additemBean.setItemname(c.getString(c.getColumnIndex("Itemname")));
                    arrayList.add(additemBean);

                } while (c.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            // db.close();
        }
        itemlistAdapter = new ItemlistAdapter(getContext(), arrayList, this);
        listView.setAdapter(itemlistAdapter);
    }

    @Override
    public void addItemsReports(String itemmasterid, String itemname, String itemMrp, boolean status) {
        Boolean isProductAdded = false;
        if (additemBeanslist.size() > 0) {
            for (int i = 0; i < additemBeanslist.size(); i++) {
                if (additemBeanslist.get(i).getItemname()
                        .equalsIgnoreCase(itemname)) {
                    isProductAdded = true;
                }
            }
        }
        if (!isProductAdded) {
            AdditemBean bean = new AdditemBean();
            bean.setItemmasterid(itemmasterid);
            bean.setItemMrp(itemMrp);
            bean.setItemname(itemname);
            additemBeanslist.add(bean);
        }
    }
}

