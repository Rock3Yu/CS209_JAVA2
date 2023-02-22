package practice.lab2;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Practice2 {
    @State(Scope.Thread)
    public static class MyState {
        int[] arrayImpl;
        List<Integer> arraylistImpl;
        List<Integer> linkedListImpl;
        HashMap<Integer, Integer> intMapImpl;

        int LENGTH = 1000;
        int OFFSET = 12010001;

        int index;

        @Setup(Level.Iteration)
        public void setUp() {
            arrayImpl = new int[LENGTH];
            arraylistImpl = new ArrayList<>();
            linkedListImpl = new LinkedList<>();
            intMapImpl = new HashMap<>();

            index = new Random().nextInt(LENGTH) + OFFSET;

            for (int i = OFFSET; i < OFFSET + LENGTH; i++) {
                int age = new Random().nextInt(4) + 18;
                intMapImpl.put(i, age);
                // note: initialize arrayImpl, arraylistImpl and linkedListImpl
                linkedListImpl.add(i - OFFSET, age);
                arraylistImpl.add(i - OFFSET, age);
                arrayImpl[i - OFFSET] = age;
            }

        }

    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testIntMap(MyState state) {
        return state.intMapImpl.get(state.index);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testArrayList(MyState state) {
        // note: return the age by state.index
        return state.arraylistImpl.get(state.index - state.OFFSET);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testLinkedList(MyState state) {
        // note: return the age by state.index
        return state.linkedListImpl.get(state.index - state.OFFSET);
    }


    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testArray(MyState state) {
        // note: return the age by state.index
        return state.arrayImpl[state.index- state.OFFSET];
    }


    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Practice2.class.getSimpleName())
                .measurementIterations(3)
                .warmupIterations(1)
                .mode(Mode.AverageTime)
                .forks(1)
                .shouldDoGC(true)
                .build();
        new Runner(options).run();
    }
}
