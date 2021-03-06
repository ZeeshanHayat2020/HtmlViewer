/*
 * 文件名称:          TXTKit.java
 *
 * 编译器:            android2.2
 * 时间:              下午5:16:41
 */
package com.zapps.docsReaderModule.fc.doc;

import android.os.Handler;
import android.util.Log;

import com.zapps.docsReaderModule.common.ICustomDialog;
import com.zapps.docsReaderModule.constant.DialogConstant;
import com.zapps.docsReaderModule.fc.fs.storage.HeaderBlock;
import com.zapps.docsReaderModule.fc.fs.storage.LittleEndian;
import com.zapps.docsReaderModule.system.FileReaderThread;
import com.zapps.docsReaderModule.system.IControl;
import com.zapps.docsReaderModule.system.IDialogAction;
import com.zapps.docsReaderModule.thirdpart.mozilla.intl.chardet.CharsetDetector;
import com.zapps.docsReaderModule.wp.dialog.TXTEncodingDialog;

import java.io.FileInputStream;
import java.util.Vector;

/**
 * text kit
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-3-12
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class TXTKit {
    //
    private static TXTKit kit = new TXTKit();

    //
    public static TXTKit instance() {
        return kit;
    }

    /**
     * @param control
     * @param handler
     * @param filePath
     */
    public void readText(final IControl control, final Handler handler, final String filePath) {
        try {
            FileInputStream in = new FileInputStream(filePath);
            byte[] b = new byte[16];
            in.read(b);
            long signature = LittleEndian.getLong(b, 0);
            if (signature == HeaderBlock._signature // doc, ppt, xls
                    || signature == 0x0006001404034b50L) // docx, pptx, xls
            {
                in.close();
                control.getSysKit().getErrorKit().writerLog(new Exception("Format error"), true);
                return;
            }
            signature = signature & 0x00FFFFFFFFFFFFFFL;
            if (signature == 0x002e312d46445025L) {
                in.close();
                control.getSysKit().getErrorKit().writerLog(new Exception("Format error"), true);
                return;
            }
            in.close();

            String code = control.isAutoTest() ? "GBK" : CharsetDetector.detect(filePath);
            Log.d("TXTKIT", "readText: " + code);
            if (code != null) {
                new FileReaderThread(control, handler, filePath, code).start();
                return;
            }

//            Edit By Zeeshan Remove Encoding Dialog
        /*    if(!control.getMainFrame().isShowTXTEncodeDlg())
            {
                Vector<Object> vector = new Vector<Object>();
                vector.add(filePath);
                IDialogAction da = new IDialogAction()
                {                    

                    public IControl getControl()
                    {
                        return control;
                    }
                    public void doAction(int id, Vector<Object> model)
                    {
                        if (TXTEncodingDialog.BACK_PRESSED.equals(model.get(0)))
                        {
                            control.getMainFrame().getActivity().onBackPressed();
                        }
                        else
                        {
                            new FileReaderThread(control, handler, filePath, model.get(0).toString()).start();
                        }
                    }
                    public void dispose()
                    {
                        
                    }
                };                

                new TXTEncodingDialog(control, control.getMainFrame().getActivity(), da, 
                    vector, DialogConstant.ENCODING_DIALOG_ID).show();
                
            }
            else
            {

            }*/

            String encode = control.getMainFrame().getTXTDefaultEncode();
            if (encode == null) {
                ICustomDialog dlgListener = control.getCustomDialog();
                if (dlgListener != null) {
                    dlgListener.showDialog(ICustomDialog.DIALOGTYPE_ENCODE);
                } else {
                    new FileReaderThread(control, handler, filePath, "UTF-8").start();
                }
            } else {
                new FileReaderThread(control, handler, filePath, encode).start();
            }
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     *
     */
    public void reopenFile(final IControl control, final Handler handler, final String filePath, String encode) {
        new FileReaderThread(control, handler, filePath, encode).start();
    }
}
