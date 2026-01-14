/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.json.impl.AbstractDeserializer;
import com.anaptecs.jeaf.tools.api.ToolsMessages;
import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.checks.InvalidParameterException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

/**
 * Class tests implementations of class {@link AbstractDeserializer}
 */
class AbstractDeserializerTest extends AbstractDeserializer<Map<String, String>> {
  public AbstractDeserializerTest( ) {
    super(Object.class);
  }

  @Override
  public Map<String, String> deserialize(ObjectNode pObjectNode) {
    Map<String, String> lMap = new HashMap<>();
    for (String lFieldName : pObjectNode.propertyNames()) {
      lMap.put(lFieldName, pObjectNode.get(lFieldName).asString());
    }
    return lMap;
  }

  @Test
  void testDeserialize( ) throws IOException {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    String lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}";
    JsonParser lParser = lObjectMapper.createParser(lJSON);
    Map<String, String> lMap = this.deserialize(lParser, null);

    assertEquals("3.1.2", lMap.get("version"));
    assertEquals("2022-01-19 17:02:45.551", lMap.get("date"));
    assertEquals("false", lMap.get("isUnknownVersion"));

    // Test null handling
    lParser = lObjectMapper.createParser("");
    assertEquals(null, this.deserialize(lParser, null));

    lParser = lObjectMapper.createParser("");
    assertEquals(null, this.deserialize(lParser, null));
  }

  @Test
  void testStringDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    String lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}";
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);

    assertEquals("3.1.2", this.getStringValueFromNode(lObjectNode, "version", null));
    assertEquals(null, this.getStringValueFromNode(lObjectNode, "versionXXX", null));
    assertEquals("123", this.getStringValueFromNode(lObjectNode, "versionXXX", "123"));

    // Test null value
    lJSON = "{\"version\":null,\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}";
    lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);
    assertEquals(null, this.getStringValueFromNode(lObjectNode, "version", null));
    assertEquals("123", this.getStringValueFromNode(lObjectNode, "version", "123"));

    // Test exception handling
    try {
      this.getStringValueFromNode(null, "version", null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getStringValueFromNode(lObjectNode, null, null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testDateDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    String lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}";
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);

    DateTools lDateTools = DateTools.getDateTools();
    assertEquals(lDateTools.toDate("2022-01-19 17:02:45.551"), this.getDateValueFromNode(lObjectNode, "date", null));
    assertEquals(null, this.getDateValueFromNode(lObjectNode, "Date", null));

    Date lDate = lDateTools.toDate("2000-12-24 18:00", false);
    assertEquals(lDate, this.getDateValueFromNode(lObjectNode, "Date", lDate));

    // Test null value
    lJSON = "{\"version\":\"3.1.2\",\"date\":null,\"isUnknownVersion\":false}";
    lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);
    assertEquals(null, this.getDateValueFromNode(lObjectNode, "date", null));
    assertEquals(lDate, this.getDateValueFromNode(lObjectNode, "date", lDate));

    // Test exception handling
    try {
      this.getDateValueFromNode(null, "version", null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getDateValueFromNode(lObjectNode, null, null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid dates
    lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02\",\"isUnknownVersion\":false}";
    lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);
    assertEquals(lDateTools.toDate("2022-01-19 17:02:00.000"), this.getDateValueFromNode(lObjectNode, "date", null));

    lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19\",\"isUnknownVersion\":false}";
    lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);
    assertEquals(lDateTools.toDate("2022-01-19 00:00:00.000"), this.getDateValueFromNode(lObjectNode, "date", null));

    lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01 17:02\",\"isUnknownVersion\":false}";
    lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);
    try {
      this.getDateValueFromNode(lObjectNode, "date", null);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(ToolsMessages.INVALID_TIMESTAMP_FORMAT, e.getErrorCode());
    }
  }

  @Test
  void testCalendarDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    String lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}";
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);

    DateTools lDateTools = DateTools.getDateTools();
    assertEquals(lDateTools.toCalendar("2022-01-19 17:02:45.551"),
        this.getCalendarValueFromNode(lObjectNode, "date", null));
    assertEquals(null, this.getCalendarValueFromNode(lObjectNode, "Date", null));

    Calendar lCalendar = lDateTools.toCalendar("2000-12-24 18:00", false);
    assertEquals(lCalendar, this.getCalendarValueFromNode(lObjectNode, "Date", lCalendar));

    // Test null value
    lJSON = "{\"version\":\"3.1.2\",\"date\":null,\"isUnknownVersion\":false}";
    lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);
    assertEquals(null, this.getCalendarValueFromNode(lObjectNode, "date", null));
    assertEquals(lCalendar, this.getCalendarValueFromNode(lObjectNode, "date", lCalendar));

    // Test exception handling
    try {
      this.getCalendarValueFromNode(null, "version", null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getCalendarValueFromNode(lObjectNode, null, null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid dates
    lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02\",\"isUnknownVersion\":false}";
    lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);
    assertEquals(lDateTools.toCalendar("2022-01-19 17:02:00.000"),
        this.getCalendarValueFromNode(lObjectNode, "date", null));

    lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19\",\"isUnknownVersion\":false}";
    lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);
    assertEquals(lDateTools.toCalendar("2022-01-19 00:00:00.000"),
        this.getCalendarValueFromNode(lObjectNode, "date", null));

    lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01 17:02\",\"isUnknownVersion\":false}";
    lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);
    try {
      this.getCalendarValueFromNode(lObjectNode, "date", null);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(ToolsMessages.INVALID_TIMESTAMP_FORMAT, e.getErrorCode());
    }
  }

  @Test
  void testPrimitiveBooleanDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"booleanValue\":\"true\"}");

    assertEquals(true, this.getBooleanValueFromNode(lObjectNode, "booleanValue", false));
    assertEquals(false, this.getBooleanValueFromNode(lObjectNode, "booleanValue2", false));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"booleanValue\":null}");
    assertEquals(false, this.getBooleanValueFromNode(lObjectNode, "booleanValue", false));

    // Test exception handling
    try {
      this.getBooleanValueFromNode(null, "version", false);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getBooleanValueFromNode(lObjectNode, null, false);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test invalid values.
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"booleanValue\":\"invalid\"}");
    assertEquals(false, this.getBooleanValueFromNode(lObjectNode, "booleanValue", true));
  }

  @Test
  void testBooleanDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"booleanValue\":\"true\"}");

    assertEquals(Boolean.TRUE, this.getBooleanValueFromNode(lObjectNode, "booleanValue", Boolean.FALSE));
    assertEquals(Boolean.FALSE, this.getBooleanValueFromNode(lObjectNode, "booleanValue2", Boolean.FALSE));
    assertEquals(null, this.getBooleanValueFromNode(lObjectNode, "booleanValue2", null));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"booleanValue\":null}");
    assertEquals(false, this.getBooleanValueFromNode(lObjectNode, "booleanValue", Boolean.FALSE));

    // Test exception handling
    try {
      this.getBooleanValueFromNode(null, "version", Boolean.FALSE);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getBooleanValueFromNode(lObjectNode, null, Boolean.FALSE);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test invalid values.
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"booleanValue\":\"invalid\"}");
    assertEquals(Boolean.FALSE, this.getBooleanValueFromNode(lObjectNode, "booleanValue", Boolean.TRUE));
  }

  @Test
  void testPrimitiveByteDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"byteValue\":\"73\"}");

    assertEquals((byte) 73, this.getByteValueFromNode(lObjectNode, "byteValue", (byte) 4));
    assertEquals((byte) 4, this.getByteValueFromNode(lObjectNode, "byteValue2", (byte) 4));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"byteValue\":null}");
    assertEquals((byte) 4, this.getByteValueFromNode(lObjectNode, "byteValue", (byte) 4));

    // Test exception handling
    try {
      this.getByteValueFromNode(null, "byteValue", (byte) 47);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getByteValueFromNode(lObjectNode, null, (byte) 47);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid dates
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"byteValue\":\"3.1\"}");
    try {
      this.getByteValueFromNode(lObjectNode, "byteValue", (byte) 4);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"byteValue\":\"a123\"}");
    try {
      this.getByteValueFromNode(lObjectNode, "byteValue", (byte) 4);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    // Test max value exceeded
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"byteValue\":\"128\"}");
    try {
      this.getByteValueFromNode(lObjectNode, "byteValue", (byte) 4);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testByteDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"byteValue\":\"73\"}");

    assertEquals((byte) 73, this.getByteValueFromNode(lObjectNode, "byteValue", (Byte) null));
    assertEquals(Byte.valueOf((byte) 4), this.getByteValueFromNode(lObjectNode, "byteValue2", Byte.valueOf((byte) 4)));
    assertEquals(null, this.getByteValueFromNode(lObjectNode, "byteValue2", null));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"byteValue\":null}");
    assertEquals((byte) 4, this.getByteValueFromNode(lObjectNode, "byteValue", Byte.valueOf((byte) 4)));

    // Test exception handling
    try {
      this.getByteValueFromNode(null, "byteValue", null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getByteValueFromNode(lObjectNode, null, null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid dates
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"byteValue\":\"3.1\"}");
    try {
      this.getByteValueFromNode(lObjectNode, "byteValue", Byte.valueOf((byte) 4));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"byteValue\":\"a123\"}");
    try {
      this.getByteValueFromNode(lObjectNode, "byteValue", Byte.valueOf((byte) 4));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    // Test max value exceeded
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"byteValue\":\"128\"}");
    try {
      this.getByteValueFromNode(lObjectNode, "byteValue", Byte.valueOf((byte) 4));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testPrimitiveShortDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"shortValue\":\"32023\"}");

    assertEquals((short) 32023, this.getShortValueFromNode(lObjectNode, "shortValue", (short) 4));
    assertEquals((short) 4, this.getShortValueFromNode(lObjectNode, "shortValue2", (short) 4));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"shortValue\":null}");
    assertEquals((short) 4, this.getShortValueFromNode(lObjectNode, "shortValue", (short) 4));

    // Test exception handling
    try {
      this.getShortValueFromNode(null, "shortValue", (short) 4711);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getShortValueFromNode(lObjectNode, null, (short) 4711);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid dates
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"shortValue\":\"3.1\"}");
    try {
      this.getShortValueFromNode(lObjectNode, "shortValue", (short) 4);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"shortValue\":\"a123\"}");
    try {
      this.getShortValueFromNode(lObjectNode, "shortValue", (short) 4);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    // Test max value exceeded
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"shortValue\":\"32768\"}");
    try {
      this.getShortValueFromNode(lObjectNode, "shortValue", (short) 4);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testShortDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"shortValue\":\"32023\"}");

    assertEquals(Short.valueOf((short) 32023),
        this.getShortValueFromNode(lObjectNode, "shortValue", Short.valueOf((short) 4)));
    assertEquals(Short.valueOf((short) 4),
        this.getShortValueFromNode(lObjectNode, "shortValue2", Short.valueOf((short) 4)));
    assertEquals(null, this.getShortValueFromNode(lObjectNode, "shortValue2", null));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"shortValue\":null}");
    assertEquals(Short.valueOf((short) 4),
        this.getShortValueFromNode(lObjectNode, "shortValue", Short.valueOf((short) 4)));

    // Test exception handling
    try {
      this.getShortValueFromNode(null, "shortValue", Short.valueOf((short) 4711));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getShortValueFromNode(lObjectNode, null, Short.valueOf((short) 4711));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid dates
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"shortValue\":\"3.1\"}");
    try {
      this.getShortValueFromNode(lObjectNode, "shortValue", Short.valueOf((short) 4));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"shortValue\":\"a123\"}");
    try {
      this.getShortValueFromNode(lObjectNode, "shortValue", Short.valueOf((short) 4));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    // Test max value exceeded
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"shortValue\":\"32768\"}");
    try {
      this.getShortValueFromNode(lObjectNode, "shortValue", Short.valueOf((short) 4));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testPrimitiveIntegerDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"intValue\":\"77023\"}");

    assertEquals(77023, this.getIntegerValueFromNode(lObjectNode, "intValue", 4));
    assertEquals(4, this.getIntegerValueFromNode(lObjectNode, "intValue2", 4));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"intValue\":null}");
    assertEquals(4, this.getIntegerValueFromNode(lObjectNode, "intValue", 4));

    // Test exception handling
    try {
      this.getIntegerValueFromNode(null, "intValue", 4711);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getIntegerValueFromNode(lObjectNode, null, 4711);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid dates
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"intValue\":\"3.1\"}");
    try {
      this.getIntegerValueFromNode(lObjectNode, "intValue", 4);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"intValue\":\"a123\"}");
    try {
      this.getIntegerValueFromNode(lObjectNode, "intValue", 4);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    // Test max value exceeded
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"intValue\":\"12345678901234567890\"}");
    try {
      this.getIntegerValueFromNode(lObjectNode, "intValue", 4);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testIntegerDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"intValue\":\"77023\"}");

    assertEquals(77023, this.getIntegerValueFromNode(lObjectNode, "intValue", null));
    assertEquals(Integer.valueOf(4), this.getIntegerValueFromNode(lObjectNode, "intValue2", Integer.valueOf(4)));
    assertEquals(null, this.getIntegerValueFromNode(lObjectNode, "intValue2", null));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"intValue\":null}");
    assertEquals(4, this.getIntegerValueFromNode(lObjectNode, "intValue", Integer.valueOf(4)));

    // Test exception handling
    try {
      this.getIntegerValueFromNode(null, "intValue", null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getIntegerValueFromNode(lObjectNode, null, null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid dates
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"intValue\":\"3.1\"}");
    try {
      this.getIntegerValueFromNode(lObjectNode, "intValue", Integer.valueOf(4));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"intValue\":\"a123\"}");
    try {
      this.getIntegerValueFromNode(lObjectNode, "intValue", Integer.valueOf(4));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    // Test max value exceeded
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"intValue\":\"12345678901234567890\"}");
    try {
      this.getIntegerValueFromNode(lObjectNode, "intValue", Integer.valueOf(4));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testPrimitiveLongDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"longValue\":\"78887023\"}");

    assertEquals(78887023, this.getLongValueFromNode(lObjectNode, "longValue", 4l));
    assertEquals(4l, this.getLongValueFromNode(lObjectNode, "longValue2", 4l));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"longValue\":null}");
    assertEquals(4l, this.getLongValueFromNode(lObjectNode, "longValue", 4l));

    // Test exception handling
    try {
      this.getLongValueFromNode(null, "longValue", 4711l);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getLongValueFromNode(lObjectNode, null, 4711l);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid dates
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"longValue\":\"3.1\"}");
    try {
      this.getLongValueFromNode(lObjectNode, "longValue", 4l);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"longValue\":\"a123\"}");
    try {
      this.getLongValueFromNode(lObjectNode, "longValue", 4l);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    // Test max value exceeded
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"longValue\":\"12345678901234567890\"}");
    try {
      this.getLongValueFromNode(lObjectNode, "longValue", 4l);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testLongDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"longValue\":\"78887023\"}");

    assertEquals(Long.valueOf(78887023), this.getLongValueFromNode(lObjectNode, "longValue", Long.valueOf(4l)));
    assertEquals(Long.valueOf(4l), this.getLongValueFromNode(lObjectNode, "longValue2", Long.valueOf(4l)));
    assertEquals(null, this.getLongValueFromNode(lObjectNode, "longValue2", null));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"longValue\":null}");
    assertEquals(Long.valueOf(4l), this.getLongValueFromNode(lObjectNode, "longValue", Long.valueOf(4l)));

    // Test exception handling
    try {
      this.getLongValueFromNode(null, "longValue", Long.valueOf(4711l));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getLongValueFromNode(lObjectNode, null, Long.valueOf(4711l));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid dates
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"longValue\":\"3.1\"}");
    try {
      this.getLongValueFromNode(lObjectNode, "longValue", Long.valueOf(4l));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"longValue\":\"a123\"}");
    try {
      this.getLongValueFromNode(lObjectNode, "longValue", Long.valueOf(4l));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    // Test max value exceeded
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"longValue\":\"12345678901234567890\"}");
    try {
      this.getLongValueFromNode(lObjectNode, "longValue", Long.valueOf(4l));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testBigIntegerDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"bigIntegerValue\":\"78887045131516164891923\"}");

    assertEquals(new BigInteger("78887045131516164891923"),
        this.getBigIntegerValueFromNode(lObjectNode, "bigIntegerValue", BigInteger.valueOf(4)));
    assertEquals(BigInteger.valueOf(4),
        this.getBigIntegerValueFromNode(lObjectNode, "bigIntegerValue2", BigInteger.valueOf(4)));
    assertEquals(null, this.getBigIntegerValueFromNode(lObjectNode, "bigIntegerValue2", null));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"bigIntegerValue\":null}");
    assertEquals(BigInteger.valueOf(4),
        this.getBigIntegerValueFromNode(lObjectNode, "bigIntegerValue", BigInteger.valueOf(4)));

    // Test exception handling
    try {
      this.getBigIntegerValueFromNode(null, "bigIntegerValue", BigInteger.valueOf(4711));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getBigIntegerValueFromNode(lObjectNode, null, BigInteger.valueOf(4711));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid dates
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"bigIntegerValue\":\"3.1\"}");
    try {
      this.getBigIntegerValueFromNode(lObjectNode, "bigIntegerValue", BigInteger.valueOf(4));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"bigIntegerValue\":\"a123\"}");
    try {
      this.getBigIntegerValueFromNode(lObjectNode, "bigIntegerValue", BigInteger.valueOf(4));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

    // Test huge value.
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"bigIntegerValue\":\"123456789012345674551189890\"}");
    assertEquals(new BigInteger("123456789012345674551189890"),
        this.getBigIntegerValueFromNode(lObjectNode, "bigIntegerValue", BigInteger.valueOf(4)));
  }

  @Test
  void testPrimitiveFloatDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"floatValue\":\"770.23\"}");

    // Be aware of precision problems with floats ;-)
    assertEquals(770.22998046875, this.getFloatValueFromNode(lObjectNode, "floatValue", 4f));
    assertEquals(4.1f, this.getFloatValueFromNode(lObjectNode, "floatValue2", 4.1f));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"floatValue\":null}");
    assertEquals(4.2f, this.getFloatValueFromNode(lObjectNode, "floatValue", 4.2f));

    // Test exception handling
    try {
      this.getFloatValueFromNode(null, "floatValue", 4711.123f);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getFloatValueFromNode(lObjectNode, null, 4711.123f);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid values
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"floatValue\":\"a123\"}");
    try {
      this.getFloatValueFromNode(lObjectNode, "floatValue", 4711.123f);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testFloatDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"floatValue\":\"770.23\"}");

    // Be aware of precision problems with floats ;-)
    assertEquals(Float.valueOf(770.22998046875f),
        this.getFloatValueFromNode(lObjectNode, "floatValue", Float.valueOf(4f)));
    assertEquals(Float.valueOf(4.1f), this.getFloatValueFromNode(lObjectNode, "floatValue2", Float.valueOf(4.1f)));
    assertEquals(null, this.getFloatValueFromNode(lObjectNode, "floatValue2", null));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"floatValue\":null}");
    assertEquals(Float.valueOf(4.2f), this.getFloatValueFromNode(lObjectNode, "floatValue", Float.valueOf(4.2f)));

    // Test exception handling
    try {
      this.getFloatValueFromNode(null, "floatValue", Float.valueOf(4711.123f));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getFloatValueFromNode(lObjectNode, null, Float.valueOf(4711.123f));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid values
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"floatValue\":\"a123\"}");
    try {
      this.getFloatValueFromNode(lObjectNode, "floatValue", Float.valueOf(4711.123f));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testPrimitiveDoubleDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"doubleValue\":\"770.23\"}");

    assertEquals(770.23, this.getDoubleValueFromNode(lObjectNode, "doubleValue", 4.0));
    assertEquals(4.1, this.getDoubleValueFromNode(lObjectNode, "doubleValue2", 4.1));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"doubleValue\":null}");
    assertEquals(4.2, this.getDoubleValueFromNode(lObjectNode, "doubleValue", 4.2));

    // Test exception handling
    try {
      this.getDoubleValueFromNode(null, "doubleValue", 4711.123);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getDoubleValueFromNode(lObjectNode, null, 4711.123);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid values
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"doubleValue\":\"a123\"}");
    try {
      this.getDoubleValueFromNode(lObjectNode, "doubleValue", 4711.123);
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testDoubleDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"doubleValue\":\"770.23\"}");

    assertEquals(Double.valueOf(770.23), this.getDoubleValueFromNode(lObjectNode, "doubleValue", Double.valueOf(4.0)));
    assertEquals(Double.valueOf(4.1), this.getDoubleValueFromNode(lObjectNode, "doubleValue2", Double.valueOf(4.1)));
    assertEquals(null, this.getDoubleValueFromNode(lObjectNode, "doubleValue2", null));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"doubleValue\":null}");
    assertEquals(Double.valueOf(4.2), this.getDoubleValueFromNode(lObjectNode, "doubleValue", Double.valueOf(4.2)));

    // Test exception handling
    try {
      this.getDoubleValueFromNode(null, "doubleValue", Double.valueOf(4711.123));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getDoubleValueFromNode(lObjectNode, null, Double.valueOf(4711.123));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid values
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"doubleValue\":\"a123\"}");
    try {
      this.getDoubleValueFromNode(lObjectNode, "doubleValue", Double.valueOf(4711.123));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }

  @Test
  void testBigDecimalDeserialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    ObjectNode lObjectNode =
        (ObjectNode) lObjectMapper.readTree("{\"bigDecimalValue\":\"71616131651670.2346488434654638\"}");

    assertEquals(new BigDecimal("71616131651670.2346488434654638"),
        this.getBigDecimalValueFromNode(lObjectNode, "bigDecimalValue", BigDecimal.valueOf(4.0)));
    assertEquals(BigDecimal.valueOf(4.1),
        this.getBigDecimalValueFromNode(lObjectNode, "bigDecimalValue2", BigDecimal.valueOf(4.1)));
    assertEquals(null, this.getBigDecimalValueFromNode(lObjectNode, "bigDecimalValue2", null));

    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"bigDecimalValue\":null}");
    assertEquals(BigDecimal.valueOf(4.2),
        this.getBigDecimalValueFromNode(lObjectNode, "bigDecimalValue", BigDecimal.valueOf(4.2)));

    // Test exception handling
    try {
      this.getBigDecimalValueFromNode(null, "bigDecimalValue", BigDecimal.valueOf(4711.123));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectNode must not be NULL.", e.getMessage());
    }

    try {
      this.getBigDecimalValueFromNode(lObjectNode, null, BigDecimal.valueOf(4711.123));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFieldName must not be NULL.", e.getMessage());
    }

    // Test handling of invalid values
    lObjectNode = (ObjectNode) lObjectMapper.readTree("{\"bigDecimalValue\":\"a123\"}");
    try {
      this.getBigDecimalValueFromNode(lObjectNode, "bigDecimalValue", BigDecimal.valueOf(4711.123));
      fail();
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }
  }
}
