#include <stdio.h>

void _alloc();
void _printInt();
void Double_getDouble();
void Dou_getDouble();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32, t33, t34;
  fp = 4999;
  sp = fp-400;
  L1:
  a0 = 0;
  _alloc();
  rv;
  t32 = rv;
  a0 = 2;
  a1 = t32;
  Dou_getDouble();
  rv;
  t34 = rv;
  a0 = 8;
  _alloc();
  rv;
  t33 = rv;
  heap[((rv + 0))/4] = 0;
  heap[((rv + 4))/4] = 0;
  a0 = 5;
  a1 = t33;
  Double_getDouble();
  rv;
  a0 = (t34 * rv);
  _printInt();
  rv = 0;
  goto L0;
  L0:
  sp = fp;
  fp += 400;
  return 0;
}

void Double_getDouble() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L3:
  t32 = a1;
  t33 = a0;
  rv = (t33 * 2);
  goto L2;
  L2:
  sp = fp;
  fp += 400;
}

void Dou_getDouble() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L5:
  t32 = a1;
  t33 = a0;
  rv = (t33 * 10);
  goto L4;
  L4:
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
