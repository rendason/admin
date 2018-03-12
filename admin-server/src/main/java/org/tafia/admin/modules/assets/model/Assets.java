package org.tafia.admin.modules.assets.model;

import org.tafia.admin.modules.common.model.Entity;

/**
 * 资产
 */
public class Assets extends Entity {

    private String name;

    private Type type;

    private String category;

    private String quantity;

    private String price;

    private String unit;

    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public enum Type {
        /**
         * 固定资产
         */
        FIXED,
        /**
         * 库存
         */
        INVENTORY,
        /**
         * 现金
         */
        CASH
    }
}
