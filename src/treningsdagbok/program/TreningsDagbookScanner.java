package treningsdagbok.program;

import treningsdagbok.enums.Belastning;
import treningsdagbok.enums.VaerType;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.interfaces.DataTable;
import treningsdagbok.interfaces.DataTableWithId;
import treningsdagbok.models.*;

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

    private Ovelse pickExercise() {
        return pickExercise(null);
    }

    private Ovelse pickExercise(Set<Ovelse> ovelser) {
        Map<Integer, Ovelse> ovelserWithKeys = new HashMap<>();
        if (ovelser == null) {
            ovelser = new HashSet<>();
            try {
                ovelser = Ovelse.getAll();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | SQLException
                    | InvocationTargetException | DataItemNotFoundException e) {
                System.out.println("Klarte ikke hente øvelser: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (ovelser.isEmpty()) {
            System.out.println("Fant ingen øvelser, du må legge til noen.");
            return null;
        }

        for (Ovelse ovelse : ovelser) {
            System.out.println("#" + ovelse.getId() + " => " + ovelse);
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

    public void createExercise() {
        System.out.println("Opprett en øvelse:");

        String navn = readData("Navn", String.class);
        String beskrivelse = readData("Beskrivelse", String.class);

        Ovelse ovelse = new Ovelse(navn, beskrivelse);
        createObject(ovelse);
    }

    public void deleteExercise() {
        System.out.println("Hvilken øvelse vil du slette?");
        Ovelse ovelse = pickExercise();
        if (ovelse != null) {
            if (getYesNoInput("Er du sikker?")) {
                deleteObject(ovelse);
            } else {
                System.out.println("Avbrytet sletting");
            }
        }
    }

    private TreningsOkt pickSession() {
        return pickSession(null);
    }

    private TreningsOkt pickSession(Set<TreningsOkt> treningsOkter) {
        Map<Integer, TreningsOkt> treningsOkterWithKeys = new HashMap<>();

        if (treningsOkter == null) {
            treningsOkter = new HashSet<>();

            try {
                treningsOkter = TreningsOkt.getAll();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | SQLException
                    | InvocationTargetException | DataItemNotFoundException e) {
                System.out.println("Klarte ikke hente trenings økter: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (treningsOkter.isEmpty()) {
            System.out.println("Fant ingen trenings økter, du må legge til noen.");
            return null;
        }

        for (TreningsOkt treningsOkt : treningsOkter) {
            System.out.println("#" + treningsOkt.getId() + " => " + treningsOkt);
            treningsOkterWithKeys.put(treningsOkt.getId(), treningsOkt);
        }

        while (true) {
            Integer tempId = readData("Velg en treningsøkt", Integer.class);
            if (treningsOkterWithKeys.containsKey(tempId)) {
                return treningsOkterWithKeys.get(tempId);
            }
            System.out.println("Denne ID-en finnes ikke, prøv igjen.");
        }
    }
    
    public void addSession() {
        Set<Ovelse> ovelser = new HashSet<>();
        try {
            ovelser = Ovelse.getAll();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | SQLException
                | InvocationTargetException | DataItemNotFoundException e) {
            System.out.println("Klarte ikke hente øvelser: " + e.getMessage());
            e.printStackTrace();
        }
        if (ovelser.isEmpty()) {
            System.out.println("Du kan ikke legge til en Treningsøkt før du har opprettet noen øvelser.");
            return;
        }

        System.out.println("Legg til treningsøkt:");

        LocalDate dato = readData("Dato (dd/mm/yy)", LocalDate.class);
        LocalTime tidspunkt = readData("Tidspunkt (hh:mm)", LocalTime.class);
        Integer varighet = readData("Varighet (minutter)", Integer.class) * 60;

        Integer form;
        while (true) {
            form = readData("Form (1-10)", Integer.class);
            if (form >= 1 && form <= 10) {
                break;
            }
            System.out.println("Form må være mellom 1 og 10.");
        }

        Integer prestasjon;
        while (true) {
            prestasjon = readData("Prestasjon (1-10)", Integer.class);
            if (prestasjon >= 1 && prestasjon <= 10) {
                break;
            }
            System.out.println("Prestasjon må være mellom 1 og 10.");
        }

        scanner.nextLine(); // There's a left-over newline, consume it.
        String notat = readData("Notat", String.class);

        TreningsOkt treningsOkt;

        if (getYesNoInput("Utendørs")) {
            Float temperatur = readData("Temperatur", Float.class);
            VaerType vaerType = readData("Værtype " + VaerType.getOptions(), VaerType.class);

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
            treningsOkt = utendorsTrening;
        } else {
            Float luftkvalitet = readData("Luftkvalitet", Float.class);
            Integer antallTilskuere = readData("Antall tilskuere: ", Integer.class);

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
            treningsOkt = innendorsTrening;
        }

        System.out.println("Legg til øvelser i økten:");
        while (true) {
            Ovelse ovelse = pickExercise(ovelser);
            if (ovelse == null) {
                System.out.println("Noe feil skjedde når vi valgte øvelse");
            }
            try {
                treningsOkt.addOvelse(ovelse);
                ovelser.remove(ovelse);
            } catch (NoSuchMethodException | IllegalAccessException | SQLException
                    | InstantiationException | DataItemNotFoundException | InvocationTargetException e) {
                System.out.println("Feil når vi prøvde å legge til øvelse:");
                e.printStackTrace();
            }
            if (ovelser.isEmpty() || !getYesNoInput("Vil du legge til flere øvelser")) {
                System.out.println("Ferdig med å legge til øvelser");
                break;
            }
        }
    }

    public void deleteSession() {
        System.out.println("Hvilken økt vil du slette?");
        TreningsOkt treningsOkt = pickSession();
        if (treningsOkt != null) {
            if (getYesNoInput("Er du sikker?")) {
                deleteObject(treningsOkt);
            } else {
                System.out.println("Avbrytet sletting");
            }
        }
    }

    public void addResults() {
        // TreningsØkt

        Set<TreningsOkt> treningsOkter = new HashSet<>();
        try {
            treningsOkter = TreningsOkt.getAll();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | SQLException
                | InvocationTargetException | DataItemNotFoundException e) {
            System.out.println("Klarte ikke hente trenings økter: " + e.getMessage());
            e.printStackTrace();
        }

        if (treningsOkter.isEmpty()) {
            System.out.println("Fant ingen trenings økter, du må legge til noen før du kan legge til resultater.");
            return;
        }

        TreningsOkt treningsOkt = pickSession(treningsOkter);
        if (treningsOkt == null) {
            System.out.println("TreningsØkten var null, det skal ikke være mulig...");
            return;
        }

        // Øvelse

        Set<Ovelse> ovelser = new HashSet<>();
        try {
            ovelser = treningsOkt.getOvelser();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | SQLException
                | InvocationTargetException | DataItemNotFoundException e) {
            System.out.println("Klarte ikke hente øvelser: " + e.getMessage());
            e.printStackTrace();
        }

        if (ovelser.isEmpty()) {
            System.out.println("Fant ingen øvelser under den valgte treningsøkten, du må legge til noen før du kan legge til resultater.");
            return;
        }

        System.out.println("Hvilken øvelse vil du legge inn et resultat for?");
        Ovelse ovelse = pickExercise();
        if (ovelse == null) {
            System.out.println("Øvelsen var null, det skal ikke være mulig...");
            return;
        }

        // Resultat
        Belastning belastning = readData("Belastning " + Belastning.getOptions(), Belastning.class);
        Integer antallRepetisjoner = readData("Antall Repetisjoner", Integer.class);
        Integer antallSett = readData("Antall sett", Integer.class);

        OvelseResultat resultat = new OvelseResultat(
                treningsOkt,
                ovelse,
                belastning,
                antallRepetisjoner,
                antallSett
        );

        createObject(resultat);
        System.out.println("La til resultat i databasen");
        /*
        try {
            treningsOkt.addOvelseResultat(ovelse, resultat);
            System.out.println("La til resultat i databasen");
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                | DataItemNotFoundException | SQLException | InvocationTargetException e) {
            System.out.println("Klarte ikke å legge til Øvelse resultat til treningsøkten:");
            e.printStackTrace();
        }*/
    }

    public boolean getYesNoInput(String text) {
        System.out.println(text + " (y/n): ");
        while (true) {
            if (scanner.hasNext()) {
                String answer = scanner.next();
                if (answer.equalsIgnoreCase("y")) {
                    return true;
                } else if (answer.equalsIgnoreCase("n")) {
                    return false;
                } else {
                    System.out.println("Skriv y eller n, skjønte ikke hva du mente.");
                }
            }
        }
    }

    public <T> T readData(String text, Class<T> dataClass) {
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
            } else if (Float.class.isAssignableFrom(dataClass)) {
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
            } else if (Belastning.class.isAssignableFrom(dataClass)) {
                if (scanner.hasNext()) {
                    String inputString = scanner.next();
                    try {
                        return dataClass.cast(Belastning.valueOf(inputString));
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

    private void deleteObject(DataTable object) {
        boolean deleted = false;
        while (!deleted) {
            try {
                object.delete();
                if (object instanceof DataTableWithId) {
                    System.out.println("Slettet objekt med ID #" + ((DataTableWithId) object).getId() + " fra databasen.");
                } else {
                    System.out.println("Slettet objekt fra databasen.");
                }
                deleted = true;
            } catch (SQLException e) {
                System.out.println("Klarte ikke slette objektet grunnet feil i SQL spørringen, se feilmelding under.");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
