import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by llxxll on 2/2/2018.
 */

public class tester {
    // (y^(0.5)*x^(1.5) + y^(2.2)*x^(-0.7))^(-2.6)
    public static ExpNode equationExample0() {
        ExpNode Q1 = Evals.mul(new Function("x", "x", 5), 2);
        ExpNode Q2 = Evals.mul(new Function("y", "y", 5), 3);
        //Q1.printByLevel();
        return Evals.sum(Q1, Q2);
    }

    public static ExpNode equationExample1(){
        ExpNode Q1 = Evals.exp(new Function("y","y",5),2);
        ExpNode Q2 = Evals.exp(new Function("x","x",5),0);
        Q1 = Evals.mul(Q1, Q2);

        ExpNode a1 = Evals.exp(new Function("y","y",5),0);
        ExpNode a2 = Evals.exp(new Function("x","x",5),0);
        a1 = Evals.mul(a1, a2);
        a1 = Evals.sum(Q1, a1);
        return Evals.exp(a1, 1);
    }

    // (ln(y)^(0.5)*ln(x)^(1.5) + ln(y)^(2.2)*ln(x)^(-0.7))^(-2.6)
    public static ExpNode equationExample2(){
        ExpNode Q1 = Evals.exp(new Function("ln","y",0),0.5);
        ExpNode Q2=Evals.exp(new Function("ln","x",0),1.5);
        Q1 = Evals.mul(Q1, Q2);

        ExpNode a1=Evals.exp(new Function("ln","y",0),2.2);
        ExpNode a2=Evals.exp(new Function("ln","x",0),-0.7);
        a1=Evals.mul(a1, a2);
        a1=Evals.mul(Q1, a1);
        return Evals.exp(a1, -2.6);
    }
    // ((ln(y)^(0.5)*ln(x)^(1.5) + ln(y)^(2.2)*ln(x)^(-0.7))^(-2.6) + (y^(0.5)*x^(1.5) + y^(2.2)*x^(-0.7))^(-2.6) )^(4.2)
    public static ExpNode equationExample3(){
        ExpNode a= Evals.mul(equationExample1(),equationExample2());
        return Evals.exp(a,4.2);
    }
    //sin(cos( (y^(0.5)*x^(1.5) + y^(2.2)*x^(-0.7))^(-2.6) )^(-2.5))
    public static ExpNode equationExample4(){
        Function s = new Function("cos", "~", 0);
        ExpNode Q1= Evals.exp(new Function("y","y",0),0.5);
        ExpNode Q2=Evals.exp(new Function("x","x",0),1.5);
        Q1 = Evals.mul(Q1, Q2);
        s.subTree=equationExample1();
        ExpNode a=Evals.exp(s, -2.5);
        Function d=new Function("sin", "~", 0);
        d.subTree=a;
        return new ExpNode(d);
    }
    // (y^(2.3)+x^(4.1))(y^(1.9)+x^(-4.7))
    public static ExpNode equationExample5(){
        ExpNode Q1 = Evals.exp(new Function("y","y",0),2.3);
        ExpNode Q2 = Evals.exp(new Function("x","x",0),4.1);
        Q1 = Evals.sum(Q1, Q2);

        ExpNode a1 = Evals.exp(new Function("y","y",0),1.9);
        ExpNode a2 = Evals.exp(new Function("x","x",0),-4.7);
        a1 = Evals.sum(a1, a2);
        a1 = Evals.mul(Q1, a1);
        return a1;
    }
    // sin( sin(cos( (y^(0.5)*x^(1.5) + y^(2.2)*x^(-0.7))^(-2.6) )^(-2.5) ) + cos((y^(2.3)+x^(4.1))(y^(1.9)+x^(-4.7))) )
    public static ExpNode equationExample6(){
        Function d=new Function("cos", "~", 0);
        d.subTree =equationExample5();
        ExpNode t=Evals.mul(equationExample4(), d);
        Function e=new Function("sin", "~", 0);
        e.subTree =t;
        return new ExpNode(e);
    }


