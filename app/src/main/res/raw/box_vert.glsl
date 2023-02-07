#version 300 es

layout (location = 0) in vec3 vPosition;

uniform mat4 projection;
uniform mat4 viewPos;

out vec3 pos;
void main() {
//    gl_Position = vec4(vPosition, 1.0);
//    gl_Position = viewPos * vec4(vPosition, 1.0);
    pos = vPosition;
    gl_Position = projection * viewPos * vec4(vPosition, 1.0);
}