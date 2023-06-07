import java.lang.reflect.Method;

public class BeforeFinal {

    public static void main(String[] args) throws Exception {
        // 3.2
        Class<?> clazz = Class.forName("java.lang.String");
        Method m = clazz.getMethod("indexOf", String.class, int.class);
        String str = "This is a sentence in the program.";
        System.out.println(m.invoke(str, "i", 3));
    }

}
