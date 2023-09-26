package edu.flightregistration.core.comparators;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

@Component
public class StringComparator {

    public boolean stringsAreEqualIgnoringCase(String string1, String string2) {
        if (Strings.isNullOrEmpty(string1)) {
            string1 = "";
        }
        if (Strings.isNullOrEmpty(string2)) {
            string2 = "";
        }
        return string1.trim().equalsIgnoreCase(string2.trim());
    }
}
