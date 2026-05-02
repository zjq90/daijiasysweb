package com.daijia.controller.client;

import com.daijia.common.Result;
import com.daijia.dto.LoginDto;
import com.daijia.entity.Client;
import com.daijia.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Api(tags = "客户端-账号管理")
@RestController
@RequestMapping("/api/client/account")
@RequiredArgsConstructor
public class ClientAccountController {
    
    private final ClientService clientService;
    
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDto dto) {
        return Result.success(clientService.login(dto));
    }
    
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("token") String token) {
        Client client = clientService.getClientByToken(token);
        clientService.logout(client.getId());
        return Result.success();
    }
    
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<Client> getInfo(@RequestHeader("token") String token) {
        Client client = clientService.getClientByToken(token);
        client.setPassword(null);
        client.setLoginToken(null);
        return Result.success(client);
    }
    
    @ApiOperation("更新用户信息")
    @PostMapping("/update")
    public Result<Client> updateInfo(
            @RequestHeader("token") String token,
            @RequestBody Map<String, Object> params) {
        Client client = clientService.getClientByToken(token);
        Client updated = clientService.updateProfile(client.getId(), params);
        updated.setPassword(null);
        updated.setLoginToken(null);
        return Result.success(updated);
    }
}
