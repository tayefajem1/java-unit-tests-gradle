package com.orangeandbronze.enlistment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.Test;

public class PeriodTest {

    @Test
    public void check_valid_periods(){
        //Given 4 valid periods
        //When they are initialized
        //No exception is thrown
        assertDoesNotThrow(()->{
            Period period1 = new Period(830,900);
            Period period2 = new Period(900, 1200);
            Period period3 = new Period(1430, 1630);
            Period period4 = new Period(900, 1030);
        });
    }

    @Test
    public void check_invalid_periods(){
        assertThrows(IllegalArgumentException.class, ()->{
            //Given 4 periods that are invalid: invalid start time, invalid end time, valid increment, invalid period
            Period period1 = new Period(815, 1030);
            Period period2 = new Period(1000, 1930);
            Period period3 = new Period (1230, 1250);
            Period period4 = new Period (1230, 1215);
        });
    }
}
