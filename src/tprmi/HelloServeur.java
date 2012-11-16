
import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class HelloServeur extends UnicastRemoteObject implements Hello {
    
    protected ArrayList<String> liste_users;
    protected HashMap<Integer,String> liste_messages;
    protected String message;
    // Implémentation du constructeur
    public HelloServeur(String msg) throws java.rmi.RemoteException {
        message = msg;
        liste_users = new ArrayList<String>();
        liste_messages = new HashMap<Integer,String>();
    }
    // Implémentation de la méthode distante
    @Override
    public void sayHello() throws java.rmi.RemoteException {
        System.out.println(message);
    }
    
    @Override
    public String connect(String id) throws RemoteException {
        //Verif si pseudo est dans liste, si oui connect(avec un tiret a la fin) et envoie un message pr prevenir, si non ajoute à liste.
        String reponse="";
        if (this.liste_users.contains(id)){
            reponse=reponse+"Cet identifiant n'est pas disponible, tentative de connexion sous l'identifiant "+id+"_.\n";
            this.connect(id+"_");
        }
        else {
            this.liste_users.add(id);
            reponse=reponse+("Connexion réussie en tant que "+id+" !");
        }
        System.out.println(reponse);
        return id+"##"+reponse;
    }

    @Override
    public String send(String id,String message) throws RemoteException {
        //Ajoute à liste_message sous format <Id>:messageblabla avec l'id de message le plus gros +1
        String reponse="";
         if (this.liste_users.contains(id)){
             int max=0;
             for (int k:this.liste_messages.keySet()) {
                 if (max<k)
                 {
                     max=k;
                 }
             }
             
             liste_messages.put(max+1,"<"+id+">: "+message);
             //reponse=reponse+("<"+id+">: "+message);
         }
         else {
            reponse="Cet id n'est pas connecté, send impossible !";
         }
         System.out.println(reponse);
         return reponse;
    }

    @Override
    public String bye(String id) throws RemoteException {
        // Envoie un msg d'adieu et le raye de la liste_users
        String reponse="";
        if (this.liste_users.contains(id)){
            this.liste_users.remove(id);
             reponse="Le user "+id+" a été deconnecté.";
        }
        else {
            reponse="Cet id n'est pas connecté, déco impossible !";
        }
        System.out.println(reponse);
        return reponse;
    }

    @Override
    public String who(String id) throws RemoteException {
        //Parcourt liste_users et envoie un message qui les liste
        String reponse="";
        if (this.liste_users.contains(id)){
            String temp="";
            int n=0;
            for (String user:this.liste_users) {
                temp=temp+user+"\n";
                n=n+1;
            }
            reponse=reponse+(n+" utilisateurs connectés :\n"+temp);
        }
        else {
            reponse=reponse+("Cet id n'est pas connecté, requete who impossible !");
        }
        System.out.println(reponse);
        return reponse;
    }
    
    public String last (int lastRead){
        String reponse="";
        for (int i:this.liste_messages.keySet()) {
            if (i>lastRead)
            {
                reponse=reponse+(this.liste_messages.get(i)+"\n");
            }
        }
        return reponse;
    }
    
    public int getLast () {
        int max=0;
        for (int k:this.liste_messages.keySet()) {
            if (max<k)
            {
                max=k;
            }
        }
        return max;
    }
    
    public String envoiCommande (String id,String commande) {
        String reponse ="";
        if (commande.split(" ")[0].equals("connect"))
        {
            try {
                reponse=this.connect(commande.substring(8));
                
            }
            catch (Exception exc) {
                System.out.println(exc.getMessage());
            }
        }
        else if (commande.split(" ")[0].equals("bye"))
        {
            try {
                reponse=this.bye(id);
                
            }
            catch (Exception exc) {
                System.out.println(exc.getMessage());
            }
        }
        else if (commande.split(" ")[0].equals("who"))
        {
            try {
                reponse=this.who(id);
                
            }
            catch (Exception exc) {
                System.out.println(exc.getMessage());
            }
        }
        else if (commande.split(" ")[0].equals("send"))
        {
            try {
                reponse=this.send(id,commande.substring(5));
                
            }
            catch (Exception exc) {
                System.out.println(exc.getMessage());
            }
        }
        else
        {
            reponse = "COMMANDE INVALIDE !";
        }
        return reponse;
    }
    

    public static void main(String args[]) {
        int port=8080; String URL;
        /*try {
            // transformation d ’une chaîne de caractères en entier
            Integer I = new Integer(args[0]);
            port = I.intValue();
        }
        catch (Exception ex) {
            System.out.println(" Please enter: Server <port>"); return;
        }*/

        try {
            // Création du serveur de nom - rmiregistry
            Registry registry = LocateRegistry.createRegistry(port);
            // Création d ’une instance de l’objet serveur
            Hello obj = new HelloServeur("Serveur de test!");
            URL = "//"+InetAddress.getLocalHost().getHostName()+":"+port+"/chat_serveur";
            //URL ="//127.0.0.1:8080/chat-serveur";
            Naming.rebind(URL, obj);
            
        }
        catch (Exception exc) {System.out.println(exc.getMessage());}
    }
}