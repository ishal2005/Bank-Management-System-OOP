package OopProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Abstract class representing a bank account
 */
abstract class Account {
    private String accountNumber;
    private String accountHolderName;
    protected double balance;

    public Account(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }
    
    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);
}

/**
 * Savings account implementation
 */
class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        }
    }
}

/**
 * Interface for bank operations
 */
interface BankOperations {
    void addAccount(Account account);
    Account getAccount(String accountNumber);
}

/**
 * Bank implementation
 */
class Bank implements BankOperations {
    private ArrayList<Account> accounts;

    public Bank() {
        accounts = new ArrayList<>();
    }

    @Override
    public void addAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public Account getAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
}

/**
 * Main GUI application for Bank Management System
 */
class BankManagementSystem extends JFrame implements ActionListener {
    private Bank bank;
    private JTextField accountNumberField, accountHolderField, amountField;
    private JTextArea outputArea;

    public BankManagementSystem() {
        bank = new Bank();
        initializeUI();
    }

    private void initializeUI() {
        // Main window configuration
        setTitle("Bank Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout(10, 10));

        // Create panels
        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();
        JPanel outputPanel = createOutputPanel();

        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);

        // Set application icon
        try {
            setIconImage(new ImageIcon("bank-icon.png").getImage());
        } catch (Exception e) {
            // Use default if icon not found
        }

        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            "Account Information"
        ));

        // Create styled labels
        JLabel accountNumberLabel = new JLabel("Account Number:");
        accountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel accountHolderLabel = new JLabel("Account Holder:");
        accountHolderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Style fields
        accountNumberField = new JTextField();
        accountNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        accountHolderField = new JTextField();
        accountHolderField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        amountField = new JTextField();
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Add components to panel
        panel.add(accountNumberLabel);
        panel.add(accountNumberField);
        panel.add(accountHolderLabel);
        panel.add(accountHolderField);
        panel.add(amountLabel);
        panel.add(amountField);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create styled buttons
        JButton createAccountButton = createStyledButton("Create Account", Color.decode("#4CAF50"));
        JButton depositButton = createStyledButton("Deposit", Color.decode("#2196F3"));
        JButton withdrawButton = createStyledButton("Withdraw", Color.decode("#F44336"));

        // Add action listeners
        createAccountButton.addActionListener(this);
        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);

        // Add buttons to panel
        panel.add(createAccountButton);
        panel.add(depositButton);
        panel.add(withdrawButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(140, 35));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createTitledBorder("Transaction Log")
        ));

        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        outputArea.setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String accountNumber = accountNumberField.getText().trim();
        String accountHolder = accountHolderField.getText().trim();
        String amountText = amountField.getText().trim();
        double amount = 0;

        // Validate inputs
        if (command.equals("Create Account")) {
            if (accountNumber.isEmpty() || accountHolder.isEmpty()) {
                outputArea.append("Error: Account number and holder name are required\n");
                return;
            }
        } else {
            if (accountNumber.isEmpty()) {
                outputArea.append("Error: Account number is required\n");
                return;
            }
        }

        // Validate amount
        if (!command.equals("Create Account")) {
            try {
                if (amountText.isEmpty()) {
                    outputArea.append("Error: Amount is required\n");
                    return;
                }
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    outputArea.append("Error: Amount must be positive\n");
                    return;
                }
            } catch (NumberFormatException ex) {
                outputArea.append("Error: Invalid amount format\n");
                return;
            }
        }

        // Process commands
        if (command.equals("Create Account")) {
            handleAccountCreation(accountNumber, accountHolder);
        } else {
            handleTransaction(command, accountNumber, amount);
        }

        // Clear input fields
        clearInputFields();
    }

    private void handleAccountCreation(String accountNumber, String accountHolder) {
        Account existingAccount = bank.getAccount(accountNumber);
        if (existingAccount != null) {
            outputArea.append("Error: Account number already exists\n");
            return;
        }

        SavingsAccount account = new SavingsAccount(accountNumber, accountHolder, 0);
        bank.addAccount(account);
        outputArea.append(String.format("[%s] Account created: %s (%s)\n",
            getCurrentTimestamp(), accountHolder, accountNumber));
    }

    private void handleTransaction(String command, String accountNumber, double amount) {
        Account account = bank.getAccount(accountNumber);
        if (account == null) {
            outputArea.append(String.format("[%s] Error: Account not found: %s\n",
                getCurrentTimestamp(), accountNumber));
            return;
        }

        if (command.equals("Deposit")) {
            account.deposit(amount);
            outputArea.append(String.format("[%s] Deposited $%.2f to %s (%s)\n",
                getCurrentTimestamp(), amount, account.getAccountHolderName(), accountNumber));
        } else if (command.equals("Withdraw")) {
            if (amount > account.getBalance()) {
                outputArea.append(String.format("[%s] Error: Insufficient funds in account %s\n",
                    getCurrentTimestamp(), accountNumber));
                return;
            }
            account.withdraw(amount);
            outputArea.append(String.format("[%s] Withdrew $%.2f from %s (%s)\n",
                getCurrentTimestamp(), amount, account.getAccountHolderName(), accountNumber));
        }

        outputArea.append(String.format("[%s] New balance: $%.2f\n\n",
            getCurrentTimestamp(), account.getBalance()));
    }

    private void clearInputFields() {
        accountNumberField.setText("");
        accountHolderField.setText("");
        amountField.setText("");
    }

    private String getCurrentTimestamp() {
        return java.time.LocalTime.now().toString().substring(0, 8);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankManagementSystem system = new BankManagementSystem();
            system.setVisible(true);
        });
    }
}