/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test.inheritance;

public class Company extends Partner {
  private String name;

  public String getName( ) {
    return name;
  }

  public void setName( String pName ) {
    name = pName;
  }

}
