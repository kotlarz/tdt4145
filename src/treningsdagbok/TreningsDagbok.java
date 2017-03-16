package treningsdagbok;

import treningsdagbok.database.DataManager;
import treningsdagbok.database.DataUtils;
import treningsdagbok.models.*;
import treningsdagbok.program.TreningsDagbookScanner;

import java.sql.*;
import java.util.logging.Logger;

public class TreningsDagbok {
    private static final Logger LOGGER = Logger.getLogger(TreningsDagbok.class.getName());
    private static DataManager dataManager;
    private static TreningsDagbookScanner scanner = new TreningsDagbookScanner();

    public static void main(String[] args) {
        // TODO: connect to DataManager
        dataManager = new DataManager("localhost", "treningsdagbok");
        dataManager.connect("treningsdagbok", "treningsdagbok");
        try {
            dropDatabase();
            createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        startScanner();
    }

    private static void startScanner() {
        while (true) {
            System.out.println("Hva vil du?");
            System.out.println("1: Legg til økt.");
            System.out.println("2: Slett økt.");
            System.out.println("3: Legg til øvelse.");
            System.out.println("4: Beste øvelser siste 3 måneder.");

            if (scanner.getScanner().hasNextInt()) {
                int i = scanner.getScanner().nextInt();
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
                        //scanner.printBestSession();
                        break;
                    default:
                        System.out.printf("Kjenner ikke operasjonen");
                }
            } else {
                throw new IllegalArgumentException("Niks.");
            }
        }
    }

    public static DataManager getDataManager() {
        return dataManager;
    }

    private static void createDatabase() throws SQLException {
        Connection connection = getDataManager().getConnection();
        connection.setAutoCommit(false);

        String[] tableSchemas = {
            DataUtils.generateTableSchema(TreningsOkt.class),
            DataUtils.generateTableSchema(UtendorsTrening.class),
            DataUtils.generateTableSchema(InnendorsTrening.class),
            DataUtils.generateTableSchema(TreningsData.class),
            DataUtils.generateTableSchema(Ovelse.class),
            DataUtils.generateTableSchema(OvelseTilhorlighet.class),
            DataUtils.generateTableSchema(OvelseResultat.class),
            DataUtils.generateTableSchema(OvelseMaal.class),
            DataUtils.generateTableSchema(OvelseKategori.class),
            DataUtils.generateTableSchema(OvelseKategoriTilhorlighet.class),
            DataUtils.generateTableSchema(TreningsMal.class),
            DataUtils.generateTableSchema(TreningsMalTilhorlighet.class)
        };
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
