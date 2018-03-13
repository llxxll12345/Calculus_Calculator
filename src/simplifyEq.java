import java.util.*;

public class simplifyEq {
    Map<String, Double> map = new HashMap<>();
    class Term {
        Double coeff = 1.0;
        List<String> var = new ArrayList<>();

        Term(Double x) {
            coeff = x;
        }
        Term(String s) {
            if (map.containsKey(s))
                coeff = map.get(s);
            else
                var.add(s);
        }
        Term(Term that) {
            this.coeff = that.coeff;
            this.var   = new ArrayList<>(that.var);
        }

        @Override
        public String toString() {
            if (coeff == 0) return "";
            String ans = "";
            for (String s:var) ans += "*" + s;
            return coeff + ans;
        }


        boolean equals(Term that) {
            if (this.var.size() != that.var.size()) return false;
            for (int i = 0;i < this.var.size(); i++)
                if (!this.var.get(i).equals(that.var.get(i)))
                    return false;
            return true;
        }
        int compareTo(Term that) {
            if (this.var.size() > that.var.size())
                return -1;
            if (this.var.size() < that.var.size())
                return 1;
            for (int i = 0;i < this.var.size(); i++) {
                int x = this.var.get(i).compareTo(that.var.get(i));
                if (x != 0) return x;
            }
            return 0;
        }
        /* compare terms */
        Term mul(Term that) {
            Term temp = new Term(this.coeff * that.coeff);
            for (String s:this.var) temp.var.add(new String(s));
            for (String s:that.var) temp.var.add(new String(s));
            Collections.sort(temp.var);
            return temp;
        }
    }
    class Expression {
        List<Term> list = new ArrayList<>();
        char oper = '+';

        Expression(Double x) {
            list.add(new Term(x));
        }
        Expression(String s) {
            list.add(new Term(s));
        }
        Expression(List<Term> l) {
            list=l;
        }
        Expression mul(Expression that) {
            List<Term> temp = new ArrayList<>();
            for (Term t1:this.list)
                for (Term t2:that.list)
                    temp.add(t1.mul(t2));
            temp = combine(temp);
            return new Expression(temp);
        }
        Expression plus(Expression that,int sign) {
            List<Term> temp = new ArrayList<>();
            for (Term t : this.list)
                temp.add(new Term(t));
            for (Term t : that.list) {
                Term t2  = new Term(t);
                t2.coeff = t2.coeff * sign;
                temp.add(t2);
            }
            temp = combine(temp);
            return new Expression(temp);
        }
        Expression cal(Expression that) {
            if (oper == '+')
                return plus(that, 1);
            if (oper=='-')
                return plus(that, -1);
            return mul(that); /* times */
        }
        List<String> toList() {
            List<String> ans = new ArrayList<>();
            for (Term t : list) {
                String s = t.toString();
                if (s.length() > 0)
                    ans.add(s);
            }
            /* to a list of terms */
            return ans;
        }
    }
    /* combine same terms */
    List<Term> combine(List<Term> termList) {
        Collections.sort(termList, (t1,t2) -> (t1.compareTo(t2)));
        List<Term> temp = new ArrayList<>();
        for (Term t : termList) {
            if (temp.size() != 0 && t.equals(temp.get(temp.size() - 1)))
                temp.get(temp.size() - 1).coeff += t.coeff;
            else temp.add(new Term(t));
        }
        return temp;
    }
    public List<String> simplifyEquation(String expression, Map<String, Double> variables, parser my_parse) {
        map = variables;
        int i = 0,len = expression.length();
        Stack<Expression> stack = new Stack<>();
        Stack<Integer> priStack = new Stack<>();
        Expression zero = new Expression(0.0);
        stack.push(zero);
        priStack.push(0);
        int priority = 0;

        while (i < len) {
            char ch = expression.charAt(i);
            if (Character.isDigit(ch)) {
                Double num = 0.0;
                while (i < len && Character.isDigit(expression.charAt(i))) {
                    num = num*10+(expression.charAt(i)-48);
                    i ++;
                }
                stack.add(new Expression(num));
                continue;
            }
            if (Character.isLetter(ch)) {
                String s = "";
                while (i < len && Character.isLetter(expression.charAt(i)))
                {
                    s += expression.charAt(i);
                    i ++;
                }
                stack.add(new Expression(s));
                continue;
            }
            if (ch=='(') priority += 2;
            if (ch==')') priority -= 2;
            if (ch=='+' || ch=='-' || ch=='*') {
                int curPri = priority;
                if (ch == '*') curPri++;
                while (!priStack.isEmpty() && curPri <= priStack.peek()) {
                    Expression cur = stack.pop(),last = stack.pop();
                    priStack.pop();
                    stack.push(last.cal(cur));
                }
                stack.peek().oper=ch;
                priStack.push(curPri);
            }
            i++;
        }
        while (stack.size() > 1) {
            Expression now = stack.pop(),last=stack.pop();
            stack.push(last.cal(now));
        }
        return stack.peek().toList();
    }
}
