/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.blockchain;

import com.scienceroot.config.ResourceService;
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
			return new BigInteger("-1");
		}
	}

	public boolean sendInitialFunds(String address) {

		long amount = 100;

		LOG.info("sending initial funding to '" + address + "'");

		LOG.info("calling web3j at " + SCR_CHAIN);
		Web3j web3 = Web3j.build(new HttpService(SCR_CHAIN));

		try {
			LOG.info("loading wallet..");
			File wallet = this.loadWallet();

			LOG.info("receiving credentials from wallet..");
			Credentials creds = WalletUtils.loadCredentials("secret", wallet);

			LOG.info("sending " + amount + " ether to '" + address + "'..");
			Transfer.sendFunds(web3, creds, address, BigDecimal.valueOf(amount), Convert.Unit.ETHER)
					.sendAsync();

		} catch (TransactionException | InterruptedException | CipherException | IOException e) {
			LOG.severe(e.getMessage());
			return false;
		}

		LOG.info("sending initial funding done");
		return true;
	}

	protected File loadWallet() throws IOException {
		Resource wallet = this.loadWalletFile();

		LOG.info("validation of wallet file ..");
		Objects.requireNonNull(wallet);
		assert wallet.exists();
		LOG.info("validation of wallet file successfully");

		File tmp_wallet = File.createTempFile("tmp_wallet", ".dat");
		LOG.info("created tmp wallet file '" + tmp_wallet.getAbsolutePath() + "'");

		FileUtils.copyInputStreamToFile(wallet.getInputStream(), tmp_wallet);
		LOG.info("copy of wallet to tmp wallet done");

		return tmp_wallet;
	}

	protected Resource loadWalletFile() {

		LOG.info("loading wallet file..");
		return resourceService.loadFromResourcesFolder("wallet.dat");
	}

}
