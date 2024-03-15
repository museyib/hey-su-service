/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.heysu.controller.v2;

import az.inci.heysu.model.LoginRequest;
import az.inci.heysu.model.Response;
import az.inci.heysu.model.User;
import az.inci.heysu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.inci.heysu.Utilities.getMessage;

/**
 * @author User
 */
@RequestMapping("/v2/user")
@RestController
@Slf4j
public class UserControllerV2
{
    private UserService service;

    @Autowired
    public void setService(UserService service)
    {
        this.service = service;
    }

    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> all()
    {
        try
        {
            List<User> result = service.all();
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> login(@RequestBody LoginRequest request)
    {
        try
        {
            boolean b = service.login(request.getUserId(), request.getPassword());
            if(b)
            {
                User result = service.getById(request.getUserId());
                return ResponseEntity.ok(Response.getResultResponse(result));
            }
            else
            {
                return ResponseEntity.ok(Response.getUserErrorResponse("İstifadəçi adı və ya şifrə yanlışdır."));
            }
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
}
