package com.obdobion.howto;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.howto.writer.IOutlineWriter;
import com.obdobion.howto.writer.OutlineWriters;

/**
 * <p>
 * Outline class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class Outline
{
    private final static Logger logger = LoggerFactory.getLogger(Outline.class.getName());

    Config                      config;
    IOutlineWriter              writer;
    String                      contents;
    List<Outline>               children;

    Outline(final Config config)
    {
        super();
        this.config = config;
        writer = OutlineWriters.create(config);
    }

    /**
     * <p>
     * add.
     * </p>
     *
     * @param childContents
     *            a {@link java.lang.String} object.
     * @param childArguments
     *            a {@link java.lang.Object} object.
     * @return a {@link com.obdobion.howto.Outline} object.
     */
    public Outline add(final String childContents, final Object... childArguments)
    {
        if (children == null)
            children = new ArrayList<>();
        final Outline child = new Outline(config);
        child.setContents(childContents, childArguments);
        children.add(child);
        return child;
    }

    /**
     * <p>
     * getCurrent.
     * </p>
     *
     * @return a {@link com.obdobion.howto.Outline} object.
     */
    public Outline getCurrent()
    {
        if (children == null || children.size() == 0)
            return this;
        return children.get(children.size() - 1).getCurrent();
    }

    /**
     * <p>
     * print.
     * </p>
     *
     * @param context
     *            a {@link com.obdobion.howto.Context} object.
     */
    public void print(final Context context)
    {
        if (context.isSubcontext())
            return;

        try (IOutlineWriter ow = OutlineWriters.create(config))
        {
            print(ow);

        } catch (final IOException e)
        {
            logger.error("attempting to print outline", e);
        }
    }

    void print(final IOutlineWriter ow)
    {
        if (contents != null)
            ow.append(contents.trim());
        if (children != null)
        {
            ow.increaseLevel();
            for (final Outline child : children)
                child.print(ow);
            ow.decreaseLevel();
        }
    }

    public void printf(final String format, final Object... args)
    {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        pw.printf(format, args);
        writer.append(sw.toString());
    }

    public void reset()
    {
        children = null;
        contents = null;
    }

    /**
     * <p>
     * Setter for the field <code>contents</code>.
     * </p>
     *
     * @param contents
     *            a {@link java.lang.String} object.
     * @param arguments
     *            a {@link java.lang.Object} object.
     */
    public void setContents(final String contents, final Object... arguments)
    {
        final StringBuilder sb = new StringBuilder();
        try (Formatter fmtr = new Formatter(sb))
        {
            fmtr.format(contents, arguments);
            this.contents = sb.toString();
        }
    }
}
