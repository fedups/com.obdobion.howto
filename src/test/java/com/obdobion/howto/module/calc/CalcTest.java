package com.obdobion.howto.module.calc;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.howto.App;
import com.obdobion.howto.Config;
import com.obdobion.howto.Context;
import com.obdobion.howto.PluginManager;

public class CalcTest
{
    @Test
    public void castError() throws Exception
    {
        final Calculator calc = new Calculator();
        final ICmdLine cmdline = CmdLine.load(calc, "\"date('08/30/2016') - date('1960-04-09')\"");

        final Config config = new Config(".");
        final Context context = PluginManager.createContext(config, new PluginManager(config));
        context.setParser(cmdline);
        calc.execute(context);
        context.getOutline().print(context);

        Assert.assertEquals("RESULT 20597", context.getOutline().getWriter().toString().trim());

        App.destroyContext(context);
    }

    @Test
    public void dateFmtFromEquation() throws Exception
    {
        final Calculator calc = new Calculator();
        final ICmdLine cmdline = CmdLine.load(calc, "\"datefmt('Apr 9, 1960', 'MMM d, yyyy')\"");

        final Config config = new Config(".");
        final Context context = PluginManager.createContext(config, new PluginManager(config));
        context.setParser(cmdline);
        calc.execute(context);
        context.getOutline().print(context);

        Assert.assertEquals("RESULT 1960-04-09", context.getOutline().getWriter().toString().trim());

        App.destroyContext(context);
    }

    @Test
    public void dateFromDirective() throws Exception
    {
        final Calculator calc = new Calculator();
        final ICmdLine cmdline = CmdLine.load(calc, "=(date('1960-04-09'))");

        final Config config = new Config(".");
        final Context context = PluginManager.createContext(config, new PluginManager(config));
        context.setParser(cmdline);
        calc.execute(context);
        context.getOutline().print(context);
        /*
         * This seems like an odd (although really even) result. Since the
         * command line actually converts to "calc 1960-04-09" you might expect
         * an error because dates are not a basic element to be parsed and need
         * to be wrapped in functions. However, close inspect would show that
         * this is 1960 * -4 * -9. Equations expect that the - sign, followed
         * immediately by a number indicates that it is a negative number.
         */
        Assert.assertEquals("RESULT 70560", context.getOutline().getWriter().toString().trim());

        App.destroyContext(context);
    }

    @Test
    public void dateFromEquation() throws Exception
    {
        final Calculator calc = new Calculator();
        final ICmdLine cmdline = CmdLine.load(calc, "\"date('1960-04-09')\"");

        final Config config = new Config(".");
        final Context context = PluginManager.createContext(config, new PluginManager(config));
        context.setParser(cmdline);
        calc.execute(context);
        context.getOutline().print(context);

        Assert.assertEquals("RESULT 1960-04-09", context.getOutline().getWriter().toString().trim());

        App.destroyContext(context);
    }

    @Test
    public void doubleResult() throws Exception
    {
        final Calculator calc = new Calculator();
        final ICmdLine cmdline = CmdLine.load(calc, "20000/25.7");

        final Config config = new Config(".");
        final Context context = PluginManager.createContext(config, new PluginManager(config));
        context.setParser(cmdline);
        calc.execute(context);
        context.getOutline().print(context);

        Assert.assertEquals("RESULT 778.210", context.getOutline().getWriter().toString().trim());

        App.destroyContext(context);
    }

    @Test
    public void longResult() throws Exception
    {
        final Calculator calc = new Calculator();
        final ICmdLine cmdline = CmdLine.load(calc, "\"20000 + 257\"");

        final Config config = new Config(".");
        final Context context = PluginManager.createContext(config, new PluginManager(config));
        context.setParser(cmdline);
        calc.execute(context);
        context.getOutline().print(context);

        Assert.assertEquals("RESULT 20257", context.getOutline().getWriter().toString().trim());

        App.destroyContext(context);
    }

    @Test
    public void stringResult() throws Exception
    {
        final Calculator calc = new Calculator();
        final ICmdLine cmdline = CmdLine.load(calc, "\"cat('abc', 'xyz')\"");

        final Config config = new Config(".");
        final Context context = PluginManager.createContext(config, new PluginManager(config));
        context.setParser(cmdline);
        calc.execute(context);
        context.getOutline().print(context);

        Assert.assertEquals("RESULT abcxyz", context.getOutline().getWriter().toString().trim());

        App.destroyContext(context);
    }

    @Test
    public void timeFmtFromEquation() throws Exception
    {
        final Calculator calc = new Calculator();
        final ICmdLine cmdline = CmdLine.load(calc, "\"timefmt('10:32', 'HH:mm')\"");

        final Config config = new Config(".");
        final Context context = PluginManager.createContext(config, new PluginManager(config));
        context.setParser(cmdline);
        calc.execute(context);
        context.getOutline().print(context);

        Assert.assertEquals("RESULT 10:32:00.000", context.getOutline().getWriter().toString().trim());

        App.destroyContext(context);
    }

}
