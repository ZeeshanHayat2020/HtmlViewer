// Copyright 2002, FreeHEP.

package com.zapps.docsReaderModule.thirdpart.emf.data;

import com.zapps.docsReaderModule.java.awt.Color;
import com.zapps.docsReaderModule.thirdpart.emf.EMFInputStream;
import com.zapps.docsReaderModule.thirdpart.emf.EMFRenderer;
import com.zapps.docsReaderModule.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * SetTextColor TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetTextColor.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetTextColor extends EMFTag
{

    private Color color;

    public SetTextColor()
    {
        super(24, 1);
    }

    public SetTextColor(Color color)
    {
        this();
        this.color = color;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetTextColor(emf.readCOLORREF());
    }


    public String toString()
    {
        return super.toString() + "\n  color: " + color;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        renderer.setTextColor(color);
    }
}
