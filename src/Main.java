import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class Main {

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static void main(String[] args) {
        System.out.println("Hello world!");

        List<String> strList = new ArrayList<>();
        strList.add(null);
        strList.add("ss");
        strList.add("");
        strList.add(null);
        System.out.println(strList);

        Set<Integer> intSet = new HashSet<>();
        intSet.add(1);
        intSet.add(null);
        System.out.println(intSet);

        Map<Integer, String> map = new HashMap<>();
        map.put(12013027, "YU Kunyi");
        map.put(33, "Rock");
        System.out.println(map.keySet());
        System.out.println(map.entrySet());

        System.out.println(strList.hashCode());

        System.out.println("----------------");
        StringConverter stringConverter = new Main().new StringConverter();
        Deserializer des = StringConverter::convertToInt;
        System.out.println(120 == des.deserialize("120"));




    }

    public interface Deserializer {
        public int deserialize(String v1);
    }

    public class StringConverter {
        public static int convertToInt(String v1) {
            return Integer.valueOf(v1);
        }
    }
}