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

    /*
    public static Object runGetter(Field field, Object o)
    {
        // MZ: Find the correct method
        for (Method method : o.getMethods())
        {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
            {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase()))
                {
                    // MZ: Method found, run it
                    try
                    {
                        return method.invoke(o);
                    }
                    catch (IllegalAccessException e)
                    {
                        LOGGER.("Could not determine method: " + method.getName());
                    }
                    catch (InvocationTargetException e)
                    {
                        Logger.fatal("Could not determine method: " + method.getName());
                    }

                }
            }
        }


        return null;
    }*/
}
