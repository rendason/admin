package org.tafia.admin.modules.member.model;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public class MemberLevel {

    @Id
    private String id;

    private String name;

    private BigDecimal discount;

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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
