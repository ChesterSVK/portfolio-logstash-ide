package cz.muni.ics.logstash.types;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.logstash.enums.LType;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class LBoolTest {

    @Test
    public void testLBool(){
        LBool bool = new LBool();
        assertThat(bool.toLogstashString(0)).isEqualTo("false");
        assertThat(bool.isInitialised()).isTrue();
        assertThat(bool.getType()).isEqualTo(LType.LBOOL);
        assertThat(bool.toString()).isNotEmpty();
    }

    @Test
    public void testLBoolWithArgument(){
        LBool bool = new LBool(true);
        assertThat(bool.toLogstashString(0)).isEqualTo("true");
        assertThat(bool.isInitialised()).isTrue();
        assertThat(bool.getType()).isEqualTo(LType.LBOOL);
        assertThat(bool.toString()).isNotEmpty();
    }
}
