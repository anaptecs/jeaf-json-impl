/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import java.io.IOException;

import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class VersionInfoSerializer extends StdSerializer<VersionInfo> {
  private static final long serialVersionUID = 1L;

  public VersionInfoSerializer( ) {
    super(VersionInfo.class);
  }

  @Override
  public void serialize( VersionInfo pVersionInfo, JsonGenerator pGenerator, SerializerProvider pProvider )
    throws IOException {

    pGenerator.writeStartObject();
    pGenerator.writeStringField("version", pVersionInfo.getVersionString());
    pGenerator.writeStringField("date", DateTools.getDateTools().toTimestampString(pVersionInfo.getCreationDate()));
    pGenerator.writeBooleanField("isUnknownVersion", pVersionInfo.isUnknownVersion());
    pGenerator.writeEndObject();
  }
}
