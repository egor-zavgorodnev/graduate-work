# Translator PL/0 to Java bytecode

Translation to Java bytecode and execution in JVM

## PL/0 BNF

``` 
program = block "." 

block = [ "const" ident "=" number {"," ident "=" number} ";"]
        [ "var" ident {"," ident} ";"]
        { "procedure" ident ";" block ";" } statement .

statement = [ ident ":=" expression | "call" ident 
              | "?" ident | "!" expression 
              | "begin" statement {";" statement } "end" 
              | "if" condition "then" statement 
              | "while" condition "do" statement ].

condition = "odd" expression |
            expression ("="|"#"|"<"|"<="|">"|">=") expression .

expression = [ "+"|"-"] term { ("+"|"-") term}.

term = factor {("*"|"/") factor}.

factor = ident | number | "(" expression ")".

```

## Specification

-	Only Integer data type
-	Arithmetic operations and comparation operators
-	Internal function ```odd```, that check whether number is odd
- IO functions is absent. Program print variable value at the change value moment.
-	If-then, While-Do construtions
-	Custom procedures without argument passing

## Example

```
var n, f;  
begin  
n := 0;  
f := 1;  
while n # 16 do  
begin  
n := n + 1;  
f := f * n;  
end;  
end. 
```

Output:
```
0
1
1
1
2
2
3
6
4
24
5
120
6
720
7
5040
8
40320
9
362880
10
3628800
11
39916800
12
479001600
13
1932053504
14
1278945280
15
2004310016
16
2004189184
```


