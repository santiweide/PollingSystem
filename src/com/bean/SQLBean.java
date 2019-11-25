package com.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import model.DBdataManager;

public class SQLBean {
	DBdataManager dbm = new DBdataManager();

	public synchronized ArrayList<Bean4party> getB4pList()
	{
		return dbm.getB4pList();
	}
	public synchronized int createInfo(String id, String name, String party)
	{
		return dbm.createInfo(id, name, party);
	}
	public synchronized ArrayList<String> getPartyList()
	{
		return dbm.getPartyList();
	}
	public synchronized int confirm(String value)
	{
		return dbm.confirm(value);
	}
	public synchronized boolean login(String id, String pass) {
		// TODO Auto-generated method stub
		if(id == null || pass == null)
			return false;
		id = id.trim();
		pass = pass.trim();
		if(id.length() == 0 || pass.length() == 0)
			return false;
		return dbm.login(id, pass);
	}
	public synchronized String getJsonResult()
	{
		List<Map<String, Object>> list = dbm.getJsonResult();  
		return JSON.toJSONString(list);
	}
}
