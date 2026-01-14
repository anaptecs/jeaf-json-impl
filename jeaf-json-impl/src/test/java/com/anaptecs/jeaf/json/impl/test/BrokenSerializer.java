/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import com.anaptecs.jeaf.json.impl.test.domain.Broken;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

public class BrokenSerializer extends StdSerializer<Broken> {
  public BrokenSerializer( ) {
    super(Broken.class);
  }

  @Override
  public void serialize(Broken pObject, JsonGenerator pGenerator, SerializationContext pProvider) {
    throw new RuntimeException("I'm so broken!");
  }
}
