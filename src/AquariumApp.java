
import Aquarium.MainFrame;

public class AquariumApp {

    public static void main(String[] args) {
        Handler globalExceptionHandler = new Handler();
        Thread.setDefaultUncaughtExceptionHandler(globalExceptionHandler);
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}
