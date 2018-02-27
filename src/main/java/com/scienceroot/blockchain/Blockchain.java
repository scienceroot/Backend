/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.blockchain;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author husche
 */
public class Blockchain {

	public Blockchain() {
	}

	public BigInteger getFunds(String address) {
		try {
			Web3j web3 = Web3j.build(new HttpService("https://chain.scienceroots.com"));
			EthGetBalance balance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
			return balance.getBalance();
		} catch (IOException ioe) {
			System.out.println(ioe.toString());
			return new BigInteger("-1");
		}
	}

	public Boolean sendInitialFunds(String address) {
		try {
			Resource wallet = this.loadWalletFile();
			Web3j web3 = Web3j.build(new HttpService("https://chain.scienceroots.com"));
			Credentials creds = WalletUtils.loadCredentials("secret", wallet.getFile());
			Transfer.sendFunds(web3, creds, address, BigDecimal.ONE, Convert.Unit.ETHER).sendAsync();
		} catch (IOException | InterruptedException | CipherException | TransactionException e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	protected Resource loadWalletFile() {
		return new ClassPathResource("wallet.dat");
	}

}
