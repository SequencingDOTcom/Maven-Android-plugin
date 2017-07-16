package com.sequencing.oauth.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Helper for common JSON data manipulations routines
 */
public class JsonHelper {
    
	/**
	 * Returns value of json node by field name
	 */
    public static String getField(String json, String fieldName) {
        JsonObject o = new JsonParser().parse(json).getAsJsonObject();
        return o.get(fieldName).getAsString();
    }
    
    /**
	 * Returns value of json node by field name
	 */
    public static JsonArray toJsonArray(String json) {
        return new JsonParser().parse(json).getAsJsonArray();
    }
    
    /**
	 * Converts JSON array into Java array
	 */
    public static String[] parseJsonArrayToStringArray(JsonArray jsonArray){
        Iterator<JsonElement> jsonArrayIterator = jsonArray.iterator();
        List<String> list = new ArrayList<String>();

        while (jsonArrayIterator.hasNext()) {
            JsonElement element = jsonArrayIterator.next();
            JsonElement name = element.getAsJsonObject().get("Name");
            JsonElement friendlyDesc1 = element.getAsJsonObject().get("FriendlyDesc1");
            JsonElement friendlyDesc2 = element.getAsJsonObject().get("FriendlyDesc2");

            list.add(name.getAsString() + ": " + friendlyDesc1.getAsString() + ", " + friendlyDesc2.getAsString());
        }

        String []resultStringArray = new String[list.size()];
        list.toArray(resultStringArray);
        return resultStringArray;
    }
    
    /**
	 * Convert json to java object
	 */
    public static <T> T convertToJavaObject(String json, Class<T> classOf){
    	Gson gson = new Gson();
    	T object =  gson.fromJson(json, classOf);
    	return object;
    }
    
    /**
	 * Convert java object to json format
	 */
    public static <T> String convertToJson(T object){
    	return new Gson().toJson(object);
    }
}
