// Copyright 2002, FreeHEP.
package com.zapps.docsReaderModule.thirdpart.emf.data;

import com.zapps.docsReaderModule.java.awt.Rectangle;
import com.zapps.docsReaderModule.thirdpart.emf.EMFConstants;
import com.zapps.docsReaderModule.thirdpart.emf.EMFInputStream;
import com.zapps.docsReaderModule.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * ExtTextOutA TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: ExtTextOutA.java 10377 2007-01-23 15:44:34Z duns $
 */
public class ExtTextOutA extends AbstractExtTextOut implements EMFConstants {

    private TextA text;

    public ExtTextOutA() {
        super(83, 1, null, 0, 1, 1);
    }

    public ExtTextOutA(
        Rectangle bounds,
        int mode,
        float xScale,
        float yScale,
        TextA text) {

        super(83, 1, bounds, mode, xScale, yScale);
        this.text = text;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new ExtTextOutA(
            emf.readRECTL(),
            emf.readDWORD(),
            emf.readFLOAT(),
            emf.readFLOAT(),
            TextA.read(emf));
    }

    public Text getText() {
        return text;
    }
}
