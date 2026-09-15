/*
 *  Copyright (C) Pr. Olivier Gruber <olivier dot gruber at acm dot org>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package oop.utils;

import java.util.LinkedList;
import java.util.Queue;
import oop.streams.OutputStream;


public class BufferedByteOutputStream implements OutputStream {

    private final int chunkCapacity;       
    private final OutputStream flux;  
    private final Queue<Chunk> chunkQueue; // file des chunks complets en attente d'etre flush
    private Chunk currentChunk;            
    private boolean closed = false;        //flux est fermé or not
    private Listener listener;             

    private static class Chunk { //classe interne représentant un chunk 

        byte[] data; 
        int pos;   //index next octet  

        Chunk(int capacity) {
            data = new byte[capacity];
            pos = 0;
        }
    }

 
    public BufferedByteOutputStream(int capacity, OutputStream os) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("La capacité doit être positive");
        }
        this.chunkCapacity = capacity;
        this.flux = os;
        this.chunkQueue = new LinkedList<>();
        this.currentChunk = new Chunk(chunkCapacity);
    }

    @Override
    public void set(Listener l) {
        this.listener = l;
    }

    @Override
    public void close() {
        
        flushBuffer(); // avant de fermer on tente de vider tout le tampon dans le flux "temporaire"
        closed = true;
        flux.close();
        if (listener != null) {
            listener.closed(this);}
    }

    @Override
    public boolean closed() {
        return closed;
    }

    @Override
    public boolean available() {
        return !closed; //accepte d'ecrire tant que le flux n'est pas closed
    }

    @Override
    public void write(byte bits) {
        if (closed()) {
            throw new IllegalStateException("Flux fermé");}
        if (currentChunk.pos >= chunkCapacity) {
            chunkQueue.offer(currentChunk);
            currentChunk = new Chunk(chunkCapacity);
        }
        currentChunk.data[currentChunk.pos++] = bits;
                flushBuffer();
        
        if (listener != null) {
            listener.available(this);}
    }

    @Override
    public int write(byte[] bytes, int offset, int length) {
        if (closed()) {
            throw new IllegalStateException("Flux fermé"); }
        int cpt = 0;
        while (cpt < length && available()) {
            write(bytes[offset + cpt]);
            cpt++;}
        return cpt;
    }

    /**
     * Flush le tampon en essayant d'écrire les chunks complets et, si possible, le chunk courant (s'il contient des données)
     * dans le flux sous-jacent, tant que ce dernier est disponible.
     */
    private void flushBuffer() {
        //flush des chunks complets dans la file.
        while (!chunkQueue.isEmpty() && flux.available()) {
            Chunk chunk = chunkQueue.peek(); //renvoie le chunk tete de la file
            try {
                flux.write(chunk.data, 0, chunk.pos);
                // si l'écriture réussit on retire le chunk de la file.
                chunkQueue.poll();
            } catch (IllegalStateException e) {
                break;  // si le flux n'est plus dispo, on arrete la tentative 
            }}
        // mtn on soccupe de flush le chunk courant s'il contient des données et que le flux est dispo
        if (currentChunk.pos > 0 && flux.available()) {
                flux.write(currentChunk.data, 0, currentChunk.pos);
                currentChunk.pos = 0; // réinitialise le chunk courant aprres lecriture
          
        }
    }
}
