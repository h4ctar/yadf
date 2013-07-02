/**
 * yadf
 * 
 * https://sourceforge.net/projects/yadf
 * 
 * Ben Smith (bensmith87@gmail.com)
 * 
 * yadf is placed under the BSD license.
 * 
 * Copyright (c) 2012-2013, Ben Smith All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * - Neither the name of the yadf project nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class Logger.
 */
public final class Logger {

    /** The instance. */
    private static Logger instance;

    private String fileName = "c:\\temp\\yadf_" + System.currentTimeMillis() + ".txt";

    /**
     * Gets the single instance of Logger.
     * @return single instance of Logger
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * Instantiates a new logger.
     */
    private Logger() {
    }

    /**
     * Log.
     * @param object the object
     * @param text the text
     * @param err true if its an error message
     */
    @SuppressWarnings("static-method")
    public void log(final Object object, final String text, final boolean err) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
        String dateString = dateFormat.format(date);
        String threadName = "[" + Thread.currentThread().getName() + "]";
        String className;
        if (object == null) {
            className = "";
        } else {
            className = "[" + object.getClass().getSimpleName() + "]";
        }
        String message = dateString + " " + threadName + " " + className + " " + text;
        if (err) {
            System.err.println(message);
        } else {
            System.out.println(message);
        }
        /**
         * try { PrintWriter fileOut = null; fileOut = new PrintWriter(new FileWriter(fileName, true));
         * fileOut.println(message); fileOut.close(); } catch (Exception e) { e.printStackTrace(); }
         **/
    }

    /**
     * Log.
     * 
     * @param object the object
     * @param text the text
     */
    @SuppressWarnings("static-method")
    public void log(final Object object, final String text) {
        log(object, text, false);
    }
}
