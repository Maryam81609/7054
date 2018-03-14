#include <stdio.h>

void _alloc();
void _printInt();
void Compute_init();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32;
  fp = 4999;
  sp = fp-400;
  L13:
  a0 = 20;
  _alloc();
  rv;
  t32 = rv;
  heap[((rv + 0))/4] = 0;
  heap[((rv + 4))/4] = 0;
  heap[((rv + 8))/4] = 0;
  heap[((rv + 12))/4] = 0;
  heap[((rv + 16))/4] = 0;
  t32;
  a0 = t32;
  Compute_init();
  rv;
  a0 = rv;
  _printInt();
  rv = 0;
  goto L12;
  L12:
  sp = fp;
  fp += 400;
  return 0;
}

void Compute_init() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L15:
  t33 = a0;
  t32 = 100;
  heap[((t33 + 0))/4] = 0;
  heap[((t33 + 4))/4] = 0;
  heap[((t33 + 8))/4] = 0;
  heap[((t33 + 12))/4] = 0;
  heap[((t33 + 16))/4] = 0;
  L0:
  if(heap[((t33 + 0))/4] < t32) goto L1;
  L2:
  rv = heap[((t33 + 16))/4];
  goto L14;
  L1:
  L3:
  if(heap[((t33 + 4))/4] < heap[((t33 + 0))/4]) goto L4;
  L5:
  heap[((t33 + 0))/4] = (heap[((t33 + 0))/4] + 1);
  goto L0;
  L4:
  L6:
  if(heap[((t33 + 8))/4] < heap[((t33 + 4))/4]) goto L7;
  L8:
  heap[((t33 + 4))/4] = (heap[((t33 + 4))/4] + 1);
  goto L3;
  L7:
  L9:
  if(heap[((t33 + 12))/4] < heap[((t33 + 8))/4]) goto L10;
  L11:
  heap[((t33 + 8))/4] = (heap[((t33 + 8))/4] + 1);
  goto L6;
  L10:
  heap[((t33 + 16))/4] = (heap[((t33 + 16))/4] + heap[((t33 + 12))/4]);
  heap[((t33 + 12))/4] = (heap[((t33 + 12))/4] + 1);
  goto L9;
  L14:
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
