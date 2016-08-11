package com.obdobion.howto;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.annotation.Arg;
import com.obdobion.argument.type.WildFiles;

final public class Config
{
    private final static Logger logger        = LoggerFactory.getLogger(Config.class.getName());

    @Arg(caseSensitive = true, required = true)
    private WildFiles           plugins;

    @Arg(caseSensitive = true, longName = "log4j", required = true)
    private String              log4jConfigFileName;

    @Arg(caseSensitive = true, required = true)
    private String              version;

    @Arg(caseSensitive = true, required = true)
    private File                history;

    private final String        configFileLocation;
    private final ICmdLine      properties;
    private ClassLoader         pluginClassLoader;

    private final Pattern       badJarPattern = Pattern.compile(
                                                      "junit|commons-codec|slf4j|log4j|algebrain|argument|calendar|howto-[.0-9]+jar");

    public Config(final String appDir) throws IOException, ParseException
    {
        configFileLocation = System.getProperty("howto.config",
                appDir + "/src/test/resources/howto.cfg");
        /*
         * Saving an instance of the CmdLine so that it can be exported later if
         * necessary.
         */
        properties = CmdLine.loadProperties(this, new File(configFileLocation));

        LogManager.resetConfiguration();
        DOMConfigurator.configure(getLog4jConfigFileName());

        final URL[] pluginJars = getPluginJars();
        if (pluginJars.length > 0)
            setPluginClassLoader(new URLClassLoader(pluginJars, this.getClass().getClassLoader()));
    }

    public File getHistoryFile()
    {
        return history;
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
        final List<URL> loadableJars = new ArrayList<>();
        for (final File jar : jarList)
            if (isLoadableJar(jar))
            {
                loadableJars.add(jar.toURI().toURL());
                logger.debug("found plugin jar: {}", jar.getAbsolutePath());
            }

        final URL[] urlArray = new URL[loadableJars.size()];
        int j = 0;
        for (final URL jar : loadableJars)
            urlArray[j++] = jar;
        return urlArray;
    }

    /**
     * Do not load jars that are part of this howto app. They would create odd
     * situations because they would overload the jars that this app expects.
     *
     * @param jar
     * @return
     */
    private boolean isLoadableJar(final File jar)
    {
        final boolean bad = badJarPattern.matcher(jar.getName()).find();
        return !bad;
    }

    public void saveToDisk() throws ParseException, IOException
    {
        properties.pull(this);
        properties.exportNamespace(new File(configFileLocation));
    }

    private void setPluginClassLoader(final ClassLoader urlClassLoader)
    {
        pluginClassLoader = urlClassLoader;
    }
}
