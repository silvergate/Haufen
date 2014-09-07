package com.dcrux.haufen.impl.base;

import java.io.IOException;

/**
 * Created by caelis on 10/08/14.
 */
public interface IDataInput extends AutoCloseable {
    /**
     * Reads some bytes from an input
     * stream and stores them into the buffer
     * set {@code b}. The number of bytes
     * read is equal
     * to the length of {@code b}.
     * <p>
     * This method blocks until one of the
     * following conditions occurs:
     * <ul>
     * <li>{@code b.length}
     * bytes of input data are available, in which
     * case a normal return is made.
     * <p>
     * <li>End of
     * file is detected, in which case an {@code EOFException}
     * is thrown.
     * <p>
     * <li>An I/O error occurs, in
     * which case an {@code IOException} other
     * than {@code EOFException} is thrown.
     * </ul>
     * <p>
     * If {@code b} is {@code null},
     * a {@code NullPointerException} is thrown.
     * If {@code b.length} is zero, then
     * no bytes are read. Otherwise, the first
     * byte read is stored into element {@code b[0]},
     * the next one into {@code b[1]}, and
     * so on.
     * If an exception is thrown from
     * this method, then it may be that some but
     * not all bytes of {@code b} have been
     * updated with data from the input stream.
     *
     * @param b the buffer into which the data is read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws java.io.IOException  if an I/O error occurs.
     */
    void readFully(byte b[]);

    /**
     * Reads {@code len}
     * bytes from
     * an input stream.
     * <p>
     * This method
     * blocks until one of the following conditions
     * occurs:
     * <ul>
     * <li>{@code len} bytes
     * of input data are available, in which case
     * a normal return is made.
     * <p>
     * <li>End of file
     * is detected, in which case an {@code EOFException}
     * is thrown.
     * <p>
     * <li>An I/O error occurs, in
     * which case an {@code IOException} other
     * than {@code EOFException} is thrown.
     * </ul>
     * <p>
     * If {@code b} is {@code null},
     * a {@code NullPointerException} is thrown.
     * If {@code off} is negative, or {@code len}
     * is negative, or {@code off+len} is
     * greater than the length of the set {@code b},
     * then an {@code IndexOutOfBoundsException}
     * is thrown.
     * If {@code len} is zero,
     * then no bytes are read. Otherwise, the first
     * byte read is stored into element {@code b[off]},
     * the next one into {@code b[off+1]},
     * and so on. The number of bytes read is,
     * at most, equal to {@code len}.
     *
     * @param b   the buffer into which the data is read.
     * @param off an int specifying the offset into the data.
     * @param len an int specifying the number of bytes to read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws IOException          if an I/O error occurs.
     */
    void readFully(byte b[], int off, int len);

    /**
     * Reads and returns one input byte.
     * The byte is treated as a signed value in
     * the range {@code -128} through {@code 127},
     * inclusive.
     * This method is suitable for
     * reading the byte written by the {@code writeByte}
     * method of interface {@code DataOutput}.
     *
     * @return the 8-bit value read.
     * @throws java.io.EOFException if this stream reaches the end before reading
     *                              all the bytes.
     * @throws IOException          if an I/O error occurs.
     */
    byte readByte();

    long getPosition();

    long getLength();

    void seek(long position);

    void retain();

    void release();
}
