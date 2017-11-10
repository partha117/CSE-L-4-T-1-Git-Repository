package com.company.helper;

public class Slot {

    private String teacher;
    private String room;
    private String subject;

    public Slot(String teacher, String room, String subject) {
        this.teacher = teacher;
        this.room = room;
        this.subject = subject;
    }

    public Slot() {
    }



    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSubject() {
        return subject;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object obj) {
        Slot temp=(Slot)obj;
        boolean c=teacher.equals(temp.teacher)&&room.equals(temp.room)&&(subject.equals(temp.subject));
        return c;
    }

    @Override
    public String toString() {
        return "("+"C"+subject+" : "+"T"+teacher+" : "+"R"+room+")";
    }
}
