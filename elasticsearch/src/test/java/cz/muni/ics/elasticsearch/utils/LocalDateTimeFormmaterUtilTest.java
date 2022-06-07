package cz.muni.ics.elasticsearch.utils;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class LocalDateTimeFormmaterUtilTest {

    @Test
    public void testGet(){
        System.out.println(LocalDateTimeFormatterUtil.getNormalizedDate(LocalDateTime.now()));
        assertThat(LocalDateTimeFormatterUtil.getNormalizedDate(LocalDateTime.now())).isNotEmpty();
    }

    @Test
    public void testParse(){
        assertThat(LocalDateTimeFormatterUtil.parseNormalizedDate("2018-11-07-14-40-01-771")).isNotNull();
    }

    @TestWith({
            "2018-11-07-14-40-01",
            "2018-11-07-14-40-AA-771",
            "2018-11-07-1440-01-771",
            "FFFF-11-07-14-40-01-771",
            "2018-00-07-14-40-01-771",
            "1.1.111",
            "771-01-40-14-01-10-2018"
    })
    public void testInvalidFormatParse(String date){
        assertThatThrownBy( () -> LocalDateTimeFormatterUtil.parseNormalizedDate(date)).isInstanceOf(DateTimeParseException.class);
    }

}
