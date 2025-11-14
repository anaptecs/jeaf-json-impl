package com.anaptecs.jeaf.json.impl.test.domain;

import java.util.List;

public class MultiValuedGenereicResponse<T> extends AbstractResponse {
  private List<T> values;

  public List<T> getValues( ) {
    return values;
  }

  public void setValues( List<T> pValues ) {
    values = pValues;
  }
}
