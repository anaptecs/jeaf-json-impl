/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class POJO {
  private String name;

  private AttackedObject attackedObject = new AttackedObject();

  public String getName( ) {
    return name;
  }

  public void setName( String pName ) {
    name = pName;
  }

  public AttackedObject getAttackedObject( ) {
    return attackedObject;
  }

  public void setAttackedObject( AttackedObject pAttackedObject ) {
    attackedObject = pAttackedObject;
  }
}
