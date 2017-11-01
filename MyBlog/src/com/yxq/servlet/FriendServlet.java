package com.yxq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yxq.dao.ArticleDao;
import com.yxq.dao.ArticleTypeDao;
import com.yxq.dao.FriendDao;
import com.yxq.toolsbean.MyTools;
import com.yxq.valuebean.ArticleBean;
import com.yxq.valuebean.ArticleTypeBean;
import com.yxq.valuebean.FriendBean;

public class FriendServlet extends HttpServlet {	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if(action==null)
			action="";
		if (action.equals("add")) {						// ���������Ϣ
			this.addFriend(request, response);
		}
		if(action.equals("modify")){					// �޸�������Ϣ
			this.modifyFriend(request,response);
		}
		if (action.equals("delete")) {					// ɾ��������Ϣ
			this.deleteFriend(request, response);
		}
		if (action.equals("single")) {					// ��ѯĳ��������ϸ��Ϣ
			this.singleFriend(request, response);
		}
		if (action.equals("list")) {
			this.listFriend(request, response); 		// ��ѯ���к�����Ϣ
		}
		if (action.equals("adminSingle")) {					// ��ѯĳ��������ϸ��Ϣ
			this.adminSingleFriend(request, response);
		}
		if (action.equals("adminList")) {
			this.adminListFriend(request, response); 		// ��ѯ���к�����Ϣ
		}
	}

	/**
	 * @���� ��̨-�޸ĺ�����Ϣ
	 */
	public void modifyFriend(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd=null;
		FriendDao friendDao=new FriendDao();		
		String type=request.getParameter("type");
		if(type==null)
			type="";
		if(!type.equals("doModify")){			
			int id=MyTools.strToint(request.getParameter("id"));
			FriendBean friendBean=friendDao.queryFriendSingle(id);			
			request.setAttribute("modifySingle",friendBean);
			rd=request.getRequestDispatcher("/admin/friend/FriendModify.jsp");
			rd.forward(request,response);
		}
		else{
			String messages="";
			String href="";
			String forward="";
			boolean flag=validateFriend(request,response);
			if(flag){
				FriendBean friendBean = new FriendBean();
				friendBean.setId(MyTools.strToint(request.getParameter("id")));
				friendBean.setName(MyTools.toChinese(request.getParameter("name")));
				friendBean.setSex(MyTools.toChinese(request.getParameter("sex")));
				friendBean.setOicq(MyTools.toChinese(request.getParameter("oicq")));
				friendBean.setBlog(MyTools.toChinese(request.getParameter("oicq")));
				
				boolean mark=friendDao.operationFriend("modify",friendBean);
				if (mark) {
					messages="<li>�޸ĳɹ���</li>";
					href="<a href='FriendServlet?action=adminList'>[�����޸���������]</a>";
					forward="/admin/success.jsp";					
				} else {
					messages="<li>�޸�ʧ�ܣ�</li>";
					href="<a href='javascript:window.history.go(-1)>[����]</a>";
					forward="/admin/error.jsp";
				}
				request.setAttribute("messages",messages);
				request.setAttribute("href",href);
			}
			else{
				href="<a href='javascript:window.history.go(-1)>[����]</a>";
				forward="/admin/error.jsp";
				request.setAttribute("href",href);
			}
			rd=request.getRequestDispatcher(forward);
			rd.forward(request,response);			
		}
	}
	/**
	 * @���� ǰ̨-��ѯĳ��������Ϣ
	 */
	public void singleFriend(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String strId=request.getParameter("id");
		int id=MyTools.strToint(strId);
		FriendDao friendDao = new FriendDao();
		FriendBean friendSingle=friendDao.queryFriendSingle(id);		
		request.setAttribute("friendSingle",friendSingle);

		RequestDispatcher rd = request.getRequestDispatcher("/front/friend/FriendSingle.jsp");
		rd.forward(request, response);
	}
	/**
	 * @���� ��̨-��ѯĳ��������Ϣ
	 */
	public void adminSingleFriend(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String strId=request.getParameter("id");
		int id=MyTools.strToint(strId);
		FriendDao friendDao = new FriendDao();
		FriendBean friendSingle=friendDao.queryFriendSingle(id);		
		request.setAttribute("friendSingle",friendSingle);

		RequestDispatcher rd = request.getRequestDispatcher("/admin/friend/FriendSingle.jsp");
		rd.forward(request, response);
	}
	/**
	 * @���� ��̨-ɾ��������Ϣ
	 */
	public void deleteFriend(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		FriendDao friendDao = new FriendDao();
		FriendBean friendBean = new FriendBean();

		String messages="";
		String href="";
		String forward="";
		
		friendBean.setId(MyTools.strToint(request.getParameter("id")));		
		boolean mark=friendDao.operationFriend("delete",friendBean);		
		if (mark) {			
			messages+="<li>ɾ�����ѳɹ���</li>";
			href="<a href='FriendServlet?action=adminList'>[����ɾ����������]</a>";
			forward="/admin/success.jsp";
		
		} else {
			messages+="<li>ɾ������ʧ�ܣ�</li>";
			href="<a href='javascript:window.history.go(-1)'>[����]</a>";
			forward="/admin/error.jsp";
		}
		request.setAttribute("messages",messages);
		request.setAttribute("href",href);
		RequestDispatcher rd = request.getRequestDispatcher(forward);
		rd.forward(request, response);
	}
	/**
	 * @���� ��֤������
	 */
	public boolean validateFriend(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		boolean mark=true;
		String messages="";
		
		String name=MyTools.toChinese(request.getParameter("name"));
		String sex=MyTools.toChinese(request.getParameter("sex"));
		String oicq=MyTools.toChinese(request.getParameter("oicq"));
		String blog=MyTools.toChinese(request.getParameter("blog"));		
		
		if(name==null||name.equals("")){
			mark=false;
			messages+="<li>������ <b>����������</b></li>";
		}
		if(sex==null||sex.equals("")){
			mark=false;
			messages+="<li>��ѡ�� <b>�����Ա�</b></li>";
		}
		if(oicq==null||oicq.equals("")){
			mark=false;
			messages+="<li>������ <b>QQ ���룡</b></li>";
		}
		if(blog==null||blog.equals("")){
			mark=false;
			messages+="<li>������ <b>BLOG��ַ��</b></li>";
		}
		request.setAttribute("messages",messages);
		return mark;
	}
	/**
	 * @���� ��̨-��Ӻ�����Ϣ
	 */
	public void addFriend(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		String messages = "";
		String href="";
		String forward="";
		
		boolean flag=validateFriend(request,response);
		if(flag){
			FriendDao friendDao = new FriendDao();
			FriendBean friendBean = new FriendBean();
			friendBean.setName(MyTools.toChinese(request.getParameter("name")));
			friendBean.setSex(MyTools.toChinese(request.getParameter("sex")));
			friendBean.setOicq(MyTools.toChinese(request.getParameter("oicq")));
			friendBean.setBlog(MyTools.toChinese(request.getParameter("blog")));
			
			boolean mark=friendDao.operationFriend("add",friendBean);
			friendDao.closeConnection();
			if(mark){
				messages = "<li>��Ӻ��ѳɹ���</li>";
				href="<a href='admin/friend/FriendAdd.jsp'>[�������]</a>";
				forward="/admin/success.jsp";
			}
			else{
				messages = "<li>��Ӻ���ʧ�ܣ�</li>";
				href="<a href='javascript:window.history.go(-1)'>[����]</a>";
				forward="/admin/error.jsp";
			}
			request.setAttribute("messages",messages);
		}
		else{
			href="<a href='javascript:window.history.go(-1)'>[����]</a>";
			forward="/admin/error.jsp";			
		}
		request.setAttribute("href",href);			
		RequestDispatcher rd = request.getRequestDispatcher(forward);
		rd.forward(request, response);
	}
	/**
	 * @���� ǰ̨-��ѯ���к���
	 */
	public void listFriend(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		FriendDao friendDao=new FriendDao();
		List friendList=friendDao.queryFriend("all");
		request.setAttribute("friendList", friendList);
		
		RequestDispatcher rd = request.getRequestDispatcher("/front/friend/FriendList.jsp");
		rd.forward(request, response);		
	}
	/**
	 * @���� ��̨-��ѯ���к���
	 */
	public void adminListFriend(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		FriendDao friendDao=new FriendDao();
		List friendList=friendDao.queryFriend("all");
		request.setAttribute("friendList", friendList);
		
		RequestDispatcher rd = request.getRequestDispatcher("/admin/friend/FriendList.jsp");
		rd.forward(request, response);		
	}	
}
