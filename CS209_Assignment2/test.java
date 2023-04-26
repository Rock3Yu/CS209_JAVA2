import com.vdurmont.emoji.EmojiParser;

public class test {

    public static void main(String[] args) {
        String str = ":grinning: :smiley: &#128516; :wink:";
        String result = EmojiParser.parseToUnicode(str);
        System.out.println("转换结果："+result);

    }
}
