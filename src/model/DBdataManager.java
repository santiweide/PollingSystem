package model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.bean.Bean4party;


public class DBdataManager implements Serializable {
	private static final long serialVersionUID = 6379710035719112850L;
	private DataSource ds = null;
	
	public DBdataManager()
	{
		try {
			Context context = new InitialContext();
			if(context != null)
				ds = (DataSource)context.lookup("java:comp/env/jdbc/dut");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized Connection getConnection()throws Exception
	{
		return ds.getConnection();
	}
	public synchronized void closeConnection(Connection con)
	{
		try {
			if(con != null)
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void closePreparedStatement(PreparedStatement prepstmt)
	{
		try {
			if(prepstmt != null)
				prepstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void closeResultSet(ResultSet rs)
	{
		try {
			if(rs != null)
				rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized ArrayList<String> getPartyList()
	{
		ArrayList<String> ret = new ArrayList<>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;//都是本地变量比较线程安全
		try
		{
			con = getConnection();
			String sql = "select * from detail"; 
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String Party = rs.getString("party");
				ret.add(Party);
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			con.close();
			con = null;//连接被放回连接池，确保不会关两次连接
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			closeConnection(con);
			closePreparedStatement(stmt);
			closeResultSet(rs);
		}
		return ret;
	}
	
	public synchronized ArrayList<Bean4party> getB4pList()
	{
		ArrayList<Bean4party> ret = new ArrayList<>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			con = getConnection();
			String sql = "select * from party"; 
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String Party = rs.getString("party");
				String count = rs.getString("count");
				Bean4party b = new Bean4party();
				b.setParty(Party);
				b.setCount(count);
				ret.add(b);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			closeConnection(con);
			closePreparedStatement(stmt);
			closeResultSet(rs);
		}
		return ret;
	}
	public synchronized List<Map<String, Object>> getJsonResult()
	{
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>> ();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			con = getConnection();
			String sql = "select * from party";
			System.out.println(sql);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();  
				map.put("value", rs.getString("value"));
				map.put("text", rs.getString("party"));
	            map.put("votes", rs.getString("count"));
	            ret.add(map);  	
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection(con);
			closePreparedStatement(stmt);
			closeResultSet(rs);
		}
		return ret;
	}
	public synchronized int createInfo(String id, String name, String party)
	{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ret = -1;
		try
		{
			con = getConnection();
			String testSql = "select count(*) from detail where id = " + id;
			ps = con.prepareStatement(testSql);
			rs = ps.executeQuery();
			if(rs.next())
			{
				if(rs.getInt(1) == 0)
				{
					PreparedStatement pss = null;
					String sql = "insert into detail (id,name,party) values ('"+id+"', '"+name+"', '"+party+"')";
					System.out.println(sql);
					pss = con.prepareStatement(sql);
					ret = pss.executeUpdate();
					PreparedStatement pss2 = null;
					String sql2 = "insert into party (party, count) values ('"+party+"', '"+0+"')";
					pss2 = con.prepareStatement(sql2);
					pss2.executeUpdate();
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			closePreparedStatement(ps);
			closeConnection(con);
		}
		return ret;
	}
	public synchronized int confirm(String value)
	{
		Connection con = null;
		PreparedStatement ps = null;
		int ret = -1;
		try
		{
			con = getConnection();
			String sql = "update party set count=count+1 where value ='"+value+"'";
			System.out.println(sql);
			ps = con.prepareStatement(sql);
			ret = ps.executeUpdate();
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			closePreparedStatement(ps);
			closeConnection(con);
		}
		return ret;
	}
	public synchronized boolean login(String id,String pass)
	{
		boolean ret = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			con = getConnection();
			String sql = "select * from face_info where id = "+id;
			System.out.println(sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next())
			{
				ret = true;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			closePreparedStatement(ps);
			closeConnection(con);
		}
		return ret;
	}
	
}
