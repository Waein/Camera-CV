package com.wjw.camera.snapshot;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ===================================
 * Created With IntelliJ IDEA
 *
 * @author Waein :)
 * @version method: TakePhotoMouseAdapter, v 0.1 拍照鼠标适配器
 * @CreateDate 2018/11/7
 * @CreateTime 13:26
 * @GitHub https://github.com/Waein
 * ===================================
 */
public class TakePhotoMouseAdapter extends MouseAdapter {

    private JButton jButton;
    private Camera camera;

    public TakePhotoMouseAdapter(JButton jButton, Camera camera) {
        this.jButton = jButton;
        this.camera = camera;
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        System.out.println("拍照");
        // 修改显示
        if (camera.getState()) {
            jButton.setText("继续拍照");
            // 暂停拍照
            camera.setState(false);
        } else {
            jButton.setText("拍照");
            // 继续拍照
            camera.setState(true);
        }
    }
}
