/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.anaptecs.jeaf.json.api.JSONMessages;
import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.checks.InvalidParameterException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.info.ApplicationProvider;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import org.junit.jupiter.api.Test;

class ApplicationInfoTest {
  @Test
  void testApplicationProviderDeserialization( ) {
    String lJSON = "{\"creator\":\"anaptecs GmbH\",\"creatorURL\":\"https://www.anaptecs.de\"}";

    JSONTools lTools = JSONTools.getJSONTools();
    ApplicationProvider lApplicationProvider = lTools.read(lJSON, ApplicationProvider.class);
    assertEquals("anaptecs GmbH", lApplicationProvider.getCreator());
    assertEquals("https://www.anaptecs.de", lApplicationProvider.getCreatorURL());

    // Test SemVer behavior
    lJSON = "{\"creator\":null,\"creatorURL\":\"https://www.anaptecs.de\"}";
    lApplicationProvider = lTools.read(lJSON, ApplicationProvider.class);
    assertEquals(null, lApplicationProvider.getCreator());
    assertEquals("https://www.anaptecs.de", lApplicationProvider.getCreatorURL());

    lJSON = "{\"creatorURL\":\"https://www.anaptecs.de\"}";
    lApplicationProvider = lTools.read(lJSON, ApplicationProvider.class);
    assertEquals(null, lApplicationProvider.getCreator());
    assertEquals("https://www.anaptecs.de", lApplicationProvider.getCreatorURL());

    lJSON = "{\"creator\":\"anaptecs GmbH\",\"creatorURL\":null}";
    lApplicationProvider = lTools.read(lJSON, ApplicationProvider.class);
    assertEquals("anaptecs GmbH", lApplicationProvider.getCreator());
    assertEquals(null, lApplicationProvider.getCreatorURL());

    lJSON = "{\"creator\":\"anaptecs GmbH\", \"unknown\":\"some value\"}";
    lApplicationProvider = lTools.read(lJSON, ApplicationProvider.class);
    assertEquals("anaptecs GmbH", lApplicationProvider.getCreator());
    assertEquals(null, lApplicationProvider.getCreatorURL());

    lApplicationProvider = lTools.read("{}", ApplicationProvider.class);
    assertEquals(ApplicationProvider.UNKNOW_APP_PROVIDER, lApplicationProvider);

    try {
      lApplicationProvider = lTools.read("", ApplicationProvider.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }
    assertEquals(ApplicationProvider.UNKNOW_APP_PROVIDER, lApplicationProvider);

    try {
      lApplicationProvider = lTools.read((String) null, ApplicationProvider.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pString must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testApplicationInfoSerialization( ) {
    VersionInfo lVersionInfo =
        new VersionInfo("1.7.77.5", DateTools.getDateTools().toDate("2021-07-25 11:21:16", false));
    ApplicationProvider lProvider = new ApplicationProvider("anaptecs GmbH", "https://www.anaptecs.de");
    ApplicationInfo lApplicationInfo = new ApplicationInfo("JSON_TEST", "JEAF JSON Test App",
        "https://www.anaptecs.de/jeaf", "Test App for JSON Serialization support by JEAF", lProvider, lVersionInfo);

    // Try to serialize application info.
    JSONTools lTools = JSONTools.getJSONTools();
    String lJSON = lTools.writeObjectToString(lApplicationInfo);
    String lExpected =
        "{\"applicationID\":\"JSON_TEST\",\"name\":\"JEAF JSON Test App\",\"websiteURL\":\"https://www.anaptecs.de/jeaf\",\"description\":\"Test App for JSON Serialization support by JEAF\",\"applicationProvider\":{\"creator\":\"anaptecs GmbH\",\"creatorURL\":\"https://www.anaptecs.de\"},\"version\":{\"version\":\"1.7.77.5\",\"date\":\"2021-07-25 11:21:16.000\",\"isUnknownVersion\":false},\"isUnknownApplication\":false}";
    assertEquals(lExpected, lJSON);

    // Serialize unknown application
    lJSON = lTools.writeObjectToString(ApplicationInfo.UNKNOWN_APPLICATION);
    XFun.getTrace().info(lJSON);
    assertEquals(true, lJSON.startsWith(
        "{\"applicationID\":\"NO_APP_ID\",\"name\":\"Unknow Application\",\"applicationProvider\":{\"creator\":\"Unkown Provider\"},\"version\":{\"version\":\"0.0-SNAPSHOT\",\"date\":\""));
    assertEquals(true, lJSON.endsWith("\"isUnknownVersion\":true},\"isUnknownApplication\":true}"));

    // Test handling of incomplete objects.

  }

  @Test
  void testApplicationInfoDeserialization( ) {
    String lJSON =
        "{\"applicationID\":\"JEAF_JSON_JUNIT\",\"name\":\"JEAF JSON JUnit Tests\",\"websiteURL\":\"https://www.anaptecs.de/jeaf\",\"description\":\"JUnit tests for JEAF JSON implementation\",\"applicationProvider\":{\"creator\":\"anaptecs GmbH\",\"creatorURL\":\"https://www.anaptecs.de\"},\"version\":{\"version\":\"1.6.0-SNAPSHOT\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false},\"isUnknownApplication\":false}";

    JSONTools lTools = JSONTools.getJSONTools();

    // Test application info itself
    ApplicationInfo lApplicationInfo = lTools.read(lJSON, ApplicationInfo.class);
    assertEquals("JEAF_JSON_JUNIT", lApplicationInfo.getApplicationID());
    assertEquals("JEAF JSON JUnit Tests", lApplicationInfo.getName());
    assertEquals("https://www.anaptecs.de/jeaf", lApplicationInfo.getWebsiteURL());
    assertEquals("JUnit tests for JEAF JSON implementation", lApplicationInfo.getDescription());
    assertEquals(false, lApplicationInfo.isUnknownApplication());

    // Test connected application provider
    ApplicationProvider lApplicationProvider = lApplicationInfo.getApplicationProvider();
    assertEquals("anaptecs GmbH", lApplicationProvider.getCreator());
    assertEquals("https://www.anaptecs.de", lApplicationProvider.getCreatorURL());

    // Test connected version info.
    VersionInfo lVersion = lApplicationInfo.getVersion();
    assertEquals("1.6.0-SNAPSHOT", lVersion.getVersionString());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersion.getCreationDate());

    // Deserialize UNKNOWN_APPLICATION
    lJSON =
        "{\"applicationID\":\"NO_APP_ID\",\"name\":\"Unknow Application\",\"websiteURL\":null,\"description\":null,\"applicationProvider\":{\"creator\":\"Unkown Provider\",\"creatorURL\":null},\"version\":{\"version\":\"0.0-SNAPSHOT\",\"date\":\"2022-01-24 09:08:56.657\",\"isUnknownVersion\":true},\"isUnknownApplication\":true}";
    lApplicationInfo = lTools.read(lJSON, ApplicationInfo.class);
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION, lApplicationInfo);
    assertEquals(ApplicationProvider.UNKNOW_APP_PROVIDER, lApplicationInfo.getApplicationProvider());
    assertEquals(VersionInfo.UNKNOWN_VERSION, lApplicationInfo.getVersion());

    // Test handling of incomplete JSON.
    lJSON =
        "{\"applicationID\":\"JEAF_JSON_JUNIT\",\"name\":\"JEAF JSON JUnit Tests\",\"websiteURL\":\"https://www.anaptecs.de/jeaf\",\"description\":\"JUnit tests for JEAF JSON implementation\",\"applicationProvider\":{\"creator\":\"anaptecs GmbH\",\"creatorURL\":\"https://www.anaptecs.de\"},\"version\":{\"version\":\"1.6.0-SNAPSHOT\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false},\"isUnknownApplication\":null}";

    // Test application info itself
    lApplicationInfo = lTools.read(lJSON, ApplicationInfo.class);
    assertEquals("JEAF_JSON_JUNIT", lApplicationInfo.getApplicationID());
    assertEquals("JEAF JSON JUnit Tests", lApplicationInfo.getName());
    assertEquals("https://www.anaptecs.de/jeaf", lApplicationInfo.getWebsiteURL());
    assertEquals("JUnit tests for JEAF JSON implementation", lApplicationInfo.getDescription());
    assertEquals(false, lApplicationInfo.isUnknownApplication());

    // Test connected application provider
    lApplicationProvider = lApplicationInfo.getApplicationProvider();
    assertEquals("anaptecs GmbH", lApplicationProvider.getCreator());
    assertEquals("https://www.anaptecs.de", lApplicationProvider.getCreatorURL());

    // Test connected version info.
    lVersion = lApplicationInfo.getVersion();
    assertEquals("1.6.0-SNAPSHOT", lVersion.getVersionString());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersion.getCreationDate());

    lJSON =
        "{\"applicationID\":\"JEAF_JSON_JUNIT\",\"name\":\"JEAF JSON JUnit Tests\",\"websiteURL\":\"https://www.anaptecs.de/jeaf\",\"description\":\"JUnit tests for JEAF JSON implementation\",\"applicationProvider\":{\"creator\":\"anaptecs GmbH\",\"creatorURL\":\"https://www.anaptecs.de\"},\"version\":{\"version\":\"1.6.0-SNAPSHOT\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}}";

    // Test application info itself
    lApplicationInfo = lTools.read(lJSON, ApplicationInfo.class);
    assertEquals("JEAF_JSON_JUNIT", lApplicationInfo.getApplicationID());
    assertEquals("JEAF JSON JUnit Tests", lApplicationInfo.getName());
    assertEquals("https://www.anaptecs.de/jeaf", lApplicationInfo.getWebsiteURL());
    assertEquals("JUnit tests for JEAF JSON implementation", lApplicationInfo.getDescription());
    assertEquals(false, lApplicationInfo.isUnknownApplication());

    // Test connected application provider
    lApplicationProvider = lApplicationInfo.getApplicationProvider();
    assertEquals("anaptecs GmbH", lApplicationProvider.getCreator());
    assertEquals("https://www.anaptecs.de", lApplicationProvider.getCreatorURL());

    // Test connected version info.
    lVersion = lApplicationInfo.getVersion();
    assertEquals("1.6.0-SNAPSHOT", lVersion.getVersionString());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersion.getCreationDate());

    lJSON =
        "{\"applicationID\":\"JEAF_JSON_JUNIT\",\"name\":null,\"websiteURL\":null,\"description\":null,\"applicationProvider\":{\"creator\":\"anaptecs GmbH\",\"creatorURL\":\"https://www.anaptecs.de\"},\"version\":{\"version\":\"1.6.0-SNAPSHOT\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false},\"isUnknownApplication\":false}";

    // Test application info itself
    lApplicationInfo = lTools.read(lJSON, ApplicationInfo.class);
    assertEquals("JEAF_JSON_JUNIT", lApplicationInfo.getApplicationID());
    assertEquals(null, lApplicationInfo.getName());
    assertEquals(null, lApplicationInfo.getWebsiteURL());
    assertEquals(null, lApplicationInfo.getDescription());
    assertEquals(false, lApplicationInfo.isUnknownApplication());

    // Test connected application provider
    lApplicationProvider = lApplicationInfo.getApplicationProvider();
    assertEquals("anaptecs GmbH", lApplicationProvider.getCreator());
    assertEquals("https://www.anaptecs.de", lApplicationProvider.getCreatorURL());

    // Test connected version info.
    lVersion = lApplicationInfo.getVersion();
    assertEquals("1.6.0-SNAPSHOT", lVersion.getVersionString());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersion.getCreationDate());

