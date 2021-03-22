/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package com.zapps.docsReaderModule.fc.hpsf;

import com.zapps.docsReaderModule.fc.util.HexDump;

/**
 * <p>This exception is thrown if HPSF encounters a variant type that is illegal
 * in the current context.</p>
 * 
 * @author Rainer Klute <a
 * href="mailto:klute@rainer-klute.de">&lt;klute@rainer-klute.de&gt;</a>
 */
public class IllegalVariantTypeException extends VariantTypeException
{

    /**
     * <p>Constructor</p>
     * 
     * @param variantType The unsupported variant type
     * @param value The value
     * @param msg A message string
     */
    public IllegalVariantTypeException(final long variantType,
                                       final Object value, final String msg)
    {
        super(variantType, value, msg);
    }

    /**
     * <p>Constructor</p>
     * 
     * @param variantType The unsupported variant type
     * @param value The value
     */
    public IllegalVariantTypeException(final long variantType,
                                       final Object value)
    {
        this(variantType, value, "The variant type " + variantType + " (" +
             Variant.getVariantName(variantType) + ", " + 
             HexDump.toHex(variantType) + ") is illegal in this context.");
    }

}