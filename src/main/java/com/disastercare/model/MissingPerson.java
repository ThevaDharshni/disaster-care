package com.disastercare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "missing_persons")
public class MissingPerson {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    @Enumerated(EnumType.STRING) private Gender gender;
    private String bloodGroup;
    private String identificationMark;
    private String lastSeenLocation;
    private String photoPath;
    @Enumerated(EnumType.STRING) private MissingStatus status = MissingStatus.MISSING;
    private LocalDateTime reportedAt = LocalDateTime.now();
    public enum Gender { MALE, FEMALE, OTHER }
    public enum MissingStatus { MISSING, FOUND, INVESTIGATING }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String n){this.name=n;}
    public int getAge(){return age;} public void setAge(int a){this.age=a;}
    public Gender getGender(){return gender;} public void setGender(Gender g){this.gender=g;}
    public String getBloodGroup(){return bloodGroup;} public void setBloodGroup(String b){this.bloodGroup=b;}
    public String getIdentificationMark(){return identificationMark;} public void setIdentificationMark(String i){this.identificationMark=i;}
    public String getLastSeenLocation(){return lastSeenLocation;} public void setLastSeenLocation(String l){this.lastSeenLocation=l;}
    public String getPhotoPath(){return photoPath;} public void setPhotoPath(String p){this.photoPath=p;}
    public MissingStatus getStatus(){return status;} public void setStatus(MissingStatus s){this.status=s;}
    public LocalDateTime getReportedAt(){return reportedAt;} public void setReportedAt(LocalDateTime r){this.reportedAt=r;}
}
