package com.orangeandbronze.enlistment;

import com.orangeandbronze.enlistment.exceptions.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.orangeandbronze.enlistment.Days.*;

public class StudentTest {
    static final Schedule DEFAULT_SCHEDULE = new Schedule(MTH, new Period(830, 1000));
    static final Room DEFAULT_ROOM = new Room("A1", 2);
    static final Subject CCPROG1 = new Subject("CCPROG1", 3, false);
    static final Subject MTH101A = new Subject("MTH101A", 3, false);
    static final Subject CCPROG2 = new Subject("CCPROG2", 3, new HashSet<Subject>() {{add(CCPROG1);}}, false); 
    static final Subject CSARCH2 = new Subject("CSARCH2", 3, false);
    static final Subject LBYARCH = new Subject("LBYARCH", 1, true);
    static final Subject STSWENG = new Subject("STSWENG", 3, false);
    static final Subject LBYPROG = new Subject("LBYPROG", 3, true);
    static final Subject CCICOMP = new Subject("CCICOMP", 3, true);
    static final DegreeProgram DEFAULT_PROGRAM = new DegreeProgram("CCS", new ArrayList<Subject>() {{add(CCPROG1); add(MTH101A); add(CCPROG2); add(CSARCH2); add(LBYARCH); add(STSWENG); add(LBYPROG); add(CCICOMP);}});

