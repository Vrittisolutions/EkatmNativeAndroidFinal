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
import android.support.design.widget.TextInputLayout;
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
import com.vritti.crm.vcrm7.BusinessProspectusActivity;
import com.vritti.crm.vcrm7.CommonListActivity;
import com.vritti.crm.vcrm7.CountryListActivity;
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

public class AddExpenseActivity_Next extends AppCompatActivity {
	@SuppressLint("SimpleDateFormat")
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	Date currentTime;

	AutoCompleteTextView spinner_subcategory,spinner_vehicle, spinner_payment;
	EditText edt_price, edt_remark, edt_card;
	TextInputLayout edit_card;
	Button btn_save;
	String category ="Official", subcategory = "", vehicle = "", date = "", time = "", price, remark, card_no = "", wallet_type = "", exp_type = "", Vehicle_type = "",
			payment_mode = "", travel_mode = "", attachment = "";
	private String format;

	SQLiteDatabase sql;
	LinearLayout len_location, len_task, len_link;
	TextInputLayout len_vehicle;
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
	ArrayList<String> Expense;

	File file;
	private static int RESULT_LOAD_IMG = 2;

	private static final int RESULT_CAPTURE_IMG = 3;
	private static final int RESULT_DOCUMENT = 4;
	private Uri outPutfileUri;
	private int APP_REQUEST_CODE = 4478;
	ImageView text_attach,text_view;


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
	private ImageView img_back,img_save,button_previous;
	private int COUNTRY=2;

	boolean Veh,Pay,Cat;

