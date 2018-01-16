package org.tafia.admin.modules.common.model;

import org.springframework.data.annotation.Id;

/**
 * Created by Dason on 2018/1/13.
 */
public class Entity {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
