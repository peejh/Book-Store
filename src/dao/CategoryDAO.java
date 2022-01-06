package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.CategoryBean;

public class CategoryDAO {

	DataSource ds;
	
	public CategoryDAO() throws ClassNotFoundException{
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/BookStore");
		} catch (NamingException e){
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, CategoryBean> retrieveAll() throws SQLException {
		String query = "SELECT * FROM CATEGORY";
		HashMap<Integer, CategoryBean> rv = new HashMap<Integer, CategoryBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("ctgry_id");
			String name = r.getString("name");
			CategoryBean category = new CategoryBean(id, name);
			rv.put(id, category);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
}
