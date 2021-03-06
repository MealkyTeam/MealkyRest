package com.mealky.rest.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mealky.rest.model.ApiError;
import com.mealky.rest.model.ApiResponse;
import com.mealky.rest.model.PasswordResetToken;
import com.mealky.rest.model.User;
import com.mealky.rest.model.UserConfirmToken;
import com.mealky.rest.model.wrapper.MessageWrapper;
import com.mealky.rest.model.wrapper.UserWrapper;
import com.mealky.rest.repository.PasswordResetTokenRepository;
import com.mealky.rest.repository.UserConfirmRepository;
import com.mealky.rest.repository.UserRepository;
import com.mealky.rest.service.EmailSender;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UserController {

    @Autowired
    UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    EmailSender emailservice;
    @Autowired
    UserConfirmRepository ucrepo;
    @Autowired
    PasswordResetTokenRepository passrepo;

    @PostMapping("/sec/signup")
    ResponseEntity<Object> addUser(@RequestBody User user) {
        if (!checkEmail(user.getEmail())) return new ResponseEntity<>(
                new MessageWrapper(ApiError.INVALID_EMAIL.error()), HttpStatus.CONFLICT);
        if (!checkUsername(user.getUsername())) return new ResponseEntity<>(
                new MessageWrapper(ApiError.INVALID_USERNAME.error()), HttpStatus.CONFLICT);
        if (!checkPasswordLength(user.getPassword())) return new ResponseEntity<>(
                new MessageWrapper(ApiError.INVALID_PASSWORD.error()), HttpStatus.CONFLICT);
        if (repository.findDistinctByEmailIgnoreCaseLike(user.getEmail()) != null) return new ResponseEntity<>(
                new MessageWrapper(ApiError.EMAIL_TAKEN.error()), HttpStatus.CONFLICT);
        if (repository.findDistinctByUsernameIgnoreCaseLike(user.getUsername()) != null) return new ResponseEntity<>(
                new MessageWrapper(ApiError.USERNAME_TAKEN.error()), HttpStatus.CONFLICT);
        
        UserConfirmToken uc = new UserConfirmToken();
        User u = new User();
        try {
            u.setUsername(user.getUsername());
            u.setEmail(user.getEmail());
            u.setPassword(passwordEncoder.encode(user.getPassword()));
            u.setConfirmed(false);
            u.setTokenDate(new Date());
            uc.setEmailToken(UUID.randomUUID().toString().replace("-", ""));
            uc.setUser(u);
            repository.save(u);
            ucrepo.save(uc);
            emailservice.sendConfirmMail(uc);
        } catch (Exception e) {
            ucrepo.delete(uc);
            repository.delete(u);
            e.printStackTrace();
            return new ResponseEntity<Object>(new MessageWrapper(ApiError.SOMETHING_WENT_WRONG.error()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private boolean checkEmail(String email) {
        String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    private boolean checkUsername(String username) {
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(username).matches();
    }

    private boolean checkPasswordLength(String password) {
        return password.length() > 5;
    }

    @PostMapping("/sec/login")
    ResponseEntity<Object> loginUser(@RequestBody User user) {
        User u;
        if (user.getToken() != null) {
            u = repository.findByToken(user.getToken());
            if (u != null) {
                if (!u.isConfirmed())
                    return new ResponseEntity<>(new MessageWrapper(ApiError.CONFIRM_EMAIL.error()), HttpStatus.NOT_FOUND);
                Duration d = Duration.between(u.getTokenDate().toInstant(), Instant.now());
                if (d.toDays() > 30) {
                    u.setToken(UUID.randomUUID().toString());
                    u.setTokenDate(new Date());
                    try {
                        repository.save(u);
                        return new ResponseEntity<>(new UserWrapper(u.getId(), u.getUsername(), u.getEmail(), u.getToken()), HttpStatus.CREATED);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ResponseEntity<>(new MessageWrapper(ApiError.SOMETHING_WENT_WRONG.error()), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                return new ResponseEntity<>(new UserWrapper(u.getId(), u.getUsername(), u.getEmail(), u.getToken()), HttpStatus.OK);
            }
        }
        if (user.getPassword() == null && user.getEmail() == null)
            return new ResponseEntity<>(ApiError.INVALID_TOKEN.error(), HttpStatus.NOT_FOUND);

        u = repository.findDistinctByEmailIgnoreCaseLike(user.getEmail());
        if (u == null)
            return new ResponseEntity<>(new MessageWrapper(ApiError.NO_SUCH_USER.error()), HttpStatus.NOT_FOUND);
        if (!u.isConfirmed())
            return new ResponseEntity<>(new MessageWrapper(ApiError.CONFIRM_EMAIL.error()), HttpStatus.NOT_FOUND);
        if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
            if (u.getToken() == null) {
                u.setToken(UUID.randomUUID().toString());
                u.setTokenDate(new Date());
            }
            try {
                repository.save(u);
                return new ResponseEntity<>(new UserWrapper(u.getId(), u.getUsername(), u.getEmail(), u.getToken()), HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(new MessageWrapper(ApiError.SOMETHING_WENT_WRONG.error()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(new MessageWrapper(ApiError.WRONG_PASSWORD.error()), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/confirm")
    public ModelAndView accountConfirm(@RequestParam(name = "token", required = false) String token) {
    	ResponseEntity<Object> ret = confirmMethod(token);
    	ModelAndView modelView = new ModelAndView("returnInfo");
    	MessageWrapper body = (MessageWrapper) ret.getBody();
    	modelView.addObject("message", body.getMessage());
    	return modelView;
    }
    private ResponseEntity<Object> confirmMethod(String token)
    {
        if (token != null) {
            try {
                UserConfirmToken uc = ucrepo.findByEmailToken(token);
                if (uc != null) {
                    User u = uc.getUser();
                    u.setConfirmed(true);
                    repository.save(u);
                    ucrepo.delete(uc);
                    return new ResponseEntity<>(new MessageWrapper(ApiResponse.ACCOUNT_HAS_BEEN_ACTIVATED.response()), HttpStatus.OK);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<Object>(new MessageWrapper(ApiError.SOMETHING_WENT_WRONG.error()),HttpStatus.NOT_FOUND);
    }
    @PostMapping("/changepassword")
    public ModelAndView changePassword(@RequestParam(name = "email", required = false) String email, @RequestParam(name = "oldpass", required = false) String currentpassword,
                                                 @RequestParam(name = "newpass", required = false) String newpass, @RequestParam(name = "confnewpass", required = false) String confnewpass) {
        ResponseEntity<Object> returned = changePasswordMethod(email, currentpassword, newpass, confnewpass);
    	ModelAndView modelView = new ModelAndView("returnInfo");
    	MessageWrapper body = (MessageWrapper) returned.getBody();
    	modelView.addObject("message", body.getMessage());
    	return modelView;
    }
    private ResponseEntity<Object> changePasswordMethod(String email, String currentpassword, String newpass, String confnewpass)
    {
    	if (currentpassword == null || email == null || newpass == null || confnewpass == null)
            return new ResponseEntity<>(ApiError.SOMETHING_WENT_WRONG.error(), HttpStatus.NOT_FOUND);
        User user = repository.findDistinctByEmailIgnoreCaseLike(email);
        if (user != null) {
            if (passwordEncoder.matches(currentpassword, user.getPassword())) {
                if (newpass.equals(confnewpass) && checkPasswordLength(newpass) && checkPasswordLength(confnewpass)) {
                    user.setPassword(passwordEncoder.encode(newpass));
                    repository.save(user);
                    return new ResponseEntity<Object>(new MessageWrapper(
                            ApiResponse.NEW_PASSWORD_SET.response()), HttpStatus.OK);
                }
                return new ResponseEntity<>(new MessageWrapper(
                        ApiError.PASSWORDS_DOES_NOT_MATCH.error()), HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(new MessageWrapper(
                    ApiError.WRONG_PASSWORD.error()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new MessageWrapper(
                ApiError.NO_SUCH_USER.error()), HttpStatus.NOT_FOUND);
    }
    @PostMapping("/resetpassword")
    public ResponseEntity<Object> sendResetPasswordToken(@RequestParam(name = "email", required = false) String email) {
    	return resetPassword(email);
    }

    @PostMapping("/resetpasswordform")
    public ModelAndView sendResetPasswordTokenForm(@RequestParam(name = "email", required = false) String email) {
    	ResponseEntity<Object> ret = resetPassword(email);
    	ModelAndView modelView = new ModelAndView("returnInfo");
    	MessageWrapper body = (MessageWrapper) ret.getBody();
    	modelView.addObject("message", body.getMessage());
    	return modelView;
    }
    
    private ResponseEntity<Object> resetPassword(String email)
    {
        if (email != null) {
            User user = repository.findDistinctByEmailIgnoreCaseLike(email);
            if (user != null) {
                PasswordResetToken prt = passrepo.findByUser_EmailIgnoreCaseLike(email);
                if (prt == null)
                    prt = new PasswordResetToken();
                prt.setToken(UUID.randomUUID().toString().replace("-", ""));
                prt.setUser(user);
                passrepo.save(prt);
                try {
                    emailservice.sendResetPassMail(prt);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<Object>(new MessageWrapper(ApiError.SOMETHING_WENT_WRONG.error()),HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new ResponseEntity<Object>(new MessageWrapper(ApiResponse.RESET_LINK_SENT.response()), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(new MessageWrapper(ApiError.NO_SUCH_USER.error()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(new MessageWrapper(ApiError.SOMETHING_WENT_WRONG.error()), HttpStatus.NOT_FOUND);
    }
    
    
    @PostMapping("/setpassword")
    public ModelAndView setNewPassword(@RequestParam(name = "token", required = false) String token, @RequestParam(name = "email", required = false) String email,
                                                 @RequestParam(name = "newpass", required = false) String newpass, @RequestParam(name = "confnewpass", required = false) String confnewpass) {
    	ResponseEntity<Object> ret = setPassword(token, email, newpass, confnewpass);
    	ModelAndView modelView = new ModelAndView("returnInfo");
    	MessageWrapper body = (MessageWrapper) ret.getBody();
    	modelView.addObject("message", body.getMessage());
    	return modelView;
    }

    private ResponseEntity<Object> setPassword(String token,String email,String newpass,String confnewpass)
    {
        if (token == null || email == null || newpass == null || confnewpass == null)
            return new ResponseEntity<>(ApiError.SOMETHING_WENT_WRONG.error(), HttpStatus.NOT_FOUND);
        PasswordResetToken prt = passrepo.findByToken(token);
        if (prt != null) {
            User user = prt.getUser();
            if (user.getEmail().equals(email)) {
                if (newpass.equals(confnewpass) && checkPasswordLength(newpass) && checkPasswordLength(confnewpass)) {
                    user.setPassword(passwordEncoder.encode(newpass));
                    repository.save(user);
                    passrepo.delete(prt);
                    return new ResponseEntity<Object>(new MessageWrapper(ApiResponse.NEW_PASSWORD_SET.response()), HttpStatus.OK);
                }
                return new ResponseEntity<>(new MessageWrapper(
                        ApiError.PASSWORDS_DOES_NOT_MATCH.error()), HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(new MessageWrapper(
                    ApiError.EMAILS_DOES_NOT_MATCH.error()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new MessageWrapper(ApiError.SOMETHING_WENT_WRONG.error()), HttpStatus.NOT_FOUND);
    }
}