    @Test
    public void enlist_2_sections_no_conflict(){
        //Given 1 student & 2 sections w/o conflict
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, new Room("A1", 2), new Subject("CCPROG1", 3));
        Section sec2 = new Section("B", new Schedule(MTH, new Period(1030, 1200)), new Room("A2", 2), new Subject("MTH101A", 3));
        //When student enlists in both sections
        student.enlist(sec1);
        student.enlist(sec2);
        //Then the 2 sections should be found in the student and student should have only 2 sections
        Collection<Section> sections = student.getSections();
        assertAll(
                () -> assertTrue(sections.containsAll(List.of(sec1, sec2))),
                () -> assertEquals(2, sections.size())
        );
    }

    @Test
    public void enlist_2_same_schedule(){
        //Given 1 student & 2 sections w/o conflict
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, new Room("A1", 2), new Subject("CCPROG1", 3));
        Section sec2 = new Section("B", DEFAULT_SCHEDULE, new Room("A2", 2), new Subject("MTH101A", 3));
        //When student enlists in both sections
        student.enlist(sec1);
        //Then the 2 sections should be found in the student and student should have only 2 sections
        assertThrows(ScheduleConflictException.class, () -> student.enlist(sec2));
    }

    @Test
    public void enlist_under_room_capacity(){
        Student student1 = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Student student2 = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, new Room("A1", 2), new Subject("CCPROG1", 3));
        student1.enlist(sec1);
        student2.enlist(sec1);
        assertAll(
                () -> assertTrue(student1.getSections().contains(sec1)),
                () -> assertTrue(student2.getSections().contains(sec1)),
                () -> assertEquals(2, sec1.getNumStudents())
        );
    }

    @Test
    public void enlist_over_room_capacity(){
        Student student1 = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Student student2 = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Student student3 = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, new Room("A1", 2), new Subject("CCPROG1", 3));
        student1.enlist(sec1);
        student2.enlist(sec1);
        assertThrows(CapacityReachedException.class, () -> student3.enlist(sec1));
    }

    @Test
    public void cancel_enlisted_section(){
        //Given student who enlisted in sec1
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, DEFAULT_ROOM, new Subject("CCPROG1", 3));
        student.enlist(sec1);

        //When student cancels sec1
        student.cancelEnlistedSection(sec1);

        //Then sec1 should be removed from students enlisted sections
        assertAll(
                ()-> assertEquals(0, student.getSections().size()),
                ()-> assertEquals(0, sec1.getNumStudents())
        );
    }

    @Test
    public void cancel_non_enlisted_section(){
        //Given student who did not enlist in sec1
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, DEFAULT_ROOM, new Subject("CCPROG1", 3));

        //When student cancels sec1 - found in line 105

        //Then student should not be able to cancel
        assertAll(
                ()-> assertThrows(IllegalArgumentException.class, ()-> student.cancelEnlistedSection(sec1)),
                ()-> assertEquals(0, student.getSections().size()),
                ()-> assertEquals(0, sec1.getNumStudents())
        );
    }

    @Test
    public void enlist_2_sections_different_subjects() {
        //Given 1 student & 2 sections w/ different subject offering
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, new Room("A1", 2), new Subject("CCPROG1", 3));
        Section sec2 = new Section("B", new Schedule(MTH, new Period(1030, 1200)), new Room("A2", 2), new Subject("MTH101A", 3));
        //When student enlists in both sections w/ different subject offering
        student.enlist(sec1);
        student.enlist(sec2);
        //Then the 2 sections should be found in the student and student should have only 2 sections
        Collection<Section> sections = student.getSections();
        assertAll(
                () -> assertTrue(sections.containsAll(List.of(sec1, sec2))),
                () -> assertEquals(2, sections.size())
        );
    }

    @Test
    public void enlist_2_sections_same_subjects(){
        //Given 1 student & 2 sections w/ the same subject
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, new Room("A1", 2), new Subject("CCPROG1", 3));
        Section sec2 = new Section("B", new Schedule(MTH, new Period(1030, 1200)), new Room("A2", 2), new Subject("CCPROG1", 3));
        //When student enlists in both sections w/ the same subject
        student.enlist(sec1);
        //Then throw an exception saying the student cannot enlist in two sections with the same subject
        assertThrows(SameSubjectException.class, () -> student.enlist(sec2));
    }

    @Test
    public void enlist_pre_req_not_met(){
        //Given a section whose subject has a pre-requisite
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, DEFAULT_ROOM, CCPROG2);

        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);

        //When student enlists to the subject without taking the pre-requisite

        //Then throw an exception
        assertThrows(NotTakenPreRequisiteException.class, ()-> student.enlist(sec1));
    }

    @Test
    public void enlist_pre_req_taken(){
        //Given a section whose subject has a pre-requisite
        Subject CCPROG1 = new Subject("CCPROG1", 3);
        HashSet<Subject> CCPROG2PreReq = new HashSet<>();
        CCPROG2PreReq.add(CCPROG1);
        Subject CCPROG2 = new Subject("CCPROG2", 3, CCPROG2PreReq, false);
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, DEFAULT_ROOM, CCPROG2);

        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        student.addTakenSubjects(CCPROG1);

        //When student enlists to the subject having taken the prerequisite
        student.enlist(sec1);

        //Then student successfully
        Collection<Section> sections = student.getSections();
        assertAll(
                () -> assertTrue(sections.contains(sec1)),
                () -> assertEquals(1, sections.size())
        );
    }

    @Test
    public void check_assessment(){
        //Given 1 student who enlists in multiple sections
        Section sec1 = new Section("A", new Schedule(MTH, new Period(1000, 1030)), new Room("A1", 2), new Subject("CCPROG1", 3, false));
        Section sec2 = new Section("B", new Schedule(TF,  new Period(1600, 1630)), new Room("A2", 2), new Subject("CSARCH2", 3, false));
        Section sec3 = new Section("C", new Schedule(WS,  new Period(830, 900)), new Room("A3", 2), new Subject("LBYARCH", 1, true));
        Section sec4 = new Section("D", new Schedule(MTH, new Period(1430, 1500)), new Room("A4", 2), new Subject("STSWENG", 3, false));
        Section sec5 = new Section("E", new Schedule(WS,  new Period(1600,1630)), new Room("A5", 2), new Subject("LBYPROG", 3, true));

        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        student.enlist(sec1);
        student.enlist(sec2);
        student.enlist(sec3);
        student.enlist(sec4);
        student.enlist(sec5);

        //When student requests for assessment
        BigDecimal assessment = student.requestAssessment();

        //Then the correct amount must be returned
        /* Calculation: 13 units * 2000         = 26000
                        2 Laboratory subjects   =  2000
                        Miscellaneous Fees      =  3000 -> Subtotal = 31000
                        VAT                     =  3720
                        Total                   = 34720*/
        BigDecimal expected = (new BigDecimal(34720.00)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        assertEquals(expected, assessment);
    }

    @Test
    public void enlist_overlapping_schedules(){
        //Given a student and two sections of overlapping schedules
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", new Schedule(MTH, new Period(830, 1200)), new Room("A1", 2), new Subject("CCPROG1", 3));
        Section sec2 = new Section("B", new Schedule (MTH, new Period(1030, 1300)), new Room("A2", 2), new Subject("CCICOMP", 3));
        //When a student enlists on both sections
        student.enlist(sec1);

        //Then student will be unable to enlist on the second section
        assertThrows(ScheduleConflictException.class, () -> student.enlist(sec2));
    }

    @Test
    public void enlist_different_days_same_time(){
        //Given a student and two sections of overlapping schedules
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", new Schedule(MTH, new Period(830, 1200)), new Room("A1", 2), new Subject("CCPROG1", 3));
        Section sec2 = new Section("B", new Schedule (WS, new Period(830, 1200)), new Room("A2", 2), new Subject("CCICOMP", 3));
        //When a student enlists on both sections
        student.enlist(sec1);
        student.enlist(sec2);

        //Then student will be unable to enlist on the second section
        Collection<Section> sections = student.getSections();
        assertAll(
                () -> assertTrue(sections.containsAll(List.of(sec1, sec2))),
                () -> assertEquals(2, sections.size())
        );
    }
    @Test
    public void enlist_not_over_max_units (){
        //Given a student and 2 sections where both sections, when enlisted, will not exceed the maximum units allowed
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", new Schedule(MTH, new Period(830, 1200)), new Room("A1", 2), new Subject("CCPROG1", 2));
        Section sec2 = new Section("B", new Schedule (WS, new Period(830, 1200)), new Room("A2", 2), new Subject("CCICOMP", 22));

        //When a student enlists on both sections
        student.enlist(sec1);
        student.enlist(sec2);

        //Student will be able to enlist the second section
        Collection<Section> sections = student.getSections();
        assertAll(
                () -> assertTrue(sections.containsAll(List.of(sec1, sec2))),
                () -> assertEquals(2, sections.size())
        );
    }

    @Test
    public void enlist_over_max_units (){
        //Given a student and 2 sections where both sections, when enlisted, will exceed the maximum units allowed
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", new Schedule(MTH, new Period(830, 1200)), new Room("A1", 2), new Subject("CCPROG1", 22));
        Section sec2 = new Section("B", new Schedule (WS, new Period(830, 1200)), new Room("A2", 2), new Subject("CCICOMP", 3));

        //When a student enlists on both sections
        student.enlist(sec1);

        //Student won't be able to enlist the second section
        assertThrows(MaxUnitsException.class, () -> student.enlist(sec2));
    }

    @Test
    public void same_room_overlapping_schedule(){
        // Given 2 sections with the same room and same schedule
        Section sec1 = new Section("A", new Schedule(MTH, new Period(830, 1200)), new Room("A1", 2), new Subject("CCPROG1", 3));
        Section sec2 = new Section("B", new Schedule(MTH, new Period(830, 1000)), new Room("A1", 2), new Subject("CCICOMP", 3));

        // Check for room conflict and removes room of sec2
        assertThrows(ScheduleConflictException.class, ()->sec1.checkRoomConflict(sec2));
    }

    @Test
    public void enlist_subject_in_program(){
        //Given a student in a specific program and a section whose subject is in the said program
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", new Schedule(MTH, new Period(1000, 1030)), new Room("A1", 2), new Subject("CCPROG1", 3, false));
        //When the student enlists in the said section
        student.enlist(sec1);
        //Then the student should successfully enlist the said section
        Collection<Section> sections = student.getSections();
        assertAll(
                () -> assertTrue(sections.containsAll(List.of(sec1))),
                () -> assertEquals(1, sections.size())
        );
    }

    @Test
    public void enlist_subject_not_in_program(){
        //Given a student in a specific program and a section whose subject si not in the said program
        Student student = new Student(1, Collections.emptyList(), DEFAULT_PROGRAM);
        Section sec1 = new Section("A", new Schedule(MTH, new Period(1000, 1030)), new Room("A1", 2), new Subject("ABCDEFG", 3, false));
        //When the student enlists in the said section
        //Then the student should unsuccessfully enlist the said section
        assertThrows(SubjectNotInProgramException.class, () -> student.enlist(sec1));
    }
}
