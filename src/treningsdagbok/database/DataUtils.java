package treningsdagbok.database;

import treningsdagbok.TreningsDagbok;
import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.utils.JavaUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;

public class DataUtils {
    private static final Logger LOGGER = Logger.getLogger(TreningsDagbok.class.getName());

    private static String formatDatabaseName(String name) {
        String newName = "";
        for (int i = 0; i < name.length(); i++) {
            if (i > 0 && Character.isUpperCase(name.charAt(i))) {
                newName += "_";
            }
            newName += Character.toLowerCase(name.charAt(i));
        }
        return newName;
    }

    public static String generateInsertQuery(Class tableClass) {
        String tableName = formatDatabaseName(tableClass.getSimpleName());
        String insertQuery = "INSERT INTO `" + tableName + "` (";
        String valuesQuery = "";
        int i = 1;
        Map<Field, TableColumn> fieldAnnotations = JavaUtils.getDataFieldAnnotations(tableClass);
        for (Map.Entry<Field, TableColumn> entry : fieldAnnotations.entrySet()) {
            Field field = entry.getKey();
            TableColumn column = entry.getValue();
            String name = formatDatabaseName(field.getName());
            boolean isLast = i++ != fieldAnnotations.size();
            if (!column.primaryKey()) {
                insertQuery += name + (isLast ? ", " : "");
                valuesQuery += "?" + (isLast ? ", " : "");
            }
        }

        insertQuery += ") VALUES (" + valuesQuery + ")";
        return insertQuery;
    }

    public static PreparedStatement generatePrepareStatementInsert(Object objectInstance)
            throws SQLException, IllegalAccessException {
        Class tableClass = objectInstance.getClass();
        String query = generateInsertQuery(tableClass);
        LOGGER.info(query);
        PreparedStatement ps = TreningsDagbok.getDataManager().getConnection().prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS
        );
        int i = 1;
        Map<Field, TableColumn> fieldAnnotations = JavaUtils.getDataFieldAnnotations(tableClass);
        for (Map.Entry<Field, TableColumn> entry : fieldAnnotations.entrySet()) {
            Field field = entry.getKey();
            field.setAccessible(true);
            TableColumn column = entry.getValue();
            String name = formatDatabaseName(field.getName());
            Class fieldType = column.dataType();

            Object value = field.get(objectInstance);

            System.exit(1);

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
            i += 1;
        }
        return ps;
    }

    public static String generateTableSchema(Class tableClass) {
        Map<String, String[]> foreignKeys = new HashMap<>();
        List<String> primaryKeys = new ArrayList<>();
        String tableName = formatDatabaseName(tableClass.getSimpleName());
        String tableSchema = "CREATE TABLE `" + tableName + "` (\n";
        for (Map.Entry<Field, TableColumn> entry : JavaUtils.getDataFieldAnnotations(tableClass).entrySet()) {
            Field field = entry.getKey();
            TableColumn column = entry.getValue();

            // NAME
            String name = formatDatabaseName(field.getName());

            // TYPE
            Class fieldType = field.getType();
            if (!TableColumn.DEFAULT.class.isAssignableFrom(column.dataType())) {
                fieldType = column.dataType();
            }

            // LENGTH
            String length = "";
            if (column.length() > 0) {
                length = "(" + column.length() + ")";
            }

            // TYPE
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
            } else if (LocalTime.class.isAssignableFrom(fieldType)) {
                type = "TIME";
            } else if (LocalDateTime.class.isAssignableFrom(fieldType)) {
                type = "DATETIME";
            } else if (float.class.isAssignableFrom(fieldType)) {
                type = "DECIMAL(" + column.precision() + ", " + column.scale() + ")";
            }

            if (type == null) {
                throw new IllegalArgumentException("Field '" + field.getName() +
                        "' [Class=" + fieldType + "] in Table class '" +
                        tableClass.getSimpleName().toLowerCase() + "' does not have a valid FieldType. " +
                        "It has to be implemented in DataUtils under generateTableSchema().");
            }

            // FIELD DEFAULT
            String fieldDefault = "";
            if (!column.fieldDefault().isEmpty()) {
                fieldDefault = "DEFAULT " + column.fieldDefault() + " ";
            }

            // NULLABLE
            String nullable = "NOT NULL";
            if (column.nullable()) {
                nullable = "NULL";
            }

            // AUTO INCREMENT
            String autoIncrement = "";
            if (column.autoIncrement()) {
                nullable += " ";
                autoIncrement = "AUTO_INCREMENT";
            }

            // FOREGIN KEY
            if (column.foreignKey().length > 0) {
                foreignKeys.put(name, column.foreignKey());
            }

            // PRIMARY KEY
            if (column.primaryKey()) {
                primaryKeys.add(name);
            }

            // APPEND TO SCHEMA
            tableSchema += "  `" + name + "` " + type + length + " " + fieldDefault + nullable + autoIncrement + ",\n";
        }

        for (Table classTable: JavaUtils.getDataClassAnnotations(tableClass)) {
            // PRIMARY KEYS
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

            // FOREIGN KEY
            for (Map.Entry<String, String[]> entry : foreignKeys.entrySet()) {
                String foreignKey = entry.getKey();
                String[] references = entry.getValue();
                tableSchema += "  FOREIGN KEY (" + foreignKey + ") ";
                tableSchema += "REFERENCES " + references[0] + " ";
                tableSchema += "(" + references[1] + "),\n";
            }

            // TODO
        }

        tableSchema += ")";
        return tableSchema;
    }
}
