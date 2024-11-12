#include "Light.hpp"
#include <vector>
#include "Camera.hpp"

Light::Light() : mVAO(0), mVBO(0), mPosition(0.0f, 0.0f, 0.0f)
{
}

Light::~Light()
{
  glDeleteBuffers(1, &mVBO);
  glDeleteVertexArrays(1, &mVAO);
}

void Light::Initialize()
{
  const std::vector<GLfloat> vertices{
      -0.1f,
      -0.1f,
      -0.1f, // Back face
      0.1f,
      -0.1f,
      -0.1f,
      0.1f,
      0.1f,
      -0.1f,
      0.1f,
      0.1f,
      -0.1f,
      -0.1f,
      0.1f,
      -0.1f,
      -0.1f,
      -0.1f,
      -0.1f,

      -0.1f,
      -0.1f,
      0.1f, // Front face
      0.1f,
      -0.1f,
      0.1f,
      0.1f,
      0.1f,
      0.1f,
      0.1f,
      0.1f,
      0.1f,
      -0.1f,
      0.1f,
      0.1f,
      -0.1f,
      -0.1f,
      0.1f,

      -0.1f,
      0.1f,
      0.1f, // Top face
      -0.1f,
      0.1f,
      -0.1f,
      0.1f,
      0.1f,
      -0.1f,
      0.1f,
      0.1f,
      -0.1f,
      0.1f,
      0.1f,
      0.1f,
      -0.1f,
      0.1f,
      0.1f,

      -0.1f,
      -0.1f,
      0.1f, // Bottom face
      -0.1f,
      -0.1f,
      -0.1f,
      0.1f,
      -0.1f,
      -0.1f,
      0.1f,
      -0.1f,
      -0.1f,
      0.1f,
      -0.1f,
      0.1f,
      -0.1f,
      -0.1f,
      0.1f,

      0.1f,
      -0.1f,
      -0.1f, // Right face
      0.1f,
      0.1f,
      -0.1f,
      0.1f,
      0.1f,
      0.1f,
      0.1f,
      0.1f,
      0.1f,
      0.1f,
      -0.1f,
      0.1f,
      0.1f,
      -0.1f,
      -0.1f,

      -0.1f,
      -0.1f,
      -0.1f, // Left face
      -0.1f,
      0.1f,
      -0.1f,
      -0.1f,
      0.1f,
      0.1f,
      -0.1f,
      0.1f,
      0.1f,
      -0.1f,
      -0.1f,
      0.1f,
      -0.1f,
      -0.1f,
      -0.1f,
  };
  // VAO setup
  glGenVertexArrays(1, &mVAO);
  // bind the VAO
  glBindVertexArray(mVAO);
  // VBO setup
  glGenBuffers(1, &mVBO);
  glBindBuffer(GL_ARRAY_BUFFER, mVBO);
  glBufferData(GL_ARRAY_BUFFER,                    // Kind of buffer we are working with
               vertices.size() * sizeof(GL_FLOAT), // Size of data in bytes
               vertices.data(),                    // Raw array of data
               GL_STATIC_DRAW);
  // Position information (x,y,z)
  glEnableVertexAttribArray(0);
  glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, (void *)0);
  // Unbind our currently bound Vertex Array Object
  glBindVertexArray(0);
  // Unbind our currently bound Vertex Buffer Object
  glBindBuffer(GL_ARRAY_BUFFER, 0);

  // Disable any attributes we opened in our Vertex Attribute Arrray,
  // as we do not want to leave them open.
  glDisableVertexAttribArray(0);

  // Load shaders
  std::string vertexShaderSource = mShader.LoadShader("./shaders/light_vert.glsl");
  std::string fragmentShaderSource = mShader.LoadShader("./shaders/light_frag.glsl");
  mShader.CreateShader(vertexShaderSource, fragmentShaderSource);
}

void Light::Update(float deltaTime, glm::vec3 objectPosition)
{
    static float angle = 0.0f;
    float rotationSpeed = 0.5f; // This controls the speed of the rotation
    float radius = 4.0f;        // This is the radius of the orbit

    angle += rotationSpeed * deltaTime; // Increment angle
    if (angle > 2 * M_PI) {
        angle -= 2 * M_PI; // Reset angle after completing a full circle
    }

    // Update light's position
    mPosition.x = objectPosition.x + cos(angle) * radius; // Circular path around the object's x
    mPosition.y = objectPosition.y; // Maintain constant elevation relative to the object's y
    mPosition.z = objectPosition.z + sin(angle) * radius; // Circular path around the object's z
}


void Light::Render(unsigned int screenWidth, unsigned int screenHeight)
{
  mShader.Bind();
  glBindVertexArray(mVAO);
  // Set uniforms like the transformation matrices
  glm::mat4 model = glm::translate(glm::mat4(1.0f), mPosition);
  model = glm::scale(model, glm::vec3(2.0f, 2.0f, 2.0f));  // Apply scaling
  glm::mat4 view = Camera::Instance().GetWorldToViewmatrix();
  glm::mat4 perspective = glm::perspective(glm::radians(45.0f), (float)screenWidth / (float)screenHeight,
                                           0.1f,
                                           1000.0f);
  glm::mat4 mvp = perspective * view * model;
  mShader.SetUniformMatrix4fv("u_MVP", &mvp[0][0]);
  // draw the light obj
  glDrawArrays(GL_TRIANGLES, 0, 36); // cube has 36 vertices (6 faces * 2 tris * 3 verts)
  glBindVertexArray(0);
  mShader.Unbind();
}


glm::vec3 Light::GetPosition() {
  return this->mPosition;
}