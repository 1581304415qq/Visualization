#version 300 es

precision mediump float;

uniform vec4 vColor;

out vec4 fragColor;
in vec3 pos;

void main() {
    fragColor = vColor * vec4(pos,0.8);
//    fragColor = vec4(0.233,0.7633,0.453,1.0);
}
