import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Practice3Answer {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("""
                    Please input the function No:
                    1 - Get even numbers
                    2 - Get odd numbers
                    3 - Get prime numbers
                    4 - Get prime numbers that are bigger than 5
                    0 - Quit
                    """
            );
            int func_no = input.nextInt();
            if (func_no == 0) break;
            System.out.println("Input size of the list:");
            int size = input.nextInt();
            int[] list = new int[size];
            System.out.println("Input elements of the list:");
            for (int i = 0; i < size; i++) list[i] = input.nextInt();
            System.out.println("Filter results:");
            Predicate<Integer> predicate = x -> true;
            switch (func_no) {
                case 1 -> predicate = x -> x % 2 == 0;
                case 2 -> predicate = x -> x % 2 == 1;
                case 3 -> predicate = Practice3Answer::isPrimeNormal;
                case 4 -> predicate = x -> isPrimeNormal(x) && x > 5;
            }
            printResult(list, predicate);
        }
    }

    private static void printResult(int[] nums, Predicate<Integer> predicate) {
        List<Integer> result = new ArrayList<>();
        for (int num : nums) if (predicate.test(num)) result.add(num);
        System.out.println(result);
    }

    private static boolean isPrimeNormal(int num) {
        for (int i = 2; i < num; i++) if (num % i == 0) return false;
        return true;
    }
}