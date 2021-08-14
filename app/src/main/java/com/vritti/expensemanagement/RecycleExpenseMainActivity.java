package com.vritti.expensemanagement;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.skyfishjy.library.RippleBackground;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.BuildConfig;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class RecycleExpenseMainActivity extends AppCompatActivity implements
		RecognitionListener {
	public ArrayList<Expenses> text_user = new ArrayList<>();
	public static final int REQ_CODE_SPEECH_INPUT = 1;
	public Intent recognizer_intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    public static final String DEFAULT_MESSAGE = "Welcome to expense management how can i help you?";
	private static final int REQUEST_RECORD_PERMISSION = 100;

	Expenses expenses;
    TextToSpeech t1;
    AssistantAdapter assistantAdapter;
    private ArrayList<String> text;
    private String assistant="",dateString="",timeString="";
     ExpenseData expenseData;
	boolean amount=false;
	Button btn_save;
	private String price="";
	SQLiteDatabase sql;
	private String source="",dest="",km="",Attachment="";
	String Remark="";

	File file;
	private static int RESULT_LOAD_IMG = 2;

	private static final int RESULT_CAPTURE_IMG = 3;
    private static final int RESULT_DOCUMENT = 4;
	private Uri outPutfileUri;
	private int APP_REQUEST_CODE = 4478;
	private String vehicle="";


	double Calculation;
	String Amount="0";
	private ImageView foundDevice;
	private SpeechRecognizer speech = null;
	private Intent recognizerIntent;
	private String text_for_assistant;
	TextView txt_msg;


	String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
			UserMasterId = "", UserName = "", MobileNo = "";
	Utility ut;
	DatabaseHandlers db;
	CommonFunction cf;
	static Context context;
	String uuidInString = "";
	private UUID uuid;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assistant_activity_main);
		Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
		topToolBar.setTitleTextColor(Color.WHITE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
		}
		expenseData=new ExpenseData();
		context = RecycleExpenseMainActivity.this;
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


		t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					int ttsLang = t1.setLanguage(Locale.US);

					if (ttsLang == TextToSpeech.LANG_MISSING_DATA
							|| ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
						Log.e("TTS", "The Language is not supported!");
					} else {
						Log.i("TTS", "Language Supported.");
					}
					Log.i("TTS", "Initialization success.");
				} else {
					Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
				}
			}
		});





		long date1 = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		dateString = sdf.format(date1);

		SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm a");
		timeString = sdf1.format(date1);

		Button speak = findViewById(R.id.speak_button);
		btn_save = findViewById(R.id.btn_save);
		txt_msg = findViewById(R.id.txt_msg);
        RecyclerView sent_to_assistant = findViewById(R.id.assitant_response);
		getspeech();
		getconvert("Book expenses");


		text_user.add(new Expenses(UserPreference.book, UserPreference.SENT_TYPE));
		getconvert(UserPreference.book);


		assistantAdapter=new AssistantAdapter(RecycleExpenseMainActivity.this,text_user);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
		sent_to_assistant.setLayoutManager(mLayoutManager);
        sent_to_assistant.setAdapter(assistantAdapter);






		final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);

		final Handler handler=new Handler();

		foundDevice=(ImageView)findViewById(R.id.foundDevice);
		ImageView button=(ImageView)findViewById(R.id.centerImage);



		rippleBackground.startRippleAnimation();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				foundDevice();
			}
		},3000);

		/*button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				rippleBackground.startRippleAnimation();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						foundDevice();
					}
				},3000);
			}
		});*/

		/*recognizer_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
		try {
			startActivityForResult(recognizer_intent, REQ_CODE_SPEECH_INPUT);
		}
		catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(), "Opps! Your device doesn’t support Speech to Text", Toast.LENGTH_SHORT).show();
		}*/

		/*speak.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					startActivityForResult(recognizer_intent, REQ_CODE_SPEECH_INPUT);
				}
				catch (ActivityNotFoundException a) {
					Toast.makeText(getApplicationContext(), "Opps! Your device doesn’t support Speech to Text", Toast.LENGTH_SHORT).show();
				}
			}
		});*/


		btn_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Amount.equals("")){
					Toast.makeText(RecycleExpenseMainActivity.this,"Please enter amount",Toast.LENGTH_SHORT).show();
				}else {
					uuid = UUID.randomUUID();
					uuidInString = uuid.toString();
					expenseData.setExpRecordId(uuidInString);
					expenseData.setExpDate(dateString + " " + timeString);
					expenseData.setRemark(Remark);
					expenseData.setFromLocation(source);
					expenseData.setToLocation(dest);
					//expenseData.setExp_type(vehicle);
					expenseData.setDistance(km+"KM");
					expenseData.setAmount(Amount);
					expenseData.setUserMasterId(UserMasterId);
					cf.AddExpenseDetails(expenseData);
					Toast.makeText(RecycleExpenseMainActivity.this,"Record save successfully",Toast.LENGTH_SHORT).show();
                    getconvert("Record save successfully");
					startActivity(new Intent(RecycleExpenseMainActivity.this,HistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();
				}

			}
		});
