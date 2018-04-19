package RegAlloc;

import java.util.HashMap;

import Mips.MipsFrame;
import Temp.*;
import RegAlloc.*;

public class RegisterMap implements TempMap
{
  //private MipsFrame frame;
  public String tempMap(Temp t)
  {
	  String regName = LiveAnalysis.frameTempMap.get(t);
	  /*String regName = frame.tempMap(t);
	  if(regName == null) {
		  regName = "$" + t.toString();
	  }*/
    return regName;
  }

  /*public RegisterMap(MipsFrame f)
  {
	  frame = f;
  }*/
}
