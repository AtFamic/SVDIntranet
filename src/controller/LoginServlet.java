// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   LoginServlet.java

package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.Account;
import dao.Login;
import model.DatabaseProp;
import model.LoginLogic;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet
{

    public LoginServlet()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        ServletContext context = getServletContext();
        String path = context.getRealPath("/WEB-INF/res/database.properties");
        DatabaseProp.setRealPath(path);
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {

        ServletContext context = getServletContext();
        String path = context.getRealPath("/WEB-INF/res/database.properties");
        HttpSession session = request.getSession();
        DatabaseProp.setRealPath(path);
        boolean loginFalt = false;
        request.setCharacterEncoding("UTF-8");
        String userID = request.getParameter("userID");
        String password = request.getParameter("password");
        Login login = new Login(userID, password);
        LoginLogic logic = new LoginLogic();
        Account account = logic.execute(login);
        RequestDispatcher dispatcher;
        if(account == null)
        {
            loginFalt = true;
            session.setAttribute("loginFalt", Boolean.valueOf(loginFalt));
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
            dispatcher.forward(request, response);
        }
        loginFalt = false;
        session.setAttribute("loginFalt", Boolean.valueOf(loginFalt));
        session.setAttribute("user", account);
        dispatcher = request.getRequestDispatcher("/DefaultViewServlet");
        dispatcher.forward(request, response);
    }
    private static final long serialVersionUID = 1L;
}
