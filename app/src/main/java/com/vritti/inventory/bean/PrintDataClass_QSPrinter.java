package com.vritti.inventory.bean;

import android.util.Base64;

import com.qs.helper.printer.LabelCommand;
import com.qs.helper.printer.LabelUtils;
import com.vritti.inventory.physicalInventory.activity.BluetoothConnectivityActivity;

import java.util.Vector;

public class PrintDataClass_QSPrinter {

    boolean isPrint = true;
    String printMessage="";

    public PrintDataClass_QSPrinter() {
    }

    public PrintDataClass_QSPrinter(String message) {
        this.printMessage = message;
    }

    /*single print 2x2*/
    public void printLabel_single(String str, String tagNo_Barcode, String itemcode) {

        LabelCommand tsc = new LabelCommand();
        tsc.addSize(80, 30);
        tsc.addGap(1);
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD,
                LabelCommand.MIRROR.NORMAL);
        tsc.addReference(0, 0);
        //tsc.addTear(LabelUtils.ENABLE.ON);

        //x-5,y-5 for 3mm label
        //public void add1DBarcode(int x, int y, LabelCommand.BARCODETYPE type, int height, LabelCommand.READABEL readable, LabelCommand.ROTATION rotation, String content) {
        tsc.add1DBarcode( 7, 5, LabelCommand.BARCODETYPE.CODE128M,50,
                LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0,4,12,tagNo_Barcode);
        int lineSpace=70;
        /*tsc.addText(20, lineSpace, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2,
                LabelCommand.FONTMUL.MUL_1, itemcode+"  "+tagNo_Barcode);
        lineSpace+=30;*/
        String[] ss = str.split("\n");

        for (int i = 0; i < ss.length; i++) {
            tsc.addText(20,lineSpace, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1, ""+ss[i]);
            lineSpace+=30;
        }

//		 tsc.addText(20, 20, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1, ""+str);
//		 tsc.addText(20, 50, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1, "???????");
//		 tsc.addText(20, 80, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1, "????");
//		 tsc.addText(20, 110, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1, "????16.00");
//		 tsc.addText(20, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1, "???2017/05/19 15:00");
//		 tsc.addText(20, 170, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1,
//		 "???18818181818");

        tsc.addPrint(1, 1);
        tsc.addSound(1, 50);
        Vector<Byte> datas = tsc.getCommand();

        byte[] bytes = LabelUtils.ByteTo_byte(datas);
        String str1 = Base64.encodeToString(bytes, Base64.DEFAULT);
        byte[] decode_datas = Base64.decode(str1, Base64.DEFAULT);

	/*	str2="YYYY";
		sendcommand("SIZE 80 mm,40 mm\r\n");
		sendcommand("REFERENCE 0,0\r\n");
		sendcommand("SPEED 4.0\r\n");
		sendcommand("DENSITY 8\r\n");
		sendcommand("SET PEEL OFF\r\n");
		sendcommand("SET CUTTER OFF\r\n");
		sendcommand("SET TEAR ON\r\n");
		sendcommand("DIRECTION 0\r\n");
		sendcommand("SHIFT 0\r\n");
		sendcommand("OFFSET 0 mm\r\n");
		sendcommand("CLS\r\n");
//		sendcommand("BAR 200,59,127,8\r\n");
		sendcommand("BARCODE 20,20,\"128M\",50,1,0,4,12,\""+str2+"\"\r\n");
		//sendcommand("TEXT 20,190,\"ARIAL.TTF\",0,15,15,\""+str+"\"\n");
		sendcommand("PRINT 1");*/
        BluetoothConnectivityActivity.pl.write(decode_datas);
    }

    /*3x2mm New Printer code*/
    public void printLabel_customsize_HMPrinter(String tagNo_Barcode, String itemcode,String itemdesc, String tagdesc,String area_loc,
                                      String wt, String qty, String dt ,String countedBy, String verifyBy) {
        LabelCommand tsc = new LabelCommand();
        //tsc.addSize(80, 30);
        tsc.addGap(1);
        tsc.addCls();
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD,
                LabelCommand.MIRROR.NORMAL);
        tsc.addReference(0, 0);
        //tsc.addTear(LabelUtils.ENABLE.ON);

        //x-5,y-5 for 3mm label
        //public void add1DBarcode(int x, int y, LabelCommand.BARCODETYPE type, int height, LabelCommand.READABEL readable, LabelCommand.ROTATION rotation, String content) {
        tsc.add1DBarcode( 5, 10, LabelCommand.BARCODETYPE.CODE128M,50,
                LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0,4,12,tagNo_Barcode);

        tsc.addText(20, 110, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2,
                LabelCommand.FONTMUL.MUL_1, itemcode+"   "+tagNo_Barcode);

        int lineSpace=70;
        /*String[] ss = str.split("\n");
        for (int i = 0; i < ss.length; i++) {
            tsc.addText(20,lineSpace, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1, ""+ss[i]);
            lineSpace+=30;
        }*/

		 tsc.addText(20, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
		 LabelCommand.FONTMUL.MUL_1, itemdesc);

        tsc.addText(20, 170, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1, area_loc);

        if(wt.equalsIgnoreCase("0") || wt.equalsIgnoreCase("0.0") || wt.equalsIgnoreCase("0.00")){
            tsc.addText(20, 200, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1, "Qty : "+qty);
        }else {
            tsc.addText(20, 200, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1, "Wt: "+wt+" kg"+"/ Qty: "+qty);
        }

		 /*tsc.addText(20, 150, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
		 LabelCommand.FONTMUL.MUL_1, qty);*/

        tsc.addText(20, 230, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1, dt);
        /*tsc.addText(20, 220, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2,
                LabelCommand.FONTMUL.MUL_1, countedBy+ " / "+verifyBy);*/
        tsc.addText(20, 260, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1, countedBy);
        tsc.addText(20, 290, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1, verifyBy);

        if(tagdesc.equalsIgnoreCase("") || tagdesc.equalsIgnoreCase(null)){
            //do not print tag
        }else {
            tsc.addText(20, 320, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1, "Tag Desc: "+tagdesc);
        }

        tsc.addPrint(1, 1);
        tsc.addSound(1, 50);
        Vector<Byte> datas = tsc.getCommand();

        byte[] bytes = LabelUtils.ByteTo_byte(datas);
        String str1 = Base64.encodeToString(bytes, Base64.DEFAULT);
        byte[] decode_datas = Base64.decode(str1, Base64.DEFAULT);

        BluetoothConnectivityActivity.pl.write(decode_datas);

    }

    public void printLabel_range(String str, String tagNo_Barcode) {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(80, 25);
        tsc.addGap(1);
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD,
                LabelCommand.MIRROR.NORMAL);
        tsc.addReference(0, 0);
        tsc.addTear(LabelUtils.ENABLE.ON);

        //	public void add1DBarcode(int x, int y, LabelCommand.BARCODETYPE type, int height, LabelCommand.READABEL readable, LabelCommand.ROTATION rotation, String content) {
        tsc.add1DBarcode( 5, 5, LabelCommand.BARCODETYPE.CODE128M,40,
                LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0,4,12,tagNo_Barcode);
        int lineSpace=50;
        String[] ss = str.split("\n");

        for (int i = 0; i < ss.length; i++) {
            tsc.addText(20,lineSpace, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1, ""+ss[i]);
            lineSpace+=30;
        }

