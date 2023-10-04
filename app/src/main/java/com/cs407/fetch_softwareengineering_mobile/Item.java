package com.cs407.fetch_softwareengineering_mobile;

public class Item {
    private final int id;
    private final int listId;
    private final String name;

    public Item(int id, int listId, String name) {
        this.id = id;
        this.listId = listId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }
}

