package ua.edu.sumdu.elit.in71.tss2020t3.pulsar.cli.commands;

import static ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.ApplicationPropertiesNames.LOG_DIRECTORY;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import picocli.CommandLine;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.Main;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.cli.CLICommandExecutionResult;

/**
 * This class is a common way to start an Active Tracker instance
 * using the command line.
 * It is supposed, that an Active Tracker instance will be started as following:
 * <p>
 * {@code java -jar /path/to/jar startup parameters}
 * <p>
 * A list of {@code startup parameters} is below:
 * <ul>
 * <li>
 *     {@code -f, -file} - a configuration file path
 * </li>
 * </ul>
 *
 * @see     CLICommand
 * @author  Mykhailo Birnintsev
 * */
@CommandLine.Command(
    name = StartActiveTrackerCLICommand.TRACK_COMMAND_NAME,
    description = "Command for starting an Active Tracker instance"
)
public class StartActiveTrackerCLICommand
    implements
    CLICommand<Scheduler>, Callable<CLICommandExecutionResult<Scheduler>> {

    public static final String TRACK_COMMAND_NAME = "track";

    @CommandLine.Option(names = {"-f", "-file"}, required = true)
    private File customApplicationPropertiesFile;

    private static final Logger LOGGER = Logger.getLogger(
        StartActiveTrackerCLICommand.class
    );

    private final Supplier<Scheduler> schedulerProvider;

    private String[] arguments;

    /**
     * A default constructor for dependency injection
     *
     * @param schedulerProvider a provider for
     * */
    public StartActiveTrackerCLICommand(
        Supplier<Scheduler> schedulerProvider
    ) {
        this.schedulerProvider = schedulerProvider;
    }

    /**
     * This method is a shortcut for
     * {@link #setArgs(String[] args)}.{@link #execute()} methods invocations
     *
     * @param   args        a set of command line parameters for this
     *                      application instance startup.
     *                      See example of valid {@code args} in javadoc for
     *                      {@link StartActiveTrackerCLICommand#canHandle}
     *
     * @throws  Exception   if some exception happened during setting,
     *                      validation the {@code args}
     *                      or command execution with them
     *
     * @see                 #setArgs(String[])
     * @see                 #execute()
     * */
    @Override
    public CLICommandExecutionResult<Scheduler> execute(String[] args)
        throws Exception {
        return setArgs(args).execute();
    }

    /**
     * Informs whether passed arguments relates to this command.
     * It is assumed that <strong>supposedly</strong> valid set of
     * startup {@code args} has "start" keyword as its first element.
     * Note, that the keyword is case-sensitive.
     * <p>
     * Examples of valid sets of startup arguments are below:
     * </p>
     * <ul>
     * <li>{@code -f /path/to/config/file}</li>
     * <li>{@code -file /path/to/config/file}</li>
     * </ul>
     * */
    @Override
    public boolean canHandle(String[] args) {
        return new CommandLine(this)
            .parseArgs(args).errors().size() == 0;
    }

    /**
     * Sets a set of arguments for this application startup command.
     * See example of valid {@code args} in javadoc for
     * {@link StartActiveTrackerCLICommand#canHandle}.
     *
     * @param       args                        a set of command line parameters
     *                                          for application instance startup
     * @exception   IllegalArgumentException    if this command can not handle
     *                                          passed set of args
     * @see                                     CLICommand#canHandle(String[])
     * @see                                     CLICommand#setArgs
     * */
    @Override
    public CLICommand<Scheduler> setArgs(String[] args) {
        if (!canHandle(args)) {
            throw new IllegalArgumentException(
                "Passed arguments can not be handled"
            );
        }
        this.arguments = args;
        return this;
    }

    /**
     * Starts an Active Tracker instance
     * according to previously set {@code args}
     * <p>
     * See arguments description on the class-level javadoc
     *
     * @return                              running application instance
     * @throws      Exception               if some error occurred during
     *                                      application starting
     * @exception   FileNotFoundException   if specified config file
     *                                      was not found
     * @exception   IllegalStateException   if command can not be executed.
     *                                      This exception can be caused
     *                                      by different reasons
     *                                      (e.g. if a config file
     *                                      is not specified)
     *
     * @see                                 Scheduler
     * */
    @Override
    public CLICommandExecutionResult<Scheduler> execute() throws Exception {
        if (!canHandle(arguments)) {
            throw new IllegalArgumentException(
                "This command can not handle provided command line input:"
                    + System.lineSeparator() + Arrays.toString(arguments)
            );
        }
        if (customApplicationPropertiesFile == null) {
            throw new IllegalStateException(
                "Application configuration file is not specified"
            );
        }
        if (!customApplicationPropertiesFile.isFile()) {
            throw new FileNotFoundException(
                "Can not find server configurations file "
                    + customApplicationPropertiesFile.getAbsolutePath()
            );
        }
        Properties appConfig = new Properties();
        appConfig.load(new FileInputStream(customApplicationPropertiesFile));
        Main.updateSystemProperties(appConfig);
        // The block of code below is command-specific. It provides
        // a possibility to forward logs into another directory during runtime
        if (appConfig.stringPropertyNames().contains(LOG_DIRECTORY)) {
            Main.reconfigureLoggers();
        }
        Scheduler scheduler = schedulerProvider.get();
        scheduler.start();
        return () -> scheduler;
    }

    @Override
    public CLICommandExecutionResult<Scheduler> call() throws Exception {
        return execute();
    }
}
