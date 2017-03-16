package treningsdagbok.program;

import treningsdagbok.enums.VaerType;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.interfaces.DataTable;
import treningsdagbok.interfaces.DataTableWithId;
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
import java.util.*;

public class TreningsDagbookScanner {
    public static final String SEPERATOR = "---------------------------------";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
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


    private Ovelse pickExercise() {
        Set<Ovelse> ovelser = new HashSet<>();
        Map<Integer, Ovelse> ovelserWithKeys = new HashMap<>();

        try {
            ovelser = Ovelse.getAll();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | SQLException
                | InvocationTargetException | DataItemNotFoundException e) {
            System.out.println("Klarte ikke hente øvelser: " + e.getMessage());
            e.printStackTrace();
        }

        if (ovelser.isEmpty()) {
            System.out.println("Fant ingen øvelser, du må legge til noen.");
            return null;
        }

        for (Ovelse ovelse : ovelser) {
            System.out.println("#" + ovelse.getId() + " - " + ovelse.getNavn());
            ovelserWithKeys.put(ovelse.getId(), ovelse);
        }

        while (true) {
            Integer tempId = readData("Velg en øvelse", Integer.class);
            if (ovelserWithKeys.containsKey(tempId)) {
                return ovelserWithKeys.get(tempId);
            }
            System.out.println("Denne ID-en finnes ikke, prøv igjen.");
        }
    }

    public void addExercise() {
        System.out.println("Legg til øvelse:");

        String navn = readData("Navn", String.class);
        String beskrivelse = readData("Beskrivelse", String.class);

        Ovelse ovelse = new Ovelse(navn, beskrivelse);
        createObject(ovelse);
    }

    public void deleteExercise() {
        // TODO;
        Ovelse ovelse = pickExercise();
        if (ovelse != null) {

        }
    }
    
    public void addSession() {
        System.out.println("Legg til treningsøkt:");

        boolean isUtendors = getYesNoInput("Utendørs");

        LocalDate dato = readData("Dato (dd/mm/yy)", LocalDate.class);
        LocalTime tidspunkt = readData("Tidspunkt (hh:mm)", LocalTime.class);
        int varighet = readData("Varighet (minutter)", int.class) * 60;
        int form = readData("Form (1-10)", int.class);
        int prestasjon = readData("Prestasjon (1-10)", int.class);
        String notat = readData("Notat", String.class);

        if (isUtendors) {
            float temperatur = readData("Temperatur", float.class);
            VaerType vaerType = readData("Værtype (" + VaerType.getOptions() + ")", VaerType.class);

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

            createObject(utendorsTrening);
        } else {
            float luftkvalitet = readData("Luftkvalitet", float.class);
            int antallTilskuere = readData("Antall tilskuere: ", int.class);

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
            createObject(innendorsTrening);
        }

        // TODO: add results
    }

    public void deleteSession() {
        System.out.println("Hvilken økt vil du slette?");
        // TODO: list økter

        int id = scanner.nextInt();

        // Check if exsists

        if (getYesNoInput("Er du sikker?")) {
            try {
                TreningsOkt treningsOkt = TreningsOkt.getById(id);

                // TODO: delete results
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Klarte ikke slette treningsøkten grunnet feil i SQL spørringen, se feilmelding under.");
                e.printStackTrace();
            } catch (DataItemNotFoundException e) {
                System.out.println("Fant ingen treningsøkt med den ID-en");
                e.printStackTrace();
            }
        }
    }

    private boolean getYesNoInput(String text) {
        System.out.println(text + " (y/n): ");
        while (true) {
            if (scanner.hasNext()) {
                if (scanner.next().equalsIgnoreCase("y")) {
                    return true;
                } else if (scanner.next().equalsIgnoreCase("n")) {
                    return false;
                } else {
                    System.out.println("Skriv y eller n, skjønte ikke hva du mente.");
                }
            }
        }
    }

    private <T> T readData(String text, Class<T> dataClass) {
        while (true) {
            System.out.println(SEPERATOR);
            System.out.println(text + ": ");
            if (String.class.isAssignableFrom(dataClass)) {
                if (scanner.hasNextLine()) {
                    String next = scanner.nextLine();
                    if (!next.isEmpty()) {
                        return dataClass.cast(next);
                    }
                }
            } else if (Integer.class.isAssignableFrom(dataClass)) {
                if (scanner.hasNextInt()) {
                    return dataClass.cast(scanner.nextInt());
                }
            } else if (float.class.isAssignableFrom(dataClass)) {
                if (scanner.hasNext() && scanner.hasNextFloat()) {
                    return dataClass.cast(scanner.nextFloat());
                }
            } else if (LocalDate.class.isAssignableFrom(dataClass)) {
                if (scanner.hasNext()) {
                    try {
                        return dataClass.cast(LocalDate.parse(scanner.next(), dateFormatter));
                    } catch (DateTimeParseException e) {
                        System.out.println("Kjenner ikke til formatet.");
                    }
                }
            } else if (LocalTime.class.isAssignableFrom(dataClass)) {
                if (scanner.hasNext()) {
                    try {
                        return dataClass.cast(LocalTime.parse(scanner.next(), timeFormatter));
                    } catch (DateTimeParseException e) {
                        System.out.println("Kjenner ikke til formatet.");
                    }
                }
            } else if (VaerType.class.isAssignableFrom(dataClass)) {
                if (scanner.hasNext()) {
                    String inputString = scanner.next();
                    try {
                        return dataClass.cast(VaerType.valueOf(inputString));
                    } catch (Exception ignore) {
                    }
                }
            }
            System.out.println("Prøv igjen");
        }
    }

    private void createObject(DataTable object) {
        boolean created = false;
        while (!created) {
            try {
                object.create();
                if (object instanceof DataTableWithId) {
                    System.out.println("Opprettet et nytt objekt med ID #" + ((DataTableWithId) object).getId());
                } else {
                    System.out.println("Opprettet et nytt objekt i databasen.");
                }
                created = true;
            } catch (SQLException e) {
                System.out.println("Klarte ikke opprette objektet grunnet feil i SQL spørringen, se feilmelding under.");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
