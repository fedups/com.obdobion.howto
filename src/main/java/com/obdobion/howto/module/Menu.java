package com.obdobion.howto.module;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
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
    @Arg(shortName = 'm', help = "Only commands matching all patterns will be displayed.")
    private Pattern[] matches;

    @Arg(allowCamelCaps = true, shortName = 's')
    private boolean   sortDescending;

    public Menu()
    {}

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

        final Set<String> keys = context.getAllKnownCommands().keySet();
        final TreeSet<String> sortedKeys = new TreeSet<>(keys);

        Iterator<String> iter = null;
        if (sortDescending)
            iter = sortedKeys.descendingIterator();
        else
            iter = sortedKeys.iterator();

        while (iter.hasNext())
        {
            final IPlugin plugin = context.getAllKnownCommands().get(iter.next());

            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            pw.printf("%1$-10s%2$s\n", plugin.getName(), plugin.getOverview());
            final String output = sw.toString();

            if (allMatchersMatch(output))
                context.getConsoleOutput().append(sw.toString());
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
