package com.vritti.crm.vcrm7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.crm.adapter.DataSheetListAdapter;
import com.vritti.crm.bean.DatasheetList;
import com.vritti.crm.bean.DatasheetListPojo;
import com.vritti.ekatm.R;
import com.vritti.vwb.vworkbench.AddDatasheetActivityMain;
import com.vritti.vwb.vworkbench.DatasheetMainActivity;
import com.vritti.vwb.vworkbench.EditDatasheetActivityMain;

import java.util.ArrayList;
import java.util.List;


public class DatasheetDisplayList extends AppCompatActivity {
    ListView list_datasheet;
    ArrayList<DatasheetList> datasheetDisplayLists = null;
    DataSheetListAdapter dataSheetListAdapter;
    String Clickitem="",ClickitemId="",SourceId="",FormId="",UseForProspect="",Result="",Mode="",PKSuspectId="",PkCssHeaderID="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datasheet_display_list);

        init();

        if(datasheetDisplayLists.isEmpty()){
            //
        }else {
            dataSheetListAdapter = new DataSheetListAdapter(DatasheetDisplayList.this,datasheetDisplayLists);
            list_datasheet.setAdapter(dataSheetListAdapter);
        }

        list_datasheet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 ClickitemId =datasheetDisplayLists.get(position).getCSSFormsCode();
                 Clickitem = datasheetDisplayLists.get(position).getCSSFormsDesc();
                 SourceId = datasheetDisplayLists.get(position).getPKSuspectId();
                 FormId = datasheetDisplayLists.get(position).getPKCssFormsId();
                 UseForProspect = datasheetDisplayLists.get(position).getUseForProspect();
                 Result= datasheetDisplayLists.get(position).getResult();
                 Mode = datasheetDisplayLists.get(position).getMode();
                 PKSuspectId = datasheetDisplayLists.get(position).getPKSuspectId();
                 PkCssHeaderID = datasheetDisplayLists.get(position).getPKCssHeaderId();


              if(ClickitemId .equalsIgnoreCase("null") || ClickitemId.equalsIgnoreCase(null) ||
                      ClickitemId.equalsIgnoreCase("")){

              }else{
                  if(Mode.equalsIgnoreCase("A")){

                      Intent intent = new Intent(DatasheetDisplayList.this, AddDatasheetActivityMain.class);
                      intent.putExtra("SourceId",SourceId);
                      intent.putExtra("FormId",FormId);
                      intent.putExtra("UseForProspect",UseForProspect);
                      intent.putExtra("Result",Result);
                      intent.putExtra("Mode",Mode);
                      intent.putExtra("PKSuspectId",PKSuspectId);
                      intent.putExtra("ActivityId",PKSuspectId);
                      intent.putExtra("PkCssHeaderID",PkCssHeaderID);
                      startActivity(intent);

                  }else if(Mode.equalsIgnoreCase("E")){
                      Intent intent = new Intent(DatasheetDisplayList.this, DatasheetMainActivity.class);
                      intent.putExtra("SourceId",SourceId);
                      intent.putExtra("FormId",FormId);
                      intent.putExtra("UseForProspect",UseForProspect);
                      intent.putExtra("Result",Result);
                      intent.putExtra("Mode",Mode);
                      intent.putExtra("PKSuspectId",PKSuspectId);
                      intent.putExtra("ActivityId",PKSuspectId);
                      intent.putExtra("PkCssHeaderID",PkCssHeaderID);
                      startActivity(intent);

                  }else {
                      Toast.makeText(DatasheetDisplayList.this, "No Datasheet", Toast.LENGTH_SHORT).show();
                  }
              }

            }
        });

    }


    public void init(){
        list_datasheet = findViewById(R.id.list_datasheet);

        datasheetDisplayLists = new ArrayList<DatasheetList>();

        if(getIntent()!= null){
            DatasheetListPojo datasheetListPojo = new Gson().fromJson(getIntent().getStringExtra("DatasheetList") ,
                    DatasheetListPojo.class);
            datasheetDisplayLists = datasheetListPojo.getDatasheetListArrayList();
            //SourceId = getIntent().getStringExtra("SourceId");
        }

    }



}
