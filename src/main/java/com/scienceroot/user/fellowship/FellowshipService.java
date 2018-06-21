package com.scienceroot.user.fellowship;

import java.util.List;

import com.scienceroot.user.ApplicationUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FellowshipService {

    private FellowshipRepository fellowshipRepository;

    @Autowired
    public FellowshipService(
        FellowshipRepository fellowshipRepository
    ) {
        this.fellowshipRepository = fellowshipRepository;
    }

    public List<Fellowship> getFollowers(ApplicationUser followed) {
        return this.fellowshipRepository.findByFollowedId(followed.getId());
    }

    public List<Fellowship> getFollows(ApplicationUser follows) {
        return this.fellowshipRepository.findByFollowerId(follows.getId());
    }
    
    public Fellowship follow(ApplicationUser followed, ApplicationUser follower) {
        Fellowship fellowship = new Fellowship(followed, follower);

        return this.fellowshipRepository.save(fellowship);
    }

    public void unfollow(ApplicationUser followed, ApplicationUser follower) {
        List<Fellowship> toDelete = this.fellowshipRepository
            .findByFollowedIdAndFollowerId(followed.getId(), follower.getId());

        this.fellowshipRepository.delete(toDelete);
    }

    public boolean isFollowing(ApplicationUser followed, ApplicationUser follower) {
        return this.fellowshipRepository
            .findByFollowedIdAndFollowerId(followed.getId(), follower.getId()).size() > 0;
    }

    public void deleteAll() {
        this.fellowshipRepository.deleteAll();
    }

    /**
     *
     * @param <S>
     * @param s
     * @return
     */
    public <S extends Fellowship> S save(S s) {
        return this.fellowshipRepository.save(s);
    }
}