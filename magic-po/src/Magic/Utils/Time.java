package Magic.Utils;


public class Time {
    /**
     * Waits!
     */
    public static void sleep(){
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
