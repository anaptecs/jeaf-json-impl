/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test.inheritance;

public class PostalAddress {
  private String street;

  private String houseNumber;

  private String city;

  private int postalCode;

  public String getStreet( ) {
    return street;
  }

  public void setStreet( String pStreet ) {
    street = pStreet;
  }

  public String getHouseNumber( ) {
    return houseNumber;
  }

  public void setHouseNumber( String pHouseNumber ) {
    houseNumber = pHouseNumber;
  }

  public String getCity( ) {
    return city;
  }

  public void setCity( String pCity ) {
    city = pCity;
  }

  public int getPostalCode( ) {
    return postalCode;
  }

  public void setPostalCode( int pPostalCode ) {
    postalCode = pPostalCode;
  }
}
