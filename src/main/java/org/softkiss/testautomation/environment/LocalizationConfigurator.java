package org.softkiss.testautomation.environment;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by v.shcherbanyuk on 3/11/2015.
 */
public class LocalizationConfigurator {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LocalizationConfigurator.class);
    private static volatile LocalizationConfigurator localizationConfigurator;
    private static Properties properties = new Properties();

    private LocalizationConfigurator() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream i18nStream = loader.getResourceAsStream(String.format("i18n/%s.properties", System.getProperty("i18n", "en")));
        try {
            properties.load(i18nStream);
            i18nStream.close();
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    public static LocalizationConfigurator getInstance() {
        LocalizationConfigurator sysProps = localizationConfigurator;
        if (sysProps == null) {
            synchronized (EnvironmentConfigurator.class) {
                sysProps = localizationConfigurator;
                if (sysProps == null) {
                    try {
                        localizationConfigurator = sysProps = new LocalizationConfigurator();
                    } catch (IOException e) {
                        LOGGER.error("", e);
                    }
                }
            }
        }
        return sysProps;
    }

    public String getActionTextOnBoardRole() {
        return properties.getProperty("action.onboard.role");
    }

}
