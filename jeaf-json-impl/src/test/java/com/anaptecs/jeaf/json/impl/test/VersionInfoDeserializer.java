/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import java.util.Date;

import com.anaptecs.jeaf.json.impl.AbstractDeserializer;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class VersionInfoDeserializer extends AbstractDeserializer<VersionInfo> {
  private static final long serialVersionUID = 1L;

  public VersionInfoDeserializer( ) {
    super(VersionInfo.class);
  }

  @Override
  public VersionInfo deserialize( ObjectNode pObjectNode ) {
    // Resolve unknown version flag
    boolean lUnknownVersion;
    JsonNode lUnknownVersionValueNode = pObjectNode.get("isUnknownVersion");
    if (lUnknownVersionValueNode != null && lUnknownVersionValueNode.isNull() == false) {
      lUnknownVersion = lUnknownVersionValueNode.asBoolean();
    }
    else {
      lUnknownVersion = false;
    }

    // Create real version info object
    VersionInfo lVersionInfo;
    if (lUnknownVersion == false) {

      // Resolve version and date.
      String lVersion = this.getStringValueFromNode(pObjectNode, "version", null);
      Date lReleaseDate = this.getDateValueFromNode(pObjectNode, "date", null);

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
