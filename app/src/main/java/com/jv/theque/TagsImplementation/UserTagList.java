package com.jv.theque.TagsImplementation;

import com.jv.theque.GameImplementation.UserGameList;
import com.jv.theque.MainActivity;
import com.jv.theque.UserData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class UserTagList extends Observable implements Serializable  {
    private List<Tag> tagList;

    public UserTagList(UserData userData) {
        addObserver(userData);
    }

    public UserTagList(UserData userData, List<Tag> tagList) {
        addObserver(userData);
        this.tagList = tagList;
    }

    public List<Tag> getList() {
        return tagList;
    }

    public Tag find(String name) {

        for (Tag tag : tagList) {
            if (tag.getName() == name) {
                return tag;
            }
        }

        return null;
    }

    public Tag find(String name, Tag.TagType tagType) {

        for (Tag tag : tagList) {
            if (tag.getName() == name && tag.getType() == tagType) {
                return tag;
            }
        }

        return null;
    }

    public boolean add(Tag tag) {
        if (tagList.contains(tag)) {
            return false;
        }
        tagList.add(tag);
        return true;
    }

    public boolean remove(Tag tag) {
        if (!tagList.contains(tag)) {
            return false;
        }
        tagList.remove(tag);
        return true;
    }

    public UserTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }
}
