@echo off
java -jar executor.jar " Var n,f;/n Begin/n   n := 0;/n   f := 0;/n   While f < 16 Do/n   Begin/n      If Odd n Then/n      Begin/n         n := n + 1/n      End;/n      f := f + 1/n   End/n  End./n"
pause