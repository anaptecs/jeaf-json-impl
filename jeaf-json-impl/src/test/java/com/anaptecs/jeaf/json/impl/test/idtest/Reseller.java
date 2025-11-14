/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test.idtest;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
// @JsonDeserialize(builder = Reseller.Builder.class)
public class Reseller {
  private Reseller( ) {
  }

  private Reseller( Builder pBuilder ) {
    id = pBuilder.id;
    name = pBuilder.name;
  }

  private long id;

  private String name;

  public long getId( ) {
    return id;
  }

  public String getName( ) {
    return name;
  }

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
  public static class Builder {
    public Builder( ) {
    }

    public Builder( long pID ) {
      id = pID;
    }

    private long id;

    private String name;

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

    public Reseller build( ) {
      return new Reseller(this);
    }
  }
}
