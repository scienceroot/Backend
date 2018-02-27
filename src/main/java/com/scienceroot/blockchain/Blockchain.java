/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.blockchain;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

/**
 * @author husche
 */
public class Blockchain {

	public Blockchain() {
	}

	public Boolean sendInitialFunds(String address) {
		try {
			Resource wallet = this.loadWalletFile();
			Web3j web3 = Web3j.build(new HttpService("https://chain.scienceroots.com"));
			Web3ClientVersion wcv = web3.web3ClientVersion().send();
			Credentials creds = WalletUtils.loadCredentials("secret", wallet.getFile());

			TransactionReceipt tr = Transfer.sendFunds(web3, creds, address, BigDecimal.ONE, Convert.Unit.ETHER).send();
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	protected Resource loadWalletFile() {
		return new ClassPathResource("wallet.dat");
	}

}
