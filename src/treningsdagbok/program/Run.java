package treningsdagbok.program;

import java.util.Scanner;

public class DaybookScanner {
    private Scanner scanner;
    private TreningsDagbok bjartes_daybook;

    public DaybookScanner() {
        scanner = new Scanner(System.in);
        account = new TreningsDagbok();
    }

    public void printSession(int sessionId) {
        TreningsOkt session = TreningsOkt.getTreningsOktById(sessionId);
        System.out.println("Dato: " + session.getDato());
        System.out.println("Tidspunkt: " + session.getTidspunkt());
        System.out.println("Varighet: " + session.getVarighet());
        System.out.println("Form: " + session.getForm());
        System.out.println("Prestasjon: " + session.getPrestasjon());
        System.out.println("Notat: " + session.getNotat());
    }

    public void printBestSession() {
        System.out.println("Din beste treningsøkt de siste 3 månedene:");
        // Økten med lengst varighet
        int sessionId = TreningsOkt.getBesteOvingSiste3Mnd();
        printSession(sessionId);
    }

    public void deleteSession() {
        System.out.println("Hvilken økt vil du slette?");
        // TODO: list økter

        int id = scanner.getInt();
        System.out.println("Er du sikker? (y/n) ");
        while (true) {
            if (scanner.hasNext()) {
                if (scanner.next() == 'y') {
                    String deleteName = TreningsOkt.getTreningsOktNavnById(id);
                    TreningsOkt.deleteOktById(id);
                    System.out.println("Treningsøkt " + slettName + " ble slettet.");
                } else if (scanner.next() == 'n')
                    return null;
                else
                    System.out.println("Skriv y eller n, skjønte ikke hva du mente.");
        }
    }
    
    public void addSession() {
        System.out.println("Legg til treningsøkt:");

        while (true) {
            System.out.println("Dato (dd/mm/yy): ");
            try {
                LocalDate date LocalDate.parse(scanner.nextLine());
                break;
            } catch (DateTimeFormatter)
                System.out.println("Kjenner ikke formatet.");
        }
        
        while (true) {
            System.out.println("Tidspunkt (hh:mm): ");
            try {
                LocalTime time LocalTime.parse(scanner.nextLine());
                break;
            } catch (DateTimeFormatter)
                System.out.println("Kjenner ikke formatet.");
        }
        
        while (true) {
            System.out.println("Varighet: ");
            if (scanner.hasNextInt()) {
                varighet = scanner.nextInt();
                break;
            }
            System.out.println("Prøv igjen");
        }
        
        while (true) {
            System.out.println("Form: ");
            if (scanner.hasNextInt()) {
                varighet = scanner.nextInt();
                break;
            }
            System.out.println("Prøv igjen");            
        }
        
        while (true) {
            System.out.println("Prestasjon: ");
            if (scanner.hasNextInt()) {
                varighet = scanner.nextInt();
                break;
            }
            System.out.println("Prøv igjen");
        }
        
        while (true) {
            System.out.println("Notat: ");
            if (scanner.hasNextLine()) {
                varighet = scanner.nextLine();
                break;
            }
            System.out.println("Prøv igjen");
        }
    }

    public void print() {
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
        DaybookScanner scanner = new DaybookScanner();
        while (true) {
            System.out.println("Hva vil du?");
            System.out.println("1: Legg til økt.");
            System.out.println("2: Slett økt.");
            System.out.println("3: Legg til øvelse.");
            System.out.println("4: Beste øvelser siste 3 måneder.");
            
            if (scanner.scanner.hasNextInt()) {
                int i = scanner.scanner.nextInt();
                switch (i) {
                    case 1:
                        scanner.addSession();
                        break;
                        
                    case 2:
                        scanner.deleteSession();
                        break;
                        
                    case 3:
                        scanner.addExercise();
                        break;
                        
                    case 4:
                        scanner.printBestSession();
                        break;

                    default:
                        System.out.printf("Kjenner ikke operasjonen");
                        
                }
            } else
                throw new IllegalArgumentException("Niks.");
        }
    }

}
