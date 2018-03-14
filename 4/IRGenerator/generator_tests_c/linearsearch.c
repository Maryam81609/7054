#include <stdio.h>

void _alloc();
void _printInt();
void LS_Start();
void LS_Print();
void LS_Search();
void LS_Init();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32;
  fp = 4999;
  sp = fp-400;
  L21:
  a0 = 8;
  _alloc();
  rv;
  t32 = rv;
  heap[((rv + 0))/4] = 0;
  heap[((rv + 4))/4] = 0;
  t32;
  a0 = 10;
  a1 = t32;
  LS_Start();
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

void LS_Start() {
  int t32, t33, t34, t35;
  fp = sp;
  sp -= 400;
  L23:
  t34 = a1;
  t35 = a0;
  t34;
  a0 = t35;
  a1 = t34;
  LS_Init();
  rv;
  t32 = rv;
  t34;
  a0 = t34;
  LS_Print();
  rv;
  t33 = rv;
  a0 = 9999;
  _printInt();
  t34;
  a0 = 8;
  a1 = t34;
  LS_Search();
  rv;
  a0 = rv;
  _printInt();
  t34;
  a0 = 12;
  a1 = t34;
  LS_Search();
  rv;
  a0 = rv;
  _printInt();
  t34;
  a0 = 17;
  a1 = t34;
  LS_Search();
  rv;
  a0 = rv;
  _printInt();
  t34;
  a0 = 50;
  a1 = t34;
  LS_Search();
  rv;
  a0 = rv;
  _printInt();
  rv = 55;
  goto L22;
  L22:
  sp = fp;
  fp += 400;
}

void LS_Print() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L25:
  t33 = a0;
  t32 = 1;
  L0:
  if(t32 < heap[((t33 + 4))/4]) goto L1;
  L2:
  rv = 0;
  goto L24;
  L1:
  heap[((t33 + 0))/4];
  a0 = heap[((heap[((t33 + 0))/4] + ((t32 + 1) * 4)))/4];
  _printInt();
  t32 = (t32 + 1);
  goto L0;
  L24:
  sp = fp;
  fp += 400;
}

void LS_Search() {
  int t32, t33, t34, t35, t36, t37, t38, t39, t40;
  fp = sp;
  sp -= 400;
  L27:
  t38 = a1;
  t39 = a0;
  t32 = 1;
  t33 = 0;
  t34 = 0;
  L3:
  if(t32 < heap[((t38 + 4))/4]) goto L4;
  L5:
  rv = t34;
  goto L26;
  L4:
  heap[((t38 + 0))/4];
  t35 = heap[((heap[((t38 + 0))/4] + ((t32 + 1) * 4)))/4];
  t36 = (t39 + 1);
  if(t35 < t39) goto L6;
  L7:
  t40 = 1;
  if(t35 < t36) goto L12;
  L13:
  t40 = 0;
  L12:
  if((1 - t40) != 0) goto L9;
  L10:
  t33 = 1;
  t34 = 1;
  t32 = heap[((t38 + 4))/4];
  L11:
  L8:
  t32 = (t32 + 1);
  goto L3;
  L6:
  t37 = 0;
  goto L8;
  L9:
  t37 = 0;
  goto L11;
  L26:
  sp = fp;
  fp += 400;
}

void LS_Init() {
  int t32, t33, t34, t35, t36, t37, t38, t39, t40;
  fp = sp;
  sp -= 400;
  L29:
  t36 = a1;
  t37 = a0;
  heap[((t36 + 4))/4] = t37;
  t40 = (t36 + 0);
  a0 = ((t37 + 1) * 4);
  _alloc();
  rv;
  t38 = rv;
  heap[(rv)/4] = t37;
  t39 = t37;
  L14:
  if(0 < t39) goto L15;
  L16:
  heap[(t40)/4] = t38;
  t32 = 1;
  t33 = (heap[((t36 + 4))/4] + 1);
  L17:
  if(t32 < heap[((t36 + 4))/4]) goto L18;
  L19:
  rv = 0;
  goto L28;
  L15:
  heap[((rv + (t39 * 4)))/4] = 0;
  t39 = (t39 - 1);
  goto L14;
  L18:
  t34 = (2 * t32);
  t35 = (t33 - 3);
  heap[((heap[((t36 + 0))/4] + ((t32 + 1) * 4)))/4] = (t34 + t35);
  t32 = (t32 + 1);
  t33 = (t33 - 1);
  goto L17;
  L28:
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
