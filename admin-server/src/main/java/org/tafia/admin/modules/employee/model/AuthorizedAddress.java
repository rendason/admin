package org.tafia.admin.modules.employee.model;

import org.tafia.admin.modules.common.model.Entity;

public class AuthorizedAddress extends Entity {

    private String name;

    private String ip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
