/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.xfun;

import com.anaptecs.jeaf.json.impl.AbstractDeserializer;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedObject;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedString;
import tools.jackson.databind.node.ObjectNode;

/**
 * Class implements custom JSON deserializer for {@link LocalizedString}.
 *
 * @author JEAF Development Team
 */
public class LocalizedStringDeserializer extends AbstractDeserializer<LocalizedString> {
  /**
   * Initialize object.
   */
  protected LocalizedStringDeserializer( ) {
    super(LocalizedObject.class);
  }

  @Override
  public LocalizedString deserialize(ObjectNode pObjectNode) {
    // Resolve localizationID
    int lLocalizationID = this.getIntegerValueFromNode(pObjectNode, "localizationID", -1);

    return new LocalizedString(lLocalizationID);
  }

}
