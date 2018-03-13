public class ExpNode {
    ExpNode left;
    ExpNode right;
    Function function;

    public ExpNode(Function function){
        this.function = function;
        this.left  = null;
        this.right = null;
    }

    public ExpNode copy(){
        Function f=this.function.copy();
        ExpNode temp=new ExpNode(f);
        if(left != null)temp.addLeft(left.copy());
        if(right!= null)temp.addRight(right.copy());
        return temp;
    }

    public void printByLevel(){
        int i=0;
        System.out.println("Level "+i);
        while(printByLevelRecursion(this, 0, i)) {
            i ++;
            System.out.println("\nLevel "+i);
        }
    }

    public boolean printByLevelRecursion(ExpNode fE, int n, int N){
        if(fE== null) return false;
        if(n == N){
            fE.function.print();
            return true;
        }
        else{
            boolean temp1=printByLevelRecursion(fE.left, n+1, N);
            boolean temp2=printByLevelRecursion(fE.right, n+1, N);
            if(temp1 || temp2) return true;
            else return false;
        }
    }

    public void addLeft(ExpNode left){
        this.left=left;
    }

    public void addRight(ExpNode right){
        this.right=right;
    }

    public static ExpNode simplifyNode(ExpNode fE){
        if(fE==null)
            return null;
        if(fE.function.expr.equals("*")){
            if(fE.left.function.expr.equals("const")&&fE.left.function.value==1)
                return fE.right;
            if(fE.right.function.expr.equals("const")&&fE.right.function.value==1)
                return fE.left;
        }
        if(fE.function.expr.equals("^")){
            if(fE.right.function.expr.equals("const") && fE.right.function.value==1)
                return fE.left;
        }
        if(fE.function.expr.equals("+") || fE.function.expr.equals("-")){
            if(fE.left.function.expr.equals("const")  && fE.left.function.value==0)
                return fE.right;
            if(fE.right.function.expr.equals("const") && fE.right.function.value==0)
                return fE.left;
        }
        return fE;
    }

    public static ExpNode simplifyExpression(ExpNode fE){
        if(fE == null)
            return null;
        fE.left  = simplifyExpression(fE.left);
        fE.right =simplifyExpression(fE.right);
        return simplifyNode(fE);
    }

}

