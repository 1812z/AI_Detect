package com.z1812.ai_detect.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ImageCapture {

    private final OkHttpClient httpClient;

    public ImageCapture() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 从 HTTP URL 下载图片
     */
    public byte[] captureFromHttp(String imageUrl) throws IOException {
        Request request = new Request.Builder()
                .url(imageUrl)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP 请求失败: " + response.code() + " - " + response.message());
            }

            byte[] imageBytes = response.body().bytes();

            // 验证是否为有效图片
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image == null) {
                throw new IOException("下载的内容不是有效的图片格式");
            }

            // 转换为 JPEG 格式
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] jpegBytes = baos.toByteArray();

            log.info("成功从 HTTP 下载图片: {}, 大小: {} bytes", imageUrl, jpegBytes.length);
            return jpegBytes;
        }
    }
}
