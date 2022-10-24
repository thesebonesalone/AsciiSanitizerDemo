package com.asciisanitizer.asciisanitizerdemo.writers;


import com.asciisanitizer.asciisanitizerdemo.Line;
import org.springframework.batch.item.ItemWriter;

import java.io.Serializable;
import java.util.List;

public class AsciiWriter implements ItemWriter<Line> {

    @Override
    public void write(List<? extends Line> list) throws Exception {
        for (Line item: list)
        {

        }
    }
}
