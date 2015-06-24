package com.denny.android.idea_note.bmob.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by hasee on 2015/6/24.
 */
public class User extends BmobUser {
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
