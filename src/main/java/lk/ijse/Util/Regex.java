package lk.ijse.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Regex {
    public static boolean isValidClientId(String staffId){
        Pattern pattern=Pattern.compile("^[A-z|\\s]{3,}$");
        Matcher matcher=pattern.matcher(staffId);
        return matcher.matches();
    }
}
