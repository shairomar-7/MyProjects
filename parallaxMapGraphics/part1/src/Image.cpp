#include "Image.hpp"
#include <fstream>
#include <iostream>
#include <string.h>
#include <stdio.h>
#include <memory>

// Constructor
Image::Image(std::string filepath) : m_filepath(filepath){
    
}

// Destructor
Image::~Image (){
    // Delete our pixel data.	
    // Note: We could actually do this sooner
    // in our rendering process.
    if(m_pixelData!=NULL){
        delete[] m_pixelData;
    }
}

// Little function for loading the pixel data
// from a PPM image.
// TODO: Expects a very specific version of PPM!
//
// flip - Will flip the pixels upside down in the data
//        If you use this be consistent.
void Image::LoadPPM(bool flip){
    std::ifstream ppmFile(m_filepath);
    if (!ppmFile.is_open()) {
        std::cout << "Unable to open ppm file: " << m_filepath << std::endl;
        return;
    }

    std::cout << "Reading in ppm file: " << m_filepath << std::endl;

    std::string line;
    std::getline(ppmFile, line); // Read the magic number line
    if (line[0] != 'P' || line[1] != '3') {
        std::cout << "Unsupported PPM format or not a P3 PPM file" << std::endl;
        return;
    }

    // Skip comments
    while (ppmFile.peek() == '#') {
        std::getline(ppmFile, line);
    }

    // Read image dimensions
    ppmFile >> m_width >> m_height;
    std::cout << "PPM width, height=" << m_width << "," << m_height << "\n";

    // Read the max color value (not used)
    int maxColorValue;
    ppmFile >> maxColorValue;

    if (m_width <= 0 || m_height <= 0) {
        std::cout << "PPM not parsed correctly, width and/or height dimensions are invalid" << std::endl;
        return;
    }

    m_pixelData = new uint8_t[m_width * m_height * 3];
    if (m_pixelData == nullptr) {
        std::cout << "Unable to allocate memory for ppm" << std::endl;
        return;
    }

    // Read pixel data
    int value;
    unsigned int pos = 0;
    while (ppmFile >> value) {
        if (pos < m_width * m_height * 3) {
            m_pixelData[pos++] = static_cast<uint8_t>(value);
        }
    }

    ppmFile.close();

    // Flip all of the pixels
    if(flip){
        // Copy all of the data to a temporary stack-allocated array
        uint8_t* copyData = new uint8_t[m_width*m_height*3];
        for(int i =0; i < m_width*m_height*3; ++i){
            copyData[i]=m_pixelData[i];
        }
        //memcpy(copyData,m_pixelData,(m_width*m_height*3)*sizeof(uint8_t));
        unsigned int pos = (m_width*m_height*3)-1;
        for(int i =0; i < m_width*m_height*3; i+=3){
            m_pixelData[pos]=copyData[i+2];
            m_pixelData[pos-1]=copyData[i+1];
            m_pixelData[pos-2]=copyData[i];
            pos-=3;
        }
        delete[] copyData;
    }
}

/*  ===============================================
Desc: Sets a pixel in our array a specific color
Precondition: 
Post-condition:
=============================================== */ 
void Image::SetPixel(int x, int y, uint8_t r, uint8_t g, uint8_t b){
  if(x > m_width || y > m_height){
    return;
  }
  else{
    /*std::cout << "modifying pixel at " 
              << x << "," << y << "from (" <<
              (int)color[x*y] << "," << (int)color[x*y+1] << "," <<
(int)color[x*y+2] << ")";*/
    m_pixelData[(x*3)+m_height*(y*3)] = r;
    m_pixelData[(x*3)+m_height*(y*3)+1] = g;
    m_pixelData[(x*3)+m_height*(y*3)+2] = b;
/*    std::cout << " to (" << (int)color[x*y] << "," << (int)color[x*y+1] << ","
<< (int)color[x*y+2] << ")" << std::endl;*/
  }
}

/*  ===============================================
Desc: 
Precondition: 
Post-condition:
=============================================== */ 
void Image::PrintPixels(){
    for(int x = 0; x <  m_width*m_height*3; ++x){
        std::cout << " " << (int)m_pixelData[x];
    }
    std::cout << "\n";
}

/*  ===============================================
Desc: Returns pixel data for our image
Precondition: 
Post-condition:
=============================================== */ 
uint8_t* Image::GetPixelDataPtr(){
    return m_pixelData;
}
