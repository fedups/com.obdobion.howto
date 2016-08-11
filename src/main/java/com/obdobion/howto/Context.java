package com.obdobion.howto;

import java.io.PrintWriter;
import java.util.Map;

import com.obdobion.argument.ICmdLine;

final public class Context
{
    private Map<String, IPlugin> allKnownCommands;
    private PrintWriter          consoleOutput;
    private PrintWriter          consoleErrorOutput;
    private ICmdLine             myParser;
    private long                 startNanoTime;
    private long                 endNanoTime;
    private boolean              recordingHistory;

    public Map<String, IPlugin> getAllKnownCommands()
    {
        return allKnownCommands;
    }

    public PrintWriter getConsoleErrorOutput()
    {
        return consoleErrorOutput;
    }

    public PrintWriter getConsoleOutput()
    {
        return consoleOutput;
    }

    public long getEndTime()
    {
        return endNanoTime;
    }

    public ICmdLine getParser()
    {
        return myParser;
    }

    public long getStartTime()
    {
        return startNanoTime;
    }

    public boolean isRecordingHistory()
    {
        return recordingHistory;
    }

    public void setAllKnownCommands(final Map<String, IPlugin> allKnownCommands)
    {
        this.allKnownCommands = allKnownCommands;
    }

    public void setConsoleErrorOutput(final PrintWriter pw)
    {
        consoleErrorOutput = pw;
    }

    public void setConsoleOutput(final PrintWriter pw)
    {
        consoleOutput = pw;
    }

    public void setEndTime(final long nanoTime)
    {
        endNanoTime = nanoTime;
    }

    public void setParser(final ICmdLine myParser)
    {
        this.myParser = myParser;
    }

    public void setRecordingHistory(final boolean recordingHistory)
    {
        this.recordingHistory = recordingHistory;
    }

    public void setStartTime(final long nanoTime)
    {
        startNanoTime = nanoTime;
    }
}
