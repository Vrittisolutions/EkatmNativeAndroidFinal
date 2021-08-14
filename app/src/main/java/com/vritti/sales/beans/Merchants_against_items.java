package com.vritti.sales.beans;

/**
 * Created by sharvari on 4/26/16.
 */
public class Merchants_against_items {
    String Merchant_name, Merchant_id, offers, product_name, Freeitemid, Freeitemname,
            validfrom, validto,productid,customername,mobileno, perDigit,
            Merchant_name_two, Merchnat_address, Merchnat_email, Merchnat_mobile;
    float Amount,price, MRP, qnty;
    int minqnty, Freeitemqty;
    private boolean selected = false;

    public Merchants_against_items() {
    }


    /* String freeitemid, int freeitemqty,
         String discratemrp, String discratepercent,*/
    public Merchants_against_items(String MerchantId, String MerchantName, int qnty, int minqnty,
                                   String offers, float price, String product_name, String validfrom, String validto,
                                   String freeitemname, int freeitemqty,String Freeitemid) {
        Merchant_name = MerchantName;
        Merchant_id = MerchantId;
        this.offers = offers;
        this.product_name = product_name;
        //Amount = amount;
        this.qnty = qnty;
        this.minqnty = minqnty;
        this.price = price;
        this.validfrom = validfrom;
        this.validto = validto;
        this.Freeitemname = freeitemname;
        this.Freeitemqty = freeitemqty;
        this.Freeitemid=Freeitemid;
    }

    public Merchants_against_items(String offers, String product_name, String freeitemid,
                                   String freeitemname, String validfrom, String validto,
                                   String productid, String customername, String mobileno,
                                   int qnty, int minqnty, float price, int freeitemqty, float MRP) {
        this.offers = offers;
        this.product_name = product_name;
        Freeitemid = freeitemid;
        Freeitemname = freeitemname;
        this.validfrom = validfrom;
        this.validto = validto;
        this.productid = productid;
        this.customername = customername;
        this.mobileno = mobileno;
        this.qnty = qnty;
        this.minqnty = minqnty;
        this.price = price;
        Freeitemqty = freeitemqty;
        this.MRP = MRP;
    }

    public String getMerchnat_address() { return Merchnat_address;   }

    public void setMerchnat_address(String merchnat_address) { Merchnat_address = merchnat_address; }

    public String getMerchnat_email() { return Merchnat_email; }

    public void setMerchnat_email(String merchnat_email) { Merchnat_email = merchnat_email; }

    public String getMerchnat_mobile() { return Merchnat_mobile; }

    public void setMerchnat_mobile(String merchnat_mobile) { Merchnat_mobile = merchnat_mobile; }

    public String getMerchant_name_two() { return Merchant_name_two; }

    public void setMerchant_name_two(String merchant_name_two) {  Merchant_name_two = merchant_name_two; }

    public String getPerDigit() {
        return perDigit;
    }

    public void setPerDigit(String perDigit) {
        this.perDigit = perDigit;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public float getMRP() {
        return MRP;
    }

    public void setMRP(float MRP) {
        this.MRP = MRP;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getMinqnty() {
        return minqnty;
    }

    public void setMinqnty(int minqnty) {
        this.minqnty = minqnty;
    }

    public String getMerchant_id() {
        return Merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        Merchant_id = merchant_id;
    }

    public float getQnty() {
        return qnty;
    }

    public void setQnty(float qnty) {
        this.qnty = qnty;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public String getMerchant_name() {
        return Merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        Merchant_name = merchant_name;
    }


    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
