package com.obdobion.howto;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.annotation.Arg;
import com.obdobion.argument.type.WildFiles;

final public class Config
{
    private final static Logger logger = LoggerFactory.getLogger(Config.class.getName());

    @Arg(caseSensitive = true)
    private WildFiles           plugins;

    @Arg(caseSensitive = true, longName = "log4j")
    private String              log4jConfigFileName;

    @Arg(caseSensitive = true)
    private String              version;

    private final String        configFileLocation;
    private final ICmdLine      properties;
    private ClassLoader         pluginClassLoader;

    public Config(final String appDir) throws IOException, ParseException
    {
        configFileLocation = System.getProperty("howto.config",
                appDir + "/src/test/resources/howto.cfg");
        /*
         * Saving an instance of the CmdLine so that it can be exported later if
         * necessary.
         */
        properties = CmdLine.loadProperties(this, new File(configFileLocation));
        setPluginClassLoader(new URLClassLoader(getPluginJars(), this.getClass().getClassLoader()));
    }

    public String getLog4jConfigFileName()
    {
        return log4jConfigFileName;
    }

    public ClassLoader getPluginClassLoader()
    {
        return pluginClassLoader;
    }

    private URL[] getPluginJars() throws ParseException, IOException
    {
        final List<File> jarList = plugins.files();
        final URL[] urlArray = new URL[jarList.size()];
        int j = 0;
        for (final File jar : jarList)
        {
            urlArray[j++] = jar.toURI().toURL();
            logger.debug("found plugin service jar: {}", jar.getAbsolutePath());
        }
        return urlArray;
    }

    public void saveToDisk() throws ParseException, IOException
    {
        properties.pull(this);
        properties.exportNamespace(new File(configFileLocation));
    }

    private void setPluginClassLoader(final URLClassLoader urlClassLoader)
    {
        pluginClassLoader = urlClassLoader;
    }
}
