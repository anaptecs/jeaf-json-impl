/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

public class VersionInfoSerializer extends StdSerializer<VersionInfo> {
  public VersionInfoSerializer( ) {
    super(VersionInfo.class);
  }

  @Override
  public void serialize(VersionInfo pVersionInfo, JsonGenerator pGenerator, SerializationContext pProvider) {
    pGenerator.writeStartObject();
    pGenerator.writeStringProperty("version", pVersionInfo.getVersionString());
    pGenerator.writeStringProperty("date", DateTools.getDateTools().toTimestampString(pVersionInfo.getCreationDate()));
    pGenerator.writeBooleanProperty("isUnknownVersion", pVersionInfo.isUnknownVersion());
    pGenerator.writeEndObject();
  }
}
