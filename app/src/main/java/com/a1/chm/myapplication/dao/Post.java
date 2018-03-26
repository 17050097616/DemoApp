package com.a1.chm.myapplication.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author chm
 * @time 2017/10/30 0030  下午 5:53
 * @desc ${TODD}
 */

@Entity
public class Post {
    @Id
    private Long id;
    private String content;
    private Long authorId;
    @Generated(hash = 1473191557)
    public Post(Long id, String content, Long authorId) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
    }
    @Generated(hash = 1782702645)
    public Post() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Long getAuthorId() {
        return this.authorId;
    }
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}