/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class implements a context resolver that should be used instead of the default implementation when it comes to JSON
 * object mapping.
 * 
 * @author JEAF Development Team
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
  /**
   * Only instance of {@link ObjectMapper}.
   */
  private final ObjectMapper mapper;

  public ObjectMapperContextResolver( ) {
    // Create object mapper based on configuration
    // Lookup configuration
    mapper = ObjectMapperFactory.createObjectMapper();
  }

  /**
   * Method will always return the same {@link ObjectMapper}.
   */
  @Override
  public ObjectMapper getContext( Class<?> type ) {
    return mapper;
  }
}