package com.obdobion.howto;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

import org.apache.log4j.NDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.howto.module.Empty;

/**
 * <p>
 * App class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
final public class App
{
    private final static Logger logger = LoggerFactory.getLogger(App.class.getName());

    /**
     * <p>
     * destroyContext.
     * </p>
     *
     * @param context
     *            a {@link com.obdobion.howto.Context} object.
     */
    static public void destroyContext(final Context context)
    {
        if (context == null)
            return;
        context.getConsoleErrorOutput().flush();
    }

    /**
     * <p>
     * main.
     * </p>
     *
     * @param args
     *            an array of {@link java.lang.String} objects.
     */
    public static void main(final String[] args)
    {
        final int rc = new App(args).run();
        if (rc != 0)
            System.exit(rc);
    }

    /**
     * @return
     */
    static private String workDir()
    {
        return System.getProperty("user.dir");
    }

    private Config               config;
    private Context              context;

    private final PluginManager  pluginManager;

    private String               currentCommandName;
    private String[]             currentCommandLineArgs;

    private final HistoryManager histman;

    private App(final String[] commandLineArgs)
    {
        try
        {
            config = new Config(workDir());

        } catch (IOException | ParseException e)
        {
            System.err.println("loading config: " + e.getMessage());
            System.exit(-1);
        }

        if (commandLineArgs.length == 0)
        {
            setCommandName(Empty.GROUP + "." + Empty.NAME);
            setCommandLineArgs(new String[0]);
        } else
        {
            setCommandName(commandLineArgs[0]);
            if (commandLineArgs.length == 1)
                setCommandLineArgs(new String[0]);
            else
                setCommandLineArgs(Arrays.copyOfRange(commandLineArgs, 1, commandLineArgs.length));
        }

        histman = new HistoryManager(config);
        pluginManager = new PluginManager(config);
        pluginManager.loadCommands();
    }

    private String[] getCommandLineArgs()
    {
        return currentCommandLineArgs;
    }

    private String getCommandName()
    {
        return currentCommandName;
    }

    private int run()
    {
        try
        {
            NDC.push(getCommandName());
            context = pluginManager.run(getCommandName(), getCommandLineArgs());
            destroyContext(context);

        } catch (PluginNotFoundException | IOException | ParseException e)
        {
            logger.error("unsuccessfull {}", e.getMessage(), e);
            System.err.println(e.getMessage());
            return -1;
        } finally
        {
            histman.record(context);
            NDC.pop();
        }
        return 0;
    }

    private void setCommandLineArgs(final String[] commandLineArgs)
    {
        currentCommandLineArgs = commandLineArgs;
    }

    private void setCommandName(final String commandName)
    {
        currentCommandName = commandName;
    }

}
