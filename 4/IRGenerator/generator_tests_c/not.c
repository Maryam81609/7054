#include <stdio.h>

void _alloc();
void _printInt();
void C_m();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32;
  fp = 4999;
  sp = fp-400;
  L6:
  a0 = 0;
  _alloc();
  rv;
  t32 = rv;
  t32;
  a0 = 78;
  a1 = t32;
  C_m();
  rv;
  a0 = rv;
  _printInt();
  rv = 0;
  goto L5;
  L5:
  sp = fp;
  fp += 400;
  return 0;
}

void C_m() {
  int t32, t33, t34, t35;
  fp = sp;
  sp -= 400;
  L8:
  t33 = a1;
  t34 = a0;
  t35 = 1;
  if(100 < t34) goto L3;
  L4:
  t35 = 0;
  L3:
  if((1 - t35) != 0) goto L0;
  L1:
  t32 = 0;
  L2:
  rv = t32;
  goto L7;
  L0:
  t32 = t34;
  goto L2;
  L7:
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
