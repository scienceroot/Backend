package com.scienceroot.blockchain;

import com.wavesplatform.wavesj.PrivateKeyAccount;
import org.springframework.data.annotation.Transient;

public class Wallet {

    @Transient
    private PrivateKeyAccount account;
    
    private String seed;

    public Wallet(PrivateKeyAccount account, String seed) {
        this.account = account;
        this.seed = seed;
    }

    public PrivateKeyAccount getAccount() {
        return account;
    }

    public String getSeed() {
        return seed;
    }
}
