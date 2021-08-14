package com.vritti.sales.beans;

public class MyCartBean {
    String CategoryId, CategoryName, SubCategoryName,SubCategoryId,MerchantId, MerchantName, CustomerName, CustMobno,offers;
    Float qnty, price,mrp,yousave, Amount;
    String Product_name, product_id, free_item_name_trade, free_item_qnty_trade;
    String  netrate, freeitemid,  validto, validfrom,validto_trade, validfrom_trade,DISCOUNT ,UNIT, Perdigit;
    String minqnty, Freeitemname,CouponId,discount,ItemImgPath,unit_V, MaxOrdQty, Range,OutOfStock,Distance,UOMCode = "",isMerchDelivery = "",UPIMerch="";
    int minvalue,freeitemqnty;
    boolean isChecked;
    String jsonData;

    String Brand= "", Content = "", ContentUOM = "",SellingUOM = "",CatImgPath = "",SubCatImgPath = "",BusiSegImgPath = "",PackOfQty="0";
    String FreeAboveAmt="0",FreeDelyMaxDist="0",MinDelyKg="0",MinDelyKm="0",ExprDelyWithinMin="0",ExpressDelyChg="0",Open_slots="",
            OpenTime1="",CloseTime1="",OpenTime2="",CloseTime2="";
    String totContentPerMerch="0", appliedDelCharges = "0", expDelMinByCust = "";

