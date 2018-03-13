import java.util.*;

/**
 * Created by llxxll on 1/24/2018.
 */
public class tokenList implements Iterable<String> {
    private int pos = 0;
    private String input;
    private String prevToken;
    String[] OPS = {"+","-","*","/","^"};

    tokenList(String input) {
        this.input = input.trim();      // remove spaces at the beginning and the end
    }

    private boolean isOp(String str){
        for(String op : OPS) {
            if (op.equals(str)) return true;
        }
        return false;
    }

    private char peekNextChar() {
        if (pos < (input.length() - 1)) {
            return input.charAt(pos + 1);
        } else {
            return 0;
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new TokenListIterator();
    }

    public class TokenListIterator implements Iterator<String>{
        myStack<String> parentheses = new myStack<>();
        boolean toBeInserted = false;
        @Override
        public boolean hasNext() {
            return (pos < input.length());
        }

        @Override
        public String next() {
            //System.out.println("To be inserted" + toBeInserted + "previous token:" + prevToken);
            //System.out.println("stack size:" + parentheses.size());

            StringBuilder tokenList = new StringBuilder();
            if (pos >= input.length()) {
                return prevToken = null;
            }
            char ch = input.charAt(pos);
            while (ch == ' ' && pos < input.length()) {
                ch = input.charAt(++pos);                                       // remove spaces
            }
            if (toBeInserted == true) {
                /* Mistake corrected. Rmember to use equals when comparing strings in JAVA !!!!!*/
                if (prevToken.equals(")")) {prevToken = "^";return prevToken;}
                if (prevToken.equals("^")) {
                    toBeInserted = false;
                    return prevToken = "0.5";
                }
            }
            if (Character.isDigit(ch)) {
                while (isPartOfNum(ch) && (pos < input.length())) {
                    tokenList.append(input.charAt(pos++));                      // Create number from characters
                    ch = (pos == input.length()) ? 0 : input.charAt(pos);
                }
            } else if (isStartingMinus(ch)) {
                tokenList.append('-');
                pos++;
                tokenList.append(next());
            } else if (ch == '(' || ch == ')') {
                if (!parentheses.isEmpty() && ch == ')'){
                    //System.out.println(pos);
                    parentheses.pop();
                    tokenList.append(ch);
                    toBeInserted = true;
                    pos ++;
                }
                else {
                    tokenList.append(ch);
                    pos++;
                }
            } else if (Character.isLetter(ch)) {
                String opName = "";
                while (Character.isLetter(ch) && (pos < input.length())) {
                    opName += ch;
                    pos ++;
                    ch = (pos == input.length()) ? 0 : input.charAt(pos);
                }
                if (opName.equals("sqrt")){
                    if (input.charAt(pos) != '(') throw new IllegalArgumentException("Incorrect Format");
                    else parentheses.push("(");
                }
                else tokenList.append(opName);
            } else {
                while (otherCharater(ch) && (pos < input.length())) {
                    tokenList.append(input.charAt(pos));
                    pos++;
                    ch = (pos == input.length()) ? 0 : input.charAt(pos);
                }
                if (!isOp(tokenList.toString())) {
                    throw new IllegalArgumentException("Unknown operator: " + tokenList);
                }
            }
            return prevToken = tokenList.toString();
        }

        @Override
        public void remove() {
            throw new IllegalArgumentException("You can't remove an element from token list.");
        }
    }

    private boolean otherCharater(char ch) {
        /* deal with operators */
        return !Character.isLetter(ch) && !Character.isDigit(ch) && ch != '-' && !Character.isWhitespace(ch) && ch != '(' && ch != ')';
    }

    private boolean isStartingMinus(char ch) {
        /* is a statring minus sign of a number */
        return ch == '-' && Character.isDigit(peekNextChar()) && ("(".equals(prevToken)|| prevToken == null || isOp(prevToken));
    }

    private boolean isPartOfNum(char ch) {
        return (Character.isDigit(ch) || ch == '.' || ch == 'e');
    }

    public static void main(String[] args) {
        tokenList myList = new tokenList("(cos(10.5 + -2e))      ^2");
        myList.peekNextChar();
        Iterator<String> TokenIter = myList.iterator();
        while (TokenIter.hasNext()) {
            System.out.println(TokenIter.next());
        }
    }
}
