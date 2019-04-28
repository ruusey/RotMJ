package com.util;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.engine.graph.world.Player;

public class Camera {

private float distanceFromPlayer = -6;
private float angleAroundPlayer = 0;

private Vector3f position = new Vector3f(0,0,0);
private float pitch = 20;
private float yaw;
private float roll;

private Player player;

public Camera(Player player) {

    this.player = player;

}

public void move(Input input) {
    calculateZoom(input);
    calculatePitch(input);
    calculateAngleAroundPlayer(input);
    float horizontalDistance = calculateHorizontalDistance();
    float verticalDistance = calculateVerticalDistance();
    calculateCameraPosition(horizontalDistance, verticalDistance);
}

public Vector3f getPosition() {
    return position;
}

public float getPitch() {
    return pitch;
}

public float getYaw() {
    return yaw;
}

public float getRoll() {
    return roll;
}

private void calculateCameraPosition(float horizDistance, float verticDistance) {
    float theta = player.getRotation().y + angleAroundPlayer;
    float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
    float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
    position.x = player.getPosition().x - offsetX;
    position.z = player.getPosition().z - offsetZ;
    position.y = player.getPosition().y + verticDistance;
    this.yaw = 180 - (player.getRotation().y + angleAroundPlayer);
}

private float calculateHorizontalDistance() {
    return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
}

private float calculateVerticalDistance() {
    return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
}

private void calculateZoom(Input input) {
    float zoomLevel = input.getMouseWheelVelocity() * 0.1f;
    distanceFromPlayer -= zoomLevel;

}

private void calculatePitch(Input input) {
    if(input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
        //possible issue
        float pitchChange = (float) (input.getMouseY() * 0.01f);
        pitch -= pitchChange;
    }
}

private void calculateAngleAroundPlayer(Input input) {
    if(input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_2)) {
        float angleChange = (float) (input.getMouseX() * 0.01f);
        angleAroundPlayer -= angleChange;
    }
}
}