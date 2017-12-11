package com.app.dropbox.entity;

import javax.persistence.*;
import java.util.*;

@Entity()
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private Date createdDate;

    @ManyToOne
    private User owner;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "group_members",
            joinColumns = { @JoinColumn(name = "group_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> members = new HashSet<>();

    @ManyToMany(mappedBy = "sharedGroups")
    private Set<Asset> sharedAssets = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<Asset> getSharedAssets() {
        return sharedAssets;
    }

    public void setSharedAssets(Set<Asset> sharedAssets) {
        this.sharedAssets = sharedAssets;
    }
}
