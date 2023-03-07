import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ConsumerSupplierPredicate {
    public static void main(String[] args) {
        // Consumer
        List<String> stringList = new ArrayList<>();
        stringList.add("abc");
        stringList.add("123yky");
        stringList.forEach(System.out::println);
        // Supplier
        Supplier<Character> textSupplier = () -> '|';
        Supplier<Double> randomSupplier = Math::random;
        for (int i = 0; i < 3; i++) System.out.print(textSupplier.get() + ", " + randomSupplier.get());
        // Predicate
        List<String> list = new ArrayList<>(Arrays.asList("This", "is", "the", "Java2", "Class"));
        list.removeIf(e -> e.length() < 3);
        System.out.println("\n" + list);

    }
}
