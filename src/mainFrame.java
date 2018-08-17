import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.*;
import java.util.Map;
import javax.swing.*;

public class mainFrame extends JFrame{
    public mainFrame() {
        setTitle("Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int gap = 20;
        setSize(500, 500);
        setVisible(true);

        setLayout(null);

        JLabel mainReminder = new JLabel("lease input the expression and variable list");
        JTextField exprField = new JTextField("/Input an expression.");
        JButton btnCalc = new JButton("Do Calculation");

        JButton btnAdd = new JButton("Add variables");

        JTextField valueField = new JTextField("x=0;");

        List varList = new List();
        varList.addItemListener(l->{
            System.out.println(varList.getItem((Integer) l.getItem()));
        });

        btnAdd.addActionListener(e->{
            JDialog fastAdd = new JDialog();
            fastAdd.setLayout(new FlowLayout());
            fastAdd.setSize(300, 100);
            fastAdd.setVisible(true);
            JLabel  var = new JLabel("Var name: ");
            JTextField varT = new JTextField("/e.g: x");
            JLabel  val = new JLabel("Var value:");
            JTextField varV = new JTextField("/e.g: 5.0");
            JButton quickAdd = new JButton("Add");
            fastAdd.add(var);
            fastAdd.add(varT);
            fastAdd.add(val);
            fastAdd.add(varV);

            varT.addFocusListener(new myFocusListener());
            varV.addFocusListener(new myFocusListener());

            fastAdd.add(quickAdd);
            var.setVisible(true);
            quickAdd.addActionListener(event->{
                String res = valueField.getText();
                res += varT.getText() + '=' + varV.getText() + ';';
                varList.add(varT.getText() + '=' + varV.getText());
                valueField.setText(res);
                fastAdd.setVisible(false);
            });
        });

        JTextPane resultField = new JTextPane();
        JComboBox<String> methodBox = new JComboBox<>();

        methodBox.addItem("Get Derivative");
        methodBox.addItem("Get Value");
        methodBox.addItem("Get Integration");

        add(btnCalc);
        add(valueField);
        add(exprField);
        add(mainReminder);
        add(resultField);
        add(btnAdd);
        add(methodBox);
        add(varList);
        varList.setBounds(10 + gap, 130 + gap, 400, 90);
        btnCalc.requestFocus();

        setUp(mainReminder, gap, 10, 10, 400, 20);
        setUp(exprField, gap, 10,40, 400, 20);
        setUp(valueField, gap, 10, 70, 400, 20);
        setUp(btnCalc, gap, 10, 100, 100, 20);
        setUp(resultField, gap, 10, 230, 400, 300);
        setUp(btnAdd, gap, 130, 100, 100, 20);
        setUp(methodBox, gap, 250, 100, 100, 20);

        resultField.setEditable(false);

        btnCalc.addActionListener(e-> {
            String expr = exprField.getText();

            Map<String, Double> variables = new HashMap<>();
            String varL = valueField.getText();
            String[] combos = varL.split(";");
            for (String combo : combos) {
                String[] c = combo.split("=");
                if (variables.containsKey(c[0])) {
                    continue;
                }
                variables.put(c[0], Double.parseDouble(c[1]));
            }
            String resetStr = "";
            varList.removeAll();
            for (Entry<String, Double> entry : variables.entrySet()) {
                resetStr += entry.getKey() + "=" + entry.getValue() + ';';
                varList.add(entry.getKey() + "=" + entry.getValue());
            }
            valueField.setText(resetStr);


            try {
                parser myCal = new parser(expr, variables);
                ExpNode myTree = myCal.parseTree();
                System.out.println(myCal.deparse(myTree));
                String res = myCal.getResults(myTree);
                resultField.setText(res);
                System.out.println(res);

            } catch (Exception err) {
                resultField.setText("Error in your input");
            }
        });

        exprField.addFocusListener(new myFocusListener());
        valueField.addFocusListener(new myFocusListener());

    }

    class myFocusListener implements FocusListener{
        @Override
        public void focusGained(FocusEvent e) {
            if (!(e.getSource() instanceof JTextField)) {
                return;
            }
            if (((JTextField) e.getSource()).getText().charAt(0) == '/')
                ((JTextField) e.getSource()).setText("");
        }

        @Override
        public void focusLost(FocusEvent e) {

        }
    }

    private static void setUp(JComponent component, int gap, int x, int y) {
        Dimension size = component.getPreferredSize();
        component.setBounds(gap + x, gap + y, size.width, size.height);
    }

    private static void setUp(JComponent component, int gap, int x, int y, int incw, int inch) {
        component.setBounds(gap + x, gap + y, incw, inch);
    }


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new mainFrame();
        });
    }
}