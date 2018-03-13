/**
 * Created by llxxll on 1/24/2018.
 */
import java.util.*;

public class parser {
    private String expression = null;
    private List<String> rpn = null;        /* reverse Polish expression */

    private String[] OPS = {"+","-","*","/","^"};
    private boolean[] leftAssoc = {true, true, true, true, false};
    private int[] precendence = {2,2,3,3,4};
    private Map<String, Double> valueList;

    public parser(String expr) {
        this.expression = expr;
        this.valueList = new HashMap<>();
    }

    public parser(String expr, Map<String, Double> valueList) {
        this.expression = expr;
        this.valueList = valueList;
    }

    private boolean isOp(String str){
        for(String op : OPS) {
            if (op.equals(str)) return true;
        }
        return false;
    }

    private boolean isLeftAssoc(String token) {
        if (isOp(token)) {
            for (int i = 0; i < OPS.length; i++) {
                if (OPS[i].equals(token)) return leftAssoc[i];
            }
        } else {
            throw new IllegalArgumentException("Token must be a valid Operator!");
        }
        return false;
    }

    private int getPrecedence(String token) {
        if (isOp(token)) {
            for (int i = 0; i < OPS.length; i++) {
                if (OPS[i].equals(token)) return precendence[i];
            }
        }
        return -1;
    }

    //////////////////////////////
    public String deparse(ExpNode root) {
        if (root == null) return "";
        String expr = root.function.expr;
        System.out.println("ROOT exp: " + expr.equals("cos"));
        if (expr.equals("const")) return Double.toString(root.function.value);
        if (valueList.containsKey(expr)) return expr;
        if (expr.equals("cos") || expr.equals("sin") || expr.equals("ln")) {
            System.out.println("deparse: meet cos/sin/ln");
            return expr + "(" + deparse(root.function.subTree) + ")";
        }
        if ((isOp(expr) && (root.left == null || root.right == null)) || !isOp(expr))
            throw new IllegalArgumentException("Incomplete or Unsupported expression tree.");

        String result = "";
        String leftExpr = root.left.function.expr;
        String rightExpr = root.right.function.expr;

        if (isOp(leftExpr) && getPrecedence(expr) > getPrecedence(leftExpr)) {
            result += "(" + deparse(root.left) + ")";
        } else {
            result += deparse(root.left);
        }
        result += expr;
        if (isOp(rightExpr) && getPrecedence(expr) > getPrecedence(rightExpr)) {
            result += "(" + deparse(root.right) + ")";
        } else {
            result += deparse(root.right);
        }

        return  result;
    }
    //////////////////////////////
    public ExpNode parseTree() {
        myStack<ExpNode> stack = new myStack<>();
        for (String token : getRPNExpression()) {
            if (isOp(token)) {
                final ExpNode argLeft = stack.pop();
                final ExpNode argRight = stack.pop();
                System.out.println("TOKEN: " + token);
                switch(token) {
                    case "+": stack.push(Evals.sum(argLeft, argRight));break;
                    case "-": stack.push(Evals.sub(argLeft, argRight));break;
                    case "*": stack.push(Evals.mul(argLeft, argRight));break;
                    case "/": stack.push(Evals.div(argLeft, argRight));break;
                    case "^": stack.push(Evals.exp(argRight, argLeft));
                }
            } else if (token.equals("cos") || token.equals("sin") || token.equals("ln")){
                Function func = new Function(token,"~", 0.0);
                System.out.println("meet cos/sin/ln");
                func.subTree = stack.pop();
                stack.push(new ExpNode(func));
            } else {
                if (valueList.containsKey(token))
                    stack.push(new ExpNode(new Function(token, token, valueList.get(token))));
                else if (isNumber(token))
                    stack.push(new ExpNode((new Function("const", "/", Double.parseDouble(token)))));
            }
            /* binary tree structure generated using stack and method rewriting */
        }
        stack.peek().printByLevel();
        return stack.pop();
    }
    //////////////////////////////


    public List<String> getRPNExpression() {
        if (rpn == null) {
            rpn = shuntingYard(this.expression);
            validateRPN(rpn);
        }
        return rpn;
    }