    public static void derivativeExample(ExpNode ExpressionTree){
        //define variables that will be used
        ArrayList<String> variables=new ArrayList<String>();
        ArrayList<Double> values=new ArrayList<Double>();
        values.add(5.0);
        variables.add("x");
        values.add(5.0);
        variables.add("y");
        //machine that handles computations
        Derivative.setVars(variables);
        Derivative.setValues(values);

        ExpNode f= ExpressionTree;
        //simplyfing the expression
        f = ExpressionTree.simplifyExpression(f);
        //printing the expression tree, by level
        //f.printByLevel();
        //f evaluated at specified point
        System.out.println("Function evaluated:\n" + Derivative.compute(f)+"\n");
        //computing the first derivative od f, with respect to x
        ExpNode firstDerivativeX = Derivative.computeDerivative("x", f);
        if(firstDerivativeX != null){
            firstDerivativeX = ExpressionTree.simplifyExpression(firstDerivativeX);
            //f.printByLevel();
            System.out.println("First derivative d/dx\n"+Derivative.compute(firstDerivativeX)+"\n");
        }
        //computing the first derivative od f, with respect to x
        ExpNode firstDerivativeY=Derivative.computeDerivative("y", f);
        if(firstDerivativeY!=null){
            firstDerivativeY= ExpressionTree.simplifyExpression(firstDerivativeY);
            //f.printByLevel();
            System.out.println("First derivative d/dy\n"+Derivative.compute(firstDerivativeY)+"\n");
        }

        ExpNode secondDerivativeXX=null;
        if(firstDerivativeX!=null) secondDerivativeXX=Derivative.computeDerivative("x", firstDerivativeX);
        if(secondDerivativeXX!=null){
            secondDerivativeXX= ExpressionTree.simplifyExpression(secondDerivativeXX);
            //f.printByLevel();
            System.out.println("Second derivative d(d/dx)/dx\n"+Derivative.compute(secondDerivativeXX)+"\n");
        }

        ExpNode secondDerivativeYY=null;
        if(firstDerivativeY!=null) secondDerivativeYY=Derivative.computeDerivative("y", firstDerivativeY);
        if(secondDerivativeYY!=null){
            secondDerivativeYY= ExpressionTree.simplifyExpression(secondDerivativeYY);
            //secondDerivativeYY.printByLevel();
            System.out.println("Second derivative d(d/dy)/dy\n"+Derivative.compute(secondDerivativeYY)+"\n");
        }


        ExpNode secondDerivativeXY=null;
        if(firstDerivativeX!=null) secondDerivativeXY=Derivative.computeDerivative("y", firstDerivativeX);
        if(secondDerivativeXY!=null){
            secondDerivativeXY= ExpressionTree.simplifyExpression(secondDerivativeXY);
            //f.printByLevel();
            System.out.println("Mixed second derivative d(d/dx)/dy\n"+Derivative.compute(secondDerivativeXY));
        }

        ExpNode thirdDerivativeXXX=null;
        if(secondDerivativeXX!=null) thirdDerivativeXXX=Derivative.computeDerivative("x", secondDerivativeXX);
        if(thirdDerivativeXXX!=null){
            thirdDerivativeXXX= ExpressionTree.simplifyExpression(thirdDerivativeXXX);
            //f.printByLevel();
            System.out.println("\nThird derivative d(d(d/dx)/dx)/dx\n"+Derivative.compute(thirdDerivativeXXX));
        }

        ExpNode thirdDerivativeYYY=null;
        if(secondDerivativeYY!=null) thirdDerivativeYYY=Derivative.computeDerivative("y", secondDerivativeYY);
        if(thirdDerivativeYYY!=null){
            thirdDerivativeYYY= ExpressionTree.simplifyExpression(thirdDerivativeYYY);
            //f.printByLevel();
            System.out.println("\nThird derivative d(d(d/dy)/dy)/dy\n"+Derivative.compute(thirdDerivativeYYY));
        }
    }

    public static void main(String[] args){
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 5.0);
        variables.put("y", 5.0);
        parser myCal = new parser( "(y^(2.3)+x^(4.1))(y^(1.9)+x^(-4.7))",variables);
        derivativeExample(equationExample0());
        derivativeExample(myCal.parseTree());
        System.out.println(myCal.deparse(myCal.parseTree()));
        //derivativeExample(equationExample0());
       // System.out.println(Derivative.compute(equationExample0()));
        //derivativesExample();
    }
}
