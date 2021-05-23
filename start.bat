@echo off
java -jar executor.jar "var x, squ, cub;/n/nprocedure square;/n begin/n   squ:= x * x/n End;/n/n Procedure cube;/n  Begin/n    cub:= x * x * x/n  End;/n/n Begin/n   x := 1;/n   While x < 10 Do/n   Begin/n       If Odd x Then/n       Begin/n          Call square;/n          Call cube/n       End;/n       x := x + 1/n   End/n End./n"
pause
exit