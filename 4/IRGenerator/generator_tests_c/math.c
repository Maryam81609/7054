#include <stdio.h>

void _alloc();
void _printInt();
void A8_fun1();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32;
  fp = 4999;
  sp = fp-400;
  L1:
  a0 = 4;
  _alloc();
  rv;
  t32 = rv;
  heap[((rv + 0))/4] = 0;
  t32;
  a0 = 11;
  a1 = t32;
  A8_fun1();
  rv;
  a0 = rv;
  _printInt();
  a0 = 763610;
  _printInt();
  rv = 0;
  goto L0;
  L0:
  sp = fp;
  fp += 400;
  return 0;
}

void A8_fun1() {
  int t32, t33, t34, t35, t36, t37, t38, t39, t40, t41;
  fp = sp;
  sp -= 400;
  L3:
  t40 = a1;
  t41 = a0;
  t33 = (t41 - 1);
  t34 = (t41 * 3);
  t35 = (t41 + 4);
  t33 = (t34 - t33);
  t34 = (t33 + t35);
  t36 = (t34 * t33);
  t37 = (t35 + 0);
  t38 = (t36 - 0);
  t39 = (t38 * 1);
  t32 = ((((t33 + (t34 * t35)) - t36) + t37) + (t38 * t39));
  rv = t32;
  goto L2;
  L2:
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
