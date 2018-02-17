package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;
import visitor.StringVisitor;
import visitor.ExpVisitor;

public class IntArrayType extends Type {
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
