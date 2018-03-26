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
public class Test {
    @Id
    private Long id;
    private String content;
    private Long authorId;
    @Generated(hash = 1709019823)
    public Test(Long id, String content, Long authorId) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
    }
    @Generated(hash = 372557997)
    public Test() {
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
