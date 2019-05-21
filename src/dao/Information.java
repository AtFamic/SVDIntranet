// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Information.java

package dao;


public class Information
{

    public Information()
    {
    }

    public Information(String sourceUserID, String sourceUserName, String title, String comment)
    {
        this.sourceUserID = sourceUserID;
        this.sourceUserName = sourceUserName;
        destinationUserID = "";
        this.title = title;
        this.comment = comment;
    }

    public Information(String sourceUserID, String sourceUserName, String destinationUserID, String title, String comment)
    {
        this.sourceUserID = sourceUserID;
        this.sourceUserName = sourceUserName;
        this.destinationUserID = destinationUserID;
        this.title = title;
        this.comment = comment;
    }

    public String getInfomationID()
    {
        return InfomationID;
    }

    public void setInfomationID(String infomationID)
    {
        InfomationID = infomationID;
    }

    public String getSourceUserID()
    {
        return sourceUserID;
    }

    public void setSourceUserID(String sourceUserID)
    {
        this.sourceUserID = sourceUserID;
    }

    public String getSourceUserName()
    {
        return sourceUserName;
    }

    public void setSourceUserName(String sourceUserName)
    {
        this.sourceUserName = sourceUserName;
    }

    public String getDestinationUserID()
    {
        return destinationUserID;
    }

    public void setDestinationUserID(String destinationUserID)
    {
        this.destinationUserID = destinationUserID;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    private String InfomationID;
    private String sourceUserID;
    private String sourceUserName;
    private String destinationUserID;
    private String title;
    private String comment;
}
