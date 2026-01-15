package com.anaptecs.jeaf.json.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.anaptecs.jeaf.json.api.JSON;
import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.json.impl.test.readonly.ReadOnlyClient;
import com.anaptecs.jeaf.json.impl.test.readonly.ReadOnlyMaster;
import org.junit.jupiter.api.Test;

public class ReadonlyTest {
  @Test
  void testTransientBackReferenceForReadonlyClasses( ) {
    final JSONTools lJSONTools = JSON.getJSONTools();

    ReadOnlyClient lClient1 = ReadOnlyClient.builder().setName("Client 1").build();
    ReadOnlyClient lClient2 = ReadOnlyClient.builder().setName("Client 2").build();
    ReadOnlyMaster lMaster = ReadOnlyMaster.builder().setName("Master 1").addToClients(lClient1, lClient2).build();

    assertEquals(lClient1, lMaster.getClients().get(0));
    assertEquals(lClient2, lMaster.getClients().get(1));
    assertEquals(lMaster, lClient1.getTransientMaster());
    assertEquals(lMaster, lClient2.getTransientMaster());

    String lAsString = lMaster.toString().trim();
    assertEquals("com.anaptecs.jeaf.json.impl.test.readonly.ReadOnlyMaster" + System.lineSeparator()
        + "name: Master 1" + System.lineSeparator() + "clients: 2 element(s)" + System.lineSeparator()
        + "    com.anaptecs.jeaf.json.impl.test.readonly.ReadOnlyClient" + System.lineSeparator()
        + "    name: Client 1" + System.lineSeparator() + System.lineSeparator()
        + "    com.anaptecs.jeaf.json.impl.test.readonly.ReadOnlyClient" + System.lineSeparator()
        + "    name: Client 2", lAsString);

    String lJSON = lJSONTools.writeObjectToString(lMaster);
    assertEquals("{\"name\":\"Master 1\",\"clients\":[{\"name\":\"Client 1\"},{\"name\":\"Client 2\"}]}", lJSON);

    ReadOnlyMaster lReadObject = lJSONTools.read(lJSON, ReadOnlyMaster.class);
    assertEquals("Master 1", lReadObject.getName());
    assertEquals("Client 1", lReadObject.getClients().get(0).getName());
    assertEquals(lReadObject, lReadObject.getClients().get(0).getTransientMaster());
    assertEquals("Client 2", lReadObject.getClients().get(1).getName());
    assertEquals(lReadObject, lReadObject.getClients().get(1).getTransientMaster());
  }
}
