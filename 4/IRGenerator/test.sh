#!/bin/bash

export MJPROG=$1
export MJOUT="${MJPROG%.*}.out"
export CPROG="${MJPROG%.*}.c" 
#$2
export CEXE=${CPROG%.*}
export COUT="${CPROG%.*}.out"


#echo "Changing the working directory to bin/"
cd bin/

#echo $PWD
#echo $MJPROG
#echo $MJOUT
#echo $CPROG
#echo $CEXE
#echo $COUT

#echo "Generating equivalent C code..."
java parser.NewParser $PWD/../../generator_tests/$MJPROG > $PWD/../generator_tests_c/$CPROG 


#echo "Compiling the equivalent C file..."
gcc -o $PWD/../generator_tests_c/$CEXE $PWD/../generator_tests_c/$CPROG


#echo "Running C program and writing the output to .out file..."
$PWD/../generator_tests_c/$CEXE > $PWD/../generator_tests_c/$COUT


#echo "Difference: \n"
diff $PWD/../../generator_tests/$MJOUT $PWD/../generator_tests_c/$COUT


echo "MiniJava output:" 
cat $PWD/../../generator_tests/$MJOUT 

echo "C output:" 
cat $PWD/../generator_tests_c/$COUT
#cd ../
