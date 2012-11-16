/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author nicolas
 */
public interface Hello extends java.rmi.Remote {
    public void sayHello() throws java.rmi.RemoteException;
    public String connect(String id) throws java.rmi.RemoteException;
    public String send(String id,String message) throws java.rmi.RemoteException;
    public String bye(String id) throws java.rmi.RemoteException;
    public String who(String id) throws java.rmi.RemoteException;
    public String envoiCommande(String id,String commande) throws java.rmi.RemoteException;      
    public int getLast () throws java.rmi.RemoteException;
    public String last (int lastRead) throws java.rmi.RemoteException;
}