package com.vritti.AlfaLavaModule.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.PI.BluetoothConnectivityActivity;

import com.vritti.AlfaLavaModule.activity.AlfaShipment.DOShipmentListActivity;
import com.vritti.AlfaLavaModule.activity.Consolidation.PickListNoListActivity;
import com.vritti.AlfaLavaModule.activity.Inspection.InspectionPackingOrderListActivity;
import com.vritti.AlfaLavaModule.activity.cartonlabel.CartonPackingOrderListActivity;
import com.vritti.AlfaLavaModule.activity.grn.GRNPendingActivity;
import com.vritti.AlfaLavaModule.activity.loading.LoadingPackingOrderListActivity;
import com.vritti.AlfaLavaModule.activity.packaging.ReceiptPackagingDOListActivity;
import com.vritti.AlfaLavaModule.activity.packetenquiry.PacketEnquiryDetails;
import com.vritti.AlfaLavaModule.activity.packing_loading.LoadingPackOrderListActivity;
import com.vritti.AlfaLavaModule.activity.packing_qc.QCPackingOrderListActivity;
import com.vritti.AlfaLavaModule.activity.pick_riversal.PickReversalActivity;
import com.vritti.AlfaLavaModule.activity.picking.DOListActivity;
import com.vritti.AlfaLavaModule.activity.unpacking.UpPackingOrderListActivity;
import com.vritti.AlfaLavaModule.activity.weight.WeightPackingOrderListActivity;
import com.vritti.AlfaLavaModule.bean.MenuModel;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlfaHomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    private DrawerLayout drawer;
    String option;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(AlfaHomePage.this);
        String settingKey = ut.getSharedPreference_SettingKey(AlfaHomePage.this);
        String dabasename = ut.getValue(AlfaHomePage.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(AlfaHomePage.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(AlfaHomePage.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(AlfaHomePage.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(AlfaHomePage.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(AlfaHomePage.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(AlfaHomePage.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(AlfaHomePage.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(AlfaHomePage.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(AlfaHomePage.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {

            }
        } else {
        }
        requestRuntimePermission();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*SingleShotLocationProvider.requestSingleUpdate(AlfaHomePage.this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        Log.d("Location", "my location is " + location.toString());
                        Log.d("Location_lat", "my _lat is " + location.latitude);
                        Log.d("Location_lng", "my lng is " + location.longitude);
                    }
                });*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareMenuData() {

       /* MenuModel menuModel = new MenuModel("Home", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }*/

        /*if (Constants.type == Constants.Type.Alfa) {

        }else {
            MenuModel menuModel = new MenuModel("Pending GRN", true, false); //Menu of Java Tutorials
            headerList.add(menuModel);
        }*/


        MenuModel menuModel1 = new MenuModel("Location Transfer", true, false); //Menu of Java Tutorials
        headerList.add(menuModel1);


        MenuModel menuModel = new MenuModel("Inward", true, true); //Menu of Java Tutorials
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();

        MenuModel childModel = new MenuModel("GRN Putaway-Manual", false, false);
        childModelsList.add(childModel);

        if (Constants.type == Constants.Type.Alfa) {

        }else {
            childModel = new MenuModel("Pending GRN", false, false); //Menu of Java Tutorials
            childModelsList.add(childModel);
        }

        if (menuModel.hasChildren) {
            Log.d("API123","here");
            childList.put(menuModel, childModelsList);
        }


        childModelsList = new ArrayList<>();

       /* menuModel = new MenuModel("Production", true, true); //Menu of Python Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel("Picklist", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Putaway", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("MO Flowcard Picklist", false, false);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }*/

        childModelsList = new ArrayList<>();

        /*menuModel = new MenuModel("FG Stores", true, true); //Menu of Python Tutorials
        headerList.add(menuModel);
*/
        if (Constants.type == Constants.Type.Alfa) {

        }else {

            menuModel = new MenuModel("Physical Inventory", true, false); //Menu of Python Tutorials
            headerList.add(menuModel);
        }

        if (Constants.type == Constants.Type.Alfa) {

        }else {

            menuModel = new MenuModel("Packet Enquiry", true, false); //Menu of Python Tutorials
            headerList.add(menuModel);
        }
        if (Constants.type == Constants.Type.Alfa) {

        }else {
            menuModel = new MenuModel("Cutover Inventory", true, false); //Menu of Python Tutorials
            headerList.add(menuModel);
        }






        menuModel = new MenuModel("Shipping", true, true); //Menu of Python Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel("Picking", false, false);
        childModelsList.add(childModel);

        if (Constants.type == Constants.Type.Alfa) {

        }else {
            childModel = new MenuModel("Picking Reversal", true, false); //Menu of Python Tutorials
            childModelsList.add(childModel);
        }

        if (Constants.type == Constants.Type.Alfa) {

        }else {
            childModel = new MenuModel("Picking Order Creation", true, false); //Menu of Python Tutorials
            childModelsList.add(childModel);
        }

        childModel = new MenuModel("Receipt To Packaging", false, false);
        childModelsList.add(childModel);

        if (Constants.type == Constants.Type.Alfa) {
            childModel = new MenuModel("Inspection Sheet", false, false);
            childModelsList.add(childModel);
        }else {

            childModel = new MenuModel("Carton Label", false, false);
            childModelsList.add(childModel);
        }

        if (Constants.type == Constants.Type.Alfa) {
            childModel = new MenuModel("Carton Weight", false, false);
            childModelsList.add(childModel);
        }else {
            childModel = new MenuModel("Carton Weight", false, false);
            childModelsList.add(childModel);
        }

        if (Constants.type == Constants.Type.Alfa) {
            childModel = new MenuModel("Pending Shipment", false, false);
            childModelsList.add(childModel);
        }
        if (Constants.type == Constants.Type.Alfa) {
            childModel = new MenuModel("Carton Label", false, false);
            childModelsList.add(childModel);
        }

        if (Constants.type == Constants.Type.Alfa) {

        }else {
            childModel = new MenuModel("Unpacking", true, false); //Menu of Python Tutorials
            childModelsList.add(childModel);
        }

        if (Constants.type == Constants.Type.Alfa) {

        }else {
            childModel = new MenuModel("Packing QC", true, false); //Menu of Python Tutorials
            childModelsList.add(childModel);
        }


        if (Constants.type == Constants.Type.Alfa) {
            /*childModel = new MenuModel("Inspection Sheet", false, false);
            childModelsList.add(childModel);*/

        }else {
            childModel = new MenuModel("Loading Confirmation", false, false);
            childModelsList.add(childModel);
        }


        if (Constants.type == Constants.Type.Alfa) {

        }else {

            childModel = new MenuModel("Post Shipment", false, false);
            childModelsList.add(childModel);
        }


        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        menuModel = new MenuModel("Loading", true, false); //Menu of Python Tutorials
        headerList.add(menuModel);

      /*  childModel = new MenuModel("Secondary Packing Print List", false, false);
        childModelsList.add(childModel);
*/
        childModelsList = new ArrayList<>();
        menuModel = new MenuModel("Printing Report", true, true); //Menu of Python Tutorials
        headerList.add(menuModel);

        childModel = new MenuModel("Packing", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Shipping Inspection", false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel("Summary", false, false);
        childModelsList.add(childModel);


        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }





        menuModel = new MenuModel("Print Label", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);






        menuModel = new MenuModel("Logout", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);



        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(AlfaHomePage.this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        String group=headerList.get(groupPosition).menuName;
                        if (group.equalsIgnoreCase("Print Label")){
                            startActivity(new Intent(AlfaHomePage.this,LabelPrintData.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            //drawer.closeDrawers();
                        } else if (group.equalsIgnoreCase( "Physical Inventory")){
                            startActivity(new Intent(AlfaHomePage.this, BluetoothConnectivityActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }else if (group.equalsIgnoreCase( "Cutover Inventory")){
                            startActivity(new Intent(AlfaHomePage.this, LocationScannerCutoff.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }else if (group.equalsIgnoreCase( "Loading")){
                            startActivity(new Intent(AlfaHomePage.this, LoadingPackOrderListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                        else if (group.equalsIgnoreCase( "Packet Enquiry")){
                            startActivity(new Intent(AlfaHomePage.this, PacketEnquiryDetails.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                        else if (group.equalsIgnoreCase( "Location Transfer")){
                            startActivity(new Intent(AlfaHomePage.this, LocationOptionsSelection.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }


                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    String menu=model.menuName;

                    if (menu.equalsIgnoreCase("Receipt To Packaging")){
                        if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {
                            startActivity(new Intent(AlfaHomePage.this, DOPackingScanDetails.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        }else {

                           startActivity(new Intent(AlfaHomePage.this, ReceiptPackagingDOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                            // drawer.closeDrawers();
                        }

                    }else  if (menu.equalsIgnoreCase("Picking")){

                        startActivity(new Intent(AlfaHomePage.this, DOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                    //    startActivity(new Intent(AlfaHomePage.this, WifiListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        // startActivity(new Intent(AlfaHomePage.this,PickingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        //  drawer.closeDrawers();

                    }
                    else  if (menu.equalsIgnoreCase("Picking Order Creation")){

                        startActivity(new Intent(AlfaHomePage.this, PickListNoListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                        //    startActivity(new Intent(AlfaHomePage.this, WifiListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        // startActivity(new Intent(AlfaHomePage.this,PickingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        //  drawer.closeDrawers();

                    }
                    //Picking Order Creation
                    else  if (menu.equalsIgnoreCase("Picklist")){
                        startActivity(new Intent(AlfaHomePage.this,PickListDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        //  drawer.closeDrawers();
                    }
                    else  if (menu.equalsIgnoreCase("GRN Putaway-Manual")){


                      //  GetSetting();

                        startActivity(new Intent(AlfaHomePage.this,ActivityGRNPutAway.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        //  startActivity(new Intent(AlfaHomePage.this,ActivityGRNPutAway.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        //drawer.closeDrawers();

                    }

                    else  if (menu.equalsIgnoreCase("MO Flowcard Picklist")){
                        startActivity(new Intent(AlfaHomePage.this,MRSHeaderActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                    }else  if (menu.equalsIgnoreCase("Inspection Sheet")){
                        startActivity(new Intent(AlfaHomePage.this, InspectionPackingOrderListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                    }
                    else  if (menu.equalsIgnoreCase("Loading Confirmation")){
                        startActivity(new Intent(AlfaHomePage.this, LoadingPackingOrderListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                    }
                    else  if (menu.equalsIgnoreCase("Packing")){
                        startActivity(new Intent(AlfaHomePage.this,PackingDONOReportActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                    }
                    else  if (menu.equalsIgnoreCase("Shipping Inspection")){
                        startActivity(new Intent(AlfaHomePage.this,InspectionReportActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                    }
                    else  if (menu.equalsIgnoreCase("Summary")){
                        startActivity(new Intent(AlfaHomePage.this,SummaryDONOReportActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                    }
                    else  if (menu.equalsIgnoreCase("Post Shipment")){
                        startActivity(new Intent(AlfaHomePage.this,ShippingPostActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                    }
                    else  if (menu.equalsIgnoreCase("Carton Label")){
                        startActivity(new Intent(AlfaHomePage.this, CartonPackingOrderListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                    }
                    else  if (menu.equalsIgnoreCase("Carton Weight")){
                        startActivity(new Intent(AlfaHomePage.this, WeightPackingOrderListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                    }else if (menu.equalsIgnoreCase( "Unpacking")){
                        startActivity(new Intent(AlfaHomePage.this, UpPackingOrderListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                    else if (menu.equalsIgnoreCase( "Picking Reversal")){
                        startActivity(new Intent(AlfaHomePage.this, PickReversalActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                    else  if (menu.equalsIgnoreCase("Packing QC")){
                        startActivity(new Intent(AlfaHomePage.this, QCPackingOrderListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                    }
                    else  if (menu.equalsIgnoreCase("Pending Shipment")){
                        startActivity(new Intent(AlfaHomePage.this, DOShipmentListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                    }
                    else if (menu.equalsIgnoreCase( "Pending GRN")){
                        startActivity(new Intent(AlfaHomePage.this, GRNPendingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }

                }

                return false;
            }
        });
    }

    private void GetSetting() {

        final AppCompatRadioButton label_manual,label_hand;
        Button btn_yes;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AlfaHomePage.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.putaway_dialog_lay, null);
        dialogBuilder.setView(dialogView);


        label_manual=dialogView.findViewById(R.id.label_manual);
        label_hand=dialogView.findViewById(R.id.label_hand);
        btn_yes=dialogView.findViewById(R.id.btn_yes);

        if (label_manual.isChecked()){
            option = "0";
        }

        dialogBuilder.setCancelable(false);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        label_hand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (label_hand.isChecked()) {

                    option = "1";
                }else {

                }
            }
        });
        label_manual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (label_manual.isChecked()) {

                    option = "0";
                }
                else {

                }
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (option.equals("0")){

                    startActivity(new Intent(AlfaHomePage.this,ActivityGRNPutAway.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }else {
                  //  startActivity(new Intent(AlfaHomePage.this, GRNScanner.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    startActivity(new Intent(AlfaHomePage.this, LocationOptionsSelection.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
                b.dismiss();


            }
        });
        // if button is clicked, close the custom dialog

    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<MenuModel> listDataHeader;
        private HashMap<MenuModel, List<MenuModel>> listDataChild;

        public ExpandableListAdapter(Context context, List<MenuModel> listDataHeader,
                                     HashMap<MenuModel, List<MenuModel>> listChildData) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listDataChild = listChildData;
        }

        @Override
        public MenuModel getChild(int groupPosition, int childPosititon) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = getChild(groupPosition, childPosition).menuName;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_child, null);
            }

            TextView txtListChild = convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
                return 0;
            else
                return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                        .size();
        }

        @Override
        public MenuModel getGroup(int groupPosition) {
            return this.listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this.listDataHeader.size();

        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = getGroup(groupPosition).menuName;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_header, null);
            }

            TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
    public void requestRuntimePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(AlfaHomePage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AlfaHomePage.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
            case 201:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}