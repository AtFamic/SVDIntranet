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
import model.TimecardWriter;

/**
 * Servlet implementation class TimecardOutputServlet
 */
@WebServlet("/TimecardOutputServlet")
public class TimecardOutputServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimecardOutputServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/timecardOutput.jsp");
		dispatcher.forward(request, response);


		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String sYear = (String)request.getParameter("startYear");
	    String sMonth = (String)request.getParameter("startMonth");
	    String sDate = (String)request.getParameter("startDate");
	    String eYear = (String)request.getParameter("endYear");
	    String eMonth = (String)request.getParameter("endMonth");
	    String eDate = (String)request.getParameter("endDate");

	    System.out.println(sYear);
	    System.out.println(sMonth);
	    System.out.println(sDate);
	    System.out.println(eYear);
	    System.out.println(eMonth);
	    System.out.println(eDate);

	    String start = sYear.concat(sMonth).concat(sDate);
	    String end = eYear.concat(eMonth).concat(eDate);
	    HttpSession session = request.getSession();
	    Account user = (Account)session.getAttribute("user");

	    //ファイルの保存先の指定
	    ServletContext context = getServletContext();
	    String file_path = context.getRealPath("/csv/timecard.csv");
	    request.setAttribute("file_path", file_path);

	    //エラーフラグの初期化
	    request.setAttribute("isError", false);

	    try {
	    	TimecardWriter.write(user.getUserID(), start, end, file_path);
	    }catch(IllegalArgumentException e) {
	    	e.printStackTrace();
	    	request.setAttribute("isError", true);
	    }
	    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/timecardOutput.jsp");
		dispatcher.forward(request, response);
	}

}
