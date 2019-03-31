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
			Long date;
			int loop = ((ObjectNode)node).get("numberOfElements").asInt();
			JsonNode tmp;
			for(int i=0;i<loop;i++)
			{
				date = ((ObjectNode)node).get("content").get(i).get("created").asLong();
				tmp = ((ObjectNode)node).get("content").get(i);
				((ObjectNode)tmp).put("created", new java.util.Date(date).toString());
			}
			((ObjectNode)node).remove("created");
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
