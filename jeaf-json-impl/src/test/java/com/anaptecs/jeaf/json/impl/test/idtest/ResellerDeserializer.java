/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test.idtest;

import com.anaptecs.jeaf.json.impl.AbstractDeserializer;
import tools.jackson.databind.node.ObjectNode;

public class ResellerDeserializer extends AbstractDeserializer<Reseller> {
  protected ResellerDeserializer( ) {
    super(Reseller.class);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Reseller deserialize(ObjectNode pObjectNode) {
    return null;
  }
}
