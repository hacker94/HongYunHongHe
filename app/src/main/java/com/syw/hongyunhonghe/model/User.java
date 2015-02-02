package com.syw.hongyunhonghe.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by Hacker_PK on 14/12/14.
 */
public class User extends BmobUser {

    private String realname;
    private String company;
    private String office;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}
