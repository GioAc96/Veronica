package rocks.gioac96.veronica.core.concurrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

public class Test {

    private static void sleep() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        System.out.println("Scheduling");

        ExecutorService executorService = PriorityFixedThreadPoolExecutor.builder()
            .poolSize(1)
            .build();


        for(int i = 0; i < 40; i++) {

            int priority = Math.abs(new Random().nextInt()) / 10000000;

            Future<?> taskResult = executorService.submit(new PriorityRunnableTask(priority, () -> {

                sleep();
                System.out.println(priority);

            }));

        }

        for(int i = 0; i < 10; i++) {

            Future<?> taskResult = executorService.submit(() -> {

                sleep();
                System.out.println("default priority");

            });

        }

        System.out.println("Scheduled");

    }

}
