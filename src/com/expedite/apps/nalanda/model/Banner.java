
package com.expedite.apps.nalanda.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Banner {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("bookIssueList")
    @Expose
    private List<BookIssueList> bookIssueList = null;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public List<BookIssueList> getBookIssueList() {
        return bookIssueList;
    }

    public void setBookIssueList(List<BookIssueList> bookIssueList) {
        this.bookIssueList = bookIssueList;
    }

    public class BookIssueList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("imgUrl")
        @Expose
        private String imgUrl;
        @SerializedName("RedirectUrl")
        @Expose
        private String redirectUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

    }

}
