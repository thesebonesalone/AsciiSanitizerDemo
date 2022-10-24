package com.asciisanitizer.asciisanitizerdemo.tasklets;



import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrepOutLogs implements Tasklet {


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {


        try
        {
            File logFile = new File("logfile.txt");
            FileWriter newWriter = new FileWriter(logFile, false);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDateTime now = LocalDateTime.now();
            newWriter.write(dtf.format(now) + "\n");
            newWriter.close();

            File badLines = new File("badLines.txt");
            FileWriter writer2 = new FileWriter(badLines, false);
            writer2.write("");
            writer2.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        //open log file





        return RepeatStatus.FINISHED;
    }
}
