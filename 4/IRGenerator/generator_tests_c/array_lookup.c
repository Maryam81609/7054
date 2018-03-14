#include <stdio.h>

void _alloc();
void _printInt();
void A_m();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32;
  fp = 4999;
  sp = fp-400;
  L4:
  a0 = 0;
  _alloc();
  rv;
  t32 = rv;
  t32;
  a0 = t32;
  A_m();
  rv;
  a0 = rv;
  _printInt();
  rv = 0;
  goto L3;
  L3:
  sp = fp;
  fp += 400;
  return 0;
}

void A_m() {
  int t32, t33, t34, t35;
  fp = sp;
  sp -= 400;
  L6:
  t33 = a0;
  a0 = ((1 + 1) * 4);
  _alloc();
  rv;
  t34 = rv;
  heap[(rv)/4] = 1;
  t35 = 1;
  L0:
  if(0 < t35) goto L1;
  L2:
  t32 = t34;
  t32;
  a0 = heap[((t32 + ((0 + 1) * 4)))/4];
  _printInt();
  rv = 1;
  goto L5;
  L1:
  heap[((rv + (t35 * 4)))/4] = 0;
  t35 = (t35 - 1);
  goto L0;
  L5:
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
