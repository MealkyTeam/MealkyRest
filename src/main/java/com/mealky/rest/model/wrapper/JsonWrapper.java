package com.mealky.rest.model.wrapper;

import java.io.IOException;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonWrapper {

	public static <T> String removeFieldsFromPageable(Page<T> list)
	{
		ObjectMapper om = new ObjectMapper();
		JsonNode node;
		String s = null;
		try {
			node = om.readTree(om.writeValueAsString(list));
			((ObjectNode)node).remove("pageable");
			((ObjectNode)node).set("sorted", ((ObjectNode)node).get("sort").get("sorted"));
			((ObjectNode)node).remove("sort");
			((ObjectNode)node).remove("size");
			s = om.writeValueAsString(node);
			return s;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
