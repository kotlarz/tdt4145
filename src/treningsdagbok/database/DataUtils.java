package treningsdagbok.database;

import treningsdagbok.TreningsDagbok;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.utils.JavaUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;

import static treningsdagbok.utils.JavaUtils.stringToSnakeCase;

public class DataUtils {
    private static final Logger LOGGER = Logger.getLogger(TreningsDagbok.class.getName());

    /**
     * Generates a INSERT SQL query for a PreparedStatement.
     *
     * @param tableClass The Table Class that we want to generate the query for.
     * @return INSER SQL query for a PreparedStatement.
     */
    private static String generateInsertQuery(Class tableClass) {
        // Generate a snake_case string for the table name.
        String tableName = stringToSnakeCase(tableClass.getSimpleName());

        // Initialize the INSERT SQL query.
        String insertQuery = "INSERT INTO `" + tableName + "` (";

        // Initialize the VALUES part of the INSERT query, we append this after finishing the loop.
        String valuesQuery = "";

        // Initialize a index counter.
        int i = 1;

        // Grab the field annotations for the table class.
        Map<Field, TableColumn> fieldAnnotations = JavaUtils.getDataFieldAnnotations(tableClass);

        // Loop through all the field annotations (TableColumn).
        for (Map.Entry<Field, TableColumn> entry : fieldAnnotations.entrySet()) {
            // Get the Field and TableColumn from the Set.
            Field field = entry.getKey();
            TableColumn column = entry.getValue();

            // Generate a snake_case string for the table field.
            String name = stringToSnakeCase(field.getName());

            // Is the TableColumn the last one in the array?
            boolean isLast = i++ != fieldAnnotations.size();

            if (!column.primaryKey()) {
                // Append the name / ? (and a comma if it is not the last TableColumn)
                insertQuery += name + (isLast ? ", " : "");
                valuesQuery += "?" + (isLast ? ", " : "");
            }
        }

        // Append the VALUES part for the query.
        insertQuery += ") VALUES (" + valuesQuery + ")";

        // Return the INSERT query.
        return insertQuery;
    }

    /**
     * Initializes a PreparedStatement INSERT query, sets the parameter values for the Table Class and returns
     * the statement.
     *
     * @param tableClass The Table Class that we want to generate the query for.
     * @param objectInstance The actual object with the data.
     * @return A PreparedStatement with all the parameters initialized.
     * @throws SQLException
     * @throws IllegalAccessException
     */
    public static PreparedStatement generatePrepareStatementInsert(Class tableClass, Object objectInstance)
            throws SQLException, IllegalAccessException {
        // Generate the INSERT SQL query.
        String query = generateInsertQuery(tableClass);

        // Initialize the PreparedStatement with RETURN_GENERATED_KEYS to return the ID of the newly inserted object.
        PreparedStatement ps = TreningsDagbok.getDataManager().getConnection().prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS
        );

        // Parameter index
        int i = 1;

        // Grab the field annotations for the table class.
        Map<Field, TableColumn> fieldAnnotations = JavaUtils.getDataFieldAnnotations(tableClass);

        // Loop through all the field annotations.
        for (Map.Entry<Field, TableColumn> entry : fieldAnnotations.entrySet()) {
            // Get the field.
            Field field = entry.getKey();

            // Set the field to be accessible (i.e. we can grab the value of the field from the object).
            field.setAccessible(true);

            // Get the TableColumn (annotation).
            TableColumn column = entry.getValue();

            if (column.primaryKey()) {
                // You do not insert primary keys, the database handles that.
                continue;
            }

            // Set the data type of the database field (String, int, etc.).
            Class fieldType = field.getType();
            if (!TableColumn.DEFAULT.class.isAssignableFrom(column.dataType())) {
                fieldType = column.dataType();
            }

            // Since the field is accessible we can grab its value.
            Object value = field.get(objectInstance);

            // Set the value for each parameter index.
            if (String.class.isAssignableFrom(fieldType)) {
                ps.setString(i, (String) value);
            } else if (int.class.isAssignableFrom(fieldType)) {
                ps.setInt(i, (int) value);
            } else if (LocalDate.class.isAssignableFrom(fieldType)) {
                ps.setDate(i, java.sql.Date.valueOf((LocalDate) value));
            } else if (LocalTime.class.isAssignableFrom(fieldType)) {
                LocalTime time = (LocalTime) value;
                ps.setTime(i, new Time(time.getHour(), time.getMinute(), time.getSecond()));
            } else if (LocalDateTime.class.isAssignableFrom(fieldType)) {
                ps.setDate(i, java.sql.Date.valueOf((LocalDate) value)); // (LocalDateTime) value
            } else if (float.class.isAssignableFrom(fieldType)) {
                ps.setFloat(i, (int) value);
            } else {
                throw new IllegalArgumentException("Field '" + field.getName() +
                        "' [Class=" + fieldType + "] in Table class '" +
                        tableClass.getSimpleName().toLowerCase() + "' does not have a valid FieldType. " +
                        "It has to be implemented in DataUtils under generatePrepareStatementInsert().");
            }

            // Increase the parameter index.
            i++;
        }

