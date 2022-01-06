package bean;

public class BookBean {

	private int id;
	private String title;
	private String author;
	private int ctgyId;
	private double price;
	private String description;
	private String isbn;
	
	public BookBean(int id, String title, String author, int ctgyId, double price, String description, String isbn) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.ctgyId = ctgyId;
		this.price = price;
		this.description = description;
		this.isbn = isbn;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getCtgyId() {
		return ctgyId;
	}
	public void setCtgyId(int ctgyId) {
		this.ctgyId = ctgyId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getDescriptionSample() {
		int wordLimit = 42;	
		String result = description.replaceAll("\\s+", " ").trim();
		String[] words = result.split(" ");
		
		if (words.length < wordLimit)
			return description;
		else {
			result = "";
			for (int i = 0; i < wordLimit; i++) {
				result += words[i] + " ";
			}
			return result + "...";
		}
	}
	
}
