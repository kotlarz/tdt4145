package treningsdagbok;

import treningsdagbok.database.DataManager;
import treningsdagbok.database.DataUtils;
import treningsdagbok.models.*;
import treningsdagbok.program.TreningsDagbookScanner;

import java.sql.*;
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
        // TODO: connect to DataManager
        dataManager = new DataManager("localhost", "treningsdagbok");
        dataManager.connect("treningsdagbok", "treningsdagbok");
        try {
            createDatbaseIfNotExists();
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
}
