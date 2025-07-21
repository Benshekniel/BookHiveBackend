package Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.sql.SQLException;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, PGobject> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public PGobject convertToDatabaseColumn(List<String> list) {
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            String json = mapper.writeValueAsString(list);
            System.out.println("Converting to JSON string: " + json);
            pgObject.setValue(json);
            return pgObject;
        } catch (SQLException e) {
            throw new RuntimeException("Could not convert list to JSONB.", e);
        } catch (Exception e) {
            throw new RuntimeException("Could not convert list to JSON string.", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(PGobject pgObject) {
        try {
            if (pgObject == null || pgObject.getValue() == null) {
                return null;
            }
            return mapper.readValue(pgObject.getValue(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Could not convert JSONB to list.", e);
        }
    }
}