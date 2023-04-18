import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Reflection {

    public static void main(String[] args)
            throws Exception {
        // Class class
        Class c1 = String.class;
//        c1 = Class.forName("java.lang.String");
//        c1 = "hello".getClass();
        System.out.println(c1.getName());

        c1 = String[][].class;
        System.out.println(c1.getName());

        // Filed class
        System.out.println();
        Field f = String.class.getDeclaredField("value");
        System.out.println(f.getName());
        System.out.println(f.getType());

        int m = f.getModifiers();
        System.out.println(Modifier.isFinal(m));
        System.out.println(Modifier.isPrivate(m));

        // hack the final field
        Student stu = new Student();
        System.out.println(stu.getID());
        Class clz = stu.getClass();
        Field f1 = clz.getDeclaredField("ID");
        f1.setAccessible(true);
        f1.set(stu, 12019999);
        System.out.println(stu.getID());

        // Method class
        System.out.println();
        String s = "Hello world";
        Method m1 = String.class.getMethod("substring", int.class);
        System.out.println((String) m1.invoke(s, 2));

        // create an instance by reflect
        System.out.println();
        c1 = Student.class;
        Constructor constructor = c1.getConstructor(int.class, String.class);
        stu = (Student) constructor.newInstance(100, "NB");
        System.out.println(stu.getID());
    }

    static class Student {
        final String name = "Rock";
        private final int ID;
        public String nickName;

        public Student(){
            this.ID = 12013027;
            this.nickName = "Kun";
        }

        public Student(int ID, String nickName) {
            this.ID = ID;
            this.nickName = nickName;
        }

        public String getName() {
            return name;
        }

        public int getID() {
            return ID;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }

}
