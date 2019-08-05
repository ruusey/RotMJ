package com.engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.engine.MouseInput;
import com.engine.graph.world.Player;
import com.util.Input;

public class Camera {
	private float distanceFromPlayer = 30;
	private float angleAroundPlayer = 180;

	private float pitch = 15;
	private float yaw;
	private float roll;
	private final Vector3f position;

	private final Vector3f rotation;

	private Matrix4f viewMatrix;
	private Player player;

	public Camera() {
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		viewMatrix = new Matrix4f();
	}

	public Camera(Player player) {
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		viewMatrix = new Matrix4f();
		this.player = player;
	}

	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}

	public void move(MouseInput input) {
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

	private void calculateZoom(MouseInput input) {
		float zoomLevel = input.getMouseWheelVelocity() * 0.1f;
		distanceFromPlayer -= zoomLevel;

	}

	private void calculatePitch(MouseInput input) {
		if (input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
			// possible issue
			float pitchChange = (float) (input.getDisplVec().x);
			pitch -= pitchChange;
		}
	}

	private void calculateAngleAroundPlayer(MouseInput input) {
		if (input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_2)) {
			float angleChange = (float) (input.getDisplVec().y);
			angleAroundPlayer -= angleChange;
		}
	}

	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public Matrix4f updateViewMatrix() {
		return Transformation.updateGenericViewMatrix(position, rotation, viewMatrix);
	}

	public Matrix4f updateViewMatrix(Matrix4f i) {
		return Transformation.updateGenericViewMatrix(position, rotation, viewMatrix);
	}

	public void movePosition(float offsetX, float offsetY, float offsetZ) {
		if (offsetZ != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
			position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
		}
		if (offsetX != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
			position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
		}
		position.y += offsetY;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
	}

	public void moveRotation(float offsetX, float offsetY, float offsetZ) {
		rotation.x += offsetX;
		rotation.y += offsetY;
		rotation.z += offsetZ;
	}
}