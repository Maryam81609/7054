package Temp;

public class RegMap implements TempMap
{
  public String tempMap(Temp t)
  {
    return "$" + t.toString();
  }

  public RegMap()
  {
  }
}
