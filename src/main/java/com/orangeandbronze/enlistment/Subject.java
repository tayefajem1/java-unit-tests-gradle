package com.orangeandbronze.enlistment;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isAlphanumeric;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

public class Subject {
    private final String subjectID;

    private final int units;

    private final Collection<Subject> prerequisites  = new HashSet<>();

    private final boolean isLab;

    Subject(String subjectID, int units){
        this(subjectID,units, new ArrayList<>(), false);
    }

    Subject(String subjectID, int units, boolean isLab){
        this(subjectID,units, new ArrayList<>(),isLab);
    }

    Subject(String subjectID, int units, Collection<Subject> prerequisites, boolean isLab){
        notBlank(subjectID);
        isTrue(isAlphanumeric(subjectID), "subjectID must be alphanumeric, was: " + subjectID);
        this.subjectID = subjectID;
        this.units = units;
        this.prerequisites.addAll(prerequisites);
        this.isLab = isLab;
    }
    
    Collection<Subject> getPrerequisites(){
        return new ArrayList<>(this.prerequisites);
    }

    int getUnits() {
        return units;
    }

    boolean isLab() {
        return isLab;
    }

    @Override
    public String toString() {
        return "subjectID: " +  subjectID;
    }

    @Override
    public int hashCode() {
        return subjectID != null ? subjectID.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(subjectID, subject.subjectID);
    }
}
