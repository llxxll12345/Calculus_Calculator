import java.util.ArrayList;
import java.util.Stack;

public class Derivative {
    static Stack<Function> stack;
    private static ArrayList<String> vars = null;
    private static ArrayList<Double> values    = null;
    public Derivative(){
    }


    public static ArrayList<String> getVars() {
        return vars;
    }

    public static void setVars(ArrayList<String> vars) {
        Derivative.vars = vars;
    }

    public static ArrayList<Double> getValues() {
        return values;
    }

    public static void setValues(ArrayList<Double> values) {
        Derivative.values = values;
    }

    public static void setVarVal(String var, double value){
        for(int i = 0;i < getVars().size();i++){
            if(getVars().get(i).equals(var)) {
                values.set(i, value);
            }
        }
    }
    
    private static boolean isOp(String s){
        return (s.equals("+")||s.equals("-")||s.equals("*")||s.equals("/")||s.equals("^"));
    }

    public boolean isVar(String s){
        for(int i=0;i<getVars().size();i++){
            if(getVars().get(i).equals(s)) return true;
        }
        return false;
    }

    public static double getValue(String var){
        for(int i=0;i<getVars().size();i++){
            if(getVars().get(i).equals(var)) return getValues().get(i);
        }
        return 0;
    }


    //returns length of the gradient of expr Tree
    public static ExpNode lengthOfGradient(ExpNode fE){
        ArrayList<ExpNode> derivatives = getDerivatives(fE);
        ExpNode out=null;
        for(int i=0;i<derivatives.size();i++){
            ExpNode a = Evals.exp(derivatives.get(i), 2.0);
            if(out==null)out=a;
            else out = Evals.sum(out, a);
        }
        return out;
    }

    //returns a list of all partial derivatives
    public static ArrayList<ExpNode> getDerivatives(ExpNode fE){
        ArrayList<ExpNode> f = new ArrayList<>();
        ArrayList<String> var = Derivative.getVars();
        for(int i=0;i<var.size();i++){
            ExpNode temp = computeDerivative(var.get(i), fE);
            f.add(ExpNode.simplifyExpression(temp));
        }
        return f;
    }

    private static double compute(String operator, Function a, Function b){
        switch(operator.charAt(0)){
            case '+': return a.evaluate() + b.evaluate();
            case '-': return a.evaluate() - b.evaluate();
            case '*': return a.evaluate() * b.evaluate();
            case '/': return a.evaluate() / b.evaluate();
            case '^': return Math.pow(a.evaluate(), b.evaluate());
        }
        return 0;
    }

    //returns the value of the ExpNode evaluated at the point
    public static double compute(ExpNode fE){
        if(fE   == null) return 0;
        if(vars == null){
            System.out.println("ERROR: List of variables have no yet been set!");
            return 0;
        }
        Stack<Function> temp = stack;
        stack = new Stack<Function>();
        computeRekursion(fE);
        double out = stack.pop().evaluate();
        stack = temp;
        return out;
    }

    private static void computeRekursion(ExpNode fE){
        if(fE != null){
            computeRekursion(fE.left);
            computeRekursion(fE.right);
            if(!isOp(fE.function.expr)){
                stack.push(fE.function);
            }
            else{
                //printStack();
                Function b = stack.pop();
                Function a = stack.pop();
                stack.push(new Function("const","/",compute(fE.function.expr,a,b)));
            }
        }
    }

