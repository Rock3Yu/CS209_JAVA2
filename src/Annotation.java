import java.util.ArrayList;
import java.util.List;

@ClassPreamble(
        author = "Rock",
        date = "2023.4.18"
)
public class Annotation {

    public static void main(String[] args) {
        @Deprecated
        class aboutToDelete {

            List list = new ArrayList();

            @SuppressWarnings("unchecked")
            public void addSth(String sth) {
                list.add(sth);
            }
        }
        aboutToDelete a = new aboutToDelete();


    }


}
