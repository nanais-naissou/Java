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

import oop.streams.InputStream;


public class ByteInputStream extends ByteStream implements InputStream {

    public ByteInputStream(ByteOutputStream os) {
        super(os.m_ring);
    }

    // vice versa, le flux d'entree est dispo pour la lecture si le tampon n'est pas vide
    @Override
    public boolean available() {
        return !m_ring.empty();
    }
    /*
     * Reads the next available byte.
     * This method must only be invoked if this
     * stream is available, that is, if the method
     * available() returns true. 
     * Otherwise, invoking this method throws
     * an illegal-state exception.
     */
    @Override
    public byte read() {
        if (!available()) {
            throw new IllegalStateException("buffer vide");
        }
        byte b = m_ring.pull();
        if (m_listener != null) {m_listener.available(this);}
        return b;
    }
    
    
    /*
     * Fills in the given array with available 
     * bytes, starting at the given offset.
     * This method returns the number of bytes
     * actually read, which may be zero and otherwise
     * is always less or equal to the given length.
     */
    @Override
    public int read(byte[] bytes, int offset, int length) {
        int read = 0;
        while (read < length && available()) {
            bytes[offset + read] = m_ring.pull();
            read++;
        }
        if (m_listener != null) {
            m_listener.available(this);}
        return read;
    }
}

