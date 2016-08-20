package com.obdobion.howto.writer;

import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.howto.Config;

/**
 * ConsoleWriter.
 *
 * Writes a simple text based output to the console or at least what the context
 * refers to as the console output.
 *
 * @author Chris DeGreef fedupforone@gmail.com
 *
 */
class SystemOutWriter extends ConsoleWriter
{
    private final static Logger logger = LoggerFactory.getLogger(SystemOutWriter.class.getName());

    @SuppressWarnings("hiding")
    PrintWriter                 console;

    SystemOutWriter(final Config config)
    {
        super(config);
        console = new PrintWriter(System.out);
        logger.debug("overriding output to System.out");
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
}
