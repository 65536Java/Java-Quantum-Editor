package assets;

public class TimeManager {
    public void pauseMS(long L){
        try {
            Thread.sleep(L);
        } catch (InterruptedException e) {
            //Empty catch
        }
    }
}
