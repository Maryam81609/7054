#include <stdio.h>

void _alloc();
void _printInt();
void OrderOfOps_init();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32;
  fp = 4999;
  sp = fp-400;
  L8:
  a0 = 0;
  _alloc();
  rv;
  t32 = rv;
  t32;
  a0 = 1300;
  a1 = t32;
  OrderOfOps_init();
  rv;
  a0 = rv;
  _printInt();
  rv = 0;
  goto L7;
  L7:
  sp = fp;
  fp += 400;
  return 0;
}

void OrderOfOps_init() {
  int t32, t33, t34, t35, t36, t37, t38;
  fp = sp;
  sp -= 400;
  L10:
  t36 = a1;
  t37 = a0;
  t32 = 2;
  t33 = 18;
  t34 = 2005;
  t38 = 1;
  if((t37 * 6) < (t34 - (40 * t33))) goto L3;
  L4:
  t38 = 0;
  L3:
  if((1 - t38) != 0) goto L5;
  L1:
  t35 = 0;
  L2:
  rv = t35;
  goto L9;
  L5:
  if(((t32 * 14) - (t37 * 8)) >= 0) goto L1;
  L6:
  if((439078 - t37) >= (t34 * t37)) goto L1;
  L0:
  t35 = 1;
  goto L2;
  L9:
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
