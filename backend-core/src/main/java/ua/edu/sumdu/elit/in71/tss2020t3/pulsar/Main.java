package ua.edu.sumdu.elit.in71.tss2020t3.pulsar;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.cli.CLIFrontController;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.cli.commands.StartApplicationInstanceCLICommand;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.ApplicationPropertiesNames;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.guice.modules.CLIModule;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.guice.modules.ActiveTrackerModule;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.guice.modules.UtilsModule;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.guice.modules.core.ConversionServicesModule;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.guice.modules.core.DatabaseModule;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.guice.modules.core.ServicesModule;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.guice.modules.core.application.ApplicationModule;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.guice.modules.core.application.HandlersModule;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    private static final String JAVA_PROTOCOL_HANDLERS_PKGS_PROPERTY =
        "java.protocol.handler.pkgs";

    public static final List<String> DEFAULT_PROPERTIES_BUNCHES_LOCATIONS =
        Arrays.asList(
            "/default.properties"
        );

    static {
        System.setProperty(
            ApplicationPropertiesNames.LOG_DIRECTORY,
            getRunningDirectory().getAbsolutePath()
        );

        String newProtocolHandlersPackage =
            "ua.edu.sumdu.elit.in71.tss2020t3.pulsar.protocolhandlers";
        String oldProtocolHandlersPackages = System.getProperty(
            JAVA_PROTOCOL_HANDLERS_PKGS_PROPERTY
        );
        System.setProperty(
            JAVA_PROTOCOL_HANDLERS_PKGS_PROPERTY,
            oldProtocolHandlersPackages == null
                ? newProtocolHandlersPackage
                : newProtocolHandlersPackage + "|" + oldProtocolHandlersPackages
        );

        updateSystemProperties(getDefaultProperties());

        reconfigureLoggers();
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(getAllModules());
        CLIFrontController cliFrontController = injector.getInstance(
            CLIFrontController.class
        );
        cliFrontController.handle(args);
    }

    /**
     * @return                     a {@link File} instance that represents
     *                             the parent directory of this jar file
     * @exception RuntimeException if an exception occurs
     *                             when identifying parent folder
     *                             for a jar in runtime
     * */
    public static File getRunningDirectory() {
        try {
            return new File(
                new File(StartApplicationInstanceCLICommand.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()).getParent()
            );
        } catch (Exception e) {
            LOGGER.error(
                "Can not identify parent folder for the running jar file", e
            );
            throw new RuntimeException(e);
        }
    }

    /**
     * Reloads log4j configuration
     * */
    public static void reconfigureLoggers() {
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(
            Main.class.getResourceAsStream("/log4j.properties")
        );
    }

    /**
     * Updates the {@link System} properties with values from {@code properties}
     *
     * @param properties a set of properties to be added/updated
     *                   in the {@link System}
     * */
    public static void updateSystemProperties(Properties properties) {
        Properties newSystemProperties = new Properties(System.getProperties());
        for (String key : properties.stringPropertyNames()) {
            newSystemProperties.setProperty(key, properties.getProperty(key));
        }
        System.setProperties(newSystemProperties);
    }

    // returns all the application components configuration modules
    public static List<Module> getAllModules() {
        return Arrays.asList(
            new UtilsModule(),
            new ActiveTrackerModule(),
            new ApplicationModule(),
            new HandlersModule(),
            new ConversionServicesModule(),
            new DatabaseModule(),
            new ServicesModule(),
            new CLIModule()
        );
    }

    private static Properties getDefaultProperties() {
        Properties defaultProperties = new Properties();
        DEFAULT_PROPERTIES_BUNCHES_LOCATIONS.forEach(
            propertiesLocation -> {
                try {
                    defaultProperties.load(
                        Main.class
                            .getResourceAsStream(
                                propertiesLocation
                            )
                    );
                } catch (IOException e) {
                    LOGGER.error(
                        "Error during default properties loading",
                        e
                    );
                    throw new UncheckedIOException(e);
                }
            }
        );
        return defaultProperties;
    }
}
