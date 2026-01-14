/**
 * 2022-01-19 17:02:45.551 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.xfun;

import com.anaptecs.jeaf.json.impl.AbstractDeserializer;
import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.info.ApplicationProvider;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;

/**
 * Class implements custom JSON deserializer for {@link VersionInfo}.
 *
 * @author JEAF Development Team
 */
public class ApplicationInfoDeserializer extends AbstractDeserializer<ApplicationInfo> {
  /**
   * Deserializer for {@link ApplicationProvider}
   */
  private final ApplicationProviderDeserializer applicationProviderDeserializer = new ApplicationProviderDeserializer();

  /**
   * Deserializer for {@link VersionInfo}
   */
  private final VersionInfoDeserializer versionInfoDeserializer = new VersionInfoDeserializer();

  /**
   * Initialize object.
   */
  public ApplicationInfoDeserializer( ) {
    super(VersionInfo.class);
  }

  @Override
  public ApplicationInfo deserialize(ObjectNode pObjectNode) {
    JsonNode lUnknownAppNode = pObjectNode.get("isUnknownApplication");
    boolean lUnknownApp;
    if (lUnknownAppNode != null && lUnknownAppNode.isNull() == false) {
      lUnknownApp = lUnknownAppNode.asBoolean();
    }
    else {
      lUnknownApp = false;
    }

    // JSON describes real app info
    ApplicationInfo lApplicationInfo;
    if (lUnknownApp == false) {
      // Resolve application provider
      JsonNode lProviderNode = pObjectNode.get("applicationProvider");
      ApplicationProvider lProvider;
      if (lProviderNode != null && lProviderNode.isNull() == false) {
        lProvider = applicationProviderDeserializer.deserialize((ObjectNode) lProviderNode);
      }
      else {
        lProvider = ApplicationProvider.UNKNOW_APP_PROVIDER;
      }

      // Resolve version info.
      JsonNode lVersionNode = pObjectNode.get("version");
      VersionInfo lVersionInfo;
      if (lVersionNode != null && lVersionNode.isNull() == false) {
        lVersionInfo = versionInfoDeserializer.deserialize((ObjectNode) lVersionNode);
      }
      else {
        lVersionInfo = VersionInfo.UNKNOWN_VERSION;
      }

      // Resolve application id
      String lApplicationID = this.getStringValueFromNode(pObjectNode, "applicationID", null);

      // Resolve name
      String lName = this.getStringValueFromNode(pObjectNode, "name", null);

      // Resolve website
      String lWebsite = this.getStringValueFromNode(pObjectNode, "websiteURL", null);

      // Resolve description
      String lDescription = this.getStringValueFromNode(pObjectNode, "description", null);

      // Create application info
      lApplicationInfo = new ApplicationInfo(lApplicationID, lName, lWebsite, lDescription, lProvider, lVersionInfo);
    }
    // Serialized app info represents an unknown application
    else {
      lApplicationInfo = ApplicationInfo.UNKNOWN_APPLICATION;
    }

    // Return application info
    return lApplicationInfo;
  }
}
