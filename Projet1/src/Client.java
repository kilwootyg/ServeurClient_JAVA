import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;

public class Client {

	public static void main(String[] args) {
		Socket s;
		Scanner sc;
		String nomClient,reponse;
		boolean continuer = true;
		Object instance = null;
		
		
		try {
		
		
			s = new Socket("localhost", 4998);
			sc = new Scanner(System.in);
			
			// "in" et "inObjet" pour les entrées, "out" et "outObjet" pour les sorties
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			DataInputStream in = new DataInputStream(s.getInputStream());
			ObjectOutputStream outObjet = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream inObjet = new ObjectInputStream(s.getInputStream());
			
			System.out.println("nom client : ");
			nomClient = sc.next();
			out.writeUTF(nomClient); 
			
			reponse = in.readUTF();  
			System.out.println(reponse);
			System.out.println("taper y pour oui et nimportte pour non ");
			reponse = sc.next();
			
			
			if(reponse.equals("y")) {
				continuer = true;	
			}else{continuer = false;}
			
			out.writeUTF(reponse);
			
			// tant que le client fait "y", continuer reste true et la boucle recommence
			while(continuer) {
					
				try {
					
					// contiendra le type de l'objet recus par le serveur 
					Class<?> c = inObjet.readObject().getClass();
					System.out.println("--objet recus--");
					
					// contient toutes les methode
					Method[] method = c.getDeclaredMethods(); 
					//contient tous les attributs de l'objet
					Field[] fields = c.getDeclaredFields();
					
					Method set_name = null,set_page=null;
					
					//affiche tous les attributs de l'objets
					for(int i=0;i<fields.length;i++) { 
						System.out.println("atribut "+(i+1)+" : "+fields[i].getName()+" | type : "+fields[i].getType().getName());			
					}
					System.out.println(" ");
					
					
					// pour recupéré les methode set_page et set_name de l'objet					
					for(int i=0;i<method.length;i++) { 
						
						if(method[i].getName().equals("set_name")) {
							set_name = method[i];
						}else if(method[i].getName().equals("set_page")) {
							set_page = method[i];
						}
					}
					
					try {
						
						// creation dun objet du meme type que celui recus par le serveur
						instance = c.newInstance();
						
						for(int i=0; i < fields.length;i++) {
							System.out.println("saisiez l'atribut : "+fields[i].getName()+" de l'objet : "+c.getName());
							if(fields[i].getType().getName().equals("java.lang.String")) {
								set_name.invoke(instance, sc.next());
							}else if(fields[i].getType().getName().equals("int")) {
								set_page.invoke(instance, sc.nextInt());
							}else {System.out.println("ca na pas marcher");}
						}					
					
					}catch(Exception e) {}
					
					/* dans le cas d'objets possedant uniquement des attributs publics 
					 j'aurais pu modifier ces derniers juste en ayant leur type 
					 sans avoir a passer par une methode set de lobjet en question et 
					 sans avoir a faire ce genr de "magouille" car je n'ai pas trouver dautre 
					 tout seul */
					
					outObjet.writeObject(instance);
					
				}catch(ClassNotFoundException ex){}
				
				reponse = in.readUTF();
				System.out.println(reponse);
				reponse = sc.next();
				out.writeUTF(reponse);
				
				if(reponse.equals("y")) {
					continuer = true;
				}else{continuer = false;}				

			}
			
			in.close();
			out.close();
			inObjet.close();
			outObjet.close();
			s.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

}
