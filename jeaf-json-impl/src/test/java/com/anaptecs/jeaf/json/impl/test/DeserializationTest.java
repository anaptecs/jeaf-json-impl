/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.anaptecs.jeaf.json.api.JSONMessages;
import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.json.impl.test.domain.Broken;
import com.anaptecs.jeaf.json.impl.test.idtest.Product;
import com.anaptecs.jeaf.json.impl.test.idtest.Reseller;
import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.checks.InvalidParameterException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

class DeserializationTest {
  private JSONTools tools = JSONTools.getJSONTools();

  @Test
  void testStringDeserialization( ) {
    String lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}";

    VersionInfo lVersionInfo = tools.read(lJSON, VersionInfo.class);
    assertEquals(3, lVersionInfo.getMajorVersion());
    assertEquals(1, lVersionInfo.getMinorVersion());
    assertEquals(2, lVersionInfo.getBugfixLevel());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersionInfo.getCreationDate());
    assertEquals(false, lVersionInfo.isUnknownVersion());

    // Test empty content
    lVersionInfo = tools.read("null", VersionInfo.class);
    assertEquals(null, lVersionInfo);

    try {
      lVersionInfo = tools.read("", VersionInfo.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

    try {
      lVersionInfo = tools.read((String) null, VersionInfo.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pString must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testStringToListDeserialization( ) throws IOException {
    String lJSON =
        "[{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]},{\"id\":815,\"name\":\"Boring Product\",\"resellers\":[1234]}]";

    List<Product> lProducts = tools.readToCollection(lJSON, List.class, Product.class);
    Product lProduct1 = lProducts.get(0);
    Reseller lReseller1 = lProduct1.getResellers().iterator().next();

    assertEquals("Fancy Product", lProduct1.getName());
    assertEquals(1, lProduct1.getResellers().size());
    assertEquals("Big big Reseller", lReseller1.getName());
    assertEquals(1234, lReseller1.getId());

    Product lProduct2 = lProducts.get(1);
    Reseller lReseller2 = lProduct2.getResellers().iterator().next();

    assertEquals("Boring Product", lProduct2.getName());
    assertEquals(1, lProduct2.getResellers().size());
    assertEquals("Big big Reseller", lReseller2.getName());
    assertEquals(1234, lReseller2.getId());
    assertEquals(lReseller1, lReseller2);
    assertSame(lReseller1, lReseller2);

    lProducts = tools.readToCollection("null", List.class, Product.class);
    assertNull(lProducts);

    lJSON = "[{\"broken\":\"3.1.2\"}, {\"broken\":\"3.1.3\"}]";
    try {
      tools.readToCollection(lJSON, List.class, Broken.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("Exception during JSON deserialization. I'm so broken!"));
    }

    try {
      tools.readToCollection(lJSON, List.class, (Class<?>) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pResultType must not be NULL.", e.getMessage());
    }

    try {
      tools.readToCollection((String) null, List.class, Broken.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pString must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testByteDeserialization( ) {
    String lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}";

    VersionInfo lVersionInfo = tools.read(lJSON.getBytes(), VersionInfo.class);
    assertEquals(3, lVersionInfo.getMajorVersion());
    assertEquals(1, lVersionInfo.getMinorVersion());
    assertEquals(2, lVersionInfo.getBugfixLevel());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersionInfo.getCreationDate());
    assertEquals(false, lVersionInfo.isUnknownVersion());

    // Test empty content
    lVersionInfo = tools.read("null".getBytes(), VersionInfo.class);
    assertEquals(null, lVersionInfo);

    try {
      lVersionInfo = tools.read("".getBytes(), VersionInfo.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

    try {
      lVersionInfo = tools.read((byte[]) null, VersionInfo.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pBytes must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testBytesToListDeserialization( ) throws IOException {
    byte[] lJSON =
        "[{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]},{\"id\":815,\"name\":\"Boring Product\",\"resellers\":[1234]}]"
            .getBytes();

    List<Product> lProducts;
    lProducts = tools.readToCollection(lJSON, List.class, Product.class);
    Product lProduct1 = lProducts.get(0);
    Reseller lReseller1 = lProduct1.getResellers().iterator().next();

    assertEquals("Fancy Product", lProduct1.getName());
    assertEquals(1, lProduct1.getResellers().size());
    assertEquals("Big big Reseller", lReseller1.getName());
    assertEquals(1234, lReseller1.getId());

    Product lProduct2 = lProducts.get(1);
    Reseller lReseller2 = lProduct2.getResellers().iterator().next();

    assertEquals("Boring Product", lProduct2.getName());
    assertEquals(1, lProduct2.getResellers().size());
    assertEquals("Big big Reseller", lReseller2.getName());
    assertEquals(1234, lReseller2.getId());
    assertEquals(lReseller1, lReseller2);
    assertSame(lReseller1, lReseller2);

    lProducts = tools.readToCollection("null", List.class, Product.class);
    assertNull(lProducts);

    lJSON = "[{\"broken\":\"3.1.2\"}, {\"broken\":\"3.1.3\"}]".getBytes();
    try {
      tools.readToCollection(lJSON, List.class, Broken.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("Exception during JSON deserialization. I'm so broken!"));
    }

    try {
      tools.readToCollection(lJSON, List.class, (Class<?>) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pResultType must not be NULL.", e.getMessage());
    }

    try {
      tools.readToCollection((byte[]) null, List.class, Broken.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pBytes must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testFileContentDeserialization( ) {
    VersionInfo lVersionInfo = tools.read(new File("./src/test/resources/Content01.json"), VersionInfo.class);
    assertEquals(3, lVersionInfo.getMajorVersion());
    assertEquals(1, lVersionInfo.getMinorVersion());
    assertEquals(2, lVersionInfo.getBugfixLevel());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersionInfo.getCreationDate());
    assertEquals(false, lVersionInfo.isUnknownVersion());

    // Test empty content
    lVersionInfo = tools.read(new File("./src/test/resources/Content02.json"), VersionInfo.class);
    assertEquals(null, lVersionInfo);

    try {
      lVersionInfo = tools.read(new File("./src/test/resources/Content03.json"), VersionInfo.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

    try {
      lVersionInfo = tools.read((File) null, VersionInfo.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFile must not be NULL.", e.getMessage());
    }

    try {
      lVersionInfo = tools.read(new File("./src/test/resources/NotExistingContent.json"), VersionInfo.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

  }

  @Test
  void testFileToListDeserialization( ) throws IOException {
    File lFile = new File("./src/test/resources/Content04.json");
    List<Product> lProducts = tools.readToCollection(lFile, List.class, Product.class);
    Product lProduct1 = lProducts.get(0);
    Reseller lReseller1 = lProduct1.getResellers().iterator().next();

    assertEquals("Fancy Product", lProduct1.getName());
    assertEquals(1, lProduct1.getResellers().size());
    assertEquals("Big big Reseller", lReseller1.getName());
    assertEquals(1234, lReseller1.getId());

    Product lProduct2 = lProducts.get(1);
    Reseller lReseller2 = lProduct2.getResellers().iterator().next();

    assertEquals("Boring Product", lProduct2.getName());
    assertEquals(1, lProduct2.getResellers().size());
    assertEquals("Big big Reseller", lReseller2.getName());
    assertEquals(1234, lReseller2.getId());
    assertEquals(lReseller1, lReseller2);
    assertSame(lReseller1, lReseller2);

    lFile = new File("./src/test/resources/Content02.json");
    lProducts = tools.readToCollection(lFile, List.class, Product.class);
    assertNull(lProducts);

    lFile = new File("./src/test/resources/Content03.json");
    try {
      tools.readToCollection(lFile, List.class, Product.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(
          e.getMessage().contains("Exception during JSON deserialization. No content to map due to end-of-input"));
    }

    lFile = new File("./src/test/resources/Content05.json");
    try {
      tools.readToCollection(lFile, List.class, Broken.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("Exception during JSON deserialization. I'm so broken!"));
    }

    try {
      tools.readToCollection(lFile, List.class, (Class<?>) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pResultType must not be NULL.", e.getMessage());
    }

    try {
      tools.readToCollection((File) null, List.class, Broken.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFile must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testFileInputStreamDeserialization( ) throws IOException {
    VersionInfo lVersionInfo =
        tools.read(new FileInputStream("./src/test/resources/Content01.json"), VersionInfo.class);
    assertEquals(3, lVersionInfo.getMajorVersion());
    assertEquals(1, lVersionInfo.getMinorVersion());
    assertEquals(2, lVersionInfo.getBugfixLevel());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersionInfo.getCreationDate());
    assertEquals(false, lVersionInfo.isUnknownVersion());

    // Test empty content
    lVersionInfo = tools.read(new FileInputStream("./src/test/resources/Content02.json"), VersionInfo.class);
    assertEquals(null, lVersionInfo);

    try {
      lVersionInfo = tools.read(new FileInputStream("./src/test/resources/Content03.json"), VersionInfo.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

    try {
      lVersionInfo = tools.read((FileInputStream) null, VersionInfo.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pInputStream must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testInputStreamToListDeserialization( ) throws IOException {
    FileInputStream lInputStream = new FileInputStream("./src/test/resources/Content04.json");
    List<Product> lProducts = tools.readToCollection(lInputStream, List.class, Product.class);
    Product lProduct1 = lProducts.get(0);
    Reseller lReseller1 = lProduct1.getResellers().iterator().next();

    assertEquals("Fancy Product", lProduct1.getName());
    assertEquals(1, lProduct1.getResellers().size());
    assertEquals("Big big Reseller", lReseller1.getName());
    assertEquals(1234, lReseller1.getId());

    Product lProduct2 = lProducts.get(1);
    Reseller lReseller2 = lProduct2.getResellers().iterator().next();

    assertEquals("Boring Product", lProduct2.getName());
    assertEquals(1, lProduct2.getResellers().size());
    assertEquals("Big big Reseller", lReseller2.getName());
    assertEquals(1234, lReseller2.getId());
    assertEquals(lReseller1, lReseller2);
    assertSame(lReseller1, lReseller2);

    lInputStream = new FileInputStream("./src/test/resources/Content02.json");
    lProducts = tools.readToCollection(lInputStream, List.class, Product.class);
    assertNull(lProducts);

    lInputStream = new FileInputStream("./src/test/resources/Content03.json");
    try {
      tools.readToCollection(lInputStream, List.class, Product.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(
          e.getMessage().contains("Exception during JSON deserialization. No content to map due to end-of-input"));
    }

    lInputStream = new FileInputStream("./src/test/resources/Content05.json");
    try {
      tools.readToCollection(lInputStream, List.class, Broken.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("Exception during JSON deserialization. I'm so broken!"));
    }

    try {
      tools.readToCollection(lInputStream, List.class, (Class<?>) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pResultType must not be NULL.", e.getMessage());
    }

    try {
      tools.readToCollection((FileInputStream) null, List.class, Broken.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pInputStream must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testFileReaderDeserialization( ) throws IOException {
    VersionInfo lVersionInfo = tools.read(new FileReader("./src/test/resources/Content01.json"), VersionInfo.class);
    assertEquals(3, lVersionInfo.getMajorVersion());
    assertEquals(1, lVersionInfo.getMinorVersion());
    assertEquals(2, lVersionInfo.getBugfixLevel());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersionInfo.getCreationDate());
    assertEquals(false, lVersionInfo.isUnknownVersion());

    // Test empty content
    lVersionInfo = tools.read(new FileReader("./src/test/resources/Content02.json"), VersionInfo.class);
    assertEquals(null, lVersionInfo);

    try {
      lVersionInfo = tools.read(new FileReader("./src/test/resources/Content03.json"), VersionInfo.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

    try {
      lVersionInfo = tools.read((FileReader) null, VersionInfo.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pReader must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testReaderToListDeserialization( ) throws IOException {
    FileReader lFileReader = new FileReader("./src/test/resources/Content04.json");
    List<Product> lProducts = tools.readToCollection(lFileReader, List.class, Product.class);
    Product lProduct1 = lProducts.get(0);
    Reseller lReseller1 = lProduct1.getResellers().iterator().next();

    assertEquals("Fancy Product", lProduct1.getName());
    assertEquals(1, lProduct1.getResellers().size());
    assertEquals("Big big Reseller", lReseller1.getName());
    assertEquals(1234, lReseller1.getId());

    Product lProduct2 = lProducts.get(1);
    Reseller lReseller2 = lProduct2.getResellers().iterator().next();

    assertEquals("Boring Product", lProduct2.getName());
    assertEquals(1, lProduct2.getResellers().size());
    assertEquals("Big big Reseller", lReseller2.getName());
    assertEquals(1234, lReseller2.getId());
    assertEquals(lReseller1, lReseller2);
    assertSame(lReseller1, lReseller2);

    lFileReader = new FileReader("./src/test/resources/Content02.json");
    lProducts = tools.readToCollection(lFileReader, List.class, Product.class);
    assertNull(lProducts);

    lFileReader = new FileReader("./src/test/resources/Content03.json");
    try {
      tools.readToCollection(lFileReader, List.class, Product.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(
          e.getMessage().contains("Exception during JSON deserialization. No content to map due to end-of-input"));
    }

    lFileReader = new FileReader("./src/test/resources/Content05.json");
    try {
      tools.readToCollection(lFileReader, List.class, Broken.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("Exception during JSON deserialization. I'm so broken!"));
    }

    try {
      tools.readToCollection(lFileReader, List.class, (Class<?>) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pResultType must not be NULL.", e.getMessage());
    }

    try {
      tools.readToCollection((FileReader) null, List.class, Broken.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pReader must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testAbstractDeserializer( ) throws JsonProcessingException {
    ObjectMapper lObjectMapper = tools.getDefaultObjectMapper();
    String lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}";
    ObjectNode lObjectNode = (ObjectNode) lObjectMapper.readTree(lJSON);
    assertEquals("3.1.2", lObjectNode.get("version").asText());
  }
}
