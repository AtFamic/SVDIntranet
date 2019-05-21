// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TimeCard.java

package dao;


public class TimeCard
{

    public TimeCard()
    {
    }

    public TimeCard(String userID, String arrivalTime, String goOutTime, String goBackTime, String leaveTime)
    {
        this.userID = userID;
        this.arrivalTime = arrivalTime;
        this.goOutTime = goOutTime;
        this.goBackTime = goBackTime;
        this.leaveTime = leaveTime;
    }

    public TimeCard(String timeCardID, String userID, String arrivalTime, String goOutTime, String goBackTime, String leaveTime)
    {
        timecardID = timeCardID;
        this.userID = userID;
        this.arrivalTime = arrivalTime;
        this.goOutTime = goOutTime;
        this.goBackTime = goBackTime;
        this.leaveTime = leaveTime;
    }

    public String getTimecardID()
    {
        return timecardID;
    }

    public void setTimecardID(String timecardID)
    {
        this.timecardID = timecardID;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getArrivalTime()
    {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    public String getGoOutTime()
    {
        return goOutTime;
    }

    public void setGoOutTime(String goOutTime)
    {
        this.goOutTime = goOutTime;
    }

    public String getGoBackTime()
    {
        return goBackTime;
    }

    public void setGoBackTime(String goBackTime)
    {
        this.goBackTime = goBackTime;
    }

    public String getLeaveTime()
    {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime)
    {
        this.leaveTime = leaveTime;
    }

    private String timecardID;
    private String userID;
    private String arrivalTime;
    private String goOutTime;
    private String goBackTime;
    private String leaveTime;
}
