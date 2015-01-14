package com.tevinjeffrey.rutgerssoc;

/**
 * Created by Tevin on 1/14/2015.
 */
public class Subject {
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getModifiedDescription() {
        return modifiedDescription;
    }

    public void setModifiedDescription(String modifiedDescription) {
        this.modifiedDescription = modifiedDescription;
    }

    String description;
    int code;
    String modifiedDescription;
}
