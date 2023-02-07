#version 300 es

precision mediump float;

uniform vec4 vColor;

out vec4 fragColor;


void main() {
    fragColor = vColor;
//    fragColor = vec4(0.233,0.7633,0.453,1.0);
}
