package com.obdobion.howto.module;

import java.util.regex.Pattern;

import com.obdobion.argument.annotation.Arg;
import com.obdobion.howto.Context;
import com.obdobion.howto.IPlugin;

/**
 * @author Chris DeGreef fedupforone@gmail.com
 *
 */
public class Menu implements IPlugin
{
    @Arg(shortName = 'm', help = "Only commands matching this pattern will be displayed.", caseSensitive = true)
    private Pattern matches;

    @Arg(positional = true,
            help = "Indicates the level of detail in the help display.",
            inList = { "MIN", "NORMAL", "MAX" },
            defaultValues = "normal")
    private String  detail;

    public Menu()
    {}

    /** {@inheritDoc} */
    @Override
    public int execute(final Context context)
    {
        for (final String commandName : context.getAllKnownCommands().keySet())
        {
            final IPlugin plugin = context.getAllKnownCommands().get(commandName);
            if ("min".equals(detail))
                context.getConsoleOutput().printf("%1$s\n", plugin.getName());
            else
                context.getConsoleOutput().printf("%1$-10s%2$s\n", plugin.getName(), plugin.getOverview());
        }
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return "menu";
    }

    /** {@inheritDoc} */
    @Override
    public String getOverview()
    {
        return "Shows a list of commands";
    }
}
