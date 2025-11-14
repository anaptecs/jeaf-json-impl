/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.core;

import java.io.IOException;

import com.anaptecs.jeaf.xfun.api.common.AbstractObjectID;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class AbstractObjectIDSerializer extends JsonSerializer<AbstractObjectID<?>> {
  /**
   * Delimiter is used to separate objectID and version label of an {@link AbstractObjectID}
   */
  public static final String VERSION_LABEL_DELIMITER = "|";

  /**
   * Method serializes any kind of {@link AbstractObjectID} implementation to a simple pure string based JSON
   * representation.
   */
  @Override
  public void serialize( AbstractObjectID<?> pValue, JsonGenerator pGenerator, SerializerProvider pSerializers )
    throws IOException {
    StringBuilder lBuilder = new StringBuilder();
    lBuilder.append(pValue.getObjectID());
    if (pValue.isVersioned()) {
      lBuilder.append(VERSION_LABEL_DELIMITER);
      lBuilder.append(pValue.getVersionLabel());
    }
    pGenerator.writeString(lBuilder.toString());
  }
}
