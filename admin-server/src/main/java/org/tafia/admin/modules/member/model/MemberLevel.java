package org.tafia.admin.modules.member.model;

import org.tafia.admin.modules.common.model.Entity;

import java.math.BigDecimal;

public class MemberLevel extends Entity{

    private String name;

    private BigDecimal discount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
