/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test.idtest;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
public class Product {
  private long id;

  private String name;

  private List<Reseller> resellers = new ArrayList<>();

  public long getId( ) {
    return id;
  }

  public void setId( long pId ) {
    id = pId;
  }

  public String getName( ) {
    return name;
  }

  public void setName( String pName ) {
    name = pName;
  }

  public void addReseller( Reseller pReseller ) {
    resellers.add(pReseller);
  }

  public List<Reseller> getResellers( ) {
    return resellers;
  }

}
