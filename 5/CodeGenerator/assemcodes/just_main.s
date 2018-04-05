	.text
	.globl main
main:
	move $fp, $sp
	addi $t2, $0, 800
	sub $sp, $sp, $t2
	sw $ra, 0($fp)
L1:

	addi $t2, $0, 7
	sw $t2, -8($fp)
	lw $t0, -8($fp)
	move $a0, $t0
	jal _printInt
	sw $v0, -12($fp)
	li $t2, 0
	sw $t2, -12($fp)
	j L0
L0:

	lw $t0, 0($fp)
	move $ra, $t0
	move $sp, $fp
	addi $fp, $fp, 800
	j _finish

 # MiniJava Library
	.text
	.globl _printInt
_printInt:
	li $v0, 1      # load print_int code
	syscall        # integer already in $a0
	la $a0, _newl  # load newl string
	li $v0, 4      # load print_string code
	syscall
	jr $ra         # return to caller

	.data
_newl:
	.asciiz "\n"

	.text
	.globl _alloc
_alloc:
	li $v0, 9      # load sbrk code
	syscall        # size already in $a0
	jr $ra         # ptr to memory already in $v0

	.text
	.globl _finish
_finish:
	li $v0, 10     # load exit code
	syscall        # end execution



