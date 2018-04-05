	.text
	.globl main
main:
	move $fp, $sp
	addi $t2, $0, 800
	sub $sp, $sp, $t2
	sw $ra, 0($fp)
L4:

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
	jal A_m
	sw $v0, -12($fp)
	lw $t0, -12($fp)
	move $a0, $t0
	jal _printInt
	sw $v0, -12($fp)
	li $t2, 0
	sw $t2, -12($fp)
	j L3
L3:

	lw $t0, 0($fp)
	move $ra, $t0
	move $sp, $fp
	addi $fp, $fp, 800
	j _finish
A_m:
	move $fp, $sp
	addi $t2, $0, 800
	sub $sp, $sp, $t2
	sw $ra, 0($fp)
L6:

	lw $t0, -12($fp)
	move $t2, $t0
	sw $t2, -8($fp)
	addi $t2, 1, 1
	sw $t2, -20($fp)
	addi $t2, $0, 4
	sw $t2, -24($fp)
	lw $t0, -20($fp)
	lw $t1, -24($fp)
	mul $t2, $t0, $t1
	sw $t2, -16($fp)
	lw $t0, -16($fp)
	move $a0, $t0
	jal _alloc
	sw $v0, -28($fp)
	lw $t0, -28($fp)
	move $t2, $t0
	sw $t2, -32($fp)
	lw $t0, -28($fp)
	lw $t2, 0($t0)
	sw $t2, -36($fp)
	li $t2, 1
	sw $t2, -36($fp)
	li $t2, 1
	sw $t2, -40($fp)
L0:

	addi $t2, $0, 0
	sw $t2, -44($fp)
	lw $t0, -44($fp)
	lw $t1, -40($fp)
	blt $t0, $t1, L1
L2:

	lw $t0, -32($fp)
	move $t2, $t0
	sw $t2, -48($fp)
	addi $t2, 0, 1
	sw $t2, -64($fp)
	addi $t2, $0, 4
	sw $t2, -68($fp)
	lw $t0, -64($fp)
	lw $t1, -68($fp)
	mul $t2, $t0, $t1
	sw $t2, -60($fp)
	lw $t0, -48($fp)
	lw $t1, -60($fp)
	add $t2, $t0, $t1
	sw $t2, -56($fp)
	lw $t0, -56($fp)
	lw $t2, 0($t0)
	sw $t2, -52($fp)
	lw $t0, -52($fp)
	move $a0, $t0
	jal _printInt
	sw $v0, -28($fp)
	li $t2, 1
	sw $t2, -28($fp)
	j L5
L1:

	addi $t2, $0, 4
	sw $t2, -84($fp)
	lw $t0, -40($fp)
	lw $t1, -84($fp)
	mul $t2, $t0, $t1
	sw $t2, -80($fp)
	lw $t0, -28($fp)
	lw $t1, -80($fp)
	add $t2, $t0, $t1
	sw $t2, -76($fp)
	lw $t0, -76($fp)
	lw $t2, 0($t0)
	sw $t2, -72($fp)
	li $t2, 0
	sw $t2, -72($fp)
	lw $t0, -40($fp)
	addi $t2, $t0, -1
	sw $t2, -88($fp)
	lw $t0, -88($fp)
	move $t2, $t0
	sw $t2, -40($fp)
	j L0
L5:

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
