package com.vritti.expensemanagement;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.vritti.crm.vcrm7.CRMHomeActivity;
import com.vritti.crm.vcrm7.CommonListActivity;
import com.vritti.crm.vcrm7.OpportunityActivity_V1;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.BuildConfig;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.EnoJobService;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.ActivityMain;
import com.vritti.vwb.vworkbench.VWBHomeActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static com.vritti.crm.vcrm7.BusinessProspectusActivity.COUNTRY;

public class AddExpenseActivity_V1 extends AppCompatActivity {


	@SuppressLint("SimpleDateFormat")
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	Date currentTime;


	AutoCompleteTextView spinner_link;
	TextView spinner_category;
	TextView txt_date, txt_time;
	Button btn_save,button_cancel;
	ImageView button_next;
	String category = "", subcategory = "", vehicle = "", date = "", time = "", price, remark, card_no = "", wallet_type = "", exp_type = "", Vehicle_type = "",
			payment_mode = "", travel_mode = "", attachment = "";
	private String format;

	SQLiteDatabase sql;
	LinearLayout len_vehicle, len_location, len_task, len_link;
	String Source = "", Dest = "", KM = "";
	public static FirebaseJobDispatcher dispatcherNew;
	public static Job myJobNew = null;


	AutoCompleteTextView ed_fromPlace, ed_toPlace;
	LinearLayout len_distance;

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";

	private static final String API_KEY = "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk";
	private String Tolocation, Fromlocation;
	private double disatnce;
	String Location_disatnce;
	public static DefaultHttpClient httpClient = new DefaultHttpClient();
	private Location location, location1;
	double source_lat, source_lng;
	double dest_lat, dest_lat_lng;
	EditText edt_km;
	double two_wheeler = 2.5;
	double four_wheeler = 7;
	double Calculation;
	ArrayList<String> Categorylist;

	File file;
	private static int RESULT_LOAD_IMG = 2;

	private static final int RESULT_CAPTURE_IMG = 3;
	private static final int RESULT_DOCUMENT = 4;
	private Uri outPutfileUri;
	private int APP_REQUEST_CODE = 4478;
	TextView text_attach,text_view;


	String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
			UserMasterId = "", UserName = "", MobileNo = "";
	Utility ut;
	DatabaseHandlers db;
	CommonFunction cf;
	static Context context;

	ArrayAdapter<CharSequence> SubcategoryAdapter;
	private String LinkTo = "", LinkId = "", Attachment = "";
	public ArrayList<String> lsTaskActivityList = new ArrayList<>();
	public ArrayList<String> lsCRMCallList = new ArrayList<>();

	ArrayAdapter<String> taskAdapter;
	ExpenseData expenseData;


	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private Bitmap bitmap;
	private static File mediaFile;
	private UUID uuid;
	String uuidInString = "";

