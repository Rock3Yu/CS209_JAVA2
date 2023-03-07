import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Stream {
    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>(Arrays.asList("1", "yky", "ok ok"));
        java.util.stream.Stream<String> stream = stringList.stream();
        java.util.stream.Stream<Double> randoms = java.util.stream.Stream.generate(Math::random);

        stringList.stream().sorted().forEach(System.out::println);
        List<point> points = new ArrayList<>();
        points.add(new point(1, 2));
        points.add(new point(3, 0));
        points.add(new point(2, 1));
        System.out.println(points);
        points.stream()
                .sorted(Comparator.comparing(p -> p.x))
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
