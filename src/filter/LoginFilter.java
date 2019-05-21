// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   LoginFilter.java

package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@WebFilter("/*")
public class LoginFilter
    implements Filter
{

    public LoginFilter()
    {
    }

    public void destroy()
    {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        request.setCharacterEncoding("UTF-8");
        String servletName = ((HttpServletRequest)request).getServletPath();
        if(!servletName.equals("/LoginServlet") && !servletName.matches("^/css/.+") && !servletName.matches("^/img/.+") && !servletName.matches("^/javascript/.+"))
        {
            HttpSession session = ((HttpServletRequest)request).getSession();
            Object account = session.getAttribute("user");
            if(account == null)
            {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
                dispatcher.forward(request, response);
            }
        }
        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterconfig)
        throws ServletException
    {
    }
}
