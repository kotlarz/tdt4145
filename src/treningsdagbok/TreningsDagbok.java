package treningsdagbok;

import treningsdagbok.database.DataManager;
import treningsdagbok.database.DataUtils;
import treningsdagbok.enums.Belastning;
import treningsdagbok.enums.VaerType;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.models.*;
import treningsdagbok.program.TreningsDagbookScanner;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static treningsdagbok.utils.JavaUtils.stringToSnakeCase;

public class TreningsDagbok {
    private static final Logger LOGGER = Logger.getLogger(TreningsDagbok.class.getName());
    private static DataManager dataManager;
    private static TreningsDagbookScanner scanner = new TreningsDagbookScanner();
    private static List<Class> tableClasses = Arrays.asList(
            TreningsOkt.class,
            InnendorsTrening.class,
            UtendorsTrening.class,
            TreningsData.class,
            Ovelse.class,
            OvelseTilhorlighet.class,
            OvelseResultat.class,
            OvelseMaal.class,
            OvelseKategori.class,
            OvelseKategoriTilhorlighet.class,
            TreningsMal.class,
            TreningsMalTilhorlighet.class
    );

    public static void main(String[] args) {
        dataManager = new DataManager("localhost", "treningsdagbok");
        dataManager.connect("treningsdagbok", "treningsdagbok");
        try {
            //dropDatabase();
            createDatbaseIfNotExists();
            //insertTestData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        startScanner();
    }

    private static void startScanner() {
        while (true) {
            System.out.println("Hva vil du?");
            System.out.println("1: Opprett øvelse.");
            System.out.println("2: Slett øvelse.");
            System.out.println("3: Legg til økt.");
            System.out.println("4: Legg til resultat.");
            System.out.println("------");
            System.out.println("6: Drop database.");
            System.out.println("7: Avslutt.");
            System.out.println(TreningsDagbookScanner.SEPERATOR);

            if (scanner.getScanner().hasNextInt()) {
                int i = scanner.getScanner().nextInt();
                scanner.getScanner().nextLine(); // There's a left-over newline, consume it.
                switch (i) {
                    case 1:
                        scanner.createExercise();
                        break;
                    case 2:
                        scanner.deleteExercise();
                        break;
                    case 3:
                        scanner.addSession();
                        break;
                    case 4:
                        scanner.addResults();
                        break;
                    case 6:
                        boolean delete = scanner.getYesNoInput("Er du sikker at du vile slette HELE databasen?");
                        if (delete) {
                            try {
                                dropDatabase();
                                System.out.println("Databasen har blitt slettet, hadebra.");
                            } catch (SQLException e) {
                                System.out.println("Klarte ikke slette databasen, se feilmeldingen under.");
                                e.printStackTrace();
                            }
                            System.exit(0);
                        } else {
                            System.out.println("Avbrøt sletting av databasen.");
                        }
                        break;
                    case 7:
                        System.exit(0);
                    default:
                        System.out.println("Kjenner ikke operasjonen, prøv igjen.");
                }
            } else {
                throw new IllegalArgumentException("Niks.");
            }
            System.out.println(TreningsDagbookScanner.SEPERATOR);
        }
    }

    public static DataManager getDataManager() {
        return dataManager;
    }

    private static void createDatbaseIfNotExists() throws SQLException {
        List<String> tableNames = new ArrayList<>();
        for (Class tableClass : tableClasses) {
            tableNames.add(stringToSnakeCase(tableClass.getSimpleName()));
        }
        if (!dataManager.tablesExists(tableNames)) {
            createDatabase();
        }
    }

    private static void createDatabase() throws SQLException {
        Connection connection = getDataManager().getConnection();
        connection.setAutoCommit(false);

        List<String> tableSchemas = new ArrayList<>();

        for (Class tableClass : tableClasses) {
            tableSchemas.add(DataUtils.generateTableSchema(tableClass));

        }

        for (String tableSchema : tableSchemas) {
            LOGGER.fine(tableSchema);
            Statement stmt = connection.createStatement();
            stmt.execute(tableSchema);
            stmt.close();
        }

        connection.commit();
        connection.setAutoCommit(true);
    }

    private static void dropDatabase() throws SQLException {
        PreparedStatement ps = getDataManager().getConnection().prepareStatement("DROP ALL OBJECTS");
        ps.executeUpdate();
        ps.close();
    }

    private static void insertTestData() {
        try {
            // Øvelser
            Ovelse fotballOvelse = new Ovelse(
                    "Fotball",
                    "Kjempe fin beskrivelse"
            );
            fotballOvelse.create();

            Ovelse lopeOvelse = new Ovelse(
                    "Løping",
                    "Løpe øvelse"
            );
            lopeOvelse.create();

            Ovelse hangupsOvelse = new Ovelse(
                    "Hangups",
                    "Veldig tung øvelse"
            );
            hangupsOvelse.create();

            // Utendørs treninger
            UtendorsTrening utendorsTrening1 = new UtendorsTrening(
                    LocalDate.now(),
                    LocalTime.now(),
                    30,
                    9,
                    6,
                    "Bra form på treningen, dårlig prestasjon, var fint vær.",
                    (float) 24.3,
                    VaerType.SOL
            );
            utendorsTrening1.create();
            utendorsTrening1.addOvelse(fotballOvelse);
            OvelseResultat ovelseResultat1 = new OvelseResultat(
                    utendorsTrening1,
                    fotballOvelse,
                    Belastning.MIDDELS,
                    1,
                    1
            );
            ovelseResultat1.create();
            utendorsTrening1.addOvelseResultat(fotballOvelse, ovelseResultat1);

            UtendorsTrening utendorsTrening2 = new UtendorsTrening(
                    LocalDate.now(),
                    LocalTime.now(),
                    45,
                    3,
                    8,
                    "Dårlig form, men god prestasjon, det regnet.",
                    (float) 22.3,
                    VaerType.REGN
            );
            utendorsTrening2.create();
            utendorsTrening2.addOvelse(fotballOvelse);
            OvelseResultat ovelseResultat2 = new OvelseResultat(
                    utendorsTrening2,
                    lopeOvelse,
                    Belastning.HØY,
                    1,
                    3
            );
            ovelseResultat2.create();
            utendorsTrening2.addOvelseResultat(lopeOvelse, ovelseResultat1);

            // Innendørs treninger
            InnendorsTrening innendorsTrening1 = new InnendorsTrening(
                    LocalDate.now(),
                    LocalTime.now(),
                    30,
                    9,
                    6,
                    "Bra form på treningen, dårlig prestasjon.",
                    (float) 89.4,
                    40
            );
            innendorsTrening1.create();
            innendorsTrening1.addOvelse(lopeOvelse);
            OvelseResultat ovelseResultat3 = new OvelseResultat(
                    innendorsTrening1,
                    lopeOvelse,
                    Belastning.MIDDELS,
                    1,
                    3
            );
            ovelseResultat3.create();
            innendorsTrening1.addOvelseResultat(lopeOvelse, ovelseResultat3);
            innendorsTrening1.addOvelse(fotballOvelse);
            OvelseResultat ovelseResultat4 = new OvelseResultat(
                    innendorsTrening1,
                    fotballOvelse,
                    Belastning.HØY,
                    1,
                    1
            );
            ovelseResultat4.create();
            innendorsTrening1.addOvelseResultat(fotballOvelse, ovelseResultat4);

            InnendorsTrening innendorsTrening2 = new InnendorsTrening(
                    LocalDate.now(),
                    LocalTime.now(),
                    45,
                    3,
                    8,
                    "Dårlig form, men god prestasjon, det regnet.",
                    (float) 89.4,
                    600
            );
            innendorsTrening2.create();
            innendorsTrening2.addOvelse(hangupsOvelse);
            OvelseResultat ovelseResultat5 = new OvelseResultat(
                    innendorsTrening2,
                    hangupsOvelse,
                    Belastning.HØY,
                    10,
                    3
            );
            ovelseResultat5.create();
            innendorsTrening2.addOvelseResultat(hangupsOvelse, ovelseResultat5);
        } catch (Exception e) {
            System.out.println("Klarte ikke sette inn test data:");
            e.printStackTrace();
        }

    }
}
