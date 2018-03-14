#include <stdio.h>

void _alloc();
void _printInt();
void BT_Start();
void Tree_Init();
void Tree_SetRight();
void Tree_SetLeft();
void Tree_GetRight();
void Tree_GetLeft();
void Tree_GetKey();
void Tree_SetKey();
void Tree_GetHas_Right();
void Tree_GetHas_Left();
void Tree_SetHas_Left();
void Tree_SetHas_Right();
void Tree_Compare();
void Tree_Insert();
void Tree_Delete();
void Tree_Remove();
void Tree_RemoveRight();
void Tree_RemoveLeft();
void Tree_Search();
void Tree_Print();
void Tree_RecPrint();

int stack[5000];
int heap[5000];
int rv, a0, a1, a2, a3, sp, fp, ra, zero=0;

int main() {
  int t32;
  fp = 4999;
  sp = fp-400;
  L79:
  a0 = 0;
  _alloc();
  rv;
  t32 = rv;
  t32;
  a0 = t32;
  BT_Start();
  rv;
  a0 = rv;
  _printInt();
  rv = 0;
  goto L78;
  L78:
  sp = fp;
  fp += 400;
  return 0;
}

void BT_Start() {
  int t32, t33, t34, t35, t36;
  fp = sp;
  sp -= 400;
  L81:
  t35 = a0;
  a0 = 24;
  _alloc();
  rv;
  t36 = rv;
  heap[((rv + 0))/4] = 0;
  heap[((rv + 4))/4] = 0;
  heap[((rv + 8))/4] = 0;
  heap[((rv + 12))/4] = 0;
  heap[((rv + 16))/4] = 0;
  heap[((rv + 20))/4] = 0;
  t32 = t36;
  t32;
  a0 = 16;
  a1 = t32;
  Tree_Init();
  rv;
  t33 = rv;
  t32;
  a0 = t32;
  Tree_Print();
  rv;
  t33 = rv;
  a0 = 1000;
  _printInt();
  t32;
  a0 = 8;
  a1 = t32;
  Tree_Insert();
  rv;
  t33 = rv;
  t32;
  a0 = t32;
  Tree_Print();
  rv;
  t33 = rv;
  t32;
  a0 = 24;
  a1 = t32;
  Tree_Insert();
  rv;
  t33 = rv;
  t32;
  a0 = 4;
  a1 = t32;
  Tree_Insert();
  rv;
  t33 = rv;
  t32;
  a0 = 12;
  a1 = t32;
  Tree_Insert();
  rv;
  t33 = rv;
  t32;
  a0 = 20;
  a1 = t32;
  Tree_Insert();
  rv;
  t33 = rv;
  t32;
  a0 = 28;
  a1 = t32;
  Tree_Insert();
  rv;
  t33 = rv;
  t32;
  a0 = 14;
  a1 = t32;
  Tree_Insert();
  rv;
  t33 = rv;
  t32;
  a0 = t32;
  Tree_Print();
  rv;
  t33 = rv;
  t32;
  a0 = 24;
  a1 = t32;
  Tree_Search();
  rv;
  a0 = rv;
  _printInt();
  t32;
  a0 = 12;
  a1 = t32;
  Tree_Search();
  rv;
  a0 = rv;
  _printInt();
  t32;
  a0 = 16;
  a1 = t32;
  Tree_Search();
  rv;
  a0 = rv;
  _printInt();
  t32;
  a0 = 50;
  a1 = t32;
  Tree_Search();
  rv;
  a0 = rv;
  _printInt();
  t32;
  a0 = 12;
  a1 = t32;
  Tree_Search();
  rv;
  a0 = rv;
  _printInt();
  t32;
  a0 = 12;
  a1 = t32;
  Tree_Delete();
  rv;
  t33 = rv;
  t32;
  a0 = t32;
  Tree_Print();
  rv;
  t33 = rv;
  t32;
  a0 = 12;
  a1 = t32;
  Tree_Search();
  rv;
  a0 = rv;
  _printInt();
  rv = 0;
  goto L80;
  L80:
  sp = fp;
  fp += 400;
}

void Tree_Init() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L83:
  t32 = a1;
  t33 = a0;
  heap[((t32 + 8))/4] = t33;
  heap[((t32 + 12))/4] = 0;
  heap[((t32 + 16))/4] = 0;
  rv = 1;
  goto L82;
  L82:
  sp = fp;
  fp += 400;
}

void Tree_SetRight() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L85:
  t32 = a1;
  t33 = a0;
  heap[((t32 + 4))/4] = t33;
  rv = 1;
  goto L84;
  L84:
  sp = fp;
  fp += 400;
}

void Tree_SetLeft() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L87:
  t32 = a1;
  t33 = a0;
  heap[((t32 + 0))/4] = t33;
  rv = 1;
  goto L86;
  L86:
  sp = fp;
  fp += 400;
}

