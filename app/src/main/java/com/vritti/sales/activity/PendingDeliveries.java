package com.vritti.sales.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.PendingDeliveryAdapter;
import com.vritti.sales.beans.ShipmentEntryBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/*new*/
public class PendingDeliveries extends AppCompatActivity {
	private Context parent;
	Toolbar toolbar;
	TextView txtnoordnote;
	ListView list_pending_deliveries;
	ProgressBar mprogress;

	Utility ut;
	static DatabaseHandlers db;
	Tbuds_commonFunctions tcf;
	static String settingKey = "";
	String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
			UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "",  usertype = "", username = "",numTomakeCall = "";
	String IsChatApplicable, IsGPSLocation;
	public static SQLiteDatabase sql;
	SharedPreferences sharedpreferences;

	ArrayList<ShipmentEntryBean> list_deliveries;
	ArrayList<ShipmentEntryBean>  newList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tbuds_activity_pending_deliveries);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Pending Deliveries");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
		}else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
		}

		init();

		//if merchantid empty callthis api then call pendingdeliveries
		if(AnyMartData.MerchantID.equalsIgnoreCase("")){
			getMerchantId();
		}else {
			//get data from server
			if (isnet()) {

				mprogress.setVisibility(View.VISIBLE);
				new StartSession(parent, new CallbackInterface() {
					@Override
					public void callMethod() {

						new DownloadPendingDeliveriesJSON().execute();
					}
					@Override
					public void callfailMethod(String msg) {

					}
				});
			}
		}

		setListeners();
	}

	public void init(){
		parent = PendingDeliveries.this;

		toolbar = (Toolbar) findViewById(R.id.toolbar1);
		toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
		toolbar.setTitle(R.string.app_name_toolbar_sales);
		toolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(toolbar);

		list_pending_deliveries = findViewById(R.id.list_pending_deliveries);
		mprogress = findViewById(R.id.toolbar_progress_Assgnwork);
		txtnoordnote = findViewById(R.id.txtnoordnote);

		sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);

		ut = new Utility();
		tcf = new Tbuds_commonFunctions(PendingDeliveries.this);
		String settingKey = ut.getSharedPreference_SettingKey(parent);
		String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
		db = new DatabaseHandlers(parent, dabasename);
		sql = db.getWritableDatabase();
		CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
		EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
		PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
		LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
		Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
		UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
		UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
		MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);
		AnyMartData.MerchantID = sharedpreferences.getString("MerchantID","");
		AnyMartData.MerchantName = sharedpreferences.getString("MerchantName","");
		AnyMartData.selected_MERCHID = sharedpreferences.getString("SelMerchId","");
		AnyMartData.SHIPToAddr = sharedpreferences.getString("SHIPToAddr","");
		AnyMartData.SHIPTOMASTERID = sharedpreferences.getString("ShipToId","");
		AnyMartData.LATITUDE = sharedpreferences.getString("Latitude","");
		AnyMartData.LONGITUDE = sharedpreferences.getString("Longitude","");
		AnyMartData.CITY = sharedpreferences.getString("City","");
		AnyMartData.PINCODE = sharedpreferences.getString("Pincode","");
		AnyMartData.ADDRESS = sharedpreferences.getString("Address","");
		AnyMartData.selected_BSEGMENTDESC = sharedpreferences.getString("SelBSegDesc","");
		AnyMartData.selected_BSEGMENTCODE = sharedpreferences.getString("SelBSegCode","");
		AnyMartData.selected_BSEGMENTID = sharedpreferences.getString("SelBSegId","");
		AnyMartData.SHOPBYMODE = sharedpreferences.getString("SHOPBYMODE","ShopBySpeciality");
		AnyMartData.STATE = sharedpreferences.getString("State","");
		AnyMartData.SpecImgPath = sharedpreferences.getString("SpecImgPath","");
		AnyMartData.USER_ID = sharedpreferences.getString("CustVendorMasterId","");

		AnyMartData.MODULE = "ORDERBILLING";
		AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
		usertype = "C";

		CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
		AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

		list_deliveries = new ArrayList<ShipmentEntryBean>();

	}

	public void setListeners(){
		list_pending_deliveries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(PendingDeliveries.this, DeliveryAgentSelection.class);
				intent.putExtra("IntentFrom","PendingDelivery");
				intent.putExtra("InvoiceNo",list_deliveries.get(position).getInvoiceNo());
				intent.putExtra("DeliveryDate",list_deliveries.get(position).getScheduleDate());
				intent.putExtra("ConsigneeName",list_deliveries.get(position).getConsignee());
				intent.putExtra("DelvAddress",list_deliveries.get(position).getDeliveryAddress());
				intent.putExtra("SONO_frmIntent",list_deliveries.get(position).getSOno());
				intent.putExtra("OrderType", AnyMartData.Order_Type);
				intent.putExtra("ShipToMasterId",list_deliveries.get(position).getShipToMasterId());
				intent.putExtra("SODate",list_deliveries.get(position).getScheduleDate());
				intent.putExtra("OrderTypeMasterId",list_deliveries.get(position).getOrderTypeMasterId());
				intent.putExtra("prfDelFrmTime",list_deliveries.get(position).getPrefDelFrmTime());
				intent.putExtra("prfDelToTime",list_deliveries.get(position).getPrefDelToTime());
				startActivity(intent);
				finish();
			}
		});

	}

	private boolean isnet() {
		// TODO Auto-generated method stub
		Context context = this.getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	class DownloadPendingDeliveriesJSON extends AsyncTask<String, Void, String> {
		Object res;
		String response;
		JSONArray jResults;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//mprogress.setVisibility(View.VISIBLE);
			Toast.makeText(parent,""+parent.getResources().getString(R.string.serchpndingdel), Toast.LENGTH_SHORT).show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String url = CompanyURL + WebUrlClass.api_GetPendingDeliveries+
						//"?MerchantId=" + AnyMartData.MerchantID;
						"?MerchantId=" + "";

				res = Utility.OpenconnectionOrferbilling(url,parent);

				if (res != null) {
					response = res.toString().replaceAll("\\\\", "");
					response = res.toString().replaceAll("^\"+ \"+$","");
					//response = response.substring(1, response.length()-1);
					jResults = new JSONArray(response);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String integer) {
			super.onPostExecute(integer);

			mprogress.setVisibility(View.GONE);

			try{
				if(jResults != null){
					txtnoordnote.setVisibility(View.GONE);
					parsePendingDeliveriesJson(jResults);
				}else {
					txtnoordnote.setVisibility(View.VISIBLE);
					txtnoordnote.setText(parent.getResources().getString(R.string.nopnddel));
					//Toast.makeText(parent,""+parent.getResources().getString(R.string.nopnddel), Toast.LENGTH_SHORT).show();
				}
			}catch (Exception e){

			}
		}
	}

	public void parsePendingDeliveriesJson(JSONArray jResults){
		tcf.clearTable(parent, DatabaseHandlers.TABLE_PENDING_DELIVERY);
		list_deliveries.clear();

		String InvoiceNo = "", InvoiceDt = "", SODate = "", ConsigneeName = "", Address = "",
				PrefDelFrmTime = "", PrefDelToTime = "", Mobile = "",ShipToMasterId = "", OrderTypeMasterId = "", SONo = "",
				deliveryMode = "",Longitude="",Latitude="",PaymentStatus="",PaymentMode="",TransactionId="",
				AmountStatus="",TransactionDate="",AddedBy="";
		String ItemDesc="",UOMCode="",Brand="",Content="",Qty="",Rate="",NetAmt="";

		for(int i=0; i<=jResults.length();i++){
			try {
				JSONObject jsonObject = jResults.getJSONObject(i);
				InvoiceNo = jsonObject.getString("InvoiceNo");
				InvoiceDt = jsonObject.getString("InvoiceDt");
				SODate = jsonObject.getString("SODate");
				ShipToMasterId = jsonObject.getString("ShipToMasterId");
				OrderTypeMasterId = jsonObject.getString("OrderTypeMasterId");
				SONo = jsonObject.getString("SONo");
				ConsigneeName = jsonObject.getString("ConsigneeName");
				Address = jsonObject.getString("Address");
				PrefDelFrmTime = jsonObject.getString("PrefDelFrmTime");
				PrefDelToTime = jsonObject.getString("PrefDelToTime");
				Mobile = jsonObject.getString("Mobile");
				Latitude = jsonObject.getString("Latitude");
				Longitude = jsonObject.getString("Longitude");
				try{
					deliveryMode = jsonObject.getString("deliveryterms");
					PaymentStatus = jsonObject.getString("PaymentStatus");
					TransactionId = jsonObject.getString("TransactionId");
					TransactionDate = jsonObject.getString("TransactionDate");
					PaymentMode = jsonObject.getString("PaymentMode");
					AmountStatus = jsonObject.getString("AmountStatus");
					ItemDesc = jsonObject.getString("ItemDesc");
					UOMCode = jsonObject.getString("UOMCode");
					Brand = jsonObject.getString("Brand");
					Content = jsonObject.getString("Content");
					Qty = jsonObject.getString("Qty");
					Rate = jsonObject.getString("Rate");
					NetAmt = jsonObject.getString("NetAmt");
					AddedBy = jsonObject.getString("AddedBy");
				}catch (Exception e){
					e.printStackTrace();
				}

				String[] idate = InvoiceDt.split("T");
				InvoiceDt = idate[0] + " " + idate[1];

				String[] sdate = SODate.split("T");
				SODate =sdate[0] + " " + sdate[1];

				ShipmentEntryBean sbean = new ShipmentEntryBean();
				sbean.setInvoiceNo(InvoiceNo);
				sbean.setScheduleDate(SODate);
				sbean.setConsignee(ConsigneeName);
				sbean.setInvoiceDate(InvoiceDt);
				sbean.setDeliveryAddress(Address);
				sbean.setSOno(SONo);
				sbean.setOrderTypeMasterId(OrderTypeMasterId);
				sbean.setOrderType("DefData1");
				sbean.setShipToMasterId(ShipToMasterId);
				sbean.setPrefDelFrmTime(PrefDelFrmTime);
				sbean.setPrefDelToTime(PrefDelToTime);
				sbean.setMobile(Mobile);
				sbean.setDeliveryTerms(deliveryMode);
				sbean.setLatitude(Latitude);
				sbean.setLongitude(Longitude);
				sbean.setPaymentStatus(PaymentStatus);
				sbean.setPaymentMode(PaymentMode);
				sbean.setTransactionId(TransactionId);
				sbean.setTransactionDate(TransactionDate);
				sbean.setAmountStatus(AmountStatus);
				sbean.setItemDesc(ItemDesc);
				sbean.setUOMCode(UOMCode);
				sbean.setBrand(Brand);
				sbean.setContent(Content);
				sbean.setQty(Qty);
				sbean.setRate(Rate);
				sbean.setSubtotal_Amt(Float.valueOf(NetAmt));
				sbean.setAddedBy(AddedBy);

				list_deliveries.add(sbean);

				tcf.insertPendingDeliveries(InvoiceNo,InvoiceDt,SODate,ShipToMasterId,OrderTypeMasterId,SONo,ConsigneeName,Address,
						PrefDelFrmTime,PrefDelToTime,Mobile,Latitude,Longitude,deliveryMode,PaymentStatus,TransactionId,TransactionDate,
						PaymentMode,AmountStatus,ItemDesc,UOMCode,Brand,Content,Qty,Rate,NetAmt);

				Collections.sort(list_deliveries, new Comparator<ShipmentEntryBean>() {
					@Override
					public int compare(ShipmentEntryBean lhs, ShipmentEntryBean rhs) {
						return rhs.getInvoiceNo().compareTo(lhs.getInvoiceNo());
					}
				});

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		System.out.println(list_deliveries);
		Set<ShipmentEntryBean> set = new TreeSet<ShipmentEntryBean>(new Comparator<ShipmentEntryBean>() {
			@Override
			public int compare(ShipmentEntryBean o1, ShipmentEntryBean o2) {
				String a = o1.getInvoiceNo();
				String b = o2.getInvoiceNo();
				Log.e("", "" + a + " " + b);
				if (o1.getInvoiceNo().equalsIgnoreCase(o2.getInvoiceNo())) {
					return 0;
				}
				return 1;
			}
		});
		set.addAll(list_deliveries);
		System.out.println(list_deliveries);

		newList = new ArrayList<ShipmentEntryBean>(set);

		System.out.println(newList);

		//historyBeanList = new ArrayList<OrderHistoryBean>(set);
		System.out.println(list_deliveries);
		int i2 = list_deliveries.size();
		Log.e("", "" + i2);

		list_pending_deliveries.setAdapter(new PendingDeliveryAdapter(this, newList));

		if(list_deliveries.size() == 0){
			txtnoordnote.setVisibility(View.VISIBLE);
			txtnoordnote.setText(parent.getResources().getString(R.string.nopnddel));
		}else {
			txtnoordnote.setVisibility(View.GONE);
		}
	}

	public void MakeCall(String mobile){
		numTomakeCall = mobile;
		try{
			if (ActivityCompat.checkSelfPermission(PendingDeliveries.this.getApplicationContext(), Manifest.permission.CALL_PHONE) ==
					PackageManager.PERMISSION_GRANTED) {

				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:+91"+mobile));
				startActivity(callIntent);
			}
			else
			{
				ActivityCompat.requestPermissions(PendingDeliveries.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public String Convertdate(String date){
		String DateToStr="";

		//SimpleDateFormat Format = new SimpleDateFormat("dd-MM-yyyy");//Feb 23 2016 12:16PM
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		SimpleDateFormat Format = new SimpleDateFormat("EEE dd MMM yyyy");//Feb 23 2016 12:16PM
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date d1 = null;
		try {
			d1 = format.parse(date);
			//DateToStr = toFormat.format(date);
			DateToStr = Format.format(d1);
			// DateToStr = format.format(d1);
			System.out.println(DateToStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return DateToStr;
	}

	public void share(String sono, String amt, String status, String custName, String custMob, String custAddr,
					  String custLat, String custLng, String sohdrId, String delDate, String frmTime, String toTime){

		String payment = "", delTime = "";
		if(status.equalsIgnoreCase("42")){
			payment = "Paid";
		}else {
			payment = "Unpaid";
		}

		delTime= Convertdate(delDate) +", "+frmTime +" "+getResources().getString(R.string.to)+" "+ toTime;

		ArrayList<String> itemlist = new ArrayList<String>();
		itemlist.clear();

		for(int i=0; i<list_deliveries.size();i++){
			if(sono.equalsIgnoreCase(list_deliveries.get(i).getSOno())){
				//add to list string
				String itmname="",content="",UOMCode="",Brand="",Rate="",Qty="";
				itmname = list_deliveries.get(i).getItemDesc();
				content = list_deliveries.get(i).getContent();
				UOMCode = list_deliveries.get(i).getUOMCode();
				Brand = list_deliveries.get(i).getBrand();
				float Rate1 = Float.parseFloat(list_deliveries.get(i).getRate());

				if(content.contains(".0")){
					content = content.replace(".0","");
				}else {
					content = content;
				}

				if(list_deliveries.get(i).getQty().contains(".0")){
					Qty = list_deliveries.get(i).getQty().replace(".0","");
				}else {
					Qty = list_deliveries.get(i).getQty();
				}

				//String mrp = list_deliveries.get(i).get();
				//String range = list_deliveries.get(i).getRange();

				String product="";
				product = Brand+" - "+itmname+", "+content+" "+UOMCode+" - "+Qty+" x "+String.format("%.2f",Rate1);

				itemlist.add(product);
			}else {
				//do not add ignore
			}
		}

		try {
			Intent i = new Intent(android.content.Intent.ACTION_SEND);
			i.setType("text/plain"); /*image/*,*/
			// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra(Intent.EXTRA_SUBJECT, "Any Dukaan" /*AnyMartData.MODULE + "App"*/);
			//String msg = "\n Let me recommend you Any Dukaan application\n\n";

			//content to send
			String msg = getResources().getString(R.string.greetings);
			msg += "\n"+getResources().getString(R.string.ordno)+" - "+sono;
			msg += "\n"+getResources().getString(R.string.ordamt)+" - "+amt+" ₹";
			msg += "\n"+getResources().getString(R.string.status)+" - "+payment;
			msg += "\n\n"+getResources().getString(R.string.custdtl);
			msg += "\n"+getResources().getString(R.string.name)+" - "+custName;
			msg += "\n"+getResources().getString(R.string.mobile)+" - "+custMob;
			msg += "\n"+getResources().getString(R.string.address)+" - "+custAddr;
			//String mapaddr = "http://maps.google.com/maps?q=loc:" + "18.4668454"+ "," + "73.7812046";
			String mapaddr = "http://maps.google.com/maps?q=loc:" + custLat+ "," + custLat;
			msg += "\n"+mapaddr;
			msg += "\n"+getResources().getString(R.string.expdelon)+" "+delTime;
			msg += "\n\n"+getResources().getString(R.string.prod)+" : ";
			for (int k=0; k<itemlist.size(); k++){
				msg += "\n"+itemlist.get(k).toString().trim();
			}
			//msg += "\n\n*************************";

			String url1= "<a href= 'https://play.google.com/store/apps/details?id=com.vritti.freshmart'>https://play.google.com/store/apps/details?id=com.vritti.freshmart</a>";
			//Uri linkurl = Uri.parse(url1);
			Uri imageUri = Uri.parse("android.resource://" + parent.getPackageName()+ "/drawable/" + "anydukan_2");
			i.putExtra(Intent.EXTRA_TEXT, msg+"\n\n"+getResources().getString(R.string.clickhere)+"\n"+Html.fromHtml(url1));
			//i.putExtra(Intent.EXTRA_STREAM, imageUri);
			i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			startActivity(Intent.createChooser(i, getResources().getString(R.string.choseone)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getMerchantId(){
		new DownloadReferenceJSON().execute();
	}

	class DownloadReferenceJSON extends AsyncTask<String, Void, String> {
		Object res;
		String response;
		JSONArray jResults;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String url = CompanyURL + WebUrlClass.api_get_Reference + "?LeadWise=V";

				res = ut.OpenConnection(url);
				if (res != null) {
					response = res.toString().replaceAll("\\r\\n","");
					response = response.toString().replaceAll("\\\\", "");
					response = response.substring(1, response.length() - 1);
					ContentValues values = new ContentValues();
					jResults = new JSONArray(response);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String integer) {
			super.onPostExecute(integer);

			//parse it here and set to list and set list to adapter
			try{
				if(jResults != null){
					for(int i=0; i<=jResults.length();i++){
						try {
							JSONObject jsonObject = jResults.getJSONObject(i);
							AnyMartData.MerchantName = jsonObject.getString("CustVendorName");
							AnyMartData.MerchantID = jsonObject.getString("CustVendorMasterId");
							String Mobile = jsonObject.getString("Mobile");
							String Email = jsonObject.getString("Email");
							String Address = jsonObject.getString("Address");

							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("MerchantID",  AnyMartData.MerchantID);
							editor.putString("MerchantName",  AnyMartData.MerchantName);
							editor.commit();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}else {
				}

				if (isnet()) {
					new StartSession(parent, new CallbackInterface() {
						@Override
						public void callMethod() {

							new DownloadPendingDeliveriesJSON().execute();
						}
						@Override
						public void callfailMethod(String msg) {

						}
					});
				}

			}catch (Exception e){

			}
		}
	}

}
