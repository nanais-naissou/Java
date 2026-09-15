package test;

import java.util.ArrayList;
import java.util.List;
import oop.streams.OutputStream;

public class FluxSortie implements OutputStream {

    // liste pour stocker les octets ecrits 
      private List<Byte> BytesEcrits = new ArrayList<>();
      private boolean closed = false;  // indique si le flux est fermé
    	private Listener listener;  //flux est dispo ou fermé
  
  @Override
  public void set(Listener l) {
      this.listener = l;
  }
  
    @Override
    public void close() {
        closed = true;
        if(listener != null){
          listener.closed(this);
        }
    }
  
  @Override
  public boolean closed() {
      return closed; 
  }
  
    @Override
    public boolean available() {
        return !closed;
    }
  
  @Override
  public void write(byte bits) {
    if (!available()) {
          throw new IllegalStateException("Flux fermé");  // erreur si le flux est fermé
    }
    BytesEcrits.add(bits);  //on ajoute l'octet a la liste
    if(listener != null) { 
      listener.available(this); //on notifie le listener que le flux est dispo
    }
  }
  
    @Override
    public int write(byte[] bytes, int offset, int length) {
        int count = 0;
        for (int i = offset; i < offset + length; i++) {
            write(bytes[i]);  // on ecrit chaque octet
            count++;
        }
        return count; 
    }
  
  //ici méthode pour recuperer la liste des octets ecrits
  public List<Byte> getBytesEcrits() {
        return BytesEcrits;
  }
  
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // on boucle sur chaque octet et on l'ajoute dans la chaine, separes par un espace
        for (byte b : BytesEcrits) {
           sb.append(b).append(" ");
        }
        return sb.toString();
    }
}
