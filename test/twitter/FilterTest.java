/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testWrittenBySingleMatch() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    @Test
    public void testWrittenByNoMatch() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "unknownUser");
        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    @Test
    public void testWrittenByEmptyList() {
        List<Tweet> writtenBy = Filter.writtenBy(new ArrayList<>(), "alyssa");
        assertTrue("expected empty list", writtenBy.isEmpty());
    }


    @Test
    public void testInTimespanNoMatch() {
        Instant testStart = Instant.parse("2016-02-17T12:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T13:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    @Test
    public void testInTimespanEmptyList() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(new ArrayList<>(), new Timespan(testStart, testEnd));
        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    @Test
    public void testContainingSingleMatch() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweet1 and tweet2", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    @Test
    public void testContainingNoMatch() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("unmatchedWord"));
        assertTrue("expected empty list", containing.isEmpty());
    }

    @Test
    public void testContainingEmptyList() {
        List<Tweet> containing = Filter.containing(new ArrayList<>(), Arrays.asList("word"));
        assertTrue("expected empty list", containing.isEmpty());
    }

    @Test
    public void testContainingCaseInsensitive() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("RIVEST"));
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweet2", containing.contains(tweet2));
    }
}