    lJSON =
        "{\"applicationID\":\"JEAF_JSON_JUNIT\",\"applicationProvider\":{\"creator\":\"anaptecs GmbH\",\"creatorURL\":\"https://www.anaptecs.de\"},\"version\":{\"version\":\"1.6.0-SNAPSHOT\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false},\"isUnknownApplication\":false}";

    // Test application info itself
    lApplicationInfo = lTools.read(lJSON, ApplicationInfo.class);
    assertEquals("JEAF_JSON_JUNIT", lApplicationInfo.getApplicationID());
    assertEquals(null, lApplicationInfo.getName());
    assertEquals(null, lApplicationInfo.getWebsiteURL());
    assertEquals(null, lApplicationInfo.getDescription());
    assertEquals(false, lApplicationInfo.isUnknownApplication());

    // Test connected application provider
    lApplicationProvider = lApplicationInfo.getApplicationProvider();
    assertEquals("anaptecs GmbH", lApplicationProvider.getCreator());
    assertEquals("https://www.anaptecs.de", lApplicationProvider.getCreatorURL());

    // Test connected version info.
    lVersion = lApplicationInfo.getVersion();
    assertEquals("1.6.0-SNAPSHOT", lVersion.getVersionString());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersion.getCreationDate());

    // Test missing provider and version info
    lJSON =
        "{\"applicationID\":\"JEAF_JSON_JUNIT\",\"applicationProvider\":null,\"version\":null,\"isUnknownApplication\":false}";

    // Test application info itself
    lApplicationInfo = lTools.read(lJSON, ApplicationInfo.class);
    assertEquals("JEAF_JSON_JUNIT", lApplicationInfo.getApplicationID());
    assertEquals(null, lApplicationInfo.getName());
    assertEquals(null, lApplicationInfo.getWebsiteURL());
    assertEquals(null, lApplicationInfo.getDescription());
    assertEquals(false, lApplicationInfo.isUnknownApplication());

    // Test connected application provider
    lApplicationProvider = lApplicationInfo.getApplicationProvider();
    assertEquals(ApplicationProvider.UNKNOW_APP_PROVIDER, lApplicationProvider);

    // Test connected version info.
    lVersion = lApplicationInfo.getVersion();
    assertEquals(VersionInfo.UNKNOWN_VERSION, lVersion);

    lJSON = "{\"applicationID\":\"JEAF_JSON_JUNIT\",\"isUnknownApplication\":false}";

    // Test application info itself
    lApplicationInfo = lTools.read(lJSON, ApplicationInfo.class);
    assertEquals("JEAF_JSON_JUNIT", lApplicationInfo.getApplicationID());
    assertEquals(null, lApplicationInfo.getName());
    assertEquals(null, lApplicationInfo.getWebsiteURL());
    assertEquals(null, lApplicationInfo.getDescription());
    assertEquals(false, lApplicationInfo.isUnknownApplication());

    // Test connected application provider
    lApplicationProvider = lApplicationInfo.getApplicationProvider();
    assertEquals(ApplicationProvider.UNKNOW_APP_PROVIDER, lApplicationProvider);

    // Test connected version info.
    lVersion = lApplicationInfo.getVersion();
    assertEquals(VersionInfo.UNKNOWN_VERSION, lVersion);

    // Test missing application id
    try {
      lTools.read("{\"applicationID\":null,\"isUnknownApplication\":false}", ApplicationInfo.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("'pApplicationID' must not be null."));
    }
    try {
      lTools.read("{\"isUnknownApplication\":false}", ApplicationInfo.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("'pApplicationID' must not be null."));
    }

  }
}
