package treningsdagbok.program;

import java.util.Scanner;

public class AccountScanner {
    private Scanner scanner;
    private Account account;

    public AccountScanner() {
        scanner = new Scanner(System.in);
        account = new Account(0, 0);
    }

    public void deposit() {
        System.out.println("Sett inn penger: ");
            String line = scanner.nextLine();
            if (scanner.hasNextInt())
                account.deposit(scanner.nextDouble());
            else
                throw new IllegalArgumentException("Niks.");
    }

    public void withdraw() {
        System.out.println("Ta ut penger: ");
            String line = scanner.nextLine();
            if (scanner.hasNextInt())
                account.withdraw(scanner.nextDouble());
            else
                throw new IllegalArgumentException("Niks.");
    }


    public void printBalance() {
        System.out.println("Din balanse: " + account.getBalance());
    }


    public void welcomeUser() {
        System.out.println("Skriv inn en startverdi for din bankkonto: ");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line == null || line.length() == 0)
                break;
        }
    }
    public static void main(String args[]) {
        AccountScanner scanner = new AccountScanner();
        while (true) {
            System.out.println("Hva vil du?");
            System.out.println("1: ta ut.");
            System.out.println("2: sette inn.");
            System.out.println("3: se balanse.");
            if (scanner.scanner.hasNextInt()) {
                int i = scanner.scanner.nextInt();
                switch (i) {
                    case 1:
                        scanner.withdraw();
                        scanner.printBalance();
                        break;
                    case 2:
                        scanner.deposit();
                        scanner.printBalance();
                        break;
                    case 3:
                        scanner.printBalance();
                        break;
                    default:
                        System.out.printf("Kjenner ikke operasjonen");
                }
            } else
                throw new IllegalArgumentException("Niks.");
        }
    }
}
