package com.obdobion.howto.module;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.argument.annotation.Arg;
import com.obdobion.howto.Context;
import com.obdobion.howto.HistoryManager;
import com.obdobion.howto.HistoryManager.HistoryRecord;
import com.obdobion.howto.IPlugin;

public class History implements IPlugin
{
    private final static Logger logger = LoggerFactory.getLogger(History.class.getName());

    @Arg(shortName = 'm', help = "Only history matching all patterns will be displayed.", caseSensitive = true)
    private Pattern[]           matches;

    @Arg(shortName = 'c', help = "Limit to this many rows of output.", range = { "1" }, defaultValues = "10")
    private int                 count;

    public History()
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

    @Override
    public int execute(final Context context)
    {
        int outputCount = 0;
        int startingPoint = 0;
        context.setRecordingHistory(false);
        final List<HistoryRecord> history = HistoryManager.getInstance().getHistory();
        logger.debug("found {} history items", history.size());
        /*
         * Scan backwards to know where to start showing when going in the
         * forward direction.
         */
        for (startingPoint = history.size(); startingPoint > 0; startingPoint--)
        {
            final String output = history.get(startingPoint - 1).getContents();
            if (allMatchersMatch(output))
                if (++outputCount == count)
                    break;
        }

        outputCount = 0;
        for (; startingPoint < history.size(); startingPoint++)
        {
            final String output = history.get(startingPoint).getContents();
            if (allMatchersMatch(output))
                context.getConsoleOutput().printf("%1$d: %2$s\n", startingPoint + 1, output);
        }
        return 0;
    }

    @Override
    public String getName()
    {
        return "history";
    }

    @Override
    public String getOverview()
    {
        return "Show / modify the history of commands";
    }

}
