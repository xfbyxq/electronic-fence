package com.semptian.entity;

import java.io.Serializable;

public class BlogEntity implements Serializable {
    private String id;

    private String title;

    private String posttime;

    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "BlogEntity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", posttime='" + posttime + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
