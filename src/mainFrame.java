import java.awt.Container;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class mainFrame extends JFrame{
    public static void addComponentsToPane(Container pane) {
        pane.setLayout(null);

        JLabel mainReminder = new JLabel("Please input the expression and variable list");
        JTextField exprField = new JTextField("INPUT EXPRESSION HERE");
        JButton btnCalc = new JButton("Do Calculation");
        JTextField ValueFiled = new JTextField("INPUT Variables and Values here");
        JButton btnImport = new JButton("Import data from .json");
        JLabel fileLabel= new JLabel("file name ...");
        JTextPane resultField = new JTextPane();

        pane.add(btnCalc);
        pane.add(ValueFiled);
        pane.add(exprField);
        pane.add(mainReminder);
        pane.add(resultField);
        pane.add(btnImport);
        pane.add(fileLabel);

        Insets insets = pane.getInsets();
        setUp(mainReminder, insets, 10, 10);
        setUp(exprField, insets, 10,40);
        setUp(ValueFiled, insets, 10, 70);
        setUp(btnCalc, insets, 10, 100);
        setUp(resultField, insets, 10, 130, 400, 400);

        resultField.setEditable(false);

        btnCalc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expr = exprField.getText();
                Map<String, Double> variables = new HashMap<>();
                variables.put("x", 5.0);
                variables.put("y", 5.0);
                parser myCal = new parser(expr, variables);
                ExpNode myTree = myCal.parseTree();
                System.out.println(myCal.deparse(myTree));

                String res = myCal.getResults(myTree);
                resultField.setText(res);
                System.out.println(res);
            }
        });
    }



    private static void setUp(JComponent component, Insets insets, int x, int y) {
        Dimension size = component.getPreferredSize();
        component.setBounds(insets.left + x, insets.top + y, size.width, size.height);
    }

    private static void setUp(JComponent component, Insets insets, int x, int y, int incw, int inch) {
        Dimension size = component.getPreferredSize();
        component.setBounds(insets.left + x, insets.top + y, size.width + incw, size.height + inch);
    }

    private static void createAndShowGUI() {
        mainFrame frame = new mainFrame();
        frame.setTitle("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentsToPane(frame.getContentPane());

        Insets insets = frame.getInsets();
        frame.setSize(500 + insets.left + insets.right,
                500 + insets.top + insets.bottom);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}