package com.twc.guanlang.service;


import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

import java.lang.reflect.Field;


/**
 * @author chenqiang
 */
public class VedioService {

    public static void rtmp() throws FrameGrabber.Exception, FrameRecorder.Exception, NoSuchFieldException, IllegalAccessException {

        final int captureWidth = 1280;
        final int captureHeight = 720;
        final FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("rtsp://admin:admin123@192.168.1.63:554/mainstream");
        grabber.setImageWidth(captureWidth);
        grabber.setImageHeight(captureHeight);
        // rtsp格式一般添加TCP配置，否则丢帧会比较严重
        // Brick在测试过程发现，该参数改成udp可以解决部分电脑出现的下列报警，但是丢帧比较严重
        // av_interleaved_write_frame() error -22 while writing interleaved video packet.
        grabber.setOption("rtsp_transport", "tcp");
        grabber.start();
        // 最后一个参数是AudioChannels，建议通过grabber获取
        //rtmp://open.stratosphere.mobi:1935/live/1
        //rtmp://192.168.1.130:554/machineHost
        final FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("rtmp://open.stratosphere.mobi:1935/live/1", captureWidth, captureHeight, 1);
        recorder.setInterleaved(true);
        // 降低编码延时
        recorder.setVideoOption("tune", "zerolatency");
        // 提升编码速度
        recorder.setVideoOption("preset", "ultrafast");
        // 视频质量参数(详见 https://trac.ffmpeg.org/wiki/Encode/H.264)
        recorder.setVideoOption("crf", "28");
        // 分辨率
        recorder.setVideoBitrate(2000000);
        // 视频编码格式
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // 视频格式
        recorder.setFormat("flv");
        // 视频帧率
        recorder.setFrameRate(15);
        recorder.setGopSize(60);
        recorder.setAudioOption("crf", "0");
        recorder.setAudioQuality(0);
        recorder.setAudioBitrate(192000);
        recorder.setSampleRate(44100);
        // 建议从grabber获取AudioChannels
        recorder.setAudioChannels(1);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        recorder.start();

        // 解决音视频同步导致的延时问题
        Field field = recorder.getClass().getDeclaredField("oc");
        field.setAccessible(true);
        AVFormatContext oc = (AVFormatContext) field.get(recorder);
        oc.max_interleave_delta(100);

//        // 用来测试的frame窗口
//        final CanvasFrame cFrame = new CanvasFrame("frame");
//        Frame capturedFrame = null;
//
//        // 有些时候，程序执行回报下列错误，添加一行代码解决此问题
//        // av_interleaved_write_frame() error -22 while writing interleaved video packet.
//        grabber.flush();
//
//        while ((capturedFrame = grabber.grab()) != null) {
//            if (cFrame.isVisible()) {
//                cFrame.showImage(capturedFrame);
//            }
//            System.out.println(grabber.getFrameNumber() + "--" + capturedFrame.timestamp);
//            recorder.setTimestamp(capturedFrame.timestamp);
//            recorder.record(capturedFrame);
//        }
//        cFrame.dispose();
        recorder.close();
        grabber.close();

    }

    static boolean exit  = false;
    int audioRecord =0; // 0 = 不录制，1=录制
    boolean saveVideo = false;
    public static void push(String rtmpPath, String rtspPath, int audioRecord, boolean saveVideo) throws Exception {
        // 使用rtsp的时候需要使用 FFmpegFrameGrabber，不能再用 FrameGrabber
        int width = 640, height = 480;
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtspPath);
        grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式，不然会丢包很严重

        grabber.setImageWidth(width);
        grabber.setImageHeight(height);
        System.out.println("grabber start");
        grabber.start();
        // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(rtmpPath, width, height, audioRecord);
        recorder.setInterleaved(true);
        //recorder.setVideoOption("crf","28");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // 28
        recorder.setFormat("flv"); // rtmp的类型
        recorder.setFrameRate(25);
        recorder.setImageWidth(width);
        recorder.setImageHeight(height);
        recorder.setPixelFormat(0); // yuv420p
        System.out.println("recorder start");
        recorder.start();
        //
        OpenCVFrameConverter.ToIplImage conveter = new OpenCVFrameConverter.ToIplImage();
        System.out.println("all start!!");
        int count = 0;
        while (!exit) {
            count++;
            Frame frame = grabber.grabImage();
            if (frame == null) {
                continue;
            }
            if (count % 100 == 0) {
                System.out.println("count=" + count);
            }
            recorder.record(frame);
        }

        grabber.stop();
        grabber.release();
        recorder.stop();
        recorder.release();
    }
}

