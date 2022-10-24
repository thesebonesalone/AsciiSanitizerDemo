package com.asciisanitizer.asciisanitizerdemo.config;

import com.asciisanitizer.asciisanitizerdemo.ChunkListenerGetResource;
import com.asciisanitizer.asciisanitizerdemo.Line;
import com.asciisanitizer.asciisanitizerdemo.processors.AsciiProcessor;
import com.asciisanitizer.asciisanitizerdemo.tasklets.DiscoverTables;
import com.asciisanitizer.asciisanitizerdemo.tasklets.FindAllTables;
import com.asciisanitizer.asciisanitizerdemo.tasklets.PrintAnalysisTasklet;
import com.asciisanitizer.asciisanitizerdemo.tasklets.Sanitize;
import com.asciisanitizer.asciisanitizerdemo.writers.AsciiWriter;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import java.io.IOException;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Bean
    public ChunkListenerGetResource chunkListener()
    {
        return new ChunkListenerGetResource();
    }

    @Bean
    public FlatFileItemReader<Line> flatReader() {
        FlatFileItemReader<Line> reader = new FlatFileItemReader<Line>();

        reader.setLinesToSkip(1);

        reader.setLineMapper(new DefaultLineMapper<>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[]{"value"});
                        setStrict(false);
                    }
                });

                setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
                    {
                        setTargetType(Line.class);
                    }
                });
            }
        });

        return reader;
    }

    @Bean
    public MultiResourceItemReader<Line> reader() {
        Resource[] resources = null;
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        try {
            resources = patternResolver.getResources("file:C:/AsciiSanitizerDemo/src/main/resources/data/*");
        } catch (IOException e) {
            e.printStackTrace();
        }


        MultiResourceItemReader<Line> reader = new MultiResourceItemReader<Line>();
        reader.setResources(resources);
        reader.setDelegate(flatReader());

        return reader;
    }

    //processor
    @Bean
    public ItemProcessor<Line, Line> processor() {
        return new AsciiProcessor();
    }

    //basic test writer
    @Bean
    public ItemWriter<Line> writer() {
        return new AsciiWriter();
    }

    @Bean
    TaskExecutor multiThreadedTaskExecutor() {
        ThreadPoolTaskExecutor threads = new ThreadPoolTaskExecutor();
        threads.setMaxPoolSize(8);
        threads.setCorePoolSize(4);
        threads.afterPropertiesSet();

        return threads;
    }

    @Bean
    public Step printStep()
    {
        return stepBuilderFactory
                .get("printTasklet")
                .tasklet(new PrintAnalysisTasklet())
                .build();
    }

    @Bean
    public Step prepDB()
    {
        //set up db for processing
        return stepBuilderFactory
                .get("discoverDB")
                .tasklet(new FindAllTables())
                .build();
    }

    @Bean
    public Step discoverDB()
    {
        return stepBuilderFactory
                .get("findDB")
                .tasklet(new DiscoverTables())
                .build();
    }

    @Bean
    public Step sanitizeDB()
    {
        return stepBuilderFactory
                .get("sanitizeDB")
                .tasklet(new Sanitize())
                .build();
    }

    @Bean
    public Step basicProcessorStep()
    {
        return stepBuilderFactory.get("asciiProcessorStep")
                .listener(chunkListener())
                .<Line, Line>chunk(20)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                //.taskExecutor(multiThreadedTaskExecutor())
                .build();
    }

    @Bean
    public Job buildJob()
    {
        return jobBuilderFactory.get("FileTreeJob")
                .incrementer(new RunIdIncrementer())
                //.start(basicProcessorStep())
                .start(prepDB())
                .next(discoverDB())
                .next(sanitizeDB())
                //.next(printStep())
                .build();
    }



}
