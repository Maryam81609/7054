#include <stdio.h>

void _alloc();
void _printInt();
void Double_getDouble();
void Dou_getDouble();
void Dou_print();

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
  a0 = 12;
  a1 = 9;
  a2 = t32;
  Dou_print();
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

void Double_getDouble() {
  int t32, t33, t34, t35, t36;
  fp = sp;
  sp -= 400;
  L6:
  t33 = a1;
  t34 = a0;
  a0 = ((3 + 1) * 4);
  _alloc();
  rv;
  t35 = rv;
  heap[(rv)/4] = 3;
  t36 = 3;
  L0:
  if(0 < t36) goto L1;
  L2:
  t32 = t35;
  heap[((t32 + ((2 + 1) * 4)))/4] = 6;
  t35 = t34;
  t32;
  rv = (t35 * heap[((t32 + ((2 + 1) * 4)))/4]);
  goto L5;
  L1:
  heap[((rv + (t36 * 4)))/4] = 0;
  t36 = (t36 - 1);
  goto L0;
  L5:
  sp = fp;
  fp += 400;
}

void Dou_getDouble() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L8:
  t32 = a1;
  t33 = a0;
  rv = (t33 * 10);
  goto L7;
  L7:
  sp = fp;
  fp += 400;
}

void Dou_print() {
  int t32, t33, t34;
  fp = sp;
  sp -= 400;
  L10:
  t32 = a2;
  t33 = a1;
  t34 = a0;
  a0 = t33;
  _printInt();
  t33 = (t33 - 1);
  a0 = t33;
  _printInt();
  a0 = t34;
  _printInt();
  rv = t34;
  goto L9;
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
