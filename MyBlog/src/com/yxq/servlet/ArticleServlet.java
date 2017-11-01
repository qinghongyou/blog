package com.yxq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yxq.dao.ArticleDao;
import com.yxq.dao.ArticleTypeDao;
import com.yxq.dao.ReviewDao;
import com.yxq.toolsbean.MyTools;
import com.yxq.valuebean.ArticleBean;
import com.yxq.valuebean.ArticleTypeBean;
import com.yxq.valuebean.ReviewBean;

public class ArticleServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if(action==null)			
			action="";
		if (action.equals("select"))
			this.selectArticle(request, response);					//��ȡĳ�������������
		if (action.equals("adminSelectList"))
			this.adminSelectList(request,response);
		if (action.equals("adminSelectSingle"))
			this.adminSelectSingle(request,response);
		if (action.equals("add"))
			this.addArticle(request, response);						//��������
		if (action.equals("delete"))
			this.deleteArticle(request, response);					//ɾ������
		if (action.equals("modify"))
			this.modifyArticle(request, response);					//�޸�����
		if (action.equals("read"))
			this.readArticle(request, response);					//�Ķ�����
		if (action.equals("followAdd"))
			this.validateFollow(request, response);					//�������»ظ�
		if (action.equals("typeAdd"))
			this.addArticleType(request, response);					//�����������
		if (action.equals("typeSelect"))
			this.selectArticleType(request, response);				//�鿴�������
		if (action.equals("typeModify"))
			this.modifyArticleType(request, response);				//�޸��������
		if (action.equals("typeDelete"))
			this.deleteArticleType(request, response);				//ɾ���������
	}
	/**
	 * @���� ��֤������Ϣ
	 */
	public void validateFollow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean mark=true;
		String messages="";
		
		String content=request.getParameter("reContent");
		if(content==null||content.equals("")){
			mark=false;
			messages="<li>������ <b>�������ݣ�</b></li>";
		}
		if(mark){
			content=MyTools.toChinese(content);
			if(content.length()>1000){
				mark=false;
				messages="<li><b>��������</b> �����������1000���ַ���</li>";
			}
		}
		if(!mark){
			request.setAttribute("messages",messages);
			RequestDispatcher rd=request.getRequestDispatcher("/front/article/error.jsp");
			rd.forward(request,response);
		}
		else{
			followAdd(request,response);			
		}		
	}
	/**
	 * @���� ����������� 
	 */
	public void followAdd(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int id=Integer.parseInt(request.getParameter("articleId"));
		String author=MyTools.toChinese(request.getParameter("reAuthor"));
		String content=MyTools.toChinese(request.getParameter("reContent"));
		String today=MyTools.changeTime(new Date());
		if(author==null||author.equals(""))
			author="��������";		
		
		ReviewBean reviewBean = new ReviewBean();		
		reviewBean.setArticleId(id);		
		reviewBean.setReAuthor(author);
		reviewBean.setReContent(content);
		reviewBean.setReSdTime(today);
		
		ReviewDao reviewDao = new ReviewDao();
		String messages="";
		String forward="";
		boolean mark=reviewDao.operationReview("add",reviewBean);
		if (mark) {
			forward="/front/article/success.jsp";
			messages="<li>�������۳ɹ���</li>";
			
		} else {
			forward="/front/article/error.jsp";
			messages="<li>��������ʧ�ܣ�</li>";
		}
		request.setAttribute("messages",messages);
		RequestDispatcher rd=request.getRequestDispatcher(forward);
		rd.forward(request,response);	

	}
	/**
	 * @���� �Ķ�����
	 * @ʵ�� 1.���������Ķ�����<br>2.��ȡָ��������Ϣ<br>3.��ȡ�Ը����µ���������
	 */
	public void readArticle(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		ArticleBean articleBean = new ArticleBean();
		ArticleDao articleDao = new ArticleDao();	
		ReviewDao reviewDao=new ReviewDao();
		
		String strId=request.getParameter("id");
		int id=MyTools.strToint(strId);		
		articleBean.setId(id);
		
		articleDao.operationArticle("readTimes", articleBean);				//�ۼ��Ķ�����
		articleBean=articleDao.queryArticleSingle(id);						//��ѯָ�����µ���ϸ����
		session.setAttribute("readSingle", articleBean);					//����articleBean��request������
		
		List reviewlist=reviewDao.queryReview(id);
		session.setAttribute("reviewlist",reviewlist);
		
		RequestDispatcher rd = request.getRequestDispatcher("/front/article/ArticleSingle.jsp");
		rd.forward(request, response);
	}
	
	/**
	 * @���� �޸�����
	 */
	public void modifyArticle(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {		
		HttpSession session=request.getSession();
		RequestDispatcher rd=null;
		ArticleDao articleDao=new ArticleDao();		
		String type=request.getParameter("type");
		if(type==null)
			type="";
		if(!type.equals("doModify")){			
			int id=MyTools.strToint(request.getParameter("id"));
			ArticleBean articleBean=articleDao.queryArticleSingle(id);			
			request.setAttribute("modifySingle",articleBean);
			rd=request.getRequestDispatcher("/admin/article/ArticleModify.jsp");
			rd.forward(request,response);
		}
		else{
			String messages="";
			String href="";
			String forward="";
			boolean flag=validateArticle(request,response);
			if(flag){
				ArticleBean articleBean = new ArticleBean();
				articleBean.setId(MyTools.strToint(request.getParameter("id")));
				articleBean.setTypeId(Integer.valueOf(request.getParameter("typeId")));
				articleBean.setTitle(MyTools.toChinese(request.getParameter("title")));
				articleBean.setCreate(MyTools.toChinese(request.getParameter("create")));
				articleBean.setInfo(MyTools.toChinese(request.getParameter("info")));
				articleBean.setContent(MyTools.toChinese(request.getParameter("content")));
				
				boolean mark=articleDao.operationArticle("modify",articleBean);
				if (mark) {
					messages="<li>�޸ĳɹ���</li>";
					href="<a href='ArticleServlet?action=adminSelectList&typeId="+session.getAttribute("showTypeId")+"'>[�����޸���������]</a>";
					forward="/admin/success.jsp";					
				} else {
					messages="<li>�޸�ʧ�ܣ�</li>";
					href="<a href='javascript:window.history.go(-1)'>[����]</a>";
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
	 * @���� ��̨-ɾ������ 
	 */
	public void deleteArticle(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		ArticleDao articleDao = new ArticleDao();
		ArticleBean articleBean = new ArticleBean();

		String messages="";
		String href="";
		String forward="";
		articleBean.setId(MyTools.strToint(request.getParameter("id")));		
		boolean mark=articleDao.operationArticle("delete", articleBean);		
		if (mark) {
			String typeId=request.getParameter("typeId");
			messages+="<li>ɾ�����³ɹ���</li>";
			href="<a href='ArticleServlet?action=adminSelectList&typeId="+typeId+"'>[����ɾ����������]</a>";
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
	 * @���� ��֤����������Ϣ
	 */
	public boolean validateArticle(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		boolean mark=true;
		String messages="";
		
		String typeId=request.getParameter("typeId");
		String title=request.getParameter("title");
		String create=request.getParameter("create");
		String info=request.getParameter("info");
		String content=request.getParameter("content");
		
		if(typeId==null||typeId.equals("")){
			mark=false;
			messages+="<li>��ѡ�� <b>�������</b></li>";
		}
		if(title==null||title.equals("")){
			mark=false;
			messages+="<li>������ <b>���±��⣡</b></li>";
		}
		if(create==null||create.equals("")){
			mark=false;
			messages+="<li>��ѡ�� <b>������Դ��</b></li>";
		}
		if(info==null||info.equals("")){
			mark=false;
			messages+="<li>������ <b>����������</b></li>";
		}
		if(content==null||content.equals("")){
			mark=false;
			messages+="<li>������ <b>�������ݣ�</b></li>";
		}		
		if(mark){
			title=MyTools.toChinese(title);
			content=MyTools.toChinese(content);
			if(title.length()>20){
				mark=false;
				messages+="<li><b>���±���</b> �����������20���ַ���</li>";
			}
			if(content.length()>4000){
				mark=false;
				messages+="<li><b>��������</b> �����������4000���ַ���</li>";
			}
		}
		request.setAttribute("messages",messages);
		return mark;
	}
	/**
	 * @���� ��̨-��������
	 */
	public void addArticle(HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		String messages = "";
		String href="";
		String forward="";
		
		boolean flag=validateArticle(request,response);		
		if(flag){
			ArticleBean articleBean = new ArticleBean();
			articleBean.setTypeId(MyTools.strToint(request.getParameter("typeId")));
			articleBean.setTitle(MyTools.toChinese(request.getParameter("title")));
			articleBean.setContent(MyTools.changeHTML(MyTools.toChinese(request.getParameter("content"))));
			articleBean.setSdTime(MyTools.changeTime(new Date()));
			articleBean.setCreate(MyTools.toChinese(request.getParameter("create")));
			articleBean.setInfo(MyTools.toChinese(request.getParameter("info")));
			articleBean.setCount(0);
			
			ArticleDao articleDao = new ArticleDao();
			boolean mark=articleDao.operationArticle("add", articleBean);
			if(mark) {
				messages = "<li>�������³ɹ���</li>";
				href="<a href='admin/article/ArticleAdd.jsp'>[��������]</a>";
				forward="/admin/success.jsp";
			}
			else{
				messages="<li>��������ʧ�ܣ�</li>";
				href="<a href='javascript:window.history.go(-1)'>[����]</a>";
				forward="/admin/error.jsp";
			}			
			request.setAttribute("messages",messages);
			request.setAttribute("href",href);
		}
		else{
			href="<a href='javascript:window.history.go(-1)'>[����]</a>";
			forward="/admin/error.jsp";			
			request.setAttribute("href",href);
		}
		RequestDispatcher rd = request.getRequestDispatcher(forward);
		rd.forward(request, response);	
	}
	/**
	 * @���� ʵ�ֺ�̨���¹����е������������ 
	 */
	public void adminSelectList(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {		
		HttpSession session=request.getSession();
		ArticleDao articleDao = new ArticleDao();
		String strId=request.getParameter("typeId");
		if(strId==null||strId.equals(""))
			strId="-1";
		int typeId=Integer.parseInt(strId);
		session.setAttribute("showTypeId",typeId);
		List articleList=articleDao.queryArticle(typeId,"all");
		request.setAttribute("articleList",articleList);
		RequestDispatcher rd=request.getRequestDispatcher("/admin/article/ArticleList.jsp");
		rd.forward(request,response);
	}
	public void adminSelectSingle(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {		
		int id=MyTools.strToint(request.getParameter("id"));		
		ArticleDao articleDao = new ArticleDao();		
		
		ArticleBean articleBean=articleDao.queryArticleSingle(id);						//��ѯָ�����µ���ϸ����
		request.setAttribute("articleSingle",articleBean);		
		
		RequestDispatcher rd=request.getRequestDispatcher("/admin/article/ArticleSingle.jsp");
		rd.forward(request,response);
	}
	/**
	 * @���� �����ݱ��л�ȡĳ����µ��������� 
	 */
	public void selectArticle(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {		
		ArticleDao articleDao = new ArticleDao();
		String strId=request.getParameter("typeId");		
		if(strId==null||strId.equals(""))
			strId="-1";
		int typeId=Integer.parseInt(strId);
		List articleList=articleDao.queryArticle(typeId,"all");
		
		request.setAttribute("articleList",articleList);
		RequestDispatcher rd=request.getRequestDispatcher("/front/article/ArticleList.jsp");
		rd.forward(request,response);
	}
	public boolean validateType(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		boolean mark=true;
		String messages="";
		
		String typeName=request.getParameter("typeName");
		String typeInfo=request.getParameter("typeInfo");
	
		
		if(typeName==null||typeName.equals("")){
			mark=false;
			messages+="<li>������ <b>������ƣ�</b></li>";
		}
		if(typeInfo==null||typeInfo.equals("")){
			mark=false;
			messages+="<li>������ <b>�����ܣ�</b></li>";
		}		
		request.setAttribute("messages",messages);
		return mark;
	}
	
	/**
	 * @���� ��̨-����������� 
	 */
	public void addArticleType(HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		String messages = "";
		String href="";
		String forward="";	
		
		boolean flag=validateType(request,response);
		if(flag){
			ArticleTypeBean typeBean = new ArticleTypeBean();
			typeBean.setTypeName(MyTools.toChinese(request.getParameter("typeName")));
			typeBean.setTypeInfo(MyTools.toChinese(request.getParameter("typeInfo")));
			
			ArticleTypeDao articleTypeDao = new ArticleTypeDao();
			boolean mark=articleTypeDao.operationArticleType("add", typeBean);
			if(mark) {
				messages+="<li>����������ɹ���</li>";
				href="<a href='admin/article/ArticleTypeAdd.jsp'>[��������������]</a>";
				forward="/admin/success.jsp";
				
			}
			else {
				messages+="<li>����������ʧ�ܣ�</li>";
				href="<a href='javascript:window.history.go(-1)'>[����]</a>";
				forward="/admin/error.jsp";
			}
			request.setAttribute("messages",messages);
			request.setAttribute("href",href);
		}
		else{
			href="<a href='javascript:window.history.go(-1)'>[����]</a>";
			forward="/admin/error.jsp";
			request.setAttribute("href",href);			
		}
		RequestDispatcher rd=request.getRequestDispatcher(forward);
		rd.forward(request,response);		
	}
	public void selectArticleType(HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		ArticleTypeDao typeDao=new ArticleTypeDao();
		List typelist=typeDao.queryTypeList();
		request.setAttribute("typelist",typelist);
		RequestDispatcher rd=request.getRequestDispatcher("/admin/article/ArticleTypeList.jsp");
		rd.forward(request,response);		
	}
	/**
	 * @���� ��̨-�޸�������� 
	 */
	public void modifyArticleType(HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd=null;
		ArticleTypeBean typeBean=null;
		ArticleTypeDao typeDao=new ArticleTypeDao();		
		
		String type=request.getParameter("type");
		if(type==null)
			type="";
		if(!type.equals("doModify")){			
			int typeId=MyTools.strToint(request.getParameter("typeId"));
			typeBean=typeDao.queryTypeSingle(typeId);
			request.setAttribute("typeSingle",typeBean);
			rd=request.getRequestDispatcher("/admin/article/ArticleTypeModify.jsp");
			rd.forward(request,response);
		}
		else{
			String messages="";
			String href="";
			String forward="";
			boolean flag=validateType(request,response);
			if(flag){
				typeBean = new ArticleTypeBean();
				typeBean.setId(MyTools.strToint(request.getParameter("typeId")));
				typeBean.setTypeName(MyTools.toChinese(request.getParameter("typeName")));
				typeBean.setTypeInfo(MyTools.toChinese(request.getParameter("typeInfo")));			
				
				boolean mark=typeDao.operationArticleType("modify",typeBean);
				if (mark) {					
					messages="<li>�޸����ɹ���</li>";
					href="<a href='ArticleServlet?action=typeSelect'>[�����޸��������]</a>";
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
	 * @���� ��̨-ɾ���������
	 */
	public void deleteArticleType(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		ArticleTypeDao typeDao = new ArticleTypeDao();
		ArticleTypeBean typeBean = new ArticleTypeBean();

		String messages="";
		String href="";
		String forward="";
		
		typeBean.setId(MyTools.strToint(request.getParameter("typeId")));		
		boolean mark=typeDao.operationArticleType("delete",typeBean);		
		if (mark) {			
			messages+="<li>ɾ�����ɹ���</li>";
			href="<a href='ArticleServlet?action=typeSelect'>[����ɾ���������]</a>";
			forward="/admin/success.jsp";
		
		} else {
			messages+="<li>ɾ�����ʧ�ܣ�</li>";
			href="<a href='javascript:window.history.go(-1)'>[����]</a>";
			forward="/admin/error.jsp";
		}
		request.setAttribute("messages",messages);
		request.setAttribute("href",href);
		RequestDispatcher rd = request.getRequestDispatcher(forward);
		rd.forward(request, response);
	}
}
