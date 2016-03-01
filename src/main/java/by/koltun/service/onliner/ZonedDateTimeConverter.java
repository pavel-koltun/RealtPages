package by.koltun.service.onliner;

import by.koltun.domain.util.JSR310PersistenceConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * ZonedDateTimeConverter implementation
 */
@Service
public class ZonedDateTimeConverter {

    @Autowired
    JSR310PersistenceConverters.ZonedDateTimeConverter converter;

    @Bean
    public JSR310PersistenceConverters.ZonedDateTimeConverter converter() {

        return new JSR310PersistenceConverters.ZonedDateTimeConverter();
    }

    public ZonedDateTime toZonedDateTime(Date date) {

        return converter.convertToEntityAttribute(date);
    }

    public Date toDate(ZonedDateTime date) {

        return converter.convertToDatabaseColumn(date);
    }
}
