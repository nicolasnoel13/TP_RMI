import java.io.*;
import java.net.InetAddress;
import java.rmi.*;

public class HelloClient {
    
    protected String id = null;
    protected int lastRead;
    
    
    public HelloClient(String id) {
        this.id=id;
        lastRead=0;
    }
    
    public void setId (String ident) {
        this.id=ident;
    }

    
    public String getId () {
        return this.id;
    }
    
    public static String lireClavier() {
        String ligne_lue=null;
        try{
            BufferedReader entree = new BufferedReader(new InputStreamReader(System.in));
            ligne_lue = entree.readLine();
        }
        catch(IOException exc){
          System.out.println(exc.getMessage());
        }
        return ligne_lue;
    }
   
    
    public static void main(String args[]) {
        try {
            // Récupération d'un stub sur l'objet serveur.
            int port=8080;
            Hello obj = (Hello) Naming.lookup("//"+InetAddress.getLocalHost().getHostName()+":"+port+"/chat_serveur");
            
            // Appel d'une méthode sur l'objet distant.
            obj.sayHello();
            HelloClient client = new HelloClient("");
            String tempClavier=null;
            boolean estConnecte=false;
            
            while (true) {
                if (estConnecte){
                    System.out.println(obj.last(client.lastRead));
                    client.setLastRead(obj.getLast());
                }
                tempClavier=lireClavier();
                if (tempClavier.split(" ")[0].equals("connect") && !estConnecte)
                {
                    try {
                        String reponse[]=obj.envoiCommande(client.id, tempClavier).split("##");
                        client.setId(reponse[0]);
                        System.out.println(reponse[1]);
                        estConnecte=true;
                    }
                    catch (Exception exc) {
                        System.out.println(exc.getMessage());
                    }
                }
                
                if (estConnecte) {
                    System.out.println(obj.envoiCommande(client.getId(), tempClavier));
                }

            }
            
            
        }
        catch (Exception exc) {}
    }

    public int getLastRead() {
        return lastRead;
    }

    public void setLastRead(int lastRead) {
        this.lastRead = lastRead;
    }
}