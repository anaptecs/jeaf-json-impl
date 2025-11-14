/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test.inheritance;

public class Person extends Partner {
  private String surname;

  private String firstName;

  public String getSurname( ) {
    return surname;
  }

  public void setSurname( String pSurname ) {
    surname = pSurname;
  }

  public String getFirstName( ) {
    return firstName;
  }

  public void setFirstName( String pFirstName ) {
    firstName = pFirstName;
  }

}
