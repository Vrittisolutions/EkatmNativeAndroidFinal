package com.vritti.expensemanagement;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditExpensesActivity extends AppCompatActivity {


	@SuppressLint("SimpleDateFormat")
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	Date currentTime;

	Spinner spinner_category,spinner_subcategory,spinner_vehicle,spinner_payment;
	TextView txt_date,txt_time;
	EditText edt_price,edt_remark,edt_card,edt_wallet;
	Button btn_save;
	String category="",subcategory="",vehicle="",date="",time="",price,remark,card_no="",wallet_type="",exp_type="",Vehicle_type="",
    payment_mode="",TravelMode="",attachment="";
	private String format;

	SQLiteDatabase sql;
    LinearLayout len_vehicle,len_location;
	String Source="",Dest="",KM="";



	AutoCompleteTextView ed_fromPlace, ed_toPlace;
	LinearLayout len_distance;

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";

	private static final String API_KEY = "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk";
	private String Tolocation="", Fromlocation="";
	private double disatnce;
	String  Location_disatnce;
	public static DefaultHttpClient httpClient = new DefaultHttpClient();
	private Location location,location1;
	double source_lat,source_lng;
	double dest_lat,dest_lat_lng;
	EditText edt_km;
	double two_wheeler=2.5;
	double four_wheeler=7;

	double Calculation;
	ArrayList<ExpenseData> expenseDataArrayList;
	int position;
    String exp_id;
	private ArrayList<String > Categorylist;
	ImageView imageView;

	ArrayAdapter<CharSequence> CategoryArrayAdapter;
	ArrayAdapter<CharSequence> SubCategoryArrayAdapter;
	ArrayAdapter<CharSequence> VehicleArrayAdapter;
	TextView text_view,text_attach;
	String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
			UserMasterId = "", UserName = "", MobileNo = "";
	Utility ut;
	DatabaseHandlers db;
	CommonFunction cf;
	static Context context;
	private String LinkTo="",LinkId ="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voicesearch_activity_main);
        getSupportActionBar().setTitle("Edit Expense");
		context = EditExpensesActivity.this;
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


		Intent intent = getIntent();
		Bundle args = intent.getBundleExtra("BUNDLE");
		position=intent.getIntExtra("pos",0);
		expenseDataArrayList= (ArrayList<ExpenseData>) args.getSerializable("ARRAYLIST");





		spinner_category=findViewById(R.id.spinner_category);
		spinner_subcategory=findViewById(R.id.spinner_subcategory);
		spinner_vehicle=findViewById(R.id.spinner_vehicle);
		txt_date=findViewById(R.id.txt_date);
		txt_time=findViewById(R.id.txt_time);
		edt_price=findViewById(R.id.edt_price);
		edt_remark=findViewById(R.id.edt_remark);
		btn_save=findViewById(R.id.btn_save);
        edt_card=findViewById(R.id.edt_card);
        edt_wallet=findViewById(R.id.edt_wallet);
        spinner_payment=findViewById(R.id.spinner_payment);
        len_vehicle=findViewById(R.id.len_vehicle);
		len_location=findViewById(R.id.len_location);
		edt_km=findViewById(R.id.edt_km);
		imageView=findViewById(R.id.imageView);
		text_view=findViewById(R.id.text_view);
		text_attach = findViewById(R.id.text_attach);

		edt_km.setEnabled(false);


		edt_price.setEnabled(false);



		ed_fromPlace = (AutoCompleteTextView) findViewById(R.id.ed_fromPlace);
		ed_fromPlace.setSelection(0);
		ed_toPlace = (AutoCompleteTextView) findViewById(R.id.ed_toPlace);
		ed_toPlace.setSelection(0);


             price=expenseDataArrayList.get(position).getAmount();
		     Source=expenseDataArrayList.get(position).getFromLocation();
             Dest=expenseDataArrayList.get(position).getToLocation();
             KM=expenseDataArrayList.get(position).getDistance();
             remark=expenseDataArrayList.get(position).getRemark();
		     subcategory=expenseDataArrayList.get(position).getExpType();
             exp_id = expenseDataArrayList.get(position).getExpRecordId();
		     date = expenseDataArrayList.get(position).getExpDate();
		    edt_price.setText(price);
            ed_fromPlace.setText(Source);
            ed_toPlace.setText(Dest);
            edt_km.setText(KM);
            edt_price.setText(price);
            edt_remark.setText(remark);
		    txt_date.setText(date.substring(0, date.indexOf(' ')));
		    txt_time.setText(date.substring(date.indexOf(' ') + 1));

		category=expenseDataArrayList.get(position).getCat_name();
		subcategory=expenseDataArrayList.get(position).getExpType();
		vehicle=expenseDataArrayList.get(position).getVehicleType();
		TravelMode=expenseDataArrayList.get(position).getTravelMode();


		CategoryArrayAdapter = ArrayAdapter.createFromResource(EditExpensesActivity.this, R.array.category, android.R.layout.simple_spinner_item);
		CategoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_category.setAdapter(CategoryArrayAdapter);

		if (category != null) {
			int spinnerPosition = CategoryArrayAdapter.getPosition(category);
			spinner_category.setSelection(spinnerPosition);
		}

		if (category != null) {
			int spinnerPosition = CategoryArrayAdapter.getPosition(category);
			spinner_category.setSelection(spinnerPosition);
		}







		if (category.equals("Personal")){
			SubCategoryArrayAdapter = ArrayAdapter.createFromResource(EditExpensesActivity.this, R.array.subcat, android.R.layout.simple_spinner_item);
			SubCategoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_subcategory.setAdapter(SubCategoryArrayAdapter);
			if (subcategory != null) {
				int spinnerPosition = SubCategoryArrayAdapter.getPosition(subcategory);
				spinner_subcategory.setSelection(spinnerPosition);
			}

			if (subcategory != null) {
				int spinnerPosition = SubCategoryArrayAdapter.getPosition(subcategory);
				spinner_subcategory.setSelection(spinnerPosition);
			}


		}else {
			SubCategoryArrayAdapter = ArrayAdapter.createFromResource(EditExpensesActivity.this, R.array.official, android.R.layout.simple_spinner_item);
			SubCategoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_subcategory.setAdapter(SubCategoryArrayAdapter);
			if (subcategory != null) {
				int spinnerPosition = SubCategoryArrayAdapter.getPosition(subcategory);
				spinner_subcategory.setSelection(spinnerPosition);
			}

			if (subcategory != null) {
				int spinnerPosition = SubCategoryArrayAdapter.getPosition(subcategory);
				spinner_subcategory.setSelection(spinnerPosition);
			}


		}



		VehicleArrayAdapter = ArrayAdapter.createFromResource(EditExpensesActivity.this, R.array.vehicle, android.R.layout.simple_spinner_item);
		VehicleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_vehicle.setAdapter(VehicleArrayAdapter);


		if (vehicle==null){

		}else {
				int spinnerPosition = VehicleArrayAdapter.getPosition(vehicle);
				spinner_vehicle.setSelection(spinnerPosition);

		}
		/*if (TravelMode==null){

		}else {
			int spinnerPosition = VehicleArrayAdapter.getPosition(TravelMode);
			spinner_vehicle.setSelection(spinnerPosition);
		}
*/
		/*if (vehicle != null||TravelMode!=null) {
			if (vehicle.equals("2 Wheeler") || vehicle.equals("4 Wheeler")) {
				int spinnerPosition = VehicleArrayAdapter.getPosition(vehicle);
				spinner_vehicle.setSelection(spinnerPosition);
			}else {


			}
		}*/

		if (subcategory != null) {

			if (vehicle==null){

			}else {

					int spinnerPosition = VehicleArrayAdapter.getPosition(vehicle);
					spinner_vehicle.setSelection(spinnerPosition);

			}
			/*if (TravelMode==null){

			}else {
				int spinnerPosition = VehicleArrayAdapter.getPosition(TravelMode);
				spinner_vehicle.setSelection(spinnerPosition);
			}*/
		}




		attachment=expenseDataArrayList.get(position).getAttachment();
		if (attachment==null||attachment.equalsIgnoreCase("")){
			text_view.setVisibility(View.GONE);

		}else {
			text_view.setVisibility(View.VISIBLE);
		}



		text_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(EditExpensesActivity.this,ImageActivity.class)
						.putExtra("Image",attachment).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});


		if (subcategory.equalsIgnoreCase("Travelling")) {
			len_vehicle.setVisibility(View.VISIBLE);
			len_location.setVisibility(View.VISIBLE);
		}else {
			spinner_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					subcategory=parent.getSelectedItem().toString();
					if (subcategory.equals("Travelling")){
						len_vehicle.setVisibility(View.VISIBLE);
						len_location.setVisibility(View.VISIBLE);



					}else {
						len_vehicle.setVisibility(View.GONE);
						len_location.setVisibility(View.GONE);

					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});

		}




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
				if (Fromlocation.equals("")){
					Fromlocation=ed_fromPlace.getText().toString();
				}
			//	Fromlocation = (String) parent.getItemAtPosition(position);


				CalculateDistance(Fromlocation, Tolocation);

				//new CalculateDistane().execute(Fromlocation,Tolocation);
			}
		});




		/*	ed_fromPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Fromlocation = (String) parent.getItemAtPosition(position);




			}
		});

		ed_toPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tolocation = (String) parent.getItemAtPosition(position);



				if (Fromlocation.equals("")){
					Fromlocation=Source;
				}
				if (Tolocation.equals("")){
					Tolocation=Dest;
				}
				CalculateDistance(Fromlocation, Tolocation);

				//new CalculateDistane().execute(Fromlocation,Tolocation);
			}
		});*/
		ed_fromPlace.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.vwb_list_item));
		ed_toPlace.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.vwb_list_item));


		/*long date1 = System.currentTimeMillis();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String dateString = sdf.format(date1);
		txt_date.setText(dateString);

		SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm a");
		String timeString = sdf1.format(date1);
		txt_time.setText(timeString);*/

		spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			category=parent.getSelectedItem().toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
        spinner_payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payment_mode=parent.getSelectedItem().toString();

                if (payment_mode.equals("Credit Card")||payment_mode.equals("Debit Card")){
                    edt_card.setVisibility(View.VISIBLE);
                }else if (payment_mode.equals("E-Wallet")){
                    edt_wallet.setVisibility(View.VISIBLE);
                }else {
                    edt_card.setVisibility(View.GONE);
                    edt_wallet.setVisibility(View.GONE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
		/*spinner_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				subcategory=parent.getSelectedItem().toString();
				if (subcategory.equals("Travel")){
                    len_vehicle.setVisibility(View.VISIBLE);
					len_location.setVisibility(View.VISIBLE);



                }else {
                    len_vehicle.setVisibility(View.GONE);
					len_location.setVisibility(View.GONE);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
*/

		spinner_vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


				vehicle=parent.getSelectedItem().toString();

				if (vehicle.equals("2 Wheeler")){
					Calculation=two_wheeler;
				}
				else if (vehicle.equals("4 Wheeler")){
					Calculation=four_wheeler;
				}



			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

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
				DatePickerDialog datePickerDialog = new DatePickerDialog(EditExpensesActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker datePicker, int year,
												  int monthOfYear, int dayOfMonth) {
								//    datePicker.setMinDate(c.getTimeInMillis());
								date =  dayOfMonth + "-"
										+ String.format("%02d", (monthOfYear + 1))
										+ "-" + year;
								 txt_date.setText(date);
							}
						}, year, month, day);
				datePickerDialog.setTitle("Select Date");

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

				mTimePicker = new TimePickerDialog(EditExpensesActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view, int hourOfDay,
												  int minute) {

								if (hourOfDay == 0) {

									hourOfDay += 12;

									format = "AM";
								}
								else if (hourOfDay == 12) {

									format = "PM";

								}
								else if (hourOfDay > 12) {

									hourOfDay -= 12;

									format = "PM";

								}
								else {

									format = "AM";
								}


								txt_time.setText(hourOfDay + ":" + minute + format);
							}
						}, hour, minute, false);
				mTimePicker.show();

			}
		});
			btn_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				price = edt_price.getText().toString();
				remark = edt_remark.getText().toString();
				date = txt_date.getText().toString();
				time = txt_time.getText().toString();
				Source = ed_fromPlace.getText().toString();
				Dest = ed_toPlace.getText().toString();
				KM = edt_km.getText().toString();

				if (vehicle.equals("2 Wheeler") || vehicle.equals("4 Wheeler")) {
					if (price.equals("")) {
						Toast.makeText(EditExpensesActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
					}else {
						ExpenseData expenseData=new ExpenseData();
						expenseData.setUserMasterId(UserMasterId);
						expenseData.setCat_name(category);
						expenseData.setExpType(subcategory);
						expenseData.setExpDate(date+" "+time);
						expenseData.setAmount(price);
						expenseData.setPaymentMode(payment_mode);
						expenseData.setAttachment(attachment);
						expenseData.setRemark(remark);
						expenseData.setFromLocation(Source);
						expenseData.setToLocation(Dest);
						expenseData.setVehicleType(vehicle);
						expenseData.setTravelMode("");
						expenseData.setDistance(KM);
						expenseData.setLinkTo(LinkTo);
						expenseData.setLinkId(LinkId);

						cf.UpdateExpenseDetails(expenseData,exp_id);

						edt_price.setText("");
						edt_remark.setText("");
						edt_card.setText("");
						edt_wallet.setText("");
						ed_fromPlace.setText("");
						edt_km.setText("");
						ed_toPlace.setText("");

						Toast.makeText(EditExpensesActivity
								.this,"Record updated successfully",Toast.LENGTH_SHORT).show();
						startActivity(new Intent(EditExpensesActivity.this,AddExpenseActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						finish();
					}
				}else {
					ExpenseData expenseData=new ExpenseData();
					expenseData.setUserMasterId(UserMasterId);
					expenseData.setCat_name(category);
					expenseData.setExpType(subcategory);
					expenseData.setExpDate(date+" "+time);
					expenseData.setAmount(price);
					expenseData.setPaymentMode(payment_mode);
					expenseData.setAttachment(attachment);
					expenseData.setRemark(remark);
					expenseData.setFromLocation(Source);
					expenseData.setToLocation(Dest);
					expenseData.setVehicleType("");
					expenseData.setTravelMode(vehicle);
					expenseData.setDistance(KM);
					expenseData.setLinkTo(LinkTo);
					expenseData.setLinkId(LinkId);

					cf.UpdateExpenseDetails(expenseData,exp_id);

					edt_price.setText("");
					edt_remark.setText("");
					edt_card.setText("");
					edt_wallet.setText("");
					ed_fromPlace.setText("");
					edt_km.setText("");
					ed_toPlace.setText("");

					Toast.makeText(EditExpensesActivity
							.this,"Record updated successfully",Toast.LENGTH_SHORT).show();
					startActivity(new Intent(EditExpensesActivity.this,AddExpenseActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				}







			}
		});




	}

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


								EditExpensesActivity.this.runOnUiThread(new Runnable() {
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

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main_widget, menu);
		return super.onCreateOptionsMenu(menu);

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
        *//*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*//*
      *//*  if(id == R.id.SupportStaff){
        }*//*

		if (id == R.id.proceed) {
			startActivity(new Intent(EditExpensesActivity.this,HistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
		if (id == R.id.microphone) {
			startActivity(new Intent(EditExpensesActivity.this,RecycleExpenseMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}

		return super.onOptionsItemSelected(item);


	}
*/
/*
	private void CalculateDistance(final String fromlocation, final String tolocation) {

		final Thread t = new Thread() {

			public void run() {
				try {
					String url = "https://maps.googleapis.com/maps/api/directions/json?key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk&origin=" + URLEncoder.encode(fromlocation, "UTF-8") + "&destination=" + URLEncoder.encode(tolocation, "UTF-8") + "&sensor=false";
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
								final String  text = jdistance.getString("text");
								final String[] separated = text.split("km");
								if (text.contains(".")) {
									*/
/*disatnce = Double.parseDouble(text);
									final String D = String.valueOf(Math.ceil(disatnce));
									String[] separated1 = D.split("\\.");
									final String Distance_claim= separated1[0];
									Log.d("KM",Distance_claim);
					*//*
			//	edt_km.setText(text);


								}else {
									EditExpensesActivity.this.runOnUiThread(new Runnable() {
										public void run() {
											Location_disatnce = text;
											String[] separated1 = Location_disatnce.split("km");
											String Distance_claim= separated1[0];
											edt_km.setText(Distance_claim+"KM");

											if (Distance_claim.contains(",")){
												Distance_claim = Distance_claim.replace(",", "");

												double calvehicle= Double.parseDouble(Distance_claim);
												String vehicledistance= String.valueOf(calvehicle*Calculation);
												edt_price.setText(String.valueOf(vehicledistance));

											}else {

												double calvehicle= Double.parseDouble(Distance_claim);
												String vehicledistance= String.valueOf(calvehicle*Calculation);
												edt_price.setText(String.valueOf(vehicledistance));

												// ed_travel.setBackground(getResources().getDrawable(R.drawable.edit_text));

											}

											//final String KM = String.valueOf(Math.ceil(disatnce));


										}
									});
								*/
/*	Intent intent = new Intent();
									intent.putExtra("source", Fromlocation);
									intent.putExtra("dest", Tolocation);
									intent.putExtra("km", text);
									setResult(10,intent);
									finish();*//*

								}

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
*/

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
	public  String OpenConnection(String url, Context mContext) {
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

	}

}