    public boolean getIsChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean ckecked) {
        isChecked = ckecked;
    }

    public String getUnit_V() {
        return unit_V;
    }

    public void setUnit_V(String unit_V) {
        this.unit_V = unit_V;
    }

    public String getPerdigit() { return Perdigit; }

    public void setPerdigit(String perdigit) {
        Perdigit = perdigit;
    }

    public String getDISCOUNT() {
        return DISCOUNT;
    }

    public void setDISCOUNT(String DISCOUNT) {
        this.DISCOUNT = DISCOUNT;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustMobno() {
        return CustMobno;
    }

    public void setCustMobno(String custMobno) {
        CustMobno = custMobno;
    }

    public String getItemImgPath() {
        return ItemImgPath;
    }

    public void setItemImgPath(String itemImgPath) {
        ItemImgPath = itemImgPath;
    }

    public String getValidto_trade() {
        return validto_trade;
    }

    public void setValidto_trade(String validto_trade) {
        this.validto_trade = validto_trade;
    }

    public String getValidfrom_trade() {
        return validfrom_trade;
    }

    public void setValidfrom_trade(String validfrom_trade) {
        this.validfrom_trade = validfrom_trade;
    }

    public String getCouponId() {
        return CouponId;
    }

    public void setCouponId(String couponId) {
        CouponId = couponId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFree_item_name_trade() {
        return free_item_name_trade;
    }

    public void setFree_item_name_trade(String free_item_name) {
        this.free_item_name_trade = free_item_name;
    }

    public String getFree_item_qnty_trade() {
        return free_item_qnty_trade;
    }

    public void setFree_item_qnty_trade(String free_item_qnty) {
        this.free_item_qnty_trade = free_item_qnty;
    }

    public String getFreeitemname() {
        return Freeitemname;
    }

    public void setFreeitemname(String freeitemname) {
        Freeitemname = freeitemname;
    }

    public String getMinqnty() {
        return minqnty;
    }

    public void setMinqnty(String minqnty) {
        this.minqnty = minqnty;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Float getAmount() {
        return Amount;
    }

    public void setAmount(Float amount) {
        Amount = amount;
    }

    public int getMinvalue() {
        return minvalue;
    }

    public void setMinvalue(int minvalue) {
        this.minvalue = minvalue;
    }

    public String getNetrate() {
        return netrate;
    }

    public void setNetrate(String netrate) {
        this.netrate = netrate;
    }

    public String getFreeitemid() {
        return freeitemid;
    }

    public void setFreeitemid(String freeitemid) {
        this.freeitemid = freeitemid;
    }

    public int getFreeitemqnty() {
        return freeitemqnty;
    }

    public void setFreeitemqnty(int freeitemqnty) {
        this.freeitemqnty = freeitemqnty;
    }

    public String getValidto() {
        return validto;
    }

    public void setValidto(String validto) {
        this.validto = validto;
    }

    public String getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(String validfrom) {
        this.validfrom = validfrom;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String merchantName) {
        MerchantName = merchantName;
    }

    public Float getQnty() {
        return qnty;
    }

    public void setQnty(Float qnty) {
        this.qnty = qnty;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public String getPrice() {
        return String.valueOf(price);
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public String getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Float getMrp() { return mrp; }

    public void setMrp(Float mrp) { this.mrp = mrp; }

    public Float getYousave() { return yousave; }

    public void setYousave(Float yousave) { this.yousave = yousave; }

    public String getMaxOrdQty() { return MaxOrdQty; }

    public void setMaxOrdQty(String maxOrdQty) { MaxOrdQty = maxOrdQty; }

    public String getRange() { return Range; }

    public void setRange(String range) { Range = range; }

    public String getOutOfStock() { return OutOfStock; }

    public void setOutOfStock(String outOfStock) { OutOfStock = outOfStock; }

    public String getDistance() {return Distance; }

    public void setDistance(String distance) { Distance = distance; }

    public String getUOMCode() { return UOMCode; }

    public void setUOMCode(String UOMCode) { this.UOMCode = UOMCode; }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getContentUOM() {
        return ContentUOM;
    }

    public void setContentUOM(String contentUOM) {
        ContentUOM = contentUOM;
    }

    public String getSellingUOM() {
        return SellingUOM;
    }

    public void setSellingUOM(String sellingUOM) { SellingUOM = sellingUOM; }

    public String getCatImgPath() {
        return CatImgPath;
    }

    public void setCatImgPath(String catImgPath) {
        CatImgPath = catImgPath;
    }

    public String getSubCatImgPath() {
        return SubCatImgPath;
    }

    public void setSubCatImgPath(String subCatImgPath) {
        SubCatImgPath = subCatImgPath;
    }

    public String getBusiSegImgPath() {
        return BusiSegImgPath;
    }

    public void setBusiSegImgPath(String busiSegImgPath) {
        BusiSegImgPath = busiSegImgPath;
    }

    public String getPackOfQty() {
        return PackOfQty;
    }

    public void setPackOfQty(String packOfQty) {
        PackOfQty = packOfQty;
    }

    public String getFreeAboveAmt() {
        return FreeAboveAmt;
    }

    public void setFreeAboveAmt(String freeAboveAmt) {
        FreeAboveAmt = freeAboveAmt;
    }

    public String getFreeDelyMaxDist() {
        return FreeDelyMaxDist;
    }

    public void setFreeDelyMaxDist(String freeDelyMaxDist) {
        FreeDelyMaxDist = freeDelyMaxDist;
    }

    public String getMinDelyKg() {
        return MinDelyKg;
    }

    public void setMinDelyKg(String minDelyKg) {
        MinDelyKg = minDelyKg;
    }

    public String getMinDelyKm() {
        return MinDelyKm;
    }

    public void setMinDelyKm(String minDelyKm) {
        MinDelyKm = minDelyKm;
    }

    public String getExprDelyWithinMin() {
        return ExprDelyWithinMin;
    }

    public void setExprDelyWithinMin(String exprDelyWithinMin) {
        ExprDelyWithinMin = exprDelyWithinMin;
    }

    public String getExpressDelyChg() {
        return ExpressDelyChg;
    }

    public void setExpressDelyChg(String expressDelyChg) {
        ExpressDelyChg = expressDelyChg;
    }

    public String getOpen_slots() {
        return Open_slots;
    }

    public void setOpen_slots(String open_slots) {
        Open_slots = open_slots;
    }

    public String getOpenTime1() {
        return OpenTime1;
    }

    public void setOpenTime1(String openTime1) {
        OpenTime1 = openTime1;
    }

    public String getCloseTime1() {
        return CloseTime1;
    }

    public void setCloseTime1(String closeTime1) {
        CloseTime1 = closeTime1;
    }

    public String getOpenTime2() {
        return OpenTime2;
    }

    public void setOpenTime2(String openTime2) {
        OpenTime2 = openTime2;
    }

    public String getCloseTime2() {
        return CloseTime2;
    }

    public void setCloseTime2(String closeTime2) {
        CloseTime2 = closeTime2;
    }

    public String getTotContentPerMerch() {
        return totContentPerMerch;
    }

    public void setTotContentPerMerch(String totContentPerMerch) {
        this.totContentPerMerch = totContentPerMerch;
    }

    public String getAppliedDelCharges() {
        return appliedDelCharges;
    }

    public void setAppliedDelCharges(String appliedDelCharges) {
        this.appliedDelCharges = appliedDelCharges;
    }

    public String getExpDelMinByCust() {
        return expDelMinByCust;
    }

    public void setExpDelMinByCust(String expDelMinByCust) {
        this.expDelMinByCust = expDelMinByCust;
    }

    public String getIsMerchDelivery() {
        return isMerchDelivery;
    }

    public void setIsMerchDelivery(String isMerchDelivery) {
        this.isMerchDelivery = isMerchDelivery;
    }

    public String getUPIMerch() {
        return UPIMerch;
    }

    public void setUPIMerch(String UPIMerch) {
        this.UPIMerch = UPIMerch;
    }
}