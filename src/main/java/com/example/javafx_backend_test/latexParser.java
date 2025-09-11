package com.example.javafx_backend_test;

import java.util.*;

public final class latexParser {

    private latexParser() {}

    public static String toLatex(String input) {
        if (input == null) return "";
        String s = noWS(input);
        List<Token> tokens = tokenize(s);
        List<Token> rpn = toRPN(tokens);
        return rpnToLatex(rpn);
    }

    // Remove all whitespace to avoid '+' artifacts for URL-encoding.
    private static String noWS(String s) {
        return s.replaceAll("\\s+", "");
    }

    // Token types
    private enum T { NUM, IDENT, OP, FUNC, LPAREN, RPAREN }

    private static final class Token {
        final T type;
        final String text;
        Token(T type, String text) { this.type = type; this.text = text; }
        public String toString() { return type + "(" + text + ")"; }
    }

    // Expression node used when converting RPN -> LaTeX
    private static final class Node {
        final String latex;
        final int prec; // precedence of the top operator in this node
        Node(String latex, int prec) {
            this.latex = latex;
            this.prec = prec;
        }
    }

    // Operator precedence (higher = binds tighter)
    // We assign: '=':1, '+/-':2, '*/':3, '/':3, '^':5, 'func':6, 'atom':10
    private static final Map<String,Integer> PREC = new HashMap<>();
    private static final Set<String> RIGHT_ASSOC = new HashSet<>();
    static {
        PREC.put("=", 1);
        PREC.put("+", 2);
        PREC.put("-", 2);
        PREC.put("*", 3);
        PREC.put("/", 3);
        PREC.put("^", 5);

        // Right-associative operators
        RIGHT_ASSOC.add("^");
    }

    private static boolean isOp(String s) {
        return PREC.containsKey(s);
    }

    // ---- 1) Tokenize ----
    private static List<Token> tokenize(String s) {
        List<Token> out = new ArrayList<>();
        for (int i = 0; i < s.length(); ) {
            char c = s.charAt(i);

            // whitespace already removed
            if (Character.isDigit(c) || (c == '.' && i + 1 < s.length() && Character.isDigit(s.charAt(i+1)))) {
                int j = i;
                boolean dot = (c == '.');
                j++;
                while (j < s.length()) {
                    char cj = s.charAt(j);
                    if (Character.isDigit(cj)) {
                        j++;
                    } else if (cj == '.' && !dot) {
                        dot = true; j++;
                    } else {
                        break;
                    }
                }
                out.add(new Token(T.NUM, s.substring(i, j)));
                i = j;
            } else if (Character.isLetter(c)) {
                int j = i + 1;
                while (j < s.length() && (Character.isLetterOrDigit(s.charAt(j)) || s.charAt(j) == '_')) j++;
                String ident = s.substring(i, j);
                if ("sqrt".equals(ident)) {
                    out.add(new Token(T.FUNC, ident));
                } else {
                    out.add(new Token(T.IDENT, ident));
                }
                i = j;
            } else if (c == '(') {
                out.add(new Token(T.LPAREN, "("));
                i++;
            } else if (c == ')') {
                out.add(new Token(T.RPAREN, ")"));
                i++;
            } else if ("+-*/^=".indexOf(c) >= 0) {
                out.add(new Token(T.OP, String.valueOf(c)));
                i++;
            } else if (c == ',') {
                // no multi-arg functions currently; accept to be lenient
                i++;
            } else {
                throw new IllegalArgumentException("Unexpected character: '" + c + "' at position " + i);
            }
        }
        return out;
    }

