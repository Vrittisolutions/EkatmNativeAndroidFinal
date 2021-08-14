package com.vritti.sales.beans;

/**
 * Created by sharvari on 4/27/16.
 */
public class AddProductsToCart {
    String product_id, product_name, merchant_name, offer, merchant_id, Freeitemid, Freeitemname, validfrom, validto;
    float qnty;
    int minqnty;
    int Freeitemqty;
    float product_rate;

    public AddProductsToCart(String product_id, String product_name, String merchant_name, String offer,
                             float product_rate, float qnty, int minqnty, String merchant_id, String Freeitemid,
                             String Freeitemname, int Freeitemqty, String validfrom, String validto) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.merchant_name = merchant_name;
        this.offer = offer;
        this.product_rate = product_rate;
        this.qnty = qnty;
        this.minqnty = minqnty;
        this.merchant_id = merchant_id;
        this.Freeitemid = Freeitemid;
        this.Freeitemname = Freeitemname;
        this.Freeitemqty = Freeitemqty;
        this.validfrom = validfrom;
        this.validto = validto;
    }

    public String getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(String validfrom) {
        this.validfrom = validfrom;
    }

    public String getValidto() {
        return validto;
    }

    public void setValidto(String validto) {
        this.validto = validto;
    }

    public String getFreeitemid() {
        return Freeitemid;
    }

    public void setFreeitemid(String freeitemid) {
        Freeitemid = freeitemid;
    }

    public String getFreeitemname() {
        return Freeitemname;
    }

    public void setFreeitemname(String freeitemname) {
        Freeitemname = freeitemname;
    }

    public int getFreeitemqty() {
        return Freeitemqty;
    }

    public void setFreeitemqty(int freeitemqty) {
        Freeitemqty = freeitemqty;
    }

    public int getMinqnty() {
        return minqnty;
    }

    public void setMinqnty(int minqnty) {
        this.minqnty = minqnty;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public float getProduct_rate() {
        return product_rate;
    }

    public void setProduct_rate(float product_rate) {
        this.product_rate = product_rate;
    }

    public float getQnty() {
        return qnty;
    }

    public void setQnty(float qnty) {
        this.qnty = qnty;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }


    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }


}
