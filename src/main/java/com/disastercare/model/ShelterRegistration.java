package com.disastercare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shelter_registrations")
public class ShelterRegistration {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    @ManyToOne @JoinColumn(name = "shelter_id")
    private Shelter shelter;
    private String name;
    private int age;
    @Enumerated(EnumType.STRING) private Gender gender;
    private String bloodGroup;
    private String identificationMark;
    private LocalDateTime registeredAt = LocalDateTime.now();
    public enum Gender { MALE, FEMALE, OTHER }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getUserId(){return userId;} public void setUserId(String u){this.userId=u;}
    public Shelter getShelter(){return shelter;} public void setShelter(Shelter s){this.shelter=s;}
    public String getName(){return name;} public void setName(String n){this.name=n;}
    public int getAge(){return age;} public void setAge(int a){this.age=a;}
    public Gender getGender(){return gender;} public void setGender(Gender g){this.gender=g;}
    public String getBloodGroup(){return bloodGroup;} public void setBloodGroup(String b){this.bloodGroup=b;}
    public String getIdentificationMark(){return identificationMark;} public void setIdentificationMark(String i){this.identificationMark=i;}
    public LocalDateTime getRegisteredAt(){return registeredAt;} public void setRegisteredAt(LocalDateTime r){this.registeredAt=r;}
}
