package com.z1812.ai_detect.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoCapture {

    private final ImageCapture imageCapture;

    public byte[] captureFrame(String streamUrl) throws Exception {
        // 判断是 HTTP 图片 URL 还是视频流
        if (streamUrl.startsWith("http://") || streamUrl.startsWith("https://")) {
            if (isImageUrl(streamUrl)) {
                log.info("检测到 HTTP 图片URL，使用图片下载方式");
                try {
                    return imageCapture.captureFromHttp(streamUrl);
                } catch (Exception e) {
                    log.warn("HTTP 图片下载失败，尝试使用视频流方式: {}", e.getMessage());
                    return captureFromVideoStream(streamUrl);
                }
            }
        }

        return captureFromVideoStream(streamUrl);
    }

    private boolean isImageUrl(String url) {
        String lowerUrl = url.toLowerCase();
        return !lowerUrl.contains(".flv") &&
               !lowerUrl.contains(".m3u8") &&
               !lowerUrl.contains("rtsp://") &&
               !lowerUrl.contains("rtmp://");
    }

    private byte[] captureFromVideoStream(String streamUrl) throws Exception {
        FFmpegFrameGrabber grabber = null;
        try {
            grabber = new FFmpegFrameGrabber(streamUrl);
            grabber.setOption("rtsp_transport", "tcp");
            grabber.setImageWidth(1280);
            grabber.setImageHeight(720);
            grabber.start();

            Frame frame = grabber.grabImage();
            if (frame == null) {
                throw new IOException("无法从视频流获取帧");
            }

            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage bufferedImage = converter.convert(frame);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();

            log.info("成功从视频流捕获帧: {}, 图片大小: {} bytes", streamUrl, imageBytes.length);
            return imageBytes;

        } catch (Exception e) {
            log.error("捕获视频帧失败: {}", streamUrl, e);
            throw e;
        } finally {
            if (grabber != null) {
                try {
                    grabber.stop();
                    grabber.release();
                } catch (Exception e) {
                    log.error("释放资源失败", e);
                }
            }
        }
    }
}
