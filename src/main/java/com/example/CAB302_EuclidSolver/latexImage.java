package com.example.CAB302_EuclidSolver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class latexImage {

    // CodeCogs LaTeX PNG endpoint
    private static final String ENDPOINT = "https://latex.codecogs.com/png.latex?";

    // ---- Rendering options ----
    private static final int    DPI        = 450;        // base DPI; we also render 2× and 3× variants
    private static final String SIZE       = "\\huge";   // \\Large, \\LARGE, \\huge
    private static final String BACKGROUND = "white";    // white = clean scaling
    private static final boolean DEBUG     = true;       // print URL & server body on failure

    /** Render a MultiResolutionImage (1x/2x/3x) so Swing is crisp on HiDPI. */
    public static Image renderMultiRes(String latex) throws IOException {
        BufferedImage img1x = ensureOpaqueWhite(renderAtDpi(latex, DPI));
        BufferedImage img2x = ensureOpaqueWhite(renderAtDpi(latex, DPI * 2));
        BufferedImage img3x = ensureOpaqueWhite(renderAtDpi(latex, DPI * 3));

        img1x = slightBlur(img1x);
        img2x = slightBlur(img2x);
        img3x = slightBlur(img3x);

        return new BaseMultiResolutionImage(new Image[]{ img1x, img2x, img3x });
    }

    /** Same as above, but applies a blended blur (0..1 = amount). */
    public static Image renderMultiResBlurred(String latex, float amount) throws IOException {
        BufferedImage img1x = ensureOpaqueWhite(renderAtDpi(latex, DPI));
        BufferedImage img2x = ensureOpaqueWhite(renderAtDpi(latex, DPI * 2));
        BufferedImage img3x = ensureOpaqueWhite(renderAtDpi(latex, DPI * 3));
        img1x = mixWithBlur(img1x, amount);
        img2x = mixWithBlur(img2x, amount);
        img3x = mixWithBlur(img3x, amount);
        return new BaseMultiResolutionImage(new Image[]{ img1x, img2x, img3x });
    }

    /** Render a single BufferedImage at a specific DPI. */
    public static BufferedImage renderAtDpi(String latex, int dpi) throws IOException {
        String fullLatex = buildFullLatex(latex, dpi);

        // Encode once; IMPORTANT: convert '+' (URLEncoder's space) to %20 to avoid stray plus signs
        String encoded = encodeForCodecogs(fullLatex);

        String urlStr = ENDPOINT + encoded;
        if (DEBUG) {
            System.err.println("[latexImage] DPI=" + dpi);
            System.err.println("[latexImage] URL: " + urlStr);
            System.err.println("[latexImage] TEX: " + fullLatex);
        }

        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Java-LaTeX-Client/1.0");
        conn.setRequestProperty("Accept", "image/png");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(15000);

        int code = conn.getResponseCode();
        String ctype = conn.getContentType();
        if (code == 200 && ctype != null && ctype.toLowerCase().startsWith("image")) {
            try (InputStream in = conn.getInputStream()) {
                BufferedImage img = ImageIO.read(in);
                if (img != null) return img;
            }
        }

        // If here, not an image — show server body to help debug
        String body = readBody(conn);
        throw new IOException("HTTP " + code + " (" + ctype + ")\n" + body);
    }

    /** Build LaTeX: high DPI + background + size + display style (no bold). */
    private static String buildFullLatex(String latex, int dpi) {
        // \displaystyle improves math layout; SIZE bumps font size.
        return "\\dpi{" + dpi + "}\\bg{" + BACKGROUND + "} \\displaystyle " + SIZE + " " + latex;
    }

    /** URLEncoder encodes spaces as '+', which CodeCogs renders as literal plus signs.
     *  Replace '+' with '%20' so spaces stay as spaces. */
    private static String encodeForCodecogs(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private static String readBody(HttpURLConnection conn) {
        try (InputStream err = conn.getErrorStream() != null ? conn.getErrorStream() : conn.getInputStream();
             ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
            if (err == null) return "<no body>";
            err.transferTo(buf);
            return buf.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "<failed to read body: " + e.getMessage() + ">";
        }
    }

    // ======== Image utilities ========

    /** Ensure opaque white (avoids alpha-edge halos when scaling/compositing). */
    public static BufferedImage ensureOpaqueWhite(BufferedImage src) {
        if (src.getType() == BufferedImage.TYPE_INT_RGB) return src;
        BufferedImage out = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, out.getWidth(), out.getHeight());
        g.setComposite(AlphaComposite.SrcOver);
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return out;
    }

    /** Downscale by a factor (e.g., 0.75). Never upscale here. */
    public static BufferedImage scaleBy(BufferedImage src, double scale) {
        if (scale >= 1.0) return src;
        int w = Math.max(1, (int) Math.round(src.getWidth() * scale));
        int h = Math.max(1, (int) Math.round(src.getHeight() * scale));
        return scaleTo(src, w, h);
    }

    /** Downscale to a target width (height keeps aspect). */
    public static BufferedImage scaleToWidth(BufferedImage src, int targetW) {
        if (targetW >= src.getWidth()) return src;
        double s = targetW / (double) src.getWidth();
        int w = targetW;
        int h = Math.max(1, (int) Math.round(src.getHeight() * s));
        return scaleTo(src, w, h);
    }

    private static BufferedImage scaleTo(BufferedImage src, int w, int h) {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return out;
    }

    // Drops a PNG of the given LaTeX math to the given file using CodeCogs.
    // No extra libs needed; requires internet access.
    public static File saveLatexPng(String latexMath, File outFile, int dpi) throws IOException {
        // Force white background + DPI from LaTeX side
        String payload = "\\dpi{" + dpi + "}\\bg{transparent} " + latexMath;
        String url = ENDPOINT + encodeForCodecogs(payload);

        if (DEBUG) {
            System.err.println("[latexImage] saveLatexPng URL: " + url);
            System.err.println("[latexImage] saveLatexPng TEX: " + payload);
        }

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(15000);
        conn.setRequestProperty("Accept", "image/png");

        try (InputStream in = conn.getInputStream()) {
            BufferedImage img = ImageIO.read(in);
            if (img == null) throw new IOException("Codecogs returned no image");
            // Optional: slight anti-aliasing/blur if you want softer edges:
            // img = slightBlur(img);
            ImageIO.write(img, "png", outFile);
            return outFile;
        } finally {
            conn.disconnect();
        }
    }

    // ---- Drop-in blur helpers ----

    /** Very slight Gaussian-like blur (3x3). Smooths jaggies without getting mushy. */
    public static BufferedImage slightBlur(BufferedImage src) {
        float[] data = {
                1f/16f, 2f/16f, 1f/16f,
                2f/16f, 4f/16f, 2f/16f,
                1f/16f, 2f/16f, 1f/16f
        };
        Kernel kernel = new Kernel(3, 3, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        op.filter(src, dst);
        return dst;
    }

    /** Blend a small amount of blur over the sharp image (amount: 0..1). */
    public static BufferedImage mixWithBlur(BufferedImage sharp, float amount) {
        amount = Math.max(0f, Math.min(1f, amount));
        BufferedImage blur = slightBlur(sharp);
        BufferedImage out = new BufferedImage(sharp.getWidth(), sharp.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        g.drawImage(sharp, 0, 0, null);
        g.setComposite(AlphaComposite.SrcOver.derive(amount));
        g.drawImage(blur, 0, 0, null);
        g.dispose();
        return out;
    }

    // ======== Demo ========

    public static void main(String[] args) {
        // If you want to **disable** JVM UI scaling (for A/B testing), uncomment:
        // System.setProperty("sun.java2d.uiScale", "1");

        SwingUtilities.invokeLater(() -> {
            try {
                String latex = "a = \\frac{(2+3)}{4} + \\sqrt{9}";

                // A) Use multi-resolution so HiDPI looks crisp (no blur):
                Image multi = renderMultiRes(latex);
                JLabel label = new JLabel(new ImageIcon(multi));

                // B) Manual single image path with downscale + blur:
                // BufferedImage img = renderAtDpi(latex, DPI * 2);   // render larger
                // img = ensureOpaqueWhite(img);
                // img = scaleToWidth(img, 700);                      // downscale for crispness
                // img = mixWithBlur(img, 0.35f);                     // 35% blended blur
                // JLabel label = new JLabel(new ImageIcon(img));

                // C) Multi-resolution with built-in blended blur:
                // Image multiBlur = renderMultiResBlurred(latex, 0.25f);
                // JLabel label = new JLabel(new ImageIcon(multiBlur));

                JFrame f = new JFrame("Rendered LaTeX (HiDPI-aware)");
                f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                f.add(label);
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to render LaTeX:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
