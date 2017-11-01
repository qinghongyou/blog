package com.yxq.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.yxq.dao.PhotoDao;
import com.yxq.toolsbean.MyTools;
import com.yxq.valuebean.PhotoBean;

public class PhotoServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if(action==null)
			action="";		
		if (action.equals("upload"))
			this.addPhoto(request, response);				//�ϴ�ͼƬ
		if (action.equals("delete"))
			this.deletePhoto(request, response); 			//ɾ��ͼƬ
		if(action.equals("list"))
			this.selectPhoto(request,response);				//��ѯ����ͼƬ
		if(action.equals("single"))
			this.singlePhoto(request,response);				//�鿴ͼƬ��ϸ����
		if(action.equals("adminList"))
			this.adminSelectPhoto(request,response);		//��ѯ����ͼƬ	
	}
	/**
	 * @���� ǰ̨-��ѯ����ͼƬ
	 */
	public void selectPhoto(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {		
		PhotoDao photoDao=new PhotoDao();
		List photoList=photoDao.queryPhoto("all");
		request.setAttribute("photoList", photoList);
		
		RequestDispatcher rd=request.getRequestDispatcher("/front/photo/PhotoList.jsp");
		rd.forward(request,response);
	}
	/**
	 * @���� ��̨-��ѯ����ͼƬ
	 */
	public void adminSelectPhoto(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {		
		PhotoDao photoDao=new PhotoDao();
		List photoList=photoDao.queryPhoto("all");
		request.setAttribute("photoList", photoList);
		
		RequestDispatcher rd=request.getRequestDispatcher("/admin/photo/PhotoList.jsp");
		rd.forward(request,response);
	}
	/**
	 * @���� �鿴ĳ��ͼƬ��ϸ����
	 */
	public void singlePhoto(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {		
		PhotoDao photoDao=new PhotoDao();		
		String strId=request.getParameter("id");
		int id=MyTools.strToint(strId);
		PhotoBean photoSingle=photoDao.queryPhoto(id);
		request.setAttribute("photoSingle",photoSingle);
		
		RequestDispatcher rd=request.getRequestDispatcher("/front/photo/PhotoSingle.jsp");
		rd.forward(request,response);
	}

	/**
	 * @���� ɾ��ͼƬ
	 */
	public void deletePhoto(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String messages="";
		String forward="";
		String href="";
		
		RequestDispatcher rd=null;
		PhotoDao photoDao= new PhotoDao();
		int id = MyTools.strToint(request.getParameter("id"));
		String fileAddr=photoDao.queryPhoto(id).getPhotoAddr();
		String photoDir=request.getSession().getServletContext().getRealPath("\\");
		String delFile=photoDir+fileAddr;
		
		java.io.File file = new java.io.File(delFile);
			
		PhotoBean photoBean = new PhotoBean();
		photoBean.setId(id);
		if (photoDao.operationPhoto("delete", photoBean)) {
			boolean result=file.delete();
			if(result){
				messages="<li>ɾ����Ƭ�ɹ���</li>";
				forward="/admin/success.jsp";
				href="<a href='PhotoServlet?action=adminList'>[����ɾ��������Ƭ]</a>";	
			}
			else{
				messages="<li>ɾ����Ƭʧ�ܣ�</li>";
				forward="/admin/error.jsp";
				href="<a href='javascript:window.history.go(-1)'>[����]</a>";
			}				
			
		} else {
			messages="<li>ɾ����Ƭʧ�ܣ�</li>";
			forward="/admin/error.jsp";
			href="<a href='javascript:window.history.go(-1)'>[����]</a>";			
		}
		request.setAttribute("messages",messages);
		request.setAttribute("href",href);
		rd=request.getRequestDispatcher(forward);
		rd.forward(request,response);
		
	}
	/**
	 * @���� �ϴ�ͼƬ
	 */
	public void addPhoto(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String filePath = "front\\\\photo\\\\pic\\\\";
		String messages="";
		String forward="";
		String href="";
		
		PhotoDao photoDao = new PhotoDao();
		PhotoBean photoBean = new PhotoBean();
		request.setCharacterEncoding("gbk");
		SmartUpload su = new SmartUpload();	
		long maxsize = 2 * 1024 * 1024; 									// ����ÿ���ϴ��ļ��Ĵ�С��Ϊ2MB
		
		try {
			su.initialize(this.getServletConfig(), request, response);
			su.setMaxFileSize(maxsize); 									// �����ϴ��ļ��Ĵ�С
			su.setAllowedFilesList("jpg,gif,bmp");							// ���������ϴ����ļ�����
			su.upload();													// �ϴ��ļ�
			String photoInfo=su.getRequest().getParameter("info");
			//String photoInfo=su.getRequest().getParameter("info");
			
			if(photoInfo==null||photoInfo.equals("")){						//��֤��Ƭ������Ϣ����û�����룬����ʾ������Ƭ������Ϣ
				messages="��������Ƭ������Ϣ��";
				forward="/admin/error.jsp";
				href="<a href='javascript:window.history.go(-1)'>[����]</a>";
			}
			else{
				File file = su.getFiles().getFile(0);						// ��ȡ�ϴ����ļ�����Ϊֻ�ϴ���һ���ļ������Կ�ֱ�ӻ�ȡ			
				if (!file.isMissing()) { 									// ���ѡ�����ļ�
					String photoAddr=filePath+photoDao.queryMaxId()+"."+file.getFileExt();		//filePathֵ(front\\photo\\pic\\)+ͼƬ��Ϣ�����ݱ��е�id�ֶ�ֵ+��.��+�ļ���׺��;����������硰front\photo\pic\12.bmp��·��
					String now=MyTools.changeTime(new Date());				//��ȡ��ǰʱ�䲢��ʽ��Ϊ�ַ���
					
					photoBean.setPhotoAddr(photoAddr);
					photoBean.setPhotoTime(now);
					photoBean.setPhotoInfo(photoInfo);
					
					boolean mark=photoDao.operationPhoto("upload",photoBean);
					if(mark){					
						try {
							file.saveAs(photoAddr,File.SAVEAS_VIRTUAL);
							messages="�ϴ��ļ��ɹ���";
							forward="/admin/success.jsp";
							href="<a href='admin/photo/PhotoUpload.jsp'>[�����ϴ�]</a>";
						} catch (SmartUploadException ee) {
							messages="�ϴ��ļ�ʧ�ܣ�";
							forward="/admin/error.jsp";
							href="<a href='javascript:window.history.go(-1)'>[����]</a>";
							ee.printStackTrace();
						}
					}
					else{
						messages="�����ļ���Ϣʧ�ܣ�";
						forward="/admin/error.jsp";
						href="<a href='javascript:window.history.go(-1)'>[����]</a>";
					}
				}
				else{
					messages="��ѡ��Ҫ�ϴ����ļ���";
					forward="/admin/error.jsp";
					href="<a href='javascript:window.history.go(-1)'>[����]</a>";
				}				
			}
			
		}catch (java.lang.SecurityException e){
			messages="<li>�ϴ��ļ�ʧ�ܣ��ϴ����ļ�����ֻ����Ϊ��jpg,gif,bmp</li>";
			forward="/admin/error.jsp";
			href="<a href='javascript:window.history.go(-1)'>[����]</a>";
			
		}catch (SmartUploadException e) {
			messages="�ϴ��ļ�ʧ�ܣ�";
			forward="/admin/error.jsp";
			href="<a href='javascript:window.history.go(-1)'>[����]</a>";
			e.printStackTrace();
		}
				
		request.setAttribute("messages",messages);
		request.setAttribute("href",href);
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(forward);
		requestDispatcher.forward(request, response);
	}
}
