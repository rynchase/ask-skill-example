package co.thehotnews.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to work on formatting what gets fed into a response to the ASK system.
 * @author rynchase
 */
public class ResponseUtils {
	
	private static final String URL_PATTERN = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
	private static final String EMPTY_STRING = "";
	
	private static final Pattern PATTERN = Pattern.compile(URL_PATTERN,Pattern.CASE_INSENSITIVE);
	
	public static String removeUrl(String content) {
        Matcher m = PATTERN.matcher(content);
        int i = 0;
        while (m.find()) {
            content = content.replaceAll(m.group(i),EMPTY_STRING).trim();
            i++;
        }
        return content;
    }
}
