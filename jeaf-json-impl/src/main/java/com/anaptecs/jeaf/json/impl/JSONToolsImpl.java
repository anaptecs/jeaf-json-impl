/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import com.anaptecs.jeaf.json.annotations.ModuleFactory;
import com.anaptecs.jeaf.json.annotations.ObjectMapperConfig;
import com.anaptecs.jeaf.json.api.JSONMessages;
import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.json.api.ObjectType;
import com.anaptecs.jeaf.json.api.ObjectType.CollectionObjectType;
import com.anaptecs.jeaf.json.api.ObjectType.GenericsObjectType;
import com.anaptecs.jeaf.json.api.ObjectType.SingleObjectType;
import com.anaptecs.jeaf.tools.annotations.ToolsImplementation;
import com.anaptecs.jeaf.xfun.api.checks.Assert;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Class provides an implementation of JEAF JSON Tools
 * 
 * @author JEAF Development Team
 */
@ToolsImplementation(toolsInterface = JSONTools.class)
public class JSONToolsImpl implements JSONTools {
  /**
   * Instance of {@link ObjectMapper} that will be used for serialization / deserialization.
   */
  private final ObjectMapper defaultObjectMapper = ObjectMapperFactory.createObjectMapper();

  /**
   * Method returns a JSON {@link ObjectMapper} that is configured as defined using {@link ObjectMapperConfig} and
   * {@link ModuleFactory}.
   * 
   * @return {@link ObjectMapper} Object mapper that should be used for JSON serialization.
   */
  @Override
  public ObjectMapper getDefaultObjectMapper( ) {
    // In order to ensure that internal object is not changed from the outside we always return a new instance of the
    // object mapper.
    return ObjectMapperFactory.createObjectMapper();
  }

  /**
   * Method serializes the passed object to the passed stream.
   * 
   * @param pObject Object that should be serialized as JSON. The parameter may be null.
   * @param pOutputStream Stream to which the object should be written. The parameter must not be null.
   * @throws JEAFSystemException if an error occurs during serialization
   */
  @Override
  public void writeObject( Object pObject, OutputStream pOutputStream ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pOutputStream, "pOutputStream");

