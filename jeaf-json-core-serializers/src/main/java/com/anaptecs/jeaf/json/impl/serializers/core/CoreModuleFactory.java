/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.core;

import com.anaptecs.jeaf.core.api.ServiceObjectID;
import com.anaptecs.jeaf.json.annotations.ModuleFactory;
import com.anaptecs.jeaf.json.api.ObjectMapperModuleFactory;
import com.anaptecs.jeaf.xfun.api.common.ObjectID;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.module.SimpleModule;

/**
 * Factory is responsible to provide a module that assembles all JEAF Core classes into a module for JSON serialization
 * / deserialization.
 *
 * @author JEAF Development Team
 */
@ModuleFactory
public class CoreModuleFactory implements ObjectMapperModuleFactory {

  /**
   * Method creates a module that consists of all serializer / deserializers for JEAF Core classes.
   */
  @Override
  public JacksonModule createModule( ) {
    // Create module for JEAF Core API classes
    SimpleModule lModule = new SimpleModule("JEAF Core Module");

    // Add serializers and deserializers
    lModule.addSerializer(ServiceObjectID.class, new AbstractObjectIDSerializer());
    lModule.addDeserializer(ServiceObjectID.class, new ServiceObjectIDDeserializer());
    lModule.addSerializer(ObjectID.class, new AbstractObjectIDSerializer());
    lModule.addDeserializer(ObjectID.class, new ObjectIDDeserializer());

    // Return created module.
    return lModule;
  }
}
