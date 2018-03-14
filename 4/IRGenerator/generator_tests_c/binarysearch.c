#include <stdio.h>

void _alloc();
void _printInt();
void BS_Start();
void BS_Search();
void BS_Div();
void BS_Compare();
void BS_Print();
void BS_Init();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32;
  fp = 4999;
  sp = fp-400;
  L60:
  a0 = 8;
  _alloc();
  rv;
  t32 = rv;
  heap[((rv + 0))/4] = 0;
  heap[((rv + 4))/4] = 0;
  t32;
  a0 = 20;
  a1 = t32;
  BS_Start();
  rv;
  a0 = rv;
  _printInt();
  rv = 0;
  goto L59;
  L59:
  sp = fp;
  fp += 400;
  return 0;
}

void BS_Start() {
  int t32, t33, t34, t35;
  fp = sp;
  sp -= 400;
  L62:
  t34 = a1;
  t35 = a0;
  t34;
  a0 = t35;
  a1 = t34;
  BS_Init();
  rv;
  t32 = rv;
  t34;
  a0 = t34;
  BS_Print();
  rv;
  t33 = rv;
  t34;
  a0 = 8;
  a1 = t34;
  BS_Search();
  rv;
  if(rv != 0) goto L0;
  L1:
  a0 = 0;
  _printInt();
  L2:
  t34;
  a0 = 19;
  a1 = t34;
  BS_Search();
  rv;
  if(rv != 0) goto L3;
  L4:
  a0 = 0;
  _printInt();
  L5:
  t34;
  a0 = 20;
  a1 = t34;
  BS_Search();
  rv;
  if(rv != 0) goto L6;
  L7:
  a0 = 0;
  _printInt();
  L8:
  t34;
  a0 = 21;
  a1 = t34;
  BS_Search();
  rv;
  if(rv != 0) goto L9;
  L10:
  a0 = 0;
  _printInt();
  L11:
  t34;
  a0 = 37;
  a1 = t34;
  BS_Search();
  rv;
  if(rv != 0) goto L12;
  L13:
  a0 = 0;
  _printInt();
  L14:
  t34;
  a0 = 38;
  a1 = t34;
  BS_Search();
  rv;
  if(rv != 0) goto L15;
  L16:
  a0 = 0;
  _printInt();
  L17:
  t34;
  a0 = 39;
  a1 = t34;
  BS_Search();
  rv;
  if(rv != 0) goto L18;
  L19:
  a0 = 0;
  _printInt();
  L20:
  t34;
  a0 = 50;
  a1 = t34;
  BS_Search();
  rv;
  if(rv != 0) goto L21;
  L22:
  a0 = 0;
  _printInt();
  L23:
  rv = 999;
  goto L61;
  L0:
  a0 = 1;
  _printInt();
  goto L2;
  L3:
  a0 = 1;
  _printInt();
  goto L5;
  L6:
  a0 = 1;
  _printInt();
  goto L8;
  L9:
  a0 = 1;
  _printInt();
  goto L11;
  L12:
  a0 = 1;
  _printInt();
  goto L14;
  L15:
  a0 = 1;
  _printInt();
  goto L17;
  L18:
  a0 = 1;
  _printInt();
  goto L20;
  L21:
  a0 = 1;
  _printInt();
  goto L23;
  L61:
  sp = fp;
  fp += 400;
}

