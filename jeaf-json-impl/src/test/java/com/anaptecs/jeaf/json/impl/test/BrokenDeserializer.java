/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import com.anaptecs.jeaf.json.impl.test.domain.Broken;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

public class BrokenDeserializer extends StdDeserializer<Broken> {

  public BrokenDeserializer( ) {
    super(Broken.class);
  }

  @Override
  public Broken deserialize(JsonParser pP, DeserializationContext pCtxt) {
    throw new RuntimeException("I'm so broken!");
  }

}
