/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import com.anaptecs.jeaf.json.annotations.ModuleFactory;
import com.anaptecs.jeaf.json.api.ObjectMapperModuleFactory;
import com.anaptecs.jeaf.json.impl.test.domain.Broken;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

@ModuleFactory
public class MyModuleFactoryImpl implements ObjectMapperModuleFactory {

  @Override
  public Module createModule( ) {
    SimpleModule lModule = new SimpleModule("Test Module");

    lModule.addSerializer(VersionInfo.class, new VersionInfoSerializer());
    lModule.addDeserializer(VersionInfo.class, new VersionInfoDeserializer());
    lModule.addSerializer(Broken.class, new BrokenSerializer());
    lModule.addDeserializer(Broken.class, new BrokenDeserializer());
    return lModule;
  }
}
