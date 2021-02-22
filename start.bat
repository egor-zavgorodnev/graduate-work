@echo off
java -jar executor.jar " Var n;/n Begin/n   n := 0;/n   While n < 16 Do/n   Begin/n      If Odd n Then/n      Begin/n         n := n + 1/n      End/n   End/n  End./n"
pause