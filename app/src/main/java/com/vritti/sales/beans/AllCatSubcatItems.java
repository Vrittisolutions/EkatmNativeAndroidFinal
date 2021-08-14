package com.vritti.sales.beans;

public class AllCatSubcatItems {
    String CategoryId, CategoryName, SubCategoryName, ItemName, ItemMasterId, SubCategoryId,ItemImgPath = "",
            Merchant_name, Merchant_id, offers, Freeitemid, Freeitemname, validfrom, validto, perDigit, sono, DoAck, Distance,
            UOMcode = "",CatImgPath = "",SubCatImgPath = "",ItemCount, segId, segCode, BusiSegImgPath = "", isMerchDelivery = "",UPIMerch="";

    float Price, TotalAmount, mrp,youSave;
    float minqnty, Freeitemqty;
    private boolean selected = false;
    String OutOfStock, MerchItemCode, BaseRate,MaxOrdQty,SGSTAmt,CGSTAmt,MinOrdQty,Range;
    String merchdist, merchseg, mercharea,merchmob,merchsegcode, merchlandmark="",merchsegDesc="";

    int subcatcount;
    int itemcount;
    float EdtQty;
    boolean isChecked;

    float MRPPrice,SPPrice;
    String Categories;
    String minQty,maxQty;
    int _rangebymerch = 0;

    String Brand = "", Content = "", ContentUOM = "",SellingUOM = "",PackOfQty = "0";
    String FreeAboveAmt="",FreeDelyMaxDist="",MinDelyKg="",MinDelyKm="",ExprDelyWithinMin="",ExpressDelyChg="",Open_slots="",
            OpenTime1="",CloseTime1="",OpenTime2="",CloseTime2="";
    String Not_Sold = "Y",Cat_flag="N", SubCatFlag="N";

    public AllCatSubcatItems(String CategoryId, String CategoryName) {
        this.CategoryId = CategoryId;
        this.CategoryName = CategoryName;
    }
    public AllCatSubcatItems(String SubCategoryId, String SubCategoryName,String cat) {
        this.SubCategoryId = SubCategoryId;
        this.SubCategoryName = SubCategoryName;
    }

    public AllCatSubcatItems(){

    }

