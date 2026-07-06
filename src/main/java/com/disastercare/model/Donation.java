package com.disastercare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
public class Donation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String donorName;
    @Enumerated(EnumType.STRING) private ItemType itemType;
    private String quantity;
    private String address;
    private String contactNumber;
    @Enumerated(EnumType.STRING) private DonationStatus status = DonationStatus.PENDING;
    private LocalDateTime donatedAt = LocalDateTime.now();
    public enum ItemType { FOOD, CLOTHES, WATER, MEDICINE, OTHER }
    public enum DonationStatus { PENDING, ACCEPTED, REJECTED }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getDonorName(){return donorName;} public void setDonorName(String d){this.donorName=d;}
    public ItemType getItemType(){return itemType;} public void setItemType(ItemType i){this.itemType=i;}
    public String getQuantity(){return quantity;} public void setQuantity(String q){this.quantity=q;}
    public String getAddress(){return address;} public void setAddress(String a){this.address=a;}
    public String getContactNumber(){return contactNumber;} public void setContactNumber(String c){this.contactNumber=c;}
    public DonationStatus getStatus(){return status;} public void setStatus(DonationStatus s){this.status=s;}
    public LocalDateTime getDonatedAt(){return donatedAt;} public void setDonatedAt(LocalDateTime d){this.donatedAt=d;}
}
