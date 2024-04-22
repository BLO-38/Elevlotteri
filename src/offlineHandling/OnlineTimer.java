package offlineHandling;

public class OnlineTimer extends Thread {
    private final int TIME = 25;
    private StudentPanel student;
    private boolean reset = false;

    public OnlineTimer(StudentPanel st) {
        student = st;
        start();
    }

    public void resetTimer() {
        reset = true;
    }

    @Override
    public void run() {
        for (int i = 0; i < 6; i++) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(reset) {
                student.tick(true);
                i = 0;
                reset = false;
                System.out.println("Vi resettade timern");
            }
            else student.tick(false);
        }

        student.timerFinished();
        System.out.println("Timer klar");
    }
}
