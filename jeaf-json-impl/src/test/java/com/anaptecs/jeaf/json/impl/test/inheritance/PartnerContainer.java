/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test.inheritance;

import java.util.ArrayList;
import java.util.List;

public class PartnerContainer {
  private List<Partner> partners = new ArrayList<>();

  public List<Partner> getPartners( ) {
    return partners;
  }

  public void addPartner( Partner pPartner ) {
    partners.add(pPartner);
  }

}
