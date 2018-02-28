/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.blockchain;

import com.scienceroot.config.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
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
import java.nio.charset.StandardCharsets;
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

	public BigInteger getFunds(String address) {
		try {
			Web3j web3 = Web3j.build(new HttpService(SCR_CHAIN));
			EthGetBalance balance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
			return balance.getBalance();
		} catch (IOException ioe) {
			System.out.println(ioe.toString());
			return new BigInteger("-1");
		}
	}

	public boolean sendInitialFunds(String address) {

		LOG.info("sending initial funding to '" + address + "'");

		LOG.info("calling web3j at " + SCR_CHAIN);
		Web3j web3 = Web3j.build(new HttpService(SCR_CHAIN));

		try {
			LOG.info("loading wallet..");
			String wallet = this.loadWallet();

			LOG.info("receiving credentials from wallet..");
			Credentials creds = WalletUtils.loadCredentials("secret", wallet);

			LOG.info("sending one ether to '" + address + "'..");
			Transfer.sendFunds(web3, creds, address, BigDecimal.ONE, Convert.Unit.ETHER)
					.sendAsync();

		} catch (TransactionException | InterruptedException | CipherException | IOException e) {
			LOG.severe(e.getMessage());
			return false;
		}

		LOG.info("sending initial funding done");
		return true;
	}

	protected String loadWallet() {
		Resource wallet = this.loadWalletFile();

		LOG.info("validation of wallet file ..");
		Objects.requireNonNull(wallet);
		assert wallet.exists();
		LOG.info("validation of wallet file successfully");

		try {
			byte[] walletData = FileCopyUtils.copyToByteArray(wallet.getInputStream());
			return new String(walletData, StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOG.severe(e.getMessage());
			return "";
		}
	}

	protected Resource loadWalletFile() {

		LOG.info("loading wallet file..");
		return resourceService.loadFromResourcesFolder("wallet.dat");
	}

}
