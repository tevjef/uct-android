package com.tevinjeffrey.rmp.common;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import dagger.ObjectGraph;

import static org.junit.Assert.*;

public class RMPTest {

    @Inject
    RMP rmp;

    @Before
    public void setUp() throws Exception {
        ObjectGraph.create(new RMPTestModule()).inject(this);
    }

    @Test
    public void testProfessorWithoutFirstName() throws Exception {
        String expected = "John";

        Parameter params = new Parameter("rutgers",
                "Mathematics", "Newark", "101", "", "Randall");

        Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

        assertEquals(expected, result.getFirstName());
    }

    @Test
    public void testProfessorWithFirstName() throws Exception {
        String expected = "John";

        Parameter params = new Parameter("rutgers",
                "Mathematics", "Newark", "101", "J", "Randall");

        Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

        assertEquals(expected, result.getFirstName());
    }

    @Test
    public void testEmptyUniversity() throws Exception {
        String expected = "John";

        Parameter params = new Parameter("",
                "Mathematics", "Newark", "101", "J", "Randall");

        Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

        assertEquals(expected, result.getFirstName());
    }

    @Test
    public void testNJITprofessor() throws Exception {
        String expected = "Lay";

        Parameter params = new Parameter("rutgers",
                "Computer Science", "Newark", "101", "L", "Lay");

        Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

        assertEquals(expected, result.getLastName());
    }

    @Test
    public void testStaff() throws Exception {
       Parameter params = new Parameter("rutgers",
                "Computer Science", "Newark", "101", "", "STAFF");

        Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

        assertNull(result);
    }

    @Test
    public void testNoProfessor() throws Exception {
        Parameter params = new Parameter("rutgers",
                "Computer Science", "Newark", "101", "", "NOPROFESSOR");

        Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

        assertNull(result);
    }
}