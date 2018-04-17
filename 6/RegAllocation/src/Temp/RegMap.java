package Temp;

import Mips.MipsFrame;

public class RegMap implements TempMap
{
  private MipsFrame frame;
  public String tempMap(Temp t)
  {
	  String regName = frame.tempMap(t);
	  if(regName == null) {
		  regName = "$" + t.toString();
	  }
    return regName;
  }

  public RegMap(MipsFrame f)
  {
	  frame = f;
  }
}