    try {
      defaultObjectMapper.writeValue(pOutputStream, pObject);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_SERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method serializes the passed object to the passed writer.
   * 
   * @param pObject Object that should be serialized as JSON. The parameter may be null.
   * @param pWriter Writer to which the object should be written. The parameter must not be null.
   * @throws JEAFSystemException if an error occurs during serialization
   */
  @Override
  public void writeObject( Object pObject, Writer pWriter ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pWriter, "pWriter");

    try {
      defaultObjectMapper.writeValue(pWriter, pObject);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_SERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method serializes the passed object to the passed file.
   * 
   * @param pObject Object that should be serialized as JSON. The parameter may be null.
   * @param pFile File to which the object should be written. The parameter must not be null.
   * @throws JEAFSystemException if an error occurs during serialization
   */
  @Override
  public void writeObject( Object pObject, File pFile ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pFile, "pFile");

    try {
      defaultObjectMapper.writeValue(pFile, pObject);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_SERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method serializes the passed object as string.
   * 
   * @param pObject Object that should be serialized as JSON. The parameter may be null.
   * @return {@link String} JSON serialized object as String. The method never returns null.
   * @throws JEAFSystemException if an error occurs during serialization
   */
  @Override
  public String writeObjectToString( Object pObject ) throws JEAFSystemException {
    try {
      return defaultObjectMapper.writeValueAsString(pObject);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_SERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method serializes the passed object as byte array.
   * 
   * @param pObject Object that should be serialized as JSON. The parameter may be null.
   * @return byte[] JSON serialized object as byte array. The method never returns null.
   * @throws JEAFSystemException if an error occurs during serialization
   */
  @Override
  public byte[] writeObjectToBytes( Object pObject ) throws JEAFSystemException {
    try {
      return defaultObjectMapper.writeValueAsBytes(pObject);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_SERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method serializes the passed {@link Collection} to the passed stream.
   * 
   * @param pObjects Collection of objects that should be serialized as JSON. The parameter may be null.
   * @param pOutputStream Stream to which the object should be written. The parameter must not be null.
   * @param pObjectType {@link Class} object representing the type of objects that are inside the collection. As type
   * info of Java generics is not present at runtime this information has to be passed as additional parameter.
   * @throws JEAFSystemException if an error occurs during serialization
   */
  @Override
  public void writeObjects( Collection<?> pObjects, OutputStream pOutputStream, Class<?> pObjectType )
    throws JEAFSystemException {

    // Check parameters
    Check.checkInvalidParameterNull(pObjectType, "pObjectType");
    Check.checkInvalidParameterNull(pOutputStream, "pOutputStream");

    // Get special writer that is able to preserve type information when serializing collections.
    ObjectWriter lWriter = this.getCollectionWriter(pObjects, pObjectType);

    // Serialize passed collection.
    try {
      lWriter.writeValue(pOutputStream, pObjects);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_SERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method serializes the passed {@link Collection} to the passed writer.
   * 
   * @param pObjects Collection of objects that should be serialized as JSON. The parameter may be null.
   * @param pWriter Writer to which the object should be written. The parameter must not be null.
   * @param pObjectType {@link Class} object representing the type of objects that are inside the collection. As type
   * info of Java generics is not present at runtime this information has to be passed as additional parameter.
   * @throws JEAFSystemException if an error occurs during serialization
   */
  @Override
  public void writeObjects( Collection<?> pObjects, Writer pWriter, Class<?> pObjectType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectType, "pObjectType");
    Check.checkInvalidParameterNull(pWriter, "pWriter");

    // Get special writer that is able to preserve type information when serializing collections.
    ObjectWriter lWriter = this.getCollectionWriter(pObjects, pObjectType);

    // Serialize passed collection.
    try {
      lWriter.writeValue(pWriter, pObjects);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_SERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method serializes the passed {@link Collection} to the passed file.
   * 
   * @param pObjects Collection of objects that should be serialized as JSON. The parameter may be null.
   * @param pFile File to which the object should be written. The parameter must not be null.
   * @param pObjectType {@link Class} object representing the type of objects that are inside the collection. As type
   * info of Java generics is not present at runtime this information has to be passed as additional parameter.
   * @throws JEAFSystemException if an error occurs during serialization
   */
  @Override
  public void writeObjects( Collection<?> pObjects, File pFile, Class<?> pObjectType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectType, "pObjectType");
    Check.checkInvalidParameterNull(pFile, "pFile");

    // Get special writer that is able to preserve type information when serializing collections.
    ObjectWriter lWriter = this.getCollectionWriter(pObjects, pObjectType);

    // Serialize passed collection.
    try {
      lWriter.writeValue(pFile, pObjects);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_SERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method serializes the passed {@link Collection} as string.
   * 
   * @param pObjects Collection of objects that should be serialized as JSON. The parameter may be null.
   * @param pObjectType {@link Class} object representing the type of objects that are inside the collection. As type
   * info of Java generics is not present at runtime this information has to be passed as additional parameter.
   * @return {@link String} JSON serialized object as String. The method never returns null.
   * @throws JEAFSystemException if an error occurs during serialization
   */
  @Override
  public String writeObjectsToString( Collection<?> pObjects, Class<?> pObjectType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectType, "pObjectType");

    // Get special writer that is able to preserve type information when serializing collections.
    ObjectWriter lWriter = this.getCollectionWriter(pObjects, pObjectType);

    // Serialize passed collection.
    try {
      return lWriter.writeValueAsString(pObjects);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_SERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method serializes the passed {@link Collection} as byte array.
   * 
   * @param pObjects Collection of objects that should be serialized as JSON. The parameter may be null.
   * @param pObjectType {@link Class} object representing the type of objects that are inside the collection. As type
   * info of Java generics is not present at runtime this information has to be passed as additional parameter.
   * @return {@link String} JSON serialized object as String. The method never returns null.
   * @throws JEAFSystemException if an error occurs during serialization
   */
  @Override
  public byte[] writeObjectsToBytes( Collection<?> pObjects, Class<?> pObjectType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pObjectType, "pObjectType");

    // Get special writer that is able to preserve type information when serializing collections.
    ObjectWriter lWriter = this.getCollectionWriter(pObjects, pObjectType);

    // Serialize passed collection.
    try {
      return lWriter.writeValueAsBytes(pObjects);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_SERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the data of passed stream into an object of the passed type.
   * 
   * @param pInputStream Stream from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return T Object of passed type that was created. The method may return null if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T> T read( InputStream pInputStream, Class<T> pResultType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pInputStream, "pInputStream");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pInputStream, pResultType);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the data from the passed reader into an object of the passed type.
   * 
   * @param pReader Reader from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return T Object of passed type that was created. The method may return null if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T> T read( Reader pReader, Class<T> pResultType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pReader, "pReader");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pReader, pResultType);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the data from the passed file into an object of the passed type.
   * 
   * @param pFile File from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return T Object of passed type that was created. The method may return null if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T> T read( File pFile, Class<T> pResultType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pFile, "pFile");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pFile, pResultType);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the passed string into an object of the passed type.
   * 
   * @param pString String from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return T Object of passed type that was created. The method may return null if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T> T read( String pString, Class<T> pResultType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pString, "pString");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pString, pResultType);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the passed byte array into an object of the passed type.
   * 
   * @param pBytes Byte array from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return T Object of passed type that was created. The method may return null if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T> T read( byte[] pBytes, Class<T> pResultType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pBytes, "pBytes");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pBytes, pResultType);
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the passed string into an object of the passed type.
   * 
   * @param pString String from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return T Object of passed type that was created. The method may return null if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T> T read( String pString, ObjectType pResultType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pString, "pString");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pString, this.getJavaType(pResultType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the passed byte array into an object of the passed type.
   * 
   * @param pBytes Byte array from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return T Object of passed type that was created. The method may return null if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T> T read( byte[] pBytes, ObjectType pResultType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pBytes, "pBytes");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pBytes, this.getJavaType(pResultType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the data of passed stream into an object of the passed type.
   * 
   * @param pInputStream Stream from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return T Object of passed type that was created. The method may return null if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T> T read( InputStream pInputStream, ObjectType pResultType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pInputStream, "pInputStream");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pInputStream, this.getJavaType(pResultType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the data from the passed reader into an object of the passed type.
   * 
   * @param pReader Reader from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return T Object of passed type that was created. The method may return null if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T> T read( Reader pReader, ObjectType pResultType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pReader, "pReader");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pReader, this.getJavaType(pResultType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the data from the passed file into an object of the passed type.
   * 
   * @param pFile File from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return T Object of passed type that was created. The method may return null if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T> T read( File pFile, ObjectType pResultType ) throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pFile, "pFile");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pFile, this.getJavaType(pResultType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the data of passed stream into a {@link List} of objects of the passed type.
   * 
   * @param pInputStream Stream from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return {@link List} List of objects of passed type that was created from the JSON data. The method may return null
   * if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T extends Collection<?>> T readToCollection( InputStream pInputStream, Class<T> pCollectionType,
      Class<?> pResultType )
    throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pInputStream, "pInputStream");
    Check.checkInvalidParameterNull(pCollectionType, "pCollectionType");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pInputStream, this.getCollectionType(pCollectionType, pResultType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the data from the passed reader into a {@link List} of objects of the passed type.
   * 
   * @param pReader Reader from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return {@link List} List of objects of passed type that was created from the JSON data. The method may return null
   * if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T extends Collection<?>> T readToCollection( Reader pReader, Class<T> pCollectionType, Class<?> pResultType )
    throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pReader, "pReader");
    Check.checkInvalidParameterNull(pCollectionType, "pCollectionType");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pReader, this.getCollectionType(pCollectionType, pResultType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the data from the passed file into a {@link List} of objects of the passed type.
   * 
   * @param pFile File from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return {@link List} List of objects of passed type that was created from the JSON data. The method may return null
   * if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T extends Collection<?>> T readToCollection( File pFile, Class<T> pCollectionType, Class<?> pResultType )
    throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pFile, "pFile");
    Check.checkInvalidParameterNull(pCollectionType, "pCollectionType");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pFile, this.getCollectionType(pCollectionType, pResultType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the passed string into a {@link List} of objects of the passed type.
   * 
   * @param pString String from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return {@link List} List of objects of passed type that was created from the JSON data. The method may return null
   * if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T extends Collection<?>> T readToCollection( String pString, Class<T> pCollectionType, Class<?> pResultType )
    throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pString, "pString");
    Check.checkInvalidParameterNull(pCollectionType, "pCollectionType");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pString, this.getCollectionType(pCollectionType, pResultType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method deserializes the passed string into a {@link List} of objects of the passed type.
   * 
   * @param pBytes Byte array from which the data should be read. The parameter must not be null.
   * @param pResultType Type of the object that should be created. The parameter must not be null.
   * @return {@link List} List of objects of passed type that was created from the JSON data. The method may return null
   * if null was serialized.
   * @throws JEAFSystemException if an error occurs during deserialization
   */
  @Override
  public <T extends Collection<?>> T readToCollection( byte[] pBytes, Class<T> pCollectionType, Class<?> pResultType )
    throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pBytes, "pBytes");
    Check.checkInvalidParameterNull(pCollectionType, "pCollectionType");
    Check.checkInvalidParameterNull(pResultType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pBytes, this.getCollectionType(pCollectionType, pResultType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  // TODO
  @Override
  public <T extends Collection<?>> T readToCollection( InputStream pInputStream, Class<T> pCollectionType,
      ObjectType pObjectType )
    throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pInputStream, "pInputStream");
    Check.checkInvalidParameterNull(pCollectionType, "pCollectionType");
    Check.checkInvalidParameterNull(pObjectType, "pObjectType");

    try {
      return defaultObjectMapper.readValue(pInputStream, this.getCollectionType(pCollectionType, pObjectType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  @Override
  public <T extends Collection<?>> T readToCollection( Reader pReader, Class<T> pCollectionType,
      ObjectType pObjectType )
    throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pReader, "pReader");
    Check.checkInvalidParameterNull(pCollectionType, "pCollectionType");
    Check.checkInvalidParameterNull(pObjectType, "pObjectType");

    try {
      return defaultObjectMapper.readValue(pReader, this.getCollectionType(pCollectionType, pObjectType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  @Override
  public <T extends Collection<?>> T readToCollection( File pFile, Class<T> pCollectionType, ObjectType pObjectType )
    throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pFile, "pFile");
    Check.checkInvalidParameterNull(pCollectionType, "pCollectionType");
    Check.checkInvalidParameterNull(pObjectType, "pObjectType");

    try {
      return defaultObjectMapper.readValue(pFile, this.getCollectionType(pCollectionType, pObjectType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  @Override
  public <T extends Collection<?>> T readToCollection( String pJSONString, Class<T> pCollectionType,
      ObjectType pObjectType )
    throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pJSONString, "pJSONString");
    Check.checkInvalidParameterNull(pCollectionType, "pCollectionType");
    Check.checkInvalidParameterNull(pObjectType, "pObjectType");

    try {
      return defaultObjectMapper.readValue(pJSONString, this.getCollectionType(pCollectionType, pObjectType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  @Override
  public <T extends Collection<?>> T readToCollection( byte[] pBytes, Class<T> pCollectionType, ObjectType pObjectType )
    throws JEAFSystemException {
    // Check parameters
    Check.checkInvalidParameterNull(pBytes, "pBytes");
    Check.checkInvalidParameterNull(pCollectionType, "pCollectionType");
    Check.checkInvalidParameterNull(pObjectType, "pResultType");

    try {
      return defaultObjectMapper.readValue(pBytes, this.getCollectionType(pCollectionType, pObjectType));
    }
    catch (IOException e) {
      throw new JEAFSystemException(JSONMessages.JSON_DESERIALIZATION_FAILED, e, e.getMessage());
    }
  }

  /**
   * Method uses the passed {@link ObjectType} to creating a Jackson type definition that can be used for
   * deserialization.
   * 
   * @param pObjectType Response type that should be used to create matching Jackson {@link JavaType}
   * @return {@link JavaType} JavaType that was created using the passed response type. The method never returns null.
   */
  private JavaType getJavaType( ObjectType pObjectType ) {
    TypeFactory lTypeFactory = defaultObjectMapper.getTypeFactory();

    JavaType lJavaType;
    if (pObjectType instanceof SingleObjectType) {
      lJavaType = lTypeFactory.constructType(((SingleObjectType) pObjectType).getObjectType());
    }
    else if (pObjectType instanceof CollectionObjectType) {
      CollectionObjectType lCollectionResponseType = (CollectionObjectType) pObjectType;
      lJavaType = lTypeFactory.constructCollectionType(lCollectionResponseType.getCollectionType(),
          lCollectionResponseType.getObjectType());
    }
    else if (pObjectType instanceof GenericsObjectType) {
      GenericsObjectType lGenericsObjectType = (GenericsObjectType) pObjectType;
      lJavaType = lTypeFactory.constructParametricType(lGenericsObjectType.getGenericType(),
          lGenericsObjectType.getParameterType());
    }
    else {
      Assert.internalError("Unexpected ResponseType implementation " + pObjectType.getClass().getName());
      lJavaType = null;
    }
    return lJavaType;
  }

  private <T> JavaType getCollectionType( Class<? extends Collection<?>> pCollectionType, Class<T> pResultType ) {
    return defaultObjectMapper.getTypeFactory().constructCollectionType(pCollectionType, pResultType);
  }

  private JavaType getCollectionType( Class<? extends Collection<?>> pCollectionType, ObjectType pResultType ) {
    JavaType lJavaType = this.getJavaType(pResultType);
    return defaultObjectMapper.getTypeFactory().constructCollectionType(pCollectionType, lJavaType);
  }

  @SuppressWarnings("rawtypes")
  private ObjectWriter getCollectionWriter( Collection<?> pCollection, Class<?> pObjectType ) {
    // Check parameters.
    Assert.assertNotNull(pObjectType, "pObjectType");

    // As null is also a valid value for the collection have to be use a default type in this case.
    Class<? extends Collection> lCollectionType;
    if (pCollection != null) {
      lCollectionType = pCollection.getClass();
    }
    else {
      lCollectionType = Collection.class;
    }

    // Create writer and return it.
    TypeFactory lTypeFactory = defaultObjectMapper.getTypeFactory();
    JavaType lType = lTypeFactory.constructCollectionType(lCollectionType, pObjectType);
    return defaultObjectMapper.writerFor(lType);
  }

}
