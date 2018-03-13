public class Function{
    String var;
    String expr;
    double value;
    ExpNode subTree = null;

    public Function(String expr, String var, double value) {
        this.var  = var;
        this.expr = expr;
        this.value=value;
    }

    public Function(String expr){
        this.expr=expr;
    }

    public Function copy(){
        Function temp = new Function(expr);
        temp.value = value;
        temp.var   = var;
        if(subTree != null) temp.subTree = subTree.copy();
        return temp;
    }

    public void subTree(ExpNode f){
        subTree = f;
    }

    public void print() {
        System.out.println("\nFunction:  " + expr);
        System.out.println("variables: " + var);
        System.out.println("Value:     " + value + "\n");
        if (subTree != null) {
            System.out.println("subTree: ");
            subTree.printByLevel();
        }
    }

    //add arbitrary function "genericFunction" by appending
    //if(expr.equals("genericFunction") return mapValue();
    public double evaluate(){
        double value;
        if(var.equals("/"))
            value = this.value;
        else value=Derivative.getValue(var);
        if(subTree!=null){
            value= Derivative.compute(subTree);
        }
        if(expr.equals("const"))
            return this.value;
        if(expr.equals("sin"))
            return Math.sin(value);
        if(expr.equals("cos"))
            return Math.cos(value);
        if(expr.equals("ln"))
            return Math.log(value);
        if(expr.equals(var))
            return value;
        return 0;
    }
}