	public AddExpenseActivity_Next() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voicesearch_activity_mainv2);
	//	getSupportActionBar().setTitle("Add Expense");

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);

		context = AddExpenseActivity_Next.this;
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

		spinner_subcategory = findViewById(R.id.spinner_subcategory);
		spinner_vehicle = findViewById(R.id.spinner_vehicle);
		edt_price = findViewById(R.id.edt_price);
		edt_remark = findViewById(R.id.edt_remark);
		btn_save = findViewById(R.id.btn_save);
		edt_card = findViewById(R.id.edt_card);
		spinner_payment = findViewById(R.id.spinner_payment);
		edt_km = findViewById(R.id.edit_km);
		text_attach = findViewById(R.id.text_attach);
		len_vehicle = findViewById(R.id.len_vehicle);
		len_location = findViewById(R.id.len_location);
		edit_card = findViewById(R.id.edit_card);
		button_previous = findViewById(R.id.button_previous);
		img_back = findViewById(R.id.img_back);
		img_save = findViewById(R.id.img_save);

		text_view=findViewById(R.id.text_view);

		text_attach.setVisibility(View.VISIBLE);

		text_view.setVisibility(View.GONE);


		img_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		ed_fromPlace = (AutoCompleteTextView) findViewById(R.id.ed_fromPlace);
		ed_fromPlace.setSelection(0);
		ed_toPlace = (AutoCompleteTextView) findViewById(R.id.ed_toPlace);
		ed_toPlace.setSelection(0);

		expenseData = new ExpenseData();


		date=getIntent().getStringExtra("date");
		time=getIntent().getStringExtra("time");
		LinkTo=getIntent().getStringExtra("LinkTo");
		LinkId=getIntent().getStringExtra("LinkId");

	/*	SubcategoryAdapter = ArrayAdapter.createFromResource(AddExpenseActivity_Next.this, R.array.official, android.R.layout.simple_spinner_item);
		SubcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_subcategory.setAdapter(SubcategoryAdapter);

		SubcategoryAdapter = ArrayAdapter.createFromResource(AddExpenseActivity_Next.this, R.array.vehicle, android.R.layout.simple_spinner_item);
		SubcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_vehicle.setAdapter(SubcategoryAdapter);


		SubcategoryAdapter = ArrayAdapter.createFromResource(AddExpenseActivity_Next.this, R.array.payment, android.R.layout.simple_spinner_item);
		SubcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_payment.setAdapter(SubcategoryAdapter);*/


		getuuid();

		ed_fromPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Fromlocation = (String) parent.getItemAtPosition(position);


			}
		});

		ed_toPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tolocation = (String) parent.getItemAtPosition(position);


				CalculateDistance(Fromlocation, Tolocation);

			}
		});


		text_attach.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addMoreImages();
			}
		});
		ed_fromPlace.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.vwb_list_item));
		ed_toPlace.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.vwb_list_item));


		long date1 = System.currentTimeMillis();

		spinner_payment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Veh=false;
				Pay=true;
				Cat=false;
				Intent intent = new Intent(AddExpenseActivity_Next.this,
						CommonListActivity.class);
				intent.putExtra("option", "payment");
				startActivityForResult(intent, COUNTRY);
			}
		});


		spinner_subcategory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Veh=false;
				Pay=false;
				Cat=true;
				Intent intent = new Intent(AddExpenseActivity_Next.this,
						CommonListActivity.class);
				intent.putExtra("option", "category");
				startActivityForResult(intent, COUNTRY);
			}
		});

		spinner_vehicle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Veh=true;
				Pay=false;
				Cat=false;
				Intent intent = new Intent(AddExpenseActivity_Next.this,
						CommonListActivity.class);
				intent.putExtra("option", "vehicle");
				startActivityForResult(intent, COUNTRY);
			}
		});


		/*spinner_payment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				payment_mode = spinner_payment.getText().toString();

				if (payment_mode.equals("Credit Card") || payment_mode.equals("Debit Card")) {
					edit_card.setVisibility(View.VISIBLE);
				} else {
					edit_card.setVisibility(View.GONE);


				}
			}
		});*/

		/*spinner_subcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				subcategory="";
				subcategory = spinner_subcategory.getText().toString();
				if (subcategory.equals("Travelling")) {
					len_vehicle.setVisibility(View.VISIBLE);
					len_location.setVisibility(View.VISIBLE);


				} else {
					vehicle="";
					travel_mode="";
					edt_price.setEnabled(true);
					len_vehicle.setVisibility(View.GONE);
					len_location.setVisibility(View.GONE);

				}
			}
		});
*/
		/*spinner_vehicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				vehicle="";
				travel_mode="";
				if (subcategory.equals("Travelling")) {
					vehicle = spinner_vehicle.getText().toString();
					if (vehicle.equals("2 Wheeler")) {
						Calculation = two_wheeler;
						edt_price.setEnabled(true);
						travel_mode="Vehicle";
					} else if (vehicle.equals("4 Wheeler")) {
						Calculation = four_wheeler;
						edt_price.setEnabled(true);
						travel_mode="Vehicle";
					}else {
						travel_mode=vehicle;
						edt_price.setEnabled(true);

					}
				}

			}
		});
*/


		button_previous.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		img_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				price = edt_price.getText().toString();
				remark = edt_remark.getText().toString();
				Source = ed_fromPlace.getText().toString();
				Dest = ed_toPlace.getText().toString();
				KM = edt_km.getText().toString();


				if (vehicle.equals("2 Wheeler") || vehicle.equals("4 Wheeler")
						||vehicle.equals("Bus")||vehicle.equals("Taxi")||vehicle.equals("Airplane")
						||vehicle.equals("Train")||vehicle.equals("Auto")||vehicle.equals("Travels")) {
					if (price.equals("")) {
						Toast.makeText(AddExpenseActivity_Next.this, "Please enter amount", Toast.LENGTH_SHORT).show();
					} else {
						expenseData.setExpRecordId(uuidInString);
						expenseData.setUserMasterId(UserMasterId);
						expenseData.setCat_name(category);
						expenseData.setExpType(subcategory);
						String exp_date = getdate(date + " " + time);
						expenseData.setExpDate(exp_date);
						expenseData.setAmount(price);
						expenseData.setPaymentMode(payment_mode);
						expenseData.setAttachment(attachment);
						expenseData.setRemark(remark);
						expenseData.setFromLocation(Source);
						expenseData.setToLocation(Dest);
						if (vehicle.equals("2 Wheeler") || vehicle.equals("4 Wheeler")) {
							expenseData.setVehicleType(vehicle);
							expenseData.setTravelMode("Vehicle");
						}else {
							expenseData.setVehicleType("");
							expenseData.setTravelMode(travel_mode);
						}
						expenseData.setDistance(KM);
						expenseData.setLinkTo(LinkTo);
						expenseData.setLinkId(LinkId);

						cf.AddExpenseDetails(expenseData);

						edt_price.setText("");
						edt_remark.setText("");
						edt_card.setText("");
						ed_fromPlace.setText("");
						edt_km.setText("");
						ed_toPlace.setText("");

						Toast.makeText(AddExpenseActivity_Next
								.this, "Record save successfully", Toast.LENGTH_SHORT).show();


						if (category.equals("Official")) {
							JSONObject obj = new JSONObject();
							JSONObject jsonobj = new JSONObject();
							try {
								obj.put("UserMasterId", UserMasterId);
								obj.put("ExpType", subcategory);
								String exp_date1 = getdate(date + " " + time);
								obj.put("ExpDate", exp_date1);
								obj.put("PaymentMode", payment_mode);
								obj.put("FromLocation", Source);
								obj.put("ToLocation", Dest);
								obj.put("Distance", KM);
								obj.put("ExpRecordId", uuid);
								obj.put("Remark", remark);
								obj.put("LinkId", LinkId);
								obj.put("LinkTo", LinkTo);
								if (vehicle.equals("2 Wheeler") || vehicle.equals("4 Wheeler")) {
									obj.put("TravelMode", "Vehicle");
									obj.put("VehicleType", vehicle);
								}else {
									obj.put("TravelMode", travel_mode);
									obj.put("VehicleType", "");
								}

								obj.put("Amount", price);
								obj.put("UserName", UserName);


								jsonobj.put("ExpenseArray", obj);
								final String FinalJson = jsonobj.toString();
								String remark1 = "Expense send successfully";
								String url = CompanyURL + WebUrlClass.api_Post_PostExpenseRecord;
								String op = "true";
								CreateOfflineExpense(url, FinalJson, WebUrlClass.POSTFLAG, remark1, op);
								Log.d("JSON",FinalJson);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}


				}
				else {
					expenseData.setExpRecordId(uuidInString);
					expenseData.setUserMasterId(UserMasterId);
					expenseData.setCat_name(category);
					expenseData.setExpType(subcategory);
					String exp_date = getdate(date + " " + time);
					expenseData.setExpDate(exp_date);
					expenseData.setAmount(price);
					expenseData.setPaymentMode(payment_mode);
					expenseData.setAttachment(attachment);
					expenseData.setRemark(remark);
					expenseData.setFromLocation(Source);
					expenseData.setToLocation(Dest);
					expenseData.setVehicleType("");
					expenseData.setTravelMode("");
					expenseData.setDistance(KM);
					expenseData.setLinkTo(LinkTo);
					expenseData.setLinkId(LinkId);

					cf.AddExpenseDetails(expenseData);

					edt_price.setText("");
					edt_remark.setText("");
					edt_card.setText("");
					ed_fromPlace.setText("");
					edt_km.setText("");
					ed_toPlace.setText("");

					Toast.makeText(AddExpenseActivity_Next
							.this, "Record save successfully", Toast.LENGTH_SHORT).show();


					if (category.equals("Official")) {

						JSONObject obj = new JSONObject();
						JSONObject jsonobj = new JSONObject();
						try {
							obj.put("UserMasterId", UserMasterId);
							obj.put("ExpType", subcategory);
							String exp_date1 = getdate(date + " " + time);
							obj.put("ExpDate", exp_date1);
							obj.put("PaymentMode", payment_mode);
							obj.put("FromLocation", Source);
							obj.put("ToLocation", Dest);
							obj.put("Distance", KM);
							obj.put("ExpRecordId", uuid);
							obj.put("Remark", remark);
							obj.put("LinkId", LinkId);
							obj.put("LinkTo", LinkTo);
							if (subcategory.equals("Travelling")) {
								obj.put("TravelMode", "");
								obj.put("VehicleType", "");
							}else {
								obj.put("TravelMode", "");
								obj.put("VehicleType", "");
							}
							obj.put("Amount", price);
							obj.put("UserName", UserName);


							jsonobj.put("ExpenseArray", obj);
							final String FinalJson = jsonobj.toString();
							String remark1 = "Expense record successfully";
							String url = CompanyURL + WebUrlClass.api_Post_PostExpenseRecord;
							String op = "true";
							CreateOfflineExpense(url, FinalJson, WebUrlClass.POSTFLAG, remark1, op);

							Log.d("JSON",FinalJson);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				uuidInString = "";
				getuuid();


			}
		});


	}

	private void getuuid() {
		uuid = UUID.randomUUID();
		uuidInString = uuid.toString();
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
			startActivity(new Intent(AddExpenseActivity_Next.this, HistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
		if (id == R.id.microphone) {
			startActivity(new Intent(AddExpenseActivity_Next.this, RecycleExpenseMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}

		return super.onOptionsItemSelected(item);


	}*/

	private void CalculateDistance(final String fromlocation, final String tolocation) {

		final Thread t = new Thread() {

			public void run() {
				try {
					String url = "https://maps.googleapis.com/maps/api/directions/json?key=AIzaSyA-HkyklaKY4n4BH33QJikLXntbpLhsJk0&origin=" + URLEncoder.encode(fromlocation, "UTF-8") + "&destination=" + URLEncoder.encode(tolocation, "UTF-8") + "&sensor=false";
					Object res = OpenConnection(url, getApplicationContext());
					if (res == null) {

					} else {
						String response = res.toString();
						JSONObject jsonObject = new JSONObject(response);

						JSONArray jsonroutes = jsonObject.getJSONArray("routes");



						for (int i = 0; i < jsonroutes.length(); i++) {
							JSONObject jorder = jsonroutes.getJSONObject(i);
							JSONArray legs = jorder.getJSONArray("legs");
							for (int j = 0; j < legs.length(); j++) {
								JSONObject jlegs = legs.getJSONObject(i);
								JSONObject jdistance = jlegs.getJSONObject("distance");
								final String text = jdistance.getString("text");


								AddExpenseActivity_Next.this.runOnUiThread(new Runnable() {
									public void run() {
										edt_price.setText("");
										edt_km.setText("");

										Location_disatnce = text;

										String[] separated1 = Location_disatnce.split("km");
										String Distance_claim = separated1[0];

										if (vehicle.equals("2 Wheeler") || vehicle.equals("4 Wheeler")) {
											//edt_km.setText(Distance_claim + "KM");

											if (Distance_claim.contains(",")) {
												edt_km.setText(Distance_claim + "KM");
												Distance_claim = Distance_claim.replace(",", "");
												double calvehicle = Double.parseDouble(Distance_claim);
												String vehicledistance = String.valueOf(calvehicle * Calculation);
												edt_price.setText(String.valueOf(vehicledistance));

											} else {
												edt_km.setText(Distance_claim + "KM");

												double calvehicle = Double.parseDouble(Distance_claim);
												String vehicledistance = String.valueOf(calvehicle * Calculation);
												edt_price.setText(String.valueOf(vehicledistance));


											}

										} else {
											edt_km.setText(Distance_claim + "KM");

										}


									}
								});

							}


						}

					}
				} catch (JSONException e1) {
					e1.printStackTrace();

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}


			}


		};
		t.start();

		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList autocomplete(String input) {
		ArrayList resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?key=" + API_KEY);
			sb.append("&components=country:ind");
			sb.append("&sensor=false");
			sb.append("&types=geocode");
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			// Log.e(LOG_TAG, "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			// Log.e(LOG_TAG, "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
				System.out.println("============================================================");

				String LocationSourceDescription = predsJsonArray.getJSONObject(i).getString("description");

				resultList.add(LocationSourceDescription);
			}
		} catch (JSONException e) {
			//  Log.e(LOG_TAG, "Cannot process JSON results", e);
		}

		return resultList;
	}

	class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
		private ArrayList resultList;

		public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return String.valueOf(resultList.get(index));
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.
						resultList = autocomplete(constraint.toString());

						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}

	public String OpenConnection(String url, Context mContext) {
		String res = "";
		InputStream inputStream = null;
		try {

			HttpGet httppost = new HttpGet(url.toString());
			httppost.setHeader("Accept", "application/json");
			//  httppost.setHeader("Content-type", "application/json");
			httppost.setHeader("Content-type", "application/json");

			HttpResponse response = null;


			response = httpClient.execute(httppost);
           /* inputStream = response.getEntity().getContent();
               String result;
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";*/

			HttpEntity entity = response.getEntity();
			res = EntityUtils.toString(entity);


		} catch (Exception e) {

		}

		return res;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

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
	private void CreateOfflineSaveAttachment(String imageUri, String name, int attachmentFlAG, String remark, String activityId) {

		long a = cf.addofflinedata(imageUri, name, attachmentFlAG, remark, activityId);

		if (a != -1) {
			Toast.makeText(getApplicationContext(), "Attachment Saved", Toast.LENGTH_LONG).show();
			setJobShedulder();


		} else {
			Toast.makeText(getApplicationContext(), "Attachment not Saved", Toast.LENGTH_LONG).show();

		}
	}

	private void setJobShedulder() {
		if (myJobNew == null) {
			dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(AddExpenseActivity_Next.this));
			callJobDispacher();
		} else {
			if (!AppCommon.getInstance(this).isServiceIsStart()) {
				dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(AddExpenseActivity_Next.this));
				callJobDispacher();
			} else {
				dispatcherNew.cancelAll();
				dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(AddExpenseActivity_Next.this));
				myJobNew = null;
				callJobDispacher();
			}
		}
	}

	private void callJobDispacher() {
		myJobNew = dispatcherNew.newJobBuilder()
				// the JobService that will be called
				.setService(EnoJobService.class)
				// uniquely identifies the job
				.setTag("Eno")
				// one-off job
				.setRecurring(true)
				// don't persist past a device reboot
				.setLifetime(Lifetime.FOREVER)

				// start between 0 and 60 seconds from now
				.setTrigger(Trigger.executionWindow(0, 180))
				// don't overwrite an existing job with the same tag
				.setReplaceCurrent(true)
				// retry with exponential backoff
				.setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
				// constraints that need to be satisfied for the job to run
				.setConstraints(

						// only run on an unmetered network
						Constraint.ON_ANY_NETWORK,
						// only run when the device is charging
						Constraint.DEVICE_IDLE


				)
				.build();

		dispatcherNew.mustSchedule(myJobNew);
		AppCommon.getInstance(this).setServiceStarted(true);
	}


	private void CreateOfflineExpense(final String url, final String parameter,
									  final int method, final String remark, final String op) {
		//final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		long a = cf.addofflinedata(url, parameter, method, remark, op);
		if (a != -1) {
			Toast.makeText(getApplicationContext(), "Record saved successfully", Toast.LENGTH_LONG).show();
			Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
			intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY, WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
			startService(intent1);
			startActivity(new Intent(AddExpenseActivity_Next.this,ExpenseSelectionActivity.class)
			.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			finish();
			overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

		} else {
			Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
		}

	}


	public String getdate(String exp_date) {
		//Format of the date defined in the input String
		@SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
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


/*	public static File getOutputMediaFile(int type) {
		File mediaStorageDir;
		// External sdcard location
		mediaStorageDir = new File(Environment.getExternalStorageDirectory(), SetAppName.IMAGE_DIRECTORY_NAME);


		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(SetAppName.IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ SetAppName.IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());

		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ timeStamp + ".jpg");

			Log.d("test", "mediaFile" + mediaFile);


		}
		return mediaFile;
	}*/


	private void addMoreImages() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.choose_attachment_option_dialog);
		dialog.setTitle(getResources().getString(R.string.app_name));
		TextView camera = (TextView) dialog.findViewById(R.id.camera);
		TextView gallery = (TextView) dialog.findViewById(R.id.gallery);
		TextView textViewCancel = (TextView) dialog.findViewById(R.id.cancel);
		TextView document=dialog.findViewById(R.id.document);
		document.setVisibility(View.GONE);
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
			Toast.makeText(AddExpenseActivity_Next.this, "Please install a File Manager.",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void startCameraIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		file = new File(Environment.getExternalStorageDirectory(),
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
			InputStream inputStream=getContentResolver().openInputStream(imageUri);
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
				byte[] bytes =getFileFromPath(file);
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
			String path1 = Environment.getExternalStorageDirectory()
					.toString();
			File file = new File(path1 + "/" + "Assistant"+"/"+"Sender");
			if (!file.exists())
				file.mkdirs();
			File file1 = new File(file, "Image-"+ new Random().nextInt() + ".jpg");
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


			Toast.makeText(AddExpenseActivity_Next.this,"Image send successfully",Toast.LENGTH_SHORT).show();

			//CreateOfflineSaveAttachment(attachment,attachment,3,"Image send successfully",uuidInString);

			if (isnet()) {
				new StartSession(context, new CallbackInterface() {
					@Override
					public void callMethod() {
						new PostUploadImageMethodProspect().execute();


					}

					@Override
					public void callfailMethod(String msg) {
						ut.displayToast(AddExpenseActivity_Next.this, msg);
					}
				});
			} else {
				ut.displayToast(AddExpenseActivity_Next.this, "No Internet connection");
				//  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
			}





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
			if (requestCode == RESULT_CAPTURE_IMG && resultCode == this.RESULT_OK) {
				String uri = outPutfileUri.toString();
				Log.e("uri-:", uri);
				try {

					Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
					FileOutputStream out = new FileOutputStream(file);

					bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
					String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
					outPutfileUri = Uri.parse(url);
					if (outPutfileUri.toString().contains("content")) {
						handleSendImage(outPutfileUri);
					}else {
						File file = new File(getRealPathFromUri(AddExpenseActivity_Next.this,outPutfileUri));//create path from uri
						attachment = file.getName();
					//	CreateOfflineSaveAttachment(attachment,attachment,3,"Image send successfully",uuidInString);
					/*	if (isnet()) {
							new StartSession(context, new CallbackInterface() {
								@Override
								public void callMethod() {
									new PostUploadImageMethodProspect().execute();
								}

								@Override
								public void callfailMethod(String msg) {
									ut.displayToast(AddExpenseActivity.this, msg);
								}
							});
						} else {
							ut.displayToast(AddExpenseActivity.this, "No Internet connection");
							//  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
						}
*/
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
					}else {
						File file = new File(getRealPathFromUri(AddExpenseActivity_Next.this,outPutfileUri));//create path from uri
						attachment = file.getName();

						//CreateOfflineSaveAttachment(attachment,attachment,3,"Image send successfully",uuidInString);

						if (isnet()) {
							new StartSession(context, new CallbackInterface() {
								@Override
								public void callMethod() {
									new PostUploadImageMethodProspect().execute();
								}

								@Override
								public void callfailMethod(String msg) {
									ut.displayToast(AddExpenseActivity_Next.this, msg);
								}
							});
						} else {
							ut.displayToast(AddExpenseActivity_Next.this, "No Internet connection");
							//  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
						}

					}



					//img_userpic.setImageURI(fileUri);
					//callChangeProfileImageApi(file.getAbsoluteFile().toString());


				} else {
					Toast.makeText(this, "You haven't picked Image",
							Toast.LENGTH_LONG).show();
				}
			}else if (requestCode == RESULT_DOCUMENT && null != data) {

				Uri selectedFileURI = data.getData();
				File file = new File(getRealPathFromUri(AddExpenseActivity_Next.this,selectedFileURI));//create path from uri
				Log.d("", "File : " + file.getName());
				attachment =file.getName();
				Toast.makeText(AddExpenseActivity_Next.this,"Document send successfully",Toast.LENGTH_SHORT).show();
			//	CreateOfflineSaveAttachment(attachment,attachment,3,"Document send successfully",uuidInString);


				if (isnet()) {
					new StartSession(context, new CallbackInterface() {
						@Override
						public void callMethod() {
							new PostUploadImageMethodProspect().execute();
						}

						@Override
						public void callfailMethod(String msg) {
							ut.displayToast(AddExpenseActivity_Next.this, msg);
						}
					});
				} else {
					ut.displayToast(AddExpenseActivity_Next.this, "No Internet connection");
					//  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
				}

			}
			else if (requestCode == COUNTRY && resultCode == COUNTRY) {
				if (Veh == true) {
					vehicle="";
					travel_mode="";
					if (subcategory.equals("Travelling")) {
						vehicle = data.getStringExtra("Name");
						spinner_vehicle.setText(vehicle);
						if (vehicle.equals("2 Wheeler")) {
							Calculation = two_wheeler;
							edt_price.setEnabled(true);
							travel_mode="Vehicle";
						} else if (vehicle.equals("4 Wheeler")) {
							Calculation = four_wheeler;
							edt_price.setEnabled(true);
							travel_mode="Vehicle";
						}else {
							travel_mode=vehicle;
							edt_price.setEnabled(true);

						}
					}

				}
				if (Pay == true) {
					payment_mode = data.getStringExtra("Name");
					spinner_payment.setText(payment_mode);
					if (payment_mode.equals("Credit Card") || payment_mode.equals("Debit Card")) {
						edit_card.setVisibility(View.VISIBLE);
					} else {
						edit_card.setVisibility(View.GONE);


					}

				}
				if (Cat == true) {
					subcategory="";
					subcategory = data.getStringExtra("Name");
					spinner_subcategory.setText(subcategory);
					if (subcategory.equals("Travelling")) {
						len_vehicle.setVisibility(View.VISIBLE);
						len_location.setVisibility(View.VISIBLE);
					} else {
						vehicle="";
						travel_mode="";
						edt_price.setEnabled(true);
						len_vehicle.setVisibility(View.GONE);
						len_location.setVisibility(View.GONE);

					}

				}

			}
			else {
				if (requestCode == APP_REQUEST_CODE) {
					Toast.makeText(this, "verification cancel",
							Toast.LENGTH_LONG).show();
				} else if (requestCode == RESULT_LOAD_IMG) {
					/*Toast.makeText(this, "You haven't picked Image",
							Toast.LENGTH_LONG).show();*/
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

				String upLoadServerUri = CompanyURL + WebUrlClass.api_UploadAttechmentnew + "?AppEnvMasterId=" + URLEncoder.encode(EnvMasterId,"UTF-8") +"&ActivityId="+ uuidInString;
				FileInputStream fileInputStream = new FileInputStream(attachment);
				Object res = null;
				File file=new File(attachment);
				response = String.valueOf(Utility.OpenMultiPart(upLoadServerUri , file));
				if (response!= null && (!response.equals(""))) {
					try {


					} catch (Exception e) {
						e.printStackTrace();
					}


				} else {

				}

			} catch (Exception e) {
				e.printStackTrace();
				Log.d("ImageText",e.getMessage());
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