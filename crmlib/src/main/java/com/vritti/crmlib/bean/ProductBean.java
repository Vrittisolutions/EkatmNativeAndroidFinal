package com.vritti.crmlib.bean;

/**
 * Created by sharvari on 10-Feb-17.
 */

public class ProductBean {

    String ItemMasterId,
            ItemDesc,
            ItemClassificationId,
            ItemCode,
            FamilyId,
            FamilyDesc,
            ItemCodeDesc,
            ItemPlantId,
            PlantId,qnty;

    public ProductBean() {
    }

   /* public ProductBean(String itemMasterId, String itemDesc) {
        ItemMasterId = itemMasterId;
        ItemDesc = itemDesc;
    }*/

    public ProductBean(String familyId, String familyDesc) {
        FamilyId = familyId;
        FamilyDesc = familyDesc;
    }

    public String getQnty() {
        return qnty;
    }

    public void setQnty(String qnty) {
        this.qnty = qnty;
    }

    public String getItemMasterId() {
        return ItemMasterId;
    }

    public void setItemMasterId(String itemMasterId) {
        ItemMasterId = itemMasterId;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getItemClassificationId() {
        return ItemClassificationId;
    }

    public void setItemClassificationId(String itemClassificationId) {
        ItemClassificationId = itemClassificationId;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getFamilyId() {
        return FamilyId;
    }

    public void setFamilyId(String familyId) {
        FamilyId = familyId;
    }

    public String getFamilyDesc() {
        return FamilyDesc;
    }

    public void setFamilyDesc(String familyDesc) {
        FamilyDesc = familyDesc;
    }

    public String getItemCodeDesc() {
        return ItemCodeDesc;
    }

    public void setItemCodeDesc(String itemCodeDesc) {
        ItemCodeDesc = itemCodeDesc;
    }

    public String getItemPlantId() {
        return ItemPlantId;
    }

    public void setItemPlantId(String itemPlantId) {
        ItemPlantId = itemPlantId;
    }

    public String getPlantId() {
        return PlantId;
    }

    public void setPlantId(String plantId) {
        PlantId = plantId;
    }
}