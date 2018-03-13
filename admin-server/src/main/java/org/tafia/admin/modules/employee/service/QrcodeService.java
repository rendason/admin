package org.tafia.admin.modules.employee.service;

/**
 * Created by Dason on 2018/1/13.
 */
public interface QrcodeService {

    byte[] generate(String content);

    byte[] generateSigninQrcode(String basePath);
}
