package com.classlocator.nitrr.controller.Public;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.services.superAdminService;

//This will expose the super admin endpoints to be used by the frontend to be developed by other team members
@RestController
@RequestMapping("/sadmin")
public class superAdminController extends controller {

    @Autowired
    private superAdminService sadmins;

    Map<String, String> token = new HashMap<String, String>();

    //Special query raise system for super admin to be created here
    

    @PostMapping("/signup")
    public ResponseEntity<?> activateAdmin(@RequestBody Map<String, String> suser) {
        int status = sadmins.saveUpdateSuperAdmin(suser);
        if(status == 1 || status == 0) {
            String action = status == 1 ? "Super Admin created" : "Super Admin updated";
            HttpStatus httpStatus = status == 1 ? HttpStatus.CREATED : HttpStatus.OK;
            token.put(action, jwt.generateToken("1",suser.get("name"),"NIT Raipur", "SUPER_ADMIN"));
            return new ResponseEntity<Map<String, String>>(token, httpStatus);
        } 
        else if(status == -1) return new ResponseEntity<String>("Wrong password", HttpStatus.UNAUTHORIZED);
        else if(status == -2) return new ResponseEntity<String>("Unauthorized", HttpStatus.FORBIDDEN);
        return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> s) {
        try {
            superAdmin status  = (superAdmin) sadmins.authorization(Integer.parseInt(s.get("rollno")),s.get("password")).get("sadmin");
            token.put("Token ", jwt.generateToken(status.getId().toString(),status.getName(),"NIT Raipur", "SUPER_ADMIN"));
            return new ResponseEntity<Map<String, String>>(token, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<String>("Wrong password", HttpStatus.UNAUTHORIZED);
        } catch (NullPointerException e) {
            return new ResponseEntity<String>("Not allowed", HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
