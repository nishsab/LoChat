package models;

/**
 * Created by nishsab on 11/18/17.
 */

public class Room {
    private int id;
    private String name;
    private int user_id;
    private double lat;
    private double lon;
    private double radius;
    private String created_at;
    private String updated_at;

    public Room() {

    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getRadius() {
        return radius;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getName() {
        return name;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
