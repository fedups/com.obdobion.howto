package com.obdobion.howto;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;

/**
 *
 *
 */
final public class App
{
    private final static Logger logger = LoggerFactory.getLogger(App.class.getName());

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
    private Map<String, IPlugin> commands;

    private String               commandName;
    private String[]             commandLineArgs;

    private App(final String[] commandLineArgs)
    {
        try
        {
            config = new Config(workDir());
        } catch (IOException | ParseException e)
        {
            System.err.println("loading config: " + e.getMessage());
            return;
        }
        LogManager.resetConfiguration();
        DOMConfigurator.configure(config.getLog4jConfigFileName());
        loadCommands();

        if (commandLineArgs.length == 0)
        {
            setCommandName("menu");
            setCommandLineArgs(new String[0]);
        } else
        {
            setCommandName(commandLineArgs[0]);
            if (commandLineArgs.length == 1)
                setCommandLineArgs(new String[0]);
            else
                setCommandLineArgs(Arrays.copyOfRange(commandLineArgs, 1, commandLineArgs.length));
        }
    }

    private IPlugin getCommand()
    {
        return commands.get(commandName);
    }

    private String[] getCommandLineArgs()
    {
        return commandLineArgs;
    }

    private String getCommandName()
    {
        return commandName;
    }

    private boolean isCommandSupported()
    {
        return commands.containsKey(getCommandName());
    }

    private void loadCommands()
    {
        /*
         * The pluginClassLoader not only includes all of the extension plugin
         * jars but also the classes in this app itself.
         */
        loadPlugins("plugin", config.getPluginClassLoader());
    }

    private void loadPlugins(final String category, final ClassLoader classLoader)
    {
        final ServiceLoader<IPlugin> pluginLoader = ServiceLoader.load(IPlugin.class, classLoader);

        commands = new HashMap<>();
        for (final IPlugin plugin : pluginLoader)
        {
            if (plugin == null)
                break;
            if (plugin.getName() == null)
            {
                logger.warn("commands must be named: {}", plugin.getClass().getName());
                continue;
            }
            if (commands.containsKey(plugin.getName()))
            {
                logger.warn("commands must be uniquely named: {} in {}",
                        plugin.getName(), plugin.getClass().getName());
                continue;
            }
            commands.put(plugin.getName(), plugin);
            logger.debug("loaded {} {} as \"{}\"", category, plugin.getClass().getName(), plugin.getName());
        }
    }

    private int run()
    {
        try
        {
            if (!isCommandSupported())
                throw new ParseException("unknown command: " + getCommandName(), 0);

            final ICmdLine cmdline = new CmdLine(getCommand().getName(), getCommand().getOverview());
            CmdLine.load(cmdline, getCommand(), getCommandLineArgs());
            if (((CmdLine) cmdline).isUsageRun())
                return 0;

            context = new Context();
            context.setMyParser(cmdline);
            context.setAllKnownCommands(commands);
            context.setConsoleOutput(new PrintWriter(System.out));
            context.setConsoleErrorOutput(new PrintWriter(System.err));

            getCommand().execute(context);

            context.getConsoleOutput().flush();
            context.getConsoleErrorOutput().flush();

        } catch (IOException | ParseException e)
        {
            logger.error("loading command {}", e.getMessage(), e);
            System.err.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    private void setCommandLineArgs(final String[] commandLineArgs)
    {
        this.commandLineArgs = commandLineArgs;
    }

    private void setCommandName(final String commandName)
    {
        this.commandName = commandName;
    }

}
