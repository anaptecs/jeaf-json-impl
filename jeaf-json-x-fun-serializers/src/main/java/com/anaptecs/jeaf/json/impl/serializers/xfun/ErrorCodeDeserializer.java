/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.xfun;

import com.anaptecs.jeaf.json.impl.AbstractDeserializer;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import tools.jackson.databind.node.ObjectNode;

/**
 * Class implements custom JSON deserializer for {@link ErrorCode}.
 *
 * @author JEAF Development Team
 */
public class ErrorCodeDeserializer extends AbstractDeserializer<ErrorCode> {
  /**
   * Initialize object.
   */
  public ErrorCodeDeserializer( ) {
    super(MessageID.class);
  }

  @Override
  public ErrorCode deserialize(ObjectNode pObjectNode) {

    // Resolve localizationID
    int lLocalizationID = this.getIntegerValueFromNode(pObjectNode, "localizationID", -1);

    // Resolve trace level.
    String lTraceLevel = this.getStringValueFromNode(pObjectNode, "traceLevel", TraceLevel.ERROR.name());

    // Create MessageID and return it.
    return new ErrorCode(lLocalizationID, TraceLevel.valueOf(lTraceLevel));
  }
}
