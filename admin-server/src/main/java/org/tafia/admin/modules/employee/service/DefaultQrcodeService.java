package org.tafia.admin.modules.employee.service;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.tafia.admin.modules.employee.dao.UserDao;
import org.tafia.admin.modules.employee.model.User;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dason on 2018/1/13.
 */
@Service
public class DefaultQrcodeService implements QrcodeService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private UserDao userDao;

    @Value("${spring.qrcode.width:300}")
    private int width;

    @Value("${spring.qrcode.height:300}")
    private int height;

    @Value("${spring.qrcode.format:png}")
    private String format;

    private Map<EncodeHintType, Object> hints;

    {
        Map<EncodeHintType, Object> configs = new HashMap<>();
        configs.put(EncodeHintType.CHARACTER_SET, "utf-8");// 字符编码
        configs.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 容错等级 L、M、Q、H 其中 L 为最低, H 为最高
        configs.put(EncodeHintType.MARGIN, 2);// 二维码与图片边距
        hints = Collections.unmodifiableMap(configs);
    }

    public DefaultQrcodeService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public byte[] generate(String content) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.warn("Error occurs on generating qrcode, content is {}", content, e);
            return new byte[0];
        }
    }

    @Override
    public byte[] generateSigninQrcode(String basePath) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);
        JSONObject params = new JSONObject()
                .fluentPut("userId", user.getId())
                .fluentPut("nonce", RandomUtils.nextInt(100_000, 1_000_000))
                .fluentPut("timestamp", System.currentTimeMillis());
        String token = Base64.getEncoder().encodeToString(params.toJSONString().getBytes());
        String url = basePath + token;
        return generate(url);
    }
}
