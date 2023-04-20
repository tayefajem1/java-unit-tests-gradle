package com.orangeandbronze.enlistment;

import com.orangeandbronze.enlistment.exceptions.MaxUnitsException;
import org.apache.commons.lang3.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.apache.commons.lang3.Validate.notNull;

class Student {
    private final int studentNumber;
    private final Collection<Section> sections = new HashSet<>();
    private final Collection<Subject> takenSubjects = new HashSet<>();
    private final DegreeProgram program;
    private final int maxUnits = 24;

    Student(int studentNumber, Collection<Section> sections, DegreeProgram program){

        Validate.isTrue(studentNumber>0, "studentNumber should be non-negative, was: " + studentNumber);
        notNull(sections);
        notNull(program);

        this.studentNumber = studentNumber;
        this.sections.addAll(sections);
        this.sections.removeIf(Objects::isNull);
        this.program = program;
    }

    Student(int studentNumber){
        this(studentNumber, Collections.emptyList(), new DegreeProgram("", Collections.emptyList()));
    }

    void enlist(Section newSection){
        notNull(newSection);
        sections.forEach(currSection -> currSection.checkScheduleConflict(newSection));
        newSection.checkSectionRoomCapacity();
        sections.forEach(currSection -> currSection.checkForDuplicateSubjects(newSection));
        newSection.checkTakenPrerequisite(takenSubjects);
        checkTotalUnits(newSection);
        newSection.checkSubjectInProgram(program);
        this.sections.add(newSection);
        newSection.addStudent();
    }

    void cancelEnlistedSection(Section section){
        Validate.notNull(section);
        Validate.isTrue(sections.contains(section), "Student not currently enlisted on the section");
        section.removeStudent();
        sections.remove(section);
    }

    void checkTotalUnits (Section newSection){
        if (getTotalUnits() + newSection.getSubject().getUnits() > maxUnits) {
            throw new MaxUnitsException("You have exceeded the maximum units allowed.");
        }
    }

    BigDecimal requestAssessment(){
        BigDecimal assessment = new BigDecimal(0);
        if(!sections.isEmpty()){
            for(Section enlistedSection: sections){
                assessment = assessment.add(new BigDecimal(2000 * enlistedSection.getSubject().getUnits()));
                if(enlistedSection.getSubject().isLab()){
                    assessment = assessment.add(new BigDecimal(1000));
                }
            }
            assessment = assessment.add(new BigDecimal(3000));
            assessment = assessment.multiply(new BigDecimal("1.12"));
        }
        assessment = assessment.setScale(2, RoundingMode.HALF_EVEN);
        return assessment;
    }

    Collection<Section> getSections(){
        return new ArrayList<>(sections);
    }

    Collection<Subject> getTakenSubjects(){
        return takenSubjects;
    }

    int getTotalUnits (){
        int totalUnits = 0;
        for (Section enlistedSection: sections) {
            totalUnits += enlistedSection.getSubject().getUnits();
        }
        return totalUnits;
    }

    void addTakenSubjects(Subject subject){
        takenSubjects.add(subject);
    }

    @Override
    public String toString() {
        return "Student# " + studentNumber;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Student student = (Student) o;
        return studentNumber == student.studentNumber;
    }

    @Override
    public int hashCode(){
        return studentNumber;
    }
}
