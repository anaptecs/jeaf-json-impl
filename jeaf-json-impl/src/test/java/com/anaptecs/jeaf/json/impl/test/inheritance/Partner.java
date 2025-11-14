/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test.inheritance;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

@JsonIgnoreProperties(value = "objectType")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "objectType", visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(value = Company.class, name = "Company"),
  @JsonSubTypes.Type(value = Person.class, name = "Person"),
  @JsonSubTypes.Type(value = Partner.class, name = "Partner") })
@JsonIdentityInfo(property = "objectID", generator = PropertyGenerator.class)
public class Partner {
  private long objectID;

  private List<PostalAddress> postalAddresses = new ArrayList<>();

  public long getObjectID( ) {
    return objectID;
  }

  public void setObjectID( long pObjectID ) {
    objectID = pObjectID;
  }

  public List<PostalAddress> getPostalAddresses( ) {
    return postalAddresses;
  }

  public void addPostalAddress( PostalAddress pPostalAddress ) {
    postalAddresses.add(pPostalAddress);
  }
}
