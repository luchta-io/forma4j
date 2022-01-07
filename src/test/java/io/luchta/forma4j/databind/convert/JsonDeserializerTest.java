package io.luchta.forma4j.databind.convert;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.luchta.forma4j.context.databind.convert.JsonDeserializer;
import io.luchta.forma4j.context.databind.convert.JsonSerializer;
import io.luchta.forma4j.context.databind.json.JsonObject;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonDeserializerTest {

	@Test
	void test() throws JsonMappingException, JsonProcessingException {
		
		String json = "{\"luchta-reader\" : {\"sheet\" : {\"index\" : \"0\", \"header\" : {\"CreatedDate\" : \"2020-11-06T00:00\"}, \"body\" : {\"Employees\" : [{\"Employee\" : {\"EmployeeCode\" : \"0001\", \"Dynamics\" : [{\"Dynamic\" : \"1.05\"}, {\"Dynamic\" : \"2.0\"}, {\"Dynamic\" : \"3.0\"}, {\"Dynamic\" : \"4.0\"}, {\"Dynamic\" : \"5.0\"}, {\"Dynamic\" : \"6.0\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"太郎\"}}, {\"Employee\" : {\"EmployeeCode\" : \"0002\", \"Dynamics\" : [{\"Dynamic\" : \"7.0\"}, {\"Dynamic\" : \"8.0\"}, {\"Dynamic\" : \"9.0\"}, {\"Dynamic\" : \"10.0\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"二郎\"}}, {\"Employee\" : {\"EmployeeCode\" : \"0003\", \"Dynamics\" : [{\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"三郎\"}}, {\"Employee\" : {\"EmployeeCode\" : \"0004\", \"Dynamics\" : [{\"Dynamic\" : \"11.0\"}, {\"Dynamic\" : \"12.0\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"よし子\"}}, {\"Employee\" : {\"EmployeeCode\" : \"0005\", \"Dynamics\" : [{\"Dynamic\" : \"13.0\"}, {\"Dynamic\" : \"14.0\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"いつこ\"}}, {\"Employee\" : {\"EmployeeCode\" : \"0006\", \"Dynamics\" : [{\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"六実\"}}, {\"Employee\" : {\"EmployeeCode\" : \"0007\", \"Dynamics\" : [{\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"七海\"}}, {\"Employee\" : {\"EmployeeCode\" : \"0008\", \"Dynamics\" : [{\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"やすみ\"}}, {\"Employee\" : {\"EmployeeCode\" : \"0009\", \"Dynamics\" : [{\"Dynamic\" : \"15.0\"}, {\"Dynamic\" : \"16.0\"}, {\"Dynamic\" : \"17.0\"}, {\"Dynamic\" : \"18.0\"}, {\"Dynamic\" : \"19.0\"}, {\"Dynamic\" : \"20.0\"}, {\"Dynamic\" : \"21.0\"}, {\"Dynamic\" : \"22.0\"}, {\"Dynamic\" : \"23.0\"}, {\"Dynamic\" : \"24.0\"}], \"FirstName\" : \"山田\", \"LastName\" : \"久太\"}}, {\"Employee\" : {\"EmployeeCode\" : \"0010\", \"Dynamics\" : [{\"Dynamic\" : \"24.0\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}, {\"Dynamic\" : \"\"}], \"FirstName\" : \"山田\", \"LastName\" : \"遠子\"}}]}}}}";
		JsonDeserializer deserializer = new JsonDeserializer();
		JsonObject jsonObject = deserializer.deserializeFromJson(json);
		
		JsonSerializer serializer = new JsonSerializer();
		String result = serializer.serializeFromJsonObject(jsonObject);
		
		assertEquals(json, result);
	}

	@Test void test_list() throws JsonProcessingException {

		String json = "{\"test\" : [1, 2, 3, {\"a\" : \"b\"}, [1, 2, 3]]}";
		JsonDeserializer deserializer = new JsonDeserializer();
		JsonObject jsonObject = deserializer.deserializeFromJson(json);

		JsonSerializer serializer = new JsonSerializer();
		String result = serializer.serializeFromJsonObject(jsonObject);

		assertEquals(json, result);
	}
}
