/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.json.impl.test.inheritance.Company;
import com.anaptecs.jeaf.json.impl.test.inheritance.Partner;
import com.anaptecs.jeaf.json.impl.test.inheritance.PartnerContainer;
import com.anaptecs.jeaf.json.impl.test.inheritance.Person;
import com.anaptecs.jeaf.json.impl.test.inheritance.PostalAddress;
import org.junit.jupiter.api.Test;

class InheritanceSerializationTest {
  private final JSONTools tools = JSONTools.getJSONTools();

  @Test
  void testInheritanceSerialization( ) {
    PostalAddress lAddress = new PostalAddress();
    lAddress.setStreet("Ricard-Huch-Str.");
    lAddress.setHouseNumber("71");
    lAddress.setCity("Reutlingen");
    lAddress.setPostalCode(72760);

    Company lCompany = new Company();
    lCompany.setObjectID(123456);
    lCompany.setName("anaptecs GmbH");
    lCompany.addPostalAddress(lAddress);
    String lJSON = tools.writeObjectToString(lCompany);
    assertEquals(
        "{\"objectType\":\"Company\",\"objectID\":123456,\"postalAddresses\":[{\"street\":\"Ricard-Huch-Str.\",\"houseNumber\":\"71\",\"city\":\"Reutlingen\",\"postalCode\":72760}],\"name\":\"anaptecs GmbH\"}",
        lJSON);

    Person lDonald = new Person();
    lDonald.setObjectID(9876);
    lDonald.setFirstName("Donald");
    lDonald.setSurname("Duck");

    Partner lPartner = new Partner();
    lPartner.setObjectID(4711);

    List<Partner> lPartners = new ArrayList<>();
    lPartners.add(lDonald);
    lPartners.add(lCompany);
    lPartners.add(lPartner);

    Partner[] lArray = lPartners.toArray(new Partner[0]);
    lJSON = tools.writeObjectToString(lArray);
    assertEquals(
        "[{\"objectType\":\"Person\",\"objectID\":9876,\"surname\":\"Duck\",\"firstName\":\"Donald\"},{\"objectType\":\"Company\",\"objectID\":123456,\"postalAddresses\":[{\"street\":\"Ricard-Huch-Str.\",\"houseNumber\":\"71\",\"city\":\"Reutlingen\",\"postalCode\":72760}],\"name\":\"anaptecs GmbH\"},{\"objectType\":\"Partner\",\"objectID\":4711}]",
        lJSON);

    lJSON = tools.writeObjectsToString(lPartners, Partner.class);
    assertEquals(
        "[{\"objectType\":\"Person\",\"objectID\":9876,\"surname\":\"Duck\",\"firstName\":\"Donald\"},{\"objectType\":\"Company\",\"objectID\":123456,\"postalAddresses\":[{\"street\":\"Ricard-Huch-Str.\",\"houseNumber\":\"71\",\"city\":\"Reutlingen\",\"postalCode\":72760}],\"name\":\"anaptecs GmbH\"},{\"objectType\":\"Partner\",\"objectID\":4711}]",
        lJSON);

    PartnerContainer lContainer = new PartnerContainer();
    lContainer.addPartner(lDonald);
    lContainer.addPartner(lCompany);
    lContainer.addPartner(lPartner);

    lJSON = tools.writeObjectToString(lContainer);
    assertEquals(
        "{\"partners\":[{\"objectType\":\"Person\",\"objectID\":9876,\"surname\":\"Duck\",\"firstName\":\"Donald\"},{\"objectType\":\"Company\",\"objectID\":123456,\"postalAddresses\":[{\"street\":\"Ricard-Huch-Str.\",\"houseNumber\":\"71\",\"city\":\"Reutlingen\",\"postalCode\":72760}],\"name\":\"anaptecs GmbH\"},{\"objectType\":\"Partner\",\"objectID\":4711}]}",
        lJSON);

    // Test handling of duplicated objects.
    lPartners.add(lDonald);
    lJSON = tools.writeObjectsToString(lPartners, Partner.class);
    assertEquals(
        "[{\"objectType\":\"Person\",\"objectID\":9876,\"surname\":\"Duck\",\"firstName\":\"Donald\"},{\"objectType\":\"Company\",\"objectID\":123456,\"postalAddresses\":[{\"street\":\"Ricard-Huch-Str.\",\"houseNumber\":\"71\",\"city\":\"Reutlingen\",\"postalCode\":72760}],\"name\":\"anaptecs GmbH\"},{\"objectType\":\"Partner\",\"objectID\":4711},9876]",
        lJSON);

  }

  @Test
  void testInheritanceDeserialization( ) {
    String lJSON =
        "[{\"objectType\":\"Person\",\"objectID\":9876,\"postalAddresses\":[],\"surname\":\"Duck\",\"firstName\":\"Donald\"},{\"objectType\":\"Company\",\"objectID\":123456,\"postalAddresses\":[{\"street\":\"Ricard-Huch-Str.\",\"houseNumber\":\"71\",\"city\":\"Reutlingen\",\"postalCode\":72760}],\"name\":\"anaptecs GmbH\"},{\"objectType\":\"Partner\",\"objectID\":4711,\"postalAddresses\":[]},9876]";

    Partner[] lPartnerArray = tools.read(lJSON, Partner[].class);
    assertEquals(4, lPartnerArray.length);
    assertEquals(Person.class, lPartnerArray[0].getClass());

    List<Partner> lPartners = tools.readToCollection(lJSON, List.class, Partner.class);
    assertEquals(4, lPartners.size());
    Person lDonald = (Person) lPartners.get(0);
    assertEquals(9876, lDonald.getObjectID());
    assertEquals("Duck", lDonald.getSurname());
    assertEquals("Donald", lDonald.getFirstName());
  }

}
