/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import com.anaptecs.jeaf.json.annotations.ModuleFactory;
import com.anaptecs.jeaf.json.annotations.ObjectMapperConfig;
import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.json.api.ObjectMapperModuleFactory;
import com.anaptecs.jeaf.xfun.api.config.AnnotationBasedConfiguration;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationReader;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.SerializationFeature;

/**
 * Class can be used to read annotation based configuration for ObjectMappers.
 *
 * @author JEAF Development Team
 */
public class ObjectMapperConfiguration extends AnnotationBasedConfiguration<ObjectMapperConfig> {
  /**
   * Initialize object. During initialization configurations will be loaded.
   */
  public ObjectMapperConfiguration( ) {
    this(ObjectMapperConfig.OBJECT_MAPPER_CONFIG_RESOURCE_NAME, JSONTools.JSON_BASE_PATH, false);
  }

  /**
   * Initialize object. During initialization configurations will be loaded.
   *
   * @param pCustomConfigurationResourceName Name of the file which contains the class name of the custom configuration
   * class. The parameter must not be null.
   * @param pBasePackagePath Path under which the file should be found in the classpath. The parameter may be null.
   * @param pExceptionOnError If parameter is set to true then an exception will be thrown in case of configuration
   * errors.
   */
  public ObjectMapperConfiguration( String pResourceName, String pConfigurationBasePath, boolean pExceptionOnError ) {
    super(pResourceName, pConfigurationBasePath, pExceptionOnError);
  }

  @Override
  protected Class<ObjectMapperConfig> getAnnotationClass( ) {
    return ObjectMapperConfig.class;
  }

  @Override
  protected String getDefaultConfigurationClass( ) {
    return DefaultJSONConfiguration.class.getName();
  }

  @Override
  public ObjectMapperConfig getEmptyConfiguration( ) {
    return new ObjectMapperConfig() {

      @Override
      public Class<? extends Annotation> annotationType( ) {
        return ObjectMapperConfig.class;
      }

      @Override
      public Visibility defaultFieldVisibility( ) {
        return Visibility.ANY;
      }

      @Override
      public Visibility defaultGetterVisibility( ) {
        return Visibility.NONE;
      }

      @Override
      public Visibility defaultSetterVisibility( ) {
        return Visibility.NONE;
      }

      @Override
      public Visibility defaultCreatorVisibility( ) {
        return Visibility.ANY;
      }

      @Override
      public Include defaultPropertyInclusion( ) {
        return Include.NON_EMPTY;
      }

      @Override
      public MapperFeature[] enabledMapperFeatures( ) {
        return new MapperFeature[] {};
      }

      @Override
      public MapperFeature[] disabledMapperFeatures( ) {
        return new MapperFeature[] {};
      }

      @Override
      public SerializationFeature[] enabledSerializationFeatures( ) {
        return new SerializationFeature[] {};
      }

      @Override
      public SerializationFeature[] disabledSerializationFeatures( ) {
        return new SerializationFeature[] {};
      }

      @Override
      public DeserializationFeature[] enabledDeserializationFeatures( ) {
        return new DeserializationFeature[] {};
      }

      @Override
      public DeserializationFeature[] disabledDeserializationFeatures( ) {
        return new DeserializationFeature[] {};
      }
    };
  }

  @Override
  public List<String> checkCustomConfiguration(ObjectMapperConfig pCustomConfiguration) {
    // Nothing to do.
    return Collections.emptyList();
  }

  /**
   * Method returns the default field visibility that should be used by an object mapper if nothing specific is defined
   * on class or field level.
   *
   * @return {@link Visibility} Default field visibility. The method never returns null.
   */
  public Visibility getDefaultFieldVisibility( ) {
    return theConfig.defaultFieldVisibility();
  }

  /**
   * Method returns the default getter visibility that should be used by an object mapper if nothing specific is defined
   * on class or method level.
   *
   * @return {@link Visibility} Default field visibility. The method never returns null.
   */
  public Visibility getDefaultGetterVisibility( ) {
    return theConfig.defaultGetterVisibility();
  }

  /**
   * Method returns the default setter visibility that should be used by an object mapper if nothing specific is defined
   * on class or method level.
   *
   * @return {@link Visibility} Default field visibility. The method never returns null.
   */
  public Visibility getDefaultSetterVisibility( ) {
    return theConfig.defaultSetterVisibility();
  }

  /**
   * Method returns the default creator visibility that should be used by an object mapper if nothing specific is
   * defined on class or method level.
   *
   * @return {@link Visibility} Default field visibility. The method never returns null.
   */
  public Visibility getDefaultCreatorVisibility( ) {
    return theConfig.defaultCreatorVisibility();
  }

  /**
   * Attribute can be used to define which properties should be included when an object is serialized to JSON. By
   * default all non empty attributes will be included. This settings differs from Jackson's default values in order to
   * reduce content to what is really needed.
   */
  public Include getDefaultPropertyInclusion( ) {
    return theConfig.defaultPropertyInclusion();
  }

  /**
   * Method returns the list of mapper features that should be enabled explicitly. This means that these features will
   * be enabled in addition to all features that are enabled by default.
   *
   * @return {@link List} List of all features that should be explicitly enabled. The method never returns null.
   */
  public MapperFeature[] getEnabledMapperFeatures( ) {
    return theConfig.enabledMapperFeatures();
  }

  /**
   * Method returns the list of mapper features that should be disabled explicitly. This means that these features will
   * be disabled in addition to all features that are disabled by default.
   *
   * @return {@link List} List of all features that should be explicitly disabled. The method never returns null.
   */
  public MapperFeature[] getDisabledMapperFeatures( ) {
    return theConfig.disabledMapperFeatures();
  }

  /**
   * Method returns the list of serialization features that should be enabled explicitly. This means that these features
   * will be enabled in addition to all features that are enabled by default.
   *
   * @return {@link List} List of all features that should be explicitly enabled. The method never returns null.
   */
  public SerializationFeature[] getEnabledSerializationFeatures( ) {
    return theConfig.enabledSerializationFeatures();
  }

  /**
   * Method returns the list of serialization features that should be disabled explicitly. This means that these
   * features will be disabled in addition to all features that are disabled by default.
   *
   * @return {@link List} List of all features that should be explicitly disabled. The method never returns null.
   */
  public SerializationFeature[] getDisabledSerializationFeatures( ) {
    return theConfig.disabledSerializationFeatures();
  }

  /**
   * Method returns the list of deserialization features that should be enabled explicitly. This means that these
   * features will be enabled in addition to all features that are enabled by default.
   *
   * @return {@link List} List of all features that should be explicitly enabled. The method never returns null.
   */
  public DeserializationFeature[] getEnabledDeserializationFeatures( ) {
    return theConfig.enabledDeserializationFeatures();
  }

  /**
   * Method returns the list of deserialization features that should be disabled explicitly. This means that these
   * features will be disabled in addition to all features that are disabled by default.
   *
   * @return {@link List} List of all features that should be explicitly disabled. The method never returns null.
   */
  public DeserializationFeature[] getDisabledDeserializationFeatures( ) {
    return theConfig.disabledDeserializationFeatures();
  }

  public List<ObjectMapperModuleFactory> getObjectMapperModuleFactories( ) {
    ConfigurationReader lReader = new ConfigurationReader();
    List<Class<? extends ObjectMapperModuleFactory>> lFactoryClasses =
        lReader.readClassesFromConfigFile(ModuleFactory.MODULE_FACTORY_CONFIG_PATH, ObjectMapperModuleFactory.class);
    return this.newInstances(lFactoryClasses, exceptionOnError);
  }
}
