/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import java.io.IOException;

import com.anaptecs.jeaf.json.impl.test.domain.Broken;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class BrokenDeserializer extends StdDeserializer<Broken> {
  private static final long serialVersionUID = 1L;

  public BrokenDeserializer( ) {
    super(Broken.class);
  }

  @Override
  public Broken deserialize( JsonParser pP, DeserializationContext pCtxt ) throws IOException, JacksonException {
    throw new IOException("I'm so broken!");
  }

}
