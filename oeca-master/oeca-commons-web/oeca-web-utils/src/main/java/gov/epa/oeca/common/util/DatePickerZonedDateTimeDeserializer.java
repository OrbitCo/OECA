package gov.epa.oeca.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;

/**
 * @author dfladung
 */
public class DatePickerZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

    private static DateTimeFormatter datePickerFormat = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('/')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral('/')
            .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).toFormatter();


    public DatePickerZonedDateTimeDeserializer() {
        this(null);
    }

    public DatePickerZonedDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser jp, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        String dateString = jp.getText();
        LocalDate localDate = LocalDate.parse(dateString, datePickerFormat);
        return ZonedDateTime.of(localDate.atStartOfDay(), ZoneId.systemDefault());
    }
}
