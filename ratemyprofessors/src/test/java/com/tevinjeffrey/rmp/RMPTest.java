package com.tevinjeffrey.rmp;

import com.squareup.okhttp.OkHttpClient;
import com.tevinjeffrey.rmp.RMP;
import com.tevinjeffrey.rmp.professor.Professor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;


public class RMPTest {

    RMP rmp;

    @Before
    public void before() throws Exception {
        rmp = new RMP(new OkHttpClient());
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

        String lastName = "WANG";

        List<Professor> results = rmp.searchProfessors(lastName);
        rmp.findBestMatch("", lastName, "rutgers", "Marketing", "Newark", results);

        assertEquals(results.get(0).getFullName().getFirst(), "Lei");
    }

    @Test
    public void testProfessorWithFirstName() throws Exception {
        RMP rmp = new RMP(new OkHttpClient());
        String lastName = "WANG";

        List<Professor> results = rmp.searchProfessors(lastName);
        rmp.findBestMatch("L", lastName, "rutgers", "Marketing", "Newark", results);

        assertEquals(results.get(0).getFullName().getFirst(), "Lei");
    }

    @Test
    public void testMain() throws Exception {
        String base = "http://www.ratemyprofessors.com";

        String lastName = "GAWISER";

        List<Professor> results = rmp.searchProfessors(lastName);
        rmp.findBestMatch("", lastName, "rutgers", "ASTROPHYSICS", "New Brunswick", results);

        assertEquals(results.get(0).getFullName().getFirst(), "Eric");
    }
} 
