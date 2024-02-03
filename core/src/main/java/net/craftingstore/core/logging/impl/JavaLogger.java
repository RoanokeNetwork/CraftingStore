package net.craftingstore.core.logging.impl;

import net.craftingstore.core.logging.CraftingStoreLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaLogger extends CraftingStoreLogger {

    public Logger LOGGER = LoggerFactory.getLogger("CraftingStore");

    public JavaLogger(Logger logger) {
        this.LOGGER = logger;
    }

    public void info(String message) {
        LOGGER.info(message);
    }
    public void info(String message, Throwable throwable) {
        LOGGER.info(message, throwable);
    }

    public void warn(String message) {LOGGER.warn(message);}

    public void warn(String message, Throwable throwable) {LOGGER.warn(message, throwable);}

    public void error(String message) { LOGGER.error(message); }
    public void error(String message, Throwable throwable) { LOGGER.error(message, throwable); }
}
