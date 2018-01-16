package org.tafia.admin.modules.employee.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tafia.admin.modules.common.util.RequestUtil;
import org.tafia.admin.modules.employee.service.QrcodeService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Dason on 2018/1/13.
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private QrcodeService qrcodeService;

    public EmployeeController(QrcodeService qrcodeService) {
        this.qrcodeService = qrcodeService;
    }

    @GetMapping(value = "/signin-qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource signinQrcode(HttpServletRequest request) {
        String signInUrl = RequestUtil.getBasePath(request) + "/employee/signin/";
        return new ByteArrayResource(qrcodeService.generateSigninQrcode(signInUrl));
    }

    @GetMapping("/signin/{token}")
    private Map<String, Object> signin(@PathVariable("token") String token) {
        return Collections.emptyMap();
    }


}
