package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bean.Bean4party;
import com.bean.SQLBean;



@WebServlet("*.do")
public class PollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String method = request.getServletPath();
		method = method.substring(1, method.length()-3);
		Method m = null;
		try
		{
			m = this.getClass().getDeclaredMethod(method,
					HttpServletRequest.class, HttpServletResponse.class);
			m.invoke(this, request, response);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	@SuppressWarnings("unused")
	private synchronized void result(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		Object o = session.getAttribute("sqlbean");//保证其他组件的访问。只加载一次数据。
		SQLBean bean = null;
		if (o == null) {
			bean = new SQLBean();
			session.setAttribute("sqlbean", bean);
		} else {
			bean = (SQLBean) o;
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8");
		PrintWriter out = null;
		try
		{
			out = response.getWriter();
			System.out.print(bean.getJsonResult());
			out.write(bean.getJsonResult());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally {
			if (out != null)
			{
				out.flush();
				out.close();
			}
		}
	}
	@SuppressWarnings("unused")
	private synchronized void register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		Object o = session.getAttribute("sqlbean");//保证其他组件的访问。只加载一次数据。
		SQLBean bean = null;
		if (o == null) {
			bean = new SQLBean();
			session.setAttribute("sqlbean", bean);
		} else {
			bean = (SQLBean) o;
		}
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String party = request.getParameter("party");
		bean.createInfo(id, name, party);
		request.getRequestDispatcher("LoginPage.html").forward(request, response);
	}
	@SuppressWarnings("unused")
	private synchronized void vote(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		Object o = session.getAttribute("sqlbean");//保证其他组件的访问。只加载一次数据。
		SQLBean bean = null;
		if (o == null) {
			bean = new SQLBean();
			session.setAttribute("sqlbean", bean);
		} else {
			bean = (SQLBean) o;
		}
		ArrayList<String> al = bean.getPartyList();
		RequestDispatcher rd = request.getRequestDispatcher("vote.html");
		request.setAttribute("list", al);
		rd.forward(request, response);

	}
	@SuppressWarnings("unused")
	private synchronized void confirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String value = request.getParameter("name")	;
		HttpSession session = request.getSession();
		Object o = session.getAttribute("sqlbean");//保证其他组件的访问。只加载一次数据。
		SQLBean bean = null;
		if (o == null) {
			bean = new SQLBean();
			session.setAttribute("sqlbean", bean);
		} else {
			bean = (SQLBean) o;
		}
		bean.confirm(value);
		RequestDispatcher rd = request.getRequestDispatcher("result.html");
		rd.forward(request, response);
	}
	@SuppressWarnings("unused")
	private synchronized void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		HttpSession session = request.getSession();
		Object o = session.getAttribute("sqlbean");//保证其他组件的访问。只加载一次数据。
		SQLBean bean = null;
		if (o == null) {
			bean = new SQLBean();
			session.setAttribute("sqlbean", bean);
		} else {
			bean = (SQLBean) o;
		}

		boolean canLogin = bean.login(name,pass);
		System.out.println(name+" "+pass);
		if (canLogin) {
			RequestDispatcher rd =request.getRequestDispatcher("RegisterPage.html");
			rd.forward(request, response);
		}else {
			RequestDispatcher rd =request.getRequestDispatcher("LoginAdmin.html");
			rd.forward(request, response);
		}
	}
}
