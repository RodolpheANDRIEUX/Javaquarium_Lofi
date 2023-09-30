import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Logger;

class Handler implements UncaughtExceptionHandler {
    private static final Logger LOGGER = Logger.getLogger(Handler.class.getName());

    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.severe("Exception non handled dans aquarium ! " + e);
    }
}
