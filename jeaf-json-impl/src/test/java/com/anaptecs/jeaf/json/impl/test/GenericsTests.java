package com.anaptecs.jeaf.json.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.json.api.JSONTools;
import com.anaptecs.jeaf.json.api.ObjectType;
import com.anaptecs.jeaf.json.impl.test.domain.BusinessObject;
import com.anaptecs.jeaf.json.impl.test.domain.MultiValuedGenereicResponse;
import com.anaptecs.jeaf.json.impl.test.domain.SingleValuedGenericResponse;

public class GenericsTests {
  private final JSONTools tools = JSONTools.getJSONTools();

  @Test
  void testGenericResponse( ) {
    // Test single objects
    BusinessObject lBusinessObject = new BusinessObject();
    lBusinessObject.setProperty("Hello Java Genericss");
    SingleValuedGenericResponse<BusinessObject> lSingleValuedResponse = new SingleValuedGenericResponse<>();
    lSingleValuedResponse.setMessage("Message-1234");
    lSingleValuedResponse.setValue(lBusinessObject);

    String lJSON = tools.writeObjectToString(lSingleValuedResponse);
    assertEquals("{\"message\":\"Message-1234\",\"value\":{\"property\":\"Hello Java Genericss\"}}", lJSON);

    ObjectType lObjectType =
        ObjectType.createGenericsObjectType(SingleValuedGenericResponse.class, BusinessObject.class);
    SingleValuedGenericResponse<BusinessObject> lReadObject = tools.read(lJSON, lObjectType);

    assertEquals("Hello Java Genericss", lReadObject.getValue().getProperty());

    // Test multi valued generics
    MultiValuedGenereicResponse<BusinessObject> lMultiValuedResponse = new MultiValuedGenereicResponse<>();
    lMultiValuedResponse.setMessage("Message-4711");
    lMultiValuedResponse.setValues(List.of(lBusinessObject));

    lJSON = tools.writeObjectToString(lMultiValuedResponse);
    assertEquals("{\"message\":\"Message-4711\",\"values\":[{\"property\":\"Hello Java Genericss\"}]}" + "", lJSON);

    lObjectType = ObjectType.createGenericsObjectType(MultiValuedGenereicResponse.class, BusinessObject.class);
    MultiValuedGenereicResponse<BusinessObject> lObject = tools.read(lJSON, lObjectType);
    assertEquals("Hello Java Genericss", lObject.getValues().get(0).getProperty());

    // Test collections.
    List<SingleValuedGenericResponse<BusinessObject>> lSingleValues = List.of(lSingleValuedResponse);
    lJSON = tools.writeObjectsToString(lSingleValues, SingleValuedGenericResponse.class);
    assertEquals("[{\"message\":\"Message-1234\",\"value\":{\"property\":\"Hello Java Genericss\"}}]", lJSON);

    lObjectType = ObjectType.createGenericsObjectType(SingleValuedGenericResponse.class, BusinessObject.class);
    List<SingleValuedGenericResponse<BusinessObject>> lReadSingleValues =
        tools.readToCollection(lJSON, List.class, lObjectType);
    assertEquals(1, lReadSingleValues.size());
    assertEquals("Hello Java Genericss", lReadSingleValues.get(0).getValue().getProperty());

    List<MultiValuedGenereicResponse<BusinessObject>> lMultiValues = List.of(lMultiValuedResponse);
    lJSON = tools.writeObjectsToString(lMultiValues, MultiValuedGenereicResponse.class);
    assertEquals("[{\"message\":\"Message-4711\",\"values\":[{\"property\":\"Hello Java Genericss\"}]}]", lJSON);

    lObjectType = ObjectType.createGenericsObjectType(MultiValuedGenereicResponse.class, BusinessObject.class);
    List<MultiValuedGenereicResponse<BusinessObject>> lReadMultiValues =
        tools.readToCollection(lJSON, List.class, lObjectType);
    assertEquals(1, lReadMultiValues.size());
    assertEquals("Hello Java Genericss", lReadMultiValues.get(0).getValues().get(0).getProperty());
  }

}
