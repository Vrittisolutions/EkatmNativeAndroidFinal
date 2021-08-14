package com.vritti.inventory.physicalInventory.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;

import com.vritti.inventory.physicalInventory.Interface.PrintCompleteService;
import com.vritti.inventory.physicalInventory.bean.Constants_wifi;
import com.vritti.inventory.physicalInventory.bean.Util_Wifi_print;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressLint("NewApi")
public class PrintServicesAdapter extends PrintDocumentAdapter {

    private Activity mActivity;
    private int pageHeight;
    private int pageWidth;
    private PdfDocument myPdfDocument;
    private int totalpages = 1;
    private File pdfFile;
    String message;
    private PrintCompleteService mPrintCompleteService;

    public PrintServicesAdapter(Activity mActivity, /*String msg*/ File pdfFile) {
        this.mActivity = mActivity;
        this.pdfFile = pdfFile;
        //this.message = msg;
        this.totalpages = Util_Wifi_print.computePDFPageCount(pdfFile);
        this.mPrintCompleteService = (PrintCompleteService) mActivity;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
                         PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback,
                         Bundle metadata) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            myPdfDocument = new PrintedPdfDocument(mActivity, newAttributes);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pageHeight =
                    newAttributes.getMediaSize().getHeightMils() / 1000 * 72;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pageWidth =
                    newAttributes.getMediaSize().getWidthMils() / 1000 * 72;
        }

        if (cancellationSignal.isCanceled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                callback.onLayoutCancelled();
            }
            return;
        }

        if (totalpages > 0) {
            PrintDocumentInfo.Builder builder = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                builder = new PrintDocumentInfo
                        .Builder(pdfFile.getName())
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT);
                       // .setPageCount(totalpages);
            }

            PrintDocumentInfo info = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                info = builder.build();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                callback.onLayoutFinished(info, true);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                callback.onLayoutFailed("Page count is zero.");
            }
        }

    }

    @Override
    public void onWrite(final PageRange[] pageRanges,
                        final ParcelFileDescriptor destination,
                        final CancellationSignal cancellationSignal,
                        final WriteResultCallback callback) {
        InputStream input = null;
        OutputStream output = null;

        try {
            input = new FileInputStream(pdfFile);
            output = new FileOutputStream(destination.getFileDescriptor());
            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            }


        } catch (FileNotFoundException ee) {
            //Catch exception
        } catch (Exception e) {
            //Catch exception
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                mPrintCompleteService.onMessage(Constants_wifi.PRINTER_STATUS_CANCELLED);
            }
        });
    }

    @Override
    public void onFinish() {
        mPrintCompleteService.onMessage(Constants_wifi.PRINTER_STATUS_COMPLETED);
    }

 /*   public interface PrintCompleteService {
        public void onMessage(int status);
    }*/

}
