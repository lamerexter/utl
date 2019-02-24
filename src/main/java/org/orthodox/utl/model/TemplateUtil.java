package org.orthodox.utl.model;

public class TemplateUtil {
    public static String dequote(String s) {
        if (s == null)
            return "";
        if ((s.startsWith("\"") && s.endsWith("\"")) ||
                (s.startsWith("'") && s.endsWith("'")))
            return s.substring(1, s.length()-1);
        else
            return s;
    }
}
