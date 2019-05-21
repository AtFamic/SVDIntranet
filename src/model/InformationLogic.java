// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InformationLogic.java

package model;

import dao.*;
import java.util.ArrayList;
import java.util.List;

public class InformationLogic
{

    public InformationLogic()
    {
    }

    public static String createPublicInfoHTML(int num)
    {
        StringBuffer result = new StringBuffer("");
        List information = InformationDAO.getPublicInfo();
        information = ((List) (information == null ? ((List) (new ArrayList(0))) : information));
        int size = information.size();
        num = num <= size ? num : size;
        for(int i = 0; i < num; i++)
        {
            result.append("<!--\u5439\u304D\u51FA\u3057\u306F\u3058\u307E\u308A-->\r\n<div class=\"balloon5\">\r\n  <div class=\"faceicon\">\r\n  <img src=\"img/user.png\" alt\"user\"></div>\r\n  <div class=\"chatting\">\r\n    <div class=\"says\">\r\n");
            String message = ((Information)information.get(i)).getComment();
            result.append((new StringBuilder("<p>")).append(message).append("</p>\r\n").toString());
            result.append("</div>\r\n  </div>\r\n</div>\r\n<!--\u5439\u304D\u51FA\u3057\u7D42\u308F\u308A-->\r\n");
        }

        if(size > num)
            result.append((new StringBuilder("<div class=\"over\">\r\n<p>...\u305D\u306E\u4ED6")).append(size - num).append("\u4EF6</p>\r\n").append("</div>").toString());
        return result.toString();
    }

    public static String createPersonalInfoHTML(int num, String userID)
    {
        StringBuffer result = new StringBuffer("");
        List information = InformationDAO.getPersonalInfo(userID);
        information = ((List) (information == null ? ((List) (new ArrayList(0))) : information));
        int size = information.size();
        num = num <= size ? num : size;
        for(int i = 0; i < num; i++)
        {
            result.append("<!--\u5439\u304D\u51FA\u3057\u306F\u3058\u307E\u308A-->\r\n<div class=\"balloon5\">\r\n  <div class=\"faceicon\">\r\n  <img src=\"img/user.png\" alt\"user\"></div>\r\n  <div class=\"chatting\">\r\n    <div class=\"says\">\r\n");
            String message = ((Information)information.get(i)).getComment();
            result.append((new StringBuilder("<p>")).append(message).append("</p>\r\n").toString());
            result.append("</div>\r\n  </div>\r\n</div>\r\n<!--\u5439\u304D\u51FA\u3057\u7D42\u308F\u308A-->\r\n");
        }

        if(size > num)
            result.append((new StringBuilder("<div class=\"over\">\r\n<p>...\u305D\u306E\u4ED6")).append(size - num).append("\u4EF6</p>\r\n").append("</div>").toString());
        return result.toString();
    }

    public static String createAllAccountsSelect(List accounts)
    {
        StringBuffer result = new StringBuffer("");
        result.append("<select name=\"destinationID\" size=\"5\" multiple>\r\n");
        result.append("<option value = \"\" id=\"all\" selected>\u5168\u54E1</option>\r\n");
        accounts = ((List) (accounts == null ? ((List) (new ArrayList())) : accounts));
        int size = accounts.size();
        for(int i = 0; i < size; i++)
        {
            Account temp = (Account)accounts.get(i);
            String name = temp.getName();
            String userID = temp.getUserID();
            result.append((new StringBuilder("<option value = \"")).append(userID).append("\" id=\"user\">").append(name).append("</option>\r\n").toString());
        }

        result.append("</select>");
        return result.toString();
    }
}
