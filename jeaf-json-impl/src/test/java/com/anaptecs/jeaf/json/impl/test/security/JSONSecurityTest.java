/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.json.api.JSONTools;

public class JSONSecurityTest {
  @Test
  void testDeseriliaztionSecurity( ) {
    JSONTools lTools = JSONTools.getJSONTools();

    POJO lPojo = new POJO();
    lPojo.setName("Hello Jackson");
    lPojo.getAttackedObject().setName("Attack me");

    String lJSON = lTools.writeObjectToString(lPojo);
    assertEquals("{\"name\":\"Hello Jackson\",\"attackedObject\":{\"name\":\"Attack me\"}}", lJSON);

    POJO lReadObject = lTools.read("{\"name\":\"Hello Jackson\",\"attackedObject.name\":\"Attacked\"}}", POJO.class);
    assertEquals("Hello Jackson", lReadObject.getName());
    assertEquals(null, lReadObject.getAttackedObject().getName());
  }
}
