package com.orangeandbronze.enlistment;

import com.orangeandbronze.enlistment.exceptions.CapacityReachedException;
import com.orangeandbronze.enlistment.exceptions.NotTakenPreRequisiteException;
import com.orangeandbronze.enlistment.exceptions.SameSubjectException;
import com.orangeandbronze.enlistment.exceptions.SubjectNotInProgramException;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

public class Section {

    private final String sectionID;
    private final Schedule schedule;
    private Room room;
    private final Subject subject;
    private int numStudents = 0;


    Section(String sectionID, Schedule schedule, Room room, Subject subject){
        notBlank(sectionID);
        notNull(schedule);
        notNull(room);
        notNull(subject);
        isTrue(isAlphanumeric(sectionID), "sectionID must be alphanumeric, was: " + sectionID);
        this.sectionID = sectionID;
        this.schedule = schedule;
        this.room = room;
        this.subject = subject;
    }

    void checkScheduleConflict(Section other){
        this.schedule.checkOverlaps(other.schedule);
    }

    void checkRoomConflict(Section other){
        if (this.room.equals(other.room)){
            checkScheduleConflict(other);
        }
    }

    void checkSectionRoomCapacity(){
        if (this.room.isFull(numStudents)){
            throw new CapacityReachedException("section " + this.sectionID + " at " + this.room + " is already full");
        }
    }

    void checkForDuplicateSubjects(Section other){
        if (this.subject.equals(other.subject)){
            throw new SameSubjectException("this section " + this + " has same subject as other section " + other + " with subject " + subject);
        }
    }

    void checkTakenPrerequisite(Collection<Subject> takenSubjects){
        if(!takenSubjects.containsAll(getSubject().getPrerequisites())){
            throw new NotTakenPreRequisiteException("You have not yet taken the pre requisite subject");
        }
    }

    void removeRoom(){
        this.room = null;
    }

    void addStudent(){
        numStudents++;
    }

    void removeStudent(){
       numStudents--;
    }

    int getNumStudents() {
        return numStudents;
    }

    void checkSubjectInProgram(DegreeProgram program){
        if (!program.containsSubject(subject)) {
            throw new SubjectNotInProgramException("This subject " + subject + " is not in your program " + program);
        }
    }

    Subject getSubject(){
        return subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Section section = (Section) o;
        return Objects.equals(sectionID, section.sectionID);
    }

    @Override
    public int hashCode() {
        return sectionID != null ? sectionID.hashCode() : 0;
    }

    @Override
    public String toString() {
        return sectionID;
    }
}
