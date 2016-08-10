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

    public ICmdLine getMyParser()
    {
        return myParser;
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

    public void setMyParser(final ICmdLine myParser)
    {
        this.myParser = myParser;
    }
}
