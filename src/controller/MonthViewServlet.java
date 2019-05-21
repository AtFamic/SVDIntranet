// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   MonthViewServlet.java

package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import model.CalendarLogic;

// Referenced classes of package controller:
//            InformationServlet
@WebServlet("/MonthViewServlet")
public class MonthViewServlet extends HttpServlet
{
	/**
	 * 二枚目のカレンダーの初期値となるuserID
	 */
	private String secondCalendar = "admin2";

    public MonthViewServlet()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        InformationServlet.checkInfo(request);
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String user1 = request.getParameter("user1");
        Account account1 = null;
        List<String> users1 = new ArrayList<String>();
        if(user1 == null) {
        	if(session.getAttribute("user1") == null) {
        		account1 = (Account)session.getAttribute("user");
        	}else {
        		account1 = (Account)session.getAttribute("user1");
        	}
        }else {
        	account1 = AccountDAO.findAccountByUserID(user1);
        }
        session.setAttribute("user1", account1);
        users1.add(account1.getUserID());
        Date date1 = new Date();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        String chgDate = request.getParameter("date");
        if(chgDate != null)
        {
            date1 = new Date(Long.parseLong(chgDate));
            calendar1.setTime(date1);
        }
        String initial = request.getParameter("isInitial");
        initial = initial == null ? "" : initial;
        if(initial.equals("true"))
        {
            calendar1.set(calendar1.get(1), calendar1.get(2), 1);
            date1 = calendar1.getTime();
        }
        String monthView = CalendarLogic.createMonthCalendarHTMLByUsersFor1(date1, users1, (Account)session.getAttribute("user"));
        session.setAttribute("monthView1", monthView);


        //2枚目のカレンダー
        String user2 = request.getParameter("user2");
        Account account2 = null;
        List users2 = new ArrayList();
        if(user2 == null) {
        	if(session.getAttribute("user2") == null) {
        		account2 = AccountDAO.findAccountByUserID("admin");
        	}else {
        		account2 = (Account)session.getAttribute("user2");
        	}
        }else
            account2 = AccountDAO.findAccountByUserID(user2);

        session.setAttribute("user2", account2);
        users2.add(account2.getUserID());
        String monthView2 = CalendarLogic.createMonthCalendarHTMLByUsersFor2(date1, users2, (Account)session.getAttribute("user"));
        session.setAttribute("monthView2", monthView2);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/index.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }

    private static final long serialVersionUID = 1L;
}
