package com.project.workshopmongo.dto;

import java.io.Serializable;
import java.util.Date;

import com.project.workshopmongo.domain.Post;

public class PostDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private String id;
    private Date date;
    private String title;
    private String body;
    private AuthorDTO author;
    
    public PostDTO() {
    }

    public PostDTO(Post obj) {
        id = obj.getId();
        date = obj.getDate();
        title = obj.getTitle();
        body = obj.getBody();
        author = obj.getAuthor();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

}
