import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class StreamTest {

    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>(Arrays.asList("1", "yky", "ok ok"));
        Stream<String> stringStream = stringList.stream();
        Stream<Double> doubleStream = Stream.generate(Math::random);

        stringStream.sorted().forEach(System.out::println);
        doubleStream.limit(10).forEach(System.out::println);

        List<point> points = new ArrayList<>();
        points.add(new point(1, 2));
        points.add(new point(3, 0));
        points.add(new point(2, 1));
        System.out.println(points);
        points.stream()
                .sorted(Comparator.comparing(p -> p.x))
                .forEach(System.out::print);
        System.out.println();
        points.stream()
                .sorted((p1, p2) -> p1.x.compareTo(p2.x))
                .forEach(System.out::print);


    }

    static class point {

        Integer x;
        Integer y;

        public point(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
