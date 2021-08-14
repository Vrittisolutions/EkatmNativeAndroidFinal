package com.vritti.AlfaLavaModule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.ekatm.R;

public class PackagingActivity extends AppCompatActivity {

    ListView listView;
    TextView textView;
    String[] listItem;
    String ItemMasterId,Pack_OrdHdrId;
    private String BoxTypeMasterId,SoScheduleId="",Packetno="",PackOrderNo="";
    int qty=0,Packed=0;;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.packaging_lay);
        getSupportActionBar().setTitle("Packaging");

        listView=(ListView)findViewById(R.id.listView);
        textView=(TextView)findViewById(R.id.textView);
        listItem = getResources().getStringArray(R.array.array_packaging);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);



     //   ItemMasterId= getIntent().getStringExtra("itemid");
        Pack_OrdHdrId=getIntent().getStringExtra("packorder");
        BoxTypeMasterId=getIntent().getStringExtra("boxTypeMasterId");
      //  SoScheduleId=getIntent().getStringExtra("soScheduleId");
       // qty=getIntent().getIntExtra("qty",0);
       // Packetno=getIntent().getStringExtra("packetno");
        //PackOrderNo=getIntent().getStringExtra("dono");
        //Packed=getIntent().getIntExtra("Packed",0);





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value=adapter.getItem(position);

                if (value.equalsIgnoreCase("Create secondary pack")){

                    startActivity(new Intent(PackagingActivity.this,CreateSecondaryPackActivity.class).
                         //   putExtra("itemid",ItemMasterId).
                            putExtra("packorder", Pack_OrdHdrId).
                            putExtra("boxTypeMasterId",BoxTypeMasterId)
                           // putExtra("soScheduleId",SoScheduleId).
                            //putExtra("qty",qty).
                            //putExtra("packetno",Packetno).
                            //putExtra("dono", PackOrderNo).
                            //putExtra("Packed", Packed)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }

                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();

            }
        });
    }
}