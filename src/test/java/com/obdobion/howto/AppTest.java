package com.obdobion.howto;

import org.junit.Test;

public class AppTest
{
    @Test
    public void historyNoArgs() throws Exception
    {
        App.main(new String[] { "history", "" });
    }

    @Test
    public void historyWithArgs() throws Exception
    {
        App.main(new String[] { "history", "-c 4 -m modify" });
    }

    @Test
    public void menuNoArgs() throws Exception
    {
        App.main(new String[] { "menu", "" });
    }

    @Test
    public void menuWithArgs() throws Exception
    {
        App.main(new String[] { "menu", "--matches 'menu|modify' -s" });
    }
}
