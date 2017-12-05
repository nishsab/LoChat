package utils;

import org.json.JSONException;
import org.json.JSONObject;

import models.ChatMessage;
import models.Room;

/**
 * Created by nishsab on 12/2/17.
 */

public class JsonToMessage {
    public static ChatMessage jsonToMessage(JSONObject jsonObject) {
        try {
            ChatMessage msg = new ChatMessage();
            msg.setId((int) jsonObject.get("id"));
            msg.setUserId((int) jsonObject.get("user_id"));
            msg.setRoomId((int) jsonObject.get("room_id"));
            msg.setText((String) jsonObject.get("text"));
            msg.setCreated_at((String) jsonObject.get("created_at"));
            msg.setUpdated_at((String) jsonObject.get("updated_at"));
            msg.setUserEmail((String) jsonObject.get("user_email"));
            return msg;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
