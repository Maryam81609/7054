package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;
import visitor.StringVisitor;
import visitor.ExpVisitor;

public class Minus extends Exp {
  public Exp e1,e2;
  
  public Minus(Exp ae1, Exp ae2) {
    e1=ae1; e2=ae2;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }

  public String accept(StringVisitor v) {
    return v.visit(this);
  }

  public Translate.Exp accept(ExpVisitor v) {
    return v.visit(this);
  }
}
