package cleanArchitectureExample.infrastructure.outputadapter.postgresrepository;

import cleanArchitectureExample.infrastructure.outputport.EntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostgresRepository implements EntityRepository {

    private final JdbcTemplate jdbcTemplate;

    @SneakyThrows
    @Override
    public <T> T save(T reg) {
        // Get the fields and their values from the entity object
        Field[] entityFields = reg.getClass().getDeclaredFields();
        String[] fields = new String[entityFields.length];
        Object[] fieldValues = new Object[entityFields.length];

        for (int i = 0; i < entityFields.length; i++) {
            fields[i] = entityFields[i].getName();
            fieldValues[i] = entityFields[i].get(reg);
        }

        // Generate SQL INSERT statement dynamically
        String sql = "INSERT INTO " +
                reg.getClass().getSimpleName() +
                "(" + String.join(",", fields) + ")" +
                " VALUES " +
                "(" + String.join(",", Collections.nCopies(fields.length, "?")) + ")";

        // Execute the SQL INSERT statement using JdbcTemplate
        jdbcTemplate.update(sql, fieldValues);

        return reg;
    }

    @Override
    public <T> T getById(String id, Class<T> clazz) {
        // Retrieve an entity object by its ID from the database
        List<T> list = jdbcTemplate.query("SELECT * FROM " + clazz.getSimpleName() + " WHERE id = ?",
                new LombokRowMapper<>(clazz),
                id);

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz) {
        // Retrieve a list of all entities of a given type from the database
        return jdbcTemplate.query("SELECT * FROM " + clazz.getSimpleName(), new LombokRowMapper<>(clazz));
    }

    // RowMapper to map SQL ResultSet to entity objects
    private static class LombokRowMapper<T> implements RowMapper<T> {
        private final Class<?> clazz;

        public LombokRowMapper(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public T mapRow(ResultSet rs, int rowNum) throws SQLException {

            try {
                Method builderMethod = clazz.getMethod("builder");

                Object row = builderMethod.invoke(null);
                Method[] m = row.getClass().getDeclaredMethods();

                for (Method method : m) {
                    int pos = -1;

                    try {
                        pos = rs.findColumn(method.getName());
                    } catch (SQLException ex) {
                    }

                    if (pos != -1) {
                        Object fieldValue = rs.getObject(pos);

                        method.invoke(row, fieldValue);
                    }
                }

                return (T) row.getClass().getMethod("build").invoke(row);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                     | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
