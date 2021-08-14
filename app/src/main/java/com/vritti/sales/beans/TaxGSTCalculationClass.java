package com.vritti.sales.beans;

import android.content.Context;

import com.vritti.sales.activity.AddDirectItemActivity;
import com.vritti.sales.activity.SalesChargeActivity;

/*created by Chetana Salunkhe */
public class TaxGSTCalculationClass {
    static float finalTotal_incltax = 0, taxamt =  0, pctgval = 0;

    public static float TaxGSTCalculationClass(String taxClass, float discountedamt, String callfrom){

        pctgval = checkTaxType(taxClass);

        taxamt = discountedamt * (pctgval/100);
        finalTotal_incltax = taxamt + discountedamt;

        if(callfrom.equalsIgnoreCase("AddDirectItem")){
            AddDirectItemActivity.setTaxAmtData(taxamt,finalTotal_incltax,pctgval);
        }else if(callfrom.equalsIgnoreCase("SalesCharge")){
            SalesChargeActivity.setTaxAmtData(taxamt,finalTotal_incltax,pctgval);
        }

        return pctgval;
    }

    public static float checkTaxType(String taxClass){
        String igstType = "" , sgstType = "" , cgstType = "", ugstType = "", sgstVal = "", cgstVal = "", igstVal = "", ugstVal = "",
                vatVal = "", vatType = "";
        float pctgvalTocalc = 0.0F;

        if(taxClass.contains("IGST")){

            String[] data;
            if(taxClass.contains(".")){
                data = taxClass.split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
            }else {
                data = taxClass.split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
            }

            // String[] data = taxClass.split("(?<=\\D)(?=\\d)");
            int sizeData = data.length;

            for (int i = 0; i < sizeData; i++) {
                String datanew = data[i];

                if (data[i].contains("%")) {
                    String[] pcgval = data[i].split("%");
                    System.out.println(pcgval[0]);

                    igstVal = pcgval[0];

                    float IGST = Float.parseFloat(igstVal);

                    float i1 = IGST / 2;

                    cgstType = "CGST";
                    cgstVal = String.valueOf(i1);

                    sgstType = "SGST";
                    sgstVal = String.valueOf(i1);
                }
            }

            pctgvalTocalc = Float.parseFloat(igstVal);

        }else if(taxClass.contains("CGST") && taxClass.contains("SGST") ){

            String[] taxtypes = taxClass.split("\\+");         // types[0] = SGST 9% , types[1] =CGST 9% OUTPUT

            System.out.println(taxtypes[0]);       //CGST 9 % OUTPUT
            // System.out.println(types[1]);       //SGST 9 % OUTPUT

            for(int i = 0; i < taxtypes.length; i++){
                String TYPE = "";
                String[] data;
                if(taxtypes[i].contains(".")){
                    data = taxtypes[i].split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
                }else {
                    data = taxtypes[i].split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
                }

                if(taxtypes[i].contains("CGST")){
                    cgstType = "CGST";
                    TYPE = cgstType;

                }else if(taxtypes[i].contains("SGST")){
                    sgstType = "SGST";
                    TYPE = sgstType;
                }

                for(int j = 0 ; j < data.length ; j++){
                    String[] pcgval = new String[0];

                    String taxTYPE = data[j];
                    System.out.println(taxTYPE);        //CGST, 9 % OUTPUT

                    if(data[j].contains("%")){
                        pcgval = data[j].split("%");
                        System.out.println(pcgval[0]);

                        if(TYPE.equalsIgnoreCase("CGST")){
                            cgstVal = String.valueOf(pcgval[0]);
                        }else if(TYPE.equalsIgnoreCase("SGST")){
                            sgstVal = String.valueOf(pcgval[0]);
                        }
                    }
                }
            }

            pctgvalTocalc = Float.parseFloat(cgstVal) + Float.parseFloat(sgstVal);

        }else if(taxClass.contains("SGST") || taxClass.contains("CGST") || taxClass.contains("UGST") || taxClass.contains("VAT")){

            String[] data;
            if(taxClass.contains(".")){
                data = taxClass.split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
            }else {
                data = taxClass.split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
            }

            //String[] data = taxClass.split("(?<=\\D)(?=\\d)");
            int sizeData = data.length;

            for (int i = 0; i < sizeData; i++) {
                String datanew = data[i];

                if (data[i].contains("%")) {
                    String[] pcgval = data[i].split("%");
                    System.out.println(pcgval[0]);

                    if(taxClass.contains("SGST")){
                        sgstVal = pcgval[0];
                        sgstType = "SGST";
                        pctgvalTocalc = Float.parseFloat(sgstVal);
                    }else if(taxClass.contains("CGST")){
                        cgstVal = pcgval[0];
                        cgstType = "CGST";
                        pctgvalTocalc = Float.parseFloat(cgstVal);
                    }else if(taxClass.contains("UGST")){
                        ugstVal = pcgval[0];
                        ugstType = "UGST";
                        pctgvalTocalc = Float.parseFloat(ugstVal);
                    }else if(taxClass.contains("VAT")){
                        vatVal = pcgval[0];
                        vatType = "VAT";
                        pctgvalTocalc = Float.parseFloat(vatVal);
                    }
                }
            }

        }else if(taxClass.contains("SGCT/CGST/IGST INCLUSIV")) {

        }

        return pctgvalTocalc;
    }

}
