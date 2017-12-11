package com.app.dropbox.entity;

import javax.persistence.*;
import java.util.*;

@Entity
public class Asset {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private String originalName;

    private Boolean isDirectory;

    private Boolean isDeleted;

    private Date createdDate;

    @Lob
    private String metadata;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Asset parent;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "asset_starred_users",
            joinColumns = { @JoinColumn(name = "asset_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> starredUsers = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "asset_shared_users",
            joinColumns = { @JoinColumn(name = "asset_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> sharedUsers = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "asset_shared_groups",
            joinColumns = { @JoinColumn(name = "asset_id") },
            inverseJoinColumns = { @JoinColumn(name = "group_id") }
    )
    private Set<Group> sharedGroups = new HashSet<>();


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

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Asset getParent() {
        return parent;
    }

    public void setParent(Asset parent) {
        this.parent = parent;
    }

    public Set<User> getStarredUsers() {
        return starredUsers;
    }

    public void setStarredUsers(Set<User> starredUsers) {
        this.starredUsers = starredUsers;
    }

    public Set<User> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(Set<User> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }

    public Set<Group> getSharedGroups() {
        return sharedGroups;
    }

    public void setSharedGroups(Set<Group> sharedGroups) {
        this.sharedGroups = sharedGroups;
    }
}
