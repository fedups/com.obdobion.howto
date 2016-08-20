package com.obdobion.howto.module;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.regex.Pattern;

import com.obdobion.argument.annotation.Arg;
import com.obdobion.howto.Context;
import com.obdobion.howto.IPluginCommand;
import com.obdobion.howto.PluginNotFoundException;

/**
 * <p>
 * Menu class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class Menu implements IPluginCommand
{
    @Arg(shortName = 'm', help = "Only commands matching all patterns will be displayed.")
    private Pattern[] matches;

    @Arg(allowCamelCaps = true, shortName = 's')
    private boolean   sortDescending;

    int               longestGroupLength = 6;
    int               longestNameLength  = 8;

    /**
     * <p>
     * Constructor for Menu.
     * </p>
     */
    public Menu()
    {
    }

    private boolean allMatchersMatch(final String output)
    {
        if (matches == null)
            return true;
        for (final Pattern pattern : matches)
            if (!pattern.matcher(output).find())
                return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int execute(final Context context)
    {
        context.setRecordingHistory(false);

        final List<String> keys = context.getPluginManager().allNames();
        keys.sort((o1, o2) -> {
            if (sortDescending)
                return o2.compareTo(o1);
            return o1.compareTo(o2);
        });

        keys.forEach(pluginName -> {
            try
            {
                final IPluginCommand pluginCommand = context.getPluginManager().get(pluginName);
                if (pluginCommand.getGroup().length() > longestGroupLength)
                    longestGroupLength = pluginCommand.getGroup().length();
                if (pluginCommand.getName().length() > longestNameLength)
                    longestNameLength = pluginCommand.getName().length();

            } catch (final PluginNotFoundException e)
            {
            }
        });

        final String layout = "%1$-" + longestGroupLength + "s %2$" + longestNameLength + "s | %3$s\n";

        context.getOutline().printf(layout, "Group", "Command", "Overview");
        context.getOutline().printf(layout, "-----", "-------", "--------");

        keys.forEach(pluginName -> {
            try
            {
                final StringWriter sw = new StringWriter();
                try (PrintWriter pw = new PrintWriter(sw))
                {
                    final IPluginCommand pluginCommand = context.getPluginManager().get(pluginName);

                    pw.printf(layout, pluginCommand.getGroup(), pluginCommand.getName(), pluginCommand.getOverview());
                    final String output = sw.toString();

                    if (allMatchersMatch(output))
                        context.getOutline().printf(sw.toString());
                }
            } catch (final PluginNotFoundException e)
            {
            }
        });

        context.getOutline().printf(layout, "-----", "-------", "--------");
        return 0;
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
        return "menu";
    }

    /** {@inheritDoc} */
    @Override
    public String getOverview()
    {
        return "Shows a list of commands";
    }
}
