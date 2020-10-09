package rocks.gioac96.veronica.core.concurrency;

import lombok.Getter;

public class PriorityRunnableTask extends PriorityTask implements Runnable {

    @Getter
    private final Runnable task;

    protected PriorityRunnableTask(int priority, Runnable task) {

        super(priority);

        this.task = task;
    }

    @Override
    public void run() {

        task.run();

    }


}
