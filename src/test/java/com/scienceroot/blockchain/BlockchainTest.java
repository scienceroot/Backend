/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.blockchain;

import org.junit.Test;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author husche
 */
public class BlockchainTest {

	/**
	 * Test of connectToBlockchain method, of class Blockchain.
	 */
	@Test
	public void testConnectToBlockchain() {
		System.out.println("connectToBlockchain");
		Blockchain instance = new Blockchain();
                System.out.println(instance.getFunds("0x368919D1B04c3B158a76765D26f4F73D12872AC9"));
                
		//assertTrue(instance.sendInitialFunds("0x368919D1B04c3B158a76765D26f4F73D12872AC9"));
		// TODO review the generated test code and remove the default call to fail.
		//fail("The test case is a prototype.");
                
	}

	@Test
	public void loadWalletFile() throws IOException {
		Blockchain blockchain = new Blockchain();
		Resource walletFile = blockchain.loadWalletFile();

		assertNotNull(walletFile);
		assertTrue(walletFile.exists());
		assertEquals("wallet.dat", walletFile.getFilename());
		assertNotNull(walletFile.getFile());
	}
}
