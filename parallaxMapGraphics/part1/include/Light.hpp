#ifndef LIGHT_HPP
#define LIGHT_HPP

#include "glm/vec3.hpp"
#include "glm/gtc/matrix_transform.hpp"
#include <glad/glad.h>
#include "Shader.hpp"

class Light {
public:
    Light();
    ~Light();

    void Initialize();
    void Update(float deltaTime, glm::vec3 objectPosition);
    void Render(unsigned int screenWidth, unsigned int screenHeight);
    glm::vec3 GetPosition();
private:
    GLuint mVAO, mVBO;
    Shader mShader;
    glm::vec3 mPosition;
};

#endif
