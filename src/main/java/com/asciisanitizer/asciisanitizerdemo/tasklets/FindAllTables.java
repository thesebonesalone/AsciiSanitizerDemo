package com.asciisanitizer.asciisanitizerdemo.tasklets;

import com.asciisanitizer.asciisanitizerdemo.TableRepo;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class FindAllTables implements Tasklet {

    String url = "jdbc:mysql://localhost:3306/picolite";

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        //set up Table Repo
        TableRepo tr = TableRepo.getInstance();

        //set up connection
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        Connection con = DriverManager.getConnection(url,"testuser","testpassword" );
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("Show tables");

        while(rs.next()) {
            tr.addToTables(rs.getString(1), "");
        }
        return RepeatStatus.FINISHED;
    }

}
