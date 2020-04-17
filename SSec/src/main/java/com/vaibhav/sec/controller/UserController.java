package com.vaibhav.sec.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vaibhav.sec.exception.AppException;
import com.vaibhav.sec.model.Articles;
import com.vaibhav.sec.model.Role;
import com.vaibhav.sec.model.RoleName;
import com.vaibhav.sec.model.User;
import com.vaibhav.sec.repo.RoleRepo;
import com.vaibhav.sec.repo.UserRepo;
import com.vaibhav.sec.security.UserPrincipal;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepo userRepo;
	@Autowired
	RoleRepo roleRepo;
	
	 @Autowired
	    private PasswordEncoder passwordEncoder;
	 
	 
	@GetMapping("/getcourent")

	ResponseEntity<User>getUser(){
		String mail;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		mail = ((UserPrincipal) principal).getEmail();
		User user = userRepo.findByEmail(mail).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username or email : " + mail));		
		 
		 System.out.println("user   "+user.getPhoto());

		try {
		  		File f=new File(user.getPhoto()); 
	 	 
				 System.out.println("user   "+user.getPhoto());

					 String encodeBase64=null;
					 String extense=FilenameUtils.getExtension(f.getName());
					 System.out.println("user   "+user.getPhoto());

						FileInputStream fileInputStream=new FileInputStream(f);
		 			byte [] bytes=new byte[(int)f.length()];
		 			fileInputStream.read(bytes);
		 			encodeBase64=Base64.getEncoder().encodeToString(bytes);
		 			user.setPhoto("data:image/"+extense+";base64,"+encodeBase64);
 		 			 
		 			fileInputStream.close();
				 System.out.println("user  3 "+user.getPhoto());

				 } catch (Exception e) {
					 System.out.println("user  3 "+e);
	 				}
 		
			return new ResponseEntity<User>(user, HttpStatus.OK);

	}
	 @PostMapping("/add")
	  
     public void uplaodImage(@RequestParam("imageFile") MultipartFile file,@RequestParam("nom") String nom,
    		 
    		 @RequestParam("prenom")String prenom,@RequestParam("numtel")String numtel,
    		 @RequestParam("email") String email,@RequestParam("password") String password,
    		 @RequestParam("lieu")String lieu ) 
    		  throws IOException {
	  System.out.println(email +"emaillll");
         System.out.println("Original Image Byte Size - " + file.getBytes().length);
	  byte [] bytes=file.getBytes();
  //Path path=Paths.get(URI.create( folder+file.getOriginalFilename()) );
  Path path = FileSystems.getDefault().getPath("C:/Users/admin/Desktop/spring-security-jwt-mysql-master/src/main/photos_profil/"+file.getOriginalFilename());

  Files.write(path, bytes);
  User user=new User();
   
 	  user.setEmail(email);
  user.setName(nom+" "+prenom); 
  user.setNumtel(numtel);
  user.setPassword(passwordEncoder.encode(password));
  user.setLieu(lieu);
   Role userRole = roleRepo.findByName(RoleName.ROLE_USER)
			.orElseThrow(() -> new AppException("User Role not set."));
   
   user.setRoles(Collections.singleton(userRole));
   user.setPhoto("C:/Users/admin/Desktop/spring-security-jwt-mysql-master/src/main/photos_profil/"+file.getOriginalFilename());
  userRepo.save(user);


  

          
  
	  
     }

}
