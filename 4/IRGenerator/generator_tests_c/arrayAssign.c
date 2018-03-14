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
  L21:
  a0 = 20;
  _alloc();
  rv;
  t32 = rv;
  heap[((rv + 0))/4] = 0;
  heap[((rv + 4))/4] = 0;
  heap[((rv + 8))/4] = 0;
  heap[((rv + 12))/4] = 0;
  heap[((rv + 16))/4] = 0;
  a0 = t32;
  Compute_init();
  rv;
  a0 = rv;
  _printInt();
  rv = 0;
  goto L20;
  L20:
  sp = fp;
  fp += 400;
  return 0;
}

void Compute_init() {
  int t32, t33, t34, t35, t36, t37;
  fp = sp;
  sp -= 400;
  L23:
  t33 = a0;
  t32 = 100;
  heap[((t33 + 0))/4] = 0;
  heap[((t33 + 4))/4] = 0;
  heap[((t33 + 8))/4] = 0;
  heap[((t33 + 12))/4] = 0;
  heap[((t33 + 16))/4] = 0;
  L0:
  t34 = 1;
  if(heap[((t33 + 0))/4] < t32) goto L3;
  L4:
  t34 = 0;
  L3:
  if(t34 != 0) goto L1;
  L2:
  rv = heap[((t33 + 16))/4];
  goto L22;
  L1:
  L5:
  t35 = 1;
  if(heap[((t33 + 4))/4] < heap[((t33 + 0))/4]) goto L8;
  L9:
  t35 = 0;
  L8:
  if(t35 != 0) goto L6;
  L7:
  heap[((t33 + 0))/4] = (heap[((t33 + 0))/4] + 1);
  goto L0;
  L6:
  L10:
  t36 = 1;
  if(heap[((t33 + 8))/4] < heap[((t33 + 4))/4]) goto L13;
  L14:
  t36 = 0;
  L13:
  if(t36 != 0) goto L11;
  L12:
  heap[((t33 + 4))/4] = (heap[((t33 + 4))/4] + 1);
  goto L5;
  L11:
  L15:
  t37 = 1;
  if(heap[((t33 + 12))/4] < heap[((t33 + 8))/4]) goto L18;
  L19:
  t37 = 0;
  L18:
  if(t37 != 0) goto L16;
  L17:
  heap[((t33 + 8))/4] = (heap[((t33 + 8))/4] + 1);
  goto L10;
  L16:
  heap[((t33 + 16))/4] = (heap[((t33 + 16))/4] + heap[((t33 + 12))/4]);
  heap[((t33 + 12))/4] = (heap[((t33 + 12))/4] + 1);
  goto L15;
  L22:
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
