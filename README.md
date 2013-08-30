This is a java program I've written for work to create somewhat random SQL statements
from an input text file containing DB names, table names, column names, datatypes, etc.
This is a hack job - therefore, it's not even close to being considered efficient, reliable,
or very useful to anyone but me!

Ideally the output file (comes out as random.txt) is run through SQLcmd to generate SQL
traffic (hence the "GO" printed after every statement). I've also included some injection
attack features, to somewhat randomly intersperse SQLi based on input.
