import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuSolver extends JFrame implements ActionListener {

    private final JTextField[][] cells = new JTextField[9][9];
    private final JButton solveButton = new JButton("Solve");

    private final Color backgroundColor = new Color(240, 248, 255); // Alice Blue
    private final Color cellColor = new Color(255, 255, 240);       // Ivory
    private final Color borderColor = new Color(224, 255, 255);     // Light Cyan
    private final Color textColor = new Color(70, 130, 180);        // Steel Blue

    public SudokuSolver() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridPanel.setBackground(backgroundColor);

        Font cellFont = new Font("SansSerif", Font.BOLD, 20);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(cellFont);
                cells[row][col].setBackground(cellColor);
                cells[row][col].setForeground(textColor);

                // Add thicker borders for 3x3 grid separation
                int top = (row % 3 == 0) ? 3 : 1;
                int left = (col % 3 == 0) ? 3 : 1;
                int bottom = (row == 8) ? 3 : 1;
                int right = (col == 8) ? 3 : 1;
                cells[row][col].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, borderColor));

                gridPanel.add(cells[row][col]);
            }
        }

        solveButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        solveButton.setBackground(new Color(224, 255, 255));
        solveButton.setForeground(textColor);
        solveButton.setFocusPainted(false);
        solveButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(solveButton);

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[][] board = new int[9][9];

        // Parse the input
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText();
                if (!text.isEmpty()) {
                    try {
                        int num = Integer.parseInt(text);
                        if (num >= 1 && num <= 9) {
                            board[i][j] = num;
                        } else {
                            JOptionPane.showMessageDialog(this, "Enter numbers between 1 and 9 only.");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid input at cell (" + (i+1) + "," + (j+1) + ")");
                        return;
                    }
                }
            }
        }

        if (solveSudoku(board)) {
            // Display the solved board
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    cells[i][j].setText(String.valueOf(board[i][j]));
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No solution exists for the given puzzle.");
        }
    }

    private boolean solveSudoku(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[row][col] = 0; // backtrack
                        }
                    }
                    return false; // no valid number found
                }
            }
        }
        return true; // solved
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        // Check row
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num) return false;
        }

        // Check column
        for (int x = 0; x < 9; x++) {
            if (board[x][col] == num) return false;
        }

        // Check 3x3 box
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuSolver::new);
    }
}
