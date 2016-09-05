package com.obdobion.howto;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.howto.writer.StringWriter;

/**
 * <p>
 * AppTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 0.0.3
 */
public class AppTest
{
    @Test
    public void consoleWriter1() throws Exception
    {
        final StringWriter cw = new StringWriter(new Config(App.workDir()));
        cw.append("012345678912345678921234567893123456789\n41234567895123456789612345678971234567898123456789", 0);
        Assert.assertEquals("012345678912345678921234567893123456789\n" +
                "41234567895123456789612345678971234567898123456789",
                cw.toString());
        cw.close();
    }

    @Test
    public void consoleWriter2() throws Exception
    {
        final StringWriter cw = new StringWriter(new Config(App.workDir()));
        cw.append("012345678912345678921234567893123456789 41234567895123456789612345678971234567898123456789", 0);
        Assert.assertEquals("012345678912345678921234567893123456789\n" +
                "41234567895123456789612345678971234567898123456789",
                cw.toString());
        cw.close();
    }

    @Test
    public void consoleWriter3() throws Exception
    {
        final StringWriter cw = new StringWriter(new Config(App.workDir()));
        cw.append("012345678912345678921234567893123456789\n\n41234567895123456789612345678971234567898123456789\n", 0);
        Assert.assertEquals("012345678912345678921234567893123456789\n\n" +
                "41234567895123456789612345678971234567898123456789\n",
                cw.toString());
        cw.close();
    }

    /**
     * <p>
     * historyNoArgs.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void historyNoArgs() throws Exception
    {
        App.main(new String[] { "history", "" });
    }

    /**
     * <p>
     * historyWithArgs.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void historyWithArgs() throws Exception
    {
        App.main(new String[] { "history", "-c 4 -m modify" });
    }

    /**
     * <p>
     * makeAPlugin.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void makeAPlugin() throws Exception
    {
        App.main(new String[] { "MAP", "--d com.obdobion --c whatEver --gro JUNIT --git c:/tmp/gitRepos" });
    }

    /**
     * <p>
     * menuNoArgs.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void menuNoArgs() throws Exception
    {
        App.main(new String[] { "menu", "" });
    }

    /**
     * <p>
     * menuWithArgs.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void menuWithArgs() throws Exception
    {
        App.main(new String[] { "S.Me", "--matches 'menu|modify' -s" });
    }
}
