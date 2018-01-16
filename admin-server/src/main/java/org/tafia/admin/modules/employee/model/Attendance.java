package org.tafia.admin.modules.employee.model;

import org.tafia.admin.modules.common.model.Entity;

/**
 * 出勤
 */
public class Attendance extends Entity{

    private String userId;

    private String action;

    private Long timestamp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
