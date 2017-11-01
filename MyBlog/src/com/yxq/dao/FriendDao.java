package com.yxq.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yxq.toolsbean.DB;
import com.yxq.valuebean.FriendBean;

public class FriendDao {
	private DB connection = null;

	public FriendDao() {
		connection = new DB();
	}

	// �޸�����
	public boolean operationFriend(String operation,FriendBean single) {
		String sql="";
		if(operation==null)
			operation="";		
		if(operation.equals("add"))
			sql="insert into tb_friend(friend_name,friend_sex,friend_OICQ,friend_blog) values('"+single.getName()+"','"+single.getSex()+"','"+single.getOicq()+"','"+single.getBlog()+"')";
		if(operation.equals("modify"))
			sql="update tb_friend set friend_name='"+single.getName()+"',friend_sex='"+single.getSex()+"',friend_OICQ='"+single.getOicq()+"',friend_blog='"+single.getBlog()+"' where id='"+single.getId()+"'";
		if(operation.equals("delete"))
			sql="delete from tb_friend where id='"+single.getId()+"'";
		
		boolean flag=connection.executeUpdate(sql);
		return flag;
	}

	/**
	 * @���� ��ѯ���к���
	 */
	public List queryFriend(String type) {
		String sql="";
		if(type==null||type.equals("")||!type.equals("all"))
			sql="select  * from tb_friend order by friend_name DESC LIMIT 0, 5";
		else
			sql="select * from tb_friend order by friend_name DESC";
		List list = new ArrayList();
		FriendBean friendBean = null;
		ResultSet rs = connection.executeQuery(sql);
		try {
			while (rs.next()) {
				friendBean = new FriendBean();
				friendBean.setId(rs.getInt(1));
				friendBean.setName(rs.getString(2));
				friendBean.setSex(rs.getString(3));
				friendBean.setOicq(rs.getString(4));
				friendBean.setBlog(rs.getString(5));
				list.add(friendBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @���� ��ѯĳ�����ѵ���ϸ��Ϣ
	 */
	public FriendBean queryFriendSingle(int id) {
		FriendBean friendBean = null;
		String sql = "select * from tb_friend where id='" + id+"'";
		ResultSet rs = connection.executeQuery(sql);
		try {
			while (rs.next()) {
				friendBean = new FriendBean();
				friendBean.setId(Integer.valueOf(rs.getString(1)));
				friendBean.setName(rs.getString(2));
				friendBean.setSex(rs.getString(3));
				friendBean.setOicq(rs.getString(4));
				friendBean.setBlog(rs.getString(5));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			connection.closed();
		}
		return friendBean;
	}
	
	public void closeConnection(){
		connection.closed();
	}
	

}
