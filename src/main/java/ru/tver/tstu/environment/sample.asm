data segment para public "data"
a dw 0
b dw 0
c dw 0
d dw 0
PRINT_BUF DB ' ' DUP(10)
BUFEND DB '$'
data ends
stk segment stack
db 256 dup ("?")
stk ends
code segment para public "code"
main proc
assume cs:code,ds:data,ss:stk
mov ax,data
mov ds,ax
mov a,2
mov b,2
mov c,2
mov ax,2
push ax
mov ax,2
push ax
mov ax,2
push ax
pop bx
pop ax
mul bx
push ax
pop bx
pop ax
add ax,bx
push ax
mov d, ax
push ax
mov ax, d
CALL PRINT
pop ax
mov ax,4c00h
int 21h
main endp
PRINT PROC NEAR
MOV CX, 10
MOV DI, BUFEND - PRINT_BUF
PRINT_LOOP:
MOV DX, 0
DIV CX
ADD DL, '0'
MOV [PRINT_BUF + DI - 1], DL
DEC DI
CMP AL, 0
JNE PRINT_LOOP
LEA DX, PRINT_BUF
ADD DX, DI
MOV AH, 09H
INT 21H
RET
PRINT ENDP
code ends
end main