	Button button_activity,button_sales;
	ImageView img_back,history,microphone;
	public static final int ACTIVITY = 3;
	public AddExpenseActivity_V1() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voicesearch_activity_main_v1);
		//getSupportActionBar().setTitle("Add Expense");

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		context = AddExpenseActivity_V1.this;
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

		spinner_category = findViewById(R.id.spinner_category);

		txt_date = findViewById(R.id.txt_date);
		txt_time = findViewById(R.id.txt_time);
		spinner_link = findViewById(R.id.spinner_link);
		button_activity = findViewById(R.id.button_activity);
		button_sales = findViewById(R.id.button_sales);
		button_next = findViewById(R.id.button_next);
		button_cancel = findViewById(R.id.button_cancel);

		img_back=findViewById(R.id.img_back);
		history=findViewById(R.id.history);
		microphone=findViewById(R.id.microphone);


		img_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		long date1 = System.currentTimeMillis();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String dateString = sdf.format(date1);
		txt_date.setText(dateString);

		SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
		String timeString = sdf1.format(date1);
		txt_time.setText(timeString);


		history.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AddExpenseActivity_V1.this,
						HistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);


			}
		});

		microphone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AddExpenseActivity_V1.this,
						RecycleExpenseMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

			}
		});


		txt_date.setOnClickListener(new View.OnClickListener() {
			int year, month, day;

			@Override
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpenseActivity_V1.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker datePicker, int year,
												  int monthOfYear, int dayOfMonth) {
								//    datePicker.setMinDate(c.getTimeInMillis());
								date = dayOfMonth + "-"
										+ String.format("%02d", (monthOfYear + 1))
										+ "-" + year;
								txt_date.setText(date);
							}
						}, year, month, day);
				datePickerDialog.setTitle("Select Date");
				datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
				datePickerDialog.show();
			}
		});

		txt_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Calendar mcurrentTime = Calendar.getInstance();
				int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				int minute = mcurrentTime.get(Calendar.MINUTE);
				TimePickerDialog mTimePicker;

				mTimePicker = new TimePickerDialog(AddExpenseActivity_V1.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view, int hourOfDay,
												  int minute) {

								if (hourOfDay == 0) {

									hourOfDay += 12;

									format = "AM";
								} else if (hourOfDay == 12) {

									format = "PM";

								} else if (hourOfDay > 12) {

									hourOfDay -= 12;

									format = "PM";

								} else {

									format = "AM";
								}


								txt_time.setText(hourOfDay + ":" + minute +" "+ format);
							}
						}, hour, minute, false);
				mTimePicker.show();

			}
		});

		spinner_link.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String ActivityName = spinner_link.getText().toString();

				if (LinkTo.equals("Activity")) {

					String query = "SELECT ActivityId FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE ActivityName LIKE '" + ActivityName + "'";
					Cursor cur = sql.rawQuery(query, null);
					if (cur.getCount() > 0) {
						cur.moveToFirst();
						do {
							LinkId = cur.getString(cur.getColumnIndex("ActivityId"));
						}
						while (cur.moveToNext());

					}
				} else {
					String query = "SELECT CallId FROM " + db.TABLE_CRM_CALL_OPPORTUNITY + " WHERE FirmName LIKE '" + ActivityName + "'";

					Cursor cur = sql.rawQuery(query, null);
					if (cur.getCount() > 0) {
						cur.moveToFirst();
						do {
							LinkId = cur.getString(cur.getColumnIndex("CallId"));

						}
						while (cur.moveToNext());
					}
				}
			}
		});


        spinner_category.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddExpenseActivity_V1.this,
						CommonListActivity.class);
				intent.putExtra("option", "exptype");
				startActivityForResult(intent, COUNTRY);
			}
		});
		button_sales.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LinkTo=button_sales.getText().toString();
				spinner_link.setText("");

				Intent intent = new Intent(AddExpenseActivity_V1.this, OpportunityActivity_V1.class);
				intent.putExtra("Opportunity", "main_opp");
				intent.putExtra("flag", "1");
				startActivityForResult(intent, COUNTRY);
				overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
			//	UpdateCRMCall();
				button_sales.setBackground(getResources().getDrawable(R.drawable.button_orange));
				button_activity.setBackground(getResources().getDrawable(R.drawable.button_grey));


			}
		});
		button_activity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				spinner_link.setText("");
				LinkTo=button_activity.getText().toString();
				Intent intent = new Intent(AddExpenseActivity_V1.this, ActivityMain.class);
				intent.putExtra("activty","act_work");
				intent.putExtra("flag", "1");
				startActivityForResult(intent, ACTIVITY);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				button_activity.setBackground(getResources().getDrawable(R.drawable.button_orange));
				button_sales.setBackground(getResources().getDrawable(R.drawable.button_grey));
			//	UpdateTaskList();
			}
		});

		button_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});



		button_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				date = txt_date.getText().toString();
				time = txt_time.getText().toString();

				if (LinkTo.equals("")){
					Toast.makeText(AddExpenseActivity_V1.this,"Please select activity/opportunity",Toast.LENGTH_LONG).show();
				}else if (LinkId.equals("")){
					Toast.makeText(AddExpenseActivity_V1.this,"Please select activity/opportunity",Toast.LENGTH_LONG).show();
				}else {

					Intent intent = new Intent(AddExpenseActivity_V1.this, AddExpenseActivity_Next.class);
					intent.putExtra("date", date);
					intent.putExtra("time", time);
					intent.putExtra("LinkTo", LinkTo);
					intent.putExtra("LinkId", LinkId);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

				}



			}
		});


	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main_expense_widget, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
        *//*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*//*
      *//*  if(id == R.id.SupportStaff){
        }*//*

		if (id == R.id.proceed) {
			startActivity(new Intent(AddExpenseActivity_V1.this, HistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
		if (id == R.id.microphone) {
			startActivity(new Intent(AddExpenseActivity_V1.this, RecycleExpenseMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}

		return super.onOptionsItemSelected(item);


	}
*/




	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

	}

	private void UpdateTaskList() {
		lsTaskActivityList.clear();
		String query = "SELECT ActivityName,ActivityId FROM " + db.TABLE_ACTIVITYMASTER_PAGING;
		Cursor cur = sql.rawQuery(query, null);
		if (cur.getCount() > 0) {
			cur.moveToFirst();
			do {
				lsTaskActivityList.add(cur.getString(cur.getColumnIndex("ActivityName")));
			} while (cur.moveToNext());
		}
		taskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lsTaskActivityList);
		taskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_link.setAdapter(taskAdapter);

	}

	private void UpdateCRMCall() {
		lsCRMCallList.clear();
		String query = "SELECT FirmName,CallId FROM " + db.TABLE_CRM_CALL_OPPORTUNITY;
		Cursor cur = sql.rawQuery(query, null);
		if (cur.getCount() > 0) {
			cur.moveToFirst();
			do {
				lsCRMCallList.add(cur.getString(cur.getColumnIndex("FirmName")));
			} while (cur.moveToNext());
		}
		taskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lsCRMCallList);
		taskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_link.setAdapter(taskAdapter);

	}
	public String getdate(String exp_date) {
		//Format of the date defined in the input String
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
		//Desired format: 24 hour format: Change the pattern as per the need
		DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
		Date date = null;
		String output = null;
		try {
			//Converting the input String to Date
			date = df.parse(exp_date);
			//Changing the format of date and storing it in String
			output = outputformat.format(date);
			//Displaying the date
			System.out.println(output);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return output;
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {

			if (requestCode == COUNTRY && resultCode == COUNTRY) {
						LinkTo = data.getStringExtra("Name");
						if (LinkTo.equalsIgnoreCase("Official")||LinkTo.equalsIgnoreCase("Personal")){
							spinner_category.setText(LinkTo);
						}else {
							LinkId = data.getStringExtra("ID");
							spinner_link.setText(LinkTo);
							Log.d("Call_id", LinkId);
							date = txt_date.getText().toString();
							time = txt_time.getText().toString();

							if (LinkTo.equals("")) {
								Toast.makeText(AddExpenseActivity_V1.this, "Please select activity/opportunity", Toast.LENGTH_LONG).show();
							} else if (LinkId.equals("")) {
								Toast.makeText(AddExpenseActivity_V1.this, "Please select activity/opportunity", Toast.LENGTH_LONG).show();
							} else {

								Intent intent = new Intent(AddExpenseActivity_V1.this, AddExpenseActivity_Next.class);
								intent.putExtra("date", date);
								intent.putExtra("time", time);
								intent.putExtra("LinkTo", LinkTo);
								intent.putExtra("LinkId", LinkId);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);

							}
						}
			}
			if (requestCode == ACTIVITY && resultCode == ACTIVITY) {
				LinkTo = data.getStringExtra("Name");
				LinkId = data.getStringExtra("ID");
				spinner_link.setText(LinkTo);
				Log.d("Call_id",LinkId);
				date = txt_date.getText().toString();
				time = txt_time.getText().toString();

				if (LinkTo.equals("")){
					Toast.makeText(AddExpenseActivity_V1.this,"Please select activity/opportunity",Toast.LENGTH_LONG).show();
				}else if (LinkId.equals("")){
					Toast.makeText(AddExpenseActivity_V1.this,"Please select activity/opportunity",Toast.LENGTH_LONG).show();
				}else {

					Intent intent = new Intent(AddExpenseActivity_V1.this, AddExpenseActivity_Next.class);
					intent.putExtra("date", date);
					intent.putExtra("time", time);
					intent.putExtra("LinkTo", LinkTo);
					intent.putExtra("LinkId", LinkId);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

				}

			}

		} catch (Exception e) {
			Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
					.show();
		}
	}

}