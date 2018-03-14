#include <stdio.h>

void _alloc();
void _printInt();
void A6_fun1();
void A6_fun2();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32;
  fp = 4999;
  sp = fp-400;
  L7:
  a0 = 4;
  _alloc();
  rv;
  t32 = rv;
  heap[((rv + 0))/4] = 0;
  t32;
  a0 = 5;
  a1 = t32;
  A6_fun1();
  rv;
  a0 = rv;
  _printInt();
  a0 = 14;
  _printInt();
  rv = 0;
  goto L6;
  L6:
  sp = fp;
  fp += 400;
  return 0;
}

void A6_fun1() {
  int t32, t33, t34;
  fp = sp;
  sp -= 400;
  L9:
  t33 = a1;
  t34 = a0;
  if(t34 < 1) goto L0;
  L1:
  t33;
  a0 = (t34 - 1);
  a1 = t33;
  A6_fun2();
  rv;
  t32 = (2 * rv);
  L2:
  rv = t32;
  goto L8;
  L0:
  t32 = 1;
  goto L2;
  L8:
  sp = fp;
  fp += 400;
}

void A6_fun2() {
  int t32, t33, t34;
  fp = sp;
  sp -= 400;
  L11:
  t33 = a1;
  t34 = a0;
  if(t34 < 1) goto L3;
  L4:
  t33;
  a0 = (t34 - 1);
  a1 = t33;
  A6_fun1();
  rv;
  t32 = (1 + rv);
  L5:
  rv = t32;
  goto L10;
  L3:
  t32 = 1;
  goto L5;
  L10:
  sp = fp;
  fp += 400;
}

void _alloc() {
  static int heap_size = 0;
  int i;

  rv = heap_size;
  heap_size += a0;

  // put "garbage" in heap to start
  for(i = rv; i < heap_size; i++)
    heap[i] =-1;
  }

void _printInt() {
  printf("%d\n", a0);
}
