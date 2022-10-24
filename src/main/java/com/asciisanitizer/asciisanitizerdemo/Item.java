package com.asciisanitizer.asciisanitizerdemo;

import java.util.HashMap;
import java.util.Map;

public class Item {

    TableDescription tableDescription;

    public HashMap<String, String> values;

    public Item(TableDescription td)
    {
        this.tableDescription = td;
        values = new HashMap<>();
    }

    public void addValue(String key, String value)
    {
        values.put(key, value);
    }

    public String insertString()
    {
        String toSql = "UPDATE " + tableDescription.tableName + " SET ";
        for (Map.Entry<String, String> e: values.entrySet())
        {
            if (!e.getKey().equals(tableDescription.primaryKey))
            {
                toSql += e.getKey() + " = '" + e.getValue() + "' , ";
            }
        }
        toSql = toSql.substring(0,toSql.length()-2) + "WHERE " + tableDescription.primaryKey + " = '" + values.get(tableDescription.primaryKey) + "'";
        return toSql;
    }
}
