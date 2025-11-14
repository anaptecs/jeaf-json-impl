/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

public class ClassWithSelfReference {
  @SuppressWarnings("unused")
  private ClassWithSelfReference self;

  public ClassWithSelfReference( ) {
    self = this;
  }
}
