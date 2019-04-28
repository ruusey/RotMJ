package com.util;
import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.engine.Window;

public class Input {


private long window;
private boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
private boolean[] mouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
private float mouseWheelVelocity = 0;


public Input(Window win) {
    super();
    this.window = win.getWindowHandle();
}

public void init() {

    GLFW.glfwSetScrollCallback(window, (long win, double dx, double dy) -> {
        System.out.println(dy);
        setMouseWheelVelocity((float) dy);


    });
}


public boolean isKeyDown(int keyCode) {

    return GLFW.glfwGetKey(window, keyCode) == 1;

}

public boolean isMouseDown(int mouseButton) {

    return GLFW.glfwGetMouseButton(window, mouseButton) == 1;

}

public boolean isKeyPressed(int keyCode) {

    return isKeyDown(keyCode) && !keys[keyCode];
}

public boolean isKeyReleased(int keyCode) {

    return !isKeyDown(keyCode) && keys[keyCode];

}

public boolean isMousePressed(int mouseButton) {

    return isMouseDown(mouseButton) && !mouseButtons[mouseButton];

}

public boolean isMouseReleased(int mouseButton) {

    return !isMouseDown(mouseButton) && mouseButtons[mouseButton];

}

public double getMouseX() {
    DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
    GLFW.glfwGetCursorPos(window, buffer, null);
    return buffer.get(0);
}

public double getMouseY() {
    DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
    GLFW.glfwGetCursorPos(window, null, buffer);
    return buffer.get(0);

}

public float getMouseWheelVelocity() {
    return mouseWheelVelocity;
}

public void setMouseWheelVelocity(float mouseWheelVelocity) {
    this.mouseWheelVelocity = mouseWheelVelocity;
}

public void updateInput() {
    for (int i = 0; i < GLFW.GLFW_KEY_LAST; i++) {
        keys[i] = isKeyDown(i);
    }
    for (int i = 0; i < GLFW.GLFW_MOUSE_BUTTON_LAST; i++) {
        mouseButtons[i] = isMouseDown(i);
    }
}

public void currentKeyBind() {
    if(isKeyPressed(GLFW.GLFW_KEY_Q)) {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }
    if(isKeyReleased(GLFW.GLFW_KEY_Q)) {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    }
}

}