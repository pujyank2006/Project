import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;

public class SudokuPage extends JFrame {

    public SudokuPage() {
        setTitle("Sudoku Menu");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1, 10, 10));

        JButton gameButton = new JButton("Sudoku Game");
        JButton solverButton = new JButton("Sudoku Solver");

        gameButton.setFont(new Font("Arial", Font.BOLD, 18));
        solverButton.setFont(new Font("Arial", Font.BOLD, 18));

        gameButton.addActionListener(_ -> {
            dispose();               // Close menu
            new SudokuGame();     // Launch game
        });

        solverButton.addActionListener(_ -> {
            new SudokuSolver(); // if you have a solver UI
        });

        add(gameButton);
        add(solverButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuPage::new);
    }
}

