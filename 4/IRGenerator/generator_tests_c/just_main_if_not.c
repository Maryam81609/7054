#include <stdio.h>

void _alloc();
void _printInt();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32, t33, t34;
  fp = 4999;
  sp = fp-400;
  L11:
  t34 = 1;
  t32 = 1;
  if(4 < 3) goto L3;
  L4:
  t32 = 0;
  L3:
  if(t32 != 0) goto L7;
  L9:
  t34 = 0;
  L8:
  if((1 - t34) != 0) goto L0;
  L1:
  a0 = 1;
  _printInt();
  L2:
  rv = 0;
  goto L10;
  L7:
  t33 = 1;
  if(6 < 7) goto L5;
  L6:
  t33 = 0;
  L5:
  if(t33 != 0) goto L8;
  L12:
  goto L9;
  L0:
  a0 = 0;
  _printInt();
  goto L2;
  L10:
  sp = fp;
  fp += 400;
  return 0;
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
