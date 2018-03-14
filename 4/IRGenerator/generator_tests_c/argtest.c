#include <stdio.h>

void _alloc();
void _printInt();
void Dou_print();
void Dou_f();
void B_get();

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
  Dou_f();
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

void Dou_print() {
  int t32, t33, t34, t35, t36, t37, t38, t39, t40;
  fp = sp;
  sp -= 400;
  L6:
  t33 = stack[((fp + 16))/4];
  t34 = stack[((fp + 12))/4];
  t35 = stack[((fp + 8))/4];
  t36 = stack[((fp + 4))/4];
  t37 = a3;
  t38 = a2;
  t39 = a1;
  t40 = a0;
  a0 = t34;
  _printInt();
  a0 = t35;
  _printInt();
  a0 = t36;
  _printInt();
  a0 = t37;
  _printInt();
  a0 = t38;
  _printInt();
  t39;
  t40;
  t34 = heap[((t40 + ((0 + 1) * 4)))/4];
  t40;
  a0 = (t34 * heap[((t40 + ((0 + 1) * 4)))/4]);
  a1 = t39;
  B_get();
  rv;
  t32 = rv;
  t40;
  a0 = heap[((t40 + ((0 + 1) * 4)))/4];
  _printInt();
  rv = t34;
  goto L5;
  L5:
  sp = fp;
  fp += 400;
}

void Dou_f() {
  int t32, t33, t34, t35, t36, t37, t38, t39;
  fp = sp;
  sp -= 400;
  L8:
  t36 = a0;
  t35 = (4 * 2);
  a0 = ((1 + 1) * 4);
  _alloc();
  rv;
  t37 = rv;
  heap[(rv)/4] = 1;
  t38 = 1;
  L0:
  if(0 < t38) goto L1;
  L2:
  t33 = t37;
  heap[((t33 + ((0 + 1) * 4)))/4] = (t35 * t35);
  t33;
  a0 = heap[((t33 + ((0 + 1) * 4)))/4];
  _printInt();
  a0 = 0;
  _alloc();
  rv;
  t39 = rv;
  t34 = t39;
  t36;
  a0 = t33;
  a1 = t34;
  a2 = 15;
  a3 = 14;
  stack[(sp + 4)/4] = 13;
  stack[(sp + 8)/4] = 12;
  stack[(sp + 12)/4] = 10;
  stack[(sp + 16)/4] = t36;
  Dou_print();
  rv;
  t32 = rv;
  rv = 11111;
  goto L7;
  L1:
  heap[((rv + (t38 * 4)))/4] = 0;
  t38 = (t38 - 1);
  goto L0;
  L7:
  sp = fp;
  fp += 400;
}

void B_get() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L10:
  t32 = a1;
  t33 = a0;
  a0 = t33;
  _printInt();
  rv = 1;
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
