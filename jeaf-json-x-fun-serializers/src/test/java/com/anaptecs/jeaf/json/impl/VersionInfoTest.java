/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;

import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

class VersionInfoTest {

  @Test
  void testVersionInfoSerialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();

    // Test serialization / deserialization of version info
    Date lDate = new Date(1642608165551l);
    VersionInfo lVersionInfo = new VersionInfo("3.1.2", lDate);
    String lJSON = lObjectMapper.writeValueAsString(lVersionInfo);
    assertEquals("{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}", lJSON);

    VersionInfo lReadVersionInfo = lObjectMapper.readValue(lJSON, VersionInfo.class);
    assertEquals(3, lReadVersionInfo.getMajorVersion());
    assertEquals(1, lReadVersionInfo.getMinorVersion());
    assertEquals(2, lReadVersionInfo.getBugfixLevel());
    assertEquals(null, lReadVersionInfo.getHotfixLevel());
    assertEquals(false, lReadVersionInfo.isSnapshotRelease());
    assertEquals(false, lReadVersionInfo.isUnknownVersion());

    // Test SemVer capabilities
    lReadVersionInfo = lObjectMapper.readValue("{\"isUnknownVersion\":true}", VersionInfo.class);
    assertEquals(VersionInfo.UNKNOWN_VERSION, lReadVersionInfo);

    lReadVersionInfo = lObjectMapper
        .readValue("{\"version\":\"3.1.2.47-SNAPSHOT\",\"date\":\"2022-01-19 17:02:45.551\"}", VersionInfo.class);
    assertEquals(3, lReadVersionInfo.getMajorVersion());
    assertEquals(1, lReadVersionInfo.getMinorVersion());
    assertEquals(2, lReadVersionInfo.getBugfixLevel());
    assertEquals(47, lReadVersionInfo.getHotfixLevel());
    assertEquals(true, lReadVersionInfo.isSnapshotRelease());
    assertEquals(false, lReadVersionInfo.isUnknownVersion());

    lReadVersionInfo = lObjectMapper.readValue(
        "{\"version\":\"3.1.2.47-SNAPSHOT\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":null, \"unknown\":\"some value\"}",
        VersionInfo.class);
    assertEquals(3, lReadVersionInfo.getMajorVersion());
    assertEquals(1, lReadVersionInfo.getMinorVersion());
    assertEquals(2, lReadVersionInfo.getBugfixLevel());
    assertEquals(47, lReadVersionInfo.getHotfixLevel());
    assertEquals(true, lReadVersionInfo.isSnapshotRelease());
    assertEquals(false, lReadVersionInfo.isUnknownVersion());

    // Test exception handling
    try {
      lObjectMapper.readValue("{\"version\":\"3.1.2.47-SNAPSHOT\"}", VersionInfo.class);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pCreationDate' must not be null.", e.getMessage());
    }

    // Test exception handling
    try {
      lObjectMapper.readValue("{\"version\":\"3.1.2.47-SNAPSHOT\",\"date\":null}", VersionInfo.class);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pCreationDate' must not be null.", e.getMessage());
    }

    try {
      lObjectMapper.readValue("{\"date\":\"2022-01-19 17:02:45.551\"}", VersionInfo.class);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pVersionString' must not be null.", e.getMessage());
    }

    try {
      lObjectMapper.readValue("{\"version\":null,\"date\":\"2022-01-19 17:02:45.551\"}", VersionInfo.class);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pVersionString' must not be null.", e.getMessage());
    }
  }
}
