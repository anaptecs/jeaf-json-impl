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
import com.anaptecs.jeaf.xfun.api.checks.AssertionFailedError;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedString;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import org.junit.jupiter.api.Test;

class LocalizedObjectTest {
  @Test
  void testLocalizedStringSerialization( ) {
    LocalizedString lLocalizedObject = new LocalizedString(789);
    JSONTools lTools = JSONTools.getJSONTools();
    String lJSON = lTools.writeObjectToString(lLocalizedObject);
    assertEquals("{\"localizationID\":789}", lJSON);
  }

  @Test
  void testLocalizedObjectDeserialization( ) {
    JSONTools lTools = JSONTools.getJSONTools();
    LocalizedString lLocalizedString = lTools.read("{\"localizationID\":789}", LocalizedString.class);
    assertEquals(789, lLocalizedString.getLocalizationID());

    try {
      lTools.read("{\"localizationID\":null}", LocalizedString.class);
    }
    catch (AssertionFailedError e) {
      assertEquals("Assertion failed. pLocalizationID must be zero or greater. Current value: -1", e.getMessage());
    }

    try {
      lTools.read("", LocalizedString.class);
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }
  }

  @Test
  void testMessageIDSerialization( ) {
    MessageID lMessageID = new MessageID(123456, TraceLevel.TRACE);
    JSONTools lTools = JSONTools.getJSONTools();
    String lJSON = lTools.writeObjectToString(lMessageID);
    assertEquals("{\"localizationID\":123456,\"traceLevel\":\"TRACE\"}", lJSON);
  }

  @Test
  void testMessageIDDeserialization( ) {
    JSONTools lTools = JSONTools.getJSONTools();
    MessageID lMessageID = lTools.read("{\"localizationID\":123456,\"traceLevel\":\"TRACE\"}", MessageID.class);
    assertEquals(123456, lMessageID.getLocalizationID());
    assertEquals(TraceLevel.TRACE, lMessageID.getTraceLevel());

    lMessageID = lTools.read("{\"localizationID\":123457}", MessageID.class);
    assertEquals(123457, lMessageID.getLocalizationID());
    assertEquals(TraceLevel.ERROR, lMessageID.getTraceLevel());

    try {
      lTools.read("{\"localizationID\":123456,\"traceLevel\":\"TRACE_XXX\"}", MessageID.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("No enum constant com.anaptecs.jeaf.xfun.api.trace.TraceLevel.TRACE_XXX"));
    }
  }

  @Test
  void testErrorCodeSerialization( ) {
    ErrorCode lErrorCode = new ErrorCode(123456, TraceLevel.TRACE);
    JSONTools lTools = JSONTools.getJSONTools();
    String lJSON = lTools.writeObjectToString(lErrorCode);
    assertEquals("{\"localizationID\":123456,\"traceLevel\":\"TRACE\"}", lJSON);
  }

  @Test
  void testErrorCodeDeserialization( ) {
    JSONTools lTools = JSONTools.getJSONTools();
    ErrorCode lErrorCode = lTools.read("{\"localizationID\":123456,\"traceLevel\":\"TRACE\"}", ErrorCode.class);
    assertEquals(123456, lErrorCode.getLocalizationID());
    assertEquals(TraceLevel.TRACE, lErrorCode.getTraceLevel());

    lErrorCode = lTools.read("{\"localizationID\":123457}", ErrorCode.class);
    assertEquals(123457, lErrorCode.getLocalizationID());
    assertEquals(TraceLevel.ERROR, lErrorCode.getTraceLevel());

    try {
      lTools.read("{\"localizationID\":123456,\"traceLevel\":\"TRACE_XXX\"}", MessageID.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("No enum constant com.anaptecs.jeaf.xfun.api.trace.TraceLevel.TRACE_XXX"));
    }
  }

}
