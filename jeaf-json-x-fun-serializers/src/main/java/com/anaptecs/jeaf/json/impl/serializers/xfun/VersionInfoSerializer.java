/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.xfun;

import java.io.IOException;

import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Class implements a custom serializer for class {@link VersionInfo}.
 * 
 * @author JEAF Development Team
 */
public class VersionInfoSerializer extends StdSerializer<VersionInfo> {
  private static final long serialVersionUID = 1L;

  public static final String VERSION = "version";

  public static final String DATE = "date";

  public static final String UNKNWON_VERSION_FLAG = "isUnknownVersion";

  public VersionInfoSerializer( ) {
    super(VersionInfo.class);
  }

  /**
   * Method serializes the passed version info into a JSON representation.
   */
  @Override
  public void serialize( VersionInfo pVersionInfo, JsonGenerator pGenerator, SerializerProvider pProvider )
    throws IOException {

    // Check parameters
    Check.checkInvalidParameterNull(pVersionInfo, "pVersionInfo");
    Check.checkInvalidParameterNull(pGenerator, "pGenerator");
    Check.checkInvalidParameterNull(pProvider, "pProvider");

    // Write relevant fields as JSON
    pGenerator.writeStartObject();
    pGenerator.writeStringField(VERSION, pVersionInfo.getVersionString());
    pGenerator.writeStringField(DATE, DateTools.getDateTools().toTimestampString(pVersionInfo.getCreationDate()));
    pGenerator.writeBooleanField(UNKNWON_VERSION_FLAG, pVersionInfo.isUnknownVersion());
    pGenerator.writeEndObject();
  }
}
