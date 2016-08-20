package com.obdobion.howto.writer;

import java.io.Closeable;

/**
 * <p>IOutlineWriter interface.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public interface IOutlineWriter extends Closeable
{

    /**
     * <p>append.</p>
     *
     * @param trim a {@link java.lang.String} object.
     */
    void append(String trim);

    /**
     * <p>decreaseLevel.</p>
     */
    void decreaseLevel();

    /**
     * <p>increaseLevel.</p>
     */
    void increaseLevel();

}
