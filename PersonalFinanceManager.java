import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

abstract class Transaction {
	protected String type;
	protected String category;
	protected double amount;
	
	public Transaction() {
		type = "";
		category = "";
		amount = 0.0;
	}
	
	public Transaction(String t, String c, Double a) {
		type = t;
		category = c;
		amount = a;
	}
	
	public String getType() {
		return type;
	}
	public String getCategory() {
		return category;
	}
	public double getAmount() {
		return amount;
	}
	public abstract void displayInfo();
}

class IncomeTransaction extends Transaction {
	public IncomeTransaction(String category, double amount) {
		type = "Income";
		this.category = category;
		this.amount = amount;
	}
	
	@Override
	public void displayInfo() {
		System.out.printf("Income, %s, $%.2f%n", category, amount);
	}
}


class ExpenseTransaction extends Transaction {
	public ExpenseTransaction(String category, double amount) {
		type = "Expense";
		this.category = category;
		this.amount = amount;
	}
	
	@Override
	public void displayInfo() {
		System.out.printf("Expense, %s, $%.2f%n", category, amount);
	}
}


class FinancialManager {
	private ArrayList<Transaction> transactions;
	
	public FinancialManager(ArrayList<Transaction> t) {
		transactions = t;
	}
	
	public void addIncome(Transaction t) {
		transactions.add(t);
		System.out.println("Successfully added income");
		t.displayInfo();
	}
	
	public void addExpense(Transaction t) {
		transactions.add(t);
		System.out.println("Successfully added expense");
		t.displayInfo();
	}
	
	public void displaySummary() {
		
		double totalIncome = 0.0;
		double totalExpense = 0.0;
		
		for (Transaction t : transactions) {
			if (t instanceof IncomeTransaction) {
				totalIncome += t.getAmount();
			} else if (t instanceof ExpenseTransaction) {
				totalExpense += t.getAmount();
			}
		}
		double totalBalance = totalIncome - totalExpense;
		
		System.out.println("---Displaying Balance---");
		System.out.println();
		System.out.printf("Total Income: $%.2f%n", totalIncome);
		System.out.printf("Total Expenses: $%.2f%n", totalExpense);
		System.out.printf("Balance: $%.2f%n", totalBalance);
		System.out.println();
	}
	
	public void displayIncomeCategoryTotals() {
		double salary = 0.0;
		double refunds = 0.0;
		double gift = 0.0;
		double other =0.0;
		
		for (Transaction t : transactions) {
			if (t instanceof IncomeTransaction) {
				String category = t.getCategory();
				double amount = t.getAmount();
				
			if (category.equals("Salary")) {
				salary += amount;
			} else if (category.equals("Refunds")) {
				refunds += amount;
			} else if (category.equals("Gift")) {
				gift += amount;
			} else if (category.equals("Other")) {
				other += amount;
			}
		}
	}
		double incomeTotal = salary + refunds + gift + other;
		
		System.out.println("\n---Displaying Income Categories---\n");
		
		if (salary > 0) {
			System.out.printf("Salary: $%.2f%n", salary);
		}
		if (refunds > 0) {
			System.out.printf("Refunds: $%.2f%n", refunds);
		}
		if (gift > 0) {
			System.out.printf("Gift: $%.2f%n", gift);
		}
		if (other > 0) {
			System.out.printf("Other: $%.2f%n", other);
		}
		System.out.println();
		System.out.printf("Total Income: $%.2f%n", incomeTotal);
	}
	
	public void displayExpenseCategoryTotals() {
		double bills = 0.0;
		double subscriptions = 0.0;
		double groceries = 0.0;
		double transportation = 0.0;
		double other = 0.0;
		
		for(Transaction t : transactions) {
			if(t instanceof ExpenseTransaction) {
				String category = t.getCategory();
				double amount = t.getAmount();
				
			if (category.equals("Bills")) {
				bills += amount;
			}
			if (category.equals("Subscriptions")) {
				subscriptions += amount;
			}
			if (category.equals("Groceries")) {
				groceries += amount;
			}
			if (category.equals("Transportation")) {
				transportation += amount;
			}
			if (category.equals("Other")) {
				other += amount;
			}
		}
	}
		
		double expenseTotal = bills + subscriptions + groceries + transportation + other;
		
		System.out.println("\n---Displaying Expenses Categories---\n");
		
		if (bills > 0) {
			System.out.printf("Bills: $%.2f%n", bills);
		}
		if (subscriptions > 0) {
			System.out.printf("Subscriptions: $%.2f%n", subscriptions);
		}
		if (groceries > 0) {
			System.out.printf("Groceries: $%.2f%n", groceries);
		}
		if (transportation > 0) {
			System.out.printf("Transportation: $%.2f%n", transportation);
		}
		if (other > 0) {
			System.out.printf("Other: $%.2f%n", other);
		}
		System.out.println();
		System.out.printf("Total Expenses: $%.2f%n", expenseTotal);
	}
	
	public static ArrayList<Transaction> loadTransactions(String filename) {
		ArrayList<Transaction> fileTransactions = new ArrayList<>();
		Scanner fileScanner = null;
		
		try {
			File file = new File(filename);
			
			if(!file.exists() ) {
				System.out.println("File does not exist.");
				return fileTransactions;
			}
			
			fileScanner = new Scanner(file);
			while (fileScanner.hasNextLine()) {
					String line = fileScanner.nextLine();
					
					if (line.trim().isEmpty()) {
						continue;
					}
					
					String[] parts = line.split(",");
					if (parts.length == 3) {
						String type = parts[0];
						String category = parts[1];
						double amount = Double.parseDouble(parts[2]);
						
						if (type.equals("Income")) {
							fileTransactions.add(new IncomeTransaction(category, amount));
							System.out.println("Read: " + type + " - " + category + ", " + "$" + amount);
						} else if (type.equals("Expense")) {
							fileTransactions.add(new ExpenseTransaction(category, amount));
							System.out.println("Read: " + type + " - " + category + ", " + "$" + amount);
						}
					}
				}
		} catch (FileNotFoundException e) {
			System.out.println("Error: Can't Read File");
		} finally {
			if (fileScanner != null) {
				fileScanner.close();
			}
		}
		return fileTransactions;
	}
	