        // Return the PreparedStatement.
        return ps;
    }

    /**
     * Generated a CREATE TABLE SQL query from a Table Class.
     *
     * @param tableClass The Table Class we want to generate a CREATE TABLE SQL query for.
     * @return A CREATE TABLE SQL query for the Table Class.
     */
    public static String generateTableSchema(Class tableClass) {
        // Initialize array that will be used at the end.
        Map<String, String[]> foreignKeys = new HashMap<>();
        List<String> primaryKeys = new ArrayList<>();

        // Generate a snake_case String for the table name.
        String tableName = stringToSnakeCase(tableClass.getSimpleName());

        // Initialize the CREATE TABLE SQL query.
        String tableSchema = "CREATE TABLE `" + tableName + "` (\n";

        // Loop through all the fields in the Table class.
        for (Map.Entry<Field, TableColumn> entry : JavaUtils.getDataFieldAnnotations(tableClass).entrySet()) {
            // Get the Field and TableColumn
            Field field = entry.getKey();
            TableColumn column = entry.getValue();

            // Generate a snake_case String for the table field.
            String name = stringToSnakeCase(field.getName());

            // Set the data type of the database field (String, int, etc.).
            Class fieldType = field.getType();
            if (!TableColumn.DEFAULT.class.isAssignableFrom(column.dataType())) {
                fieldType = column.dataType();
            }

            // Set the length (i.e. INT(6), where 6 is the length) if the TableColumn has it specified.
            String length = "";
            if (column.length() > 0) {
                length = "(" + column.length() + ")";
            }

            // Set the actual SQL data type of the column.
            // Reset the length if the data type field does not support it.
            String type = null;
            if (String.class.isAssignableFrom(fieldType)) {
                if (column.length() == 0) {
                    type = "TEXT";
                } else {
                    type = "VARCHAR";
                }
            } else if (int.class.isAssignableFrom(fieldType)) {
                type = "INT";
            } else if (LocalDate.class.isAssignableFrom(fieldType)) {
                type = "DATE";
                length = "";
            } else if (LocalTime.class.isAssignableFrom(fieldType)) {
                type = "TIME";
                length = "";
            } else if (LocalDateTime.class.isAssignableFrom(fieldType)) {
                type = "DATETIME";
                length = "";
            } else if (float.class.isAssignableFrom(fieldType)) {
                type = "DECIMAL(" + column.precision() + ", " + column.scale() + ")";
                length = "";
            }

            if (type == null) {
                throw new IllegalArgumentException("Field '" + field.getName() +
                        "' [Class=" + fieldType + "] in Table class '" +
                        tableClass.getSimpleName().toLowerCase() + "' does not have a valid FieldType. " +
                        "It has to be implemented in DataUtils under generateTableSchema().");
            }

            // Set the column default if set.
            String fieldDefault = "";
            if (!column.fieldDefault().isEmpty()) {
                fieldDefault = "DEFAULT " + column.fieldDefault() + " ";
            }

            // The column is not nullable by default, set it as nullable if specified.
            String nullable = "NOT NULL";
            if (column.nullable()) {
                nullable = "NULL";
            }

            // Set auto incrementation if specified.
            String autoIncrement = "";
            if (column.autoIncrement()) {
                nullable += " ";
                autoIncrement = "AUTO_INCREMENT";
            }

            // Set the foreign keys temporary.
            if (column.foreignKey().length > 0) {
                foreignKeys.put(name, column.foreignKey());
            }

            // Append the primary keys to an array.
            if (column.primaryKey()) {
                primaryKeys.add(name);
            }

            // Append the current values to the table schema.
            tableSchema += "  `" + name + "` " + type + length + " " + fieldDefault + nullable + autoIncrement + ",\n";
        }

        // Loop through all the Table annotation.
        /*
        for (Table classTable: JavaUtils.getDataClassAnnotations(tableClass)) {
        }*/


        // Loop through the primary keys (if set) and append them to the Table schema.
        if (primaryKeys.size() > 0) {
            tableSchema += "  PRIMARY KEY (";
            int i = 1;
            for (String primaryKey : primaryKeys) {
                tableSchema += "`" + primaryKey + "`";
                if (i++ != primaryKeys.size()) {
                    tableSchema += " ,";
                }
            }
            tableSchema += "),\n";
        }

        // Loop through the foreign keys (if set) and append them to the Table schema.
        for (Map.Entry<String, String[]> entry : foreignKeys.entrySet()) {
            String foreignKey = entry.getKey();
            String[] references = entry.getValue();
            tableSchema += "  FOREIGN KEY (" + foreignKey + ") ";
            tableSchema += "REFERENCES " + references[0] + " ";
            tableSchema += "(" + references[1] + "),\n";
        }

        // End the Table creation query.
        tableSchema += ")";

        // TODO: Add suport for index?

        // Return the CREATE TABLE SQL query.
        return tableSchema;
    }
}
