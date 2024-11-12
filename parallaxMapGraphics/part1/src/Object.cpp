#include "Object.hpp"
#include "Error.hpp"
#include "Camera.hpp"

Object::Object()
{
        this->blinnOrPhong = false;
        this->normalOrSteepOrOcclusion = 0;
}

Object::~Object()
{
}

// Initialization of object as a 'quad'
//
// This could be called in the constructor or
// otherwise 'explicitly' called this
// so we create our objects at the correct time
void Object::MakeTexturedQuad(std::string fileName)
{

        // Setup geometry
        // We are using a new abstraction which allows us
        // to create triangles shapes on the fly
        // Position and Texture coordinate
        m_geometry.AddVertex(-1.0f, -1.0f, 0.0f, 0.0f, 0.0f);
        m_geometry.AddVertex(1.0f, -1.0f, 0.0f, 1.0f, 0.0f);
        m_geometry.AddVertex(1.0f, 1.0f, 0.0f, 1.0f, 1.0f);
        m_geometry.AddVertex(-1.0f, 1.0f, 0.0f, 0.0f, 1.0f);

        // Make our triangles and populate our
        // indices data structure
        m_geometry.MakeTriangle(0, 1, 2);
        m_geometry.MakeTriangle(2, 3, 0);

        // This is a helper function to generate all of the geometry
        m_geometry.Gen();

        // Create a buffer and set the stride of information
        // NOTE: How we are leveraging our data structure in order to very cleanly
        //       get information into and out of our data structure.
        m_vertexBufferLayout.CreateNormalBufferLayout(m_geometry.GetBufferDataSize(),
                                                      m_geometry.GetIndicesSize(),
                                                      m_geometry.GetBufferDataPtr(),
                                                      m_geometry.GetIndicesDataPtr());

        // Load our actual texture
        // We are using the input parameter as our texture to load
        m_textureDiffuse.LoadTexture(fileName.c_str());

        // Load the normal map texture
        m_normalMap.LoadTexture("./../examples/openGLbricks2_normal.ppm");

        m_heightMap.LoadTexture("./../examples/openGlbrick2_heightmap.ppm");

        // Setup shaders
        std::string vertexShader = m_shader.LoadShader("./shaders/vert.glsl");
        std::string fragmentShader = m_shader.LoadShader("./shaders/frag.glsl");
        // Actually create our shader
        m_shader.CreateShader(vertexShader, fragmentShader);
}

// TODO: In the future it may be good to
// think about loading a 'default' texture
// if the user forgets to do this action!
void Object::LoadTexture(std::string fileName)
{
        // Load our actual textures
        m_textureDiffuse.LoadTexture(fileName);
}

// Bind everything we need in our object
// Generally this is called in update() and render()
// before we do any actual work with our object
void Object::Bind()
{
        // Make sure we are updating the correct 'buffers'
        m_vertexBufferLayout.Bind();
        // Diffuse map is 0 by default, but it is good to set it explicitly
        m_textureDiffuse.Bind(0);
        // We need to set the texture slot explicitly for the normal map
        m_normalMap.Bind(1);
        m_heightMap.Bind(2);
        // Select our appropriate shader
        m_shader.Bind();
}

void Object::Update(unsigned int screenWidth, unsigned int screenHeight, const glm::vec3 &lightPos)
{
        // Call our helper function to just bind everything
        Bind();
        // TODO: Read and understand
        // For our object, we apply the texture in the following way
        // Note that we set the value to 0, because we have bound
        // our texture to slot 0.
        m_shader.SetUniform1i("u_DiffuseMap", 0);
        // If we want to load another texture, we assign it to another slot
        m_shader.SetUniform1i("u_NormalMap", 1);
        m_shader.SetUniform1i("u_HeightMap", 2);
        // Here we apply the 'view' matrix which creates perspective.
        // The first argument is 'field of view'
        // Then perspective
        // Then the near and far clipping plane.
        // Note I cannot see anything closer than 0.1f units from the screen.
        // TODO: In the future this type of operation would be abstracted away
        //       in a camera class.
        m_projectionMatrix = glm::perspective(glm::radians(45.0f), ((float)screenWidth) / ((float)screenHeight), 0.1f, 1000.0f);
        // Update the View Matrix
        // GLint u_ViewMatrixLocation = glGetUniformLocation(, "u_ViewMatrix");
        // if (u_ViewMatrixLocation >= 0)
        // {
        //         glm::mat4 viewMatrix = g.gCamera.GetViewMatrix();
        //         glUniformMatrix4fv(u_ViewMatrixLocation, 1, GL_FALSE, &viewMatrix[0][0]);
        // }
        // else
        // {
        //         std::cout << "Could not find u_ViewMatrix, maybe a mispelling?\n";
        //         exit(EXIT_FAILURE);
        // }
        glm::mat4 viewMatrix = Camera::Instance().GetWorldToViewmatrix();
        // Set the uniforms in our current shader
        m_shader.SetUniformMatrix4fv("viewMatrix", &viewMatrix[0][0]);
        m_shader.SetUniformMatrix4fv("modelTransformMatrix", m_transform.GetTransformMatrix());
        m_shader.SetUniformMatrix4fv("projectionMatrix", &m_projectionMatrix[0][0]);

        // Create a first 'light'
        // Set in a light source position
        m_shader.SetUniform3f("lightPos", lightPos.x, lightPos.y, lightPos.z);
        // Set a view and a vector
        m_shader.SetUniform3f("viewPos", Camera::Instance().GetEyeXPosition(), Camera::Instance().GetEyeYPosition(), Camera::Instance().GetEyeZPosition());
        m_shader.SetUniform1i("blinn", this->blinnOrPhong);
        m_shader.SetUniform1i("normalSteepOrOcclusion", this->normalOrSteepOrOcclusion);
}

// Render our geometry
void Object::Render()
{
        // Call our helper function to just bind everything
        Bind();
        // Render data
        glDrawElements(GL_TRIANGLES,
                       m_geometry.GetIndicesSize(), // The number of indices, not triangles.
                       GL_UNSIGNED_INT,             // Make sure the data type matches
                       nullptr);                    // Offset pointer to the data.
                                                    // nullptr because we are currently bound
}

// Returns the actual transform stored in our object
// which can then be modified
Transform &Object::GetTransform()
{
        return m_transform;
}

void Object::toggleBlinnOrPhong()
{
        std::cout << "Setting the illumination to: ";
        if (this->blinnOrPhong) std::cout << "Phong Illumination\n";
        else std::cout << "Blinn-Phong Illumination\n";
        this->blinnOrPhong = !this->blinnOrPhong;
}

/**
 * If textureMappingID == 0 -> Normal Mapping
 * If textureMappingID == 1 -> Steep Parallax Mapping
 * If textureMappingID == 2 -> Parallax Occlusion Mapping
*/
void Object::setNormalOrSteepOrOcclusion(int textureMappingID)
{
        this->normalOrSteepOrOcclusion = textureMappingID;
        std::cout << "Setting the texture mapping to: ";
        if (this->normalOrSteepOrOcclusion == 0) std::cout << "Normal Mapping\n";  
        else if (this->normalOrSteepOrOcclusion == 1) std::cout << "Steep Parallax Mapping\n";  
        else std::cout << "Parallax Occlusion Mapping\n";
}