/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl;

import java.util.List;

import com.anaptecs.jeaf.json.annotations.ObjectMapperConfig;
import com.anaptecs.jeaf.json.api.ObjectMapperModuleFactory;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.json.JsonMapper.Builder;

/**
 * Class can be used to create new object mappers for JSON serialization.
 * 
 * @author JEAF Development Team
 */
public class ObjectMapperFactory {
  /**
   * Standard configuration.
   */
  private static final ObjectMapperConfiguration STANDARD_CONFIG = new ObjectMapperConfiguration();

  /**
   * Hide public constructor.
   */
  private ObjectMapperFactory( ) {
  }

  /**
   * Method creates a new {@link ObjectMapper} based on the default configuration mechanism {@link ObjectMapperConfig}.
   * 
   * @return {@link ObjectMapper} Created object mapper. The method never returns null.
   */
  public static ObjectMapper createObjectMapper( ) {
    return createObjectMapper(STANDARD_CONFIG);
  }

  /**
   * Method creates a new {@link ObjectMapper} based on the passed configuration.
   * 
   * @param pConfiguration Configuration for the {@link ObjectMapper}. The parameter must not be null.
   * @return {@link ObjectMapper} Created object mapper. The method never returns null.
   */
  public static ObjectMapper createObjectMapper( ObjectMapperConfiguration pConfiguration ) {
    // Check parameters.
    Check.checkInvalidParameterNull(pConfiguration, "pConfiguration");

    // Create builder for object mappper.
    Builder lBuilder = JsonMapper.builder();

    // Set default visibilities.
    VisibilityChecker<?> lVisibilityChecker = VisibilityChecker.Std.defaultInstance();
    lVisibilityChecker = lVisibilityChecker.withFieldVisibility(pConfiguration.getDefaultFieldVisibility());
    lVisibilityChecker = lVisibilityChecker.withGetterVisibility(pConfiguration.getDefaultGetterVisibility());
    lVisibilityChecker = lVisibilityChecker.withIsGetterVisibility(pConfiguration.getDefaultSetterVisibility());
    lVisibilityChecker = lVisibilityChecker.withSetterVisibility(pConfiguration.getDefaultSetterVisibility());
    lVisibilityChecker = lVisibilityChecker.withCreatorVisibility(pConfiguration.getDefaultCreatorVisibility());
    lBuilder.visibility(lVisibilityChecker);

    // Configure mapper features
    lBuilder.enable(pConfiguration.getEnabledMapperFeatures());
    lBuilder.disable(pConfiguration.getDisabledMapperFeatures());
    lBuilder.enable(pConfiguration.getEnabledSerializationFeatures());
    lBuilder.disable(pConfiguration.getDisabledSerializationFeatures());
    lBuilder.enable(pConfiguration.getEnabledDeserializationFeatures());
    lBuilder.disable(pConfiguration.getDisabledDeserializationFeatures());

    // Create configured modules and add them as well.
    List<ObjectMapperModuleFactory> lModuleFactories = pConfiguration.getObjectMapperModuleFactories();
    for (ObjectMapperModuleFactory lFactory : lModuleFactories) {
      lBuilder.addModule(lFactory.createModule());
    }

    // Create object mapper.
    JsonMapper lObjectMapper = lBuilder.build();

    // Configure which properties should be included by default.
    lObjectMapper.setDefaultPropertyInclusion(pConfiguration.getDefaultPropertyInclusion());

    return lObjectMapper;
  }
}
