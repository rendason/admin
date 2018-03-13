package org.tafia.admin.modules.finance.model;

import org.tafia.admin.modules.common.model.Entity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 月度薪资
 */
public class Salary extends Entity {

    /**
     * 年
     */
    private Integer year;

    /**
     * 月
     */
    private Integer month;

    /**
     * 计划薪资
     */
    private Map<String, BigDecimal> plannedSalary;

    /**
     * 扣减薪资
     */
    private Map<String, BigDecimal> deductedSalary;

    /**
     * 是否支付
     */
    private Boolean paid;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Map<String, BigDecimal> getPlannedSalary() {
        return plannedSalary;
    }

    public void setPlannedSalary(Map<String, BigDecimal> plannedSalary) {
        this.plannedSalary = plannedSalary;
    }

    public Map<String, BigDecimal> getDeductedSalary() {
        return deductedSalary;
    }

    public void setDeductedSalary(Map<String, BigDecimal> deductedSalary) {
        this.deductedSalary = deductedSalary;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }
}
