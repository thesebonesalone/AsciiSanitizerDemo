package com.asciisanitizer.asciisanitizerdemo.tasklets;

import com.asciisanitizer.asciisanitizerdemo.TableDescription;
import com.asciisanitizer.asciisanitizerdemo.TableRepo;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DiscoverTables implements Tasklet {

    String url = "jdbc:mysql://localhost:3306/picolite";

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        //set up TableRepo

        TableRepo tb = TableRepo.getInstance();
        ArrayList<TableDescription> tables = tb.getTables();

        //set up conn
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        Connection con = DriverManager.getConnection(url,"testuser","testpassword" );


        for (TableDescription t : tables)
        {
            System.out.println(t.getTableName());
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("DESCRIBE " + t.getTableName());

            while (rs.next())
            {
                t.addColumn(rs.getString(1), rs.getString(2));
                if (rs.getString(4).equals("PRI"))
                {
                    t.setPrimaryKey(rs.getString(1));
                }
            }
        }

        tb.setTables(tables);

        for (TableDescription t: tables)
        {
            System.out.println(t);
        }

        return RepeatStatus.FINISHED;
    }
}
