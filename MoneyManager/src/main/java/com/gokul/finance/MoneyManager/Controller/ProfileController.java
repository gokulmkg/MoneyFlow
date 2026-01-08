package com.gokul.finance.MoneyManager.Controller;

import com.gokul.finance.MoneyManager.Dto.AuthDto;
import com.gokul.finance.MoneyManager.Dto.ProfileDto;
import com.gokul.finance.MoneyManager.Services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDto> registerProfile(@RequestBody ProfileDto profileDto) {
        ProfileDto registerProfile =  profileService.registerProfile(profileDto);
       return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activationProfile(@RequestParam String token) {
        boolean isActiveed = profileService.activateProfile(token);
        if(isActiveed) {
            return ResponseEntity.ok("Profile activated Successfully");
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDto authDto) {
            try {
                if(!profileService.isAccountActive(authDto.getEmail())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                       "message","Account is not active. Please Activate your account first."
                    ));
                }
                Map<String,Object> respose = profileService.authenticateAndGenrateToken(authDto);
                return ResponseEntity.ok(respose);
            } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",e.getMessage()));
            }


    }

    @GetMapping("/test")
    public String test() {
        return "Test Successful";
    }

    }



