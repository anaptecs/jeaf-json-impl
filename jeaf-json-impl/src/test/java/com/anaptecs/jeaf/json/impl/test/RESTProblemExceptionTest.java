/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.json.problem.Problem;
import com.anaptecs.jeaf.json.problem.RESTProblemException;

public class RESTProblemExceptionTest {
  @Test
  void testRESTProblemException( ) {
    RESTProblemException lProblemException = new RESTProblemException(400);
    assertTrue(lProblemException.getMessage().endsWith("REST call returned problem JSON (http status code: 400)"));
    Problem lProblem = lProblemException.getProblem();
    assertNotNull(lProblem);
    assertEquals("about:blank", lProblem.getType());
    assertEquals("Bad Request", lProblem.getTitle());
    assertEquals(400, lProblem.getStatus());
    assertEquals(null, lProblem.getDetail());
    assertEquals(null, lProblem.getInstance());

    lProblemException =
        new RESTProblemException(Problem.builder().setType("PROBLEM-123456789").setDetail("Some detail").setStatus(400)
            .setTitle("Yet another Problem!").setInstance("Instance Information").build());
    lProblem = lProblemException.getProblem();
    assertEquals("PROBLEM-123456789", lProblem.getType());
    assertEquals("Some detail", lProblem.getDetail());
    assertEquals(400, lProblem.getStatus());
    assertEquals("Yet another Problem!", lProblem.getTitle());
    assertEquals("Instance Information", lProblem.getInstance());

    String lProblemJSON =
        "{\"type\": \"PROBLEM-123456789\", \"title\": \"Yet another Problem!\", \"status\": 400, \"detail\": \"Some detail\", \"instance\": \"Instance Information\"}";
    ByteArrayInputStream lInputStream = new ByteArrayInputStream(lProblemJSON.getBytes());
    lProblemException = new RESTProblemException(500, lInputStream);
    lProblem = lProblemException.getProblem();
    assertEquals("PROBLEM-123456789", lProblem.getType());
    assertEquals("Some detail", lProblem.getDetail());
    assertEquals(400, lProblem.getStatus());
    assertEquals("Yet another Problem!", lProblem.getTitle());
    assertEquals("Instance Information", lProblem.getInstance());

    // Test exception handling
    lProblemException = new RESTProblemException(444, new ByteArrayInputStream("Hello World!".getBytes()));
    lProblem = lProblemException.getProblem();
    assertEquals("about:blank", lProblem.getType());
    assertEquals(null, lProblem.getDetail());
    assertEquals(444, lProblem.getStatus());
    assertEquals("Unknown HTTP Status", lProblem.getTitle());
    assertEquals(null, lProblem.getInstance());
  }
}
