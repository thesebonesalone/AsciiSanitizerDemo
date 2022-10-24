package com.asciisanitizer.asciisanitizerdemo.processors;

import com.asciisanitizer.asciisanitizerdemo.Line;
import org.springframework.batch.item.ItemProcessor;

import java.io.FileWriter;
import java.io.IOException;

public class AsciiProcessor implements ItemProcessor<Line, Line> {

    private final String regex = "[^A-zÀ-ú\\d\\s(~!@#$%^&*.,?\\-\\/':\"=_�;{}\\|\\(\\)<>\\[\\])\\p{M}]+";

    @Override
    public Line process(Line s) throws Exception {

        boolean toPrint = false;
        //Analysis a = Analysis.getInstance();

        String parse = s.getValue();
        String parse2 = parse.replaceAll(regex," ");

        if (!parse2.equals(parse))
        {
            toPrint = true;

        }

        if (toPrint)
        {
            try {
                FileWriter fw = new FileWriter("badLines.txt", true);
                fw.write(parse + "\n");
                fw.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            //delete line in SQL
            parse2 = "";

        }
        s.setValue(parse2);
        return s;
    }
}
