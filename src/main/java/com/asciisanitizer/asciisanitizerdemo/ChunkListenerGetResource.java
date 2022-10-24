package com.asciisanitizer.asciisanitizerdemo;

import org.springframework.aop.framework.Advised;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.file.MultiResourceItemReader;


import java.util.ArrayList;
import java.util.List;



public class ChunkListenerGetResource  implements ChunkListener, StepExecutionListener {

    private StepExecution stepExecution;
    private Object proxy;
    private final List<String> fileNames = new ArrayList<>();

    public void setProxy(Object mrir) {
        this.proxy = mrir;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution){
        return stepExecution.getExitStatus();
    }

    @Override
    public void beforeChunk(ChunkContext cc)
    {

        if (proxy instanceof Advised)
        {
            try
            {
                Advised advised = (Advised) proxy;
                Object obj = advised.getTargetSource().getTarget();
                MultiResourceItemReader mrirTarget = (MultiResourceItemReader) obj;
                if (mrirTarget != null
                    && mrirTarget.getCurrentResource() != null
                    && !fileNames.contains(mrirTarget.getCurrentResource().getFilename())) {
                    String filename = mrirTarget.getCurrentResource().getFilename();
                    fileNames.add(filename);
                    String index = String.valueOf(fileNames.indexOf(filename));
                    stepExecution.getExecutionContext().put("current.resource" + index, filename);
                }

            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void afterChunk(ChunkContext cc) {

    }

    @Override
    public void afterChunkError(ChunkContext cc) {

    }


}
