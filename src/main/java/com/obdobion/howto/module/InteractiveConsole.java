package com.obdobion.howto.module;

import java.io.Console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.howto.Context;
import com.obdobion.howto.IPluginCommand;

public class InteractiveConsole implements IPluginCommand
{
    private final static Logger logger = LoggerFactory.getLogger(InteractiveConsole.class.getName());

    private Context             context;
    private Thread              consoleInputThread;
    private boolean             stop;

    public InteractiveConsole()
    {
    }

    @Override
    public int execute(final Context p_context)
    {
        context = p_context;
        logger.debug("interactive console opened");

        setConsoleInputThread(new Thread()
        {
            @Override
            public void run()
            {
                setStop(false);

                final Console c = System.console();
                if (c == null)
                {
                    logger.error("the system console is not available");
                    System.err.println("No console.");
                    System.exit(1);
                    return;
                }

                while (true)
                {
                    if (isStop())
                        return;
                    final String aLine = c.readLine("howto > ");
                    if (aLine == null)
                        return;
                    if (aLine.trim().length() == 0)
                        continue;

                    processInputRequest(aLine.trim());

                }
            }
        });
        getConsoleInputThread().start();
        try
        {
            getConsoleInputThread().join();
        } catch (final InterruptedException e)
        {
            logger.debug("waiting for interactive console input", e);
        }
        logger.trace("interactive console closed");
        return 0;
    }

    /**
     * @return the consoleInputThread
     */
    Thread getConsoleInputThread()
    {

        return consoleInputThread;
    }

    /** {@inheritDoc} */
    @Override
    public String getGroup()
    {
        return "System";
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return "interactiveConsole";
    }

    /** {@inheritDoc} */
    @Override
    public String getOverview()
    {
        return "Interactive console mode";
    }

    /**
     * @return the stop
     */
    boolean isStop()
    {

        return stop;
    }

    private void processInputRequest(final String inputRequest)
    {
        String commandName;
        String arguments;

        if (inputRequest.length() == 0)
        {
            commandName = "menu";
            arguments = "";
        } else
        {
            final int firstWordEnd = inputRequest.indexOf(' ');
            if (firstWordEnd <= 0)
            {
                commandName = inputRequest;
                arguments = "";

            } else
            {
                commandName = inputRequest.substring(0, firstWordEnd);
                arguments = inputRequest.substring(firstWordEnd + 1);
            }
        }

        if (commandName.equalsIgnoreCase("stop"))
        {
            stop();
            return;
        }

        context.getPluginManager().run(context,
                context.getPluginManager().uniqueNameFor(commandName),
                arguments);

        // histman.record(context);

        context.getOutline().print(context);
        context.getOutline().reset();
    }

    /**
     * @param consoleInputThread
     *            the consoleInputThread to set
     */
    void setConsoleInputThread(final Thread consoleInputThread)
    {

        this.consoleInputThread = consoleInputThread;
    }

    /**
     * @param stop
     *            the stop to set
     */
    void setStop(final boolean stop)
    {

        this.stop = stop;
    }

    void stop()
    {

        setStop(true);
    }
}
