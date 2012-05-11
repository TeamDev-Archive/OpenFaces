/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.util;

import org.apache.commons.fileupload.disk.DiskFileItem;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class ProgressMonitorFileItem extends DiskFileItem {

    private static final int MAX_REQUEST_LENGTH = 1000; //without files
    private ProgressObserver observer;
    private long passedInFileSize; // sizeOfAllFile
    private long bytesRead; // how many bytes already read
    private boolean shouldProcess;

    public ProgressMonitorFileItem(String fieldName, String contentType,
                                   boolean isFormField, String fileName,
                                   int sizeThreshold, File repository,
                                   ProgressObserver observer,
                                   long passedInFileSize, boolean shouldProcess) {
        super(fieldName, contentType, isFormField, fileName, sizeThreshold, repository);
        this.observer = observer;
        this.passedInFileSize = passedInFileSize;
        this.shouldProcess = shouldProcess;
        if (shouldProcess && observer != null) {
            observer.setProgress(0);
        }
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        OutputStream baseOutputStream = super.getOutputStream();
        if (observer != null) {
            return new BytesCountingOutputStream(baseOutputStream);
        } else {
            return baseOutputStream;
        }
    }

    private class BytesCountingOutputStream extends OutputStream {

        private long previousProgressUpdate;
        private OutputStream base;

        public BytesCountingOutputStream(OutputStream ous) {
            base = ous;
        }

        public void close() throws IOException {
            base.close();
        }

        public boolean equals(Object arg0) {
            return base.equals(arg0);
        }

        public void flush() throws IOException {
            base.flush();
        }

        public int hashCode() {
            return base.hashCode();
        }

        public String toString() {
            return base.toString();
        }

        public void write(byte[] bytes, int offset, int len) throws IOException {
            if (shouldProcess) {
                base.write(bytes, offset, len);
                fireProgressEvent(len);
            }
        }

        public void write(byte[] bytes) throws IOException {
            if (shouldProcess) {
                base.write(bytes);
                fireProgressEvent(bytes.length);
            }
        }

        public void write(int b) throws IOException {
            if (shouldProcess) {
                base.write(b);
                fireProgressEvent(1);
            }
        }

        //this method calls observer.setProgress which in its turn save progress number in session
        private void fireProgressEvent(int b) {
            bytesRead += b;
            if (bytesRead - previousProgressUpdate > (passedInFileSize / 100.0)) {//enough for refreshing results or not
                double rateOfLoad = ((double) bytesRead) / passedInFileSize;
                observer.setProgress((int) (rateOfLoad * 100.0));
                previousProgressUpdate = bytesRead;
            }
            if (passedInFileSize - bytesRead <= MAX_REQUEST_LENGTH) {
                observer.setProgress(100);
            }
        }

    }
}
