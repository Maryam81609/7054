	.text
	.globl main
main:
	move $fp, $sp
	addi $t2, $0, 800
	sub $sp, $sp, $t2
	sw $ra, 0($fp)
L5:

	addi $t2, $0, 0
	sw $t2, -8($fp)
	lw $t0, -8($fp)
	move $a0, $t0
	jal _alloc
	sw $v0, -12($fp)
	lw $t0, -12($fp)
	move $t2, $t0
	sw $t2, -16($fp)
	lw $t0, -16($fp)
	move $a0, $t0
	addi $t2, $0, 1
	sw $t2, -20($fp)
	lw $t0, -20($fp)
	move $a1, $t0
	addi $t2, $0, 78
	sw $t2, -24($fp)
	lw $t0, -24($fp)
	move $a2, $t0
	jal C_m
	sw $v0, -12($fp)
	lw $t0, -12($fp)
	move $a0, $t0
	jal _printInt
	sw $v0, -12($fp)
	li $t2, 0
	sw $t2, -12($fp)
	j L4
L4:

	lw $t0, 0($fp)
	move $ra, $t0
	move $sp, $fp
	addi $fp, $fp, 800
	j _finish
C_m:
	move $fp, $sp
	addi $t2, $0, 800
	sub $sp, $sp, $t2
	sw $ra, 0($fp)
L7:

	lw $t0, -12($fp)
	move $t2, $t0
	sw $t2, -8($fp)
	lw $t0, -20($fp)
	move $t2, $t0
	sw $t2, -16($fp)
	lw $t0, -28($fp)
	move $t2, $t0
	sw $t2, -24($fp)
	addi $t2, $0, 0
	sw $t2, -32($fp)
	lw $t0, -16($fp)
	lw $t1, -32($fp)
	bne $t0, $t1, L3
L1:

	li $t2, 56
	sw $t2, -36($fp)
L2:

	lw $t0, -36($fp)
	move $t2, $t0
	sw $t2, -40($fp)
	j L6
L3:

	addi $t2, $0, 100
	sw $t2, -44($fp)
	lw $t0, -44($fp)
	lw $t1, -24($fp)
	bge $t0, $t1, L1
L0:

	li $t2, 14
	sw $t2, -36($fp)
	j L2
L6:

	lw $t0, 0($fp)
	move $ra, $t0
	move $sp, $fp
	addi $fp, $fp, 800
	jr $ra

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

