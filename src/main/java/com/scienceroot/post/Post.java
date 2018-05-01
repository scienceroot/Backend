package com.scienceroot.post;

import com.scienceroot.user.ApplicationUser;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author svenseemann
 */
@Entity
public class Post implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid_posts")
    @GenericGenerator(name = "uuid_posts", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @ManyToOne()
    private ApplicationUser creator;

    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the creator
     */
    public ApplicationUser getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(ApplicationUser creator) {
        this.creator = creator;
    }
    
    
}
