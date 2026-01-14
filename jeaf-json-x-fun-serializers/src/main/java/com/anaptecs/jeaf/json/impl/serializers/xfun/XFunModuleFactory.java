/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.xfun;

import com.anaptecs.jeaf.json.annotations.ModuleFactory;
import com.anaptecs.jeaf.json.api.ObjectMapperModuleFactory;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.info.ApplicationProvider;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedString;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.module.SimpleModule;

/**
 * Factory is responsible to provide a module that assembles all JEAF X-Fun classes into a module for JSON serialization
 * / deserialization.
 *
 * @author JEAF Development Team
 */
@ModuleFactory
public class XFunModuleFactory implements ObjectMapperModuleFactory {

  /**
   * Method creates a module that consists of all serializer / deserializers for JEAF X-Fun classes.
   */
  @Override
  public JacksonModule createModule( ) {
    // Create module for JEAF Core API classes
    SimpleModule lModule = new SimpleModule("JEAF X-Fun Module");

    // Add serializers and deserializers
    lModule.addSerializer(VersionInfo.class, new VersionInfoSerializer());
    lModule.addDeserializer(VersionInfo.class, new VersionInfoDeserializer());
    lModule.addDeserializer(ApplicationProvider.class, new ApplicationProviderDeserializer());
    lModule.addDeserializer(ApplicationInfo.class, new ApplicationInfoDeserializer());

    // Add serializers and deserializers for message classes
    lModule.addDeserializer(LocalizedString.class, new LocalizedStringDeserializer());
    lModule.addDeserializer(MessageID.class, new MessageIDDeserializer());
    lModule.addDeserializer(ErrorCode.class, new ErrorCodeDeserializer());

    // Return created module.
    return lModule;
  }
}
