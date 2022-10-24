package com.asciisanitizer.asciisanitizerdemo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Analysis {

    private HashMap<Integer, Integer> basicMap;

    private HashMap<Integer, Set<Character>> charSet;

    private static Analysis singleton;

    private Analysis()
    {
        basicMap = new HashMap<>();
        charSet = new HashMap<>();
    }

    public void addCharToSet(Integer set, Character ch) {
        synchronized (charSet) {
            if (!charSet.containsKey(set))
            {
                charSet.put(set, new HashSet<>());
            }
            charSet.get(set).add(ch);
        }
    }

    public static Analysis getInstance()
    {
        if (singleton == null)
        {
            singleton = new Analysis();
        }
        return singleton;
    }

    public void addToTally(char c)
    {
        Integer i = Character.getType(c);
        synchronized (basicMap)
        {
            if (!basicMap.containsKey(i))
            {
                basicMap.put(i,0);
            }

            basicMap.put(i, basicMap.get(i) + 1);
        }
    }

    public void PrintHash()
    {
        try {
            FileWriter newWriter = new FileWriter("logfile.txt", true);
            for (Map.Entry e : basicMap.entrySet()) {
                String s = "Character category " + e.getKey() + " had " + e.getValue() + " entries.";

                newWriter.write(s);

            }
            newWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void actOnSet()
    {
        try {
            FileWriter newWriter = new FileWriter("logfile.txt", true);
            for (Map.Entry e : charSet.entrySet()) {
                String s = "";
                for (Character c : (Set<Character>) e.getValue()) {
                    s += c;
                }
                newWriter.write("Set " + e.getKey() + " contains \n");
                newWriter.write(s + "\n");
            }
            newWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