    // returns the derivative of ExpNode with respect to <withRespectTo>, as a ExpNode
    public static ExpNode computeDerivative(String withRespectTo, ExpNode fE){
        if(fE==null) return null;
        ExpNode out=null;
        if(fE.function.expr.equals("+")||fE.function.expr.equals("-")){
            ExpNode temp=new ExpNode(new Function(new String(fE.function.expr)));

            ExpNode derivativeLeft=computeDerivative(withRespectTo,fE.left);
            ExpNode derivativeRight=computeDerivative(withRespectTo,fE.right);
            if(derivativeLeft!=null)temp.addLeft(derivativeLeft);
            if(derivativeRight!=null)temp.addRight(derivativeRight);
            if(derivativeLeft==null&&derivativeRight==null) return null;
            if(derivativeLeft==null&&derivativeRight!=null)	temp= derivativeRight;
            if(derivativeLeft!=null&&derivativeRight==null) temp= derivativeLeft;
            out= temp;
        }
        else if(fE.function.expr.equals("/")){
            out=computeDerivative(withRespectTo, Evals.mul(fE.left, Evals.exp(fE.right, -1)));
        }
        else if(fE.function.expr.equals("*")){
            ExpNode tempPlus=new ExpNode(new Function("+"));
            ExpNode tempMul1=new ExpNode(new Function("*"));
            ExpNode tempMul2=new ExpNode(new Function("*"));

            ExpNode derivativeLeft=computeDerivative(withRespectTo,fE.left);
            ExpNode derivativeRight=computeDerivative(withRespectTo,fE.right);

            ExpNode copyLeft=null;
            ExpNode copyRight=null;
            if(fE.left!=null) copyLeft=fE.left.copy();
            if(fE.right!=null) copyRight=fE.right.copy();

            if(derivativeLeft==null) tempMul1=null;
            else {
                tempMul1.addRight(copyRight);
                tempMul1.addLeft(derivativeLeft);
                tempPlus.addLeft(tempMul1);
            }

            if(derivativeRight==null) tempMul2=null;
            else {
                tempMul2.addLeft(copyLeft);
                tempMul2.addRight(derivativeRight);
                tempPlus.addRight(tempMul2);
            }
            if(derivativeLeft==null&&derivativeRight==null) return null;
            if(derivativeLeft==null) tempPlus= tempMul2;
            if(derivativeRight==null) tempPlus= tempMul1;
            out= tempPlus;
        }
        else if(fE.function.expr.equals("^")){
            if(fE.right.function.expr.equals("const")){
                if(fE.right.function.value==0) return null;
                ExpNode muliEx = Evals.exp(fE.left.copy(), new Function("const", "/", (fE.right.function.value-1) ));
                ExpNode Exp = Evals.mul(muliEx, new Function("const", "/", (fE.right.function.value)));
                ExpNode f = computeDerivative(withRespectTo, fE.left);
                if(f!=null) out= (Evals.mul(Exp, f));
                else return null;
            }
        }
        else if(!isOp(fE.function.expr)) return out=derive(withRespectTo,fE);
        return ExpNode.simplifyNode(out);
    }

    //add a derivative of an arbitraryFunction in the table by appending
    //else if(fE.function.expr.equals("arbitraryFunction")){
    //			ExpNode temp=arbitraryFuntion.copy();
    //			temp.function.expr="derivativeOfArbitraryFunction";
    //			out=temp;
    //}
    //append just before "derive" handles differentiation of composite functions (after last else if)
    //returns the derivative of a ExpNode as a ExpNode
    public static ExpNode derive(String withrespectTo, ExpNode fE){
        if(fE.function==null) return null;
        if(fE.function.subTree==null&&!(fE.function.var.equals(withrespectTo))) return null;
        ExpNode out=null;
        if(fE.function.expr.equals("sin")){
            ExpNode temp=fE.copy();
            temp.function.expr="cos";
            out=temp;
        }
        else if(fE.function.expr.equals("cos")){
            ExpNode temp=fE.copy();
            temp.function.expr="sin";
            out= Evals.mul(temp,-1.0);
        }
        else if(fE.function.expr.equals("ln")){
            ExpNode temp=fE.copy();
            temp.function.expr=""+(fE.function.var);
            out= Evals.exp(temp,-1.0);
        }
        else if(withrespectTo.equals(fE.function.var)){
            ExpNode temp=new ExpNode(new Function("const", "/", 1.0));
            out= temp;
        }
        else if(fE.function.expr.equals("const")) return null;
        //computes derivatives of composed functions, if needed
        if(fE.function.subTree!=null){
            ExpNode temp=computeDerivative(withrespectTo,fE.function.subTree);
            if(temp==null) return null;
            out = Evals.mul(out, temp);
        }
        return ExpNode.simplifyNode(out);
    }
}
