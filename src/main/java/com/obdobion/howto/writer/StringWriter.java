package com.obdobion.howto.writer;

import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.howto.Config;

/**
 * ConsoleWriter.
 *
 * Writes a simple text based output to the console. This class is necessary for
 * supporting jUnits. The java Console class returns null when they run. The
 * selection of this class is controlled through the Config file. And in the
 * case of jUnit testing that file is in the src/test/resources folder.
 *
 * @author Chris DeGreef fedupforone@gmail.com
 *
 */
public class StringWriter extends ConsoleWriter
{
    private final static Logger logger = LoggerFactory.getLogger(StringWriter.class.getName());

    final java.io.StringWriter  sw;
    @SuppressWarnings("hiding")
    PrintWriter                 console;

    public StringWriter(final Config config)
    {
        super(config);
        sw = new java.io.StringWriter();
        console = new PrintWriter(sw);
        logger.debug("overriding output to a java.io.StringWriter");
    }

    @Override
    void newline()
    {
        setCurrentLineLength(getIndentSize() * getCurrentIndentLevelForWrapping());
        setNewLineCount(getNewLineCount() + 1);
        if (getNewLineCount() > 2)
            /*
             * suppress more than one blank line
             */
            return;
        console.printf("\n");
    }

    /**
     * Don't use new lines in this.
     */
    @Override
    void printf(final String contents)
    {
        setNewLineCount(0);
        console.printf(contents);
        console.flush();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return sw.toString();
    }
}
