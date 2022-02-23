package com.vritti.vwb.vworkbench;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.BuildConfig;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Attachment;
import com.vritti.vwb.Beans.Datasheet;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

/**
 * Created by 300151 on 12/14/2016.
 */
public class EditDatasheetDetailActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    public String FKQuesId, QuesText, ValueMax, SelectionValue, Weightage;
    public String PKCssFormsQuesID, IsResponseMandatory, SelectionText,
            ValueMin, SelectionType;
    public String MaxValueText, ControlWidth, MaxNoOfResponses, ResponseType,
            Notes, FKPrimaryQuesId, FKSecondaryQuesId, IfResponseId,
            IsBranching, ExpectedResponse, DisableQuesStr, GroupID, GroupName,
            QuesCode, MaxExpectedResponse, ResponseValue, Answer = " ", Answer_Value = " ", Attachment = "";
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
    private ArrayList<String> selectedTravelId = new ArrayList<>();
    private AppCompatCheckBox checkBoxcheck;
    private ArrayList<String> selectedcvalue = new ArrayList<>();

    File file;
    private static int RESULT_LOAD_IMG = 2;

    private static final int RESULT_CAPTURE_IMG = 3;
    private static final int RESULT_DOCUMENT = 4;
    private Uri outPutfileUri;
    private int APP_REQUEST_CODE = 4478;
    private String attachment, PKCssDtlsID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
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
        Log.d("test", "btn width " + btnwidth);


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
                detailid = AddDatasheetActivityMain.datasheetlists.get(
                        newId).getDetailid();
                //detailid = uuid.toString();

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
                //edtAns.setInputType(InputType.TYPE_CLASS_NUMBER);
                edtAns.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
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
                //layoutbtnCode.addView(layoutQR);
                //layoutbtnCode.addView(layoutBarcode);

                //layoutbtnCode.setPadding(10, 10, 10, 10);
                //setContentView(layoutque);

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
                // layoutBtn.addView(layoutbtnCode);
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
                        addMoreImages();

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
                                    "Download a scanner code activity?", "Yes",
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
                                    "Download a scanner code activity?", "Yes",
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
                // UUID uuid = UUID.randomUUID();
                // detailid = com.vritti.vwb.vworkbench.AddDatasheetActivityMain.datasheetlists.get(
                //newId).getDetailid();
                // detailid = uuid.toString();

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
                // layoutBtn.addView(layoutbtnCode);
                layoutBtn.addView(layoutans);
                layoutBtn.addView(view);
                layoutBtn.addView(layoutbtnatch);
                layoutBtn.addView(layoutlist);
                layoutque.addView(layoutBtn);
                //getPhotoFromDatabase();
                btnAttach.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        addMoreImages();

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
                                    "Download a scanner code activity?", "Yes",
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
                                    "Download a scanner code activity?", "Yes",
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
                                EditDatasheetActivityMain.editDatasheetslist.get(position).setAnswer(radiselection_text);
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
                                //btnNext.setVisibility(View.INVISIBLE);
                                btnNext.setText("Cancel");
                                btnNext.setTextColor(getResources().getColor(R.color.white));
                                btnNext.setBackgroundColor(getResources().getColor(R.color.btnNext));
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
        if (ResponseType.equals("Selection")) {
            // if (SelectionType.equals("Radio button list")) {


            if (SelectionType.equalsIgnoreCase("Check box list")) {
                UUID uuid = UUID.randomUUID();
                detailid = AddDatasheetActivityMain.datasheetlists.get(
                        newId).getDetailid();
                // detailid = uuid.toString();
                int totalLength = 0;
                String token = null;
                final StringTokenizer st = new StringTokenizer(SelectionText, "|");

                final int sentenceCount = st.countTokens();
                Log.d("test", "sentenceCount:" + sentenceCount);

                final StringTokenizer stvalue = new StringTokenizer(SelectionValue,
                        "|");

                final int stvaluecount = stvalue.countTokens();
                Log.d("test", "stvaluecount:" + stvaluecount);

                final TextView txtque = new TextView(this);
                RelativeLayout.LayoutParams paramstxt = new RelativeLayout.LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
                paramstxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                txtque.setLayoutParams(paramstxt);
                txtque.setText(QuesText);
                txtque.setTextSize(18);
                txtque.setPadding(10, 10, 10, 10);
                txtque.setTextColor(getResources().getColor(R.color.black));
                // Log.d("test", "no:" + selection_text[1]);

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
                checkBoxcheck = new AppCompatCheckBox(this);
                checkBoxcheck.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                for (i = 0; i < sentenceCount; i++) {

                    final CheckBox checkBoxcheck = new AppCompatCheckBox(this);
                    checkBoxcheck.setId(i);
                    txt = st.nextToken();
                    txtvalue = stvalue.nextToken();
                    checkBoxcheck.setText(txt);
                    cf.addDatasheetSelection(i, txt, txtvalue, FormId, FKQuesId);

                    if (Answer == null || Answer.equalsIgnoreCase("") || Answer.equalsIgnoreCase("null")) {

                    } else {
                        String[] arr = Answer.split("@,");

                        Answer = arr[0];
                    }


                    if (Answer == null || Answer.equalsIgnoreCase("") || Answer.equalsIgnoreCase("null")) {
                        checkBoxcheck.setChecked(false);
                    } else if (Answer.equalsIgnoreCase(txt)) {
                        checkBoxcheck.setChecked(true);
                        radiselection_text = txt;
                        radioselection_value = txtvalue;
                    } else {
                        checkBoxcheck.setChecked(false);
                    }

                    checkBoxcheck.setClickable(true);
                    // radioGroup.addView(rbutton1);
                    layoutque.addView(checkBoxcheck);

                    checkBoxcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            int a = buttonView.getId();
                            radiselection_text = checkBoxcheck.getText().toString();
                            radioselection_value = getCheckboxvalue(radiselection_text);
                            if (isChecked) {
                                selectedTravelId.add(radiselection_text);
                                selectedcvalue.add(radioselection_value);
                            } else {
                                if (selectedTravelId.contains(radiselection_text) || selectedcvalue.contains(radioselection_value)) {
                                    selectedTravelId.remove(radiselection_text);
                                    selectedcvalue.remove(radioselection_value);

                                }
                            }


                        }
                    });

                }

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
                        addMoreImages();

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
                                    "Please answer the question.",
                                    Toast.LENGTH_LONG).show();
                            // }
                        } else {
                            flag = 1;
                            if (ResponseType.equals("Selection")) {
                                // if (SelectionType.equals("Check box list")) {


                                // add travel ids

                                StringBuilder travelId = new StringBuilder();
                                StringBuilder value = new StringBuilder();

                                String data = "", data1 = "";
                                for (int i = 0; i < selectedTravelId.size(); i++) {

                                    travelId.append(selectedTravelId.get(i));

                                    if (!(i == selectedTravelId.size() - 1)) {

                                        travelId.append("@,");
                                    }

                                    // to create travelId string

                                    data = travelId.toString();// to convert StringBuilder to String

                                }

                                for (int i = 0; i < selectedcvalue.size(); i++) {

                                    value.append(selectedcvalue.get(i));

                                    if (!(i == selectedcvalue.size() - 1)) {

                                        value.append("@,");
                                    }

                                    // to create travelId string

                                    data1 = value.toString();// to convert StringBuilder to String

                                }


                                cf.addDatasheetANS(new Datasheet(
                                        PKCssFormsQuesID, FKQuesId,
                                        data, data1,
                                        flag, FormId));
                                cf.deleteDatasheetSelection(selectionid,
                                        selection_Text, selection_Value);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setFlag(flag);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setDetailid(detailid);
                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setAnswer(data);

                                EditDatasheetActivityMain.editDatasheetslist.get(position)
                                        .setAnswer_value(data1);


                                selectedcvalue.clear();
                                selectedTravelId.clear();

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

                                // }
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
                                // btnNext.setVisibility(View.INVISIBLE);
                                btnNext.setText("Cancel");
                                btnNext.setTextColor(getResources().getColor(R.color.white));
                                btnNext.setBackgroundColor(getResources().getColor(R.color.btnNext));
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
            } else {

                UUID uuid = UUID.randomUUID();
                detailid = AddDatasheetActivityMain.datasheetlists.get(
                        newId).getDetailid();
                //detailid = uuid.toString();
                int totalLength = 0;
                String token = null;
                final StringTokenizer st = new StringTokenizer(SelectionText, "|");

                final int sentenceCount = st.countTokens();
                Log.d("test", "sentenceCount:" + sentenceCount);

                final StringTokenizer stvalue = new StringTokenizer(SelectionValue,
                        "|");

                final int stvaluecount = stvalue.countTokens();
                Log.d("test", "stvaluecount:" + stvaluecount);

                final TextView txtque = new TextView(this);
                RelativeLayout.LayoutParams paramstxt = new RelativeLayout.LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
                paramstxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                txtque.setLayoutParams(paramstxt);
                txtque.setText(QuesText);
                txtque.setTextSize(18);
                txtque.setPadding(10, 10, 10, 10);
                txtque.setTextColor(getResources().getColor(R.color.black));
                // Log.d("test", "no:" + selection_text[1]);

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
                ScrollView sv = new ScrollView(this);
                LinearLayout layoutque = new LinearLayout(this);
                layoutque.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                layoutque.setOrientation(LinearLayout.VERTICAL);
                sv.addView(layoutque);
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
                                //    cf.addDatasheetSelection(id1, selectedRadioVal, selection_Value, FormId, FKQuesId);
                                radiselection_text = selectedRadioVal;
                                getRowFromDatabase();
                                radioselection_value = selection_Value;
                                selected = id1;

                            }
                        });

                setContentView(sv);

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

                setContentView(sv);

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
                        addMoreImages();

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
                                // btnNext.setVisibility(View.INVISIBLE);
                                btnNext.setText("Cancel");
                                btnNext.setTextColor(getResources().getColor(R.color.white));
                                btnNext.setBackgroundColor(getResources().getColor(R.color.btnNext));
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
        }

        /************************************* Numeric ****************************************************************/
        if (ResponseType.equals("Numeric")) {
            UUID uuid = UUID.randomUUID();
            // detailid = uuid.toString();
            detailid = AddDatasheetActivityMain.datasheetlists.get(
                    newId).getDetailid();

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
            // edtAns.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtAns.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
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
            // layoutbtnCode.addView(layoutQR);
            //layoutbtnCode.addView(layoutBarcode);

            //layoutbtnCode.setPadding(10, 10, 10, 10);
            //setContentView(layoutque);

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
            //  layoutBtn.addView(layoutbtnCode);
            layoutBtn.addView(layoutans);
            layoutBtn.addView(view);
            layoutBtn.addView(layoutbtnatch);
            layoutBtn.addView(layoutlist);
            layoutque.addView(layoutBtn);
            // getPhotoFromDatabase();
            btnAttach.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    addMoreImages();

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
                                "Download a scanner code activity?", "Yes",
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
                                "Download a scanner code activity?", "Yes",
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
                      //  int valuemax = Integer.parseInt(ValueMax);
                      //  int valuemin = Integer.parseInt(ValueMin);
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
        SQLiteDatabase sql1 = db.getWritableDatabase();
        String que = "SELECT * FROM " + db.TABLE_DATASHEET_SELECTION + " WHERE FormId='" + FormId + "' AND FKQuesId='" + FKQuesId + "'";
        Cursor cur = sql1.rawQuery(que, null);
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


    private String getCheckboxvalue(String Value) {
        SQLiteDatabase sql1 = db.getWritableDatabase();
        String que = "SELECT * FROM " + db.TABLE_DATASHEET_SELECTION + " WHERE selectionText='" + Value + "'";
        Cursor cur = sql1.rawQuery(que, null);
        if (cur.getCount() == 0) {

        } else {
            cur.moveToFirst();


            selection_Value = cur.getString(cur.getColumnIndex("selectionValue"));

        }
        return selection_Value;
    }


    private void requestDocumentPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    201);
        } else {
            DocumentIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    startCameraIntent();
                }
                break;
            case 201:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startGalleryIntent();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                    200);
        } else {
            startCameraIntent();
        }
    }

    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    201);
        } else {
            startGalleryIntent();
        }
    }


    private void addMoreImages() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_attachment_option_dialog);
        dialog.setTitle(getResources().getString(R.string.app_name));
        TextView camera = (TextView) dialog.findViewById(R.id.camera);
        TextView gallery = (TextView) dialog.findViewById(R.id.gallery);
        TextView textViewCancel = (TextView) dialog.findViewById(R.id.cancel);
        TextView document = dialog.findViewById(R.id.document);
        gallery.setVisibility(View.VISIBLE);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestCameraPermission();

            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestGalleryPermission();

            }
        });
        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestDocumentPermission();

            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void startGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMG);

    }

    private void DocumentIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    RESULT_DOCUMENT);

        } catch (ActivityNotFoundException ex) {
            Toast.makeText(EditDatasheetDetailActivity.this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "attachment.jpg");
        outPutfileUri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        startActivityForResult(intent, RESULT_CAPTURE_IMG);
    }

    private String getRealPathFromURI(Uri outPutfileUri) {
        Cursor cur = getContentResolver().query(outPutfileUri, null, null, null, null);
        cur.moveToFirst();
        int idx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cur.getString(idx);

    }

    public void handleSendImage(Uri imageUri) throws IOException {
        //Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            File file = new File(getCacheDir(), "image");
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            try {

                OutputStream output = new FileOutputStream(file);
                try {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;

                    while ((read = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }

                    output.flush();
                } finally {
                    output.close();
                }
            } finally {
                inputStream.close();
                byte[] bytes = getFileFromPath(file);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmapToUriConverter(bitmap);
                //Upload Bytes.
            }
        }
    }

    public static byte[] getFileFromPath(File file) {
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }


    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;


        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);
            int w = mBitmap.getWidth();
            int h = mBitmap.getHeight();
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, w, h,
                    true);
            String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString();
            File file = new File(path1 + "/" + "Sahara" + "/" + "Sender");
            if (!file.exists())
                file.mkdirs();
            File file1 = new File(file, "Image-" + new Random().nextInt() + ".jpg");
            if (file1.exists())
                file1.delete();
           /* File file = new File(SharefunctionActivity.this.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");*/
            FileOutputStream out = new FileOutputStream(file1);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
            out.flush();
            out.close();
            attachment = file1.getAbsolutePath();
            File f = new File(attachment);
            //Attachment=f.getName();
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new PostUploadImageMethodProspect().execute();


                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(EditDatasheetDetailActivity.this, msg);
                    }
                });
            } else {
                ut.displayToast(EditDatasheetDetailActivity.this, "No Internet connection");
                //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
            }
            Toast.makeText(EditDatasheetDetailActivity.this, "Document attached successfully", Toast.LENGTH_SHORT).show();


            //	uri = Uri.fromFile(f);
