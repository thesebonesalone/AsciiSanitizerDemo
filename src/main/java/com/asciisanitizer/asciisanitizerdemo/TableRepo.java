package com.asciisanitizer.asciisanitizerdemo;

import java.util.ArrayList;

public class TableRepo {

    private ArrayList<TableDescription> tables;


    private static TableRepo singleton;

    private TableRepo()
    {
        tables = new ArrayList<>();
    }

    public static TableRepo getInstance()
    {
        if (singleton == null)
        {
            synchronized (TableRepo.class)
            {
                if (singleton == null)
                {
                    singleton = new TableRepo();
                }
            }
        }
        return singleton;
    }

    public void addToTables(String s, String prime)
    {
        tables.add(new TableDescription(s, prime));
    }

    public ArrayList<TableDescription> getTables()
    {
        return tables;
    }

    public void setTables(ArrayList<TableDescription> newTables)
    {
        tables = newTables;
    }
}
