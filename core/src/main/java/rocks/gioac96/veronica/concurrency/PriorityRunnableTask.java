package rocks.gioac96.veronica.concurrency;

import rocks.gioac96.veronica.util.PriorityEntry;

public class PriorityRunnableTask extends PriorityEntry<Runnable> implements Runnable {

    protected PriorityRunnableTask(int priority, Runnable task) {

        super(task, priority);

    }

    @Override
    public void run() {

        getValue().run();

    }


}
