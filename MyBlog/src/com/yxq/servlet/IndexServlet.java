package com.yxq.servlet;

import java.io.IOException;
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
import com.yxq.dao.LogonDao;
import com.yxq.dao.PhotoDao;
import com.yxq.dao.WordDao;
import com.yxq.valuebean.MasterBean;

public class IndexServlet extends HttpServlet {
	private static MasterBean masterBean;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request,response);
	}	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session=request.getSession();
		ArticleDao articleDao = new ArticleDao();
		ArticleTypeDao artTypeDao = new ArticleTypeDao();
		PhotoDao photoDao = new PhotoDao();
		WordDao wordDao=new WordDao();
		FriendDao friendDao=new FriendDao();
		
		/********** ��ȡ����ҳ���������ʾ������ʾ������ *********/
		//��tb_article���ݱ��л�ȡǰ3ƪ���� 
		List articleList=articleDao.queryArticle(-1,null);
		request.setAttribute("articleList",articleList);
		//��tb_photo���ݱ��л�ȡǰ8����Ƭ
		List photoList=photoDao.queryPhoto("sub");
		request.setAttribute("photoList",photoList);
		
		/********** ��ȡ��ҳ���������ʾ������ *********/
		/* ��tb_word���ݱ��л�ȡǰ5������ */
		List wordList=wordDao.queryWord("sub");
		session.setAttribute("wordList",wordList);
		/* ��tb_article���ݱ��л�ȡǰ5���Ƽ����� */
		List artTJList=articleDao.queryArticle(4,"sub");
		session.setAttribute("artTJList",artTJList);
		/* ��tb_friend���ݱ��л�ȡǰ5λ������Ϣ */
		List friendList=friendDao.queryFriend("sub");
		session.setAttribute("friendList", friendList);		
		
		/********** ��ȡ������� *******************/
		/* ��tb_articleType���ݱ��л�ȡ������� */
		List artTypeList=artTypeDao.queryTypeList();
		session.setAttribute("artTypeList",artTypeList);
		
		/*********** ���沩����Ϣ *****************/
		session.setAttribute("master",masterBean);
		//response.sendRedirect("/front/FrontIndex.jsp");
		RequestDispatcher rd=request.getRequestDispatcher("/front/FrontIndex.jsp");
		rd.forward(request,response);
	}
	static{
		LogonDao logonDao=new LogonDao();
		masterBean=logonDao.getMaster();		
	}
}
