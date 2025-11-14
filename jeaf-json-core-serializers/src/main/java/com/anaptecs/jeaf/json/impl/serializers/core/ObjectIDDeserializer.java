/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.serializers.core;

import com.anaptecs.jeaf.xfun.api.common.ObjectID;

/**
 * Class implements custom JSON deserializer for {@link ObjectID}.
 * 
 * @author JEAF Development Team
 */
public class ObjectIDDeserializer extends AbstractObjectIDDeserializer<ObjectID> {
  /**
   * The only thing that needs to be done by this class is to create a {@link ObjectID}. all the rest is implemented by
   * the base class.
   */
  @Override
  protected ObjectID createObjectID( String pObjectId, Integer pVersionLabel ) {
    return new ObjectID(pObjectId, pVersionLabel);
  }
}
