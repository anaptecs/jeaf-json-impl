/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.core;

import com.anaptecs.jeaf.xfun.api.common.AbstractObjectID;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class AbstractObjectIDSerializer extends ValueSerializer<AbstractObjectID<?>> {
  /**
   * Delimiter is used to separate objectID and version label of an {@link AbstractObjectID}
   */
  public static final String VERSION_LABEL_DELIMITER = "|";

  /**
   * Method serializes any kind of {@link AbstractObjectID} implementation to a simple pure string based JSON
   * representation.
   */
  @Override
  public void serialize(AbstractObjectID<?> pValue, JsonGenerator pGenerator, SerializationContext pSerializers) {
    StringBuilder lBuilder = new StringBuilder();
    lBuilder.append(pValue.getObjectID());
    if (pValue.isVersioned()) {
      lBuilder.append(VERSION_LABEL_DELIMITER);
      lBuilder.append(pValue.getVersionLabel());
    }
    pGenerator.writeString(lBuilder.toString());
  }
}
