#include <stdio.h>

void _alloc();
void _printInt();
void C_print();
void C_f();
void B_print();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32;
  fp = 4999;
  sp = fp-400;
  L13:
  a0 = 4;
  _alloc();
  rv;
  t32 = rv;
  heap[((rv + 0))/4] = 0;
  t32;
  a0 = t32;
  C_f();
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

void C_print() {
  int t32, t33, t34, t35, t36, t37, t38;
  fp = sp;
  sp -= 400;
  L15:
  t32 = stack[((fp + 12))/4];
  t33 = stack[((fp + 8))/4];
  t34 = stack[((fp + 4))/4];
  t35 = a3;
  t36 = a2;
  t37 = a1;
  t38 = a0;
  if(t33 != 0) goto L0;
  L1:
  a0 = 0;
  _printInt();
  L2:
  if(t35 != 0) goto L3;
  L4:
  a0 = 0;
  _printInt();
  L5:
  if(t37 != 0) goto L6;
  L7:
  a0 = 0;
  _printInt();
  L8:
  rv = 1;
  goto L14;
  L0:
  a0 = t34;
  _printInt();
  goto L2;
  L3:
  t36;
  a0 = heap[((t36 + ((0 + 1) * 4)))/4];
  _printInt();
  goto L5;
  L6:
  t38;
  t36;
  t34 = heap[((t36 + ((0 + 1) * 4)))/4];
  t36;
  a0 = (t34 * heap[((t36 + ((0 + 1) * 4)))/4]);
  a1 = t38;
  B_print();
  rv;
  t33 = rv;
  goto L8;
  L14:
  sp = fp;
  fp += 400;
}

void C_f() {
  int t32, t33, t34, t35, t36, t37, t38, t39;
  fp = sp;
  sp -= 400;
  L17:
  t36 = a0;
  a0 = 0;
  _alloc();
  rv;
  t37 = rv;
  t34 = t37;
  t32 = (2 * 2);
  a0 = ((1 + 1) * 4);
  _alloc();
  rv;
  t38 = rv;
  heap[(rv)/4] = 1;
  t39 = 1;
  L9:
  if(0 < t39) goto L10;
  L11:
  t35 = t38;
  heap[((t35 + ((0 + 1) * 4)))/4] = (t32 * t32);
  t36;
  a0 = t34;
  a1 = 1;
  a2 = t35;
  a3 = 1;
  stack[(sp + 4)/4] = t32;
  stack[(sp + 8)/4] = 1;
  stack[(sp + 12)/4] = t36;
  C_print();
  rv;
  t33 = rv;
  rv = 1;
  goto L16;
  L10:
  heap[((rv + (t39 * 4)))/4] = 0;
  t39 = (t39 - 1);
  goto L9;
  L16:
  sp = fp;
  fp += 400;
}

void B_print() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L19:
  t32 = a1;
  t33 = a0;
  a0 = t33;
  _printInt();
  rv = 1;
  goto L18;
  L18:
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
