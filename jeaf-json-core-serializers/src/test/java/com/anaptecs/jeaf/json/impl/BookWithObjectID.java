/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl;

import com.anaptecs.jeaf.xfun.api.common.ObjectID;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

@JsonIdentityInfo(property = "oid", generator = PropertyGenerator.class)
public class BookWithObjectID {
  public ObjectID oid;

  public String title;

  public String author;
}
