package com.invoicesystem.model;

import com.invoicesystem.enums.ProductCategory;
import com.invoicesystem.enums.TaxType;

import java.io.Serializable;

/**
 * Digital product that is delivered electronically
 */
public class DigitalProduct extends Product implements Serializable {
    private String downloadLink;
    private String licenseKey;
    private boolean isSubscription;

    public DigitalProduct(String id, String name, int stockQuantity, int reorderLevel,
                          String supplierInfo, double basePrice, ProductCategory category,
                          TaxType taxType, double seasonalDiscount, String downloadLink,
                          String licenseKey, boolean isSubscription) {
        super(id, name, stockQuantity, reorderLevel, supplierInfo, basePrice, category,
                taxType, seasonalDiscount);
        this.downloadLink = downloadLink;
        this.licenseKey = licenseKey;
        this.isSubscription = isSubscription;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public boolean isSubscription() {
        return isSubscription;
    }

    public void setSubscription(boolean subscription) {
        isSubscription = subscription;
    }

    @Override
    public String getProductType() {
        return "Digital";
    }

    @Override
    public String toString() {
        return "DigitalProduct{" +
                "downloadLink='" + downloadLink + '\'' +
                ", licenseKey='" + licenseKey + '\'' +
                ", isSubscription=" + isSubscription +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", reorderLevel=" + reorderLevel +
                ", supplierInfo='" + supplierInfo + '\'' +
                ", basePrice=" + basePrice +
                ", category=" + category +
                ", taxType=" + taxType +
                ", seasonalDiscount=" + seasonalDiscount +
                ", lastRestockDate=" + lastRestockDate +
                '}';
    }
}