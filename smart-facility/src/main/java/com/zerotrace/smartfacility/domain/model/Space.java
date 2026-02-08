package com.zerotrace.smartfacility.domain.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "spaces")
public class Space extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String location;

    private Integer capacity;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "space_features", joinColumns = @JoinColumn(name = "space_id"))
    @Column(name = "feature")
    private Set<String> features;

    @Column(nullable = false)
    private boolean active;

    public Space() {
    }

    public Space(String name, String location, Integer capacity, Set<String> features, boolean active) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.features = features;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
