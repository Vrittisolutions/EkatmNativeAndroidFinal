package com.vritti.inventory.physicalInventory.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.ekatm.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PrintScreenWIFI extends AppCompatActivity {

    TextView txtplantno, txtseqno, txtitemcode, txtpartcode,txtwt, txtqty,txtarea, txtlocation, txtcntby, txtverfyby, txtdatetime;
    Bitmap bitmap;
    LinearLayout llscroll, layout;
    Button Button;
    String seqno, itemcode_desc, partcode, weight, quantity, area, location, countby, verifyby, datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_screen_wifi);

        init();

        Intent intent = getIntent();
        seqno = intent.getStringExtra("Seqnumber");
        itemcode_desc = intent.getStringExtra("itemdesc");
        partcode = intent.getStringExtra("partcode");
        weight = intent.getStringExtra("weight");
        quantity = intent.getStringExtra("quantity");
        area = intent.getStringExtra("area");
        location = intent.getStringExtra("location");
        countby = intent.getStringExtra("countedby");
        verifyby = intent.getStringExtra("verifiedby");
        datetime = intent.getStringExtra("datetime");

        //txtplantno.setText("PL1");
        txtseqno.setText(seqno);
        txtitemcode.setText("Part Name : "+itemcode_desc);
        txtpartcode.setText("Part No.      :  "+partcode);
        txtwt.setText("Weight       :  "+weight+" kg");
        txtqty.setText("Qty     :    "+quantity);
        txtarea.setText("Area           :  "+area);
        txtlocation.setText("Location :  "+location);
        txtcntby.setText(countby);
        txtverfyby.setText(verifyby);
        txtdatetime.setText(datetime);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                createPdf();
                return true;
            }
        });

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });

    }

    public void init(){
        layout = findViewById(R.id.layout);
        Button = findViewById(R.id.button);
        llscroll= findViewById(R.id.llscroll);
        txtplantno = findViewById(R.id.txtplantno);
        txtseqno = findViewById(R.id.txtseqno);
        txtitemcode = findViewById(R.id.txtitemcode);
        txtpartcode = findViewById(R.id.txtpartcode);
        txtwt = findViewById(R.id.txtwt);
        txtqty = findViewById(R.id.txtqty);
        txtarea = findViewById(R.id.txtarea);
        txtlocation = findViewById(R.id.txtlocation);
        txtcntby = findViewById(R.id.txtcntby);
        txtverfyby = findViewById(R.id.txtverfyby);
        txtdatetime = findViewById(R.id.txtdatetime);
    }

    public void setListeners(){

    }

    private String createPdf(){

        bitmap = loadBitmapFromView(llscroll, llscroll.getWidth(), llscroll.getHeight());

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document = new PdfDocument();
        }
        PdfDocument.PageInfo pageInfo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        }
        PdfDocument.Page page = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            page = document.startPage(pageInfo);
        }

        Canvas canvas = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            canvas = page.getCanvas();
        }

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        //bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);    //testing purpose

        paint.setTextSize(15);
        paint.setTextScaleX(50);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.finishPage(page);
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_YYYY");
        String addedDt = sdf.format(c.getTime());

        // write the document content
        final String fileName = addedDt+"_inventory.pdf";
        String targetPdf = Environment.getExternalStorageDirectory().getAbsolutePath() +"/PROINV/"+"_"+fileName;
        String _fileName = fileName;
        File filePath;
        filePath = new File(targetPdf);
        try {

            if (!filePath.exists()) {
                filePath.getParentFile().mkdirs();
                filePath.createNewFile();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //FileOutputStream fileOutputStream = new FileOutputStream(filePath,true);
                document.writeTo(new FileOutputStream(filePath));
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.close();
        }
        Toast.makeText(this, "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();

        return _fileName;

        // openGeneratedPDF();
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }
}