void Tree_GetRight() {
  int t32;
  fp = sp;
  sp -= 400;
  L89:
  t32 = a0;
  rv = heap[((t32 + 4))/4];
  goto L88;
  L88:
  sp = fp;
  fp += 400;
}

void Tree_GetLeft() {
  int t32;
  fp = sp;
  sp -= 400;
  L91:
  t32 = a0;
  rv = heap[((t32 + 0))/4];
  goto L90;
  L90:
  sp = fp;
  fp += 400;
}

void Tree_GetKey() {
  int t32;
  fp = sp;
  sp -= 400;
  L93:
  t32 = a0;
  rv = heap[((t32 + 8))/4];
  goto L92;
  L92:
  sp = fp;
  fp += 400;
}

void Tree_SetKey() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L95:
  t32 = a1;
  t33 = a0;
  heap[((t32 + 8))/4] = t33;
  rv = 1;
  goto L94;
  L94:
  sp = fp;
  fp += 400;
}

void Tree_GetHas_Right() {
  int t32;
  fp = sp;
  sp -= 400;
  L97:
  t32 = a0;
  rv = heap[((t32 + 16))/4];
  goto L96;
  L96:
  sp = fp;
  fp += 400;
}

void Tree_GetHas_Left() {
  int t32;
  fp = sp;
  sp -= 400;
  L99:
  t32 = a0;
  rv = heap[((t32 + 12))/4];
  goto L98;
  L98:
  sp = fp;
  fp += 400;
}

void Tree_SetHas_Left() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L101:
  t32 = a1;
  t33 = a0;
  heap[((t32 + 12))/4] = t33;
  rv = 1;
  goto L100;
  L100:
  sp = fp;
  fp += 400;
}

void Tree_SetHas_Right() {
  int t32, t33;
  fp = sp;
  sp -= 400;
  L103:
  t32 = a1;
  t33 = a0;
  heap[((t32 + 16))/4] = t33;
  rv = 1;
  goto L102;
  L102:
  sp = fp;
  fp += 400;
}

void Tree_Compare() {
  int t32, t33, t34, t35, t36, t37;
  fp = sp;
  sp -= 400;
  L105:
  t34 = a2;
  t35 = a1;
  t36 = a0;
  t32 = 0;
  t33 = (t36 + 1);
  if(t35 < t36) goto L0;
  L1:
  t37 = 1;
  if(t35 < t33) goto L6;
  L7:
  t37 = 0;
  L6:
  if((1 - t37) != 0) goto L3;
  L4:
  t32 = 1;
  L5:
  L2:
  rv = t32;
  goto L104;
  L0:
  t32 = 0;
  goto L2;
  L3:
  t32 = 0;
  goto L5;
  L104:
  sp = fp;
  fp += 400;
}

void Tree_Insert() {
  int t32, t33, t34, t35, t36, t37, t38, t39;
  fp = sp;
  sp -= 400;
  L107:
  t37 = a1;
  t38 = a0;
  a0 = 24;
  _alloc();
  rv;
  t39 = rv;
  heap[((rv + 0))/4] = 0;
  heap[((rv + 4))/4] = 0;
  heap[((rv + 8))/4] = 0;
  heap[((rv + 12))/4] = 0;
  heap[((rv + 16))/4] = 0;
  heap[((rv + 20))/4] = 0;
  t32 = t39;
  t32;
  a0 = t38;
  a1 = t32;
  Tree_Init();
  rv;
  t33 = rv;
  t36 = t37;
  t34 = 1;
  L8:
  if(t34 != 0) goto L9;
  L10:
  rv = 1;
  goto L106;
  L9:
  t36;
  a0 = t36;
  Tree_GetKey();
  rv;
  t35 = rv;
  if(t38 < t35) goto L11;
  L12:
  t36;
  a0 = t36;
  Tree_GetHas_Right();
  rv;
  if(rv != 0) goto L17;
  L18:
  t34 = 0;
  t36;
  a0 = 1;
  a1 = t36;
  Tree_SetHas_Right();
  rv;
  t33 = rv;
  t36;
  a0 = t32;
  a1 = t36;
  Tree_SetRight();
  rv;
  t33 = rv;
  L19:
  L13:
  goto L8;
  L11:
  t36;
  a0 = t36;
  Tree_GetHas_Left();
  rv;
  if(rv != 0) goto L14;
  L15:
  t34 = 0;
  t36;
  a0 = 1;
  a1 = t36;
  Tree_SetHas_Left();
  rv;
  t33 = rv;
  t36;
  a0 = t32;
  a1 = t36;
  Tree_SetLeft();
  rv;
  t33 = rv;
  L16:
  goto L13;
  L14:
  t36;
  a0 = t36;
  Tree_GetLeft();
  rv;
  t36 = rv;
  goto L16;
  L17:
  t36;
  a0 = t36;
  Tree_GetRight();
  rv;
  t36 = rv;
  goto L19;
  L106:
  sp = fp;
  fp += 400;
}

