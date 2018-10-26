package com.example.abhinav.quitsmoking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private static User user;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("next")
    @Expose
    private String next;

    @SerializedName("previous")
    @Expose
    private String previous;

    @SerializedName("results")
    @Expose
    private List<UserResult> users;

    public static User getUser() {
        if (user == null) user = new User();
        return user;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<UserResult> getUsers() {
        return users;
    }

    public void setUsers(List<UserResult> users) {
        this.users = users;
    }

    public static class UserResult implements Serializable {

        private static UserResult userResult;

        @SerializedName("id")
        @Expose
        private int userId;

        @SerializedName("username")
        @Expose
        private String username;

        @SerializedName("first_name")
        @Expose
        private String firstName;

        @SerializedName("last_name")
        @Expose
        private String lastName;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("url")
        @Expose
        private String userUrl;

        @SerializedName("token")
        @Expose
        private String token;

        @SerializedName("user_type")
        @Expose
        private int userType;

        @SerializedName("bio")
        @Expose
        private String bio;

        @SerializedName("date_of_birth")
        @Expose
        private String dateOfBirth;

        @SerializedName("profile_picture")
        @Expose
        private String profilePicture;

        @SerializedName("is_approved")
        @Expose
        private boolean approved;

        @SerializedName("has_paid_subscription")
        @Expose
        private boolean paidSubscription;

        @SerializedName("profile_tags")
        @Expose
        private List<String> profileTags;

        @SerializedName("referral_code")
        @Expose
        private String referralCode;

        @SerializedName("code")
        @Expose
        private String code;

        @SerializedName("referral_count")
        @Expose
        private String referralCount;

        private String password;

        public static UserResult getUserResult() {
            if (userResult == null) userResult = new UserResult();
            return userResult;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUserUrl() {
            return userUrl;
        }

        public void setUserUrl(String userUrl) {
            this.userUrl = userUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getReferralCode() {
            return referralCode;
        }

        public void setReferralCode(String referralCode) {
            this.referralCode = referralCode;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getProfilePicture() {
            return profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        public boolean isApproved() {
            return approved;
        }

        public void setApproved(boolean approved) {
            this.approved = approved;
        }

        public boolean hasPaidSubscription() {
            return paidSubscription;
        }

        public void setPaidSubscription(boolean paidSubscription) {
            this.paidSubscription = paidSubscription;
        }

        public List<String> getProfileTags() {
            return profileTags;
        }

        public void setProfileTags(List<String> profileTags) {
            this.profileTags = profileTags;
        }

        @Override
        public String toString() {
            return username;
        }

        public String getReferralCount() {
            return referralCount;
        }

        public void setReferralCount(String referralCount) {
            this.referralCount = referralCount;
        }
    }
}