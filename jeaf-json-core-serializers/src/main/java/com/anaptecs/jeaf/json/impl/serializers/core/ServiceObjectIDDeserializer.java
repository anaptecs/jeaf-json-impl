/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.core;

import com.anaptecs.jeaf.core.api.ServiceObjectID;

/**
 * Class implements custom JSON deserializer for {@link ServiceObjectID}.
 * 
 * @author JEAF Development Team
 */
public class ServiceObjectIDDeserializer extends AbstractObjectIDDeserializer<ServiceObjectID> {
  /**
   * The only thing that needs to be done by this class is to create a {@link ServiceObjectID}. all the rest is
   * implemented by the base class.
   */
  @Override
  protected ServiceObjectID createObjectID( String pObjectId, Integer pVersionLabel ) {
    return new ServiceObjectID(pObjectId, pVersionLabel);
  }
}
