import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SudokuGame extends JFrame {

    private final JTextField[][] cells = new JTextField[9][9];
    private int[][] currentBoard;
    private final int[][][] puzzles = {
        {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        },
        {
            {0, 2, 0, 6, 0, 8, 0, 0, 0},
            {5, 8, 0, 0, 0, 9, 7, 0, 0},
            {0, 0, 0, 0, 4, 0, 0, 0, 0},
            {3, 7, 0, 0, 0, 0, 5, 0, 0},
            {6, 0, 0, 0, 0, 0, 0, 0, 4},
            {0, 0, 8, 0, 0, 0, 0, 1, 3},
            {0, 0, 0, 0, 2, 0, 0, 0, 0},
            {0, 0, 9, 8, 0, 0, 0, 3, 6},
            {0, 0, 0, 3, 0, 6, 0, 9, 0}
        },
        {
            {0, 0, 0, 0, 0, 0, 2, 0, 0},
            {0, 8, 0, 0, 0, 7, 0, 9, 0},
            {6, 0, 2, 0, 0, 0, 5, 0, 0},
            {0, 7, 0, 0, 6, 0, 0, 0, 0},
            {0, 0, 0, 9, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 2, 0, 0, 4, 0},
            {0, 0, 5, 0, 0, 0, 6, 0, 3},
            {0, 9, 0, 4, 0, 0, 0, 7, 0},
            {0, 0, 6, 0, 0, 0, 0, 0, 0}
        }
    };

    private final Color initialColor = new Color(200, 200, 200);
    private final Color editableColor = Color.WHITE;
    private final Color textColor = new Color(50, 50, 120);
    private final Color errorColor = Color.RED;

    public SudokuGame() {
        setTitle("Play Sudoku");
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

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font font = new Font("SansSerif", Font.BOLD, 20);

        for (int row = 0; row < 9; row++)
            for (int col = 0; col < 9; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(font);
                cells[row][col] = cell;
                gridPanel.add(cell);
            }

        JButton checkButton = new JButton("Check");
        checkButton.addActionListener(_ -> checkBoard());

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(_ -> loadRandomPuzzle());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(checkButton);
        buttonPanel.add(playAgainButton);

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadRandomPuzzle();
        setVisible(true);
    }

    private void loadRandomPuzzle() {
        Random rand = new Random();
        currentBoard = deepCopy(puzzles[rand.nextInt(puzzles.length)]);
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                JTextField cell = cells[i][j];
                if (currentBoard[i][j] != 0) {
                    cell.setText(String.valueOf(currentBoard[i][j]));
                    cell.setEditable(false);
                    cell.setBackground(initialColor);
                    cell.setForeground(textColor);
                } else {
                    cell.setText("");
                    cell.setEditable(true);
                    cell.setBackground(editableColor);
                    cell.setForeground(textColor);
                    final int r = i, c = j;
                    cell.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            String text = cell.getText().trim();
                            if (!text.matches("[1-9]?")) {
                                JOptionPane.showMessageDialog(null, "Please enter digits 1 to 9 only.");
                                cell.setText("");
                            } else if (!text.isEmpty() && !isValidMove(r, c, Integer.parseInt(text))) {
                                cell.setForeground(errorColor);
                            } else {
                                cell.setForeground(textColor);
                            }
                        }
                    });
                }
            }
    }

    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        return copy;
    }

    private boolean isValidMove(int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (i != col && getCellValue(row, i) == num) return false;
            if (i != row && getCellValue(i, col) == num) return false;
        }

        int boxRow = row / 3 * 3;
        int boxCol = col / 3 * 3;
        for (int i = boxRow; i < boxRow + 3; i++)
            for (int j = boxCol; j < boxCol + 3; j++)
                if ((i != row || j != col) && getCellValue(i, j) == num) return false;

        return true;
    }

    private int getCellValue(int row, int col) {
        String text = cells[row][col].getText().trim();
        if (!text.isEmpty() && text.matches("[1-9]")) {
            return Integer.parseInt(text);
        }
        return 0;
    }

    private void checkBoard() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText().trim();
                if (!text.isEmpty()) {
                    int num = Integer.parseInt(text);
                    cells[i][j].setForeground(isValidMove(i, j, num) ? textColor : errorColor);
                }
            }
        JOptionPane.showMessageDialog(this, "Checked board for conflicts (Red = conflict).");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGame::new);
    }
}
