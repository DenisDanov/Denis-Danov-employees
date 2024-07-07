package bg.duosoft.sirmatask.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class CustomDateDeserializer extends JsonDeserializer<Date> {

    private static final List<SimpleDateFormat> dateFormats = Arrays.asList(
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("yyyy/MM/dd"),
            new SimpleDateFormat("yyyy.MM.dd"),
            new SimpleDateFormat("dd-MM-yyyy"),
            new SimpleDateFormat("dd/MM/yyyy"),
            new SimpleDateFormat("dd.MM.yyyy"),
            new SimpleDateFormat("MM-dd-yyyy"),
            new SimpleDateFormat("MM/dd/yyyy"),
            new SimpleDateFormat("MM.dd.yyyy")
    );

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String date = jsonParser.getText();

        if (date == null || date.trim().isEmpty() || date.equalsIgnoreCase("null")) {
            return new Date();
        }

        for (SimpleDateFormat dateFormat : dateFormats) {
            try {
                return dateFormat.parse(date);
            } catch (ParseException e) {
                // Continue to the next date format
            }
        }

        throw new IOException("Unable to parse date: " + date);
    }
}
