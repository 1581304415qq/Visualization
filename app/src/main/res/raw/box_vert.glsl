#version 300 es

//attribute vec4 vPosition;
layout (location = 0) in vec4 vPosition;

uniform mat4 uMVPMatrix;
uniform mat4 viewPos;

void main() {
    gl_Position = vPosition;
//    gl_Position = uMVPMatrix * viewPos * vPosition;
}