package treningsdagbok.utils;

import treningsdagbok.TreningsDagbok;
import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class JavaUtils {
    private static final Logger LOGGER = Logger.getLogger(TreningsDagbok.class.getName());

    public static Set<Table> getDataClassAnnotations(Class inspectionClass) {
        Set<Table> annotations = new HashSet<>();
        for (Annotation annotation : inspectionClass.getDeclaredAnnotations()) {
            if (annotation instanceof Table) {
                annotations.add((Table) annotation);
            }
        }
        return annotations;
    }

    public static Map<Field, TableColumn> getDataFieldAnnotations(Class inspectionClass) {
        Map<Field, TableColumn> annotations = new HashMap<>();
        for (Field f : inspectionClass.getDeclaredFields()) {
            TableColumn column = f.getAnnotation(TableColumn.class);
            if (column != null) {
                annotations.put(f, column);
            }
        }
        return annotations;
    }

    /**
     * Formats the string as a snake_case by inserting a underscore
     * before every capitalized character and lower-casing every character.
     * Example:
     * - TreningsMalTilhorlighet => trenings_mal_tilhorlighet
     * - InnendorsTrenings => innendors_trening
     * - antallTilskuere => antall_tilskuere
     *
     * @param string The String that we want to format.
     * @return A snake_case formatted string.
     */
    public static String stringToSnakeCase(String string) {
        // Initialize the new String.
        String snakeCase = "";

        // Loop through all the characters in the String.
        for (int i = 0; i < string.length(); i++) {
            // Is the current character capitalized?
            if (i > 0 && Character.isUpperCase(string.charAt(i))) {
                // Prefix the character with an underscore if so.
                snakeCase += "_";
            }

            // Append the current character to the new String.
            snakeCase += Character.toLowerCase(string.charAt(i));
        }

        // Return the formatted string.
        return snakeCase;
    }

    /**
     * Formats the string as a PascalCase by captializing every character
     * after an underscore. Does the opposite of stringToSnakeCase().
     * Example:
     * - trenings_mal_tilhorlighet => TreningsMalTilhorlighet
     * - innendors_trening => InnendorsTrenings
     * - antall_tilskuere => AntallTilskuere
     *
     * @param string The String that we want to format.
     * @return A PascalCase formatted string.
     */
    public static String stringToPascalCase(String string) {
        // Initialize the new String.
        String pascalCase = "";
        boolean capitalizeCharacter = true;

        // Loop through all the characters in the String.
        for (int i = 0; i < string.length(); i++) {
            // Is the current character an underscore?
            if (string.charAt(i) == '_') {
                // Captialize the next character in the loop.
                capitalizeCharacter = true;
                continue;
            }

            // Append the current character to the new String.
            pascalCase += capitalizeCharacter ? Character.toUpperCase(string.charAt(i)) : string.charAt(i);

            // Reset the boolean.
            capitalizeCharacter = false;
        }

        // Return the formatted string.
        return pascalCase;
    }

    /**
     *
     * @param methodClass
     * @param methodName
     * @return
     * @throws IllegalAccessException
     */
    public static Method getMethodFromClass(Class methodClass, String methodName) throws IllegalAccessException {
        Class initialClass = methodClass;
        while (methodClass != null) {
            Method[] methods = methodClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
            methodClass = methodClass.getSuperclass();
        }
        throw new IllegalAccessException("Could not find method '" + methodName + "' in Class '" + initialClass.getName() + "'");
    }
}
