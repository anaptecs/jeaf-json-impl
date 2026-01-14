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

import com.anaptecs.jeaf.json.api.JSONMessages;
import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.xfun.api.common.ObjectID;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.CollectionType;

class ObjectIDTest {

  @Test
  void testObjectIDSerialization( ) {
    ObjectMapper lObjectMapper = JSONTools.getJSONTools().getDefaultObjectMapper();
    // Try to serialize and deserialize ObjectID
    ObjectID lObjectID = new ObjectID("ABC123", 2);
    String lJSON = lObjectMapper.writeValueAsString(lObjectID);
    assertEquals("\"ABC123|2\"", lJSON);

    ObjectID lReadObjectID = lObjectMapper.readValue(lJSON, ObjectID.class);
    assertEquals(lObjectID.getObjectID(), lReadObjectID.getObjectID());
    assertEquals(lObjectID.getVersionLabel(), lReadObjectID.getVersionLabel());

    // Serialize and deserialize unversioned ObjectID.
    ObjectID lUnversionedObjectID = lObjectID.getUnversionedObjectID();
    assertEquals(lObjectID.getObjectID(), lUnversionedObjectID.getObjectID());
    assertEquals(null, lUnversionedObjectID.getVersionLabel());
    lJSON = lObjectMapper.writeValueAsString(lUnversionedObjectID);
    assertEquals("\"ABC123\"", lJSON);

    lReadObjectID = lObjectMapper.readValue(lJSON, ObjectID.class);
    assertEquals(lObjectID.getObjectID(), lReadObjectID.getObjectID());
    assertEquals(null, lReadObjectID.getVersionLabel());

    // Test missing objectID.
    try {
      lJSON = "{\"versionLabel\":4711}";
      lObjectMapper.readValue(lJSON, ObjectID.class);
      fail();
    }
    catch (JEAFSystemException e) {
      assertEquals(JSONMessages.UNEXPECTED_NODE_TYPE_FOR_OBJECT_ID_DESERIAL, e.getErrorCode());
      assertTrue(e.getMessage().endsWith(
          "1802] Only TextNodes are expected to be processed by this deserializer. Current JSON node '{\"versionLabel\":4711}' is of type 'tools.jackson.databind.node.ObjectNode'."));
    }
  }

  @Test
  void testObjectSerialization( ) {
    BookWithObjectID lBook = new BookWithObjectID();
    lBook.oid = new ObjectID("123456789", 4711);
    lBook.title = "Maus";
    lBook.author = "Art Spiegelman";
    JSONTools lTools = JSONTools.getJSONTools();
    String lJSON = lTools.writeObjectToString(lBook);
    assertEquals("{\"oid\":\"123456789|4711\",\"title\":\"Maus\",\"author\":\"Art Spiegelman\"}", lJSON);

    List<BookWithObjectID> lBooks = new ArrayList<>();
    lBooks.add(lBook);
    lBooks.add(lBook);

    lJSON = lTools.writeObjectToString(lBooks);
    assertEquals("[{\"oid\":\"123456789|4711\",\"title\":\"Maus\",\"author\":\"Art Spiegelman\"},\"123456789|4711\"]",
        lJSON);

    ObjectMapper lObjectMapper = lTools.getDefaultObjectMapper();
    CollectionType lType = lObjectMapper.getTypeFactory().constructCollectionType(List.class, BookWithObjectID.class);

    lBooks = lObjectMapper.readValue(lJSON, lType);
    assertEquals(2, lBooks.size());
    assertEquals("123456789", lBooks.get(0).oid.getObjectID());
    assertEquals(4711, lBooks.get(0).oid.getVersionLabel());
    assertEquals("Art Spiegelman", lBooks.get(0).author);
    assertEquals("Maus", lBooks.get(0).title);
    assertSame(lBooks.get(0), lBooks.get(1));
  }
}