//file:///data/data/vworkbench7.vritti.com.vworkbench7/files/Image1825476171.jpeg


        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String getRealPathFromUri(Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }

        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 3) {
                String uri = outPutfileUri.toString();
                Log.e("uri-:", uri);
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                  /*  FileOutputStream out = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                    outPutfileUri = Uri.parse(url);*/
                    if (outPutfileUri.toString().contains("content")) {
                        handleSendImage(outPutfileUri);
                    } else {
                        File file = new File(getRealPathFromUri(EditDatasheetDetailActivity.this, outPutfileUri));//create path from uri
                        attachment = file.getName();
                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostUploadImageMethodProspect().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    ut.displayToast(EditDatasheetDetailActivity.this, msg);
                                }
                            });
                        } else {
                            ut.displayToast(EditDatasheetDetailActivity.this, "No Internet connection");
                            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
                        }

                    }
                    //Log.d("FileURI",file.getAbsoluteFile().toString());
                    //callChangeProfileImageApi(file.getAbsoluteFile().toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == RESULT_LOAD_IMG && resultCode == this.RESULT_OK && null != data) {

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                if (data.getData() != null) {
                    outPutfileUri = data.getData();
                    // Get the cursor
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                    //	uploadFileBitMap = bitmap;
                    file = new File(getRealPathFromURI(outPutfileUri));
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "attachment", null);
                    outPutfileUri = Uri.parse(url);
                    if (outPutfileUri.toString().contains("content")) {
                        handleSendImage(outPutfileUri);
                    } else {
                        File file = new File(getRealPathFromUri(EditDatasheetDetailActivity.this, outPutfileUri));//create path from uri
                        attachment = file.getName();

                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostUploadImageMethodProspect().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    ut.displayToast(EditDatasheetDetailActivity.this, msg);
                                }
                            });
                        } else {
                            ut.displayToast(EditDatasheetDetailActivity.this, "No Internet connection");
                            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
                        }

                    }


                    //img_userpic.setImageURI(fileUri);
                    //callChangeProfileImageApi(file.getAbsoluteFile().toString());


                } else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == RESULT_DOCUMENT && null != data) {

                Uri selectedFileURI = data.getData();
                File file = new File(getRealPathFromUri(EditDatasheetDetailActivity.this, selectedFileURI));//create path from uri
                Log.d("", "File : " + file.getName());
                attachment = file.toString();

                if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PostUploadImageMethodProspect().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(EditDatasheetDetailActivity.this, msg);
                        }
                    });
                } else {
                    ut.displayToast(EditDatasheetDetailActivity.this, "No Internet connection");
                    //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
                }

            } else {
                if (requestCode == APP_REQUEST_CODE) {
                    Toast.makeText(this, "verification cancel",
                            Toast.LENGTH_LONG).show();
                } else if (requestCode == RESULT_LOAD_IMG) {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


    }

    public class PostUploadImageMethodProspect extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;
        String response = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... urls) {

            try {
                String upLoadServerUri = CompanyURL + WebUrlClass.api_UploadAttechmentDatasheet + "?AppEnvMasterId=" + EnvMasterId + "&ActivityId=" + detailid+"&SourceType=CSSDetail";
                FileInputStream fileInputStream = new FileInputStream(attachment);
                Object res = null;
                File file = new File(attachment);
                response = String.valueOf(Utility.OpenMultiPart(upLoadServerUri, file));
                if (response != null && (!response.equals(""))) {
                    try {
                        /*Log.i("imageNameDone:", urls[0]);
                        jsonimage.put("File", urls[0]);
                        jsonArray.put(jsonimage);
                        Log.i("imageNameError:", urls[0]);
                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                        SQLiteDatabase sql1 = db.getWritableDatabase();
                        sql1.update(db.TABLE_DATA_OFFLINE, contentValues1, "linkurl=?",
                                new String[]{ urls[0]});*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                   /* Log.i("imageNameError:", urls[0]);
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                    SQLiteDatabase sql1 = db.getWritableDatabase();
                    sql1.update(db.TABLE_DATA_OFFLINE, contentValues1, "linkurl=?",
                            new String[]{ urls[0]});*/
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("ImageText", e.getMessage());
            }

            return response;

        }

        protected void onPostExecute(String feed) {

            if (feed != null) {

                Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_LONG).show();

            }

        }
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
