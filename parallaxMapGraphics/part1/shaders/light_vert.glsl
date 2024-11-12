#version 410 core
layout(location=0) in vec3 position;

uniform mat4 u_MVP;

void main() {
  vec4 newPos = u_MVP * vec4(position, 1.0f);

  // computer MVP matrix for light
  gl_Position = vec4(newPos.x, newPos.y, newPos.z, newPos.w);
}