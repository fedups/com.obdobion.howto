package com.obdobion.howto;

import org.junit.Test;

/**
 * <p>AppTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 0.0.3
 */
public class AppTest
{
    /**
     * <p>historyNoArgs.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void historyNoArgs() throws Exception
    {
        App.main(new String[] { "history", "" });
    }

    /**
     * <p>historyWithArgs.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void historyWithArgs() throws Exception
    {
        App.main(new String[] { "history", "-c 4 -m modify" });
    }

    /**
     * <p>makeAPlugin.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void makeAPlugin() throws Exception
    {
        App.main(new String[] { "MAP", "--d com.obdobion --c whatEver --gro JUNIT --git c:/tmp/gitRepos" });
    }

    /**
     * <p>menuNoArgs.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void menuNoArgs() throws Exception
    {
        App.main(new String[] { "menu", "" });
    }

    /**
     * <p>menuWithArgs.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void menuWithArgs() throws Exception
    {
        App.main(new String[] { "S.Me", "--matches 'menu|modify' -s" });
    }
}
