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
  L1:
  a0 = 20;
  _alloc();
  rv;
  heap[(rv)/4] = 4;
  heap[((rv + 4))/4] = 0;
  heap[((rv + 8))/4] = 0;
  heap[((rv + 12))/4] = 0;
  heap[((rv + 16))/4] = 0;
  t32;
  a0 = heap[((heap[(t32)/4] + ((4 + 0) * 4)))/4];
  _printInt();
  rv = 0;
  goto L0;
  L0:
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
