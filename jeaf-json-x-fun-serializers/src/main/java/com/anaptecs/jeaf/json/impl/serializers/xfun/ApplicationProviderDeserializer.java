/**
 * 2022-01-19 17:02:45.551 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.xfun;

import com.anaptecs.jeaf.json.impl.AbstractDeserializer;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.info.ApplicationProvider;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class implements custom JSON deserializer for {@link VersionInfo}.
 * 
 * @author JEAF Development Team
 */
public class ApplicationProviderDeserializer extends AbstractDeserializer<ApplicationProvider> {
  private static final long serialVersionUID = 1L;

  /**
   * Initialize object.
   */
  public ApplicationProviderDeserializer( ) {
    super(VersionInfo.class);
  }

  /**
   * Method converts the passed JSON {@link ObjectNode} into an {@link ApplicationProvider} object.
   */
  @Override
  public ApplicationProvider deserialize( ObjectNode pObjectNode ) {
    // Check parameter
    Check.checkInvalidParameterNull(pObjectNode, "pObjectNode");

    // Resolve creator
    String lCreator = this.getStringValueFromNode(pObjectNode, "creator", null);

    // Resolve creator url
    String lURL = this.getStringValueFromNode(pObjectNode, "creatorURL", null);

    ApplicationProvider lProvider;
    if (lCreator != null || lURL != null) {
      lProvider = new ApplicationProvider(lCreator, lURL);
    }
    else {
      lProvider = ApplicationProvider.UNKNOW_APP_PROVIDER;
    }
    return lProvider;
  }
}
