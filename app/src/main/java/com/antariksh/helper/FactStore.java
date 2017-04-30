package com.antariksh.helper;

/**
 * Created by Aditya Singh on 25-04-2017.
 */

public class FactStore {
    String topic,picture,description;

    public FactStore(String topic, String picture, String description) {
        this.topic = topic;
        this.picture = picture;
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }
}
