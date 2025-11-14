/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.core.api.ServiceObjectID;
import com.anaptecs.jeaf.json.impl.ObjectMapperContextResolver;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.json.JsonMapper;

class ContextResolverTest {

  @Test
  void testContextResolver( ) throws JsonProcessingException {
    JsonMapper lDefaultMapper = JsonMapper.builder().build();

    ServiceObjectID lServiceObjectID = new ServiceObjectID("ABC123", 2);

    try {
      lDefaultMapper.writeValueAsString(lServiceObjectID);
      fail("Expecting exception");
    }
    catch (InvalidDefinitionException e) {
      assertEquals(
          "Direct self-reference leading to cycle (through reference chain: com.anaptecs.jeaf.core.api.ServiceObjectID[\"unversionedObjectID\"]->com.anaptecs.jeaf.core.api.ServiceObjectID[\"unversionedObjectID\"])",
          e.getMessage());
    }

    // Test our context resolver implementation
    ObjectMapperContextResolver lResolver = new ObjectMapperContextResolver();
    ObjectMapper lCustomMapper = lResolver.getContext(this.getClass());

    // Test serialization / deserialization of version info
    Date lDate = new Date(1642608165551l);
    VersionInfo lVersionInfo = new VersionInfo("3.1.2", lDate);
    String lJSON = lCustomMapper.writeValueAsString(lVersionInfo);
    assertEquals("{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}", lJSON);

    VersionInfo lReadVersionInfo = lCustomMapper.readValue(lJSON, VersionInfo.class);
    assertEquals(3, lReadVersionInfo.getMajorVersion());
    assertEquals(1, lReadVersionInfo.getMinorVersion());
    assertEquals(2, lReadVersionInfo.getBugfixLevel());
    assertEquals(null, lReadVersionInfo.getHotfixLevel());
    assertEquals(false, lReadVersionInfo.isSnapshotRelease());
    assertEquals(false, lReadVersionInfo.isUnknownVersion());

    // Test SemVer capabilities
    lReadVersionInfo = lCustomMapper.readValue("{\"isUnknownVersion\":true}", VersionInfo.class);
    assertEquals(VersionInfo.UNKNOWN_VERSION, lReadVersionInfo);

    lReadVersionInfo = lCustomMapper
        .readValue("{\"version\":\"3.1.2.47-SNAPSHOT\",\"date\":\"2022-01-19 17:02:45.551\"}", VersionInfo.class);
    assertEquals(3, lReadVersionInfo.getMajorVersion());
    assertEquals(1, lReadVersionInfo.getMinorVersion());
    assertEquals(2, lReadVersionInfo.getBugfixLevel());
    assertEquals(47, lReadVersionInfo.getHotfixLevel());
    assertEquals(true, lReadVersionInfo.isSnapshotRelease());
    assertEquals(false, lReadVersionInfo.isUnknownVersion());

    // Test exception handling
    try {
      lCustomMapper.readValue("{\"version\":\"3.1.2.47-SNAPSHOT\"}", VersionInfo.class);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pCreationDate' must not be null.", e.getMessage());
    }

    try {
      lCustomMapper.readValue("{\"date\":\"2022-01-19 17:02:45.551\"}", VersionInfo.class);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pVersionString' must not be null.", e.getMessage());
    }

    // Try to serialize and deserialize ServiceObjectID
    lJSON = lCustomMapper.writeValueAsString(lServiceObjectID);
    assertEquals("{\"objectID\":\"ABC123\",\"versionLabel\":2}", lJSON);
  }
}
