package com.asciisanitizer.asciisanitizerdemo.tasklets;

import com.asciisanitizer.asciisanitizerdemo.Item;
import com.asciisanitizer.asciisanitizerdemo.TableDescription;
import com.asciisanitizer.asciisanitizerdemo.TableRepo;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

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
        Connection con = DriverManager.getConnection(url, "testuser", "testpassword" );

        //second conn for insert
        Connection inCon = DriverManager.getConnection(url,"testuser", "testpassword" );

        //add multithreading
        for (TableDescription t : tables)
        {
            Statement st = con.createStatement();
            boolean tapped = false;
            int offset = 0;
            while (!tapped) {
                ResultSet rs = st.executeQuery("SELECT * FROM " + t.getTableName() + " LIMIT 40 OFFSET " + offset);

                int count = 0;
                System.out.println(t.getTableName());
                ResultSetMetaData rsmd;
                //build item
                while (rs.next()) {
                    count += 1;
                    rsmd = rs.getMetaData();


                    int nOfCol = rsmd.getColumnCount();
                    //build item
                    Item item = new Item(t);
                    for (int i = 1; i < nOfCol; i++) {
                        if (rsmd.getColumnType(i) == 12 || rsmd.getColumnType(i) == -16)
                        {
                            item.addValue(rsmd.getColumnName(i), clean(rs.getObject(i).toString()));
                        } else {
                            item.addValue(rsmd.getColumnName(i), rs.getObject(i).toString());
                        }
                    }
                    Statement inState = inCon.createStatement();
                    inState.execute(item.insertString());
                    System.out.println(item.insertString());

                }
                if (count != 40)
                {
                    tapped = true;
                }
                offset += 40;
            }
        }

        return RepeatStatus.FINISHED;
    }
}
