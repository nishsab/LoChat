package models;

/**
 * Created by nishsab on 12/2/17.
 */

public class ChatMessage {

    private int id;
    private int userId;
    private int roomId;
    private String text;
    private String created_at;
    private String updated_at;
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
            return userId;
        }

    public int getRoomId() { return roomId; }

    public String getText() {
        return text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setId(int id) {
            this.id = id;
        }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRoomId(int roomId) {
        this.userId = roomId;
    }

    public void setText(String text) {
        this.text = text;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


}
