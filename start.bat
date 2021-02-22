@echo off
java -jar executor.jar "Var x, squ, cub;/n/nProcedure square;/n Begin/n   squ:= x * x;/n   Call cube/n End;/n/n Procedure cube;/n  Begin/n    cub:= x * x * x/n  End;/n/n Begin/n   x := 1;/n   While x < 10 Do/n   Begin/n      Call square;/n      x := x + 1/n   End/n End./n"
pause