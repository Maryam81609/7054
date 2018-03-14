#include <stdio.h>

void _alloc();
void _printInt();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32;
  fp = 4999;
  sp = fp-400;
  L0:
  if(0 != 0) goto L1;
  L2:
  rv = 0;
  goto L3;
  L1:
  a0 = 222;
  _printInt();
  goto L0;
  L3:
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
