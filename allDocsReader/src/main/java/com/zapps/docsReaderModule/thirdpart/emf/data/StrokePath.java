// Copyright 2002, FreeHEP.

package com.zapps.docsReaderModule.thirdpart.emf.data;

import com.zapps.docsReaderModule.java.awt.Rectangle;
import com.zapps.docsReaderModule.java.awt.geom.GeneralPath;
import com.zapps.docsReaderModule.thirdpart.emf.EMFInputStream;
import com.zapps.docsReaderModule.thirdpart.emf.EMFRenderer;
import com.zapps.docsReaderModule.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * StrokePath TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: StrokePath.java 10367 2007-01-22 19:26:48Z duns $
 */
public class StrokePath extends EMFTag
{

    private Rectangle bounds;

    public StrokePath()
    {
        super(64, 1);
    }

    public StrokePath(Rectangle bounds)
    {
        this();
        this.bounds = bounds;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new StrokePath(emf.readRECTL());
    }

    public String toString()
    {
        return super.toString() + "\n  bounds: " + bounds;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        GeneralPath currentPath = renderer.getPath();
        // fills the current path
        if (currentPath != null)
        {
            renderer.drawShape(currentPath);
            renderer.setPath(null);
        }
    }
}
