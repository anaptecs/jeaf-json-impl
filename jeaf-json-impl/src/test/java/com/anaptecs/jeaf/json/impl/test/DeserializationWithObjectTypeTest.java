/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.json.api.JSONMessages;
import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.json.api.ObjectType;
import com.anaptecs.jeaf.json.impl.test.domain.Broken;
import com.anaptecs.jeaf.json.impl.test.idtest.Product;
import com.anaptecs.jeaf.json.impl.test.idtest.Reseller;
import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.checks.InvalidParameterException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;

class DeserializationWithObjectTypeTest {
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
    lVersionInfo = tools.read("null", ObjectType.createObjectType(VersionInfo.class));
    assertEquals(null, lVersionInfo);

    try {
      lVersionInfo = tools.read("", ObjectType.createObjectType(VersionInfo.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

    try {
      lVersionInfo = tools.read((String) null, ObjectType.createObjectType(VersionInfo.class));
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

    List<Product> lProducts = tools.read(lJSON, ObjectType.createObjectType(List.class, Product.class));
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

    lProducts = tools.read("null", ObjectType.createObjectType(List.class, Product.class));
    assertNull(lProducts);

    lJSON = "[{\"broken\":\"3.1.2\"}, {\"broken\":\"3.1.3\"}]";
    try {
      tools.read(lJSON, ObjectType.createObjectType(List.class, Broken.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("Exception during JSON deserialization. I'm so broken!"));
    }

    try {
      tools.read(lJSON, (ObjectType) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pResultType must not be NULL.", e.getMessage());
    }

    try {
      tools.read((String) null, ObjectType.createObjectType(List.class, Broken.class));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pString must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testByteDeserialization( ) {
    String lJSON = "{\"version\":\"3.1.2\",\"date\":\"2022-01-19 17:02:45.551\",\"isUnknownVersion\":false}";

    VersionInfo lVersionInfo = tools.read(lJSON.getBytes(), ObjectType.createObjectType(VersionInfo.class));
    assertEquals(3, lVersionInfo.getMajorVersion());
    assertEquals(1, lVersionInfo.getMinorVersion());
    assertEquals(2, lVersionInfo.getBugfixLevel());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersionInfo.getCreationDate());
    assertEquals(false, lVersionInfo.isUnknownVersion());

    // Test empty content
    lVersionInfo = tools.read("null".getBytes(), ObjectType.createObjectType(VersionInfo.class));
    assertEquals(null, lVersionInfo);

    try {
      lVersionInfo = tools.read("".getBytes(), ObjectType.createObjectType(VersionInfo.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

    try {
      lVersionInfo = tools.read((byte[]) null, ObjectType.createObjectType(VersionInfo.class));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pBytes must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testBytesToSetDeserialization( ) throws IOException {
    byte[] lJSON =
        "[{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]},{\"id\":815,\"name\":\"Boring Product\",\"resellers\":[1234]}]"
            .getBytes();

    Set<Product> lProducts = tools.read(lJSON, ObjectType.createObjectType(Set.class, Product.class));

    Map<String, Product> lProductMap = new HashMap<>();
    for (Product lNext : lProducts) {
      lProductMap.put(lNext.getName(), lNext);
    }
    Product lProduct1 = lProductMap.get("Fancy Product");
    Reseller lReseller1 = lProduct1.getResellers().iterator().next();

    assertEquals("Fancy Product", lProduct1.getName());
    assertEquals(1, lProduct1.getResellers().size());
    assertEquals("Big big Reseller", lReseller1.getName());
    assertEquals(1234, lReseller1.getId());

    Product lProduct2 = lProductMap.get("Boring Product");
    Reseller lReseller2 = lProduct2.getResellers().iterator().next();

    assertEquals("Boring Product", lProduct2.getName());
    assertEquals(1, lProduct2.getResellers().size());
    assertEquals("Big big Reseller", lReseller2.getName());
    assertEquals(1234, lReseller2.getId());
    assertEquals(lReseller1, lReseller2);
    assertSame(lReseller1, lReseller2);

    assertEquals(2, lProducts.size());

    lProducts = tools.read("null", ObjectType.createObjectType(Set.class, Product.class));
    assertNull(lProducts);

    lJSON = "[{\"broken\":\"3.1.2\"}, {\"broken\":\"3.1.3\"}]".getBytes();
    try {
      tools.read(lJSON, ObjectType.createObjectType(Set.class, Broken.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("Exception during JSON deserialization. I'm so broken!"));
    }

    try {
      tools.read(lJSON, (ObjectType) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pResultType must not be NULL.", e.getMessage());
    }

    try {
      tools.read((byte[]) null, ObjectType.createObjectType(List.class, Broken.class));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pBytes must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testFileContentDeserialization( ) {
    VersionInfo lVersionInfo =
        tools.read(new File("./src/test/resources/Content01.json"), ObjectType.createObjectType(VersionInfo.class));
    assertEquals(3, lVersionInfo.getMajorVersion());
    assertEquals(1, lVersionInfo.getMinorVersion());
    assertEquals(2, lVersionInfo.getBugfixLevel());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersionInfo.getCreationDate());
    assertEquals(false, lVersionInfo.isUnknownVersion());

    // Test empty content
    lVersionInfo =
        tools.read(new File("./src/test/resources/Content02.json"), ObjectType.createObjectType(VersionInfo.class));
    assertEquals(null, lVersionInfo);

    try {
      lVersionInfo =
          tools.read(new File("./src/test/resources/Content03.json"), ObjectType.createObjectType(VersionInfo.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

    try {
      lVersionInfo = tools.read((File) null, ObjectType.createObjectType(VersionInfo.class));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFile must not be NULL.", e.getMessage());
    }

    try {
      lVersionInfo = tools.read(new File("./src/test/resources/NotExistingContent.json"),
          ObjectType.createObjectType(VersionInfo.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

  }

  @Test
  void testFileToListDeserialization( ) throws IOException {
    File lFile = new File("./src/test/resources/Content04.json");
    List<Product> lProducts = tools.read(lFile, ObjectType.createObjectType(List.class, Product.class));
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
    lProducts = tools.read(lFile, ObjectType.createObjectType(List.class, Product.class));
    assertNull(lProducts);

    lFile = new File("./src/test/resources/Content03.json");
    try {
      tools.read(lFile, ObjectType.createObjectType(List.class, Product.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(
          e.getMessage().contains("Exception during JSON deserialization. No content to map due to end-of-input"));
    }

    lFile = new File("./src/test/resources/Content05.json");
    try {
      tools.read(lFile, ObjectType.createObjectType(List.class, Broken.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("Exception during JSON deserialization. I'm so broken!"));
    }

    try {
      tools.read(lFile, (ObjectType) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pResultType must not be NULL.", e.getMessage());
    }

    try {
      tools.read((File) null, ObjectType.createObjectType(List.class, Broken.class));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFile must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testFileInputStreamDeserialization( ) throws IOException {
    VersionInfo lVersionInfo = tools.read(new FileInputStream("./src/test/resources/Content01.json"),
        ObjectType.createObjectType(VersionInfo.class));
    assertEquals(3, lVersionInfo.getMajorVersion());
    assertEquals(1, lVersionInfo.getMinorVersion());
    assertEquals(2, lVersionInfo.getBugfixLevel());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersionInfo.getCreationDate());
    assertEquals(false, lVersionInfo.isUnknownVersion());

    // Test empty content
    lVersionInfo = tools.read(new FileInputStream("./src/test/resources/Content02.json"),
        ObjectType.createObjectType(VersionInfo.class));
    assertEquals(null, lVersionInfo);

    try {
      lVersionInfo = tools.read(new FileInputStream("./src/test/resources/Content03.json"),
          ObjectType.createObjectType(VersionInfo.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

    try {
      lVersionInfo = tools.read((FileInputStream) null, ObjectType.createObjectType(VersionInfo.class));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pInputStream must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testInputStreamToListDeserialization( ) throws IOException {
    FileInputStream lInputStream = new FileInputStream("./src/test/resources/Content04.json");
    List<Product> lProducts = tools.read(lInputStream, ObjectType.createObjectType(List.class, Product.class));
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
    lProducts = tools.read(lInputStream, ObjectType.createObjectType(List.class, Product.class));
    assertNull(lProducts);

    lInputStream = new FileInputStream("./src/test/resources/Content03.json");
    try {
      tools.read(lInputStream, ObjectType.createObjectType(List.class, Product.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(
          e.getMessage().contains("Exception during JSON deserialization. No content to map due to end-of-input"));
    }

    lInputStream = new FileInputStream("./src/test/resources/Content05.json");
    try {
      tools.read(lInputStream, ObjectType.createObjectType(List.class, Broken.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("Exception during JSON deserialization. I'm so broken!"));
    }

    try {
      tools.read(lInputStream, (ObjectType) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pResultType must not be NULL.", e.getMessage());
    }

    try {
      tools.read((FileInputStream) null, ObjectType.createObjectType(List.class, Broken.class));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pInputStream must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testFileReaderDeserialization( ) throws IOException {
    VersionInfo lVersionInfo = tools.read(new FileReader("./src/test/resources/Content01.json"),
        ObjectType.createObjectType(VersionInfo.class));
    assertEquals(3, lVersionInfo.getMajorVersion());
    assertEquals(1, lVersionInfo.getMinorVersion());
    assertEquals(2, lVersionInfo.getBugfixLevel());
    assertEquals(DateTools.getDateTools().toDate("2022-01-19 17:02:45.551"), lVersionInfo.getCreationDate());
    assertEquals(false, lVersionInfo.isUnknownVersion());

    // Test empty content
    lVersionInfo = tools.read(new FileReader("./src/test/resources/Content02.json"),
        ObjectType.createObjectType(VersionInfo.class));
    assertEquals(null, lVersionInfo);

    try {
      lVersionInfo = tools.read(new FileReader("./src/test/resources/Content03.json"),
          ObjectType.createObjectType(VersionInfo.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
    }

    try {
      lVersionInfo = tools.read((FileReader) null, ObjectType.createObjectType(VersionInfo.class));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pReader must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testReaderToListDeserialization( ) throws IOException {
    FileReader lFileReader = new FileReader("./src/test/resources/Content04.json");
    List<Product> lProducts = tools.read(lFileReader, ObjectType.createObjectType(List.class, Product.class));
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
    lProducts = tools.read(lFileReader, ObjectType.createObjectType(List.class, Product.class));
    assertNull(lProducts);

    lFileReader = new FileReader("./src/test/resources/Content03.json");
    try {
      tools.read(lFileReader, ObjectType.createObjectType(List.class, Product.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(
          e.getMessage().contains("Exception during JSON deserialization. No content to map due to end-of-input"));
    }

    lFileReader = new FileReader("./src/test/resources/Content05.json");
    try {
      tools.read(lFileReader, ObjectType.createObjectType(List.class, Broken.class));
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_DESERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().contains("Exception during JSON deserialization. I'm so broken!"));
    }

    try {
      tools.read(lFileReader, (ObjectType) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pResultType must not be NULL.", e.getMessage());
    }

    try {
      tools.read((FileReader) null, ObjectType.createObjectType(List.class, Broken.class));
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pReader must not be NULL.", e.getMessage());
    }
  }
}