    // ---- 2) Shunting-yard to RPN ----
    private static List<Token> toRPN(List<Token> tokens) {
        List<Token> output = new ArrayList<>();
        Deque<Token> stack = new ArrayDeque<>();

        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            switch (t.type) {
                case NUM, IDENT -> output.add(t);
                case FUNC -> stack.push(t);
                case OP -> {
                    while (!stack.isEmpty() && (
                            (stack.peek().type == T.OP && (
                                    (prec(stack.peek()) > prec(t)) ||
                                    (prec(stack.peek()) == prec(t) && !isRightAssoc(t))
                            )) ||
                            (stack.peek().type == T.FUNC)
                    )) {
                        output.add(stack.pop());
                    }
                    stack.push(t);
                }
                case LPAREN -> stack.push(t);
                case RPAREN -> {
                    while (!stack.isEmpty() && stack.peek().type != T.LPAREN) {
                        output.add(stack.pop());
                    }
                    if (stack.isEmpty()) throw new IllegalArgumentException("Mismatched parentheses.");
                    stack.pop(); // pop '('
                    // If there is a function on top, pop it onto output
                    if (!stack.isEmpty() && stack.peek().type == T.FUNC) {
                        output.add(stack.pop());
                    }
                }
            }
        }
        while (!stack.isEmpty()) {
            Token t = stack.pop();
            if (t.type == T.LPAREN || t.type == T.RPAREN) throw new IllegalArgumentException("Mismatched parentheses.");
            output.add(t);
        }
        return output;
    }

    private static int prec(Token t) {
        return (t.type == T.OP) ? PREC.get(t.text) : Integer.MAX_VALUE;
    }

    private static boolean isRightAssoc(Token t) {
        return t.type == T.OP && RIGHT_ASSOC.contains(t.text);
    }

    // ---- 3) RPN -> LaTeX ----
    private static String rpnToLatex(List<Token> rpn) {
        Deque<Node> st = new ArrayDeque<>();
        for (Token t : rpn) {
            switch (t.type) {
                case NUM -> st.push(new Node(t.text, 10));
                case IDENT -> st.push(new Node(t.text, 10));
                case FUNC -> {
                    // only sqrt supported
                    Node a = st.pop();
                    st.push(new Node("\\\\sqrt{" + a.latex + "}", 6));
                }
                case OP -> {
                    if ("=".equals(t.text)) {
                        Node r = st.pop(), l = st.pop();
                        st.push(new Node(l.latex + " = " + r.latex, 1));
                    } else if ("+".equals(t.text)) {
                        Node r = st.pop(), l = st.pop();
                        st.push(new Node(wrap(l, 2) + " + " + wrap(r, 2), 2));
                    } else if ("-".equals(t.text)) {
                        Node r = st.pop(), l = st.pop();
                        st.push(new Node(wrap(l, 2) + " - " + wrap(r, 2), 2));
                    } else if ("*".equals(t.text)) {
                        Node r = st.pop(), l = st.pop();
                        st.push(new Node(wrap(l, 3) + " \\\\cdot " + wrap(r, 3), 3));
                    } else if ("/".equals(t.text)) {
                        Node r = st.pop(), l = st.pop();
                        st.push(new Node("\\\\frac{" + l.latex + "}{" + r.latex + "}", 4));
                    } else if ("^".equals(t.text)) {
                        Node r = st.pop(), l = st.pop();
                        st.push(new Node(wrap(l, 5) + "^{" + r.latex + "}", 5));
                    } else {
                        throw new IllegalStateException("Unknown operator: " + t.text);
                    }
                }
                default -> throw new IllegalStateException("Unexpected token in RPN: " + t);
            }
        }
        if (st.size() != 1) throw new IllegalStateException("Malformed expression. Stack=" + st);
        return st.peek().latex;
    }

    private static String wrap(Node n, int outerPrec) {
        return (n.prec < outerPrec) ? "\\\\left(" + n.latex + "\\\\right)" : n.latex;
    }

    // Optional quick test
    public static void main(String[] args) {
        String expr = "(4) * ((4) * (a)) = (4) * ((4) * (((8*9) + (3/2-8))))";
        System.out.println(toLatex(expr));
        // Expected shape (spacing may vary):
        // 4 \cdot \left(4 \cdot a\right) = 4 \cdot \left(4 \cdot \left(8 \cdot 9 + \frac{3}{2} - 8\right)\right)
    }
}
