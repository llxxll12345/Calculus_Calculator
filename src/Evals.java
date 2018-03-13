public class Evals {
    ///////////////////////////////////// sum /////////////////////////////////////
    public static ExpNode sum(Function a, Function b){
        return sum(new ExpNode(a), new ExpNode((b)));
    }

    public static ExpNode sum(Function a, ExpNode b){
        return sum(new ExpNode(a), b);
    }

    public static ExpNode sum(ExpNode a, Function b){
        return sum(a, new ExpNode((b)));
    }

    public static ExpNode sum(Function a, double b){
        return sum(new ExpNode(a), new ExpNode(new Function("const", "/", b)));
    }

    public static ExpNode sum(ExpNode a, double b){
        return sum(a, new ExpNode(new Function("const", "/", b)));
    }

    public static ExpNode sum(ExpNode a, ExpNode b){
        ExpNode result= new ExpNode(new Function("+"));
        result.addLeft(a);
        result.addRight(b);
        return result;
    }

    ///////////////////////////////////// subtract /////////////////////////////////////
    public static ExpNode sub(ExpNode a, double b){
       return sub(a, new ExpNode(new Function("const","/",b)));
    }

    public static ExpNode sub(Function a, Function b) {
        return sub(new ExpNode(a), new ExpNode(b));
    }

    public static ExpNode sub(ExpNode a, ExpNode b){
        ExpNode result = new ExpNode(new Function("-"));
        result.addLeft(a);
        result.addRight(b);
        return result;
    }

    ///////////////////////////////////// division /////////////////////////////////////
    public static ExpNode div(Function a, Function b){
        return div(new ExpNode(a), new ExpNode(b));
    }

    public static ExpNode div(ExpNode a, Function b){
        return div(a, new ExpNode(b));
    }

    public static ExpNode div(Function a, double b){
        return div(new ExpNode(a), new ExpNode(new Function("const", "/", b)));
    }

    public static ExpNode div(ExpNode a, double b){
        return div(a, new ExpNode(new Function("const","/",b)));
    }

    public static ExpNode div(ExpNode a, ExpNode b){
        ExpNode result = new ExpNode(new Function("/"));
        result.addLeft(a);
        result.addRight(b);
        return result;
    }

    ///////////////////////////////////// multiplication /////////////////////////////////////
    public static ExpNode mul(Function a, Function b){
        return mul(new ExpNode(a), new ExpNode(b));
    }

    public static ExpNode mul(ExpNode a, Function b){
        return mul(a, new ExpNode(b));
    }

    public static ExpNode mul(Function a, ExpNode b){
        return mul(new ExpNode(a), b);
    }

    public static ExpNode mul(Function a, double b){
        return mul(new ExpNode(a), new ExpNode(new Function("const", "/", b)));
    }

    public static ExpNode mul(ExpNode a, double b){
        return mul(a, new ExpNode(new Function("const","/",b)));
    }

    public static ExpNode mul(ExpNode a, ExpNode b){
        ExpNode result = new ExpNode(new Function("*"));
        result.addLeft(a);
        result.addRight(b);
        return result;
    }

    ///////////////////////////////////// exponential /////////////////////////////////////
    public static ExpNode exp(Function a, Function b){
        return exp(new ExpNode(a), new ExpNode(b));
    }

    public static ExpNode exp(ExpNode a, Function b){
        return exp(a, new ExpNode(b));
    }

    public static ExpNode exp(Function a, double b){
       return exp(new ExpNode(a), new ExpNode(new Function("const", "/", b)));
    }

    public static ExpNode exp(ExpNode a, double b){
        return exp(a, new ExpNode(new Function("const","/",b)));
    }

    public static ExpNode exp(ExpNode a, ExpNode b) {
        ExpNode result = new ExpNode(new Function("^"));
        result.addLeft(a);
        result.addRight(b);
        return result;
    }
}