void Tree_Delete() {
  int t32, t33, t34, t35, t36, t37, t38, t39, t40;
  fp = sp;
  sp -= 400;
  L109:
  t39 = a1;
  t40 = a0;
  t32 = t39;
  t33 = t39;
  t34 = 1;
  t35 = 0;
  t36 = 1;
  L20:
  if(t34 != 0) goto L21;
  L22:
  rv = t35;
  goto L108;
  L21:
  t32;
  a0 = t32;
  Tree_GetKey();
  rv;
  t37 = rv;
  if(t40 < t37) goto L23;
  L24:
  if(t37 < t40) goto L29;
  L30:
  if(t36 != 0) goto L35;
  L36:
  t39;
  a0 = t32;
  a1 = t33;
  a2 = t39;
  Tree_Remove();
  rv;
  t38 = rv;
  L37:
  t35 = 1;
  t34 = 0;
  L31:
  L25:
  t36 = 0;
  goto L20;
  L23:
  t32;
  a0 = t32;
  Tree_GetHas_Left();
  rv;
  if(rv != 0) goto L26;
  L27:
  t34 = 0;
  L28:
  goto L25;
  L26:
  t33 = t32;
  t32;
  a0 = t32;
  Tree_GetLeft();
  rv;
  t32 = rv;
  goto L28;
  L29:
  t32;
  a0 = t32;
  Tree_GetHas_Right();
  rv;
  if(rv != 0) goto L32;
  L33:
  t34 = 0;
  L34:
  goto L31;
  L32:
  t33 = t32;
  t32;
  a0 = t32;
  Tree_GetRight();
  rv;
  t32 = rv;
  goto L34;
  L35:
  t32;
  a0 = t32;
  Tree_GetHas_Right();
  rv;
  if((1 - rv) != 0) goto L41;
  L39:
  t39;
  a0 = t32;
  a1 = t33;
  a2 = t39;
  Tree_Remove();
  rv;
  t38 = rv;
  L40:
  goto L37;
  L41:
  t32;
  a0 = t32;
  Tree_GetHas_Left();
  rv;
  if((1 - rv) == 0) goto L39;
  L38:
  t38 = 1;
  goto L40;
  L108:
  sp = fp;
  fp += 400;
}

void Tree_Remove() {
  int t32, t33, t34, t35, t36, t37;
  fp = sp;
  sp -= 400;
  L111:
  t35 = a2;
  t36 = a1;
  t37 = a0;
  t37;
  a0 = t37;
  Tree_GetHas_Left();
  rv;
  if(rv != 0) goto L42;
  L43:
  t37;
  a0 = t37;
  Tree_GetHas_Right();
  rv;
  if(rv != 0) goto L45;
  L46:
  t37;
  a0 = t37;
  Tree_GetKey();
  rv;
  t33 = rv;
  t36;
  a0 = t36;
  Tree_GetLeft();
  rv;
  rv;
  a0 = rv;
  Tree_GetKey();
  rv;
  t34 = rv;
  t35;
  a0 = t34;
  a1 = t33;
  a2 = t35;
  Tree_Compare();
  rv;
  if(rv != 0) goto L48;
  L49:
  t36;
  a0 = heap[((t35 + 20))/4];
  a1 = t36;
  Tree_SetRight();
  rv;
  t32 = rv;
  t36;
  a0 = 0;
  a1 = t36;
  Tree_SetHas_Right();
  rv;
  t32 = rv;
  L50:
  L47:
  L44:
  rv = 1;
  goto L110;
  L42:
  t35;
  a0 = t37;
  a1 = t36;
  a2 = t35;
  Tree_RemoveLeft();
  rv;
  t32 = rv;
  goto L44;
  L45:
  t35;
  a0 = t37;
  a1 = t36;
  a2 = t35;
  Tree_RemoveRight();
  rv;
  t32 = rv;
  goto L47;
  L48:
  t36;
  a0 = heap[((t35 + 20))/4];
  a1 = t36;
  Tree_SetLeft();
  rv;
  t32 = rv;
  t36;
  a0 = 0;
  a1 = t36;
  Tree_SetHas_Left();
  rv;
  t32 = rv;
  goto L50;
  L110:
  sp = fp;
  fp += 400;
}

