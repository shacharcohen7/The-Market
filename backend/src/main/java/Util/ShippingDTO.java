package Util;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "shipping_dto")
public class ShippingDTO {
    @Id
    @Column(name = "shipping_id")
    private String shipping_id;
    @Column(name = "memberId", nullable = false)
    private String memberId;
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "zip")
    private String zip;
    @Column(name = "date", nullable = false)
    private Date date;
    @Column(name = "acquisitionId", nullable = false)
    private String acquisitionId;
    @Column(name = "transactionId", nullable = false)
    private int transactionId;



    // Constructor
    public ShippingDTO(String shipping_id , int transactionId , String memberId, String country, String city,
                       String address , String acquisitionId ) {
        this.shipping_id = shipping_id;
        this.memberId = memberId;
        this.country = country;
        this.city = city;
        this.address = address;
        this.date = new Date(); // Current date and time
        this.acquisitionId = acquisitionId;
        this.transactionId = transactionId;
    }

    // No-argument constructor required by JPA
    public ShippingDTO() {
    }

    public String getShipping_id() {
        return shipping_id;
    }



    // Getters
    public String getMemberId() {
        return memberId;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
    }

    public Date getDate() {
        return date;
    }

    public String getAcquisitionId() {
        return acquisitionId;
    }

    public void setAcquisitionId(String acquisitionId) {
        this.acquisitionId = acquisitionId;
    }

    public int getTransactionId() {
        return transactionId;
    }
}
