// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Account.java

package dao;

import java.util.Date;

public class Account
{

    public Account()
    {
    }

    public Account(String userID, String password, String name, String mail, long lastLogin)
    {
        setUserID(userID);
        setPassword(password);
        setName(name);
        setMail(mail);
        setLastLogin(lastLogin);
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin)
    {
        this.lastLogin = new Date(lastLogin);
    }

    public String getMail()
    {
        return mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    private String userID;
    private String password;
    private String name;
    private String mail;
    private Date lastLogin;
}
