package com.disastercare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "district_status")
public class DistrictStatus {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String districtName;
    @Enumerated(EnumType.STRING) private DisasterLevel status = DisasterLevel.SAFE;
    private String description;
    private LocalDateTime updatedAt = LocalDateTime.now();
    public enum DisasterLevel { SAFE, WARNING, DISASTER }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getDistrictName(){return districtName;} public void setDistrictName(String d){this.districtName=d;}
    public DisasterLevel getStatus(){return status;} public void setStatus(DisasterLevel s){this.status=s;}
    public String getDescription(){return description;} public void setDescription(String d){this.description=d;}
    public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime u){this.updatedAt=u;}
}
