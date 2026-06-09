package com.lolmyeon.lol.participation.entity;

public enum ParticipationType {
       NORMAL("내전"),
       FREE("자유내전"),
       FLEX("자유랭크"),
       ARAM("증바람");

       private final String label;

           ParticipationType(String label){
           this.label = label;
       }

       public String getLabel(){
           return label;
       }
}
