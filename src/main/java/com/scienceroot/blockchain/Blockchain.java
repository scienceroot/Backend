/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.blockchain;

import com.wavesplatform.wavesj.Node;
import com.wavesplatform.wavesj.PrivateKeyAccount;
import com.wavesplatform.wavesj.PublicKeyAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author husche
 */
@Service
public class Blockchain {

    private static final Logger LOG = Logger.getLogger(Blockchain.class.getName());

    private static final String GENESIS_SEED = "scienceroot";
    private static final String NODE_URL = "https://scienceblock.org";
    private static final char NETWORK_ID = 'D';
    
    private Node node;
    
    /**
     *
     */
    @Autowired
    public Blockchain() {
        try {
            this.node = new Node(Blockchain.NODE_URL);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Blockchain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param publicKey
     * @return
     */
    public boolean sendInitialFunds(String publicKey) {
        PrivateKeyAccount genesisAcc = this.getGenesisAccount();
        long amount = 1000000;

        try {
            PublicKeyAccount pka = new PublicKeyAccount(publicKey, Blockchain.NETWORK_ID);
            this.node.transfer(genesisAcc, pka.getAddress(), amount, 100000, "initial funds");
        } catch (IOException e) {
            LOG.severe(e.getMessage());
            return false;
        }

        LOG.log(Level.INFO, "sending initial funding to {0}", publicKey);
        
        return true;
    }
    
    public PrivateKeyAccount createAccount() {
        String seed = PrivateKeyAccount.generateSeed();
        
        return this.getAccount(seed);
    }
    
    private PrivateKeyAccount getGenesisAccount() {
        String seed = System.getenv(Blockchain.GENESIS_SEED);
        
        return this.getAccount(seed);
    }
    
    private PrivateKeyAccount getAccount(String seed) {
        return PrivateKeyAccount.fromSeed(seed, 0, Blockchain.NETWORK_ID);
    }
}
