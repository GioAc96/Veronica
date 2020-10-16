package rocks.gioac96.veronica.core.concurrency;

import static java.lang.Math.atan;
import static java.lang.Math.cbrt;
import static java.lang.Math.tan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PriorityFixedThreadPoolExecutorTest {

    private static final Random random = new Random();

    private static final boolean logging = false;

    private static final int defaultTasksCount = 20;
    private static final int defaultLoadFactor = 2;
    private static final int defaultThreadCount = Runtime.getRuntime().availableProcessors();
    private static final int defaultSleepTime = 500;
    private static final double defaultErrorMarginFactor = 0.1;

    private List<Event> events;

    private static long measureTime(Runnable action) {

        long start = System.currentTimeMillis();

        action.run();

        return System.currentTimeMillis() - start;

    }

    private static int randomPriority() {

        return Math.abs(random.nextInt()) / 1000000;

    }

    private static void log(String message) {

        if (logging) {

            System.out.println(message);

        }

    }

    private static PriorityFixedThreadPoolExecutor ex(int poolSize, int defaultPriority) {

        return PriorityFixedThreadPoolExecutor.builder()
            .poolSize(poolSize)
            .defaultPriority(defaultPriority)
            .provide();

    }

    private static PriorityFixedThreadPoolExecutor ex(int poolSize) {

        return PriorityFixedThreadPoolExecutor.builder()
            .poolSize(poolSize)
            .provide();

    }

    private static PriorityFixedThreadPoolExecutor ex() {

        return PriorityFixedThreadPoolExecutor.builder()
            .poolSize(defaultThreadCount)
            .provide();

    }

    private static void sleep(int duration) {

        try {

            Thread.sleep(duration);

        } catch (InterruptedException e) {

            fail();

        }

    }

    private static void load() {

        load(defaultLoadFactor);

    }

    private static void load(int loadFactor) {

        for (int i = 0; i < 100000 * loadFactor; i++) {

            double d = cbrt(tan(atan(tan(atan(tan(atan(tan(atan(tan(atan(123456789.123456789)))))))))));
            cbrt(d);

        }

    }

    private static void assertSorted(List<Integer> values) {

        if (values == null || values.size() < 2) {

            return;

        }

        Iterator<Integer> valuesIterator = values.iterator();

        Integer previousVal = valuesIterator.next();

        assertNotNull(previousVal);

        while (valuesIterator.hasNext()) {

            Integer val = valuesIterator.next();

            assertNotNull(val);

            assertTrue(val >= previousVal);

            previousVal = val;

        }

    }

    private static void await(PriorityFixedThreadPoolExecutor ex) {

        ex.shutdown();

        try {
            ex.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        } catch (InterruptedException e) {

            fail();

        }

    }

    private static void assertTakes(Runnable action, long expectedDuration) {

        assertTakes(action, expectedDuration, defaultErrorMarginFactor);

    }

    private static void assertTakes(Runnable action, long expectedDuration, double errorMarginFactor) {

        double expectedMaxDuration = expectedDuration * (1 + errorMarginFactor);
        double expectedMinDuration = expectedDuration * (1 - errorMarginFactor);

        long duration = measureTime(action);

        assertTrue(duration <= expectedMaxDuration, "Action took too long (" + duration + ", " + expectedMaxDuration + ")");
        assertTrue(duration >= expectedMinDuration, "Action executed too fast (" + duration + ", " + expectedMinDuration + ")");

    }

    @BeforeEach
    void setUp() {

        events = new LinkedList<>();

    }

    void addEvent(Event event) {

        events.add(event);
        log(event.message);

    }

    void addEventReverse(Event event) {

        events.add(0, event);
        log(event.message);

    }

    private Runnable task(int priority) {

        return () -> {
            addEvent(new JobStarted(priority));
            load();
            addEvent(new JobCompleted(priority));
        };

    }

    private Runnable defaultPriorityTask(int priority) {

        return () -> {
            addEvent(new DefaultPriorityJobStarted(priority));
            load();
            addEvent(new DefaultPriorityJobCompleted(priority));
        };

    }

    private Runnable timedTask(int priority, int duration) {

        return () -> {

            addEvent(new DefaultPriorityJobStarted(priority));
            sleep(duration);
            addEvent(new DefaultPriorityJobCompleted(priority));

        };

    }

    private Runnable timedTask(int priority) {

        return timedTask(priority, defaultSleepTime);

    }

    void assertJobsStartedSortedByPriority() {

        assertSorted(events.stream().filter(event -> event instanceof JobStarted).skip(1).map(
            event -> ((JobStarted) event).priority
        ).collect(Collectors.toList()));

    }

    @Test
    void testExecuteOrderSingleThread() throws InterruptedException {

        PriorityFixedThreadPoolExecutor ex = ex(1, 0);

        for (int i = 0; i < defaultTasksCount; i++) {

            int priority = randomPriority();

            ex.execute(task(priority), priority);

        }

        addEvent(new SchedulingComplete());

        await(ex);

        assertJobsStartedSortedByPriority();

    }

    @Test
    void testExecuteOrderSingleThreadHighDefaultPriority() throws InterruptedException {

        int defaultPriority = Integer.MAX_VALUE;
        PriorityFixedThreadPoolExecutor ex = ex(1, defaultPriority);

        for (int i = 0; i < defaultTasksCount; i++) {

            ex.execute(defaultPriorityTask(defaultPriority));

        }

        for (int i = 0; i < defaultTasksCount; i++) {

            int priority = randomPriority();

            ex.execute(task(priority), priority);

        }

        addEventReverse(new SchedulingComplete());

        await(ex);

        events
            .stream()
            .filter(event -> event instanceof JobStarted)
            .skip(defaultTasksCount + 1)
            .forEach(event -> {
                assertTrue(event instanceof DefaultPriorityJobStarted);
                assertEquals(defaultPriority, ((JobStarted) event).priority);
            });

    }

    @Test
    void testExecuteOrderSingleThreadLowDefaultPriority() throws InterruptedException {

        int defaultPriority = -1;
        PriorityFixedThreadPoolExecutor ex = ex(1, defaultPriority);

        for (int i = 0; i < defaultTasksCount; i++) {

            int priority = randomPriority();

            ex.execute(task(priority), priority);

        }

        for (int i = 0; i < defaultTasksCount; i++) {

            ex.execute(defaultPriorityTask(defaultPriority));

        }

        addEvent(new SchedulingComplete());

        await(ex);

        events
            .stream()
            .filter(event -> event instanceof JobStarted)
            .skip(1)
            .limit(defaultTasksCount)
            .forEach(event -> {
                    assertTrue(event instanceof DefaultPriorityJobStarted);
                    assertEquals(defaultPriority, ((JobStarted) event).priority);
                }
            );

    }

    @Test
    void testExecuteOrderMultipleThreads() throws InterruptedException {

        PriorityFixedThreadPoolExecutor ex = ex();

        for (int i = 0; i < defaultTasksCount; i++) {

            int priority = randomPriority();
            ex.execute(task(priority), priority);

        }

        await(ex);

        List<Integer> startPriorities = events
            .stream()
            .filter(event -> event instanceof JobStarted)
            .skip(defaultThreadCount)
            .map(event -> ((JobStarted) event).priority)
            .collect(Collectors.toList());

        List<Integer> sumOfPrioritiesByThreadGroup = new LinkedList<>();

        for (int i = 0; i < defaultTasksCount / defaultThreadCount; i += defaultThreadCount) {

            int groupPriorityTotal = 0;


            for (int j = 0; j < defaultThreadCount; j++) {

                groupPriorityTotal += startPriorities.get(i + j);

            }

            sumOfPrioritiesByThreadGroup.add(groupPriorityTotal);

        }

        assertSorted(sumOfPrioritiesByThreadGroup);

    }

    @Test
    void testConcurrentExecution() throws Exception {

        PriorityFixedThreadPoolExecutor ex = ex(defaultThreadCount);

        int rounds = 5;

        int nOfTasks = rounds * defaultThreadCount;

        assertTakes(
            () -> {

                for (int i = 0; i < nOfTasks; i++) {

                    int priority = randomPriority();

                    ex.execute(timedTask(priority), priority);

                }

                await(ex);

            },
            rounds * defaultSleepTime
        );

    }

    private static class Event {

        private final String message;

        public Event(String message) {
            this.message = message;
        }
    }

    private static final class DefaultPriorityJobStarted extends JobStarted {

        public DefaultPriorityJobStarted(int priority) {

            super(priority);

        }

    }

    private static final class DefaultPriorityJobCompleted extends JobCompleted {

        public DefaultPriorityJobCompleted(int priority) {

            super(priority);

        }

    }

    private static class SchedulingComplete extends Event {

        public SchedulingComplete() {

            super("Scheduling complete");

        }

    }

    private static class JobCompleted extends Event {

        private final int priority;

        public JobCompleted(int priority) {

            super("Completed job: " + priority);

            this.priority = priority;

        }

    }

    private static class JobStarted extends Event {

        private final int priority;

        public JobStarted(int priority) {

            super("Started job: " + priority);

            this.priority = priority;

        }

    }

}