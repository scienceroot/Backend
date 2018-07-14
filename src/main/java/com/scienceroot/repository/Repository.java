package com.scienceroot.repository;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.scienceroot.blockchain.Blockchain;
import com.scienceroot.user.ApplicationUser;
import com.wavesplatform.wavesj.DataEntry;
import com.wavesplatform.wavesj.DataEntry.BinaryEntry;
import com.wavesplatform.wavesj.PrivateKeyAccount;
import com.wavesplatform.wavesj.Transaction;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "repositories")
public class Repository implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "uuid_repositories")
    @GenericGenerator(name = "uuid_repositories", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    @JsonProperty("id")
    @JsonView(RepositoryViews.Public.class)
    private UUID id;
    
    @Column
    @JsonView(RepositoryViews.Public.class)
    private String name;
    
    @Column
    @JsonView(RepositoryViews.Authorized.class)
    private String privateKey;
    
    @Column
    @JsonView(RepositoryViews.Public.class)
    private String publicKey;

    @Column
    @JsonView(RepositoryViews.Authorized.class)
    private Integer pageSequenceNextValue = 1;
    
    @ManyToOne()
    @JsonView(RepositoryViews.Public.class)
    private ApplicationUser creator; 

    public Repository() {
        
    }
    
    /**
     *
     * @param data
     * @return 
     */
    public Transaction create(byte[] data, double fee){
        String key = this.getPageKey();
        
        return this.getDataTransaction(key, data, fee);
    }

    public Transaction update(String key, byte[] data, double fee){
        return this.getDataTransaction(key, data, fee);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationUser getCreator() {
        return creator;
    }

    public void setCreator(ApplicationUser creator) {
        this.creator = creator;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    
    /**
     * @return the pageSequenceNextValue
     */
    public Integer getPageSequenceNextValue() {
        return pageSequenceNextValue;
    }

    /**
     * @param pageSequenceNextValue the pageSequenceNextValue to set
     */
    public void setPageSequenceNextValue(Integer pageSequenceNextValue) {
        this.pageSequenceNextValue = pageSequenceNextValue;
    }

    private String getPageKey() {
        return "wiki_" + this.pageSequenceNext().toString();
    }

    private Integer pageSequenceNext() {
        Integer currentValue = this.pageSequenceNextValue;

        this.pageSequenceNextValue += 1;

        return currentValue;
    }

    private Transaction getDataTransaction(String key, byte[] data, double fee) {
        BinaryEntry entry = new DataEntry.BinaryEntry(key, data);
        PrivateKeyAccount sender = PrivateKeyAccount.fromPrivateKey(this.privateKey, Blockchain.NETWORK_ID);
        List<DataEntry<?>> dataEntries = new LinkedList<>();

        dataEntries.add(entry);
        
        return Transaction.makeDataTx(sender, dataEntries, (int) fee);
    }
}
