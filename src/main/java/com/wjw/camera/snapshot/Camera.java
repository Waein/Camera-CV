package com.wjw.camera.snapshot;

/**
 * ===================================
 * Created With IntelliJ IDEA
 *
 * @author Waein :)
 * @version method: Camera, v 0.1  相机类:主要是查看摄像头状态
 * @CreateDate 2018/11/7
 * @CreateTime 13:28
 * @GitHub https://github.com/Waein
 * ===================================
 */
public class Camera {

    private Boolean state = true;

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

}
