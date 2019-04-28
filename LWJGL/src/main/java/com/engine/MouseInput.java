package com.engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private final Vector2d previousPos;

    private final Vector2d currentPos;

    private final Vector2f displVec;

    private boolean inWindow = false;

    private boolean leftButtonPressed = false;

    private boolean rightButtonPressed = false;
    private long windowHandle;
    private boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    private boolean[] mouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private float mouseWheelVelocity = 0;
    public MouseInput() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public void init(Window window) {
    	this.windowHandle=window.getWindowHandle();
        glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
        	
            currentPos.x = xpos;
            currentPos.y = ypos;
            input(window);
        });
        glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
        GLFW.glfwSetScrollCallback(window.getWindowHandle(), (long win, double dx, double dy) -> {
            System.out.println(dy);
            setMouseWheelVelocity((float) dy);


        });
    }

    public Vector2f getDisplVec() {
        return displVec;
    }
    public Vector2d getLastPos() {
        return previousPos;        
    }
    public Vector2d getCurrentPos() {
        return currentPos;        
    }
    public float getMouseWheelVelocity() {
        return mouseWheelVelocity;
    }

    public void setMouseWheelVelocity(float mouseWheelVelocity) {
        this.mouseWheelVelocity = mouseWheelVelocity;
    }
    public void input(Window window) {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
    public boolean isKeyDown(int keyCode) {

        return GLFW.glfwGetKey(windowHandle, keyCode) == 1;

    }

    public boolean isMouseDown(int mouseButton) {

        return GLFW.glfwGetMouseButton(windowHandle, mouseButton) == 1;

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
        GLFW.glfwGetCursorPos(windowHandle, buffer, null);
        return buffer.get(0);
    }

    public double getMouseY() {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(windowHandle, null, buffer);
        return buffer.get(0);

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