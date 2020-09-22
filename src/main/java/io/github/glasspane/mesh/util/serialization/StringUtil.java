package io.github.glasspane.mesh.util.serialization;

public class StringUtil {

    public static String quoteCSV(String input) {
        String result;
        boolean needQuote = input.contains(",");

        if (input.contains("\"")) {
            needQuote = true;
            result = input.replace("\"", "\"\"");
        } else {
            result = input;
        }

        if (needQuote) {
            return "\"" + result + "\"";
        } else {
            return result;
        }
    }
}
