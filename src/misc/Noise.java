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
package misc;

import java.util.Random;

// Copyright 2001 Ken Perlin
// CHECKSTYLE:OFF

/**
 * The Class Noise.
 */
public final class Noise {

    /** The seed. */
    public static int seed = 100;

    /** The Constant P. */
    private static final int P = 16;

    /** The Constant B. */
    private static final int B = 1 << P;

    /** The Constant M. */
    private static final int M = B - 1;

    /** The Constant NP. */
    private static final int NP = 16;

    /** The Constant N. */
    private static final int N = 1 << NP;
    // private static final int NM = N-1;

    /** The p. */
    private static int[] p = new int[B + B + 2];

    /** The g2. */
    private static double[][] g2 = new double[B + B + 2][2];

    /** The g1. */
    private static double[] g1 = new double[B + B + 2];
    // private static int start = 1;
    /** The points. */
    private static double[][] points = new double[32][3];

    /**
     * Inits the.
     */
    public static void init() {
        int i, j, k;
        double u, v, w, U, V, W, Hi, Lo;
        Random r = MyRandom.getInstance();
        for (i = 0; i < B; i++) {
            p[i] = i;
            g1[i] = 2 * r.nextDouble() - 1;

            do {
                u = 2 * r.nextDouble() - 1;
                v = 2 * r.nextDouble() - 1;
            } while (u * u + v * v > 1 || Math.abs(u) > 2.5 * Math.abs(v) || Math.abs(v) > 2.5 * Math.abs(u)
                    || Math.abs(Math.abs(u) - Math.abs(v)) < .4);
            g2[i][0] = u;
            g2[i][1] = v;
            normalize2(g2[i]);

            do {
                u = 2 * r.nextDouble() - 1;
                v = 2 * r.nextDouble() - 1;
                w = 2 * r.nextDouble() - 1;
                U = Math.abs(u);
                V = Math.abs(v);
                W = Math.abs(w);
                Lo = Math.min(U, Math.min(V, W));
                Hi = Math.max(U, Math.max(V, W));
            } while (u * u + v * v + w * w > 1 || Hi > 4 * Lo
                    || Math.min(Math.abs(U - V), Math.min(Math.abs(U - W), Math.abs(V - W))) < .2);
        }

        while (--i > 0) {
            k = p[i];
            j = (int) (r.nextLong() & M);
            p[i] = p[j];
            p[j] = k;
        }
        for (i = 0; i < B + 2; i++) {
            p[B + i] = p[i];
            g1[B + i] = g1[i];
            for (j = 0; j < 2; j++) {
                g2[B + i][j] = g2[i][j];
            }
        }

        points[3][0] = points[3][1] = points[3][2] = Math.sqrt(1. / 3);
        double r2 = Math.sqrt(1. / 2);
        double s = Math.sqrt(2 + r2 + r2);

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                points[i][j] = (i == j ? 1 + r2 + r2 : r2) / s;
            }
        }
        for (i = 0; i <= 1; i++) {
            for (j = 0; j <= 1; j++) {
                for (k = 0; k <= 1; k++) {
                    int n = i + j * 2 + k * 4;
                    if (n > 0) {
                        for (int m = 0; m < 4; m++) {
                            points[4 * n + m][0] = (i == 0 ? 1 : -1) * points[m][0];
                            points[4 * n + m][1] = (j == 0 ? 1 : -1) * points[m][1];
                            points[4 * n + m][2] = (k == 0 ? 1 : -1) * points[m][2];
                        }
                    }
                }
            }
        }
    }

    /**
     * Noise.
     * 
     * @param x the x
     * @return the double
     */
    public static double noise(final double x) {

        int bx0, bx1;
        double rx0, rx1, sx, t, u, v;
        t = x + N;
        bx0 = ((int) t) & M;
        bx1 = (bx0 + 1) & M;
        rx0 = t - (int) t;
        rx1 = rx0 - 1;

        sx = s_curve(rx0);
        u = rx0 * g1[p[bx0]];
        v = rx1 * g1[p[bx1]];

        return lerp(sx, u, v);
    }

    /**
     * Noise.
     * 
     * @param x the x
     * @param y the y
     * @return the double
     */
    public static double noise(final double x, final double y) {

        int bx0, bx1, by0, by1, b00, b10, b01, b11;
        double rx0, rx1, ry0, ry1, sx, sy, a, b, t, u, v, q[];
        int i, j;

        t = x + N;
        bx0 = ((int) t) & M;
        bx1 = (bx0 + 1) & M;
        rx0 = t - (int) t;
        rx1 = rx0 - 1;

        t = y + N;
        by0 = ((int) t) & M;
        by1 = (by0 + 1) & M;
        ry0 = t - (int) t;
        ry1 = ry0 - 1;

        i = p[bx0];
        j = p[bx1];

        b00 = p[i + by0];
        b10 = p[j + by0];
        b01 = p[i + by1];
        b11 = p[j + by1];

        sx = s_curve(rx0);
        sy = s_curve(ry0);

        q = g2[b00];
        u = rx0 * q[0] + ry0 * q[1];
        q = g2[b10];
        v = rx1 * q[0] + ry0 * q[1];
        a = lerp(sx, u, v);

        q = g2[b01];
        u = rx0 * q[0] + ry1 * q[1];
        q = g2[b11];
        v = rx1 * q[0] + ry1 * q[1];
        b = lerp(sx, u, v);

        return lerp(sy, a, b);
    }

    /**
     * G.
     * 
     * @param i the i
     * @return the double[]
     */
    private static double[] G(final int i) {
        return points[i % 32];
    }

    /**
     * Lerp.
     * 
     * @param t the t
     * @param a the a
     * @param b the b
     * @return the double
     */
    private static double lerp(final double t, final double a, final double b) {
        return a + t * (b - a);
    }

    /**
     * Normalize2.
     * 
     * @param v the v
     */
    private static void normalize2(final double v[]) {
        double s;
        s = Math.sqrt(v[0] * v[0] + v[1] * v[1]);
        v[0] = v[0] / s;
        v[1] = v[1] / s;
    }

    /**
     * S_curve.
     * 
     * @param t the t
     * @return the double
     */
    private static double s_curve(final double t) {
        return t * t * (3 - t - t);
    }

    /**
     * Noise.
     * 
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the double
     */
    static double noise(final double x, final double y, final double z) {

        int bx, by, bz, b0, b1, b00, b10, b01, b11;
        double rx0, rx1, ry0, ry1, rz, sx, sy, sz, a, b, c, d, u, v, q[];

        bx = (int) Math.IEEEremainder(Math.floor(x), B);
        if (bx < 0) {
            bx += B;
        }
        rx0 = x - Math.floor(x);
        rx1 = rx0 - 1;

        by = (int) Math.IEEEremainder(Math.floor(y), B);
        if (by < 0) {
            by += B;
        }
        ry0 = y - Math.floor(y);
        ry1 = ry0 - 1;

        bz = (int) Math.IEEEremainder(Math.floor(z), B);
        if (bz < 0) {
            bz += B;
        }
        rz = z - Math.floor(z);

        b0 = p[bx];

        bx++;

        b1 = p[bx];

        b00 = p[b0 + by];
        b10 = p[b1 + by];

        by++;

        b01 = p[b0 + by];
        b11 = p[b1 + by];

        sx = s_curve(rx0);
        sy = s_curve(ry0);
        sz = s_curve(rz);

        q = G(b00 + bz);
        u = rx0 * q[0] + ry0 * q[1] + rz * q[2];
        q = G(b10 + bz);
        v = rx1 * q[0] + ry0 * q[1] + rz * q[2];
        a = lerp(sx, u, v);
        q = G(b01 + bz);
        u = rx0 * q[0] + ry1 * q[1] + rz * q[2];
        q = G(b11 + bz);
        v = rx1 * q[0] + ry1 * q[1] + rz * q[2];
        b = lerp(sx, u, v);
        c = lerp(sy, a, b);
        bz++;
        rz--;
        q = G(b00 + bz);
        u = rx0 * q[0] + ry0 * q[1] + rz * q[2];
        q = G(b10 + bz);
        v = rx1 * q[0] + ry0 * q[1] + rz * q[2];
        a = lerp(sx, u, v);
        q = G(b01 + bz);
        u = rx0 * q[0] + ry1 * q[1] + rz * q[2];
        q = G(b11 + bz);
        v = rx1 * q[0] + ry1 * q[1] + rz * q[2];
        b = lerp(sx, u, v);
        d = lerp(sy, a, b);

        return lerp(sz, c, d);
    }
}
// CHECKSTYLE:ON
