// Copyright 2002, FreeHEP.
package com.zapps.docsReaderModule.thirdpart.emf.data;

import com.zapps.docsReaderModule.thirdpart.emf.EMFInputStream;
import com.zapps.docsReaderModule.thirdpart.emf.EMFRenderer;
import com.zapps.docsReaderModule.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * CloseFigure TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: CloseFigure.java 10367 2007-01-22 19:26:48Z duns $
 */
public class CloseFigure extends EMFTag {

    public CloseFigure() {
        super(61, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return this;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        renderer.closeFigure();
    }
}
