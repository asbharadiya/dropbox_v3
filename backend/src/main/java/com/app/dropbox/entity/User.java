package com.app.dropbox.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String contactNo;

    private String about;

    private String education;

    private String occupation;

    @OneToMany(mappedBy = "owner")
    private List<UserActivity> userActivity = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Group> myGroups = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Asset> myAssets = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    private List<Group> sharedGroups = new ArrayList<>();

    @ManyToMany(mappedBy = "starredUsers")
    private Set<Asset> starredAssets = new HashSet<>();

    @ManyToMany(mappedBy = "sharedUsers")
    private Set<Asset> sharedAssets = new HashSet<>();

    @Transient
    private String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public List<UserActivity> getUserActivity() {
        return userActivity;
    }

    public void setUserActivity(List<UserActivity> userActivity) {
        this.userActivity = userActivity;
    }

    public List<Group> getMyGroups() {
        return myGroups;
    }

    public void setMyGroups(List<Group> myGroups) {
        this.myGroups = myGroups;
    }

    public List<Group> getSharedGroups() {
        return sharedGroups;
    }

    public void setSharedGroups(List<Group> sharedGroups) {
        this.sharedGroups = sharedGroups;
    }

    public Set<Asset> getStarredAssets() {
        return starredAssets;
    }

    public void setStarredAssets(Set<Asset> starredAssets) {
        this.starredAssets = starredAssets;
    }

    public Set<Asset> getSharedAssets() {
        return sharedAssets;
    }

    public void setSharedAssets(Set<Asset> sharedAssets) {
        this.sharedAssets = sharedAssets;
    }

    public List<Asset> getMyAssets() {
        return myAssets;
    }

    public void setMyAssets(List<Asset> myAssets) {
        this.myAssets = myAssets;
    }
}