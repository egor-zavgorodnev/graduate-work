@echo off
java -jar executor.jar "Var x, squ;/n/nProcedure rec;/n Begin/n   x := x - 1;/n   If x > 0 Then/n   Begin/n      Call rec/n   End/n End;/n/n Begin/n   x := 9;/n   Call rec/n End./n"
pause
exit
