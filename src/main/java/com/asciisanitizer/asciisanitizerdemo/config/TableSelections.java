package com.asciisanitizer.asciisanitizerdemo.config;

import java.util.ArrayList;

public class TableSelections {

    //since we're doing this as a one timer I'm not too worried about adding these as environment variables.
    //go ahead and add all table names here using all caps
    public static ArrayList<String> toSanitize() {
        ArrayList<String> tables = new ArrayList<>();

        //just ctrl-c this and add table names
        //all names here are from a local table I had
        tables.add("COMMENT");

        return tables;
    }
}
