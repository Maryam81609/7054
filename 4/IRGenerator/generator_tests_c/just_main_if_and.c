#include <stdio.h>

void _alloc();
void _printInt();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32, t33;
  fp = 4999;
  sp = fp-400;
  L15:
  t32 = 1;
  if(4 < 6) goto L6;
  L7:
  t32 = 0;
  L6:
  if(t32 != 0) goto L13;
  L1:
  a0 = 0;
  _printInt();
  L2:
  rv = 0;
  goto L14;
  L13:
  t33 = 1;
  if(9 < (2 * 4)) goto L11;
  L12:
  t33 = 0;
  L11:
  if(t33 == 0) goto L1;
  L0:
  a0 = 1;
  _printInt();
  goto L2;
  L14:
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
