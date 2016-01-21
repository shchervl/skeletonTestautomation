package org.softkiss.testautomation.environment;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentConfigurator {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EnvironmentConfigurator.class);
    private static volatile EnvironmentConfigurator environmentConfigurator;
    private static Properties properties = new Properties();

    private EnvironmentConfigurator() throws IOException {

        ConfigSlurper configSlurper = new ConfigSlurper();
        configSlurper.setEnvironment(getTestEnvironment());

        ConfigObject configObject = configSlurper.parse(new File("config.groovy").toURI().toURL());

        configObject.flatten();
        properties = configObject.toProperties();
    }

    public static EnvironmentConfigurator getInstance() {
        EnvironmentConfigurator sysProps = environmentConfigurator;
        if (sysProps == null) {
            synchronized (EnvironmentConfigurator.class) {
                sysProps = environmentConfigurator;
                if (sysProps == null) {
                    try {
                        environmentConfigurator = sysProps = new EnvironmentConfigurator();
                    } catch (IOException e) {
                        LOGGER.error("", e);
                    }
                }
            }
        }
        return sysProps;
    }

    public Boolean isGridUsed() {
        return Boolean.parseBoolean(properties.getProperty("grid.isUsed"));
    }

    public String getSeleniumHub() {
        return properties.getProperty("grid.seleniumHub");
    }


    public static String getTestEnvironment() {
        return System.getProperty("env", "google");
    }

    public String getTestClient() {
        return System.getProperty("client", "gc");
    }

    public String getAppUrl() {
        return System.getProperty("url", properties.get("url").toString());
    }

}
