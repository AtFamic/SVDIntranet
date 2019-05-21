// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   AccountLogic.java

package model;

import java.util.ArrayList;
import java.util.List;

import dao.Account;
import dao.AccountDAO;

public class AccountLogic
{

    public AccountLogic()
    {
    }

    public static String createAllAcountsSelectHTMLFor1(String selectedUserID)
    {
        selectedUserID = selectedUserID == null ? "" : selectedUserID;
        StringBuffer result = new StringBuffer("");
        List allAccounts = AccountDAO.getAllAccounts();
        allAccounts = ((List) (allAccounts == null ? ((List) (new ArrayList())) : allAccounts));
        int size = allAccounts.size();
        Account account = null;
        result.append("\r\n<form name=\"name1\">\r\n<select name=\"nameSelect1\" onChange=\"changeUserFor1()\">\r\n");
        for(int i = 0; i < size; i++)
        {
            account = (Account)allAccounts.get(i);
            String name = account.getName();
            String userID = account.getUserID();
            if(userID.equals(selectedUserID))
                result.append((new StringBuilder("<option value=\"/SVD_IntraNet/MonthViewServlet?isInitial=true&user1=")).append(userID).append("\" selected>").append(name).append("\u3055\u3093</option>\r\n").toString());
            else
                result.append((new StringBuilder("<option value=\"/SVD_IntraNet/MonthViewServlet?isInitial=true&user1=")).append(userID).append("\">").append(name).append("\u3055\u3093</option>\r\n").toString());
        }

        result.append("</select>\r\n</form>\r\n");
        return result.toString();
    }

    public static String createAllAcountsSelectHTMLFor2(String selectedUserID)
    {
        selectedUserID = selectedUserID == null ? "" : selectedUserID;
        StringBuffer result = new StringBuffer("");
        List allAccounts = AccountDAO.getAllAccounts();
        allAccounts = ((List) (allAccounts == null ? ((List) (new ArrayList())) : allAccounts));
        int size = allAccounts.size();
        Account account = null;
        result.append("\r\n<form name=\"name2\">\r\n<select name=\"nameSelect2\" onChange=\"changeUserFor2()\">\r\n");
        for(int i = 0; i < size; i++)
        {
            account = (Account)allAccounts.get(i);
            String name = account.getName();
            String userID = account.getUserID();
            if(userID.equals(selectedUserID))
                result.append((new StringBuilder("<option value=\"/SVD_IntraNet/MonthViewServlet?isInitial=true&user2=")).append(userID).append("\" selected>").append(name).append("\u3055\u3093</option>\r\n").toString());
            else
                result.append((new StringBuilder("<option value=\"/SVD_IntraNet/MonthViewServlet?isInitial=true&user2=")).append(userID).append("\">").append(name).append("\u3055\u3093</option>\r\n").toString());
        }

        result.append("</select>\r\n</form>\r\n");
        return result.toString();
    }
}
