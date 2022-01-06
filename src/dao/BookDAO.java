package dao;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.BookBean;

public class BookDAO {

	DataSource ds;
	
	public BookDAO() throws ClassNotFoundException{
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/BookStore");
		} catch (NamingException e){
			e.printStackTrace();
		}
	}
	
	public HashMap<String, BookBean> retrieveAll() throws SQLException{
		String query = "SELECT * FROM BOOK";
		HashMap<String, BookBean> rv = new HashMap<String, BookBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("book_id");
			String title = r.getString("title");
			String author = r.getString("author");
			int categoryId = r.getInt("ctgry_id");
			double price = r.getDouble("price");
			String description = r.getString("description");
			String isbn = r.getString("isbn");
			BookBean book = new BookBean(id, title, author, categoryId, price, description, isbn);
			rv.put(isbn, book);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	public BookBean retrieveBookByIsbn(String isbn) throws SQLException{
		String query = "SELECT * FROM BOOK WHERE isbn = ?";
		BookBean book = null;
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1, isbn);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("book_id");
			String title = r.getString("title");
			String author = r.getString("author");
			int categoryId = r.getInt("ctgry_id");
			double price = r.getDouble("price");
			String description = r.getString("description");
			book = new BookBean(id, title, author, categoryId, price, description, isbn);
		}
		r.close();
		p.close();
		con.close();
		return book;
	}

	public BookBean retrieveBookById(int id) throws SQLException{
		String query = "SELECT * FROM BOOK WHERE book_id = ?";
		BookBean book = null;
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, id);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			String title = r.getString("title");
			String author = r.getString("author");
			int categoryId = r.getInt("ctgry_id");
			double price = r.getDouble("price");
			String description = r.getString("description");
			String isbn = r.getString("isbn");
			book = new BookBean(id, title, author, categoryId, price, description, isbn);
		}
		r.close();
		p.close();
		con.close();
		return book;
	}
	
	public HashMap<String, BookBean> retrieveByCategory(int wantedCategory) throws SQLException{
		String query = "SELECT * FROM BOOK WHERE ctgry_id = ?";
		HashMap<String, BookBean> rv = new HashMap<String, BookBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, wantedCategory);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("book_id");
			String title = r.getString("title");
			String author = r.getString("author");
			int categoryId = r.getInt("ctgry_id");
			double price = r.getDouble("price");
			String description = r.getString("description");
			String isbn = r.getString("isbn");
			BookBean book = new BookBean(id, title, author, categoryId, price, description, isbn);
			rv.put(isbn, book);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	
	public Map<String, BookBean> getProductById(int bid) throws SQLException{
		String query = "SELECT * FROM BOOK WHERE BOOK_ID = " + bid;
		Map<String, BookBean> rv = new HashMap<String, BookBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("book_id");
			String title = r.getString("title");
			String author = r.getString("author");
			int categoryId = r.getInt("ctgry_id");
			double price = r.getDouble("price");
			String description = r.getString("description");
			String isbn = r.getString("isbn");
			BookBean book = new BookBean(id, title, author, categoryId, price, description, isbn);
			rv.put(isbn, book);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	public String getJsonByProductId(int bid) throws SQLException {
		Map<String, BookBean> rv = getProductById(bid);
		StringWriter sw = new StringWriter();
		sw.write("\n");
		int count = 0;
		for (BookBean bb : rv.values()) {
			count++;
			JsonObject value = Json.createObjectBuilder().add("BOOK_ID", bb.getId()).add("TITLE", bb.getTitle()).add
					("AUTHOR", bb.getAuthor()).add("CTGRY_ID", Integer.toString(bb.getCtgyId())).add
					("PRICE", Double.toString(bb.getPrice())).add("DESCRIPTION", bb.getDescription()).add
					("ISBN", bb.getIsbn()).build();
			sw.write(value.toString());
			if (count < rv.size()) {
				sw.write(",\n");
			}
		}
		return sw.toString();
	}
	
	public HashMap<String, BookBean> retrieveByKeyWords(String wantedKeyWords) throws SQLException{
		String query = "SELECT * FROM BOOK";
		HashMap<String, BookBean> rv = new HashMap<String, BookBean>();
		String[] keyWords = wantedKeyWords.split(" ");
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("book_id");
			String title = r.getString("title");
			String author = r.getString("author");
			int categoryId = r.getInt("ctgry_id");
			double price = r.getDouble("price");
			String description = r.getString("description");
			String isbn = r.getString("isbn");
			BookBean book = new BookBean(id, title, author, categoryId, price, description, isbn);
			Boolean containsAllTitle = true;
			Boolean containsAllAuthor = true;
			for (String t : keyWords) {
				if (!title.toLowerCase().contains(t.toLowerCase())) {
					containsAllTitle = false;
					break;
				}	
			}
			
			for (String t : keyWords) {
				if (!author.toLowerCase().contains(t.toLowerCase())) {
					containsAllAuthor = false;
					break;
				}
			}
			
			if (containsAllTitle || containsAllAuthor) {
				rv.put(isbn, book);
			}
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
}
