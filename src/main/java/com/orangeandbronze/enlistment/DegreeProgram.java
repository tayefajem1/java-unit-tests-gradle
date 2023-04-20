package com.orangeandbronze.enlistment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;

class DegreeProgram {
    private final Collection<Subject> subjects  = new HashSet<>();
    private final String programName;
    DegreeProgram(String programName, Collection<Subject> subjects){
        notBlank(programName);
        if (subjects == null){
            throw new NullPointerException();
        }
        this.programName = programName;
        this.subjects.addAll(subjects);
    }

    boolean containsSubject(Subject subject){
        return this.subjects.contains(subject);
    }

    @Override
    public String toString() {
        return programName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DegreeProgram)) return false;

        DegreeProgram degreeProgram = (DegreeProgram) o;
        if (!subjects.equals(degreeProgram.subjects)) return false;
        return Objects.equals(programName, degreeProgram.programName);
    }

    @Override
    public int hashCode() {
        int result = subjects.hashCode();
        result = 31 * result + (programName != null ? programName.hashCode() : 0);
        return result;
    }
}
