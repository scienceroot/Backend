/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.blockchain;

import com.scienceroot.config.ResourceService;
import com.wavesplatform.wavesj.Node;
import com.wavesplatform.wavesj.PrivateKeyAccount;
import com.wavesplatform.wavesj.PublicKeyAccount;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author husche
 */
@Service
public class Blockchain {

    private static final String SCR_CHAIN = "https://chain.scienceroots.com";
    private Logger LOG = Logger.getLogger(Blockchain.class.getName());
    private ResourceService resourceService;

    @Autowired
    public Blockchain(ResourceService resourceService) {
        this.resourceService = resourceService;
    }
    public boolean sendInitialFunds(String address) {

        String genesisAccountSeed = "scienceroot";
        String nodeAddress = "https://scienceblock.org";
        PrivateKeyAccount genesisAcc = PrivateKeyAccount.fromSeed(genesisAccountSeed, 0, 'D');
        long amount = 1000000;

        try {
            PublicKeyAccount pka = new PublicKeyAccount(address, 'D');
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
