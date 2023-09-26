package edu.flightregistration.core.comparators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringComparatorTest {

    private final StringComparator comparator = new StringComparator();

    @Test
    void testTwoNullAreEqual() {
        boolean actual = comparator.stringsAreEqualIgnoringCase(null, null);
        assertTrue(actual, "Nulls are not equal");
    }

    @Test
    void testNullIsNotEqual() {
        boolean actual = comparator.stringsAreEqualIgnoringCase("a", null);
        assertFalse(actual, "One null is still equal");
        actual = comparator.stringsAreEqualIgnoringCase(null, "a");
        assertFalse(actual, "One null is still equal");
    }

    @Test
    void testEqualsIgnoringCase() {
        boolean actual = comparator.stringsAreEqualIgnoringCase("a", "a");
        assertTrue(actual, "Equal strings are not equal");
        actual = comparator.stringsAreEqualIgnoringCase("a", "A");
        assertTrue(actual, "Equal ignoring case strings are not equal");
    }
}