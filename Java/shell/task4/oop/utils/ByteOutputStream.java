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

import oop.streams.OutputStream;


 //cette classe permet d'envoyer des octets dans le tampon tant que celui-ci n'est pas plein.
public class ByteOutputStream extends ByteStream implements OutputStream {

    //cree un ByteRing avec la capacité donnee
    public ByteOutputStream(int capacity) {
        super(new ByteRing(capacity));
    }

    //le flux est dispo pour l'ecriture si le tampon n'est pas full
    @Override
    public boolean available() {
        return !m_ring.full();
    }

    /*
     * This method writes the given byte, 
     * if this stream is available, that is, 
     * if the method available() returns true. 
     * Otherwise, invoking this method throws
     * an illegal-state exception.
     */
    @Override
    public void write(byte bits) {
        // Si le flux n'est pas dispo, on leve une exception
        if (!available()) {
            throw new IllegalStateException("buffer plein");}
        // on push l'octet dans le tampon
        m_ring.push(bits);
   
        if (m_listener != null) { // notifie qu'un octet est ecrit, donc mring not full
            m_listener.available(this);}
    }
    /*
     * This is a request to write the bytes
     * in the range [offset,offset+length[.
     * This method returns the number of bytes
     * actually written, which may be zero.
     */
    @Override
    public int write(byte[] bytes, int offset, int length) {
        int cpt = 0;
          while (cpt < length && available()) {
           m_ring.push(bytes[offset + cpt]);
            cpt++;}
        if (m_listener != null) {
            m_listener.available(this);}
           return cpt; //nb d'octets ecrits
    }
}
