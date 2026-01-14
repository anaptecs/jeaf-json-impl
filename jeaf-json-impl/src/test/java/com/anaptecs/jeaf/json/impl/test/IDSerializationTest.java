/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.json.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.json.impl.test.idtest.Product;
import com.anaptecs.jeaf.json.impl.test.idtest.Reseller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.CollectionType;

class IDSerializationTest {
  private Reseller reseller;

  private Product product1;

  private Product product2;

  private List<Product> products = new ArrayList<>();

  private final JSONTools tools = JSONTools.getJSONTools();

  @BeforeEach
  void setup( ) {
    Reseller.Builder lBuilder = new Reseller.Builder();
    lBuilder.setId(1234);
    lBuilder.setName("Big big Reseller");
    reseller = lBuilder.build();

    product1 = new Product();
    product1.setId(4711);
    product1.setName("Fancy Product");
    product1.addReseller(reseller);

    product2 = new Product();
    product2.setId(815);
    product2.setName("Boring Product");
    product2.addReseller(reseller);

    products.add(product1);
    products.add(product2);
  }

  @Test
  void testIDSerialization( ) {
    String lJSON = tools.writeObjectToString(products);
    String lExpected =
        "[{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]},{\"id\":815,\"name\":\"Boring Product\",\"resellers\":[1234]}]";
    assertEquals(lExpected, lJSON);
  }

  @Test
  void testIDDeserialization( ) {
    String lJSON =
        "[{\"id\":4711,\"name\":\"Fancy Product\",\"resellers\":[{\"id\":1234,\"name\":\"Big big Reseller\"}]},{\"id\":815,\"name\":\"Boring Product\",\"resellers\":[1234]}]";
    ObjectMapper lObjectMapper = tools.getDefaultObjectMapper();
    CollectionType lType = lObjectMapper.getTypeFactory().constructCollectionType(List.class, Product.class);

    List<Product> lProducts = lObjectMapper.readValue(lJSON, lType);

    Product lProduct1 = lProducts.get(0);
    Reseller lReseller1 = lProduct1.getResellers().iterator().next();

    assertEquals("Fancy Product", lProduct1.getName());
    assertEquals(1, lProduct1.getResellers().size());
    assertEquals("Big big Reseller", lReseller1.getName());
    assertEquals(1234, lReseller1.getId());

    Product lProduct2 = lProducts.get(1);
    Reseller lReseller2 = lProduct2.getResellers().iterator().next();

    assertEquals("Boring Product", lProduct2.getName());
    assertEquals(1, lProduct2.getResellers().size());
    assertEquals("Big big Reseller", lReseller2.getName());
    assertEquals(1234, lReseller2.getId());
    assertEquals(lReseller1, lReseller2);
    assertSame(lReseller1, lReseller2);

    lProducts = tools.readToCollection(lJSON, List.class, Product.class);
    lProduct1 = lProducts.get(0);
    lReseller1 = lProduct1.getResellers().iterator().next();

    assertEquals("Fancy Product", lProduct1.getName());
    assertEquals(1, lProduct1.getResellers().size());
    assertEquals("Big big Reseller", lReseller1.getName());
    assertEquals(1234, lReseller1.getId());

    lProduct2 = lProducts.get(1);
    lReseller2 = lProduct2.getResellers().iterator().next();

    assertEquals("Boring Product", lProduct2.getName());
    assertEquals(1, lProduct2.getResellers().size());
    assertEquals("Big big Reseller", lReseller2.getName());
    assertEquals(1234, lReseller2.getId());
    assertEquals(lReseller1, lReseller2);
    assertSame(lReseller1, lReseller2);
  }

}
