/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.anaptecs.jeaf.json.api.JSONMessages;
import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.json.impl.test.domain.Broken;
import com.anaptecs.jeaf.json.impl.test.idtest.Image;
import com.anaptecs.jeaf.json.impl.test.idtest.Product;
import com.anaptecs.jeaf.json.impl.test.idtest.Reseller;
import com.anaptecs.jeaf.tools.api.file.FileTools;
import com.anaptecs.jeaf.xfun.api.checks.InvalidParameterException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SerializationTest {
  private Reseller reseller;

  private Product product1;

  private Product product2;

  private List<Product> products = new ArrayList<>();

  private final JSONTools tools = JSONTools.getJSONTools();

  @BeforeEach
  void setup( ) {
    Reseller.Builder lBuilder = new Reseller.Builder();
    lBuilder.setId(1234);
    lBuilder.setName("Big big Reseller");
    reseller = lBuilder.build();

    product1 = new Product();
    product1.setId(4711);
    product1.setName("Fancy Product");
    product1.addReseller(reseller);

    product2 = new Product();
    product2.setId(815);
    product2.setName("Boring Product");
    product2.addReseller(reseller);

    products.add(product1);
    products.add(product2);
  }

  @Test
  void testStringSerialization( ) {
    String lJSON = tools.writeObjectToString(product1);
    assertEquals("{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]}",
        lJSON);

    lJSON = tools.writeObjectToString(null);
    assertEquals("null", lJSON);

    try {
      tools.writeObjectToString(new Broken());
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_SERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().endsWith(
          "Exception during JSON serialization. Unexpected IOException (of type java.io.IOException): I'm so broken!"));
    }

    // Test serialization of binary content.
    Image lImage = new Image();
    lImage.setContent(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
    lJSON = tools.writeObjectToString(lImage);
    assertEquals("{\"content\":\"AQIDBAUGBwgJ\"}", lJSON);
    Image lDeserializedObject = tools.read(lJSON, Image.class);
    assertTrue(Arrays.equals(lImage.getContent(), lDeserializedObject.getContent()));
  }

  @Test
  void testCollectionToStringSerialization( ) {
    String lJSON = tools.writeObjectsToString(products, Product.class);
    assertEquals(
        "[{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]},{\"id\":815,\"name\":\"Boring Product\",\"resellers\":[1234]}]",
        lJSON);

    lJSON = tools.writeObjectsToString(null, Product.class);
    assertEquals("null", lJSON);

    try {
      tools.writeObjectsToString(products, null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectType must not be NULL.", e.getMessage());
    }

    List<Broken> lBrokenObjects = new ArrayList<>();
    lBrokenObjects.add(new Broken());
    lBrokenObjects.add(new Broken());
    try {
      tools.writeObjectsToString(lBrokenObjects, Broken.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_SERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().endsWith(
          "Exception during JSON serialization. Unexpected IOException (of type java.io.IOException): I'm so broken!"));
    }
  }

  @Test
  void testBytesSerialization( ) {
    byte[] lJSON = tools.writeObjectToBytes(product1);

    assertTrue(Arrays
        .equals("{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]}"
            .getBytes(), lJSON));

    lJSON = tools.writeObjectToBytes(null);
    assertTrue(Arrays.equals("null".getBytes(), lJSON));

    try {
      tools.writeObjectToBytes(new Broken());
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_SERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().endsWith(
          "Exception during JSON serialization. Unexpected IOException (of type java.io.IOException): I'm so broken!"));
    }
  }

  @Test
  void testCollectionToBytesSerialization( ) {
    byte[] lJSON = tools.writeObjectsToBytes(products, Product.class);
    assertTrue(Arrays.equals(
        "[{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]},{\"id\":815,\"name\":\"Boring Product\",\"resellers\":[1234]}]"
            .getBytes(),
        lJSON));

    lJSON = tools.writeObjectsToBytes(null, Product.class);
    assertTrue(Arrays.equals("null".getBytes(), lJSON));

    try {
      tools.writeObjectsToBytes(products, null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectType must not be NULL.", e.getMessage());
    }

    List<Broken> lBrokenObjects = new ArrayList<>();
    lBrokenObjects.add(new Broken());
    lBrokenObjects.add(new Broken());
    try {
      tools.writeObjectsToBytes(lBrokenObjects, Broken.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_SERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().endsWith(
          "Exception during JSON serialization. Unexpected IOException (of type java.io.IOException): I'm so broken!"));
    }
  }

  @Test
  void testOutputStreamSerialization( ) {
    ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
    tools.writeObject(product1, lOutputStream);
    byte[] lJSON = lOutputStream.toByteArray();

    assertTrue(Arrays
        .equals("{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]}"
            .getBytes(), lJSON));

    lOutputStream = new ByteArrayOutputStream();
    tools.writeObject(null, lOutputStream);
    lJSON = lOutputStream.toByteArray();
    assertTrue(Arrays.equals("null".getBytes(), lJSON));

    lOutputStream = new ByteArrayOutputStream();
    try {
      tools.writeObject(new Broken(), lOutputStream);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_SERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().endsWith("Exception during JSON serialization. I'm so broken!"));
    }

    try {
      tools.writeObject(product1, (OutputStream) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pOutputStream must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testCollectionToOutputStreamSerialization( ) {
    ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
    tools.writeObjects(products, lOutputStream, Product.class);
    byte[] lJSON = lOutputStream.toByteArray();
    assertTrue(Arrays.equals(
        "[{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]},{\"id\":815,\"name\":\"Boring Product\",\"resellers\":[1234]}]"
            .getBytes(),
        lJSON));

    lOutputStream = new ByteArrayOutputStream();
    tools.writeObjects(null, lOutputStream, Product.class);
    lJSON = lOutputStream.toByteArray();

    assertTrue(Arrays.equals("null".getBytes(), lJSON));

    try {
      tools.writeObjects(products, (OutputStream) null, Product.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pOutputStream must not be NULL.", e.getMessage());
    }

    try {
      tools.writeObjects(products, lOutputStream, null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectType must not be NULL.", e.getMessage());
    }

    List<Broken> lBrokenObjects = new ArrayList<>();
    lBrokenObjects.add(new Broken());
    lBrokenObjects.add(new Broken());
    try {
      tools.writeObjects(lBrokenObjects, lOutputStream, Broken.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_SERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().endsWith("Exception during JSON serialization. I'm so broken!"));
    }
  }

  @Test
  void testWriterSerialization( ) {
    ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
    OutputStreamWriter lWriter = new OutputStreamWriter(lOutputStream);
    tools.writeObject(product1, lWriter);
    byte[] lJSON = lOutputStream.toByteArray();

    assertTrue(Arrays
        .equals("{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]}"
            .getBytes(), lJSON));

    lOutputStream = new ByteArrayOutputStream();
    lWriter = new OutputStreamWriter(lOutputStream);
    tools.writeObject(null, lWriter);
    lJSON = lOutputStream.toByteArray();
    assertTrue(Arrays.equals("null".getBytes(), lJSON));

    lOutputStream = new ByteArrayOutputStream();
    lWriter = new OutputStreamWriter(lOutputStream);
    try {
      tools.writeObject(new Broken(), lWriter);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_SERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().endsWith("Exception during JSON serialization. I'm so broken!"));
    }

    try {
      tools.writeObject(product1, (Writer) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pWriter must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testCollectionToWriterSerialization( ) {
    ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
    OutputStreamWriter lWriter = new OutputStreamWriter(lOutputStream);
    tools.writeObjects(products, lWriter, Product.class);
    byte[] lJSON = lOutputStream.toByteArray();
    assertTrue(Arrays.equals(
        "[{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]},{\"id\":815,\"name\":\"Boring Product\",\"resellers\":[1234]}]"
            .getBytes(),
        lJSON));

    lOutputStream = new ByteArrayOutputStream();
    lWriter = new OutputStreamWriter(lOutputStream);
    tools.writeObjects(null, lWriter, Product.class);
    lJSON = lOutputStream.toByteArray();

    assertTrue(Arrays.equals("null".getBytes(), lJSON));

    try {
      tools.writeObjects(products, (Writer) null, Product.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pWriter must not be NULL.", e.getMessage());
    }

    try {
      tools.writeObjects(products, lWriter, null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectType must not be NULL.", e.getMessage());
    }

    List<Broken> lBrokenObjects = new ArrayList<>();
    lBrokenObjects.add(new Broken());
    lBrokenObjects.add(new Broken());
    try {
      tools.writeObjects(lBrokenObjects, lWriter, Broken.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_SERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().endsWith("Exception during JSON serialization. I'm so broken!"));
    }
  }

  @Test
  void testFileSerialization( ) throws IOException {
    File lFile = File.createTempFile("jeaf", ".json.test");
    tools.writeObject(product1, lFile);
    FileTools lFileTools = FileTools.getFileTools();
    String lJSON = lFileTools.getFileContentAsString(lFile);

    assertEquals("{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]}",
        lJSON);

    lFile = File.createTempFile("jeaf", ".json.test");
    tools.writeObject(null, lFile);
    lJSON = lFileTools.getFileContentAsString(lFile);
    assertEquals("null", lJSON);

    lFile = File.createTempFile("jeaf", ".json.test");
    try {
      tools.writeObject(new Broken(), lFile);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_SERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().endsWith("Exception during JSON serialization. I'm so broken!"));
    }

    try {
      tools.writeObject(product1, (File) null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFile must not be NULL.", e.getMessage());
    }
  }

  @Test
  void testCollectionToFileSerialization( ) throws IOException {
    File lFile = File.createTempFile("jeaf", ".json.test");
    tools.writeObjects(products, lFile, Product.class);
    FileTools lFileTools = FileTools.getFileTools();
    String lJSON = lFileTools.getFileContentAsString(lFile);
    assertEquals(
        "[{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]},{\"id\":815,\"name\":\"Boring Product\",\"resellers\":[1234]}]",
        lJSON);

    lFile = File.createTempFile("jeaf", ".json.test");
    tools.writeObjects(null, lFile, Product.class);
    lJSON = lFileTools.getFileContentAsString(lFile);
    assertEquals("null", lJSON);

    try {
      tools.writeObjects(products, (File) null, Product.class);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pFile must not be NULL.", e.getMessage());
    }

    try {
      tools.writeObjects(products, lFile, null);
      fail();
    }
    catch (InvalidParameterException e) {
      assertEquals("Check failed. pObjectType must not be NULL.", e.getMessage());
    }

    List<Broken> lBrokenObjects = new ArrayList<>();
    lBrokenObjects.add(new Broken());
    lBrokenObjects.add(new Broken());
    try {
      tools.writeObjects(lBrokenObjects, lFile, Broken.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.JSON_SERIALIZATION_FAILED, e.getErrorCode());
      assertTrue(e.getMessage().endsWith("Exception during JSON serialization. I'm so broken!"));
    }
  }

  @Test
  void testPrimitiveTypeSerilization( ) {
    String lJSON = tools.writeObjectToString("Hello");
    assertEquals("\"Hello\"", lJSON);

    lJSON = tools.writeObjectToString(4711);
    assertEquals("4711", lJSON);
  }

}
