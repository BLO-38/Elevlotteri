package offlineHandling;

public class OnlineTimer extends Thread {
    private final int TIME = 25;
    private StudentPanel student;
    private boolean stillOnline = true;

    public OnlineTimer(StudentPanel st) {
        student = st;
        start();
    }

    public void setStillOnline() {
        stillOnline = true;
    }

    @Override
    public void run() {
        for (int i = 0; i < 6; i++) {
            student.tick(stillOnline);
            if(stillOnline) {
                i = 0;
                stillOnline = false;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        student.timerFinished();
        System.out.println("Timer klar");
    }
}
