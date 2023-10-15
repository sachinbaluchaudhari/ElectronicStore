package com.electonic.store.controllers;

import com.electonic.store.dtos.JwtRequest;
import com.electonic.store.dtos.JwtResponse;
import com.electonic.store.dtos.UserDto;
import com.electonic.store.entities.User;
import com.electonic.store.exception.BadApiRequestException;
import com.electonic.store.security.JwtHelper;
import com.electonic.store.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
//frondEnd app for accessing Api
//@CrossOrigin(origins = {"http://localhsot:4200"},allowedHeaders = {"Authorization"},methods = {RequestMethod.GET,RequestMethod.POST},maxAge = 3600)
public class AuthController {
    @Autowired
    AuthenticationManager manager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private ModelMapper mapper;
    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal)
    {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());

        System.out.println("------------"+userDetails.getAuthorities().stream().map(auth->auth).collect(Collectors.toList()));
        return new ResponseEntity<>(  mapper.map(userDetails,UserDto.class) , HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest)
    {
        this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = jwtHelper.generateToken(userDetails);
        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(token)
                .user(mapper.map(userDetails, UserDto.class)).build();

        return new ResponseEntity<>(  jwtResponse , HttpStatus.OK);
    }
    public void doAuthenticate(String email,String password)
    {

        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(email,password);
         try {
             manager.authenticate(authentication);
         }catch (BadCredentialsException e)
         {
             throw  new BadApiRequestException("Invalid username and password!!");
         }
    }
    @Autowired
    private UserService userService;
    @Value("${googleClientId}")
    private String googleClientId;

    private Logger logger= LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/google")
    public ResponseEntity<JwtResponse> googleWithLogin(@RequestBody Map<String,Object> data) throws IOException {
      String idToken=data.get("idToken").toString();
        NetHttpTransport netHttpTransport=new NetHttpTransport();
        JacksonFactory jacksonFactory=JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder  verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));
        GoogleIdToken googleIdToken=GoogleIdToken.parse(verifier.getJsonFactory(),idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
       logger.info("Payload: {}",payload);
       String email = payload.getEmail();
        User user=null;
        user=userService.findByEmailForGooleWithLogin(email).orElse(null);
        if (user==null)
        {
            user= this.userService.saveUser(email, data.get("name").toString(), data.get("photoUrl").toString());
        }
        ResponseEntity<JwtResponse> jwtResponse= login(JwtRequest.builder().email(email).password("12345678").build());

       return jwtResponse; 
    }
}
