// ==================================================================
#version 330 core

out vec4 FragColor;

// Texture coordinates and tangent space vectors from the vertex shader.
in vec2 v_texCoord;
in vec3 TangentLightPos;
in vec3 TangentViewPos;
in vec3 TangentFragPos;
in vec3 FragPos;

// Texture samplers for the diffuse color and normal map.
uniform sampler2D u_DiffuseMap; 
uniform sampler2D u_NormalMap;
uniform sampler2D u_HeightMap;
uniform bool blinn; //whether we are applying the Blinn-Phong illumination
uniform int normalSteepOrOcclusion; // whether we are applying Steep Parallax Mapping or Parallax Occlusion Mapping
struct PointLight {
    vec3 position;
    vec3 color;
    float ambientIntensity;
    float specularStrength;
    float constant;
    float linear;
    float quadratic;
};
uniform vec3 lightPos; // Our light source position from where light is hitting this object
// uniform PointLight pointLight;



// Simple lighting calculation for demonstration.
vec3 CalculateLighting(vec3 normal, vec3 lightDir, vec3 viewDir) {
    // Ambient component
    float ambientStrength = 0.9;
    vec3 ambient = ambientStrength * vec3(0.85, 0.96, 0.65);  // Simple yellow light

    // Diffuse component
    float diff = max(dot(lightDir, normal), 0.0);
    vec3 diffuse = diff * vec3(0.85, 0.96, 0.65);  // Assuming yellow light

    // Specular component (simple Phong for demonstration)
    float specularStrength = 0.5;
    // vec3 reflectDir = reflect(-lightDir, normal); // uncomment for Phong (but comment below)
    float spec = 0.0;
    if(blinn)
    {
        vec3 halfwayDir = normalize(lightDir + viewDir);  
        spec = pow(max(dot(normal, halfwayDir), 0.0), 32.0);
    }
    else
    {
        vec3 reflectDir = reflect(-lightDir, normal);
        spec = pow(max(dot(viewDir, reflectDir), 0.0), 8.0);
    }
    vec3 specular = specularStrength * spec * vec3(0.85, 0.96, 0.65);

    return (ambient + diffuse + specular);
}

vec2 SteepParallaxMapping(vec2 texCoords, vec3 viewDir)
{ 
    float heightScale = 0.1f;
    // number of depth layers
    const float minLayers = 8;
    const float maxLayers = 32;
    float numLayers = mix(maxLayers, minLayers, abs(dot(vec3(0.0, 0.0, 1.0), viewDir)));  
    // calculate the size of each layer
    float layerDepth = 1.0 / numLayers;
    // depth of current layer
    float currentLayerDepth = 0.0;
    // the amount to shift the texture coordinates per layer (from vector P)
    vec2 P = viewDir.xy / viewDir.z * heightScale; 
    vec2 deltaTexCoords = P / numLayers;
  
    // get initial values
    vec2  currentTexCoords     = texCoords;
    float currentDepthMapValue = texture(u_HeightMap, currentTexCoords).r;
      
    while(currentLayerDepth < currentDepthMapValue)
    {
        // shift texture coordinates along direction of P
        currentTexCoords -= deltaTexCoords;
        // get depthmap value at current texture coordinates
        currentDepthMapValue = texture(u_HeightMap, currentTexCoords).r;  
        // get depth of next layer
        currentLayerDepth += layerDepth;  
    }
    
    return currentTexCoords;
}

vec2 ParallaxMapping(vec2 texCoords, vec3 viewDir)
{ 
    float heightScale = 0.1f;
    // number of depth layers
    const float minLayers = 8;
    const float maxLayers = 32;
    float numLayers = mix(maxLayers, minLayers, abs(dot(vec3(0.0, 0.0, 1.0), viewDir)));  
    // calculate the size of each layer
    float layerDepth = 1.0 / numLayers;
    // depth of current layer
    float currentLayerDepth = 0.0;
    // the amount to shift the texture coordinates per layer (from vector P)
    vec2 P = viewDir.xy / viewDir.z * heightScale; 
    vec2 deltaTexCoords = P / numLayers;
  
    // get initial values
    vec2  currentTexCoords     = texCoords;
    float currentDepthMapValue = texture(u_HeightMap, currentTexCoords).r;
    while(currentLayerDepth < currentDepthMapValue)
    {
        // shift texture coordinates along direction of P
        currentTexCoords -= deltaTexCoords;
        // get depthmap value at current texture coordinates
        currentDepthMapValue = texture(u_HeightMap, currentTexCoords).r;  
        // get depth of next layer
        currentLayerDepth += layerDepth;  
    }
    
    // get texture coordinates before collision (reverse operations)
    vec2 prevTexCoords = currentTexCoords + deltaTexCoords;

    // get depth after and before collision for linear interpolation
    float afterDepth  = currentDepthMapValue - currentLayerDepth;
    float beforeDepth = texture(u_HeightMap, prevTexCoords).r - currentLayerDepth + layerDepth;
 
    // interpolation of texture coordinates
    float weight = afterDepth / (afterDepth - beforeDepth);
    vec2 finalTexCoords = prevTexCoords * weight + currentTexCoords * (1.0 - weight);

    return finalTexCoords;
}

void main()
{
    // offset texture coordinates with Parallax Mapping
    vec3 viewDir = normalize(TangentViewPos - TangentFragPos);
    vec2 texCoords;
    if (normalSteepOrOcclusion == 0)
        texCoords = v_texCoord;
    else if (normalSteepOrOcclusion == 1)
        texCoords = SteepParallaxMapping(v_texCoord, viewDir);
    else
        texCoords = ParallaxMapping(v_texCoord,  viewDir); // make the remainder of the code use newTexCoords
    if(texCoords.x > 1.0 || texCoords.y > 1.0 || texCoords.x < 0.0 || texCoords.y < 0.0)
        discard;
    // Fetch the normal from the normal map and transform it from [0, 1] to [-1, 1].
    vec3 sampledNormal = texture(u_NormalMap, texCoords).rgb;
    vec3 normal = normalize(sampledNormal * 2.0 - 1.0);
    // Fetch the color from the diffuse map.
    vec3 color = texture(u_DiffuseMap, texCoords).rgb;
    // Lighting calculations in tangent space.
    vec3 lightDir = normalize(TangentLightPos - TangentFragPos);
    vec3 lighting = CalculateLighting(normal, lightDir, viewDir);
    // Apply the lighting to the color.
    vec3 litColor = color * lighting;
    // Output the final color.
    FragColor = vec4(litColor, 1.0);
}

// ==================================================================
