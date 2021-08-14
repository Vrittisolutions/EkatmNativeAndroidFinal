package com.vritti.sales.beans;

import android.content.Context;

import com.vritti.sales.activity.AddDirectItemActivity;

public class InclusiveExclusiveTaxCalc {
    Context parent;
    Boolean isInclusiveTax=false;
    static float rate = 0.0f;
    float mrp=0.0f;
    String taxClass="";
    float taxVal=0.0f;
    static float tax = 0;

    public void InclusiveExclusiveTaxCalc(){

    }

    public static float calcRateInclTax(Boolean isInclusiveTax, float mrp, String taxClass, float taxVal){
        if(isInclusiveTax){
            //taxClass = "SGST 9% + CGST 9% OUTPUT";
            tax = TaxGSTCalculationClass.checkTaxType(taxClass);  //get tax summation here

            mrp = (mrp/((100 + tax)/100));
            rate = mrp;
        }else {
            mrp = mrp;
            rate = Math.round(mrp);
        }

        return rate;
    }

    public static float calcMarginVal(float mrp,float margin){
        if(margin > 0){
            mrp = (mrp / (1+(margin/100)));
        }else {
            mrp = mrp;
        }

        rate = Math.round(mrp);

        return rate;
    }

}
