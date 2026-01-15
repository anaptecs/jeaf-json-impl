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
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.json.JsonMapper.Builder;

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
  public static ObjectMapper createObjectMapper(ObjectMapperConfiguration pConfiguration) {
    // Check parameters.
    Check.checkInvalidParameterNull(pConfiguration, "pConfiguration");

    // Create builder for object mappper.
    Builder lBuilder = JsonMapper.builder();

    // Set default visibilities.
    lBuilder.changeDefaultVisibility(v -> v.withFieldVisibility(pConfiguration.getDefaultFieldVisibility()));
    lBuilder.changeDefaultVisibility(v -> v.withGetterVisibility(pConfiguration.getDefaultGetterVisibility()));
    lBuilder.changeDefaultVisibility(v -> v.withIsGetterVisibility(pConfiguration.getDefaultSetterVisibility()));
    lBuilder.changeDefaultVisibility(v -> v.withSetterVisibility(pConfiguration.getDefaultSetterVisibility()));
    lBuilder.changeDefaultVisibility(v -> v.withCreatorVisibility(pConfiguration.getDefaultCreatorVisibility()));

    // Configure which properties should be included by default.
    lBuilder.changeDefaultPropertyInclusion(i -> i.withValueInclusion(pConfiguration.getDefaultPropertyInclusion()));
    lBuilder.changeDefaultPropertyInclusion(i -> i.withContentInclusion(pConfiguration.getDefaultPropertyInclusion()));

    // For better backward compatibility with Jackson 2 we preserve the following settings as there were in Jackson 2
    //
    // For further details please have a look here:
    // https://github.com/FasterXML/jackson/blob/main/jackson3/MIGRATING_TO_JACKSON_3.md
    lBuilder.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
    lBuilder.enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS);

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

    return lObjectMapper;
  }
}
