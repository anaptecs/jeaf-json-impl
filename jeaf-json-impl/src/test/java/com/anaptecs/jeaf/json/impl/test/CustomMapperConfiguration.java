/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import com.anaptecs.jeaf.json.annotations.ObjectMapperConfig;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

@ObjectMapperConfig(
    defaultFieldVisibility = Visibility.NON_PRIVATE,
    defaultGetterVisibility = Visibility.PUBLIC_ONLY,
    defaultSetterVisibility = Visibility.PUBLIC_ONLY,
    defaultCreatorVisibility = Visibility.PROTECTED_AND_PUBLIC,
    defaultPropertyInclusion = Include.ALWAYS,
    enabledMapperFeatures = { MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, MapperFeature.APPLY_DEFAULT_VALUES },
    disabledMapperFeatures = { MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS },
    enabledSerializationFeatures = { SerializationFeature.FLUSH_AFTER_WRITE_VALUE },
    disabledSerializationFeatures = SerializationFeature.FAIL_ON_SELF_REFERENCES,
    enabledDeserializationFeatures = DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,
    disabledDeserializationFeatures = { DeserializationFeature.FAIL_ON_INVALID_SUBTYPE,
      DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES })

public interface CustomMapperConfiguration {

}
