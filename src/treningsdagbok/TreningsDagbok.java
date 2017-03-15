package treningsdagbok;

import treningsdagbok.database.DataManager;
import treningsdagbok.database.DataUtils;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.models.*;
import treningsdagbok.utils.JavaUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.logging.Logger;

public class TreningsDagbok {
    private static final Logger LOGGER = Logger.getLogger(TreningsDagbok.class.getName());
    private static DataManager dataManager;

    public static void main(String[] args) {
        // TODO: connect to DataManager
        dataManager = new DataManager("localhost", "treningsdagbok");
        dataManager.connect("treningsdagbok", "treningsdagbok");
        LOGGER.info("Hello world!");
        try {
            dropDatabase();
            createDatabase();
            insertTestData();
            //startReader();
            TreningsOkt treningsOkt = TreningsOkt.getById(1);
            System.out.println(treningsOkt.getDato());
        } catch (SQLException | DataItemNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static void startReader() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter a number: ");
        int n = reader.nextInt();
        LOGGER.info("lol: " + n);
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
            LOGGER.info(tableSchema);
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
        InnendorsTrening treningsOkt = new InnendorsTrening(
                LocalDate.now(),
                LocalTime.now(),
                3600,
                6,
                8,
                "Eksempel notat",
                "BRA",
                105
        );
        try {
            treningsOkt.create();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