	public static void appendTransactions(String filename, Transaction userTransaction) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(filename, true)); //Appending
			writer.println(userTransaction.getType() + "," + userTransaction.getCategory() + "," + userTransaction.getAmount());
		}catch (IOException e) {
			System.out.println("Error: Cannot Write on File");
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}

public class PersonalFinanceManager {

	public static void main(String[] args) {
		
		String filename = "transactions.txt";
		ArrayList<Transaction> load = FinancialManager.loadTransactions(filename);
		FinancialManager manager = new FinancialManager(load);
		
		Scanner userinput = new Scanner(System.in);
		int number = 0;

		ArrayList<String> incomeMenu = new ArrayList<>();
		incomeMenu.add("Salary");
		incomeMenu.add("Refunds");
		incomeMenu.add("Gift");
		incomeMenu.add("Other");

		ArrayList<String> expenseMenu = new ArrayList<>();
		expenseMenu.add("Bills");
		expenseMenu.add("Subscriptions");
		expenseMenu.add("Groceries");
		expenseMenu.add("Transportation");
		expenseMenu.add("Other");

		do {
			System.out.println("\nYour Personal Finance Manager");
			System.out.println("\n1. Add Income ");
			System.out.println("2. Add Expenses ");
			System.out.println("3. View Balance ");
			System.out.println("4. View Income Category Totals");
			System.out.println("5. View Expense Category Totals");
			System.out.println("6. Exit");

			System.out.println("\nPlease Select An Option: ");
			
			try {
				number = userinput.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Error: Invalid input. Not a number.");
				userinput.next();
				continue;
			} finally {
				System.out.println("Continuing Program...");
			}

			switch(number) {
			case 1:
				addIncome(manager, incomeMenu, userinput, filename); 
				break;

			case 2:
				addExpenses(manager, expenseMenu, userinput, filename);
				break;

			case 3:
				manager.displaySummary();
				break;
				
			case 4:
				manager.displayIncomeCategoryTotals();
				break;
				
			case 5:
				manager.displayExpenseCategoryTotals();
				break;

			case 6:
				System.out.println("Successfully logged out.");
				break;

			default:
				System.out.println("Invalid choice. Please pick a number from the menu: ");
				break;
			}

		} while (number != 6);
		userinput.close();
	}
	
	public static void setAmount(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount cannot be zero or negative.");
		}
	}

	public static void addIncome(FinancialManager manager, ArrayList<String> menu, Scanner scanner, String filename) {
		double incomeAmount = 0;
		
		while (true) {
		try {
			System.out.println("Enter Income Amount:");
			incomeAmount = scanner.nextDouble();
			setAmount(incomeAmount);
			break;
		} catch (InputMismatchException e) {
			System.out.println("Invalid Input. Not a number.");
			scanner.next();
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}finally {
			System.out.println("Continuing Program...");
		}
		}
		
		System.out.println("\nSelect income type:");
		for (int i = 0; i < menu.size(); i++) {
			System.out.println((i + 1) + ". " + menu.get(i));
		}

		int menuChoice = 0;
		
		while (true) {
			try {
				menuChoice = scanner.nextInt();
				
				if (menuChoice > 0 && menuChoice <= menu.size()) {
					break;
				} else {
					System.out.println("Invalid choice. Please pick a number from the menu: ");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input. Not a number.");
				scanner.next();
			} 
		}

		String incomeCategory = menu.get(menuChoice - 1);
		
		IncomeTransaction income = new IncomeTransaction (incomeCategory, incomeAmount);
		manager.addIncome(income);
		FinancialManager.appendTransactions(filename, income);
		System.out.println();
	}
	

	public static void addExpenses(FinancialManager manager, ArrayList<String> menu,Scanner scanner, String filename) {

		double expenseAmount = 0.0;
		
		while(true) {
			try {
				System.out.println("Enter Expenses Amount:");
				expenseAmount = scanner.nextDouble();
				setAmount(expenseAmount);
				break;
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input. Enter a number.");
				scanner.next();
			} catch (IllegalArgumentException e) {
				System.out.println("Error: " + e.getMessage());
			} finally {
				System.out.println("Continuing Program...");
			}
		}

		System.out.println("\nSelect expense type:");
		for (int i = 0; i < menu.size(); i++) {
			System.out.println((i + 1) + ". " + menu.get(i));
		}

		
		int menuChoice = 0;

		while (true) {
			try {
				menuChoice = scanner.nextInt();
				
				if (menuChoice > 0 && menuChoice <= menu.size()) {
					break;
				} else {
					System.out.println("Invalid choice. Please pick a number from the menu: ");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input. Enter a number.");
				scanner.next();
			}
		}
		String expensesCategory = menu.get(menuChoice - 1);
		
		ExpenseTransaction expenses = new ExpenseTransaction(expensesCategory, expenseAmount);
		manager.addExpense(expenses);
		FinancialManager.appendTransactions(filename, expenses);
		System.out.println();
	}
}
