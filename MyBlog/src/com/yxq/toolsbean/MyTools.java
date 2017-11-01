package com.yxq.toolsbean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTools {
	/**
	 * @���� ת���ַ���ֵΪint��ֵ
	 * @���� valueΪҪת�����ַ���
	 * @����ֵ int��ֵ
	 */
	public static String changeHTML(String value){
		value=value.replace("&","&amp;");
		value=value.replace(" ","&nbsp;");
		value=value.replace("<","&lt;");
		value=value.replace(">","&gt;");
		value=value.replace("\r\n","<br>");
		return value;
	}
	/**
	 * @���� ת���ַ���ֵΪint��ֵ
	 * @���� valueΪҪת�����ַ���
	 * @����ֵ int��ֵ
	 */
	public static int strToint(String value){
		int i=-1;
		if(value==null||value.equals(""))
			return i;
		try{
			i=Integer.parseInt(value);
		}catch(NumberFormatException e){
			i=-1;
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * @���� ���ͨ���ύ����������������
	 * @���� valueΪҪת�����ַ���
	 * @����ֵ String��ֵ
	 */
	
    public  static String  toChinese(String value) {
    	if (value == null)
    		return "";
    	try {
            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
            return value;
        } catch (Exception e) {
            return "";
        }
    }
    /**
     * @���� ��Date������ת����ָ����ʽ���ַ�����ʽ���硰yyyy��MM��dd�� HH:mm:ss��
     * @���� dateΪҪ��ת����Date������
     * @����ֵ String��ֵ
     */
	public static String changeTime(Date date) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");
		return format.format(date);		
	}
}
