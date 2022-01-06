package model;

import bean.BookBean;

public class CartItem {

	private BookBean b;
	private int qty;
	
	public CartItem(BookBean b, int qty) {
		this.b = b;
		this.qty = qty;
	}
	
	public BookBean getBook() {
		return b;
	}
	
	public int getQuantity() {
		return qty;
	}
	
	public double getSubtotal() {
		double sub = b.getPrice() * qty;
		return ((int) (sub * 100)) / 100.0;
	}
}
