// Copyright 2002, FreeHEP.

package com.zapps.docsReaderModule.thirdpart.emf.data;

import com.zapps.docsReaderModule.thirdpart.emf.EMFInputStream;
import com.zapps.docsReaderModule.thirdpart.emf.EMFTag;

import java.io.IOException;

/**
 * AbortPath TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: AbortPath.java 10140 2006-12-07 07:50:41Z duns $
 */
public class AbortPath extends EMFTag
{

    public AbortPath()
    {
        super(68, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return this;
    }
}
