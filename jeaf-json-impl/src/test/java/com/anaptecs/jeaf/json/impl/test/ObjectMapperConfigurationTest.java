/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.json.annotations.ObjectMapperConfig;
import com.anaptecs.jeaf.json.api.ObjectMapperModuleFactory;
import com.anaptecs.jeaf.json.impl.ObjectMapperConfiguration;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

class ObjectMapperConfigurationTest {
  @Test
  void testDefaultconfiguration( ) {
    ObjectMapperConfiguration lConfiguration = new ObjectMapperConfiguration();

    assertEquals(Visibility.ANY, lConfiguration.getDefaultFieldVisibility());
    assertEquals(Visibility.NONE, lConfiguration.getDefaultGetterVisibility());
    assertEquals(Visibility.NONE, lConfiguration.getDefaultSetterVisibility());
    assertEquals(Visibility.ANY, lConfiguration.getDefaultCreatorVisibility());
    assertEquals(Include.NON_EMPTY, lConfiguration.getDefaultPropertyInclusion());
    assertEquals(0, lConfiguration.getEnabledMapperFeatures().length);
    assertEquals(0, lConfiguration.getDisabledMapperFeatures().length);
    assertEquals(0, lConfiguration.getEnabledSerializationFeatures().length);
    assertEquals(0, lConfiguration.getDisabledSerializationFeatures().length);
    assertEquals(0, lConfiguration.getEnabledDeserializationFeatures().length);
    assertEquals(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
        lConfiguration.getDisabledDeserializationFeatures()[0]);
    assertEquals(1, lConfiguration.getDisabledDeserializationFeatures().length);
    assertEquals(MyModuleFactoryImpl.class, lConfiguration.getObjectMapperModuleFactories().get(0).getClass());
    assertEquals(1, lConfiguration.getObjectMapperModuleFactories().size());

    assertEquals(true, lConfiguration.checkCustomConfiguration(lConfiguration.getEmptyConfiguration()).isEmpty());
  }

  @Test
  void testCustomConfiguration( ) {
    ObjectMapperConfiguration lConfiguration = new ObjectMapperConfiguration("CustomMapper", null, true);
    assertEquals(Visibility.NON_PRIVATE, lConfiguration.getDefaultFieldVisibility());
    assertEquals(Visibility.PUBLIC_ONLY, lConfiguration.getDefaultGetterVisibility());
    assertEquals(Visibility.PUBLIC_ONLY, lConfiguration.getDefaultSetterVisibility());
    assertEquals(Visibility.PROTECTED_AND_PUBLIC, lConfiguration.getDefaultCreatorVisibility());
    assertEquals(Include.ALWAYS, lConfiguration.getDefaultPropertyInclusion());
    assertEquals(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, lConfiguration.getEnabledMapperFeatures()[0]);
    assertEquals(MapperFeature.APPLY_DEFAULT_VALUES, lConfiguration.getEnabledMapperFeatures()[1]);
    assertEquals(2, lConfiguration.getEnabledMapperFeatures().length);

    assertEquals(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS, lConfiguration.getDisabledMapperFeatures()[0]);
    assertEquals(1, lConfiguration.getDisabledMapperFeatures().length);

    assertEquals(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, lConfiguration.getEnabledSerializationFeatures()[0]);
    assertEquals(1, lConfiguration.getEnabledSerializationFeatures().length);

    assertEquals(SerializationFeature.FAIL_ON_SELF_REFERENCES, lConfiguration.getDisabledSerializationFeatures()[0]);
    assertEquals(1, lConfiguration.getDisabledSerializationFeatures().length);

    assertEquals(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,
        lConfiguration.getEnabledDeserializationFeatures()[0]);
    assertEquals(1, lConfiguration.getEnabledDeserializationFeatures().length);

    assertEquals(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE,
        lConfiguration.getDisabledDeserializationFeatures()[0]);
    assertEquals(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
        lConfiguration.getDisabledDeserializationFeatures()[1]);
    assertEquals(2, lConfiguration.getDisabledDeserializationFeatures().length);

    List<ObjectMapperModuleFactory> lFactories = lConfiguration.getObjectMapperModuleFactories();
    assertEquals(MyModuleFactoryImpl.class, lFactories.get(0).getClass());
    assertEquals(1, lConfiguration.getObjectMapperModuleFactories().size());
  }

  @Test
  void testEmptyConfiguration( ) {
    ObjectMapperConfig lEmptyConfiguration = new ObjectMapperConfiguration().getEmptyConfiguration();
    assertEquals(Visibility.ANY, lEmptyConfiguration.defaultFieldVisibility());
    assertEquals(Visibility.NONE, lEmptyConfiguration.defaultGetterVisibility());
    assertEquals(Visibility.NONE, lEmptyConfiguration.defaultSetterVisibility());
    assertEquals(Visibility.ANY, lEmptyConfiguration.defaultCreatorVisibility());
    assertEquals(Include.NON_EMPTY, lEmptyConfiguration.defaultPropertyInclusion());
    assertEquals(0, lEmptyConfiguration.enabledMapperFeatures().length);
    assertEquals(0, lEmptyConfiguration.disabledMapperFeatures().length);
    assertEquals(0, lEmptyConfiguration.enabledSerializationFeatures().length);
    assertEquals(0, lEmptyConfiguration.disabledSerializationFeatures().length);
    assertEquals(0, lEmptyConfiguration.enabledDeserializationFeatures().length);
    assertEquals(0, lEmptyConfiguration.disabledDeserializationFeatures().length);
    assertEquals(ObjectMapperConfig.class, lEmptyConfiguration.annotationType());
  }

}
