package com.vritti.AlfaLavaModule.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.activity.ActivityGRNPutAway;
import com.vritti.AlfaLavaModule.activity.PutAwayDetails;
import com.vritti.AlfaLavaModule.adapter.Adp_Putaway_Detail;
import com.vritti.AlfaLavaModule.bean.PutAwaysBean;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vritti.databaselib.other.WebUrlClass.isSessionActive;

/**
 * Created by Admin-1 on 10/14/2016.
 */
public class FragmentPutAwaysDetail extends Fragment implements SearchView.OnQueryTextListener {

    private static RecyclerView recycler;
    private static Adp_Putaway_Detail adapter;
    private static Paint p = new Paint();
    private static ArrayList<PutAwaysBean> list;
    static View Creatview;
    private String S_GRN, S_HDR;

    private static Context pContext;//Dowmloaditem
    private Dowmloaditem dowmloaditem;
    RelativeLayout bgTransParent;
    TextView grnText;
    TextView poNumberText;
    TextView poDate;
    TextView currentDate;


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;


    public FragmentPutAwaysDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        Creatview = inflater.inflate(R.layout.alfa_activity_putaway, container, false);
        pContext = getContext();


        userpreferences = pContext.getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(pContext);
        String settingKey = ut.getSharedPreference_SettingKey(pContext);
        String dabasename = ut.getValue(pContext, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(pContext, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(pContext, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(pContext, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(pContext, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(pContext, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(pContext, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(pContext, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(pContext, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(pContext, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        S_GRN = PutAwayDetails.GRN_ID;
        S_HDR = PutAwayDetails.GRN_Header;
        initView(Creatview);
        if (Check_Db()) {
            Details_Putaway();
        } else {


            if (isnet()) {
                ProgressHUD.show(getActivity(), "Fetching Putaway...", true, false);
                new StartSession(getActivity(), new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        dowmloaditem = new Dowmloaditem();
                        dowmloaditem.execute(S_HDR, S_GRN);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        dowmloaditem = new Dowmloaditem();
                        dowmloaditem.execute(S_HDR, S_GRN);
                    }


                });

            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

                    }
        return Creatview;
    }

    private void initView(View creatview) {
        recycler = (RecyclerView) creatview.findViewById(R.id.my_recycler_view);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        list = new ArrayList<PutAwaysBean>();
        bgTransParent = creatview.findViewById(R.id.bgTransParent);
        grnText = creatview.findViewById(R.id.grnText);
        poNumberText = creatview.findViewById(R.id.poNumberText);
        poDate = creatview.findViewById(R.id.poDate);
        currentDate = creatview.findViewById(R.id.currentDate);
    }



    public void Details_Putaway() {
        list.clear();

        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PUTAWAY + " WHERE GRNHeader='" + S_HDR + "' AND DoneFlag !='" + WebUrlClass.DoneFlag_Complete + "'", null);//WHERE GRNId =" + GRN_ID + "
            if (c.getCount() == 0) {
            } else {
                c.moveToFirst();
                do {
                    PutAwaysBean bean = new PutAwaysBean();
                    bean.setPutAwaySr(c.getInt(c.getColumnIndex("PutAwaysr")));
                    bean.setGRN_Number(c.getString(c.getColumnIndex("GRNNumber")));
                    bean.setGRN_Header(c.getString(c.getColumnIndex("GRNHeader")));
                    bean.setSuggPutAwayId(c.getString(c.getColumnIndex("SuggPutAwayId")));
                    bean.setGRNDetailId(c.getString(c.getColumnIndex("GRNDetailId")));
                    bean.setLocationMasterId(c.getString(c.getColumnIndex("LocationMasterId")));
                    bean.setItemCode(c.getString(c.getColumnIndex("ItemCode")));
                    bean.setItemDesc(c.getString(c.getColumnIndex("ItemDesc")));
                    bean.setLocationCode(c.getString(c.getColumnIndex("LocationCode")));
                    bean.setPutAwayQty(c.getString(c.getColumnIndex("PutAwayQty")));
                    bean.setFlgDone(c.getString(c.getColumnIndex("DoneFlag")));
                    bean.setFlgInsertUpdate(c.getString(c.getColumnIndex("InsertUpadate")));
                    list.add(bean);
                } while (c.moveToNext());
                adapter = new Adp_Putaway_Detail(list,getActivity());
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                inithelper();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private Boolean Check_Db() {

        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_PUTAWAY + "' WHERE GRNHeader='" + S_HDR + "'", null);
            if (c.getCount() > 0) {

                return true;
            } else {

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;

        }
    }

    private void inithelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {// ItemTouchHelper.LEFT |

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.RIGHT) {
                    // adapter.removeItem(position);
                    String Suggested = adapter.getSuggested(position);
                    ContentValues values = new ContentValues();
                    String aa = WebUrlClass.DoneFlag_Complete;
                    values.put("DoneFlag", aa);
                    sql.update(db.TABLE_PUTAWAY, values, "SuggPutAwayId=?", new String[]{String.valueOf(Suggested)});
                    onResume();
                    Log.e("SuggId Id :   ", Suggested);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    float x = dX;
                    float y = dY;
                    if (dX > 0) { //swipe right
                        p.setColor((Color.parseColor("#A2D6FD")));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_done_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else { //swipe left
                        p.setColor(Color.parseColor("#faeae9"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_done_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler);
    }

    @Override
    public void onResume() {
        super.onResume();
        S_GRN = PutAwayDetails.GRN_ID;
        S_HDR = PutAwayDetails.GRN_Header;
        Details_Putaway();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_putdetail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
          /*  case R.id.menu_scan:
                try {
                    Intent intent = new Intent(WebUrlClass.ACTION_SCAN);
                    intent.putExtra("CODE_39", "PRODUCT_MODE");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException anfe) {
                    pUt.showDialog(getActivity(), "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                }
                return true;*/
            case R.id.menu_send:
                if (isPresentToLocal_Doneflg()) {


                    if (isnet()) {
                        ProgressHUD.show(getActivity(), "sending data to server...", true, false);
                        new StartSession(getActivity(), new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                JSONObject J_obj = getdata();
                                if (J_obj != null) {
                                    String s = J_obj.toString();
                                    s = s.replace("\\\\", "");
                                    new UpLoadData().execute(s);
                                }else {
                                    ProgressHUD.Destroy();
                                    Toast.makeText(getActivity(),"No Records",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                JSONObject J_obj = getdata();
                                if (J_obj != null) {
                                    String s = J_obj.toString();
                                    s = s.replace("\\\\", "");
                                    new UpLoadData().execute(s);
                                }else {
                                    ProgressHUD.Destroy();
                                    Toast.makeText(getActivity(),"No Records",Toast.LENGTH_SHORT).show();
                                }
                            }


                        });

                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "First complete all Items", Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }

        return false;
    }

    private boolean isPresentToLocal(String itemCode) {
        Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_PUTAWAY + "' WHERE SuggPutAwayId=?", new String[]{String.valueOf(itemCode)});
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPresentToLocal_Doneflg() {

        Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_PUTAWAY + "' WHERE DoneFlag=?", new String[]{String.valueOf(WebUrlClass.DoneFlag_Default)});
        if (c.getCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean CheckStatus(String itemCode) {
        String Status;
        Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_PUTAWAY + "' WHERE SuggPutAwayId=?", new String[]{String.valueOf(itemCode)});
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                Status = c.getString(c.getColumnIndex("DoneFlag"));
            } while (c.moveToNext());
            if (Status.equalsIgnoreCase(WebUrlClass.DoneFlag_Default)) {
                return true;
            } else if (Status.equalsIgnoreCase(WebUrlClass.DoneFlag_Complete)) {
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                SharedPreferences share = getContext().getSharedPreferences("GETITEM", Context.MODE_PRIVATE);
                //  String Itemcode = share.getString("Key_Itemcode", null);
                if (isPresentToLocal(contents)) {
                    if (CheckStatus(contents)) {
                        Complete_putlist(contents);
                    } else {
                        Undo_Complete_putList(contents);
                    }

                } else {
                    showAlert(contents);
                    // Toast.makeText(getContext(), "This Item is not present in list" + contents, Toast.LENGTH_LONG).show();
                }
                Toast toast = Toast.makeText(getContext(), "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(getContext(), "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

            }
        }
    }

    public void Undo_Complete_putList(String data) {
        // TODO Auto-generated method stub
        final String itemcode = data;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(data + " Item is already completed \n Do you wish to remove from complete");
        builder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ContentValues values = new ContentValues();
                        String aa = "N";
                        values.put("DoneFlag", aa);
                        String ss = itemcode;
                        sql.update(db.TABLE_PUTAWAY, values, "SuggPutAwayId=?", new String[]{String.valueOf(ss)});
                        onResume();
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onResume();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showAlert(String data) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Item " + data + " is not present in list");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void Complete_putlist(String data) {
        // TODO Auto-generated method stub
        final String itemcode = data;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to complete " + data + " Item");
        builder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        ContentValues values = new ContentValues();
                        String aa = "Y";
                        values.put("DoneFlag", aa);
                        String ss = itemcode;
                        sql.update(db.TABLE_PUTAWAY, values, "SuggPutAwayId=?", new String[]{String.valueOf(ss)});
                        onResume();
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onResume();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private JSONObject getdata() {
        String myTable = db.TABLE_PUTAWAY;
        String y = "Y";
        String searchQuery = "SELECT  * FROM " + myTable + " where GRNNumber='" + S_GRN + "' AND DoneFlag='" + WebUrlClass.DoneFlag_Complete + "'";
        Cursor cursor = sql.rawQuery(searchQuery, null);
        JSONArray resultSet = new JSONArray();
        JSONObject rowObject1 = null;

        int count = cursor.getCount();
        if (count == 0) {
            rowObject1 = null;
        } else {
            cursor.moveToFirst();
            do {
                JSONObject rowObject = new JSONObject();
                try {
                    rowObject.put("SuggPutAwayId", cursor.getString(cursor.getColumnIndex("SuggPutAwayId")));
                    rowObject.put("GRNDetailId", cursor.getString(cursor.getColumnIndex("GRNDetailId")));
                    rowObject.put("LocationMasterId", cursor.getString(cursor.getColumnIndex("LocationMasterId")));
                    rowObject.put("ItemCode", cursor.getString(cursor.getColumnIndex("ItemCode")));
                    rowObject.put("ItemDesc", cursor.getString(cursor.getColumnIndex("ItemDesc")));
                    rowObject.put("LocationCode", cursor.getString(cursor.getColumnIndex("LocationCode")));
                    rowObject.put("PutAwayQty", cursor.getString(cursor.getColumnIndex("PutAwayQty")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultSet.put(rowObject);

            } while (cursor.moveToNext());

            rowObject1 = new JSONObject();

            try {
                rowObject1.put("ItemDetails", resultSet);
                rowObject1.put("GRNHeaderId", S_HDR);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return rowObject1;
    }

    private class UpLoadData extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String strRes = null;
            String url = CompanyURL+ WebUrlClass.api_PostGRN;

            try {
                res = ut.OpenPostConnection(url, params[0],pContext);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgressHUD.Destroy();
            if (s.contains("true")) {
                Toast.makeText(pContext, "Data Send Successfully", Toast.LENGTH_LONG).show();
                String myTable = db.TABLE_PUTAWAY;
                String myTable_user = db.TABLE_PUTAWAY_USER;
                String y = "Y";
                String searchQuery = "SELECT  * FROM " + myTable + " where GRNNumber='" + s + "' AND DoneFlag='" + WebUrlClass.DoneFlag_Complete + "'";
                sql.delete(myTable, "GRNNumber=? And DoneFlag=?", new String[]{String.valueOf(S_GRN), String.valueOf(WebUrlClass.DoneFlag_Complete)});
                Cursor c = sql.rawQuery("SELECT * FROM " + myTable, null);
                int i = c.getCount();
                sql.delete(myTable_user, "GRNNo=?", new String[]{String.valueOf(S_GRN)});
                Cursor c1 = sql.rawQuery("SELECT * FROM " + myTable_user, null);
                int i2 = c.getCount();
            } else {
                Toast.makeText(pContext, "Data Not Send", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class Dowmloaditem extends AsyncTask<String, String, ArrayList<String>> {
        Object res;
        String response;
        String GRNNumber = null;
        String GRNHeader = null;

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String strRes = null;
            GRNNumber = params[1];
            GRNHeader = params[0];
            String url = CompanyURL + WebUrlClass.api_getItem + "?id=" + params[0];

            try {
                res = ut.OpenConnection(url,pContext);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                response = "Error";
                e.printStackTrace();
            }
            ArrayList<String> Data = new ArrayList<>();
            Data.add(params[0]);
            Data.add(params[1]);
            Data.add(response);
            return Data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);//GRNHeaderId
            String GRN_No = s.get(1);
            String GRN_Hdr = s.get(0);
            String res = s.get(2);
            if (!(res.equalsIgnoreCase("[]"))) {
                if (res.contains("SuggPutAwayId")) {
                    try {
                        JSONArray jResults = new JSONArray(res);
                        Log.d("test", "jResults :=" + jResults);
                        cf.DeleteAllRecord(db.TABLE_PUTAWAY);
                        Cursor cur = sql.rawQuery("SELECT *   FROM " + db.TABLE_PUTAWAY, null);
                        Log.e("Table values----", "" + cur.getCount());
                        for (int index = 0; index < jResults.length(); index++) {
                            JSONObject jstring = jResults.getJSONObject(index);
                            PutAwaysBean bean = new PutAwaysBean();
                            bean.setGRN_Number(GRN_No);
                            bean.setGRN_Header(GRN_Hdr);
                            bean.setSuggPutAwayId(jstring.getString("SuggPutAwayId"));
                            bean.setGRNDetailId(jstring.getString("GRNDetailId"));
                            bean.setLocationMasterId(jstring.getString("LocationMasterId"));
                            bean.setItemCode(jstring.getString("ItemCode"));
                            bean.setItemDesc(jstring.getString("ItemDesc"));
                            bean.setLocationCode(jstring.getString("LocationCode"));
                            bean.setPutAwayQty(jstring.getString("PutAwayQty"));
                            bean.setFlgDone(WebUrlClass.DoneFlag_Default);
                            bean.setFlgInsertUpdate(WebUrlClass.Flag_Update);
                            cf.Insert_Putaways(bean);
                        }
                    } catch (Exception e) {

                    }
                    Details_Putaway();
                } else {
                    Toast.makeText(pContext, "Invalid GRN", Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(pContext, "No Items Present", Toast.LENGTH_SHORT).show();
            }
            ProgressHUD.Destroy();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<PutAwaysBean> filteredModelList = filter(list, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<PutAwaysBean> filter(ArrayList<PutAwaysBean> models, String query) {
        query = query.toLowerCase();
        final ArrayList<PutAwaysBean> filteredModelList = new ArrayList<>();
        for (PutAwaysBean model : models) {
            final String text = model.getItemDesc().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public boolean isnet() {
        Context context = pContext.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