//		 tsc.addText(20, 20, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1, ""+str);
//		 tsc.addText(20, 50, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1, "???????");
//		 tsc.addText(20, 80, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1, "????");
//		 tsc.addText(20, 110, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1, "????16.00");
//		 tsc.addText(20, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1, "???2017/05/19 15:00");
//		 tsc.addText(20, 170, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
//		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
//		 LabelCommand.FONTMUL.MUL_1,
//		 "???18818181818");
        tsc.addPrint(5, 1);
        tsc.addSound(1, 50);
        Vector<Byte> datas = tsc.getCommand();

        byte[] bytes = LabelUtils.ByteTo_byte(datas);
        String str1 = Base64.encodeToString(bytes, Base64.DEFAULT);
        byte[] decode_datas = Base64.decode(str1, Base64.DEFAULT);

        BluetoothConnectivityActivity.pl.write(decode_datas);

    }

    /*3x2mm QsPrinter code*/
    public void printLabel_customsize(String tagNo_Barcode, String itemcode,String itemdesc, String tagdesc,String area_loc,
                                      String wt, String qty, String dt ,String countedBy, String verifyBy) {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(80, 30);
        tsc.addGap(1);
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD,
                LabelCommand.MIRROR.NORMAL);
        tsc.addReference(0, 0);
        //tsc.addTear(LabelUtils.ENABLE.ON);

        //x-5,y-5 for 3mm label
        //public void add1DBarcode(int x, int y, LabelCommand.BARCODETYPE type, int height, LabelCommand.READABEL readable, LabelCommand.ROTATION rotation, String content) {
        tsc.add1DBarcode( 5, 5, LabelCommand.BARCODETYPE.CODE128M,50,
                LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0,4,12,tagNo_Barcode);

        tsc.addText(20, 70, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2,
                LabelCommand.FONTMUL.MUL_1, itemcode+"   "+tagNo_Barcode);

        int lineSpace=70;
        /*String[] ss = str.split("\n");
        for (int i = 0; i < ss.length; i++) {
            tsc.addText(20,lineSpace, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1, ""+ss[i]);
            lineSpace+=30;
        }*/

        tsc.addText(20, 100, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1, itemdesc);

        tsc.addText(20, 130, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1, area_loc);

        if(wt.equalsIgnoreCase("0") || wt.equalsIgnoreCase("0.0") || wt.equalsIgnoreCase("0.00")){
            tsc.addText(20, 160, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2,
                    LabelCommand.FONTMUL.MUL_1, "Qty : "+qty);
        }else {
            tsc.addText(20, 160, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2,
                    LabelCommand.FONTMUL.MUL_1, "Wt: "+wt+" kg"+"/ Qty: "+qty);
        }

		 /*tsc.addText(20, 150, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
		 LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
		 LabelCommand.FONTMUL.MUL_1, qty);*/

        tsc.addText(20, 190, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1, dt);
        /*tsc.addText(20, 220, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2,
                LabelCommand.FONTMUL.MUL_1, countedBy+ " / "+verifyBy);*/
        tsc.addText(20, 220, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1, countedBy);
        tsc.addText(20, 250, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1, verifyBy);

        if(tagdesc.equalsIgnoreCase("") || tagdesc.equalsIgnoreCase(null)){
            //do not print tag
        }else {
            tsc.addText(20, 280, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                    LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1,
                    LabelCommand.FONTMUL.MUL_1, "Tag Desc: "+tagdesc);
        }

        tsc.addPrint(1, 1);
        tsc.addSound(1, 50);
        Vector<Byte> datas = tsc.getCommand();

        byte[] bytes = LabelUtils.ByteTo_byte(datas);
        String str1 = Base64.encodeToString(bytes, Base64.DEFAULT);
        byte[] decode_datas = Base64.decode(str1, Base64.DEFAULT);

        BluetoothConnectivityActivity.pl.write(decode_datas);

    }


}
