package com.wjw.camera.snapshot;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ===================================
 * Created With IntelliJ IDEA
 *
 * @author Waein :)
 * @version method: SavePhotoMouseAdapter, v 0.1  保存照片鼠标适配器
 * @CreateDate 2018/11/7
 * @CreateTime 13:24
 * @GitHub https://github.com/Waein
 * ===================================
 */
public class SavePhotoMouseAdapter extends MouseAdapter {

    private IplImage iplImage;

    public SavePhotoMouseAdapter(IplImage iplImage) {
        this.iplImage = iplImage;
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        System.out.println("保存");
        // 保存结果提示框
        JFrame myFrame = new JFrame();
        try {
            if (iplImage != null) {
                // 保存图片
                cvSaveImage("/Users/SeungRi/opt/snapshot/savedImage.jpg", iplImage);
                // 发送修改用户头像请求...也可以直接发送字节数组到服务器，由服务器上传图片并修改用户头像
                JOptionPane.showMessageDialog(myFrame, "上传成功");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(myFrame, "保存失败");
            e.printStackTrace();
        } finally {
            // 关闭提示jframe
            myFrame.dispose();
            myFrame = null;
        }
    }

    public static void cvSaveImage(String path, IplImage image) throws IOException {
        File file = new File(path);
        // ImageIO.write(toBufferedImage(image), "jpg", file);
        // 使用字节保存
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(toBufferedImage(image), "jpg", out);
        byte[] bs = out.toByteArray();

        // 保存字节数组为图片到本地
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bs, 0, bs.length);
        //流关闭
        fos.close();
        out.close();
    }

    /**
     * 通过image获取bufferedImage
     *
     * @param image
     * @return
     */
    public static BufferedImage toBufferedImage(IplImage image) {
        ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();// iplConverter 可以查看convert方法可以转换的对象, Frame IplImage,Mat之间转换
        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();// Java2DFrameConverter 让 Frame和BufferedImage之间相互转换
        BufferedImage bufferedImage = java2dConverter.convert(iplConverter.convert(image));

        return bufferedImage;
    }

}
