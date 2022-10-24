package com.asciisanitizer.asciisanitizerdemo.tasklets;

import com.asciisanitizer.asciisanitizerdemo.Item;
import com.asciisanitizer.asciisanitizerdemo.TableDescription;
import com.asciisanitizer.asciisanitizerdemo.TableRepo;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.sql.*;
import java.util.ArrayList;

public class Sanitize implements Tasklet {

    private final String regex = "[^A-zÀ-ú\\d\\s(~!@#$%^&*.,?\\-\\/':\"=_�;{}\\|\\(\\)<>\\[\\])\\p{M}]+";

    String url = "jdbc:mysql://localhost:3306/picolite";

    public String clean(String s)
    {
        return s.replaceAll(regex, "");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        TableRepo tb = TableRepo.getInstance();
        ArrayList<TableDescription> tables = tb.getTables();


        //set up conn
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        Connection con = DriverManager.getConnection(url,"testuser","testpassword" );

        for (TableDescription t : tables)
        {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + t.getTableName());

            System.out.println(t.getTableName());

            ResultSetMetaData rsmd;

            //build item
            while (rs.next())
            {
                rsmd = rs.getMetaData();
                int nOfCol = rsmd.getColumnCount();
                //build item
                Item item = new Item(t);
                for (int i = 1; i < nOfCol; i++)
                {
                    item.addValue(rsmd.getColumnName(i), clean(rs.getObject(i).toString()));
                }
                System.out.println(item.insertString());

            }
        }

        return RepeatStatus.FINISHED;
    }
}
