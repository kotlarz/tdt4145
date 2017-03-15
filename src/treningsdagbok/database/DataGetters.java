package treningsdagbok.database;

import treningsdagbok.TreningsDagbok;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.utils.JavaUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DataGetters {
    public static Set<Object> getAll(Class objectClass)
            throws SQLException, DataItemNotFoundException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Set<Object> results = new HashSet<>();
        String tableName = JavaUtils.stringToSnakeCase(objectClass.getSimpleName());
        String query = "SELECT * FROM " + tableName;
        PreparedStatement ps = TreningsDagbok.getDataManager().getConnection().prepareStatement(query);
        ps.execute();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            results.add(DataUtils.getObjectFromResultSet(objectClass, rs));
        }
        ps.close();
        return results;
    }

    public static Set<Object> getByMultiple(String fieldName, Class valueClass, Class objectClass, Object value)
            throws SQLException, DataItemNotFoundException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Set<Object> results = new HashSet<>();
        String tableName = JavaUtils.stringToSnakeCase(objectClass.getSimpleName());
        String formatedFieldName = JavaUtils.stringToSnakeCase(fieldName);
        String query = "SELECT * FROM " + tableName + " WHERE " + formatedFieldName + " = ?";
        PreparedStatement ps = TreningsDagbok.getDataManager().getConnection().prepareStatement(query);

        ps = DataUtils.setParameterValue(ps, valueClass, 1, value);

        ps.execute();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            results.add(DataUtils.getObjectFromResultSet(objectClass, rs));
        }
        ps.close();
        return results;
    }



    public static Object getByTwoFields(String fieldName1, String fieldName2, Class value1Class, Class value2Class,
                                  Class objectClass, Object value1, Object value2) throws SQLException,
            DataItemNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        String tableName = JavaUtils.stringToSnakeCase(objectClass.getSimpleName());
        String formattedFieldName1 = JavaUtils.stringToSnakeCase(fieldName1);
        String formattedFieldName2 = JavaUtils.stringToSnakeCase(fieldName2);
        String query = "SELECT * FROM " + tableName + " WHERE " + formattedFieldName1 + " = ? AND " +
                formattedFieldName2 + " = ?";
        PreparedStatement ps = TreningsDagbok.getDataManager().getConnection().prepareStatement(query);

        ps = DataUtils.setParameterValue(ps, value1Class, 1, value1);
        ps = DataUtils.setParameterValue(ps, value2Class, 2, value2);

        ps.execute();
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            throw new DataItemNotFoundException("Ingen data funnet for spørringen: " + query);
        }
        Object object = DataUtils.getObjectFromResultSet(objectClass, rs);
        ps.close();
        return object;
    }

    public static Object getBy(String fieldName, Class valueClass, Class objectClass, Object value) throws SQLException,
            DataItemNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        String tableName = JavaUtils.stringToSnakeCase(objectClass.getSimpleName());
        String formattedFieldName = JavaUtils.stringToSnakeCase(fieldName);
        String query = "SELECT * FROM " + tableName + " WHERE " + formattedFieldName + " = ?";
        PreparedStatement ps = TreningsDagbok.getDataManager().getConnection().prepareStatement(query);

        ps = DataUtils.setParameterValue(ps, valueClass, 1, value);

        ps.execute();
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            throw new DataItemNotFoundException("Ingen data funnet for spørringen: " + query);
        }
        Object object = DataUtils.getObjectFromResultSet(objectClass, rs);
        ps.close();
        return object;
    }

    public static Object getById(Class objectClass, int id) throws SQLException, DataItemNotFoundException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return getBy("Id", int.class, objectClass, id);
    }

    public static Object getByName(Class objectClass, String name) throws SQLException, DataItemNotFoundException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return getBy("Name", String.class, objectClass, name);
    }
}
