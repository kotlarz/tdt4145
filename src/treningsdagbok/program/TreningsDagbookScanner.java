package treningsdagbok.program;

import treningsdagbok.enums.VaerType;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.models.InnendorsTrening;
import treningsdagbok.models.Ovelse;
import treningsdagbok.models.TreningsOkt;
import treningsdagbok.models.UtendorsTrening;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

public class TreningsDagbookScanner {
    private Scanner scanner;

    public TreningsDagbookScanner() {
        this.scanner = new Scanner(System.in);
    }

    public Scanner getScanner() {
        return scanner;
    }

    /*
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
    }*/

    public void deleteSession() {
        System.out.println("Hvilken økt vil du slette?");
        // TODO: list økter

        int id = scanner.nextInt();
        System.out.println("Er du sikker? (y/n) ");
        while (true) {
            if (scanner.hasNext()) {
                if (scanner.next().equalsIgnoreCase("y")) {
                    try {
                        TreningsOkt treningsOkt = TreningsOkt.getById(id);
                        if (treningsOkt.isUtendors()) {
                            UtendorsTrening utendorsTrening = UtendorsTrening.getById(treningsOkt.getId());
                            utendorsTrening.delete();
                        } else {
                            InnendorsTrening innendorsTrening = InnendorsTrening.getById(treningsOkt.getId());
                            innendorsTrening.delete();
                        }
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        System.out.println("Klarte ikke slette treningsøkten grunnet feil i SQL spørringen, se feilmelding under.");
                        e.printStackTrace();
                    } catch (DataItemNotFoundException e) {
                        System.out.println("Fant ingen treningsøkt med den ID-en");
                        e.printStackTrace();
                    }

                    System.out.println("Treningsøkt med ID #" + id + " ble slettet.");
                } else if (scanner.next().equalsIgnoreCase("n")) {
                    break;
                } else {
                    System.out.println("Skriv y eller n, skjønte ikke hva du mente.");
                }
            }
        }
    }
    
    public void addSession() {
        System.out.println("Legg til treningsøkt:");

        System.out.println("Utendørs (y/n[*]): ");
        boolean isUtendors = scanner.next().equalsIgnoreCase("y");

        LocalDate dato;
        LocalTime tidspunkt;
        int varighet;
        int form;
        int prestasjon;
        String notat;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/yy");

        while (true) {
            System.out.println("Dato (dd/mm/yy): ");

            if (scanner.hasNext()) {
                try {
                    dato = LocalDate.parse(scanner.next(), formatter);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Kjenner ikke til formatet.");
                }
            }
            System.out.println("Prøv igjen");
        }
        
        while (true) {
            System.out.println("Tidspunkt (hh:mm): ");

            if (scanner.hasNextLine()) {
                try {
                    tidspunkt = LocalTime.parse(scanner.nextLine());
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Kjenner ikke til formatet.");
                }
            }
            System.out.println("Prøv igjen");
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
                form = scanner.nextInt();
                break;
            }
            System.out.println("Prøv igjen");            
        }
        
        while (true) {
            System.out.println("Prestasjon: ");
            if (scanner.hasNextInt()) {
                prestasjon = scanner.nextInt();
                break;
            }
            System.out.println("Prøv igjen");
        }
        
        while (true) {
            System.out.println("Notat: ");
            if (scanner.hasNextLine()) {
                notat = scanner.nextLine();
                break;
            }
            System.out.println("Prøv igjen");
        }

        if (isUtendors) {
            float temperatur;
            VaerType vaerType;

            String vaerTypeOptions = "";
            int i = 1;
            for (VaerType vaerTypeOption : VaerType.values()) {
                vaerTypeOptions += vaerTypeOption.name() + (i++ != VaerType.values().length ? "," : "");
            }

            while (true) {
                System.out.println("Temperatur: ");
                if (scanner.hasNextFloat()) {
                    temperatur = scanner.nextFloat();
                    break;
                } else if (scanner.hasNextInt()) {
                    temperatur = (float) scanner.nextInt();
                    break;
                }
                System.out.println("Prøv igjen");
            }

            while (true) {
                System.out.println("Værtype (" + vaerTypeOptions + "): ");
                if (scanner.hasNextLine()) {
                    String inputString = scanner.next();
                    VaerType tempVaerType = VaerType.valueOf(inputString);
                    // TODO: error if invalid vær type
                    System.out.println(tempVaerType);
                    vaerType = tempVaerType;
                    break;
                }
                System.out.println("Prøv igjen");
            }

            UtendorsTrening utendorsTrening = new UtendorsTrening(
                    dato,
                    tidspunkt,
                    varighet,
                    form,
                    prestasjon,
                    notat,
                    temperatur,
                    vaerType
            );

            try {
                utendorsTrening.create();
                System.out.println("Opprettet ny utendørs trenings med ID #" + utendorsTrening.getId());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Klarte ikke opprette treningsøkten grunnet feil i SQL spørringen, se feilmelding under.");
                e.printStackTrace();
            }
        } else {
            float luftkvalitet;
            int antallTilskuere;


            while (true) {
                System.out.println("Luftkvalitet: ");
                if (scanner.hasNextFloat()) {
                    luftkvalitet = scanner.nextFloat();
                    break;
                } else if (scanner.hasNextInt()) {
                    luftkvalitet = (float) scanner.nextInt();
                    break;
                }
                System.out.println("Prøv igjen");
            }

            while (true) {
                System.out.println("Antall tilskuere: ");
                if (scanner.hasNextInt()) {
                    antallTilskuere = scanner.nextInt();
                    break;
                }
                System.out.println("Prøv igjen");
            }

            InnendorsTrening innendorsTrening = new InnendorsTrening(
                    dato,
                    tidspunkt,
                    varighet,
                    form,
                    prestasjon,
                    notat,
                    luftkvalitet,
                    antallTilskuere
            );

            try {
                innendorsTrening.create();
                System.out.println("Opprettet ny innendørs trenings med ID #" + innendorsTrening.getId());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Klarte ikke opprette treningsøkten grunnet feil i SQL spørringen, se feilmelding under.");
                e.printStackTrace();
            }
        }
    }


    public void addExercise() {
        System.out.println("Legg til øvelse:");

        String navn;
        String beskrivelse;

        while (true) {
            System.out.println("Navn: ");
            if (scanner.hasNextLine()) {
                navn = scanner.nextLine();
                break;
            }
            System.out.println("Prøv igjen");
        }
        
        while (true) {
            System.out.println("Beskrivelse: ");
            if (scanner.hasNextLine()) {
                beskrivelse = scanner.nextLine();
                break;
            }
            System.out.println("Prøv igjen");            
        }

        Ovelse ovelse = new Ovelse(navn, beskrivelse);

        try {
            ovelse.create();
            System.out.println("Opprettet ny øvelse med ID #" + ovelse.getId());
        } catch (SQLException e) {
            System.out.println("Klarte ikke opprette øvelsen grunnet feil i SQL spørringen, se feilmelding under.");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // TODO: add results/goals [resultater/mål]
    }

}
