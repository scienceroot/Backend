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
import java.util.logging.Logger;

/**
 * @author husche
 */
@Service
public class Blockchain {

    private static final Logger LOG = Logger.getLogger(Blockchain.class.getName());

    /**
     *
     */
    @Autowired
    public Blockchain() {
    }

    /**
     *
     * @param publicKey
     * @return
     */
    public boolean sendInitialFunds(String publicKey) {

        String genesisAccountSeed = "scienceroot";
        String nodeAddress = "https://scienceblock.org";
        PrivateKeyAccount genesisAcc = PrivateKeyAccount.fromSeed(genesisAccountSeed, 0, 'D');
        long amount = 1000000;

        try {
            PublicKeyAccount pka = new PublicKeyAccount(publicKey, 'D');
            Node node = new Node(nodeAddress);
            node.transfer(genesisAcc, pka.getAddress(), amount, 100000, "initial funds");
        } catch (URISyntaxException | IOException e) {
            LOG.severe(e.getMessage());
            return false;
        }

        LOG.info("sending initial funding done");
        return true;
    }

}
