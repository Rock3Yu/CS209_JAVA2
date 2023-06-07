import java.lang.reflect.Method;

public class BeforeFinal {

    public static void main(String[] args) throws Exception {
        // 3.2
        Class<?> clazz = Class.forName("java.lang.String");
        Method m = clazz.getMethod("indexOf", String.class, int.class);
        String str = "This is a sentence in the program.";
        System.out.println(m.invoke(str, "i", 3));
        // quiz 1
        interface A {

            int aMethod(String s);
        }
//        A a = a -> a.length();
//        A x = y -> {return y;};
        A b = (String s) -> 1;
//        A s = "2" -> Integer.parseInt(s);
        System.out.println(b.aMethod("hello"));
    }

}
