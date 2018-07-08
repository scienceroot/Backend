package com.scienceroot.repository;

import com.scienceroot.blockchain.Blockchain;
import com.scienceroot.user.ApplicationUser;
import com.wavesplatform.wavesj.Base58;
import com.wavesplatform.wavesj.PrivateKeyAccount;
import com.wavesplatform.wavesj.Transaction;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {

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
        repository.setPrivateKey(dataRequestBody.privateKey);
        
        Transaction dataTx = repository.store(dataRequestBody.data);

        this.save(repository);
        
        return this.blockchain.sendTx(dataTx);
    }
    
    
    public Optional<Repository> findOne(UUID id) {
        return Optional.ofNullable(this.repositoryRepository.findOne(id));
    }

    public List<Repository> find(ApplicationUser user) {
        return this.repositoryRepository.findByCreator(user);
    }
}
