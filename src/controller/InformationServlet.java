// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   InformationServlet.java

package controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.Account;
import dao.AccountDAO;
import dao.Information;
import dao.InformationDAO;
import model.InformationLogic;
@WebServlet("/InformationServlet")
public class InformationServlet extends HttpServlet
{

    public InformationServlet()
    {
    }

    public static void checkInfo(HttpServletRequest request)
        throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        Account user = (Account)session.getAttribute("user");
        String userID = user.getUserID();
        List publicInfo = InformationDAO.getPublicInfo();
        List personalInfo = InformationDAO.getPersonalInfo(userID);
        session.setAttribute("publicInfo", publicInfo);
        session.setAttribute("personalInfo", personalInfo);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        Account user = (Account)session.getAttribute("user");
        String userID = user.getUserID();
        List allAccounts = AccountDAO.getAllAccounts();
        for(Iterator iterator = allAccounts.iterator(); iterator.hasNext();)
            if(((Account)iterator.next()).getUserID().equals(userID))
                iterator.remove();

        String select = InformationLogic.createAllAccountsSelect(allAccounts);
        session.setAttribute("allAccountsSelect", select);
        List publicInfo = InformationDAO.getPublicInfo();
        List personalInfo = InformationDAO.getPersonalInfo(userID);
        session.setAttribute("publicInfo", publicInfo);
        session.setAttribute("personalInfo", personalInfo);
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/comment.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account user = (Account)session.getAttribute("user");
        String userID = user.getUserID();
        String userName = user.getName();
        String title = request.getParameter("title");
        String sourceID = userID;
        String sourceName = userName;
        String destinationID = request.getParameter("destinationID");
        String content = request.getParameter("content");
        if(destinationID == null)
            destinationID = "";
        Information information = new Information(sourceID, sourceName, destinationID, title, content);
        InformationDAO.newInfo(information);
        String publicInformation = InformationLogic.createPublicInfoHTML(4);
        session.setAttribute("publicInformation", publicInformation);
        response.sendRedirect("/SVD_IntraNet/DefaultViewServlet");
    }

    private static final long serialVersionUID = 1L;
}
