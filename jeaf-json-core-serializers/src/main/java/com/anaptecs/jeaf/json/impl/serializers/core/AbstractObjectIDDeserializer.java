/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.core;

import com.anaptecs.jeaf.core.api.ServiceObjectID;
import com.anaptecs.jeaf.json.api.JSONMessages;
import com.anaptecs.jeaf.xfun.api.common.AbstractObjectID;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.node.StringNode;

/**
 * Class implements a generic JSON deserializer for {@link AbstractObjectID} implementations.
 *
 * @author JEAF Development Team
 */
public abstract class AbstractObjectIDDeserializer<T extends AbstractObjectID<?>> extends ValueDeserializer<T> {
  /**
   * Regex is used to separate objectID and version label part of an serialized objectID.
   */
  private static final String REGEX = "\\" + AbstractObjectIDSerializer.VERSION_LABEL_DELIMITER;

  /**
   * Method reads version info data from the JSON data and creates a {@link ServiceObjectID} object out of it.
   *
   * @param pParser JSON parser. The parameter must not be null.
   * @param pContext Context information. The parameter is not used by this implementation.
   */
  @Override
  public final T deserialize(JsonParser pParser, DeserializationContext pContext) {
    // Parse JSON content.
    JsonNode lNode = pParser.objectReadContext().readTree(pParser);

    // We expect that objectIDs are always serialized as plain text which will result in a TextNode.
    if (lNode instanceof StringNode) {
      // Get serialized version of objectID.
      String lSerializedID = lNode.asString();

      // Try to resolve may be existing version label
      String[] lSplit = lSerializedID.split(REGEX);
      int lLength = lSplit.length;
      Integer lVersionLabel;
      if (lLength == 2) {
        lSerializedID = lSplit[0];
        lVersionLabel = Integer.valueOf(lSplit[1]);
      }
      // Nothing to do. "objectID" remains as it is
      else {
        lVersionLabel = null;
      }
      // Create new ObjectID and return it.
      return this.createObjectID(lSerializedID, lVersionLabel);
    }
    // objectID is not present as TextNode
    else {
      throw new JEAFSystemException(JSONMessages.UNEXPECTED_NODE_TYPE_FOR_OBJECT_ID_DESERIAL, lNode.toString(),
          lNode.getClass().getName());
    }
  }

  /**
   * Abstract method needs to be implemented by concrete subclasses in oder to create a concrete object ID
   * implementation based on the passed objectID and version label.
   *
   * @param pObjectId String representation of the object ID. The parameter must not be null.
   * @param pVersionLabel Version label the belongs to the object ID. The parameter may be null.
   * @return T Created ObjectID object. The method must not return null.
   */
  protected abstract T createObjectID(String pObjectId, Integer pVersionLabel);
}
