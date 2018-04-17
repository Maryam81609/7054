package Assem;

public class InstrList
{
  public Instr     head;
  public InstrList tail;

  public InstrList(Instr h, InstrList t)
  {
    head = h;
    tail = t;
  }
  
  public void print(Temp.TempMap map) {
	  if(this.head != null) {
		  System.out.println(this.head.format(map));
	  }
	  InstrList t = this.tail;
	  while(t != null) {
		  System.out.println(t.head.format(map));
		  t = t.tail;
	  }
  }
}
