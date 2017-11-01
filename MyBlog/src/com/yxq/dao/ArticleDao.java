package com.yxq.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yxq.toolsbean.DB;
import com.yxq.valuebean.ArticleBean;

public class ArticleDao {
	private DB connection = null;
	private ArticleBean articleBean = null;

	public ArticleDao() {
		connection = new DB();
	}

	/**
	 * @���� ʵ�ֶ����½�������ɾ���ĵĲ���
	 * @���� operΪһ��String���ͱ�����������ʾҪ���еĲ�����singleΪArticleBean����������洢ĳ�����µ���Ϣ
	 * @����ֵ boolean��ֵ
	 */
	public boolean operationArticle(String oper, ArticleBean single) {		
		/* ����SQL��� */
		String sql = null;
		if (oper.equals("add"))					//����������
			sql = "insert into tb_article(article_typeID,article_title,article_content,article_sdTime,article_create,article_info,article_count) values ('" + single.getTypeId() + "','"+ single.getTitle() + "','" + single.getContent() + "','"+ single.getSdTime()+ "','"+single.getCreate()+"','" + single.getInfo()+"','"+single.getCount() + "')";
		if (oper.equals("modify"))				//�޸�����
			sql = "update tb_article set article_typeID='" + single.getTypeId()+ "',article_title='" + single.getTitle() + "',article_content='"+ single.getContent() +"',article_create='"+single.getCreate()+ "',article_info='"+single.getInfo()+"' where id='" + single.getId()+"'";
		if (oper.equals("delete"))				//ɾ������
			sql = "delete from tb_article where id='" + single.getId()+"'";
		if (oper.equals("readTimes"))			//�ۼ��Ķ�����
			sql = "update tb_article set article_count=article_count+1 where id='"+ single.getId()+"'";
		
		/* ִ��SQL��� */	
		boolean flag =connection.executeUpdate(sql);
		return flag;
	}

	/** 
	 * @���� ��ѯָ����������
	 * @���� typeId��ʾ�������IDֵ
	 * @����ֵ List����
	 */
	public List queryArticle(int typeId,String type) {
		List articlelist = new ArrayList();
		String sql = "";
		if (typeId <=0)					//��������ѯ����ѯ��ǰ3����¼
			sql = "select * from tb_article order by article_sdTime DESC LIMIT 0, 2";
		else							//������ѯ
			if(type==null||type.equals("")||!type.equals("all"))
				sql = "select * from tb_article where article_typeID='" + typeId+ "' order by article_sdTime DESC LIMIT 0, 5";
			else
				sql = "select * from tb_article where article_typeID='" + typeId+ "' order by article_sdTime DESC";
		ResultSet rs = connection.executeQuery(sql);
		if(rs!=null){
			try {
				while (rs.next()) {
					/* ��ȡ������Ϣ */
					articleBean = new ArticleBean();
					articleBean.setId(rs.getInt(1));
					articleBean.setTypeId(rs.getInt(2));
					articleBean.setTitle(rs.getString(3));
					articleBean.setContent(rs.getString(4));
					articleBean.setSdTime(rs.getString(5));
					articleBean.setCreate(rs.getString(6));
					articleBean.setInfo(rs.getString(7));					
					articleBean.setCount(rs.getInt(8));
					
					/* ��ѯtb_review���ݱ�ͳ�Ƶ�ǰ���µ������� */
					sql="select count(id) from tb_review where review_articleId='"+articleBean.getId()+"'";
					ResultSet rsr=connection.executeQuery(sql);
					if(rsr!=null){
						rsr.next();
						articleBean.setReview(rsr.getInt(1));
						rsr.close();						
					}					
					articlelist.add(articleBean);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				connection.closed();
			}			
		}
		return articlelist;
	}

	/**
	 * @���� ��ѯָ�����µ���ϸ����
	 * @���� idΪ����IDֵ
	 * @����ֵ ArticleBean����󣬷�װ��������Ϣ
	 */
	public ArticleBean queryArticleSingle(int id) {
		String sql = "select * from tb_article where id='" + id + "'";
		ResultSet rs = connection.executeQuery(sql);
		try {
			while (rs.next()) {
				articleBean = new ArticleBean();
				articleBean.setId(rs.getInt(1));
				articleBean.setTypeId(rs.getInt(2));
				articleBean.setTitle(rs.getString(3));
				articleBean.setContent(rs.getString(4));
				articleBean.setSdTime(rs.getString(5));
				articleBean.setCreate(rs.getString(6));
				articleBean.setInfo(rs.getString(7));					
				articleBean.setCount(rs.getInt(8));
				
				/* ��ѯtb_review���ݱ�ͳ�Ƶ�ǰ���µ������� */
				sql="select count(id) from tb_review where review_articleId='"+articleBean.getId()+"'";
				ResultSet rsr=connection.executeQuery(sql);
				if(rsr!=null){
					rsr.next();
					articleBean.setReview(rsr.getInt(1));
					rsr.close();						
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articleBean;
	}
}
