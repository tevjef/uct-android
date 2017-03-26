package com.tevinjeffrey.rmp.common;

import dagger.ObjectGraph;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        "Mathematics", "Newark", "101", "", "Randall"
    );

    Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

    assertEquals(expected, result.getFirstName());
  }

  @Test
  public void testProfessorWithFirstName() throws Exception {
    String expected = "John";

    Parameter params = new Parameter("rutgers",
        "Mathematics", "Newark", "101", "J", "Randall"
    );

    Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

    assertEquals(expected, result.getFirstName());
  }

  @Test
  public void testEmptyUniversity() throws Exception {
    String expected = "John";

    Parameter params = new Parameter("",
        "Mathematics", "Newark", "101", "J", "Randall"
    );

    Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

    assertEquals(expected, result.getFirstName());
  }

  @Test
  public void testNJITprofessor() throws Exception {
    String expected = "Lay";

    Parameter params = new Parameter("rutgers",
        "Computer Science", "Newark", "101", "L", "Lay"
    );

    Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

    assertEquals(expected, result.getLastName());
  }

  @Test
  public void testStaff() throws Exception {
    Parameter params = new Parameter("rutgers",
        "Computer Science", "Newark", "101", "", "STAFF"
    );

    Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

    assertNull(result);
  }

  @Test
  public void testNoProfessor() throws Exception {
    Parameter params = new Parameter("rutgers",
        "Computer Science", "Newark", "101", "", "NOPROFESSOR"
    );

    Professor result = rmp.getProfessor(params).toBlocking().lastOrDefault(null);

    assertNull(result);
  }
}