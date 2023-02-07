#version 300 es

layout (location = 0) in vec4 vPosition;

uniform mat4 projection;
uniform mat4 viewPos;
uniform mat4 model;

out vec3 pos;
void main() {
//    gl_Position = vPosition;
//    gl_Position = viewPos * vPosition;
    pos = vPosition.xyz;
//    gl_Position = projection * viewPos * vPosition;
    gl_Position = projection * viewPos * model * vPosition;
}