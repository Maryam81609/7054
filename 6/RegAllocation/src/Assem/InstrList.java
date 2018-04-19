package Assem;

import java.util.ArrayList;

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
  public ArrayList<Instr> toList(){
	  ArrayList<Instr> ret = null;
	  
	  if(this.head == null) {
		  return null;
	  }
	  ret = new ArrayList<Instr>();
	  ret.add(this.head);
	  InstrList t = this.tail;
	  while(t != null) {
		  ret.add(t.head);
		  t = t.tail;
	  }
	  return ret;
  }
}
