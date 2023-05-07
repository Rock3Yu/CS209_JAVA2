import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<Date> dateList = new ArrayList<>();
        dateList.add(new Date(250181935403L));
        dateList.add(new Date(21930403L));
        dateList.add(new Date(100181935403L));
        System.out.println(dateList);
        dateList = dateList.stream().sorted().toList();
        System.out.println(dateList);
        int a = 3;
        double b = 1.29;
        System.out.println(a - b);


    }

}
