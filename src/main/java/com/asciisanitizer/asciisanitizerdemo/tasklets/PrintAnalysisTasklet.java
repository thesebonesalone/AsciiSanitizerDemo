package com.asciisanitizer.asciisanitizerdemo.tasklets;

import com.asciisanitizer.asciisanitizerdemo.Analysis;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class PrintAnalysisTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        Analysis.getInstance().PrintHash();
        Analysis.getInstance().actOnSet();
        return RepeatStatus.FINISHED;
    }
}
