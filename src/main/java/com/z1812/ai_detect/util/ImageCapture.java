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
                .connectTimeout(15, TimeUnit.SECONDS)  // 增加连接超时
                .readTimeout(60, TimeUnit.SECONDS)     // 增加读取超时到 60 秒
                .writeTimeout(15, TimeUnit.SECONDS)
                .followRedirects(true)                 // 允许重定向
                .build();
    }

    /**
     * 从 HTTP URL 下载图片
     */
    public byte[] captureFromHttp(String imageUrl) throws IOException {
        log.info("开始从 HTTP URL 下载图片: {}", imageUrl);

        Request request = new Request.Builder()
                .url(imageUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .header("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Connection", "keep-alive")
                .build();

        long startTime = System.currentTimeMillis();

        try (Response response = httpClient.newCall(request).execute()) {
            long requestTime = System.currentTimeMillis() - startTime;
            log.info("HTTP 请求完成，耗时: {}ms, 状态码: {}", requestTime, response.code());

            if (!response.isSuccessful()) {
                throw new IOException("HTTP 请求失败: " + response.code() + " - " + response.message());
            }

            // 检查 Content-Type
            String contentType = response.header("Content-Type");
            log.info("Content-Type: {}", contentType);

            byte[] imageBytes = response.body().bytes();
            log.info("下载完成，数据大小: {} bytes", imageBytes.length);

            // 验证是否为有效图片
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image == null) {
                log.error("下载的内容不是有效的图片格式，Content-Type: {}, 数据大小: {}",
                         contentType, imageBytes.length);
                throw new IOException("下载的内容不是有效的图片格式");
            }

            log.info("图片解析成功，尺寸: {}x{}", image.getWidth(), image.getHeight());

            // 转换为 JPEG 格式
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] jpegBytes = baos.toByteArray();

            log.info("成功从 HTTP 下载图片: {}, 原始大小: {} bytes, JPEG大小: {} bytes",
                    imageUrl, imageBytes.length, jpegBytes.length);
            return jpegBytes;

        } catch (IOException e) {
            log.error("从 HTTP URL 下载图片失败: {}, 错误: {}", imageUrl, e.getMessage(), e);
            throw e;
        }
    }
}
