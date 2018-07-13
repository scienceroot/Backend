package com.scienceroot.repository;

import com.scienceroot.blockchain.Blockchain;
import com.scienceroot.repository.exceptions.DataTransactionSizeException;
import com.scienceroot.repository.exceptions.InsufficientFundsException;
import com.scienceroot.user.ApplicationUser;
import com.wavesplatform.wavesj.Base58;
import com.wavesplatform.wavesj.PrivateKeyAccount;
import com.wavesplatform.wavesj.PublicKeyAccount;
import com.wavesplatform.wavesj.Transaction;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {

    /**
     * Max size in bytes of binary type in data transaction
     */
    private static final int MAX_DATA_TRANSACTION_SIZE = 32768;

    private final RepositoryRepository repositoryRepository;
    private final Blockchain blockchain;

    @Autowired
    public RepositoryService(
            RepositoryRepository repositoryRepository,
            Blockchain blockchain
    ) {
        this.repositoryRepository = repositoryRepository;
        this.blockchain = blockchain;
    }
    
    public Repository create(Repository repository) {
        PrivateKeyAccount acc = this.blockchain.createAccount();
            
        repository.setPrivateKey(Base58.encode(acc.getPrivateKey()));
        repository.setPublicKey(Base58.encode(acc.getPublicKey()));
        
        return this.save(repository);
    }
    
    public Repository save(Repository s) {
        return this.repositoryRepository.save(s);
    }
    
    public String store(Repository repository, DataRequestBody dataRequestBody) throws IOException {
        if (this.validateDataRequest(dataRequestBody.data)) {
            return null;    
        }

        repository.setPrivateKey(dataRequestBody.privateKey);
        
        int fee = this.calculateFee(dataRequestBody.data);
        Transaction dataTx = repository.create(dataRequestBody.data, fee);

        /**
         * Save updated page sequence value
         */
        this.save(repository);
        
        return this.blockchain.sendTx(dataTx);
    }

    public String update(Repository repository, DataRequestBody dataRequestBody) throws IOException {
        if (this.validateDataRequest(dataRequestBody.data)) {
            return null;    
        }
        
        repository.setPrivateKey(dataRequestBody.privateKey);
        
        
        int fee = this.calculateFee(dataRequestBody.data);
        PublicKeyAccount account = new PublicKeyAccount(repository.getPublicKey().getBytes(), Blockchain.NETWORK_ID);
        
        if (this.validateSufficientFunding(account.getAddress(), fee)) {
            Transaction dataTx = repository.update(dataRequestBody.key, dataRequestBody.data, fee);
        
            return this.blockchain.sendTx(dataTx);   
        } else {
            return null;
        }
    }
    
    
    public Optional<Repository> findOne(UUID id) {
        return Optional.ofNullable(this.repositoryRepository.findOne(id));
    }

    public List<Repository> find(ApplicationUser user) {
        return this.repositoryRepository.findByCreator(user);
    }

    private boolean validateDataRequest(byte[] dataRequest) {
        if (dataRequest.length > MAX_DATA_TRANSACTION_SIZE) {
            throw new DataTransactionSizeException();
        }

        return true;
    }

    private boolean validateSufficientFunding(String address, long fee) throws IOException {
        long balance = this.blockchain.getBalance(address);
        
        if (balance < fee) {
            throw new InsufficientFundsException();
        }

        return true;
    }

    private int calculateFee(byte[] data) {
        int kb = data.length / 1024;
        int fee = 100000 * kb;

        if (fee < 100000) {
            fee = 100000;
        }

        return fee;
    }
}