    public boolean getIsChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean ckecked) { isChecked = ckecked; }

    public String getSono() {
        return sono;
    }

    public void setSono(String sono) {
        this.sono = sono;
    }

    public String getDoAck() {
        return DoAck;
    }

    public void setDoAck(String doAck) {
        DoAck = doAck;
    }

    public String getPerDigit(){
        return perDigit;
    }

    public void setPerDigit(String perDigit) {
        this.perDigit = perDigit;
    }

    public String getMerchant_id() {
        return Merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        Merchant_id = merchant_id;
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

    public float getMinqnty() { return minqnty; }

    public void setMinqnty(int minqnty) {
        this.minqnty = minqnty;
    }

    public float getFreeitemqty() {
        return Freeitemqty;
    }

    public void setFreeitemqty(float freeitemqty) {
        Freeitemqty = freeitemqty;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public float getPrice(){ return Price; }

    public void setPrice(float price){this.Price = price; }

    public float getEdtQty(){ return EdtQty; }

    public void setEdtQty(float edtQty){this.EdtQty = edtQty; }

    public float getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getItemImgPath() { return ItemImgPath; }

    public void setItemImgPath(String itemImgPath) {
        ItemImgPath = itemImgPath;
    }

    public int getSubcatcount() {
        return this.subcatcount;
    }

    public void setSubcatcount(int subcatcount) {
        this.subcatcount = subcatcount;
    }

    public int getItemcount() {
        return itemcount;
    }

    public void setItemcount(int itemcount) {
        this.itemcount = itemcount;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public String getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public void setCategoryId(String categoryId) {
        this.CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getSubCategoryName() {
        return this.SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public String getItemName() { return this.ItemName; }

    public void setItemName(String itemName) {
        this.ItemName = itemName;
    }

    public String getItemMasterId() {
        return this.ItemMasterId;
    }

    public void setItemMasterId(String itemMasterId) {
        this.ItemMasterId = itemMasterId;
    }

    public float getMrp() { return mrp; }

    public void setMrp(float mrp) { this.mrp = mrp; }

    public void setMinqnty(float minqnty) {
        this.minqnty = minqnty;
    }

    public String getOutOfStock() {
        return OutOfStock;
    }

    public void setOutOfStock(String outOfStock) {
        OutOfStock = outOfStock;
    }

    public String getMerchItemCode() {
        return MerchItemCode;
    }

    public void setMerchItemCode(String merchItemCode) {
        MerchItemCode = merchItemCode;
    }

    public String getBaseRate() {
        return BaseRate;
    }

    public void setBaseRate(String baseRate) {
        BaseRate = baseRate;
    }

    public String getMaxOrdQty() {
        return MaxOrdQty;
    }

    public void setMaxOrdQty(String maxOrdQty) {
        MaxOrdQty = maxOrdQty;
    }

    public String getSGSTAmt() {
        return SGSTAmt;
    }

    public void setSGSTAmt(String SGSTAmt) {
        this.SGSTAmt = SGSTAmt;
    }

    public String getCGSTAmt() {
        return CGSTAmt;
    }

    public void setCGSTAmt(String CGSTAmt) {
        this.CGSTAmt = CGSTAmt;
    }

    public String getMinOrdQty() {
        return MinOrdQty;
    }

    public void setMinOrdQty(String minOrdQty) {
        MinOrdQty = minOrdQty;
    }

    public float getYouSave() {
        return youSave;
    }

    public void setYouSave(float youSave) {
        this.youSave = youSave;
    }

    public String getRange() { return Range; }

    public void setRange(String range) { Range = range; }

    public float getMRPPrice() {
        return MRPPrice;
    }

    public void setMRPPrice(float MRPPrice) {
        this.MRPPrice = MRPPrice;
    }

    public float getSPPrice() {
        return SPPrice;
    }

    public void setSPPrice(float SPPrice) {
        this.SPPrice = SPPrice;
    }

    public String getCategories() {
        return Categories;
    }

    public void setCategories(String categories) {
        Categories = categories;
    }

    public String getMinQty() {
        return minQty;
    }

    public void setMinQty(String minQty) {
        this.minQty = minQty;
    }

    public String getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(String maxQty) {
        this.maxQty = maxQty;
    }

    public int get_rangebymerch() {
        return _rangebymerch;
    }

    public void set_rangebymerch(int _rangebymerch) {
        this._rangebymerch = _rangebymerch;
    }

    public String getDistance() {return Distance; }

    public void setDistance(String distance) { Distance = distance; }

    public String getUOMcode() { return UOMcode; }

    public void setUOMcode(String UOMcode) { this.UOMcode = UOMcode; }

    public String getMerchdist() { return merchdist; }

    public void setMerchdist(String merchdist) { this.merchdist = merchdist; }

    public String getMerchseg() { return merchseg; }

    public void setMerchseg(String merchseg) { this.merchseg = merchseg; }

    public String getMercharea() { return mercharea; }

    public void setMercharea(String mercharea) { this.mercharea = mercharea; }

    public String getMerchmob() { return merchmob; }

    public void setMerchmob(String merchmob) { this.merchmob = merchmob; }

    public boolean isChecked() { return isChecked; }

    public String getMerchsegcode() { return merchsegcode; }

    public void setMerchsegcode(String merchsegcode) { this.merchsegcode = merchsegcode; }

    public String getCatImgPath() { return CatImgPath; }

    public void setCatImgPath(String catImgPath) { CatImgPath = catImgPath; }

    public String getSubCatImgPath() { return SubCatImgPath; }

    public void setSubCatImgPath(String subCatImgPath) { SubCatImgPath = subCatImgPath; }

    public String getItemCount() { return ItemCount; }

    public void setItemCount(String itemCount) { ItemCount = itemCount; }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) { Brand = brand; }

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

    public void setSellingUOM(String sellingUOM) {
        SellingUOM = sellingUOM;
    }

    public String getSegId() {
        return segId;
    }

    public void setSegId(String segId) {
        this.segId = segId;
    }

    public String getSegCode() {
        return segCode;
    }

    public void setSegCode(String segCode) {
        this.segCode = segCode;
    }

    public String getBusiSegImgPath() { return BusiSegImgPath; }

    public void setBusiSegImgPath(String busiSegImgPath) { BusiSegImgPath = busiSegImgPath; }

    public String getPackOfQty() { return PackOfQty; }

    public void setPackOfQty(String packOfQty) { PackOfQty = packOfQty; }

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

    public String getMerchlandmark() {
        return merchlandmark;
    }

    public void setMerchlandmark(String merchlandmark) {
        this.merchlandmark = merchlandmark;
    }

    public String getMerchsegDesc() {
        return merchsegDesc;
    }

    public void setMerchsegDesc(String merchsegDesc) {
        this.merchsegDesc = merchsegDesc;
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

    public String getNot_Sold() {
        return Not_Sold;
    }

    public void setNot_Sold(String not_Sold) {
        Not_Sold = not_Sold;
    }

    public String getCat_flag() {
        return Cat_flag;
    }

    public void setCat_flag(String cat_flag) {
        Cat_flag = cat_flag;
    }

    public String getSubCatFlag() {
        return SubCatFlag;
    }

    public void setSubCatFlag(String subCatFlag) {
        SubCatFlag = subCatFlag;
    }
}