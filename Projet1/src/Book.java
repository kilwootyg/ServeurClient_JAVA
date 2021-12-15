import java.io.*;

public class Book implements Serializable {

	private String nom; 
	private int page;
	
	public Book() {}
	public Book(String nom, int page) {
		this.nom = nom;
		this.page = page;
	}
	public String get_nom() {return nom;}
	public int get_page() {return page;}
	public void set_name(String nom) {this.nom = nom;}
	public void set_page(int page) {this.page = page;}
	
}
