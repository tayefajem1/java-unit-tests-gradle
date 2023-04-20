package com.orangeandbronze.enlistment;

import com.orangeandbronze.enlistment.exceptions.RoomConflictException;
import com.orangeandbronze.enlistment.exceptions.ScheduleConflictException;
import org.apache.commons.lang3.Validate;

class Schedule {
    private final Days days;
    private final Period period;

    Schedule(Days days, Period period){
        Validate.notNull(days);
        Validate.notNull(period);
        this.days = days;
        this.period = period;
    }

    void checkOverlaps(Schedule other){
        if(this.days.equals(other.days))
            this.period.checkOverlaps(other.period);
    }

    @Override
    public String toString() {
        return days + " " + period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Schedule schedule = (Schedule) o;
        return days == schedule.days && period == schedule.period;
    }

    @Override
    public int hashCode() {
        int result = days != null ? days.hashCode() : 0;
        result = 31 * result + (period != null ? period.hashCode() : 0);
        return result;
    }
}

enum Days{
    MTH, TF, WS
}

class Period{
    private final int startTime;
    private final int endTime;

    Period(int startTime, int endTime){
        Validate.isTrue((startTime%100 == 30 || startTime%100 == 0) && (endTime%100 == 30 || endTime%100 == 0), "Time has to start at 00 or 30");
        Validate.isTrue(startTime >= 830 && startTime <= 1730 && endTime >= 830 && endTime <= 1730, "Time has to be from 0830 and 1730");
        Validate.isTrue(endTime > startTime, "End time has to be later than start time.");

        this.startTime = startTime;
        this.endTime = endTime;
    }

    void checkOverlaps(Period period){
        if(!(this.startTime >= period.endTime || period.startTime >= this.endTime))
            throw new ScheduleConflictException("Overlapping schedule");
    }

    void checkRoomOverlaps(Period period, Section other){
        if(!(this.startTime >= period.endTime || period.startTime >= this.endTime)){
            other.removeRoom();
            throw new RoomConflictException("Same room and schedule.");
        }
    }

}
