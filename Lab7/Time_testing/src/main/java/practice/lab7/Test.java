package practice.lab7;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Test {

    @State(Scope.Thread)
    public static class MyTest {

        @Setup(Level.Iteration)
        public void Setup() {
        }

        @Benchmark
        @OutputTimeUnit(TimeUnit.MILLISECONDS)
        public static void testLock() {
            AccountLock account = new AccountLock();
            ExecutorService service = Executors.newFixedThreadPool(100);
            for (int i = 1; i <= 100; i++) {
                service.execute(new DepositThread(account, 10));
            }
            service.shutdown();
            while (!service.isTerminated()) {
            }
            System.out.println("Balance: " + account.getBalance());
        }

        @Benchmark
        @OutputTimeUnit(TimeUnit.MILLISECONDS)
        public static void testSync() {
            AccountSync account = new AccountSync();
            ExecutorService service = Executors.newFixedThreadPool(100);
            for (int i = 1; i <= 100; i++) {
                service.execute(new DepositThread(account, 10));
            }
            service.shutdown();
            while (!service.isTerminated()) {
            }
            System.out.println("Balance: " + account.getBalance());
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Test.class.getSimpleName())
                .measurementIterations(3)
                .warmupIterations(1)
                .mode(Mode.AverageTime)
                .forks(1)
                .shouldDoGC(true)
                .build();
        new Runner(opt).run();
    }
}
