package rocks.gioac96.veronica.core.concurrency;

class ExecutorWithPriorityTest {

    public static void main(String[] args) {

        PriorityFixedThreadPoolExecutor executorService = PriorityFixedThreadPoolExecutor.builder()
            .provide();

        executorService.getExecutorWithPriority(0).execute(() -> System.out.println("Ciao"));

    }

}