import java.net.*;
import java.util.*;
import java.io.*;

public class Serveur {

	private static final int port=4998;
	private static ServerSocket ss;
	private static Socket s;
	private static List<Book> tab = new ArrayList<>(); // contient les objets a envoyer au client
	private static List<Book> tab2 = new ArrayList<>(); // contiendras les objets recus des clients
	
	public static void main(String[] args){
		
			// pour le nombre de clients connecter		
			int compteur = 0; 
			
			// les objets a envoyer au client
			Book livre1 = new Book();
			Book livre2 = new Book();
			Book livre3 = new Book();
			Book livre4 = new Book();
			Book livre5 = new Book();
			Book livre6 = new Book();
			Book livre7 = new Book();
			Book livre8 = new Book();
			Book livre9 = new Book();
			Book livre10 = new Book();
			
			tab.add(livre1);
			tab.add(livre2);
			tab.add(livre3);
			tab.add(livre4);
			tab.add(livre5);
			tab.add(livre6);
			tab.add(livre7);
			tab.add(livre8);
			tab.add(livre9);
			tab.add(livre10);
			
			// creation du serveur
			try { 
				ss = new ServerSocket(port); 
				System.out.println("Serveur lance, port : " + (port));			
			}catch(IOException e){ System.out.println("serveur non lancé");System.out.println(e); System.exit(-1);}
						
			// connection des clients 
			try { 
				
				// serveur peut accepter 3 clients
				while(compteur != 3) { 
					
					System.out.println("en attente de connexion ");
					s = ss.accept();
					
					String client_Ip = s.getInetAddress().toString() ;
					System.out.format("connexion etablie avec : %s \n", client_Ip);
					
					compteur +=1;
					System.out.println("objet dispo ---"+tab.size()+"--- ");
					System.out.println("objet recus ---"+tab2.size()+"--- ");
					
					// ouverture dun thread pour un nouveau clien 
					new Service(s,tab,tab2); 
										
				}
								
				ss.close();
				s.close();
				
			}catch(IOException e) {System.out.println("erreur de connection");System.out.println(e);}

			System.out.format("Serveur eteint");
						
	}

}
