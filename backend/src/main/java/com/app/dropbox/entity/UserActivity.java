package com.app.dropbox.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class UserActivity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String action;

    private Date createdDate;

    @ManyToOne
    private User owner;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
