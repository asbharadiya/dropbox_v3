package com.demo.demo.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
public class Asset {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String filePath;

    private String fileName;

    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    @Fetch(FetchMode.SELECT)
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
