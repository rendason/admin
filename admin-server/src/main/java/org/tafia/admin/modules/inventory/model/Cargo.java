package org.tafia.admin.modules.inventory.model;

import org.springframework.data.annotation.Id;

/**
 * 商品
 */
public class Cargo {

    @Id
    private String id;

    private String name;

    private int quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