    private boolean isNumber(String str) {
        System.out.println(str);
        if (str.length() == 1 && isOp(str)) return false;
        for (char ch : str.toCharArray()) {
            if (!Character.isDigit(ch) && ch != '-' && ch != '.' && ch != 'e' && ch != '+')
                return false;
        }
        return true;
    }

    private void validateRPN(List<String> rpn) {
        int cnt = 0;
        for (String token : rpn) {
            if(token.equals("cos") || token.equals("sin") || token.equals("ln"))
                continue;
            if (isOp(token)) {
                cnt -= 2;
            }
            if (cnt < 0) {
                throw new IllegalArgumentException("Too many operators or functions at: " + token);
            }
            cnt++;
        }
        if (cnt > 1) throw new IllegalArgumentException("Too many numbers or variables");
        else if (cnt < 1) throw new IllegalArgumentException("Empty expression");
    }

    /* shutting yard algorithm */
    private List<String> shuntingYard(String expression) {
        List<String> outputQueue = new ArrayList<>();
        myStack<String> stack = new myStack<>();

        tokenList tokenList = new tokenList(expression);
        Iterator<String> tokenIter = tokenList.iterator();

        while (tokenIter.hasNext()) {
            String token = tokenIter.next();
            if (token.equals("")) continue;
            if (isNumber(token) || valueList.containsKey(token)) {
                outputQueue.add(token);
            } else if (isOp(token)) {
                String nextToken = stack.isEmpty() ? null : stack.peek();
                System.out.println(nextToken);
                while ((nextToken != null && isOp(nextToken)
                        && ((isLeftAssoc(token) && getPrecedence(token) == getPrecedence(nextToken))|| getPrecedence(token) < getPrecedence(nextToken))))
                {
                    outputQueue.add(stack.pop());
                    nextToken = stack.isEmpty() ? null : stack.peek();
                    //nextOP = OPERATORS.get(nextToken);
                }
                stack.push(token);
            } else if (token.equals("cos") || token.equals("sin") || token.equals("ln")) {
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {         // Mistake corrected here. Be careful, it should be "(" here
                    outputQueue.add(stack.pop());
                }
                if (stack.isEmpty()) {
                    throw new RuntimeException("Mismatched parentheses");
                }
                stack.pop();
                if (!stack.isEmpty()) {
                    String front = stack.peek();
                    if (front.equals("cos") || front.equals("sin") || front.equals("ln")) {
                        outputQueue.add(stack.pop());
                    }
                }
            }
        }
        this.print(outputQueue);
        while (!stack.isEmpty()) {
            String element = stack.pop();
            if (element.equals("(") || element.equals(")")) {
                throw new RuntimeException("Mismatched parentheses");
            }
            if (!isOp(element)) {
                throw new RuntimeException("Unknown operator or function: " + element);
            }
            outputQueue.add(element);
        }
        return outputQueue;
    }

    public void print(List<String> list) {
        StringBuilder strB = new StringBuilder();
        for (String str : list) strB.append(str + " ");
        System.out.println(strB.toString());
    }

