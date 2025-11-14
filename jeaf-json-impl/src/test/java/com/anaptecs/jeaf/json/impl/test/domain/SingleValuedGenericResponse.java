package com.anaptecs.jeaf.json.impl.test.domain;

public class SingleValuedGenericResponse<T> extends AbstractResponse {
  T value;

  public T getValue( ) {
    return value;
  }

  public void setValue( T pValue ) {
    value = pValue;
  }
}
