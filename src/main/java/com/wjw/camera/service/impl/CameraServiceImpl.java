package com.wjw.camera.service.impl;

import com.wjw.camera.service.CameraService;
import com.wjw.camera.snapshot.Camera;
import com.wjw.camera.snapshot.SavePhotoMouseAdapter;
import com.wjw.camera.snapshot.TakePhotoMouseAdapter;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ===================================
 * Created With IntelliJ IDEA
 *
 * @author Waein :)
 * @version method: CameraServiceImpl, v 0.1
 * @CreateDate 2018/11/7
 * @CreateTime 14:50
 * @GitHub https://github.com/Waein
 * ===================================
 */
@Service
public class CameraServiceImpl implements CameraService {

    @Override
    public void cameraSnapshot() throws Exception {

        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.setImageWidth(800);
        grabber.setImageHeight(640);
        grabber.start();

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        IplImage grabbedImage = null;
        grabbedImage = converter.convertToIplImage(grabber.grab());
        CanvasFrame frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        frame.setSize(800, 600);
        frame.setBounds(200, 100, 640, 640);
        // 设置操作界面
        JPanel contentPane = new JPanel();
        contentPane.setBounds(0, 0, 640, 640);
        Container contentPane2 = frame.getContentPane();

        JButton take_photo = new JButton("拍照");
        JButton save_photo = new JButton("保存");
        JButton cancle = new JButton("关闭");
        Camera camera = new Camera();
        // 监听拍摄
        take_photo.addMouseListener(new TakePhotoMouseAdapter(take_photo, camera));
        // 监听保存
        save_photo.addMouseListener(new SavePhotoMouseAdapter(grabbedImage));
        // 关闭
        cancle.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                frame.setVisible(false);
            }
        });
        // 添加按钮
        contentPane.add(take_photo, BorderLayout.SOUTH);
        contentPane.add(save_photo, BorderLayout.SOUTH);
        contentPane.add(cancle, BorderLayout.SOUTH);
        // 添加面板
        contentPane2.add(contentPane, BorderLayout.SOUTH);
        // 操作状态
        while (frame.isVisible()) {
            // 获取图像
            if (camera.getState()) {
                grabbedImage = converter.convert(grabber.grab());
            }
            frame.showImage(converter.convert(grabbedImage));
            // 每40毫秒刷新视频,一秒25帧
            Thread.sleep(40);
        }

        frame.dispose();

        grabber.stop();
    }

    @Override
    public void cameraRecorde() throws Exception {
//        System.out.println(Loader.load(opencv_objdetect.class));// Preload the opencv_objdetect module to work around a known bug.

        FrameGrabber grabber = FrameGrabber.createDefault(0);
        grabber.start();
        Frame grabbedImage = grabber.grab();//抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
        int width = grabbedImage.imageWidth;
        int height = grabbedImage.imageHeight;

        String outputFile = "/Users/SeungRi/opt/video/record.mp4";
        FrameRecorder recorder = FrameRecorder.createDefault(outputFile, width, height); //org.bytedeco.javacv.FFmpegFrameRecorder
//        System.out.println(recorder.getClass().getName());//org.bytedeco.javacv.FFmpegFrameRecorder
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);// avcodec.AV_CODEC_ID_H264，编码
        recorder.setFormat("flv");//封装格式，如果是推送到rtmp就必须是flv封装格式
        recorder.setFrameRate(25);
        recorder.start();//开启录制器
        long startTime = 0;
        long videoTS;
        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma()); //2.2/2.2=1
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        Frame rotatedFrame;
        while (frame.isVisible() && (rotatedFrame = grabber.grab()) != null) {
            frame.showImage(rotatedFrame);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            videoTS = (System.currentTimeMillis() - startTime) * 1000;//这里要注意，注意位
            recorder.setTimestamp(videoTS);
            recorder.record(rotatedFrame);
            Thread.sleep(40);
        }
        recorder.stop();
        recorder.release();
        frame.dispose();
        grabber.stop();
        grabber.close();
    }
}
