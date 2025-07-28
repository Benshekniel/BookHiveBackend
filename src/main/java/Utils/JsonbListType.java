package Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Objects;

public class JsonbListType implements UserType<List<String>> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public int getSqlType() {
        return Types.OTHER; // Use Types.OTHER for PostgreSQL jsonb
    }

    @Override
    public Class<List<String>> returnedClass() {
        return (Class<List<String>>) (Class<?>) List.class;
    }

    @Override
    public boolean equals(List<String> x, List<String> y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(List<String> x) throws HibernateException {
        return Objects.hashCode(x);
    }

    @Override
    public List<String> nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        PGobject pgObject = (PGobject) rs.getObject(position);
        if (pgObject == null || pgObject.getValue() == null) {
            return null;
        }
        try {
            return mapper.readValue(pgObject.getValue(), new TypeReference<List<String>>() {});
        } catch (IOException e) {
            throw new SQLException("Could not convert JSONB to List<String>", e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, List<String> value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            try {
                PGobject pgObject = new PGobject();
                pgObject.setType("jsonb");
                pgObject.setValue(mapper.writeValueAsString(value));
                st.setObject(index, pgObject);
            } catch (IOException e) {
                throw new SQLException("Could not convert List<String> to JSONB", e);
            }
        }
    }

    @Override
    public List<String> deepCopy(List<String> value) throws HibernateException {
        return value == null ? null : List.copyOf(value); // Immutable copy
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(List<String> value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public List<String> assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy((List<String>) cached);
    }

    @Override
    public List<String> replace(List<String> original, List<String> target, Object owner) throws HibernateException {
        return deepCopy(original);
    }
}