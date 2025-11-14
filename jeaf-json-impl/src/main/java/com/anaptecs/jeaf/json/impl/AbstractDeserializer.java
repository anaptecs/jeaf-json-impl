/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class implements a base class that can be used to implement JSON deserializers.
 * 
 * @author JEAF Development Team
 */
public abstract class AbstractDeserializer<T> extends StdDeserializer<T> {
  private static final long serialVersionUID = 1L;

  protected AbstractDeserializer( Class<?> pType ) {
    super(pType);
  }

  /**
   * Method reads version info data from the JSON data and creates a {@link VersionInfo} object out of it.
   * 
   * @param pParser JSON parser. The parameter must not be null.
   * @param pContext Context information. The parameter is not used by this implementation.
   */
  @Override
  public final T deserialize( JsonParser pParser, DeserializationContext pContext )
    throws JacksonException, IOException {

    // Check parameters
    Check.checkInvalidParameterNull(pParser, "pParser");

    // Get JSON node that contains the data.
    ObjectNode lNode = pParser.getCodec().readTree(pParser);

    // Deserialize JSON and return created object.
    T lObject;
    if (lNode != null && lNode.isNull() == false) {
      lObject = this.deserialize(lNode);
    }
    else {
      lObject = null;
    }
    return lObject;

  }

  /**
   * Method deserializes the passed JSON {@link ObjectNode} into its matching "real" object.
   * 
   * @param pObjectNode JSON data that should be converted into an real object. The parameter must not be null.
   * @return T Real object that was created using JSON data. The method may return null.
   */
  public abstract T deserialize( ObjectNode pObjectNode );

  /**
   * Method returns the {@link String} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link String} Value of the passed field or its default value.
   */
  protected final String getStringValueFromNode( ObjectNode pObjectNode, String pFieldName, String pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    String lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = lNodeValue.asText();
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;

  }

  /**
   * Method returns the {@link Date} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link Date} Value of the passed field or its default value.
   */
  protected final Date getDateValueFromNode( ObjectNode pObjectNode, String pFieldName, Date pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    Date lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = DateTools.getDateTools().toDate(lNodeValue.asText(), false);
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the {@link Calendar} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link Calendar} Value of the passed field or its default value.
   */
  protected final Calendar getCalendarValueFromNode( ObjectNode pObjectNode, String pFieldName,
      Calendar pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    Calendar lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = DateTools.getDateTools().toCalendar(lNodeValue.asText(), false);
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the boolean value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null.
   * @return boolean Value of the passed field or its default value.
   */
  protected final boolean getBooleanValueFromNode( ObjectNode pObjectNode, String pFieldName, boolean pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    boolean lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Boolean.parseBoolean(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the {@link Boolean} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link Boolean} Value of the passed field or its default value.
   */
  protected final Boolean getBooleanValueFromNode( ObjectNode pObjectNode, String pFieldName, Boolean pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    Boolean lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Boolean.valueOf(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the byte value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null.
   * @return byte Value of the passed field or its default value.
   */
  protected final byte getByteValueFromNode( ObjectNode pObjectNode, String pFieldName, byte pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    byte lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Byte.parseByte(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the {@link Byte} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link Byte} Value of the passed field or its default value.
   */
  protected final Byte getByteValueFromNode( ObjectNode pObjectNode, String pFieldName, Byte pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    Byte lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Byte.valueOf(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the short value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null.
   * @return byte Value of the passed field or its default value.
   */
  protected final short getShortValueFromNode( ObjectNode pObjectNode, String pFieldName, short pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    short lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Short.parseShort(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the {@link Short} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link Short} Value of the passed field or its default value.
   */
  protected final Short getShortValueFromNode( ObjectNode pObjectNode, String pFieldName, Short pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    Short lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Short.valueOf(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the int value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null.
   * @return int Value of the passed field or its default value.
   */
  protected final int getIntegerValueFromNode( ObjectNode pObjectNode, String pFieldName, int pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    int lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Integer.parseInt(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the {@link Integer} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link Integer} Value of the passed field or its default value.
   */
  protected final Integer getIntegerValueFromNode( ObjectNode pObjectNode, String pFieldName, Integer pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    Integer lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Integer.valueOf(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the long value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null.
   * @return long Value of the passed field or its default value.
   */
  protected final long getLongValueFromNode( ObjectNode pObjectNode, String pFieldName, long pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    long lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Long.parseLong(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the {@link Long} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link Long} Value of the passed field or its default value.
   */
  protected final Long getLongValueFromNode( ObjectNode pObjectNode, String pFieldName, Long pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    Long lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Long.valueOf(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the {@link BigInteger} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link BigInteger} Value of the passed field or its default value.
   */
  protected final BigInteger getBigIntegerValueFromNode( ObjectNode pObjectNode, String pFieldName,
      BigInteger pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    BigInteger lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = new BigInteger(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the float value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null.
   * @return long float of the passed field or its default value.
   */
  protected final float getFloatValueFromNode( ObjectNode pObjectNode, String pFieldName, float pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    float lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Float.parseFloat(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the {@link Float} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link Float} Value of the passed field or its default value.
   */
  protected final Float getFloatValueFromNode( ObjectNode pObjectNode, String pFieldName, Float pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    Float lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Float.valueOf(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the double value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null.
   * @return long float of the passed field or its default value.
   */
  protected final double getDoubleValueFromNode( ObjectNode pObjectNode, String pFieldName, double pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    double lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Double.parseDouble(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the {@link Double} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link Double} Value of the passed field or its default value.
   */
  protected final Double getDoubleValueFromNode( ObjectNode pObjectNode, String pFieldName, Double pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    Double lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = Double.valueOf(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }

  /**
   * Method returns the {@link BigDecimal} value from the field with the passed name from the passed JSON node.
   * 
   * @param pObjectNode Node from which the field should be read. The parameter must not be null.
   * @param pFieldName Name of the field from which the value should be read. The parameter must not be null.
   * @param pDefaultValue Default value that should be used in case that the field is not present or null. The parameter
   * may be null.
   * @return {@link BigDecimal} Value of the passed field or its default value.
   */
  protected final BigDecimal getBigDecimalValueFromNode( ObjectNode pObjectNode, String pFieldName,
      BigDecimal pDefaultValue ) {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");
    Check.checkInvalidParameterNull(pFieldName, "pFieldName");

    // Resolve JSON node
    JsonNode lNodeValue = pObjectNode.get(pFieldName);
    BigDecimal lValue;
    if (lNodeValue != null && lNodeValue.isNull() == false) {
      lValue = new BigDecimal(lNodeValue.asText());
    }
    else {
      lValue = pDefaultValue;
    }
    return lValue;
  }
}
