/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import com.anaptecs.jeaf.core.api.ServiceObjectID;
import com.anaptecs.jeaf.json.api.JSONMessages;
import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.Test;

class ServiceObjectIDTest {

  @Test
  void testServiceObjectIDSerialization( ) throws JsonProcessingException {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    // Try to serialize and deserialize ServiceObjectID
    ServiceObjectID lServiceObjectID = new ServiceObjectID("ABC123", 2);
    String lJSON = lObjectMapper.writeValueAsString(lServiceObjectID);
    assertEquals("\"ABC123|2\"", lJSON);

    ServiceObjectID lReadServiceObjectID = lObjectMapper.readValue(lJSON, ServiceObjectID.class);
    assertEquals(lServiceObjectID.getObjectID(), lReadServiceObjectID.getObjectID());
    assertEquals(lServiceObjectID.getVersionLabel(), lReadServiceObjectID.getVersionLabel());

    // Serialize and deserialize unversioned ServiceObjectID.
    ServiceObjectID lUnversionedObjectID = lServiceObjectID.getUnversionedObjectID();
    assertEquals(lServiceObjectID.getObjectID(), lUnversionedObjectID.getObjectID());
    assertEquals(null, lUnversionedObjectID.getVersionLabel());
    lJSON = lObjectMapper.writeValueAsString(lUnversionedObjectID);
    assertEquals("\"ABC123\"", lJSON);

    lReadServiceObjectID = lObjectMapper.readValue(lJSON, ServiceObjectID.class);
    assertEquals(lServiceObjectID.getObjectID(), lReadServiceObjectID.getObjectID());
    assertEquals(null, lReadServiceObjectID.getVersionLabel());

    // Test missing objectID.
    try {
      lJSON = "{\"versionLabel\":4711}";
      lObjectMapper.readValue(lJSON, ServiceObjectID.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.UNEXPECTED_NODE_TYPE_FOR_OBJECT_ID_DESERIAL, e.getErrorCode());
      assertTrue(e.getMessage().endsWith(
          "1802] Only TextNodes are expected to be processed by this deserializer. Current JSON node '{\"versionLabel\":4711}' is of type 'com.fasterxml.jackson.databind.node.ObjectNode'."));
    }
  }

  @Test
  void testObjectSerialization( ) throws JsonMappingException, JsonProcessingException {
    Book lBook = new Book();
    lBook.oid = new ServiceObjectID("123456789", 4711);
    lBook.title = "Maus";
    lBook.author = "Art Spiegelman";
    JSONTools lTools = JSONTools.getJSONTools();
    String lJSON = lTools.writeObjectToString(lBook);
    assertEquals("{\"oid\":\"123456789|4711\",\"title\":\"Maus\",\"author\":\"Art Spiegelman\"}", lJSON);

    List<Book> lBooks = new ArrayList<>();
    lBooks.add(lBook);
    lBooks.add(lBook);

    lJSON = lTools.writeObjectToString(lBooks);
    assertEquals("[{\"oid\":\"123456789|4711\",\"title\":\"Maus\",\"author\":\"Art Spiegelman\"},\"123456789|4711\"]",
        lJSON);

    ObjectMapper lObjectMapper = lTools.getDefaultObjectMapper();
    CollectionType lType = lObjectMapper.getTypeFactory().constructCollectionType(List.class, Book.class);

    lBooks = lObjectMapper.readValue(lJSON, lType);
    assertEquals(2, lBooks.size());
    assertEquals("123456789", lBooks.get(0).oid.getObjectID());
    assertEquals(4711, lBooks.get(0).oid.getVersionLabel());
    assertEquals("Art Spiegelman", lBooks.get(0).author);
    assertEquals("Maus", lBooks.get(0).title);
    assertSame(lBooks.get(0), lBooks.get(1));
  }
}
