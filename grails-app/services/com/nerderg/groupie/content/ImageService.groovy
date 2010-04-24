/*
    Copyright 2009, 2010 Peter McNeil

    This file is part of Groupie.

    Licensed under the Apache License, Version 2.0 (the "License"); you may not 
    use this file except in compliance with the License. You may obtain a copy 
    of the License at http://www.apache.org/licenses/LICENSE-2.0 
    
    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License. 
*/

package com.nerderg.groupie.content
import java.awt.RenderingHints
import java.awt.image.renderable.ParameterBlock
import javax.media.jai.*
import com.sun.media.jai.codec.*

class ImageService {

    boolean transactional = true
    def hintMap = [(RenderingHints.KEY_ANTIALIASING): RenderingHints.VALUE_ANTIALIAS_OFF,
        (RenderingHints.KEY_RENDERING): RenderingHints.VALUE_RENDER_QUALITY,
        (RenderingHints.KEY_DITHERING): RenderingHints.VALUE_DITHER_ENABLE,
        (RenderingHints.KEY_INTERPOLATION): RenderingHints.VALUE_INTERPOLATION_BICUBIC]
    def hints = new RenderingHints(hintMap)

    def thumb(bytes, width, height, type, keepAspect) {
        def stream = new ByteArraySeekableStream(bytes)
        def image = JAI.create("stream", stream, new PNGDecodeParam())
        keepAspect = keepAspect || !width || !height
        image = scale(width?:image.width , height?:image.height, keepAspect, image, hints)
        def bos = new ByteArrayOutputStream()
        log.debug "Rendering image type: $type"
        //can't encode to gif just decode
        JAI.create("encode", image, bos, (type =='gif' ? 'png' : type), null, hints)
        return bos.toByteArray()

    }

    def scale(float width, float height, keepAspect, image, hints) {
        float modifierx
        float modifiery
        if (image.height <= height && image.width <= width) {
            return image
        }
        if(keepAspect){
            boolean tall = (image.height * (width / height) > image.width)
            modifierx = modifiery = width / (float) (tall ? (image.height * (width / height)) : image.width)
        } else {
            modifierx = width / (float) (image.width)
            modifiery = height / (float) (image.height)
        }
        ParameterBlock params = new ParameterBlock()
        params.addSource(image)

        params.add(modifierx)//x scale factor
        params.add(modifiery)//y scale factor
        params.add(0.0F)//x translate
        params.add(0.0F)//y translate
        params.add(new InterpolationBicubic2(8)) // Supposed to produce the best of the two, but also largest size(byte)
        return JAI.create("Scale", params, hints)
    }

}
