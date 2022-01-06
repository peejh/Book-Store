package bean;

public class InventoryBean {

	private int bookId;
	private int inStock;
	
	public InventoryBean(int bookId, int inStock) {
		super();
		this.bookId = bookId;
		this.inStock = inStock;
	}
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getInStock() {
		return inStock;
	}
	public void setInStock(int inStock) {
		this.inStock = inStock;
	}
	
}