    public static ArrayList<String> derivativeExample(ExpNode ExpressionTree) {
        //define variables that will be used
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> variables = new ArrayList<>();
        ArrayList<Double> values = new ArrayList<>();
        values.add(5.0);
        variables.add("x");
        values.add(5.0);
        variables.add("y");
        //machine that handles computations
        Derivative.setVars(variables);
        Derivative.setValues(values);

        ExpNode f = ExpressionTree;
        //simplyfing the expression
        f = ExpressionTree.simplifyExpression(f);
        //printing the expression tree, by level
        //f.printByLevel();
        //f evaluated at specified point
        result.add("Function evaluated:\n" + Derivative.compute(f) + "\n");
        System.out.println("Function evaluated:\n" + Derivative.compute(f) + "\n");
        //computing the first derivative od f, with respect to x
        ExpNode firstDerivativeX = Derivative.computeDerivative("x", f);
        if (firstDerivativeX != null) {
            firstDerivativeX = ExpressionTree.simplifyExpression(firstDerivativeX);
            //f.printByLevel();
            result.add("First derivative d/dx\n" + Derivative.compute(firstDerivativeX) + "\n");
            System.out.println("First derivative d/dx\n" + Derivative.compute(firstDerivativeX) + "\n");
        }
        //computing the first derivative od f, with respect to x
        ExpNode firstDerivativeY = Derivative.computeDerivative("y", f);
        if (firstDerivativeY != null) {
            firstDerivativeY = ExpressionTree.simplifyExpression(firstDerivativeY);
            //f.printByLevel();
            result.add("First derivative d/dy\n" + Derivative.compute(firstDerivativeY) + "\n");
            System.out.println("First derivative d/dy\n" + Derivative.compute(firstDerivativeY) + "\n");
        }

        ExpNode secondDerivativeXX = null;
        if (firstDerivativeX != null) secondDerivativeXX = Derivative.computeDerivative("x", firstDerivativeX);
        if (secondDerivativeXX != null) {
            secondDerivativeXX = ExpressionTree.simplifyExpression(secondDerivativeXX);
            //f.printByLevel();
            result.add("Second derivative d(d/dx)/dx\n" + Derivative.compute(secondDerivativeXX) + "\n");
            System.out.println("Second derivative d(d/dx)/dx\n" + Derivative.compute(secondDerivativeXX) + "\n");
        }

        ExpNode secondDerivativeYY = null;
        if (firstDerivativeY != null) secondDerivativeYY = Derivative.computeDerivative("y", firstDerivativeY);
        if (secondDerivativeYY != null) {
            secondDerivativeYY = ExpressionTree.simplifyExpression(secondDerivativeYY);
            //secondDerivativeYY.printByLevel();
            result.add("Second derivative d(d/dy)/dy\n" + Derivative.compute(secondDerivativeYY) + "\n");
            System.out.println("Second derivative d(d/dy)/dy\n" + Derivative.compute(secondDerivativeYY) + "\n");
        }


        ExpNode secondDerivativeXY = null;
        if (firstDerivativeX != null) secondDerivativeXY = Derivative.computeDerivative("y", firstDerivativeX);
        if (secondDerivativeXY != null) {
            secondDerivativeXY = ExpressionTree.simplifyExpression(secondDerivativeXY);
            //f.printByLevel();
            result.add("Mixed second derivative d(d/dx)/dy\n" + Derivative.compute(secondDerivativeXY));
            System.out.println("Mixed second derivative d(d/dx)/dy\n" + Derivative.compute(secondDerivativeXY));
        }

        ExpNode thirdDerivativeXXX = null;
        if (secondDerivativeXX != null) thirdDerivativeXXX = Derivative.computeDerivative("x", secondDerivativeXX);
        if (thirdDerivativeXXX != null) {
            thirdDerivativeXXX = ExpressionTree.simplifyExpression(thirdDerivativeXXX);
            //f.printByLevel();
            System.out.println("\nThird derivative d(d(d/dx)/dx)/dx\n" + Derivative.compute(thirdDerivativeXXX));
        }

        ExpNode thirdDerivativeYYY = null;
        if (secondDerivativeYY != null) thirdDerivativeYYY = Derivative.computeDerivative("y", secondDerivativeYY);
        if (thirdDerivativeYYY != null) {
            thirdDerivativeYYY = ExpressionTree.simplifyExpression(thirdDerivativeYYY);
            //f.printByLevel();
            System.out.println("\nThird derivative d(d(d/dy)/dy)/dy\n" + Derivative.compute(thirdDerivativeYYY));
        }

        return result;
    }

    public static void main(String[] args) {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 0.0);
        variables.put("y", 1.0);
        parser myCal = new parser("cos((y^(0.5)*x^(1.5) + y^(2.2)*x^(-0.7))^(-2.6))",variables);
        ExpNode myTree = myCal.parseTree();
        System.out.println(myCal.deparse(myTree));
        //myCal.print(myCal.shuntingYard("(sqrt(sqrt(10.5 + -2e)))      ^2"));
        //myCal.print(myCal.getRPNExpression());
       // myCal.parseTree();
        //System.out.println(myCal.init());
    }
}
