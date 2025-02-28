package com.ffxivcensus.gatherer;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class to run instance of GatherController from the CLI.
 * 
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.GathererController
 * @since v1.0
 */
@SpringBootApplication
public class Console implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(Console.class);
    // Hold the command-line arguments so that we can reference them in Spring
    private static String[] cliArgs;
    // Main Gathering Controller
    private GathererController controller;

    public static void main(final String[] args) {
        cliArgs = args;
        SpringApplication.run(Console.class, args);
    }

    /**
     * Run the program provided a set of arguments.
     * 
     * @param args arguments to supply to the system.
     */
    public void run(final String... args) {
        try {
            if(controller != null) {
                controller.run();
            }
        } catch(ParseException pEx) {
            new HelpFormatter().printHelp(CLIConstants.CLI_USAGE, CLIConstants.setupOptions());
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Autowired
    public void setGathererController(final GathererController controller) {
        this.controller = controller;
    }

    /**
     * Returns the arguments passed in from the CLI.
     * These are held in a static array so that they're available for Spring configurations to use later in the boot sequence.
     * 
     * @return Array of CLI arguments passed in at runtime.
     */
    public static String[] getARGS() {
        return cliArgs;
    }

}
