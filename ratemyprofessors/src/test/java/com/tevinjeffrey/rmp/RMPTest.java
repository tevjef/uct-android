package com.tevinjeffrey.rmp;

import com.squareup.okhttp.OkHttpClient;
import com.tevinjeffrey.rmp.professor.Professor;
import com.tevinjeffrey.rmp.search.Decider;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;


public class RMPTest {

    RMP rmp;
    Decider.Parameter params;

    @Before
    public void before() throws Exception {
        rmp = new RMP(new OkHttpClient());
        params = new Decider.Parameter("rutgers", "Writing", "New Brunswick",
                new Professor.Name("A", "Williams"));
    }

    @Test
    public void testRateMyProfessor() throws Exception {
        String base = "http://www.ratemyprofessors.com";

        String response = rmp.get(base + "/search.jsp?query=GAWISER+rutgers");

        assertNotNull(response);
        assertNotSame(response, "");
    }

    @Test
    public void testProfessorWithoutFirstName() throws Exception {
        String expected = "Lei";
        String lastName = "WANG";

        params.name = new Professor.Name("", lastName);
        params.department = "Marketing";
        params.location = "Newark";

        String result = rmp.findBestProfessor(params).toBlocking().first().getFullName().getFirst();

        assertEquals(expected, result);
    }

    @Test
    public void testCloseMatch() throws Exception {
        String expected = "Douglas";
        String lastName = "Morrison";

        params.name = new Professor.Name("", lastName);
        params.department = "Biology";
        params.location = "Newark";

        String result = rmp.findBestProfessor(params).toBlocking().first().getFullName().getFirst();

        assertEquals(expected, result);
    }

    @Test
    public void testProfessorWithFirstName() throws Exception {
        String expected = "Lei";
        String lastName = "WANG";

        params.name = new Professor.Name("L", lastName);
        params.department = "Marketing";
        params.location = "Newark";

        String result = rmp.findBestProfessor(params).toBlocking().first().getFullName().getFirst();

        assertEquals(expected, result);
    }

    @Test
    public void testEmptyUniversity() throws Exception {
        String expected = "Douglas";
        String lastName = "Morrison";

        params.name = new Professor.Name("D", lastName);
        params.department = "Biology";
        params.location = "Newark";
        params.university = "";

        String result = rmp.findBestProfessor(params).toBlocking().first().getFullName().getFirst();

        assertEquals(expected, result);
    }

    @Test
    public void testNJITprofessor() throws Exception {
        String expected = "Larry";
        String lastName = "Lay";

        params.name = new Professor.Name("L", lastName);
        params.department = "Computer Science";
        params.location = "Newark";

        String result = rmp.findBestProfessor(params).toBlocking().first().getFullName().getFirst();

        assertEquals(expected, result);
    }


    @Test
    public void testStaff() throws Exception {
        String expected = "";
        String lastName = "STAFF";

        params.name = new Professor.Name("", lastName);
        params.department = "Computer Science";
        params.location = "Newark";

        String result = rmp.findBestProfessor(params).toBlocking().first().getFullName().getFirst();

        assertEquals(expected, result);
    }

    @Test
    public void testMultipage() throws Exception {
        RMP rmp = new RMP(new OkHttpClient());

        String expected = "Alicia";
        String firstName = rmp.findBestProfessor(new Decider.Parameter("rutgers", "Writing", "New Brunswick",
                new Professor.Name("A", "Williams"))).toBlocking().first().getFullName().getFirst();

        assertEquals(expected, firstName);
    }

    @Test
    public void testNoProfressor() throws Exception {
        RMP rmp = new RMP(new OkHttpClient());

        Professor result = rmp.findBestProfessor(new Decider.Parameter("rutgers", "Writing", "New Brunswick",
                new Professor.Name("A", "NOProfessor"))).toBlocking().firstOrDefault(null);

        assertNull(result);
    }

    @Test
    public void testMain() throws Exception {
        String expected = "Eric";
        String lastName = "GAWISER";

        params.name = new Professor.Name("", lastName);
        params.department = "ASTROPHYSICS";
        params.location = "New Brunswick";

        String result = rmp.findBestProfessor(params).toBlocking().first().getFullName().getFirst();

        assertEquals(expected, result);

    }
} 
