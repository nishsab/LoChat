package utils;

import org.json.JSONException;
import org.json.JSONObject;

import models.Room;

/**
 * Created by nishsab on 11/19/17.
 */

public class JsonToRoom {
    public static Room jsonToRoom(JSONObject jsonObject) {
        try {
            Room room = new Room();
            room.setId((int) jsonObject.get("id"));
            room.setName((String) jsonObject.get("name"));
            room.setUser_id((int) jsonObject.get("user_id"));
            room.setLat(((Number) jsonObject.get("lat")).doubleValue());
            room.setLon(((Number) jsonObject.get("lon")).doubleValue());
            room.setRadius(((Number) jsonObject.get("radius")).doubleValue());
            room.setCreated_at((String) jsonObject.get("created_at"));
            room.setUpdated_at((String) jsonObject.get("updated_at"));
            return room;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
