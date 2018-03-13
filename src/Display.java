
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
/**
 * Created by llxxll on 1/24/2018.
 */
public class Display extends JFrame{
    private static final int CANVAS_WIDTH = 500;
    private static final int CANVAS_HEIGHT = 500;

    JPanel btnPanel = new JPanel();
    JLabel outputs;
    JButton btnCalc;
    JTextField inputs;

    public Display() {
        setLayout(new FlowLayout());
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        btnPanel.setLayout(null);
        inputs = new JTextField("Input an expression.");
        btnCalc = new JButton("Do calculation");
        outputs = new JLabel("Result is ...");

        /*inputs.setVisible(true);
        inputs.setSize(100, 10);
        inputs.setLocation(10, 10);

        btnCalc.setVisible(true);
        btnCalc.setSize(50, 10);
        btnCalc.setLocation(40, 30);

        outputs.setVisible(true);
        outputs.setSize(100, 10);
        outputs.setLocation(10, 50);*/

        /*btnPanel.add(inputs);
        btnPanel.add(btnCalc);
        btnPanel.add(outputs);
        btnPanel.setVisible(true);*/

        btnCalc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expression = inputs.getText();

            }
        });

        //Container cp = getContentPane();
        //cp.add(btnPanel);
        btnPanel.add(inputs);
        btnPanel.add(btnCalc);
        btnPanel.add(outputs);
        btnPanel.setBounds(100, 100, 100, 100);
        this.add(btnPanel);
        this.setVisible(true);

        inputs.setBounds(10, 10, 100, 10);
        btnCalc.setBounds(300, 400, 50, 10);
        outputs.setBounds(500, 600, 100, 10);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Basic Expression Calculator 1.0");
        pack();
        setVisible(true);
        requestFocus();
    }

    public static void main(String[] args) {
        Display myFrame = new Display();

    }

}
