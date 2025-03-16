package com.murek.appsocial.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.Date;

    @ParseClassName("Comentario")
    public class Comentario extends ParseObject {

        public Comentario() {}
        public String getId() {
            return getObjectId();
        }

        public ParseUser getUser() {
            return getParseUser("user");
        }

        public void setUser(ParseUser user) {
            put("user", user);
        }

        public Post getPost() {
            return (Post) getParseObject("post");
        }

        public void setPost(Post post) {
            put("post", post);
        }

        public Date getCreatedAt() {
            return super.getCreatedAt();
        }
    }



