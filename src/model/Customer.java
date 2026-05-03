package model;

import java.util.UUID;

public class Customer {

    private String customerId;
    private String name;
    private String phone;
    private String licenseNo; // ehliyet numarası

    // Yeni müşteri oluştururken
    public Customer(String name, String phone, String licenseNo) {
        this.customerId = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.licenseNo = licenseNo;
    }

    // Dosyadan okurken
    public Customer(String customerId, String name, String phone, String licenseNo) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.licenseNo = licenseNo;
    }

    // Dosyaya şu formatta yazar:
    // id,name,phone,licenseNo
    public String toFileString() {
        return customerId + "," + name + "," + phone + "," + licenseNo;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getLicenseNo() { return licenseNo; }

    @Override
    public String toString() {
        return name + " [" + phone + "]";
    }
}