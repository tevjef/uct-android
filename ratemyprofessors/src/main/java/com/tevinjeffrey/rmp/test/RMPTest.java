package com.tevinjeffrey.rmp.test;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

/**
 * RMP Tester.
 *
 * @author <Authors name>
 * @since <pre>May 19, 2015</pre>
 * @version 1.0
 */
public class RMPTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     *
     * Method: run(String url)
     *
     */
    @Test
    public void testRun() throws Exception {

    }

    /**
     *
     * Method: main(String[] args)
     *
     */

    @Test
    public void testRateMyProfessor() throws Exception {
        String base = "http://www.ratemyprofessors.com";
        RMP rmp = new RMP();

        String response = rmp.run(base + "/search.jsp?query=GAWISER+rutgers");

        assertNotNull(response);
        assertNotSame(response, "");
    }

    @Test
    public void testProfessorWithoutFirstName() throws Exception {
        RMP rmp = new RMP();

        String lastName = "WANG";

        List<Professor> results = rmp.searchProfessors(lastName);
        rmp.findBestMatch("", lastName, "rutgers", "Marketing", "Newark", results);

        assertEquals(results.get(0).getFullName().getFirst(), "Lei");
    }

    @Test
    public void testProfessorWithFirstName() throws Exception {
        RMP rmp = new RMP();
        String lastName = "WANG";

        List<Professor> results = rmp.searchProfessors(lastName);
        rmp.findBestMatch("L", lastName, "rutgers", "Marketing", "Newark", results);

        assertEquals(results.get(0).getFullName().getFirst(), "Lei");
    }

    @Test
    public void testMain() throws Exception {
        String base = "http://www.ratemyprofessors.com";
        RMP rmp = new RMP();

        String lastName = "GAWISER";

        List<Professor> results = rmp.searchProfessors(lastName);
        rmp.findBestMatch("", lastName, "rutgers", "ASTROPHYSICS", "New Brunswick", results);

        assertEquals(results.get(0).getFullName().getFirst(), "Eric");
    }
} 
