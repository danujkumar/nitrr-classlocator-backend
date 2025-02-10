package com.classlocator.nitrr.controller.Public;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.searchTool;
import com.classlocator.nitrr.interfaces.Pair;
import com.classlocator.nitrr.services.adminService;
import com.classlocator.nitrr.services.jwtService;

@RestController
public class controller {

    @Autowired
    private adminService admins;

    @Autowired
    protected jwtService jwt;

    @GetMapping("/generate")
    public ResponseEntity<String> generate(){
        
        if(admins.searchToolsGenerator())
        {
            return new ResponseEntity<String>("Generated successfully...", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Map not generated", HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/map")
    public ResponseEntity<String> map(){
        if(admins.generateMap(new searchTool())) {
            return new ResponseEntity<String>("JSON File Generated...", HttpStatus.CREATED);
        }
        return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/download/{version}")
    public ResponseEntity<String> map(@PathVariable("version") Integer version) {
        Pair<Integer, String> download = admins.downloadMap(version);
        if(download.getKey() == 1) {
            return new ResponseEntity<>(download.getValue(), HttpStatus.OK);
        }
        else if(download.getKey() == 0) return new ResponseEntity<String>("Already the latest version.", HttpStatus.OK);
        return new ResponseEntity<String>("Something went wrong, and we couldn't update.", HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<String> check(){
        return new ResponseEntity<String>("Yeah..., the setup is running...", HttpStatus.OK);
    }

    @GetMapping("/getAllQueries")
    public ResponseEntity<?> getAllQueries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(admins.getAllQueries(authentication.getName()), HttpStatus.OK);
    }

}
