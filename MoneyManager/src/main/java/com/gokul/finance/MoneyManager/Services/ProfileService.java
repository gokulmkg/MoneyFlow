package com.gokul.finance.MoneyManager.Services;

import com.gokul.finance.MoneyManager.Config.SecurityConfig;
import com.gokul.finance.MoneyManager.Dto.AuthDto;
import com.gokul.finance.MoneyManager.Dto.ProfileDto;
import com.gokul.finance.MoneyManager.Entities.ProfileEntity;
import com.gokul.finance.MoneyManager.Repository.ProfileRepository;
import com.gokul.finance.MoneyManager.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ProfileDto registerProfile(ProfileDto profileDto) {
       ProfileEntity newProfile =  toEntity(profileDto);
       newProfile.setActivationToken(UUID.randomUUID().toString());

     newProfile =  profileRepository.save(newProfile);
     // send activation email
        String activationLink = "http://localhost:8080/api/v1.0/activate?token=" + newProfile.getActivationToken();
        String subject = "Activate your Money Manager account";
       //String body = "Click on the following link to activate your account: " + activationLink;
        String body =
                "Hi " + newProfile.getFullName() + ",<br><br>"
                        + "Welcome to <b>Money Manager</b>! 🎉<br><br>"
                        + "Thank you for registering with us. To complete your registration and activate your account, "
                        + "please click the button below:<br><br>"
                        + "<a href=\"" + activationLink + "\" "
                        + "style=\"background-color:#1E88E5;"
                        + "color:white;"
                        + "padding:12px 22px;"
                        + "text-decoration:none;"
                        + "border-radius:6px;"
                        + "font-weight:bold;"
                        + "display:inline-block;\">"
                        + "Activate Account"
                        + "</a><br><br>"
                        + "If the button above does not work, copy and paste the following link into your browser:<br>"
                        + "<a href=\"" + activationLink + "\">" + activationLink + "</a><br><br>"
                        + "For your security, this activation link is valid for a limited time.<br><br>"
                        + "If you did not create this account, you can safely ignore this email.<br><br>"
                        + "Best regards,<br>"
                        + "<b>Money Manager Team</b>";

        emailService.sendEmail(newProfile.getEmail(),subject,body);
    return toDto(newProfile);
    }

    // helper method
     public ProfileEntity toEntity(ProfileDto profileDto) {
        return ProfileEntity.builder()
                .id(profileDto.getId())
                .fullName(profileDto.getFullName())
                .email(profileDto.getEmail())
                .password(passwordEncoder.encode(profileDto.getPassword()))
                .profileImageUrl(profileDto.getProfileImageUrl())
                .createdAt(profileDto.getCreatedAt())
                .updatedAt(profileDto.getUpdatedAt())
                .build();
    }
    // helper method
    public ProfileDto toDto(ProfileEntity profileEntity) {
        return ProfileDto.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }

    public boolean activateProfile(String activationToken) {
        return profileRepository.findByActivationToken(activationToken)
                .map(profile ->{
                  profile.setIsActive(true);
                  profileRepository.save(profile);
                    return true;

                }) .orElse(false);
    }

    public boolean isAccountActive(String email) {
          return  profileRepository.findByEmail(email)
                  .map(ProfileEntity::getIsActive)
                  .orElse(false);
    }

    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email =  authentication.getName();
      return profileRepository.findByEmail(email)
               .orElseThrow(()->new UsernameNotFoundException("Profile not found with email: " + email));
    }

   public ProfileDto getPublicProfile(String email) {
        ProfileEntity currentProfile = null;
        if(email == null) {
          currentProfile =  getCurrentProfile();
        } else {
           currentProfile =  profileRepository.findByEmail(email)
                   .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        }

        return ProfileDto.builder()
                .id(currentProfile.getId())
                .email(currentProfile.getEmail())
                .fullName(currentProfile.getFullName())
                .profileImageUrl(currentProfile.getProfileImageUrl())
                .createdAt(currentProfile.getCreatedAt())
                .updatedAt(currentProfile.getUpdatedAt())
                .build();
   }

    public Map<String, Object> authenticateAndGenrateToken(AuthDto authDto) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(),authDto.getPassword()));
         // Generate JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
           String token = jwtUtil.generateToken(userDetails);
            return Map.of(
                    "token",token   ,
                    "user",getPublicProfile(authDto.getEmail())
            );
        }catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }
}
