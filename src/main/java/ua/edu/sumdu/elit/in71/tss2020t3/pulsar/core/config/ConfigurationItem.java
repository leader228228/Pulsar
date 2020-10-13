package ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.config;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * This is an enum that represents a configurable item
 * for an application instance
 *
 * @author Mykhailo Birintsev
 * */
public enum ConfigurationItem {
    /**
     * Represents a port which an application instance will use
     * */
    SERVER_PORT("pulsar.server.port"),

    /**
     * Represents a database connection URL
     * */
    DATABASE_URL("pulsar.database.connection.url"),

    /**
     * Represents a username which will be used when connecting to a database
     * */
    DATABASE_USER("pulsar.database.connection.user"),

    /**
     * Represents a password of a database user profile
     * which will be used when connecting to a database
     * */
    DATABASE_PASSWORD("pulsar.database.connection.password"),

    /**
     * Represents a JDBC connection driver class
     * which will be used when interacting with a database
     * */
    DATABASE_DRIVER_CLASS("pulsar.database.connection.driver_class"),

    /**
     * Represents a dialect class, which will be used by Hibernate framework
     * <p>
     * See more <a href="https://hibernate.org/orm/documentation">here</a>
     * */
    DATABASE_DIALECT("pulsar.database.dialect"),

    /**
     * Represents a root directory for file-based log files
     * */
    LOG_DIRECTORY("pulsar.log.directory");

    /**
     * Returns a {@link ConfigurationItem} that represents passed property
     *
     * @param       property                a property name
     * @return                              an instance of
     *                                      {@link ConfigurationItem}
     *                                      that stands for passed property
     * @exception   NullPointerException    if passed {@code property}
     *                                      is {@code null}
     * @exception   NoSuchElementException  if there is not any
     *                                      {@link ConfigurationItem}
     *                                      representing passed property
     * */
    public static ConfigurationItem valueOfProperty(String property) {
        return Arrays.stream(values())
            .filter(ci -> property.equals(ci.propertyName))
            .findFirst()
            .orElseThrow(
                () -> new NoSuchElementException(
                    "There is not any ConfigurationItem for" + property
                )
            );
    }

    /**
     * A name of this configuration item
     * configured in a {@code .properties} file
     *
     * @see java.util.Properties
     * @see PropertyBasedAppConfig
     * @see PropertiesAppConfigService
     * */
    private final String propertyName;

    ConfigurationItem(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * This method is created to support {@code .property}-based configuration
     * of an application instance
     *
     * @return a name of a property
     * */
    public String getPropertyName() {
        return propertyName;
    }
}