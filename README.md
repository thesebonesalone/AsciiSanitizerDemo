# AsciiSanitizerDemoBasic rundown of the ASCII sanitizer.

Under Config is the area you add all the table names. Make sure they are uppercase to make everything uniform.

The main logic can be found in the Tasklets directory. 

FindAllTables is the first step in the job. It scans the DB and matches tables to the list defined in TableSelections.
It then creates a TableDescription instance for each and places it in the TableRepo singleton.

The next step is DiscoverTables, this creates a HashMap of each of the table's definition.

The final step is Sanitize. Currently it is single threaded which is a problem. Being a set of SQL inserts it should be easy enough to multithread without much thread locking.
It reads each entry from each table and runs it through the sanitizer process. It then inserts that back in the DB.

As far as optimizations go, the afformentioned multithreading would be important. As well as potentially moving to StringBuilders to save on RAM.

For connection, you'll want to set these in your environment. Especially you'll want to move your DB driver, username and password over to environment variables. Unsure how that's implemented on your end so I've left that up to you
