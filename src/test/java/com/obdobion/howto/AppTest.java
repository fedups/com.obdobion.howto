package com.obdobion.howto;

import org.junit.Test;

public class AppTest
{
    @Test
    public void testFindClasses() throws Exception
    {
        App.main(new String[] { "menu", "--help" });
    }
}