void Tree_RemoveRight() {
  int t32, t33, t34, t35;
  fp = sp;
  sp -= 400;
  L113:
  t33 = a2;
  t34 = a1;
  t35 = a0;
  L51:
  t35;
  a0 = t35;
  Tree_GetHas_Right();
  rv;
  if(rv != 0) goto L52;
  L53:
  t34;
  a0 = heap[((t33 + 20))/4];
  a1 = t34;
  Tree_SetRight();
  rv;
  t32 = rv;
  t34;
  a0 = 0;
  a1 = t34;
  Tree_SetHas_Right();
  rv;
  t32 = rv;
  rv = 1;
  goto L112;
  L52:
  t35;
  t35;
  a0 = t35;
  Tree_GetRight();
  rv;
  rv;
  a0 = rv;
  Tree_GetKey();
  rv;
  a0 = rv;
  a1 = t35;
  Tree_SetKey();
  rv;
  t32 = rv;
  t34 = t35;
  t35;
  a0 = t35;
  Tree_GetRight();
  rv;
  t35 = rv;
  goto L51;
  L112:
  sp = fp;
  fp += 400;
}

void Tree_RemoveLeft() {
  int t32, t33, t34, t35;
  fp = sp;
  sp -= 400;
  L115:
  t33 = a2;
  t34 = a1;
  t35 = a0;
  L54:
  t35;
  a0 = t35;
  Tree_GetHas_Left();
  rv;
  if(rv != 0) goto L55;
  L56:
  t34;
  a0 = heap[((t33 + 20))/4];
  a1 = t34;
  Tree_SetLeft();
  rv;
  t32 = rv;
  t34;
  a0 = 0;
  a1 = t34;
  Tree_SetHas_Left();
  rv;
  t32 = rv;
  rv = 1;
  goto L114;
  L55:
  t35;
  t35;
  a0 = t35;
  Tree_GetLeft();
  rv;
  rv;
  a0 = rv;
  Tree_GetKey();
  rv;
  a0 = rv;
  a1 = t35;
  Tree_SetKey();
  rv;
  t32 = rv;
  t34 = t35;
  t35;
  a0 = t35;
  Tree_GetLeft();
  rv;
  t35 = rv;
  goto L54;
  L114:
  sp = fp;
  fp += 400;
}

void Tree_Search() {
  int t32, t33, t34, t35, t36, t37;
  fp = sp;
  sp -= 400;
  L117:
  t36 = a1;
  t37 = a0;
  t34 = t36;
  t32 = 1;
  t33 = 0;
  L57:
  if(t32 != 0) goto L58;
  L59:
  rv = t33;
  goto L116;
  L58:
  t34;
  a0 = t34;
  Tree_GetKey();
  rv;
  t35 = rv;
  if(t37 < t35) goto L60;
  L61:
  if(t35 < t37) goto L66;
  L67:
  t33 = 1;
  t32 = 0;
  L68:
  L62:
  goto L57;
  L60:
  t34;
  a0 = t34;
  Tree_GetHas_Left();
  rv;
  if(rv != 0) goto L63;
  L64:
  t32 = 0;
  L65:
  goto L62;
  L63:
  t34;
  a0 = t34;
  Tree_GetLeft();
  rv;
  t34 = rv;
  goto L65;
  L66:
  t34;
  a0 = t34;
  Tree_GetHas_Right();
  rv;
  if(rv != 0) goto L69;
  L70:
  t32 = 0;
  L71:
  goto L68;
  L69:
  t34;
  a0 = t34;
  Tree_GetRight();
  rv;
  t34 = rv;
  goto L71;
  L116:
  sp = fp;
  fp += 400;
}

void Tree_Print() {
  int t32, t33, t34;
  fp = sp;
  sp -= 400;
  L119:
  t34 = a0;
  t32 = t34;
  t34;
  a0 = t32;
  a1 = t34;
  Tree_RecPrint();
  rv;
  t33 = rv;
  rv = 1;
  goto L118;
  L118:
  sp = fp;
  fp += 400;
}

void Tree_RecPrint() {
  int t32, t33, t34;
  fp = sp;
  sp -= 400;
  L121:
  t33 = a1;
  t34 = a0;
  t34;
  a0 = t34;
  Tree_GetHas_Left();
  rv;
  if(rv != 0) goto L72;
  L73:
  t32 = 1;
  L74:
  t34;
  a0 = t34;
  Tree_GetKey();
  rv;
  a0 = rv;
  _printInt();
  t34;
  a0 = t34;
  Tree_GetHas_Right();
  rv;
  if(rv != 0) goto L75;
  L76:
  t32 = 1;
  L77:
  rv = 1;
  goto L120;
  L72:
  t33;
  t34;
  a0 = t34;
  Tree_GetLeft();
  rv;
  a0 = rv;
  a1 = t33;
  Tree_RecPrint();
  rv;
  t32 = rv;
  goto L74;
  L75:
  t33;
  t34;
  a0 = t34;
  Tree_GetRight();
  rv;
  a0 = rv;
  a1 = t33;
  Tree_RecPrint();
  rv;
  t32 = rv;
  goto L77;
  L120:
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
