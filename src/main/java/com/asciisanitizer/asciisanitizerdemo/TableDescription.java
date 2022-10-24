package com.asciisanitizer.asciisanitizerdemo;

import java.util.HashMap;
import java.util.Map;

public class TableDescription {

    String tableName;

    HashMap<String, String> descriptionList;

    String primaryKey;

    public TableDescription(String name, String primaryKey)
    {
        tableName = name;
        this.primaryKey = primaryKey;
        descriptionList = new HashMap<>();
    }

    public String getTableName()
    {
        return tableName;
    }

    public void addColumn(String name, String type)
    {
        descriptionList.put(name, type);
    }

    public HashMap<String, String> getColumns()
    {
        return descriptionList;
    }

    @Override
    public String toString()
    {
        String description = tableName + " : ";
        for (Map.Entry<String, String> e : descriptionList.entrySet())
        {
            description += e.getKey() + " " + e.getValue() + " , ";
        }

        return description;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getPrimaryKey()
    {
        return primaryKey;
    }
}
