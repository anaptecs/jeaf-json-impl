/**
 * 2022-01-19 17:02:45.551 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.xfun;

import java.util.Date;

import com.anaptecs.jeaf.json.impl.AbstractDeserializer;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class implements custom JSON deserializer for {@link VersionInfo}.
 * 
 * @author JEAF Development Team
 */
public class VersionInfoDeserializer extends AbstractDeserializer<VersionInfo> {
  private static final long serialVersionUID = 1L;

  /**
   * Initialize object.
   */
  public VersionInfoDeserializer( ) {
    super(VersionInfo.class);
  }

  /**
   * Method reads version info data from the JSON data and creates a {@link VersionInfo} object out of it.
   * 
   * @param pParser JSON parser. The parameter must not be null.
   * @param pContext Context information. The parameter is not used by this implementation.
   */
  @Override
  public VersionInfo deserialize( ObjectNode pObjectNode ) {
    // Resolve unknown version flag
    boolean lUnknownVersion;
    JsonNode lUnknownVersionValueNode = pObjectNode.get(VersionInfoSerializer.UNKNWON_VERSION_FLAG);
    if (lUnknownVersionValueNode != null && lUnknownVersionValueNode.isNull() == false) {
      lUnknownVersion = lUnknownVersionValueNode.asBoolean();
    }
    else {
      lUnknownVersion = false;
    }

    // Create real version info object
    VersionInfo lVersionInfo;
    if (lUnknownVersion == false) {

      // Resolve version
      String lVersion = this.getStringValueFromNode(pObjectNode, VersionInfoSerializer.VERSION, null);

      // Resolve date
      Date lReleaseDate = this.getDateValueFromNode(pObjectNode, VersionInfoSerializer.DATE, null);

      // Create version info object based on the available information
      lVersionInfo = new VersionInfo(lVersion, lReleaseDate);
    }
    // Version is unknown
    else {
      lVersionInfo = VersionInfo.UNKNOWN_VERSION;
    }

    return lVersionInfo;
  }

}
