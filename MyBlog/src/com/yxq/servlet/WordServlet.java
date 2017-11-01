package com.yxq.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yxq.dao.ArticleDao;
import com.yxq.dao.WordDao;
import com.yxq.toolsbean.MyTools;
import com.yxq.valuebean.ArticleBean;
import com.yxq.valuebean.WordBean;

public class WordServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if(action==null)
			action="";		
		if (action.equals("add"))
			this.validateWord(request, response);				//�������
		if (action.equals("select"))
			this.selectWord(request, response); 				//ǰ̨�鿴����
		if(action.equals("adminselect"))
			this.adminSelect(request,response);
		if(action.equals("delete"))
			this.delete(request,response);
			
	}
	public void validateWord(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		boolean mark=true;
		String messages="";
		
		String author=request.getParameter("wordAuthor");
		String title=request.getParameter("wordTitle");
		String content=request.getParameter("wordContent");
		if(author==null||author.equals(""))
			author="��������";
		if(title==null||title.equals("")){
			mark=false;
			messages+="<li>������ <b>���Ա��⣡</b></li>";
		}
		if(content==null||content.equals("")){
			mark=false;
			messages+="<li>������ <b>�������ݣ�</b></li>";
		}
		if(!mark){
			request.setAttribute("messages",messages);
			RequestDispatcher rd=request.getRequestDispatcher("/front/word/error.jsp");
			rd.forward(request,response);
		}
		else{
			addWord(request,response);
			
		}		
	}
	public void addWord(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		WordBean wordSingle=new WordBean();
		
		String author=MyTools.toChinese(request.getParameter("wordAuthor"));
		String title=MyTools.toChinese(request.getParameter("wordTitle"));
		String content=MyTools.toChinese(request.getParameter("wordContent"));		
		String sdTime=MyTools.changeTime(new Date());		
		if(author==null||author.equals(""))
			author="��������";
		
		wordSingle.setWordAuthor(author);
		wordSingle.setWordTitle(title);
		wordSingle.setWordContent(content);
		wordSingle.setWordTime(sdTime);
		
		WordDao wordDao=new WordDao();
		String messages="";
		String forward="";
		boolean mark=wordDao.operationWord("add",wordSingle);
		if(mark){
			forward="/front/word/success.jsp";
			messages="<li>���Գɹ���</li>";
			
		}
		else{
			forward="/front/word/error.jsp";
			messages="<li>����ʧ�ܣ�</li>";
		}
		request.setAttribute("messages",messages);
		RequestDispatcher rd=request.getRequestDispatcher(forward);
		rd.forward(request,response);		
	}
	public void selectWord(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		WordDao wordDao=new WordDao();
		List wordList=wordDao.queryWord("all");
		request.setAttribute("wordList",wordList);
		
		RequestDispatcher rd=request.getRequestDispatcher("/front/word/WordShow.jsp");
		rd.forward(request,response);			
	}
	public void adminSelect(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		WordDao wordDao=new WordDao();
		List wordList=wordDao.queryWord("all");
		request.setAttribute("adminwordList",wordList);
		
		RequestDispatcher rd=request.getRequestDispatcher("/admin/word/WordList.jsp");
		rd.forward(request,response);
	}
	public void delete(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		WordDao wordDao = new WordDao();
		WordBean wordBean = new WordBean();

		String messages="";
		String href="";
		String forward="";
		wordBean.setId(MyTools.strToint(request.getParameter("id")));		
		boolean mark=wordDao.operationWord("delete", wordBean);		
		if (mark) {			
			messages+="<li>ɾ�����Գɹ���</li>";
			href="<a href='WordServlet?action=adminselect'>[����ɾ����������]</a>";
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
}
