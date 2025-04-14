import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuSolver extends JFrame implements ActionListener {

    private final JTextField[][] cells = new JTextField[9][9];
    private final JButton solveButton = new JButton("Solve");
    private final JButton clearButton = new JButton("Clear Board");

    private final Color backgroundColor = new Color(240, 248, 255); // Alice Blue
    private final Color cellColor = new Color(255, 255, 240);       // Ivory
    private final Color borderColor = new Color(224, 255, 255);     // Light Cyan
    private final Color textColor = new Color(70, 130, 180);        // Steel Blue
    private final Color errorColor = new Color(220, 20, 60);        // Crimson Red

    public SudokuSolver() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new MainPage(); // Relaunch main menu
            }
        });
        setSize(500, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setJMenuBar(createMenuBar());

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

        clearButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        clearButton.setBackground(new Color(255, 248, 220)); // Cornsilk
        clearButton.setForeground(textColor);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(_ -> clearBoard());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.addActionListener(_ -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        menu.add(exitItem);
        menuBar.add(menu);
        return menuBar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        resetCellColors();

        int[][] board = new int[9][9];

        // Read input and validate
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText().trim();

                if (!text.isEmpty()) {
                    try {
                        int num = Integer.parseInt(text);
                        if (num < 1 || num > 9) {
                            showInvalidEntry(i, j, "Please enter digits from 1 to 9 only.");
                            return;
                        }

                        board[i][j] = num;

                        cells[i][j].setText("");
                        if (!isValid(board, i, j, num)) {
                            showInvalidEntry(i, j, "Duplicate in row/column/box at (" + (i+1) + "," + (j+1) + ")");
                            return;
                        }
                        cells[i][j].setText(String.valueOf(num));

                    } catch (NumberFormatException ex) {
                        showInvalidEntry(i, j, "Please enter only numbers.");
                        return;
                    }
                } else {
                    board[i][j] = 0;
                }
            }
        }

        if (solveSudoku(board)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    cells[i][j].setText(String.valueOf(board[i][j]));
                    cells[i][j].setForeground(textColor);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No solution exists for the given puzzle.");
        }
    }

    private void showInvalidEntry(int row, int col, String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Entry", JOptionPane.ERROR_MESSAGE);
        cells[row][col].setText("");
        cells[row][col].setForeground(errorColor);
        cells[row][col].requestFocus();
    }

    private void resetCellColors() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                cells[i][j].setForeground(textColor);
    }

    private void clearBoard() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText("");
                cells[i][j].setForeground(textColor);
            }
    }

    private boolean solveSudoku(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num && x != col) return false;
            if (board[x][col] == num && x != row) return false;
        }

        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;

        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num && (i != row || j != col)) return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuSolver::new);
    }
}
