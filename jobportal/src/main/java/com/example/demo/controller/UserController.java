package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.entity.UserType;
import com.example.demo.service.UserService;
import com.example.demo.service.UserTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {
	

private final UserTypeService userTypeService;
private final UserService userService;

@Autowired
public UserController(UserTypeService userTypeService,UserService userService) {
	
	this.userTypeService = userTypeService;
	this.userService =userService;
}

@GetMapping("/register")
public String register(Model model) {
	List<UserType> usertype=userTypeService.getAll();
	model.addAttribute("getAllTypes",usertype);
	model.addAttribute("user",new User());
   return "register";
}



@PostMapping("/register/new")
public String userRegistration(
        @Valid User user,
        BindingResult result,
        Model model) {

    if (result.hasErrors()) {
        List<UserType> usertype = userTypeService.getAll();
        model.addAttribute("getAllTypes", usertype);
        return "register";
    }

    Optional<User> optionalUser = userService.getUserByEmail(user.getEmail());
    if (optionalUser.isPresent()) {
        model.addAttribute("error", "Email already registered");
        List<UserType> usertype = userTypeService.getAll();
        model.addAttribute("getAllTypes", usertype);
        return "register";
    }

    userService.addNew(user);

    model.addAttribute("verificationSent", true);

    List<UserType> usertype = userTypeService.getAll();
    model.addAttribute("getAllTypes", usertype);
    model.addAttribute("user", new User());

    return "register";
}


@GetMapping("/login")
public String login()
{
	return "login";
}

@GetMapping("/logout")
public String logout(HttpServletRequest request,HttpServletResponse response)
{
	Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
	
	if(authentication!=null)
	{
		new SecurityContextLogoutHandler().logout(request,response,authentication);
	}
	return "redirect:/";
}

}