void BS_Search() {
  int t32, t33, t34, t35, t36, t37, t38, t39, t40;
  fp = sp;
  sp -= 400;
  L64:
  t39 = a1;
  t40 = a0;
  t37 = 0;
  t32 = 0;
  heap[((t39 + 0))/4];
  t33 = heap[(heap[((t39 + 0))/4])/4];
  t33 = (t33 - 1);
  t34 = 0;
  t35 = 1;
  L24:
  if(t35 != 0) goto L25;
  L26:
  t39;
  a0 = t40;
  a1 = t37;
  a2 = t39;
  BS_Compare();
  rv;
  if(rv != 0) goto L36;
  L37:
  t32 = 0;
  L38:
  rv = t32;
  goto L63;
  L25:
  t36 = (t34 + t33);
  t39;
  a0 = t36;
  a1 = t39;
  BS_Div();
  rv;
  t36 = rv;
  heap[((t39 + 0))/4];
  t37 = heap[((heap[((t39 + 0))/4] + ((t36 + 1) * 4)))/4];
  if(t40 < t37) goto L33;
  L34:
  t34 = (t36 + 1);
  L35:
  t39;
  a0 = t40;
  a1 = t37;
  a2 = t39;
  BS_Compare();
  rv;
  if(rv != 0) goto L30;
  L31:
  t35 = 1;
  L32:
  if(t33 < t34) goto L27;
  L28:
  t38 = 0;
  L29:
  goto L24;
  L33:
  t33 = (t36 - 1);
  goto L35;
  L30:
  t35 = 0;
  goto L32;
  L27:
  t35 = 0;
  goto L29;
  L36:
  t32 = 1;
  goto L38;
  L63:
  sp = fp;
  fp += 400;
}

void BS_Div() {
  int t32, t33, t34, t35, t36;
  fp = sp;
  sp -= 400;
  L66:
  t35 = a1;
  t36 = a0;
  t32 = 0;
  t33 = 0;
  t34 = (t36 - 1);
  L39:
  if(t33 < t34) goto L40;
  L41:
  rv = t32;
  goto L65;
  L40:
  t32 = (t32 + 1);
  t33 = (t33 + 2);
  goto L39;
  L65:
  sp = fp;
  fp += 400;
}

void BS_Compare() {
  int t32, t33, t34, t35, t36, t37;
  fp = sp;
  sp -= 400;
  L68:
  t34 = a2;
  t35 = a1;
  t36 = a0;
  t32 = 0;
  t33 = (t36 + 1);
  if(t35 < t36) goto L42;
  L43:
  t37 = 1;
  if(t35 < t33) goto L48;
  L49:
  t37 = 0;
  L48:
  if((1 - t37) != 0) goto L45;
  L46:
  t32 = 1;
  L47:
  L44:
  rv = t32;
  goto L67;
  L42:
  t32 = 0;
  goto L44;
  L45:
  t32 = 0;
  goto L47;
  L67:
  sp = fp;
  fp += 400;
}

void BS_Print() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L70:
  t33 = a0;
  t32 = 1;
  L50:
  if(t32 < heap[((t33 + 4))/4]) goto L51;
  L52:
  a0 = 999;
  _printInt();
  rv = 0;
  goto L69;
  L51:
  heap[((t33 + 0))/4];
  a0 = heap[((heap[((t33 + 0))/4] + ((t32 + 1) * 4)))/4];
  _printInt();
  t32 = (t32 + 1);
  goto L50;
  L69:
  sp = fp;
  fp += 400;
}

void BS_Init() {
  int t32, t33, t34, t35, t36, t37, t38, t39, t40;
  fp = sp;
  sp -= 400;
  L72:
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
  L53:
  if(0 < t39) goto L54;
  L55:
  heap[(t40)/4] = t38;
  t32 = 1;
  t33 = (heap[((t36 + 4))/4] + 1);
  L56:
  if(t32 < heap[((t36 + 4))/4]) goto L57;
  L58:
  rv = 0;
  goto L71;
  L54:
  heap[((rv + (t39 * 4)))/4] = 0;
  t39 = (t39 - 1);
  goto L53;
  L57:
  t35 = (2 * t32);
  t34 = (t33 - 3);
  heap[((heap[((t36 + 0))/4] + ((t32 + 1) * 4)))/4] = (t35 + t34);
  t32 = (t32 + 1);
  t33 = (t33 - 1);
  goto L56;
  L71:
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
