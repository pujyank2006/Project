import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame implements ActionListener {

    private JButton[][] buttons = new JButton[3][3];
    private char[][] board = new char[3][3];
    private char currentPlayer = 'X';
    private boolean gameActive = true;
    private JLabel statusLabel;
    private JButton playAgainButton;

    // Mild color palette
    private Color backgroundColor = new Color(240, 248, 255); // Alice Blue
    private Color buttonColor = new Color(224, 255, 255);     // Light Cyan
    private Color buttonHoverColor = new Color(204, 255, 255); // Slightly darker Cyan
    private Color textColor = new Color(70, 130, 180);         // Steel Blue
    private Color winningColor = new Color(255, 160, 122);     // Light Salmon

    public TicTacToe() {
        setTitle("Playful Tic-Tac-Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 500)); // Increased height for status and play again

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add some padding

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3, 5, 5)); // Add some spacing between buttons
        boardPanel.setBackground(backgroundColor);

        statusLabel = new JLabel("Player X's turn");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        statusLabel.setForeground(textColor);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(new EmptyBorder(0, 0, 10, 0)); // Padding below status

        playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        playAgainButton.setBackground(new Color(176, 224, 230)); // LightSteelBlue
        playAgainButton.setForeground(textColor);
        playAgainButton.setFocusPainted(false);
        playAgainButton.addActionListener(_ -> resetGame());
        playAgainButton.setVisible(false); // Initially hide the button

        initializeBoard();
        addButtons(boardPanel);

        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(playAgainButton, BorderLayout.SOUTH);

        add(mainPanel);
        pack(); // Adjusts window size to fit components
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private void addButtons(JPanel panel) {
        for (int i = 0; i < 3; i++) {
            final int row = i; // Create a final local copy of i
            for (int j = 0; j < 3; j++) {
                final int col = j; // Create a final local copy of j
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[i][j].setBackground(buttonColor);
                buttons[i][j].setForeground(textColor);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);

                // Add hover effect
                buttons[i][j].getModel().addChangeListener(e -> {
                    ButtonModel model = (ButtonModel) e.getSource();
                    if (model.isRollover() && buttons[row][col].isEnabled()) {
                        buttons[row][col].setBackground(buttonHoverColor);
                    } else {
                        buttons[row][col].setBackground(buttonColor);
                    }
                });
                panel.add(buttons[i][j]);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameActive) {
            return;
        }

        JButton clickedButton = (JButton) e.getSource();
        int row = -1, col = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == clickedButton) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        if (row != -1 && board[row][col] == ' ') {
            board[row][col] = currentPlayer;
            clickedButton.setText(String.valueOf(currentPlayer));
            clickedButton.setEnabled(false);

            if (isWinner()) {
                statusLabel.setText("Player " + currentPlayer + " wins!");
                highlightWinningLine();
                gameActive = false;
                playAgainButton.setVisible(true);
            } else if (isBoardFull()) {
                statusLabel.setText("The game is a tie!");
                gameActive = false;
                playAgainButton.setVisible(true);
            } else {
                switchPlayer();
                statusLabel.setText("Player " + currentPlayer + "'s turn");
            }
        }
    }

    private void highlightWinningLine() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                setWinningColor(buttons[i][0], buttons[i][1], buttons[i][2]);
                return;
            }
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                setWinningColor(buttons[0][i], buttons[1][i], buttons[2][i]);
                return;
            }
        }
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            setWinningColor(buttons[0][0], buttons[1][1], buttons[2][2]);
            return;
        }
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            setWinningColor(buttons[0][2], buttons[1][1], buttons[2][0]);
            return;
        }
    }

    private void setWinningColor(JButton b1, JButton b2, JButton b3) {
        b1.setForeground(winningColor);
        b2.setForeground(winningColor);
        b3.setForeground(winningColor);
    }

    private boolean isWinner() {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) ||
                    (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer)) {
                return true;
            }
        }
        if ((board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
                (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer)) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private void resetGame() {
        initializeBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setForeground(textColor); // Reset text color
            }
        }
        currentPlayer = 'X';
        gameActive = true;
        statusLabel.setText("Player X's turn");
        playAgainButton.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }
}