package com.example.chillcup02_ui.data.dto;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;

/**
 * Gson deserializer that accepts either a string ID or an object for the `storeId` field.
 * If the JSON value is a string, it returns that string.
 * If the JSON value is an object, it tries to extract "_id" or "id" field as the id string.
 */
public class StoreIdDeserializer implements JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) return null;
        try {
            if (json.isJsonPrimitive()) {
                return json.getAsString();
            }
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                if (obj.has("_id") && !obj.get("_id").isJsonNull()) return obj.get("_id").getAsString();
                if (obj.has("id") && !obj.get("id").isJsonNull()) return obj.get("id").getAsString();
                // Fall back to name or other field if needed
                if (obj.has("name") && !obj.get("name").isJsonNull()) return obj.get("name").getAsString();
                // As a last resort, return the whole object string
                return obj.toString();
            }
            // Unexpected type
            return json.toString();
        } catch (Exception e) {
            throw new JsonParseException("Failed to deserialize storeId", e);
        }
    }
}
