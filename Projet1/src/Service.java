import java.io.*;
import java.util.*;
import java.net.*;

public class Service extends Thread{
	Socket socket;
	Book livre;
	List<Book> tab, tabFinal; // liste des objets a envoyer et recus par le serveur 
	DataOutputStream out; // permet denvoyer du data via le reseau
	DataInputStream in; // permet de recevoir du data via le reseau
	ObjectInputStream inObjet; // permet de recevoir des objets via le reseau
	ObjectOutputStream outObjet; // permet denvoyer des objets via le reseau
	
	Service(Socket socket,List<Book> tab,List<Book> tabFinal) throws IOException{ 
		this.tabFinal= tabFinal;
		this.socket = socket;
		this.tab = tab;
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
		outObjet = new ObjectOutputStream(socket.getOutputStream());
		inObjet = new ObjectInputStream(socket.getInputStream());
		this.start(); // lance le thread
				
	}
	
	// fonction synchronise pour lenvoie dun objet et sa suppression de la list a un clients a la fois
	public synchronized void EnvoieLivre() {      
		Random rand = new Random();
		int chiffre = rand.nextInt(tab.size());
		livre = tab.get(chiffre);
		try {
			outObjet.writeObject(livre);
		}catch(IOException ex) {}
		tab.remove(chiffre);	
	}
	
	// fonction synchronise pour la reception dun objet remplis et lajout a la liste un client a la fois
	public synchronized void RangerLivre() {                
		try {
		Object obj = inObjet.readObject();
		Book livreRempli = (Book) obj;
		System.out.println("nouvel objet recu : "+livreRempli.get_nom()+" page : "+livreRempli.get_page());
		tabFinal.add(livreRempli);
		}catch(Exception exp) {}
	}
	
	public void run() {
		
		String nomClient,reponse, repClient;
		// passe false quand le client ne veux plus remplir dautre objet
		Boolean rester = true;                          	
		
		try {
			
			// en attente dune rep du client, valable pour tous les "in" et "inObjet"
			nomClient = in.readUTF();                      	
			reponse = "felicitation "+nomClient+" vouslez vous remplir des atributs ? ";
			
			//envoie de donné au client, valable pour tous les "out" et "outObjet"
			out.writeUTF(reponse);                         	
			repClient = in.readUTF();                       
			
			// si le client a repondu y et que il reste des objet a envoyer on continue
			if(repClient.contentEquals("y") && tab.size() != 0) {
				rester = true;
			}else {rester = false;}
			
			while(rester) {
				
				// synchronisation des processus pour lenvoie des objest au client
				EnvoieLivre();  
				
				// synchronisation des processus pour la reception et larchivage de lobjet envoyer par le client
				RangerLivre(); 
											
				reponse = "vouslez vous remplir un autre ? y = oui, autre touche = non";
				out.writeUTF(reponse);
				repClient = in.readUTF();
				
				if(repClient.equals("y") && tab.size() != 0) {
					rester = true;
				}else {rester = false;}
				
			}			
				
			in.close();
			out.close();
			socket.close();
			outObjet.close();
			inObjet.close();	
			
		}
		catch(IOException e) {
			System.out.println(e);
		}
		
	}		
}
