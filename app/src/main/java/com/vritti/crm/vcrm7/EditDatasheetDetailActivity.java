package com.vritti.crm.vcrm7;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.bean.Datasheet;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

import java.util.StringTokenizer;
import java.util.UUID;


/**
 * Created by 300151 on 12/14/2016.
 */
public class EditDatasheetDetailActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;


    public String FKQuesId, QuesText, ValueMax, SelectionValue, Weightage;
    public String PKCssFormsQuesID, IsResponseMandatory, SelectionText,
            ValueMin, SelectionType;
    public String MaxValueText, ControlWidth, MaxNoOfResponses, ResponseType,
            Notes, FKPrimaryQuesId, FKSecondaryQuesId, IfResponseId,
            IsBranching, ExpectedResponse, DisableQuesStr, GroupID, GroupName,
            QuesCode, MaxExpectedResponse, ResponseValue, Answer = " ", Answer_Value = " ";
    public int SequenceNo, position, id, setFlag;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "vWorkbench";

    SQLiteDatabase sql;
    /* static Uri url;
     static File mediaFile;
     Bitmap bitmap;*/
    public static String filename, filepath, fileBase64Code, fileName;
    /*DatabaseHandler_Datasheet databaseHandler;*/
    Datasheet datasheet;
    public String stredittext, mobno;
    int i, getpos = 0;
    public int newId;
    String txt = null;
    int sid;
    String selection_Text, selection_Value, result, fid, qid;
    int selectionid;
    String txtvalue = null;
    String selectedRadioVal = null;
    // Cursor c;
    public static String radioselection_value, radiselection_text;
    public static int flag = 0;
    RadioButton rbutton1;
    int selected = -1;
    int width, height, btnwidth, btnwidth_fortwo;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    EditText edtAns;
    String detailid;
    // public DatasheetattachmentAdapter adapter;
    public static ListView listView;
    String fileToUpload, Upload, queid, lat, lang;
    // DatasheetAttachmentBean datasheetAttachmentBean;
    // Cursor cursor;
    //String FormId;
    int pid;
    //GPSTracker gps;
    public static double latitude, longitude;
    String SourceId, FormId, ActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        sql = db.getWritableDatabase();
        if (getIntent().hasExtra("FormId") && getIntent().hasExtra("FKQuesId")) {
            Intent intent = getIntent();
            FormId = intent.getStringExtra("FormId");
            position = intent.getIntExtra("position", 0);
            FKQuesId = intent.getStringExtra("FKQuesId");
            SourceId = intent.getStringExtra("SourceId");

        }

        InitData();
        Display display = EditDatasheetDetailActivity.this.getWindowManager()
                .getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        btnwidth = (width / 3) - 10;
        btnwidth_fortwo = (width / 2) - 10;
        Log.d("crm_dialog_action", "btn width " + btnwidth);


        setdata();
    }

    private void InitData() {
        String que = "SELECT * FROM " + db.TABLE_EDIT_DATASHEET + " WHERE FormId='" + FormId + "' AND FKQuesId='" + FKQuesId + "'";
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                SequenceNo = Integer.parseInt(cur.getString(cur.getColumnIndex("SequenceNo")));


                Answer = cur.getString(cur.getColumnIndex("ResponseByCustomer"));
                Answer_Value = cur.getString(cur.getColumnIndex("SelectionValue"));

                FKQuesId = cur.getString(cur.getColumnIndex("FKQuesId"));
                PKCssFormsQuesID = cur.getString(cur.getColumnIndex("PKCssFormsQuesID"));
                Weightage = cur.getString(cur.getColumnIndex("Weightage"));
                QuesText = cur.getString(cur.getColumnIndex("QuesText"));
                IsResponseMandatory = cur.getString(cur.getColumnIndex("IsResponseMandatory"));
                SelectionText = cur.getString(cur.getColumnIndex("SelectionText"));
                SelectionValue = cur.getString(cur.getColumnIndex("SelectionValue"));
                ValueMin = cur.getString(cur.getColumnIndex("ValueMin"));
                ValueMax = cur.getString(cur.getColumnIndex("ValueMax"));
                MaxValueText = cur.getString(cur.getColumnIndex("MaxValueText"));
                SelectionType = cur.getString(cur.getColumnIndex("SelectionType"));
                ControlWidth = cur.getString(cur.getColumnIndex("ControlWidth"));
                MaxNoOfResponses = cur.getString(cur.getColumnIndex("MaxNoOfResponses"));
                ResponseType = cur.getString(cur.getColumnIndex("ResponseType"));

                Notes = cur.getString(cur.getColumnIndex("Notes"));
                FKPrimaryQuesId = cur.getString(cur.getColumnIndex("FKPrimaryQuesId"));
                FKSecondaryQuesId = cur.getString(cur.getColumnIndex("FKSecondaryQuesId"));

                IsBranching = cur.getString(cur.getColumnIndex("IsBranching"));
                ExpectedResponse = cur.getString(cur.getColumnIndex("ExpectedResponse"));
                IfResponseId = cur.getString(cur.getColumnIndex("IfResponseId"));
                DisableQuesStr = cur.getString(cur.getColumnIndex("DisableQuesStr"));
                GroupID = cur.getString(cur.getColumnIndex("GroupID"));
                GroupName = cur.getString(cur.getColumnIndex("GroupName"));
                QuesCode = cur.getString(cur.getColumnIndex("QuesCode"));

                MaxExpectedResponse = cur.getString(cur.getColumnIndex("MaxExpectedResponse"));

                ResponseValue = cur.getString(cur.getColumnIndex("ResponseValue"));
            } while (cur.moveToNext());
        }


    }

    private void setdata() {

        /************************************* Text ****************************************************************/
        if (ResponseType.equals("Text")) {
            if (SelectionType.equals("Value")) {

                UUID uuid = UUID.randomUUID();
                detailid = uuid.toString();

                final TextView txtque = new TextView(this);
                RelativeLayout.LayoutParams paramstxt = new RelativeLayout.LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
                paramstxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                txtque.setLayoutParams(paramstxt);
                txtque.setText(QuesText);
                txtque.setTextSize(18);
                txtque.setPadding(10, 10, 10, 10);
                txtque.setTextColor(getResources().getColor(R.color.black));

                edtAns = new EditText(this);
                RelativeLayout.LayoutParams paramEdt = new RelativeLayout.LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

                edtAns.setLayoutParams(paramEdt);

                edtAns.setMaxWidth(200);
                //edtAns.setBackgroundResource(R.drawable.edittext_border);
                edtAns.setInputType(InputType.TYPE_CLASS_NUMBER);
                edtAns.setTextSize(18);
                edtAns.setHint("Enter answer");
                edtAns.setFocusable(true);

                String answer = EditDatasheetActivityMain.editDatasheetslist.get(position)
                        .getAnswer();

                if (!(answer == null || answer.equalsIgnoreCase(""))) {
                    if (getpos == position) {
                        edtAns.setText(answer);
                    } else if (getpos == position + 1) {

                        edtAns.setText("");

                    } else {

                        edtAns.setText(answer);
                    }

                }

                LinearLayout layoutque = new LinearLayout(this);
                layoutque.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                layoutque.setOrientation(LinearLayout.VERTICAL);
                layoutque.addView(txtque);

                layoutque.addView(edtAns);
                layoutque.setPadding(10, 10, 10, 10);

                layoutque.setId(Integer.parseInt("1"));
                setContentView(layoutque);

                final Button btnQR = new Button(this);
                RelativeLayout.LayoutParams parambtnQR = new RelativeLayout.LayoutParams(
                        btnwidth - 10, 40);

                // parambtnQR.setMargins(10, 10, 10, 10);
                btnQR.setGravity(Gravity.CENTER);
                btnQR.setLayoutParams(parambtnQR);
                // btnQR.setText("QR Code");

                btnQR.setBackgroundResource(R.drawable.ic_qr);
                // btnQR.setTextColor(getResources().getColor(R.color.white));
                // btnPrevious.setLeft(10);

                btnQR.setId(Integer.parseInt("12"));

                final Button btnBarcode = new Button(this);
                RelativeLayout.LayoutParams parambtnBarcode = new RelativeLayout.LayoutParams(
                        btnwidth - 10, 40);

                // parambtnBarcode.setMargins(10, 10, 10, 10);

                btnBarcode.setLayoutParams(parambtnBarcode);
                // btnBarcode.setText("Barcode");
                btnBarcode.setGravity(Gravity.CENTER);
                btnBarcode.setBackgroundResource(R.drawable.ic_barcode);
                // btnBarcode.setTextColor(getResources().getColor(R.color.white));

                btnBarcode.setId(Integer.parseInt("13"));

                LinearLayout layoutQR = new LinearLayout(this);
                layoutQR.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutQR.setOrientation(LinearLayout.VERTICAL);
                layoutQR.setGravity(Gravity.CENTER);
                layoutQR.addView(btnQR);

                layoutQR.setPadding(10, 10, 10, 10);

                LinearLayout layoutBarcode = new LinearLayout(this);
                layoutBarcode.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutBarcode.setOrientation(LinearLayout.VERTICAL);
                layoutBarcode.setGravity(Gravity.CENTER);
                layoutBarcode.addView(btnBarcode);

                layoutBarcode.setPadding(10, 10, 10, 10);

                LinearLayout layoutbtnCode = new LinearLayout(this);
                layoutbtnCode.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                layoutbtnCode.setOrientation(LinearLayout.HORIZONTAL);

                layoutbtnCode.setGravity(Gravity.CENTER);
                layoutbtnCode.addView(layoutQR);
                layoutbtnCode.addView(layoutBarcode);

                layoutbtnCode.setPadding(10, 10, 10, 10);
                setContentView(layoutque);

                final Button btnPrevious = new Button(this);
                RelativeLayout.LayoutParams parambtnsave = new RelativeLayout.LayoutParams(
                        btnwidth, LayoutParams.WRAP_CONTENT);

                // parambtnsave.setMargins(10, 10, 10, 10);
                btnPrevious.setLayoutParams(parambtnsave);
                btnPrevious.setText("Previous");
                btnPrevious.setBackgroundColor(getResources().getColor(
                        R.color.btnPrv));
                btnPrevious
                        .setTextColor(getResources().getColor(R.color.white));
                // btnPrevious.setLeft(10);

                btnPrevious.setId(Integer.parseInt("1007"));

                final Button btnNext = new Button(this);
                RelativeLayout.LayoutParams paramsNext = new RelativeLayout.LayoutParams(
                        btnwidth, LayoutParams.WRAP_CONTENT);
                // paramsNext.setMargins(10, 10, 10, 10);
                btnNext.setLayoutParams(paramsNext);
                btnNext.setText("Next");
                btnNext.setTextColor(getResources().getColor(R.color.white));

                btnNext.setBackgroundColor(getResources().getColor(R.color.btnNext));
                btnNext.setId(Integer.parseInt("1008"));

                Button btnReturn = new Button(this);
                RelativeLayout.LayoutParams paramsReturn = new RelativeLayout.LayoutParams(
                        btnwidth, LayoutParams.WRAP_CONTENT);
                // paramsReturn.setMargins(10, 10, 10, 10);
                btnReturn.setLayoutParams(paramsReturn);
                btnReturn.setText("Return");
                // btnReturn.setLeft(10);

                btnReturn.setTextColor(getResources().getColor(R.color.white));

                btnReturn.setBackgroundColor(getResources().getColor(
                        R.color.btnPrv));
                btnReturn.setId(Integer.parseInt("1009"));

                LinearLayout layoutP = new LinearLayout(this);
                layoutP.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutP.setOrientation(LinearLayout.VERTICAL);

                layoutP.addView(btnPrevious);

                layoutP.setGravity(Gravity.CENTER);
                layoutP.setPadding(0, 10, 10, 10);

                LinearLayout layoutN = new LinearLayout(this);
                layoutN.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutN.setOrientation(LinearLayout.HORIZONTAL);
                // layoutans.addRule(RelativeLayout.LEFT_OF, 1001);

                layoutN.addView(btnNext);

                layoutN.setGravity(Gravity.CENTER);
                layoutN.setPadding(0, 10, 10, 10);

                // layoutque.addView(layoutans);

                LinearLayout layoutR = new LinearLayout(this);
                layoutR.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutR.setOrientation(LinearLayout.HORIZONTAL);

                layoutR.addView(btnReturn);
                layoutR.setGravity(Gravity.CENTER);
                layoutR.setPadding(0, 10, 0, 10);

                LinearLayout layoutans = new LinearLayout(this);
                layoutans.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                layoutans.setOrientation(LinearLayout.HORIZONTAL);
                layoutans.setPadding(10, 10, 10, 10);
                layoutans.addView(layoutP);
                layoutans.addView(layoutN);
                layoutans.addView(layoutR);
                layoutans.setGravity(Gravity.CENTER);
                // setContentView(layoutans);

                final Button btnAttach = new Button(this);
                RelativeLayout.LayoutParams parambtnAttach = new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                // parambtnAttach.setMargins(10, 10, 10, 10);

                btnAttach.setLayoutParams(parambtnAttach);
                btnAttach.setText("Attach");

                btnAttach.setBackgroundColor(getResources().getColor(
                        R.color.btn));
                btnAttach.setTextColor(getResources().getColor(R.color.white));
                // btnPrevious.setLeft(10);

                btnAttach.setId(Integer.parseInt("11"));

                LinearLayout layoutbtnatch = new LinearLayout(this);
                layoutbtnatch.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                layoutbtnatch.setOrientation(LinearLayout.VERTICAL);

                layoutbtnatch.addView(btnAttach);

                layoutbtnatch.setPadding(10, 10, 10, 10);

                View view = new View(this);
                view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                        2));
                view.setPadding(10, 10, 10, 20);
                view.setBackgroundColor(getResources()
                        .getColor(R.color.divider));

                listView = new ListView(this);
                RelativeLayout.LayoutParams paramlist = new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                listView.setLayoutParams(paramlist);

                LinearLayout layoutlist = new LinearLayout(this);
                layoutlist.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutlist.setOrientation(LinearLayout.VERTICAL);

                layoutlist.addView(listView);

                layoutlist.setPadding(10, 10, 10, 10);

                LinearLayout layoutBtn = new LinearLayout(this);
                layoutBtn.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT));
                layoutBtn.setOrientation(LinearLayout.VERTICAL);
                layoutBtn.addView(layoutbtnCode);
                layoutBtn.addView(layoutans);
                layoutBtn.addView(view);
                layoutBtn.addView(layoutbtnatch);
                layoutBtn.addView(layoutlist);
                layoutque.addView(layoutBtn);
                // getPhotoFromDatabase();
                btnAttach.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                       /* Intent intent = new Intent(
                                DatasheetDetailsActivity.this,
                                DatasheetAttachmentActivity.class);
                        intent.putExtra("PKCssFormsQuesID", PKCssFormsQuesID);
                        intent.putExtra("DetailId", detailid);
                        startActivity(intent);*/
                    }
                });

                btnQR.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        try {
                            Intent intent = new Intent(ACTION_SCAN);
                            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                            startActivityForResult(intent, 0);
                        } catch (ActivityNotFoundException anfe) {
                           /* showDialog(DatasheetDetailsActivity.this,
                                    "No Scanner Found",
                                    "Download a scanner code custom_travel_plan_show?", "Yes",
                                    "No").show();*/
                        }
                    }
                });

                btnBarcode.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        try {
                            Intent intent = new Intent(ACTION_SCAN);
                            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                            startActivityForResult(intent, 0);
                        } catch (ActivityNotFoundException anfe) {
                           /* showDialog(DatasheetDetailsActivity.this,
                                    "No Scanner Found",
                                    "Download a scanner code custom_travel_plan_show?", "Yes",
                                    "No").show();*/
                        }
                    }
                });
                if (position == 0) {
                    btnPrevious.setVisibility(View.INVISIBLE);
                } else {
                    btnPrevious.setVisibility(View.VISIBLE);
                }
                btnPrevious.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        radiselection_text = "";
                        newId = position - 1;
                        if (newId > -1) {

                            Answer = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getAnswer();
                            Answer_Value = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getAnswer_value();
                            detailid = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getDetailid();
                            setFlag = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getFlag();

                            FKQuesId = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getFKQuesId();
                            QuesText = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getQuesText();
                            ValueMax = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getValueMax();

                            PKCssFormsQuesID = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getPKCssFormsQuesID();
                            Weightage = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getWeightage();

                            IsResponseMandatory = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getIsResponseMandatory();
                            SelectionText = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionText();
                            SelectionValue = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionValue();
                            ValueMin = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getValueMin();

                            MaxValueText = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxValueText();
                            SelectionType = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionType();
                            ControlWidth = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getControlWidth();
                            MaxNoOfResponses = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxNoOfResponses();
                            ResponseType = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getResponseType();

                            if (ResponseType.equals("Numeric")
                                    && ResponseType.equals("Text")) {
                                if (!(Answer == null && Answer.equals(""))) {
                                    if (newId == position) {
                                        edtAns.setText("");
                                    } else {
                                        edtAns.setText(Answer);
                                    }
                                }
                            } else {

                            }
                            Notes = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                    .getNotes();
                            FKPrimaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getFKPrimaryQuesId();
                            FKSecondaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getFKSecondaryQuesId();

                            IsBranching = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getIsBranching();
                            ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getExpectedResponse();
                            IfResponseId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getIfResponseId();
                            DisableQuesStr = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getDisableQuesStr();
                            GroupID = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getGroupID();
                            GroupName = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getGroupName();
                            QuesCode = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getQuesCode();

                            MaxExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxExpectedResponse();

                            ResponseValue = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getResponseValue();
                            ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getExpectedResponse();

                            txtque.setText(QuesText);

                            position = position - 1;
                            newId = position;
                            getpos = newId;
                            setdata();
                        } else if (newId < 0) {
                            btnPrevious.setVisibility(View.INVISIBLE);
                        }

                    }

                });

                btnNext.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        radiselection_text = "";

                        if (edtAns.getText().toString().equals("")
                                && IsResponseMandatory.equals("Y")) {
                            // if (IsResponseMandatory.equals("Y")) {
                            flag = 0;
                            Toast.makeText(getApplicationContext(),
                                    "Please Answer the Question.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            // radiselection_text="";
                            flag = 1;
                            radiselection_text = edtAns.getText().toString();
                            Answer_Value = "-1";
                            if (ResponseType.equals("Text")) {


                                cf.addDatasheetANS(new Datasheet(
                                        PKCssFormsQuesID, FKQuesId,
                                        radiselection_text,
                                        Answer_Value, flag, FormId));

                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setFlag(flag);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setDetailid(detailid);

                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setAnswer(radiselection_text);

                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setAnswer_value(Answer_Value);

                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setFKQuesId(FKQuesId);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setSequenceNo(SequenceNo);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setQuesText(QuesText);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setValueMax(ValueMax);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setSelectionValue(SelectionValue);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setWeightage(Weightage);

                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setPKCssFormsQuesID(PKCssFormsQuesID);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setIsResponseMandatory(
                                                IsResponseMandatory);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setSelectionText(SelectionText);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setMaxValueText(MaxValueText);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setControlWidth(ControlWidth);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setMaxNoOfResponses(MaxNoOfResponses);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setResponseType(ResponseType);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setNotes(Notes);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setFKPrimaryQuesId(FKPrimaryQuesId);
                                EditDatasheetActivityMain.editDatasheetslist
                                        .get(position)
                                        .setExpectedResponse(radiselection_text);
                                EditDatasheetActivityMain.editDatasheetslist
                                        .get(position)
                                        .setFKSecondaryQuesId(FKSecondaryQuesId);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setDisableQuesStr(DisableQuesStr);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setGroupID(GroupID);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setGroupName(GroupName);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setQuesCode(QuesCode);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setMaxExpectedResponse(
                                                MaxExpectedResponse);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setResponseValue(ResponseValue);

                            }
                            newId = position + 1;

                            if (newId <= EditDatasheetActivityMain.editDatasheetslist
                                    .size() - 1) {

                                setFlag = EditDatasheetActivityMain.editDatasheetslist.get(
                                        newId).getFlag();
                                Answer = EditDatasheetActivityMain.editDatasheetslist.get(
                                        newId).getAnswer();
                                detailid = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getDetailid();
                                Answer_Value = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getAnswer_value();
                                FKQuesId = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getFKQuesId();
                                QuesText = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getQuesText();
                                ValueMax = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getValueMax();

                                PKCssFormsQuesID = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getPKCssFormsQuesID();
                                Weightage = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getWeightage();

                                IsResponseMandatory = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getIsResponseMandatory();
                                SelectionText = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getSelectionText();
                                SelectionValue = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getSelectionValue();
                                ValueMin = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getValueMin();

                                MaxValueText = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getMaxValueText();
                                SelectionType = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getSelectionType();
                                ControlWidth = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getControlWidth();
                                MaxNoOfResponses = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getMaxNoOfResponses();
                                ResponseType = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getResponseType();

                                Notes = EditDatasheetActivityMain.editDatasheetslist.get(
                                        newId).getNotes();
                                FKPrimaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getFKPrimaryQuesId();
                                FKSecondaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getFKSecondaryQuesId();

                                IsBranching = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getIsBranching();
                                ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getExpectedResponse();
                                IfResponseId = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getIfResponseId();
                                DisableQuesStr = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getDisableQuesStr();
                                GroupID = EditDatasheetActivityMain.editDatasheetslist.get(
                                        newId).getGroupID();
                                GroupName = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getGroupName();
                                QuesCode = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getQuesCode();

                                MaxExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getMaxExpectedResponse();

                                ResponseValue = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getResponseValue();
                                ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getExpectedResponse();
                                txtque.setText(QuesText);

                                if (ResponseType.equals("Text")) {
                                    radiselection_text = "";
                                    edtAns.setText("");
                                } else if (ResponseType.equals("Numeric")) {
                                    radiselection_text = "";
                                    edtAns.setText(Answer);
                                } else {
                                    radiselection_text = "";
                                }
                                // SequenceNo = newId + 1;
                                position = position + 1;
                                newId = position;
                                getpos = newId;
                                setdata();
                            } else {
                                btnNext.setVisibility(View.INVISIBLE);
                            }

                        }
                    }

                });

                btnReturn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // flag = 0;
                        // getposition = position;
                        EditDatasheetDetailActivity.this.finish();

                    }
                });

            } else {
                UUID uuid = UUID.randomUUID();
                detailid = uuid.toString();

                final TextView txtque = new TextView(this);
                RelativeLayout.LayoutParams paramstxt = new RelativeLayout.LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
                paramstxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                txtque.setLayoutParams(paramstxt);
                txtque.setText(QuesText);
                txtque.setTextSize(18);
                txtque.setPadding(10, 10, 10, 10);
                txtque.setTextColor(getResources().getColor(R.color.black));
                edtAns = new EditText(this);
                RelativeLayout.LayoutParams paramEdt = new RelativeLayout.LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

                edtAns.setLayoutParams(paramEdt);
                edtAns.setHint("Enter answer");
                edtAns.setMaxWidth(200);
                edtAns.setTextSize(18);
                //  edtAns.setBackgroundResource(R.drawable.edittext_border);
                edtAns.setPadding(10, 10, 10, 10);
                // edtAns.setFocusable(true);
                String answer = EditDatasheetActivityMain.editDatasheetslist.get(position)
                        .getAnswer();

                if (!(answer == null || answer.equalsIgnoreCase(""))) {
                    if (getpos == position) {
                        edtAns.setText(answer);
                    } else if (getpos == position + 1) {

                        edtAns.setText("");

                    } else {

                        edtAns.setText(answer);
                    }

                }

                LinearLayout layoutque = new LinearLayout(this);
                layoutque.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                layoutque.setOrientation(LinearLayout.VERTICAL);
                layoutque.addView(txtque);

                layoutque.addView(edtAns);
                layoutque.setPadding(10, 10, 10, 10);

                layoutque.setId(Integer.parseInt("1"));
                setContentView(layoutque);

                final Button btnQR = new Button(this);
                RelativeLayout.LayoutParams parambtnQR = new RelativeLayout.LayoutParams(
                        btnwidth - 10, 40);

                // parambtnQR.setMargins(10, 10, 10, 10);
                btnQR.setGravity(Gravity.CENTER);
                btnQR.setLayoutParams(parambtnQR);
                // btnQR.setText("QR Code");

                btnQR.setBackgroundResource(R.drawable.ic_qr);
                // btnQR.setTextColor(getResources().getColor(R.color.white));
                // btnPrevious.setLeft(10);

                btnQR.setId(Integer.parseInt("12"));

                final Button btnBarcode = new Button(this);
                RelativeLayout.LayoutParams parambtnBarcode = new RelativeLayout.LayoutParams(
                        btnwidth - 10, 40);

                // parambtnBarcode.setMargins(10, 10, 10, 10);

                btnBarcode.setLayoutParams(parambtnBarcode);
                // btnBarcode.setText("Barcode");
                btnBarcode.setGravity(Gravity.CENTER);
                btnBarcode.setBackgroundResource(R.drawable.ic_barcode);
                // btnBarcode.setTextColor(getResources().getColor(R.color.white));

                btnBarcode.setId(Integer.parseInt("13"));

                LinearLayout layoutQR = new LinearLayout(this);
                layoutQR.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutQR.setOrientation(LinearLayout.VERTICAL);
                layoutQR.setGravity(Gravity.CENTER);
                layoutQR.addView(btnQR);

                layoutQR.setPadding(10, 10, 10, 10);

                LinearLayout layoutBarcode = new LinearLayout(this);
                layoutBarcode.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutBarcode.setOrientation(LinearLayout.VERTICAL);
                layoutBarcode.setGravity(Gravity.CENTER);
                layoutBarcode.addView(btnBarcode);

                layoutBarcode.setPadding(10, 10, 10, 10);

                LinearLayout layoutbtnCode = new LinearLayout(this);
                layoutbtnCode.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                layoutbtnCode.setOrientation(LinearLayout.HORIZONTAL);

                layoutbtnCode.setGravity(Gravity.CENTER);
                layoutbtnCode.addView(layoutQR);
                layoutbtnCode.addView(layoutBarcode);

                layoutbtnCode.setPadding(10, 10, 10, 10);
                setContentView(layoutque);

                final Button btnPrevious = new Button(this);
                RelativeLayout.LayoutParams parambtnsave = new RelativeLayout.LayoutParams(
                        btnwidth, LayoutParams.WRAP_CONTENT);

                // parambtnsave.setMargins(10, 10, 10, 10);
                btnPrevious.setLayoutParams(parambtnsave);
                btnPrevious.setText("Previous");
                btnPrevious.setBackgroundColor(getResources().getColor(
                        R.color.btnPrv));
                btnPrevious
                        .setTextColor(getResources().getColor(R.color.white));
                // btnPrevious.setLeft(10);

                btnPrevious.setId(Integer.parseInt("1007"));

                final Button btnNext = new Button(this);
                RelativeLayout.LayoutParams paramsNext = new RelativeLayout.LayoutParams(
                        btnwidth, LayoutParams.WRAP_CONTENT);
                // paramsNext.setMargins(10, 10, 10, 10);
                btnNext.setLayoutParams(paramsNext);
                btnNext.setText("Next");
                btnNext.setTextColor(getResources().getColor(R.color.white));

                btnNext.setBackgroundColor(getResources().getColor(R.color.btnNext));
                btnNext.setId(Integer.parseInt("1008"));

                Button btnReturn = new Button(this);
                RelativeLayout.LayoutParams paramsReturn = new RelativeLayout.LayoutParams(
                        btnwidth, LayoutParams.WRAP_CONTENT);
                // paramsReturn.setMargins(10, 10, 10, 10);
                btnReturn.setLayoutParams(paramsReturn);
                btnReturn.setText("Return");
                // btnReturn.setLeft(10);

                btnReturn.setTextColor(getResources().getColor(R.color.white));

                btnReturn.setBackgroundColor(getResources().getColor(
                        R.color.btnPrv));
                btnReturn.setId(Integer.parseInt("1009"));

                LinearLayout layoutP = new LinearLayout(this);
                layoutP.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutP.setOrientation(LinearLayout.VERTICAL);

                layoutP.addView(btnPrevious);

                layoutP.setGravity(Gravity.CENTER);
                layoutP.setPadding(0, 10, 10, 10);

                LinearLayout layoutN = new LinearLayout(this);
                layoutN.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutN.setOrientation(LinearLayout.HORIZONTAL);
                // layoutans.addRule(RelativeLayout.LEFT_OF, 1001);

                layoutN.addView(btnNext);

                layoutN.setGravity(Gravity.CENTER);
                layoutN.setPadding(0, 10, 10, 10);

                // layoutque.addView(layoutans);

                LinearLayout layoutR = new LinearLayout(this);
                layoutR.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutR.setOrientation(LinearLayout.HORIZONTAL);

                layoutR.addView(btnReturn);
                layoutR.setGravity(Gravity.CENTER);
                layoutR.setPadding(0, 10, 0, 10);

                LinearLayout layoutans = new LinearLayout(this);
                layoutans.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                layoutans.setOrientation(LinearLayout.HORIZONTAL);
                layoutans.setPadding(10, 10, 10, 10);
                layoutans.addView(layoutP);
                layoutans.addView(layoutN);
                layoutans.addView(layoutR);
                layoutans.setGravity(Gravity.CENTER);
                // setContentView(layoutans);

                final Button btnAttach = new Button(this);
                RelativeLayout.LayoutParams parambtnAttach = new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                // parambtnAttach.setMargins(10, 10, 10, 10);

                btnAttach.setLayoutParams(parambtnAttach);
                btnAttach.setText("Attach");

                btnAttach.setBackgroundColor(getResources().getColor(
                        R.color.btn));
                btnAttach.setTextColor(getResources().getColor(R.color.white));
                // btnPrevious.setLeft(10);

                btnAttach.setId(Integer.parseInt("11"));

                LinearLayout layoutbtnatch = new LinearLayout(this);
                layoutbtnatch.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                layoutbtnatch.setOrientation(LinearLayout.VERTICAL);

                layoutbtnatch.addView(btnAttach);

                layoutbtnatch.setPadding(10, 10, 10, 10);

                View view = new View(this);
                view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                        2));
                view.setPadding(10, 10, 10, 20);
                view.setBackgroundColor(getResources()
                        .getColor(R.color.divider));

                listView = new ListView(this);
                RelativeLayout.LayoutParams paramlist = new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                listView.setLayoutParams(paramlist);

                LinearLayout layoutlist = new LinearLayout(this);
                layoutlist.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutlist.setOrientation(LinearLayout.VERTICAL);

                layoutlist.addView(listView);

                layoutlist.setPadding(10, 10, 10, 10);

                LinearLayout layoutBtn = new LinearLayout(this);
                layoutBtn.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT));
                layoutBtn.setOrientation(LinearLayout.VERTICAL);
                layoutBtn.addView(layoutbtnCode);
                layoutBtn.addView(layoutans);
                layoutBtn.addView(view);
                layoutBtn.addView(layoutbtnatch);
                layoutBtn.addView(layoutlist);
                layoutque.addView(layoutBtn);
                //getPhotoFromDatabase();
                btnAttach.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        /*Intent intent = new Intent(
                                DatasheetDetailsActivity.this,
                                DatasheetAttachmentActivity.class);
                        intent.putExtra("PKCssFormsQuesID", PKCssFormsQuesID);
                        intent.putExtra("DetailId", detailid);
                        startActivity(intent);*/
                    }
                });

                btnQR.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        try {
                            Intent intent = new Intent(ACTION_SCAN);
                            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                            startActivityForResult(intent, 0);
                        } catch (ActivityNotFoundException anfe) {
                           /* showDialog(DatasheetDetailsActivity.this,
                                    "No Scanner Found",
                                    "Download a scanner code custom_travel_plan_show?", "Yes",
                                    "No").show();*/
                        }
                    }
                });

                btnBarcode.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        try {
                            Intent intent = new Intent(ACTION_SCAN);
                            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                            startActivityForResult(intent, 0);
                        } catch (ActivityNotFoundException anfe) {
                           /* showDialog(DatasheetDetailsActivity.this,
                                    "No Scanner Found",
                                    "Download a scanner code custom_travel_plan_show?", "Yes",
                                    "No").show();*/
                        }
                    }
                });
                if (position == 0) {
                    btnPrevious.setVisibility(View.INVISIBLE);
                } else {
                    btnPrevious.setVisibility(View.VISIBLE);
                }
                btnPrevious.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        radiselection_text = "";
                        newId = position - 1;
                        if (newId > -1) {

                            Answer = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getAnswer();
                            Answer_Value = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getAnswer_value();
                            detailid = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getDetailid();
                            setFlag = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getFlag();

                            FKQuesId = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getFKQuesId();
                            QuesText = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getQuesText();
                            ValueMax = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getValueMax();

                            PKCssFormsQuesID = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getPKCssFormsQuesID();
                            Weightage = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getWeightage();

                            IsResponseMandatory = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getIsResponseMandatory();
                            SelectionText = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionText();
                            SelectionValue = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionValue();
                            ValueMin = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getValueMin();

                            MaxValueText = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxValueText();
                            SelectionType = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionType();
                            ControlWidth = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getControlWidth();
                            MaxNoOfResponses = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxNoOfResponses();
                            ResponseType = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getResponseType();

                            if (ResponseType.equals("Numeric")
                                    && ResponseType.equals("Text")) {
                                if (!(Answer == null && Answer.equals(""))) {
                                    if (newId == position) {
                                        edtAns.setText("");
                                    } else {
                                        edtAns.setText(Answer);
                                    }
                                }
                            } else {

                            }
                            Notes = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                    .getNotes();
                            FKPrimaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getFKPrimaryQuesId();
                            FKSecondaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getFKSecondaryQuesId();

                            IsBranching = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getIsBranching();
                            ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getExpectedResponse();
                            IfResponseId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getIfResponseId();
                            DisableQuesStr = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getDisableQuesStr();
                            GroupID = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getGroupID();
                            GroupName = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getGroupName();
                            QuesCode = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getQuesCode();

                            MaxExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxExpectedResponse();

                            ResponseValue = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getResponseValue();
                            ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getExpectedResponse();

                            txtque.setText(QuesText);

                            position = position - 1;
                            newId = position;
                            getpos = newId;
                            setdata();
                        } else if (newId < 0) {
                            btnPrevious.setVisibility(View.INVISIBLE);
                        }

                    }

                });

                btnNext.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        radiselection_text = "";

                        if (edtAns.getText().toString().equals("")
                                && IsResponseMandatory.equals("Y")) {
                            // if (IsResponseMandatory.equals("Y")) {
                            flag = 0;
                            Toast.makeText(getApplicationContext(),
                                    "Please Answer the Question.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            // radiselection_text="";
                            flag = 1;
                            radiselection_text = edtAns.getText().toString();
                            Answer_Value = "-1";
                            if (ResponseType.equals("Text")) {


                                cf.addDatasheetANS(new Datasheet(
                                        PKCssFormsQuesID, FKQuesId,
                                        radiselection_text,
                                        Answer_Value, flag, FormId));

                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setFlag(flag);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setDetailid(detailid);
                                //  EditDatasheetActivityMain.editDatasheetslist.get(position).setAnswer(radiselection_text);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setAnswer(radiselection_text);
                                EditDatasheetActivityMain.editDatasheetslist.get(position).setResponsebycustomer(radiselection_text);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setAnswer_value(Answer_Value);

                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setFKQuesId(FKQuesId);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setSequenceNo(SequenceNo);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setQuesText(QuesText);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setValueMax(ValueMax);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setSelectionValue(SelectionValue);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setWeightage(Weightage);

                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setPKCssFormsQuesID(PKCssFormsQuesID);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setIsResponseMandatory(
                                                IsResponseMandatory);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setSelectionText(SelectionText);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setMaxValueText(MaxValueText);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setControlWidth(ControlWidth);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setMaxNoOfResponses(MaxNoOfResponses);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setResponseType(ResponseType);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setNotes(Notes);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setFKPrimaryQuesId(FKPrimaryQuesId);
                                EditDatasheetActivityMain.editDatasheetslist
                                        .get(position)
                                        .setExpectedResponse(radiselection_text);
                                EditDatasheetActivityMain.editDatasheetslist
                                        .get(position)
                                        .setFKSecondaryQuesId(FKSecondaryQuesId);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setDisableQuesStr(DisableQuesStr);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setGroupID(GroupID);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setGroupName(GroupName);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setQuesCode(QuesCode);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setMaxExpectedResponse(
                                                MaxExpectedResponse);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setResponseValue(ResponseValue);

                            }
                            newId = position + 1;

                            if (newId <= EditDatasheetActivityMain.editDatasheetslist
                                    .size() - 1) {

                                setFlag = EditDatasheetActivityMain.editDatasheetslist.get(
                                        newId).getFlag();
                                Answer = EditDatasheetActivityMain.editDatasheetslist.get(
                                        newId).getAnswer();
                                detailid = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getDetailid();
                                Answer_Value = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getAnswer_value();
                                FKQuesId = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getFKQuesId();
                                QuesText = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getQuesText();
                                ValueMax = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getValueMax();

                                PKCssFormsQuesID = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getPKCssFormsQuesID();
                                Weightage = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getWeightage();

                                IsResponseMandatory = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getIsResponseMandatory();
                                SelectionText = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getSelectionText();
                                SelectionValue = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getSelectionValue();
                                ValueMin = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getValueMin();

                                MaxValueText = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getMaxValueText();
                                SelectionType = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getSelectionType();
                                ControlWidth = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getControlWidth();
                                MaxNoOfResponses = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getMaxNoOfResponses();
                                ResponseType = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getResponseType();

                                Notes = EditDatasheetActivityMain.editDatasheetslist.get(
                                        newId).getNotes();
                                FKPrimaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getFKPrimaryQuesId();
                                FKSecondaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getFKSecondaryQuesId();

                                IsBranching = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getIsBranching();
                                ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getExpectedResponse();
                                IfResponseId = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getIfResponseId();
                                DisableQuesStr = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getDisableQuesStr();
                                GroupID = EditDatasheetActivityMain.editDatasheetslist.get(
                                        newId).getGroupID();
                                GroupName = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getGroupName();
                                QuesCode = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getQuesCode();

                                MaxExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getMaxExpectedResponse();

                                ResponseValue = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getResponseValue();
                                ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                        .get(newId).getExpectedResponse();
                                txtque.setText(QuesText);

                                if (ResponseType.equals("Text")) {
                                    radiselection_text = "";
                                    edtAns.setText("");
                                } else if (ResponseType.equals("Numeric")) {
                                    radiselection_text = "";
                                    edtAns.setText(Answer);
                                } else {
                                    radiselection_text = "";
                                }
                                // SequenceNo = newId + 1;
                                position = position + 1;
                                newId = position;
                                getpos = newId;
                                setdata();
                            } else {
                                btnNext.setVisibility(View.INVISIBLE);
                            }

                        }

                    }

                });

                btnReturn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // flag = 0;
                        // getposition = position;
                        EditDatasheetDetailActivity.this.finish();

                    }
                });
            }
        }
        /*************************************
         * Selection
         ********/
        else if (ResponseType.equals("Selection")) {
            // if (SelectionType.equals("Radio button list")) {
            UUID uuid = UUID.randomUUID();
            detailid = uuid.toString();
            int totalLength = 0;
            String token = null;
            final StringTokenizer st = new StringTokenizer(SelectionText, "|");

            final int sentenceCount = st.countTokens();
            Log.d("crm_dialog_action", "sentenceCount:" + sentenceCount);

            final StringTokenizer stvalue = new StringTokenizer(SelectionValue,
                    "|");

            final int stvaluecount = stvalue.countTokens();
            Log.d("crm_dialog_action", "stvaluecount:" + stvaluecount);

            final TextView txtque = new TextView(this);
            RelativeLayout.LayoutParams paramstxt = new RelativeLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            paramstxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            txtque.setLayoutParams(paramstxt);
            txtque.setText(QuesText);
            txtque.setTextSize(18);
            txtque.setPadding(10, 10, 10, 10);
            txtque.setTextColor(getResources().getColor(R.color.black));
            // Log.d("crm_dialog_action", "no:" + selection_text[1]);

            final Button btnPrevious = new Button(this);
            RelativeLayout.LayoutParams parambtnsave = new RelativeLayout.LayoutParams(
                    btnwidth, LayoutParams.WRAP_CONTENT);

            // parambtnsave.setMargins(10, 10, 10, 10);
            btnPrevious.setLayoutParams(parambtnsave);
            btnPrevious.setText("Previous");
            btnPrevious
                    .setBackgroundColor(getResources().getColor(R.color.btnPrv));
            btnPrevious.setTextColor(getResources().getColor(R.color.white));
            // btnPrevious.setLeft(10);

            btnPrevious.setId(Integer.parseInt("1004"));

            final Button btnNext = new Button(this);
            RelativeLayout.LayoutParams paramsNext = new RelativeLayout.LayoutParams(
                    btnwidth, LayoutParams.WRAP_CONTENT);
            // paramsNext.setMargins(10, 10, 10, 10);
            btnNext.setLayoutParams(paramsNext);
            btnNext.setText("Next");
            btnNext.setTextColor(getResources().getColor(R.color.white));

            btnNext.setBackgroundColor(getResources().getColor(R.color.btnNext));
            btnNext.setId(Integer.parseInt("1005"));

            Button btnReturn = new Button(this);
            RelativeLayout.LayoutParams paramsReturn = new RelativeLayout.LayoutParams(
                    btnwidth, LayoutParams.WRAP_CONTENT);
            // paramsReturn.setMargins(10, 10, 10, 10);
            btnReturn.setLayoutParams(paramsReturn);
            btnReturn.setText("Return");
            // btnReturn.setLeft(10);

            btnReturn.setTextColor(getResources().getColor(R.color.white));

            btnReturn.setBackgroundColor(getResources().getColor(R.color.btnPrv));
            btnReturn.setId(Integer.parseInt("1006"));

            final Button btnAttach = new Button(this);
            RelativeLayout.LayoutParams parambtnAttach = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            // parambtnAttach.setMargins(10, 10, 10, 10);

            btnAttach.setLayoutParams(parambtnAttach);
            btnAttach.setText("Attach");

            btnAttach.setBackgroundColor(getResources().getColor(R.color.btn));
            btnAttach.setTextColor(getResources().getColor(R.color.white));
            // btnPrevious.setLeft(10);

            btnAttach.setId(Integer.parseInt("11"));

            LinearLayout layoutbtnatch = new LinearLayout(this);
            layoutbtnatch.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            layoutbtnatch.setOrientation(LinearLayout.VERTICAL);

            layoutbtnatch.addView(btnAttach);

            layoutbtnatch.setPadding(10, 10, 10, 10);

            LinearLayout layoutque = new LinearLayout(this);
            layoutque.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            layoutque.setOrientation(LinearLayout.VERTICAL);
            layoutque.addView(txtque);
            radiselection_text = "";
            String answer = EditDatasheetActivityMain.editDatasheetslist.get(position)
                    .getAnswer();
            final RadioGroup rGroup3 = new RadioGroup(this);
            RadioGroup radioGroup = new RadioGroup(this);
            for (i = 0; i < sentenceCount; i++) {

                rbutton1 = new RadioButton(this);
                rbutton1.setId(i);
                txt = st.nextToken();
                txtvalue = stvalue.nextToken();
                rbutton1.setText(txt);
                cf.addDatasheetSelection(i, txt, txtvalue, FormId, FKQuesId);
                if (Answer == null || Answer.equalsIgnoreCase("") || Answer.equalsIgnoreCase("null")) {
                    rbutton1.setChecked(false);
                } else if (Answer.equalsIgnoreCase(txt)) {
                    rbutton1.setChecked(true);
                    radiselection_text = txt;
                    radioselection_value = txtvalue;
                } else {
                    rbutton1.setChecked(false);
                }

                rbutton1.setClickable(true);

                radioGroup.addView(rbutton1);

            }

            layoutque.addView(radioGroup);


            radioGroup
                    .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup,
                                                     int id1) {

                            selectedRadioVal = ((RadioButton) findViewById(id1))
                                    .getText().toString();
                            //    db.addDatasheetSelection(id1, selectedRadioVal, selection_Value, FormId, FKQuesId);
                            radiselection_text = selectedRadioVal;
                            getRowFromDatabase();
                            radioselection_value = selection_Value;
                            selected = id1;

                        }
                    });

            setContentView(layoutque);

            LinearLayout layoutP = new LinearLayout(this);
            layoutP.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            layoutP.setOrientation(LinearLayout.VERTICAL);
            // layoutans.addRule(RelativeLayout.LEFT_OF, 1001);
            layoutP.addView(btnPrevious);

            layoutP.setGravity(Gravity.CENTER);
            layoutP.setPadding(0, 10, 10, 10);

            LinearLayout layoutN = new LinearLayout(this);
            layoutN.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            layoutN.setOrientation(LinearLayout.HORIZONTAL);

            layoutN.addView(btnNext);

            layoutN.setGravity(Gravity.CENTER);
            layoutN.setPadding(0, 10, 10, 10);

            LinearLayout layoutR = new LinearLayout(this);
            layoutR.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            layoutR.setOrientation(LinearLayout.HORIZONTAL);

            layoutR.addView(btnReturn);
            layoutR.setGravity(Gravity.CENTER);
            layoutR.setPadding(0, 10, 0, 10);

            LinearLayout layoutans = new LinearLayout(this);
            layoutans.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            layoutans.setOrientation(LinearLayout.HORIZONTAL);
            // layoutans.addRule(RelativeLayout.LEFT_OF, 1001);
            layoutans.setPadding(10, 10, 10, 10);
            layoutans.addView(layoutP);
            layoutans.addView(layoutN);
            layoutans.addView(layoutR);
            layoutans.setGravity(Gravity.CENTER);

            setContentView(layoutque);

            View view = new View(this);
            view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2));
            view.setPadding(10, 10, 10, 20);
            view.setBackgroundColor(getResources().getColor(R.color.divider));

            LinearLayout layoutattach = new LinearLayout(this);
            layoutattach.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT));
            layoutattach.setOrientation(LinearLayout.VERTICAL);
            layoutattach.addView(layoutans);
            layoutattach.addView(view);
            layoutattach.addView(layoutbtnatch);

            layoutque.addView(layoutattach);
            // getPhotoFromDatabase();
            btnAttach.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    /*Intent intent = new Intent(EditDatasheetActivityMain.this,
                            DatasheetAttachmentActivity.class);
                    intent.putExtra("PKCssFormsQuesID", PKCssFormsQuesID);
                    intent.putExtra("DetailId", detailid);
                    startActivity(intent);*/
                }
            });

            if (position == 0) {
                btnPrevious.setVisibility(View.INVISIBLE);
            } else {
                btnPrevious.setVisibility(View.VISIBLE);
            }
            btnPrevious.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    radiselection_text = "";
                    newId = position - 1;
                    if (newId > -1) {

                        cf.deleteDatasheetSelection(selectionid,
                                selection_Text, selection_Value);
                        detailid = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getDetailid();
                        setFlag = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getFlag();
                        Answer = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getAnswer();
                        Answer_Value = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getAnswer_value();
                        FKQuesId = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getFKQuesId();
                        QuesText = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getQuesText();
                        ValueMax = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getValueMax();

                        PKCssFormsQuesID = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getPKCssFormsQuesID();
                        Weightage = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getWeightage();

                        IsResponseMandatory = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getIsResponseMandatory();
                        SelectionText = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getSelectionText();
                        SelectionValue = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getSelectionValue();
                        ValueMin = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getValueMin();

                        MaxValueText = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getMaxValueText();
                        SelectionType = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getSelectionType();
                        ControlWidth = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getControlWidth();
                        MaxNoOfResponses = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getMaxNoOfResponses();
                        ResponseType = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getResponseType();

                        Notes = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getNotes();
                        FKPrimaryQuesId = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getFKPrimaryQuesId();
                        FKSecondaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getFKSecondaryQuesId();

                        IsBranching = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getIsBranching();
                        ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getExpectedResponse();
                        IfResponseId = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getIfResponseId();
                        DisableQuesStr = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getDisableQuesStr();
                        GroupID = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getGroupID();
                        GroupName = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getGroupName();
                        QuesCode = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getQuesCode();

                        MaxExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getMaxExpectedResponse();

                        ResponseValue = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getResponseValue();
                        ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getExpectedResponse();

                        txtque.setText(QuesText);

                        position = position - 1;
                        newId = position;
                        getpos = newId;
                        setdata();
                    } else {
                        btnPrevious.setVisibility(View.INVISIBLE);
                    }

                }

            });

            btnNext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // radiselection_text = "";
                    if (IsResponseMandatory.equals("Y")
                            && radiselection_text.matches("")) {
                        // if (rbName.matches("")) {
                        flag = 0;
                        Toast.makeText(EditDatasheetDetailActivity.this,
                                "Please Answer the Question.",
                                Toast.LENGTH_LONG).show();
                        // }
                    } else {
                        flag = 1;
                        if (ResponseType.equals("Selection")) {

                            cf.addDatasheetANS(new Datasheet(
                                    PKCssFormsQuesID, FKQuesId,
                                    radiselection_text, radioselection_value,
                                    flag, FormId));
                            cf.deleteDatasheetSelection(selectionid,
                                    selection_Text, selection_Value);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setFlag(flag);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setDetailid(detailid);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setAnswer(radiselection_text);

                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setAnswer_value(radioselection_value);

                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setFKQuesId(FKQuesId);

                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setSequenceNo(SequenceNo);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setQuesText(QuesText);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setValueMax(ValueMax);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setSelectionValue(SelectionValue);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setWeightage(Weightage);

                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setPKCssFormsQuesID(PKCssFormsQuesID);
                            EditDatasheetActivityMain.editDatasheetslist
                                    .get(position)
                                    .setIsResponseMandatory(IsResponseMandatory);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setSelectionText(SelectionText);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setMaxValueText(MaxValueText);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setControlWidth(ControlWidth);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setMaxNoOfResponses(MaxNoOfResponses);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setResponseType(ResponseType);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setNotes(Notes);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setFKPrimaryQuesId(FKPrimaryQuesId);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setExpectedResponse(radiselection_text);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setFKSecondaryQuesId(FKSecondaryQuesId);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setDisableQuesStr(DisableQuesStr);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setGroupID(GroupID);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setGroupName(GroupName);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setQuesCode(QuesCode);
                            EditDatasheetActivityMain.editDatasheetslist
                                    .get(position)
                                    .setMaxExpectedResponse(MaxExpectedResponse);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setResponseValue(ResponseValue);

                        }
                        newId = position + 1;

                        if (newId <= EditDatasheetActivityMain.editDatasheetslist.size() - 1) {
                            detailid = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getDetailid();
                            setFlag = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getFlag();
                            Answer = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getAnswer();
                            Answer_Value = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getAnswer_value();
                            FKQuesId = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getFKQuesId();
                            QuesText = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getQuesText();
                            ValueMax = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getValueMax();

                            PKCssFormsQuesID = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getPKCssFormsQuesID();
                            Weightage = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getWeightage();

                            IsResponseMandatory = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getIsResponseMandatory();
                            SelectionText = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionText();
                            SelectionValue = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionValue();
                            ValueMin = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getValueMin();

                            MaxValueText = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxValueText();
                            SelectionType = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionType();
                            ControlWidth = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getControlWidth();
                            MaxNoOfResponses = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxNoOfResponses();
                            ResponseType = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getResponseType();

                            Notes = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                    .getNotes();
                            FKPrimaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getFKPrimaryQuesId();
                            FKSecondaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getFKSecondaryQuesId();

                            IsBranching = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getIsBranching();
                            ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getExpectedResponse();
                            IfResponseId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getIfResponseId();
                            DisableQuesStr = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getDisableQuesStr();
                            GroupID = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getGroupID();
                            GroupName = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getGroupName();
                            QuesCode = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getQuesCode();

                            MaxExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxExpectedResponse();

                            ResponseValue = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getResponseValue();
                            ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getExpectedResponse();
                            txtque.setText(QuesText);

                            if (ResponseType.equals("Text")) {
                                radiselection_text = "";

                            } else if (ResponseType.equals("Numeric")) {
                                radiselection_text = "";

                            } else {
                                radiselection_text = "";
                            }
                            // SequenceNo = newId + 1;
                            position = position + 1;
                            newId = position;
                            getpos = newId;
                            setdata();
                        } else {
                            btnNext.setVisibility(View.INVISIBLE);
                        }

                    }

                }

            });

            btnReturn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // flag = 0;
                    // getposition = position;
                    EditDatasheetDetailActivity.this.finish();
                }
            });

            // }
        }

        /************************************* Numeric ****************************************************************/
        else if (ResponseType.equals("Numeric")) {
            UUID uuid = UUID.randomUUID();
            detailid = uuid.toString();

            final TextView txtque = new TextView(this);
            RelativeLayout.LayoutParams paramstxt = new RelativeLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            paramstxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            txtque.setLayoutParams(paramstxt);
            txtque.setText(QuesText);
            txtque.setTextSize(18);
            txtque.setPadding(10, 10, 10, 10);
            txtque.setTextColor(getResources().getColor(R.color.black));

            edtAns = new EditText(this);
            RelativeLayout.LayoutParams paramEdt = new RelativeLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            edtAns.setLayoutParams(paramEdt);
            // edtAns.setHint("Enter answer");
            edtAns.setMaxWidth(200);
            // edtAns.setBackgroundResource(R.drawable.edittext_border);
            edtAns.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtAns.setTextSize(18);
            edtAns.setHint("Enter answer " + ValueMin + " - " + ValueMax);
            edtAns.setFocusable(true);

            String answer = EditDatasheetActivityMain.editDatasheetslist.get(position)
                    .getAnswer();
            if (!(answer == null || answer.equalsIgnoreCase(""))) {
                if (getpos == position) {
                    edtAns.setText(answer);
                } else if (getpos == position + 1) {

                    edtAns.setText("");

                } else {

                    edtAns.setText(answer);

                }

            }

            LinearLayout layoutque = new LinearLayout(this);
            layoutque.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            layoutque.setOrientation(LinearLayout.VERTICAL);
            layoutque.addView(txtque);

            layoutque.addView(edtAns);
            layoutque.setPadding(10, 10, 10, 10);

            layoutque.setId(Integer.parseInt("1"));
            setContentView(layoutque);

            final Button btnQR = new Button(this);
            RelativeLayout.LayoutParams parambtnQR = new RelativeLayout.LayoutParams(
                    btnwidth - 10, 40);

            // parambtnQR.setMargins(10, 10, 10, 10);
            btnQR.setGravity(Gravity.CENTER);
            btnQR.setLayoutParams(parambtnQR);
            // btnQR.setText("QR Code");

            btnQR.setBackgroundResource(R.drawable.ic_qr);
            // btnQR.setTextColor(getResources().getColor(R.color.white));
            // btnPrevious.setLeft(10);

            btnQR.setId(Integer.parseInt("12"));

            final Button btnBarcode = new Button(this);
            RelativeLayout.LayoutParams parambtnBarcode = new RelativeLayout.LayoutParams(
                    btnwidth - 10, 40);

            // parambtnBarcode.setMargins(10, 10, 10, 10);

            btnBarcode.setLayoutParams(parambtnBarcode);
            // btnBarcode.setText("Barcode");
            btnBarcode.setGravity(Gravity.CENTER);
            btnBarcode.setBackgroundResource(R.drawable.ic_barcode);
            // btnBarcode.setTextColor(getResources().getColor(R.color.white));

            btnBarcode.setId(Integer.parseInt("13"));

            LinearLayout layoutQR = new LinearLayout(this);
            layoutQR.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            layoutQR.setOrientation(LinearLayout.VERTICAL);
            layoutQR.setGravity(Gravity.CENTER);
            layoutQR.addView(btnQR);

            layoutQR.setPadding(10, 10, 10, 10);

            LinearLayout layoutBarcode = new LinearLayout(this);
            layoutBarcode.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            layoutBarcode.setOrientation(LinearLayout.VERTICAL);
            layoutBarcode.setGravity(Gravity.CENTER);
            layoutBarcode.addView(btnBarcode);

            layoutBarcode.setPadding(10, 10, 10, 10);

            LinearLayout layoutbtnCode = new LinearLayout(this);
            layoutbtnCode.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            layoutbtnCode.setOrientation(LinearLayout.HORIZONTAL);

            layoutbtnCode.setGravity(Gravity.CENTER);
            layoutbtnCode.addView(layoutQR);
            layoutbtnCode.addView(layoutBarcode);

            layoutbtnCode.setPadding(10, 10, 10, 10);
            setContentView(layoutque);

            final Button btnPrevious = new Button(this);
            RelativeLayout.LayoutParams parambtnsave = new RelativeLayout.LayoutParams(
                    btnwidth, LayoutParams.WRAP_CONTENT);

            // parambtnsave.setMargins(10, 10, 10, 10);
            btnPrevious.setLayoutParams(parambtnsave);
            btnPrevious.setText("Previous");
            btnPrevious
                    .setBackgroundColor(getResources().getColor(R.color.btnPrv));
            btnPrevious.setTextColor(getResources().getColor(R.color.white));
            // btnPrevious.setLeft(10);

            btnPrevious.setId(Integer.parseInt("1007"));

            final Button btnNext = new Button(this);
            RelativeLayout.LayoutParams paramsNext = new RelativeLayout.LayoutParams(
                    btnwidth, LayoutParams.WRAP_CONTENT);
            // paramsNext.setMargins(10, 10, 10, 10);
            btnNext.setLayoutParams(paramsNext);
            btnNext.setText("Next");
            btnNext.setTextColor(getResources().getColor(R.color.white));

            btnNext.setBackgroundColor(getResources().getColor(R.color.btnNext));
            btnNext.setId(Integer.parseInt("1008"));

            Button btnReturn = new Button(this);
            RelativeLayout.LayoutParams paramsReturn = new RelativeLayout.LayoutParams(
                    btnwidth, LayoutParams.WRAP_CONTENT);
            // paramsReturn.setMargins(10, 10, 10, 10);
            btnReturn.setLayoutParams(paramsReturn);
            btnReturn.setText("Return");
            // btnReturn.setLeft(10);

            btnReturn.setTextColor(getResources().getColor(R.color.white));

            btnReturn.setBackgroundColor(getResources().getColor(R.color.btnPrv));
            btnReturn.setId(Integer.parseInt("1009"));

            LinearLayout layoutP = new LinearLayout(this);
            layoutP.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            layoutP.setOrientation(LinearLayout.VERTICAL);

            layoutP.addView(btnPrevious);

            layoutP.setGravity(Gravity.CENTER);
            layoutP.setPadding(0, 10, 10, 10);

            LinearLayout layoutN = new LinearLayout(this);
            layoutN.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            layoutN.setOrientation(LinearLayout.HORIZONTAL);
            // layoutans.addRule(RelativeLayout.LEFT_OF, 1001);

            layoutN.addView(btnNext);

            layoutN.setGravity(Gravity.CENTER);
            layoutN.setPadding(0, 10, 10, 10);

            // layoutque.addView(layoutans);

            LinearLayout layoutR = new LinearLayout(this);
            layoutR.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            layoutR.setOrientation(LinearLayout.HORIZONTAL);

            layoutR.addView(btnReturn);
            layoutR.setGravity(Gravity.CENTER);
            layoutR.setPadding(0, 10, 0, 10);

            LinearLayout layoutans = new LinearLayout(this);
            layoutans.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            layoutans.setOrientation(LinearLayout.HORIZONTAL);
            layoutans.setPadding(10, 10, 10, 10);
            layoutans.addView(layoutP);
            layoutans.addView(layoutN);
            layoutans.addView(layoutR);
            layoutans.setGravity(Gravity.CENTER);
            // setContentView(layoutans);

            final Button btnAttach = new Button(this);
            RelativeLayout.LayoutParams parambtnAttach = new RelativeLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            // parambtnAttach.setMargins(10, 10, 10, 10);

            btnAttach.setLayoutParams(parambtnAttach);
            btnAttach.setText("Attach");

            btnAttach.setBackgroundColor(getResources().getColor(R.color.btn));
            btnAttach.setTextColor(getResources().getColor(R.color.white));
            // btnPrevious.setLeft(10);

            btnAttach.setId(Integer.parseInt("11"));

            LinearLayout layoutbtnatch = new LinearLayout(this);
            layoutbtnatch.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            layoutbtnatch.setOrientation(LinearLayout.VERTICAL);

            layoutbtnatch.addView(btnAttach);

            layoutbtnatch.setPadding(10, 10, 10, 10);

            View view = new View(this);
            view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2));
            view.setPadding(10, 10, 10, 20);
            view.setBackgroundColor(getResources().getColor(R.color.divider));

            listView = new ListView(this);
            RelativeLayout.LayoutParams paramlist = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            listView.setLayoutParams(paramlist);

            LinearLayout layoutlist = new LinearLayout(this);
            layoutlist.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            layoutlist.setOrientation(LinearLayout.VERTICAL);

            layoutlist.addView(listView);

            layoutlist.setPadding(10, 10, 10, 10);

            LinearLayout layoutBtn = new LinearLayout(this);
            layoutBtn.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT));
            layoutBtn.setOrientation(LinearLayout.VERTICAL);
            layoutBtn.addView(layoutbtnCode);
            layoutBtn.addView(layoutans);
            layoutBtn.addView(view);
            layoutBtn.addView(layoutbtnatch);
            layoutBtn.addView(layoutlist);
            layoutque.addView(layoutBtn);
            // getPhotoFromDatabase();
            btnAttach.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                   /* Intent intent = new Intent(DatasheetDetailsActivity.this,
                            DatasheetAttachmentActivity.class);
                    intent.putExtra("PKCssFormsQuesID", PKCssFormsQuesID);
                    intent.putExtra("DetailId", detailid);
                    startActivity(intent);*/
                }
            });

            btnQR.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                        Intent intent = new Intent(ACTION_SCAN);
                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                        startActivityForResult(intent, 0);

                    } catch (ActivityNotFoundException anfe) {
                       /* showDialog(EditDatasheetActivityMain.this,
                                "No Scanner Found",
                                "Download a scanner code custom_travel_plan_show?", "Yes",
                                "No").show();*/
                    }
                }
            });

            btnBarcode.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    try {
                        Intent intent = new Intent(ACTION_SCAN);
                        intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                        startActivityForResult(intent, 0);
                    } catch (ActivityNotFoundException anfe) {
                        /*showDialog(DatasheetDetailsActivity.this,
                                "No Scanner Found",
                                "Download a scanner code custom_travel_plan_show?", "Yes",
                                "No").show();*/
                    }
                }
            });

            if (position == 0) {
                btnPrevious.setVisibility(View.INVISIBLE);
            } else {
                btnPrevious.setVisibility(View.VISIBLE);
            }
            btnPrevious.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    radiselection_text = "";
                    newId = position - 1;
                    if (newId > -1) {
                        detailid = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getDetailid();

                        Answer = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getAnswer();
                        Answer_Value = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getAnswer_value();
                        setFlag = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getFlag();

                        FKQuesId = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getFKQuesId();
                        QuesText = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getQuesText();
                        ValueMax = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getValueMax();

                        PKCssFormsQuesID = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getPKCssFormsQuesID();
                        Weightage = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getWeightage();

                        IsResponseMandatory = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getIsResponseMandatory();
                        SelectionText = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getSelectionText();
                        SelectionValue = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getSelectionValue();
                        ValueMin = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getValueMin();

                        MaxValueText = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getMaxValueText();
                        SelectionType = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getSelectionType();
                        ControlWidth = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getControlWidth();
                        MaxNoOfResponses = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getMaxNoOfResponses();
                        ResponseType = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getResponseType();

                        if (ResponseType.equals("Numeric")
                                && ResponseType.equals("Text")) {
                            if (!(Answer == null && Answer.equals(""))) {
                                if (newId == position) {
                                    edtAns.setText("");
                                } else {
                                    edtAns.setText(Answer);
                                }
                            }
                        } else {
                            radiselection_text = "";
                        }
                        Notes = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getNotes();
                        FKPrimaryQuesId = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getFKPrimaryQuesId();
                        FKSecondaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getFKSecondaryQuesId();

                        IsBranching = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getIsBranching();
                        ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getExpectedResponse();
                        IfResponseId = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getIfResponseId();
                        DisableQuesStr = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getDisableQuesStr();
                        GroupID = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getGroupID();
                        GroupName = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getGroupName();
                        QuesCode = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                .getQuesCode();

                        MaxExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getMaxExpectedResponse();

                        ResponseValue = EditDatasheetActivityMain.editDatasheetslist.get(
                                newId).getResponseValue();
                        ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                .get(newId).getExpectedResponse();

                        txtque.setText(QuesText);

                        position = position - 1;
                        newId = position;
                        getpos = newId;
                        setdata();
                    } else if (newId < 0) {
                        btnPrevious.setVisibility(View.INVISIBLE);
                    }

                }

            });

            btnNext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    radiselection_text = "";

                    if (edtAns.getText().toString().equals("")) {

                        flag = 0;
                        Toast.makeText(getApplicationContext(),
                                "Please Answer the Question.",
                                Toast.LENGTH_LONG).show();
                    } else {

                        // int edt =
                        // Integer.parseInt(edtAns.getText().toString());
                        int valuemax = Integer.parseInt(ValueMax);
                        int valuemin = Integer.parseInt(ValueMin);
                        flag = 1;
                        radiselection_text = edtAns.getText().toString();
                        Answer_Value = "-1";

                        if (ResponseType.equals("Numeric")) {

                            cf.addDatasheetANS(new Datasheet(
                                    PKCssFormsQuesID, FKQuesId,
                                    radiselection_text, Answer_Value, flag, FormId));

                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setFlag(flag);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setDetailid(detailid);

                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setAnswer(radiselection_text);

                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setAnswer_value(Answer_Value);

                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setFKQuesId(FKQuesId);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setSequenceNo(SequenceNo);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setQuesText(QuesText);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setValueMax(ValueMax);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setSelectionValue(SelectionValue);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setWeightage(Weightage);

                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setPKCssFormsQuesID(PKCssFormsQuesID);
                            EditDatasheetActivityMain.editDatasheetslist
                                    .get(position)
                                    .setIsResponseMandatory(IsResponseMandatory);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setSelectionText(SelectionText);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setMaxValueText(MaxValueText);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setControlWidth(ControlWidth);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setMaxNoOfResponses(MaxNoOfResponses);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setResponseType(ResponseType);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setNotes(Notes);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setFKPrimaryQuesId(FKPrimaryQuesId);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setExpectedResponse(radiselection_text);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setFKSecondaryQuesId(FKSecondaryQuesId);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setDisableQuesStr(DisableQuesStr);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setGroupID(GroupID);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setGroupName(GroupName);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setQuesCode(QuesCode);
                            EditDatasheetActivityMain.editDatasheetslist
                                    .get(position)
                                    .setMaxExpectedResponse(MaxExpectedResponse);
                            EditDatasheetActivityMain.editDatasheetslist.get(position)
                                    .setResponseValue(ResponseValue);
                            // EditDatasheetActivityMain.editDatasheetslist.add(datasheet);

                        }
                        newId = position + 1;

                        if (newId <= EditDatasheetActivityMain.editDatasheetslist.size() - 1) {
                            detailid = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getDetailid();
                            setFlag = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getFlag();
                            Answer = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getAnswer();
                            Answer_Value = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getAnswer_value();
                            FKQuesId = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getFKQuesId();
                            QuesText = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getQuesText();
                            ValueMax = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getValueMax();

                            PKCssFormsQuesID = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getPKCssFormsQuesID();
                            Weightage = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getWeightage();

                            IsResponseMandatory = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getIsResponseMandatory();
                            SelectionText = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionText();
                            SelectionValue = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionValue();
                            ValueMin = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getValueMin();

                            MaxValueText = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxValueText();
                            SelectionType = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getSelectionType();
                            ControlWidth = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getControlWidth();
                            MaxNoOfResponses = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxNoOfResponses();
                            ResponseType = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getResponseType();

                            Notes = EditDatasheetActivityMain.editDatasheetslist.get(newId)
                                    .getNotes();
                            FKPrimaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getFKPrimaryQuesId();
                            FKSecondaryQuesId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getFKSecondaryQuesId();

                            IsBranching = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getIsBranching();
                            ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getExpectedResponse();
                            IfResponseId = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getIfResponseId();
                            DisableQuesStr = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getDisableQuesStr();
                            GroupID = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getGroupID();
                            GroupName = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getGroupName();
                            QuesCode = EditDatasheetActivityMain.editDatasheetslist.get(
                                    newId).getQuesCode();

                            MaxExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getMaxExpectedResponse();

                            ResponseValue = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getResponseValue();
                            ExpectedResponse = EditDatasheetActivityMain.editDatasheetslist
                                    .get(newId).getExpectedResponse();
                            txtque.setText(QuesText);

                            if (ResponseType.equals("Text")) {
                                radiselection_text = "";
                                edtAns.setText("");
                            } else if (ResponseType.equals("Numeric")) {
                                radiselection_text = "";
                                edtAns.setText(Answer);
                            } else {
                                radiselection_text = "";
                            }
                            // SequenceNo = newId + 1;
                            position = position + 1;
                            newId = position;
                            getpos = newId;
                            setdata();
                        } else {
                            btnNext.setVisibility(View.INVISIBLE);
                        }

                    }

                }

            });

            btnReturn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // flag = 0;
                    // getposition = position;
                    EditDatasheetDetailActivity.this.finish();
                }
            });

        }

    }

    private void getRowFromDatabase() {
        String que = "SELECT * FROM " + db.TABLE_DATASHEET_SELECTION + " WHERE FormId='" + FormId + "' AND FKQuesId='" + FKQuesId + "'";
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() == 0) {

        } else {
            cur.moveToFirst();

            sid = cur.getInt(cur.getColumnIndex("sid"));
            selectionid = cur.getInt(cur.getColumnIndex("selectionid"));
            selection_Text = cur.getString(cur.getColumnIndex("selectionText"));
            selection_Value = cur.getString(cur.getColumnIndex("selectionValue"));
            fid = cur.getString(cur.getColumnIndex("FormId"));
            qid = cur.getString(cur.getColumnIndex("FKQuesId"));
        }
    }
}
