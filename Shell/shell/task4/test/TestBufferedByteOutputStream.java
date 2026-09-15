/*
 * ce test écrit 20 octets dans le buffer, ferme le flux pour le flush,
 * puis affiche le contenu du flux sousjacent (fluxSortie ici)
 * si 20 octets sont bien écrits, le test affiche "OK: Test 2 validé"
 */
package test;

import oop.utils.BufferedByteOutputStream;

public class TestBufferedByteOutputStream {
    public static void main(String[] args) {
        int chunkCapacity = 5; 
        
        // on crée un flux pour stocker les octets écrits
        FluxSortie flux = new FluxSortie();
        //on crée un buffer qui wrappe le flux avec une capacité de chunk de 5 octets
        BufferedByteOutputStream bbos = new BufferedByteOutputStream(chunkCapacity, flux);
        
        for (int i = 0; i < 20; i++) { 
            bbos.write((byte) i);
            System.out.println("Écrit: " + i);}
        	bbos.close();
        
        System.out.println("Octets écrits dans le flux sousjacent:");
          String output = flux.toString();
          System.out.println(output);
        
        //on vérifie que les 20 octets ont bel et bien ete ecrits
        String[] parts =output.trim().split(" ");
        if (parts.length == 20) {
            System.out.println("OK: Test 2 validé");
        }  else { 
            System.out.println("test échoué car nombre d'octets incorrect"); }
    }
}
