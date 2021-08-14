package com.vritti.AlfaLavaModule.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.adapter.Adp_ViewPager;
import com.vritti.AlfaLavaModule.bean.PutAwaysBean;
import com.vritti.AlfaLavaModule.fragment.FragmentPutAwaysComplete;
import com.vritti.AlfaLavaModule.fragment.FragmentPutAwaysDetail;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin-1 on 10/5/2016.
 */
public class PutAwayDetails extends AppCompatActivity {
    private RecyclerView recycler;
   // private static UpLoadData upLoadData;
    private ArrayList<PutAwaysBean> list;
    private AlertDialog.Builder alertDialog;
    private Dialog mdialog;
    private EditText et_country;
    private int edit_position;
    private View view;
    public static String GRN_ID, GRN_Header;
    private boolean add = false;
    private EditText Edt_location;
    private EditText Edt_quantity;
    private String UserId;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public ViewPager viewPager;
    private static Context pContext;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_activity_putawaydetail);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.app_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(PutAwayDetails.this);
        String settingKey = ut.getSharedPreference_SettingKey(PutAwayDetails.this);
        String dabasename = ut.getValue(PutAwayDetails.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(PutAwayDetails.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(PutAwayDetails.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(PutAwayDetails.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(PutAwayDetails.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(PutAwayDetails.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(PutAwayDetails.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(PutAwayDetails.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(PutAwayDetails.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(PutAwayDetails.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
        list = new ArrayList<PutAwaysBean>();


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);






        Intent intent = getIntent();
        GRN_ID = intent.getStringExtra("GRN_No");//GRN_header
        GRN_Header = intent.getStringExtra("GRN_header");


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("Page scrolled", " page " + position);
            /*  if(position == 0){
                  new FragmentPutAwaysDetail();
              }else {
                  new  FragmentPutAwaysComplete();
              }*/
            }

            @Override
            public void onPageSelected(int position) {
                //    actionBar.setSelectedNavigationItem(position);
                Fragment fragment = ((Adp_ViewPager) viewPager.getAdapter()).getFragment(position);
                if (position == 1 && fragment != null) {
                    fragment.onResume();
                } else if (position == 0 && fragment != null) {
                    fragment.onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public ViewPager getViewPager() {
        return viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adp_ViewPager adapter = new Adp_ViewPager(getSupportFragmentManager(), this);
        adapter.addFragment(new FragmentPutAwaysDetail(), "Detail");
        adapter.addFragment(new FragmentPutAwaysComplete(), "Completed");
        viewPager.setAdapter(adapter);

    }
    /*  private void inithelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {// ItemTouchHelper.LEFT |

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT) {
                    adapter.removeItem(position);
                  *//*  edit_position = position;
                    Edit_dig(list.get(position).getLocationCode(), list.get(position).getPutAwayQty());*//*
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
    }*/

    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }


    /*private void initDialog() {
        alertDialog = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.dailog_putaway_detail, null);
        alertDialog.setView(view);
        // alertDialog.setCancelable(false);
        final Dialog dialog = alertDialog.create();
        dialog.setCancelable(false);
        TextView text = (TextView) view.findViewById(R.id.btn_ok_grn);
         text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }*/



/*
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
            String url = pUt.getSharedPreference_URL(pContext) + WebUrlClass.api_PostGRN;

            try {
                res = ut.OpenPostConnection(url, params[0]);
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
                String myTable = pFd.TABLE_PUTAWAY;
                SQLiteDatabase Sql = pDb.getWritableDatabase();
                String y = "Y";
                String searchQuery = "SELECT  * FROM " + myTable + " where GRNNumber='" + GRN_ID + "' AND DoneFlag='" + y + "'";
                Sql.delete(myTable, "GRNNumber=? And DoneFlag=?", new String[]{String.valueOf(GRN_ID), String.valueOf(y)});
            } else {
                Toast.makeText(pContext, "Data Not Send", Toast.LENGTH_LONG).show();
            }
        }
    }
*/




  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_first, menu);
       *//* SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*//*
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:

                return true;
            case R.id.menu_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }*/

    private String getInsert_XML() {

        int count = 0;
        String xml1 = null;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PUTAWAY + " WHERE InsertUpadate=?", new String[]{"Insert"});//WHERE GRNId =" + GRN_ID + "
            if (c.getCount() == 0) {
                c.close();
                xml1 = "<Detail></Detail>";

            } else {
                StringBuilder sb = new StringBuilder();
                sb.setLength(0);
                sb.append("<Detail>");
                c.moveToFirst();
                //SuggPutAwayId   GRNDetailId   LocationMasterId   PutAwayQty
                do {

                    int s = c.getInt(c.getColumnIndex("PutAwaysr"));
                    String s1 = c.getString(c.getColumnIndex("GRNId"));
                    String s2 = c.getString(c.getColumnIndex("ItemCode"));
                    String s3 = c.getString(c.getColumnIndex("ItemDesc"));
                    String s4 = c.getString(c.getColumnIndex("LocationCode"));
                    String s5 = c.getString(c.getColumnIndex("DoneFlag"));
                    String s6 = c.getString(c.getColumnIndex("InsertUpadate"));
                    sb.append("<Table>");
                    sb.append("<SuggPutAwayId>" + c.getString(c.getColumnIndex("SuggPutAwayId"))
                            + "</SuggPutAwayId>");
                    sb.append("<GRNDetailId>" + c.getString(c.getColumnIndex("GRNDetailId"))
                            + "</GRNDetailId>");
                    sb.append("<LocationMasterId>" + c.getString(c.getColumnIndex("LocationMasterId"))
                            + "</LocationMasterId>");
                    sb.append("<PutAwayQty>" + c.getString(c.getColumnIndex("PutAwayQty"))
                            + "</PutAwayQty>");
                    sb.append("</Table>");
                } while (c.moveToNext());
                c.close();
                sql.close();
                sb.append("</Detail>");
                xml1 = sb.toString();
            }
        } catch (Exception e) {
            xml1 = "<Detail> NoData </Detail>";

        }

        return xml1;
    }



}
