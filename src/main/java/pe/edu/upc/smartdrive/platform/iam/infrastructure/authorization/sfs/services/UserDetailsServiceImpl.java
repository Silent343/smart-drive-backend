package pe.edu.upc.smartdrive.platform.iam.infrastructure.authorization.sfs.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.iam.domain.repositories.UserRepository;
import pe.edu.upc.smartdrive.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;

/**
 * Loads users for Spring Security by their login handle, which is an admin email or a
 * seller username. Emails are matched case-insensitively; usernames are matched as stored.
 */
@Service("defaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String handle) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(handle.toLowerCase())
                .or(() -> userRepository.findByUsername(handle))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + handle));
        return UserDetailsImpl.build(user);
    }
}
