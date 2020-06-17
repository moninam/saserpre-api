package com.saserpe.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class Province {
    private Integer Id;

    @Column(name="shortname")
    @JsonProperty("short_name")
    private String short_name;

    private String Name;

    public Province(){

    }
    public Province(Integer id, String shortName, String name) {
        Id = id;
        short_name = shortName;
        Name = name;

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    @Column(name="shortname")
    public String getShortName() {
        return short_name;
    }

    @Column(name="shortname")
    public void setShortName(String shortName) {
        short_name = shortName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