/*
		if("hello".matches(UserPreference.hello)){
			getconvert(UserPreference.hello);
			String text_for_assistant = "Welcome to expense management how can i help you?";
			//text_user.add(new Expenses(UserPreference.hello ,UserPreference.SENT_TYPE ));
			text_user.add(new Expenses(text_for_assistant , UserPreference.RECEIVE_TYPE ));
			getconvert(text_for_assistant);

		}*/

	}

	private void getspeech() {
		speech = SpeechRecognizer.createSpeechRecognizer(this);
		speech.setRecognitionListener(this);
		recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
				"en");
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

		ActivityCompat.requestPermissions
				(RecycleExpenseMainActivity.this,
						new String[]{Manifest.permission.RECORD_AUDIO},
						REQUEST_RECORD_PERMISSION);

	}

	public void cash(Context context,String cash) {


		if(cash.matches(UserPreference.cash)){
			getconvert(UserPreference.cash);
			String  text_for_assistant = "How much amount you pay?";

			//text_user.add(new Expenses(UserPreference.cash ,UserPreference.SENT_TYPE ));
			text_user.add(new Expenses(text_for_assistant , UserPreference.RECEIVE_TYPE ));
			getconvert(text_for_assistant);
			expenseData.setPaymentMode("Cash");
			assistantAdapter.notifyDataSetChanged();
		}

	}
	public void ewallet(Context context,String cash) {

		if(cash.matches(UserPreference.ewallet)){
			getconvert(UserPreference.ewallet);
			String  text_for_assistant = "What type of wallet?";

			//text_user.add(new Expenses(UserPreference.cash ,UserPreference.SENT_TYPE ));
			text_user.add(new Expenses(text_for_assistant , UserPreference.RECEIVE_TYPE ));
			getconvert(text_for_assistant);
			expenseData.setPaymentMode("E-Wallet");

			assistantAdapter.notifyDataSetChanged();
		}

	}
	public void Official(Context context,String official) {

		if(official.matches(UserPreference.official)){
			expenseData.setCat_name("Official");
			text_for_assistant = "What type of expenses ?"+"\n"+"Travel,Local,Lodging,Maintenance,Food";
			text_user.add(new Expenses(UserPreference.official, UserPreference.SENT_TYPE));
			text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
			getconvert(text_for_assistant);
			assistantAdapter.notifyDataSetChanged();
		}

	}
	public void Personal(Context context,String personal) {

		if(personal.matches(UserPreference.personal)){
			expenseData.setCat_name("Personal");
			text_for_assistant = "For Grocery Movie Doctor Hotel Hospital Food";
			text_user.add(new Expenses(UserPreference.personal, UserPreference.SENT_TYPE));
			text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
			getconvert(text_for_assistant);
			assistantAdapter.notifyDataSetChanged();
		}

	}

	public void Travel(Context context,String travel) {

		if(travel.matches("Travel")){
			expenseData.setExpType("Travelling");
			text_for_assistant = "2 wheeler 4 wheeler Bus Taxi Train Flight";
			text_user.add(new Expenses("Travel", UserPreference.SENT_TYPE));
			text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
			getconvert(text_for_assistant);
			assistantAdapter.notifyDataSetChanged();
		}

	}
	public void TwoWheeler(Context context,String two) {

		if(two.matches(UserPreference.two)){
			text_for_assistant = "Please select location?";
			text_user.add(new Expenses("2 wheeler", UserPreference.SENT_TYPE));
			text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
			getconvert(text_for_assistant);

			Intent intent = new Intent(RecycleExpenseMainActivity.this, LocationActivity.class);
			intent.putExtra("vehicle", UserPreference.two);
			startActivityForResult(intent, 10);
			text = null;
			assistantAdapter.notifyDataSetChanged();
		}

	}
	public void FourWheeler(Context context,String four) {

		if(four.matches(UserPreference.four)){
			text_for_assistant = "Please select location?";
			text_user.add(new Expenses("4 wheeler", UserPreference.SENT_TYPE));
			text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
			getconvert(text_for_assistant);

			Intent intent = new Intent(RecycleExpenseMainActivity.this, LocationActivity.class);
			intent.putExtra("vehicle", UserPreference.four);
			startActivityForResult(intent, 10);
			text = null;
			assistantAdapter.notifyDataSetChanged();
		}

	}

	public void Bus(Context context,String bus) {

		if(bus.matches(UserPreference.bus)){
			text_for_assistant = "Please select location?";
			text_user.add(new Expenses("Bus", UserPreference.SENT_TYPE));
			text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
			getconvert(text_for_assistant);

			Intent intent = new Intent(RecycleExpenseMainActivity.this, LocationActivity.class);
			intent.putExtra("vehicle", UserPreference.bus);
			startActivityForResult(intent, 10);
			text = null;
			assistantAdapter.notifyDataSetChanged();
		}

	}
	public void Taxi(Context context,String taxi) {

		if(taxi.matches(UserPreference.taxi)){
			text_for_assistant = "Please select location?";
			text_user.add(new Expenses("Taxi", UserPreference.SENT_TYPE));
			text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
			getconvert(text_for_assistant);

			Intent intent = new Intent(RecycleExpenseMainActivity.this, LocationActivity.class);
			intent.putExtra("vehicle", UserPreference.taxi);
			startActivityForResult(intent, 10);
			text = null;
			assistantAdapter.notifyDataSetChanged();
		}

	}
	public void Train(Context context,String train) {

		if(train.matches(UserPreference.train)){
			text_for_assistant = "Please select location?";
			text_user.add(new Expenses("Train", UserPreference.SENT_TYPE));
			text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
			getconvert(text_for_assistant);

			Intent intent = new Intent(RecycleExpenseMainActivity.this, LocationActivity.class);
			intent.putExtra("vehicle", UserPreference.train);
			startActivityForResult(intent, 10);
			text = null;
			assistantAdapter.notifyDataSetChanged();
		}

	}
	public void Flight(Context context,String train) {

		if(train.matches(UserPreference.flight)){
			text_for_assistant = "Please select location?";
			text_user.add(new Expenses("Flight", UserPreference.SENT_TYPE));
			text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
			getconvert(text_for_assistant);

			Intent intent = new Intent(RecycleExpenseMainActivity.this, LocationActivity.class);
			intent.putExtra("vehicle", UserPreference.flight);
			startActivityForResult(intent, 10);
			text = null;
			assistantAdapter.notifyDataSetChanged();
		}

	}
	public void card(Context context,String cash) {

		if(cash.matches(UserPreference.card)){
			getconvert(UserPreference.card);
			String  text_for_assistant = "How much amount you pay?";

			//text_user.add(new Expenses(UserPreference.cash ,UserPreference.SENT_TYPE ));
			text_user.add(new Expenses(text_for_assistant , UserPreference.RECEIVE_TYPE ));
			getconvert(text_for_assistant);
			expenseData.setPaymentMode("Card");
			assistantAdapter.notifyDataSetChanged();
		}

	}
	private void getconvert(String text_for_assistant) {


			//	Toast.makeText(getApplicationContext(), text_for_assistant,Toast.LENGTH_SHORT).show();
		//int speechStatus = t1.speak(text_for_assistant, TextToSpeech.QUEUE_FLUSH, null);
		int speechStatus =-1;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			 speechStatus=	t1.speak(text_for_assistant,TextToSpeech.QUEUE_ADD,null,null);

		} else {
			 speechStatus = t1.speak(text_for_assistant, TextToSpeech.QUEUE_ADD, null);

		}
		if (speechStatus == TextToSpeech.ERROR) {
			Log.e("TTS", "Error in converting Text to Speech!");
		}
		getspeech();



	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


		if(resultCode == 10 && requestCode == 10){
			text = null;
			source = data.getStringExtra("source");
			dest = data.getStringExtra("dest");
			km = data.getStringExtra("km");
			String[] separated1 = km.split("km");
			km= separated1[0];

			vehicle=data.getStringExtra("vehicle");
						//getconvert(text_for_assistant);

			if (vehicle.equals(UserPreference.two)){
				double two_wheeler=2.5;
				if (km.contains(",")){
					km = km.replace(",", "");

					    double calvehicle= Double.parseDouble(km);
					String vehicledistance= String.valueOf(calvehicle*two_wheeler);
					Amount="\u20B9"+String.valueOf(vehicledistance);

				}else {

					double calvehicle= Double.parseDouble(km);
					String vehicledistance= String.valueOf(calvehicle*two_wheeler);
					Amount="\u20B9"+String.valueOf(vehicledistance);

					// ed_travel.setBackground(getResources().getDrawable(R.drawable.edit_text));

				}
				//Calculation=two_wheeler;
				String loc = "2 wheeler" + "\n" + source + "\n" + dest + "\n" + km+"KM" + "\n" + Amount;
				text_for_assistant = loc;
				expenseData.setFromLocation(source);
				expenseData.setToLocation(dest);
				expenseData.setVehicleType("2 Wheeler");
				expenseData.setTravelMode("Vehicle");
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				text_for_assistant = "Do you want to add remark?";
				getconvert(text_for_assistant);
			}
			else if (vehicle.equals(UserPreference.four)){
				double four_wheeler=7;

				if (km.contains(",")){
					km = km.replace(",", "");

					double calvehicle= Double.parseDouble(km);
					String vehicledistance= String.valueOf(calvehicle*four_wheeler);
					Amount="\u20B9"+String.valueOf(vehicledistance);

				}else {

					double calvehicle= Double.parseDouble(km);
					String vehicledistance= String.valueOf(calvehicle*four_wheeler);
					Amount="\u20B9"+String.valueOf(vehicledistance);

					// ed_travel.setBackground(getResources().getDrawable(R.drawable.edit_text));

				}
				//Calculation=four_wheeler;
				String loc = "4 wheeler" + "\n" + source + "\n" + dest + "\n" + km+"KM" + "\n" + Amount;
				text_for_assistant = loc;
				expenseData.setFromLocation(source);
				expenseData.setToLocation(dest);
				expenseData.setVehicleType("4 Wheeler");
				expenseData.setTravelMode("Vehicle");

				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				text_for_assistant = "Do you want to add remark?";

				getconvert(text_for_assistant);

			}

			else if (vehicle.equals(UserPreference.taxi)){
				String loc = "Taxi" + "\n" +  source + "\n" + dest + "\n" + km+"KM";
				text_for_assistant = loc;
				expenseData.setFromLocation(source);
				expenseData.setToLocation(dest);
				expenseData.setVehicleType("Taxi");
				expenseData.setTravelMode("");
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert("Do you want to attach Bill or Ticket? Yes or No");


			}
			else if (vehicle.equals(UserPreference.bus)){
				String loc = "Bus" + "\n" + source + "\n" + dest + "\n" + km+"KM";
				text_for_assistant = loc;
				expenseData.setFromLocation(source);
				expenseData.setToLocation(dest);
				expenseData.setVehicleType("Bus");
				expenseData.setTravelMode("");
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				//text_for_assistant = "Mode of payment";

				//getconvert(text_for_assistant);

				getconvert("Do you want to attach Bill or Ticket? Yes or No");
			}
			else if (vehicle.equals(UserPreference.flight)){
				String loc = "Flight" + "\n" + source + "\n" + dest + "\n" + km+"KM";
				text_for_assistant = loc;
				expenseData.setFromLocation(source);
				expenseData.setToLocation(dest);
				expenseData.setVehicleType("Airplane");
				expenseData.setTravelMode("");
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				//text_for_assistant = "Mode of payment";

				//getconvert(text_for_assistant);

				getconvert("Do you want to attach Bill or Ticket? Yes or No");
			}
			else if (vehicle.equals(UserPreference.train)){
				String loc = "Train" + "\n" + source + "\n" + dest + "\n" + km+"KM";
				text_for_assistant = loc;
				expenseData.setFromLocation(source);
				expenseData.setToLocation(dest);
				expenseData.setVehicleType("Train");
				expenseData.setTravelMode("");
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				//text_for_assistant = "Mode of payment";
				//getconvert(text_for_assistant);

				getconvert("Do you want to attach Bill or Ticket? Yes or No");
			}


			/*String loc = source + "\n" + dest + "\n" + km+"KM" + "\n" +Amount;
			text_for_assistant = loc;
			expenseData.setSource(source);
			expenseData.setDestination(dest);
			text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
			getconvert("Do you want to attach Bill or Ticket? Yes or No");
*/


		}
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
						File file = new File(getRealPathFromUri(RecycleExpenseMainActivity.this,outPutfileUri));//create path from uri
						Attachment = file.getName();
						expenseData.setAttachment(file.getAbsoluteFile().toString());

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
						File file = new File(getRealPathFromUri(RecycleExpenseMainActivity.this,outPutfileUri));//create path from uri
						Attachment = file.getName();
						expenseData.setAttachment(file.getAbsoluteFile().toString());

					}
					//img_userpic.setImageURI(fileUri);
					//callChangeProfileImageApi(file.getAbsoluteFile().toString());


				} else {
					Toast.makeText(this, "You haven't picked Image",
							Toast.LENGTH_LONG).show();
				}
			}else if (requestCode == RESULT_DOCUMENT && null != data) {

                Uri selectedFileURI = data.getData();
				File file = new File(getRealPathFromUri(RecycleExpenseMainActivity.this,selectedFileURI));//create path from uri
                Log.d("", "File : " + file.getName());
                String uploadedFileName = file.toString();
				Toast.makeText(RecycleExpenseMainActivity.this,"Document attached successfully",Toast.LENGTH_SHORT).show();
				text_user.add(new Expenses(Attachment+"&"+"img_type", UserPreference.RECEIVE_TYPE));
				text_for_assistant = "Mode of payment";
				getconvert(text_for_assistant);


            }
			else {
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
			assistantAdapter.notifyDataSetChanged();

	}

	@Override
	public void onStart(){
		// call the superclass method first
		super.onStart();
	}

	@Override
	public void onResume() {
		// Always call the superclass method first
		super.onResume();
	}

	@Override
	public void onPause() {
		// Always call the superclass method first
		super.onPause();
	}

	@Override
	public void onDestroy(){
		// Always call the superclass method first
		super.onDestroy();
	}
	private void addMoreImages() {
		 final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.choose_option_attach_dialog);
		dialog.setTitle(getResources().getString(R.string.app_name));
		TextView camera = (TextView) dialog.findViewById(R.id.camera);
		TextView gallery = (TextView) dialog.findViewById(R.id.gallery);
		TextView textViewCancel = (TextView) dialog.findViewById(R.id.cancel);
        TextView document=dialog.findViewById(R.id.document);
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

	private void requestCameraPermission() {
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
				Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
				Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
					200);
		} else {
			startCameraIntent();
		}
	}

	private void requestGalleryPermission() {
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
				Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
					201);
		} else {
			startGalleryIntent();
		}
	}
    private void requestDocumentPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
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
			case REQUEST_RECORD_PERMISSION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					speech.startListening(recognizerIntent);
				} else {
					Toast.makeText(RecycleExpenseMainActivity.this, "Permission Denied!", Toast
							.LENGTH_SHORT).show();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	private void startGalleryIntent() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, RESULT_LOAD_IMG);
      /*  Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        // galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), RESULT_LOAD_IMG);*/
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
            Toast.makeText(RecycleExpenseMainActivity.this, "Please install a File Manager.",
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
			Attachment = file1.getAbsolutePath();
			File f = new File(Attachment);
			//Attachment=f.getName();
			expenseData.setAttachment(Attachment);

			Toast.makeText(RecycleExpenseMainActivity.this,"Document attached successfully",Toast.LENGTH_SHORT).show();
			text_user.add(new Expenses(Attachment+"&"+"img_type", UserPreference.RECEIVE_TYPE));
			text_for_assistant = "Mode of payment";
			getconvert(text_for_assistant);





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
	protected void onStop() {
		super.onStop();
		if (speech != null) {
			speech.destroy();
		}
	}


	@Override
	public void onBeginningOfSpeech() {

	}

	@Override
	public void onBufferReceived(byte[] buffer) {
	}

	@Override
	public void onEndOfSpeech() {
		Log.i("End", "onEndOfSpeech");

	}

	@Override
	public void onError(int errorCode) {
		String errorMessage = getErrorText(errorCode);
		//Toast.makeText(RecycleExpenseMainActivity.this,errorMessage,Toast.LENGTH_SHORT).show();

		//returnedText.setText(errorMessage);
		//toggleButton.setChecked(false);
	}

	@Override
	public void onEvent(int arg0, Bundle arg1) {


	}

	@Override
	public void onPartialResults(Bundle arg0) {
	}

	@Override
	public void onReadyForSpeech(Bundle arg0) {
	}

	@Override
	public void onResults(Bundle results) {

		text = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		assistant = text.get(0);

		try {
			int x = Integer.parseInt(text.get(0));
			amount = true;


		} catch (NumberFormatException e) {
			amount = false;


		}

		if (text==null){

		}else if(text != null) {
			if (text.get(0).matches(UserPreference.hello)) {
				text_for_assistant = "Welcome to expense management how can i help you?";
				text_user.add(new Expenses(UserPreference.hello, UserPreference.SENT_TYPE));

				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);





			} else if (text.get(0).matches(UserPreference.book)) {
				text_for_assistant = "Is it Personal or Official?";
				text_user.add(new Expenses(UserPreference.book, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));

				getconvert(text_for_assistant);





			} else if (text.get(0).matches(UserPreference.book1)) {
				text_for_assistant = "Is it Personal or Official?";
				text_user.add(new Expenses(UserPreference.book1, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));

				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.book1_ex)) {
				text_for_assistant = "Is it Personal or Official?";
				text_user.add(new Expenses(UserPreference.book1_ex, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);
				//



			} /*else if (text.get(0).matches(UserPreference.type_expense)) {
				text_for_assistant = "Travel,Maintainence,";
				text_user.add(new Expenses(UserPreference.type_expense, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);

			}*/ else if (text.get(0).matches(UserPreference.type_expenses)) {
				text_for_assistant = "Travel";
				text_user.add(new Expenses(UserPreference.type_expenses, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.two)) {
				text_for_assistant = "Please select location?";
				text_user.add(new Expenses("2 wheeler", UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);

				Intent intent = new Intent(RecycleExpenseMainActivity.this, LocationActivity.class);
				intent.putExtra("vehicle", UserPreference.two);
				startActivityForResult(intent, 10);
				text = null;

			} else if (text.get(0).matches(UserPreference.four)) {
				text_for_assistant = "Please select location?";
				text_user.add(new Expenses("4 wheeler", UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);

				Intent intent = new Intent(RecycleExpenseMainActivity.this, LocationActivity.class);
				intent.putExtra("vehicle", UserPreference.four);
				startActivityForResult(intent, 10);
				text = null;

			} else if (text.get(0).matches(UserPreference.bus)) {
				text_for_assistant = "Please select location?";
				text_user.add(new Expenses("Bus", UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);

				Intent intent = new Intent(RecycleExpenseMainActivity.this, LocationActivity.class);
				intent.putExtra("vehicle", UserPreference.bus);
				startActivityForResult(intent, 10);
				text = null;

				//startActivity(new Intent(RecycleExpenseMainActivity.this,LocationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


			} else if (text.get(0).matches(UserPreference.train)) {
				text_for_assistant = "Please select location?";
				text_user.add(new Expenses("Train", UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);
				Intent intent = new Intent(RecycleExpenseMainActivity.this, LocationActivity.class);
				intent.putExtra("vehicle", UserPreference.train);
				startActivityForResult(intent, 10);
				text = null;

				//getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.flight)) {
				text_for_assistant = "Please select location?";
				text_user.add(new Expenses("Flight", UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);

				Intent intent = new Intent(RecycleExpenseMainActivity.this, LocationActivity.class);
				intent.putExtra("vehicle", UserPreference.flight);
				startActivityForResult(intent, 10);
				text = null;

				// startActivity(new Intent(RecycleExpenseMainActivity.this,LocationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


			} else if (text.get(0).matches(UserPreference.taxi)) {
				text_for_assistant = "Please select location";
				text_user.add(new Expenses("Taxi", UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);

				Intent intent = new Intent(this, LocationActivity.class);
				intent.putExtra("vehicle", UserPreference.taxi);
				startActivityForResult(intent, 10);
				text = null;


			}

			/*else if (text.get(0).matches(UserPreference.type_travel)) {

				text_for_assistant = "Two wheeler,four wheeler,Bus,Taxi";
				text_user.add(new Expenses(UserPreference.type_travel, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			}*/
			else if (text.get(0).matches(UserPreference.personal)) {
				expenseData.setCat_name("Personal");
				text_for_assistant = "For Grocery Movie Doctor Hotel Hospital Food";
				text_user.add(new Expenses(UserPreference.personal, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.official)) {
				expenseData.setCat_name("Official");
				text_for_assistant = "What type of expenses ?" + "\n" + "Is it Travel Local Lodging Maintenance Food";
				text_user.add(new Expenses(UserPreference.official, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.trival)) {
				expenseData.setExpType("Travelling");
				text_for_assistant = "2 wheeler 4 wheeler Bus Taxi Train Flight";
				text_user.add(new Expenses("Travel", UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.grocery)) {
				expenseData.setExpType("Grocery");

				text_for_assistant = "Mode of payment";
				text_user.add(new Expenses(UserPreference.grocery, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.maintain)) {
				expenseData.setExpType("Maintenance");
				text_for_assistant = "Mode of payment";
				text_user.add(new Expenses(UserPreference.maintain, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.log)) {
				expenseData.setExpType("Lodging");

				text_for_assistant = "Mode of payment";
				text_user.add(new Expenses(UserPreference.log, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.doctor)) {
				expenseData.setExpType("Doctor");
				text_for_assistant = "Mode of payment";
				text_user.add(new Expenses(UserPreference.doctor, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.Hospital)) {
				expenseData.setExpType("Hospital");
				text_for_assistant = "Mode of payment";
				text_user.add(new Expenses(UserPreference.Hospital, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.movie)) {
				expenseData.setExpType("Movie");
				text_for_assistant = "Mode of payment";
				text_user.add(new Expenses(UserPreference.movie, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.cash)) {
				expenseData.setPaymentMode("Cash");
				text_for_assistant = "How much amount did you pay?";
				text_user.add(new Expenses(UserPreference.cash, UserPreference.SENT_TYPE));
				text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
				getconvert(text_for_assistant);


			} else if (text.get(0).matches(UserPreference.yes)) {

				addMoreImages();

			}
			else if (text.get(0).matches(UserPreference.no)) {
				text_for_assistant = "Mode of payment";
                text_user.add(new Expenses(UserPreference.no, UserPreference.SENT_TYPE));
                text_user.add(new Expenses(text_for_assistant, UserPreference.RECEIVE_TYPE));
                getconvert(text_for_assistant);

			}
			else if (amount) {
				Amount = text.get(0);
				getconvert(Amount);
				expenseData.setAmount(Amount);
				text_user.add(new Expenses(Amount, UserPreference.RECEIVE_TYPE));
				text_for_assistant = "Do you want to add remark?";
				getconvert(text_for_assistant);


			} else {
				String message = "Didn't understand, please try again.";
				txt_msg.setVisibility(View.VISIBLE);
				txt_msg.setText(message);
				getspeech();


			}

			assistantAdapter.notifyDataSetChanged();
		}

		//	returnedText.setText(text);
	}

	@Override
	public void onRmsChanged(float rmsdB) {
		//progressBar.setProgress((int) rmsdB);
	}

	public  String getErrorText(int errorCode) {
		String message;
		switch (errorCode) {
			case SpeechRecognizer.ERROR_AUDIO:
				message = "Audio recording error";
				getspeech();
				break;
			case SpeechRecognizer.ERROR_CLIENT:
				message = "Client side error";
				getspeech();
				break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
				message = "Insufficient permissions";
				getspeech();
				break;
			case SpeechRecognizer.ERROR_NETWORK:
				message = "Network error";
				break;
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
				message = "Network timeout";
				getspeech();
				break;
			case SpeechRecognizer.ERROR_NO_MATCH:
				message = "Didn't understand, please try again.";
				txt_msg.setVisibility(View.VISIBLE);
				txt_msg.setText(message);
				getspeech();

				break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
				message = "RecognitionService busy";
				getspeech();
				break;
			case SpeechRecognizer.ERROR_SERVER:
				message = "error from server";
				getspeech();
				break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
				message = "No speech input";
				getspeech();
				break;
			default:
				message = "Didn't understand, please try again.";
				txt_msg.setVisibility(View.VISIBLE);
				txt_msg.setText(message);
				getspeech();
				break;

		}

		return message;
	}

	private void foundDevice(){
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(400);
		animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
		ArrayList<Animator> animatorList=new ArrayList<Animator>();
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
		animatorList.add(scaleXAnimator);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
		animatorList.add(scaleYAnimator);
		animatorSet.playTogether(animatorList);
		foundDevice.setVisibility(View.VISIBLE);
		animatorSet.start();
		animatorSet.end();
	}

}
