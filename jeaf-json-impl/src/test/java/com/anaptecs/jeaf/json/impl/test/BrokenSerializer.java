/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import java.io.IOException;

import com.anaptecs.jeaf.json.impl.test.domain.Broken;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class BrokenSerializer extends StdSerializer<Broken> {
  private static final long serialVersionUID = 1L;

  public BrokenSerializer( ) {
    super(Broken.class);
  }

  @Override
  public void serialize( Broken pObject, JsonGenerator pGenerator, SerializerProvider pProvider ) throws IOException {
    throw new IOException("I'm so broken!");
  }
}
