package se.abalon.cache.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import se.abalon.cache.exception.FormatException;
import se.abalon.cache.resource.ResourceKeys;

/**
 * Static helperfunctions.
 * @author Fredrik Hed [f.hed@abalon.se]
 */
public class StringUtil  {
    public static Category log = Category.getInstance(StringUtil.class);

    /**
     * Static method to find corresponding bracket '[' -> ']' in a array for String to List conversion.
     * @see #stringToList
     * @param s leading bracket
     * @param e closing bracket
     * @param s string containing the array as a string
     */
    public static int findCorespondingBracket(char separator, char s, char e, String array) throws FormatException {
        char [] c = array.toCharArray();
        int nrOfStart = 0;
        int nrOfEnd = 0;
        int toReturn = 0;

        if (c[0] != s) {
            throw new FormatException(ResourceKeys.CONVERSION_ERROR_EXPECTED_CHAR_BUT_FOUND, new Object[] {"" + s, "" + c[0]});
        }
        nrOfStart++;

        boolean found = false;
        for (int i = 1; i < c.length; i++) {
            if (c[i] == s) {
                if ((c[i-1] == s) || (c[i-1] == separator))
                    nrOfStart++;
//					log.debug("found [ " + nrOfStart);
            }

            if (c[i] == e) {
                if (i+2 > c.length) {
                    nrOfEnd++;
//					log.debug("found the very end ] " + nrOfEnd);
                }
                else if ((c[i+1] == e) || (c[i+1] == separator)) {
                    nrOfEnd++;
//					log.debug("found ] " + nrOfEnd);
                }
            }

            if ((nrOfEnd == nrOfStart) && !found) {
                toReturn = i;
                found = true;
            }
        }

        if (nrOfEnd == nrOfStart) {
            return toReturn;
        }
        throw new FormatException(ResourceKeys.CONVERSION_ERROR_EXPECTED_CHAR_BUT_FOUND, new Object[] {"" + e, "" + c[c.length-1]});
    }


    /**
     * Convinience method to convert a String to a List.
     * If the String is in format "[a,b,[c,d]]" or just "a" the string is converted to a List
     * @param separator the character to use as a element separator
     * @param list string to be parsed as an List
     */
    public static List stringToList(char separator, String list) throws FormatException {
        return stringToList(separator, '[',']', list);
    }

    /**
     * Convinience method to convert a String to a List.
     * If the String is in format "[a,b,[c,d]]" or just "a" the string is converted to a List
     * @param list string to be parsed as an List
     */
    public static List stringToList(String list) throws FormatException {
        return stringToList(',', '[',']', list);
    }

    /**
     * Convinience method to convert a String to a Map.
     * If the String is in format "{a=b,c=d}" the string is converted to a Map
     * @param list string to be parsed as an Map
     */
    public static Map<String, String> stringToMap(String list) throws FormatException {
        return stringToMap(',', list);
    }

    /**
     * Convinience method to convert a String to a Map.
     * If the String is in format "{a=b,c=d}" the string is converted to a Map
     * @param separator the character to use as a element separator
     * @param list string to be parsed as an Map
     *
     */
    public static Map<String,String> stringToMap(char separator, String list) throws FormatException {
        Map<String, String> map = new HashMap<String, String>();
        Iterator iterator = stringToList(separator, '{','}', list).iterator();
        String keyValue;
        while (iterator.hasNext()) {
            keyValue = (String)iterator.next();

            int equalsPos = keyValue.indexOf('=');
            if (equalsPos == -1) {
                throw new FormatException(ResourceKeys.CONVERSION_ERROR_EXPECTED_KEY_AND_VALUE, new Object [] {"keyValue"});
            }

            String key = keyValue.substring(0, equalsPos).trim();
            String value = "";
            if (keyValue.length() >= equalsPos) {
                value = keyValue.substring(equalsPos + 1, keyValue.length()).trim();
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * Helper to convert arbitrary array definied as string. Example [a,b,[c,d,[e]],f].
     * The character to use to define a start/end of the structore is pased as parameters
     * @param separator the character to use as a element separator
     * @param s leading bracket
     * @param e closing bracket
     * @param s string containing the array as a string
     */
    public static List<String> stringToList(char separator, char startChar, char endChar, String list) throws FormatException {
        List array = new ArrayList();
        String s = "" + startChar;
        String theElements = null;
        int end = findCorespondingBracket(separator, startChar, endChar, list);
        int start = list.indexOf(s);
        if (start == -1) {
            throw new FormatException(ResourceKeys.CONVERSION_ERROR_EXPECTED_CHAR_BUT_FOUND, new Object[] {"" + startChar, "" + list.substring(0,1)});
        }
        theElements = list.substring(start+1, end);

        // while sting not end
        boolean ready = false;
        end = 0;
        while (!ready) {
            // identift next sting coresponding to element
            // starts with [?
            if (theElements.startsWith(s)) {
                end = findCorespondingBracket(separator, startChar, endChar, theElements); // throws FormatException
                array.add(stringToList(separator, theElements.substring(0,end+1)));
                if (end+2 < theElements.length()) {
                    theElements = theElements.substring(end+2, theElements.length());
                }
                else {
                    return array;
                }
            }
            // string... just add to list
            else {
                int nextComma = theElements.indexOf(separator);

                // no comma found
                if (nextComma == -1) {
                    if (!theElements.equals("")) {
                        array.add(theElements.trim());
                    }
                    return array;
                }
                array.add(theElements.substring(0, nextComma).trim());
                theElements = theElements.substring(nextComma + 1 , theElements.length());
            }
        }
        return array;
    }

    /**
     * Convenience method to concatenate the elements of an collection to a
     * string with the deliminater separating each element. Compare to 'join' in Perl.
     *
     * @param set the collection to concatenate
     * @param delimiter the deliminator to use
     * @return the concatenated string
     */
    public static String join(Collection set, String delimiter) {
        Object oSet[] = set.toArray();
        return join(oSet, delimiter);
    }

    /**
     * Convenience method to concatenate the elements of an collection to a
     * string with the deliminater separating each element. Compare to 'join' in Perl.
     *
     * @param set the collection to concatenate
     * @param delimiter the deliminator to use
     * @return the concatenated string
     */
    public static String join(Object [] oSet, String delimiter) {
        StringBuffer sb = new StringBuffer();

        if (oSet.length > 0) {
            sb.append(oSet[0].toString());

            for (int i = 1; i < oSet.length; i++) {
                sb.append(delimiter);
                sb.append(oSet[i].toString());
            }
        }
        return sb.toString();
    }

    public static String SafeSubstractStringFromPos(String str, int pos) {
        if (str.length() > pos-1) {
            str = str.substring(0,pos);
        }

        return str;
    }

    public static boolean isEmpty(String value) {
        return (value == null || value.length() == 0);
    }

}