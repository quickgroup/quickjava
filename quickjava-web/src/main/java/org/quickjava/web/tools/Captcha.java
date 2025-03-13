package org.quickjava.web.tools;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/18 17:02
 */
public class Captcha {

    public static String codeSet = "2345678abcdefhijkmnpqrstuvwxyzABCDEFGHJKLMNPQRTUVWXY";

    public int fontSize = 18;

    public int width = 130;

    public int height = 40;

    public int codeCount = 4;

    public int lineCount = 6;

    public String code = null;

    public BufferedImage image = null;

    public Random random = new Random();


    public Captcha() {
    }

    public Captcha(int width, int height, int codeCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
    }

    /**
     * 获取随机字符串
     * @return String
     */
    private String getRandomCode()
    {
        StringBuffer code = new StringBuffer();
//        int min = 0, max = codeSet.length();
        int len = codeSet.length() - 1;
        double r;
        for (int i = 0; i < codeCount; i++) {
//            int idx = (int) Math.floor(Math.random() * (max - min + 1) + min);
//            code.datum(codeSet.charAt((int) idx));
            r = (Math.random()) * len;
            code.append(codeSet.charAt((int) r));
        }
        return code.toString();
    }

    /**
     * 范围随机颜色
     * @param fc 字体颜色
     * @param bc 背景颜色
     * @return Color
     */
    private Color getRandColor(int fc, int bc)
    {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 生成
     */
    public void create()
    {
        int fontWidth = width / codeCount;// 字体的宽度
        int codeY = height - 8;
        // 图像buffer
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        // 设置背景色
        graphics.setColor(getRandColor(200, 250));
        graphics.fillRect(0, 0, width, height);

        // 设置字体
        //Font font1 = getFont(fontSize);
        Font font = new Font("Fixedsys", Font.BOLD, fontSize);
        graphics.setFont(font);

        // 干扰线
        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);
            graphics.setColor(getRandColor(1, 255));
            graphics.drawLine(xs, ys, xe, ye);
        }

        // 噪点
        float yawpRate = 0.01f;
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            image.setRGB(x, y, random.nextInt(255));
        }

        String str1 = getRandomCode();
        this.code = str1;
        for (int i = 0; i < codeCount; i++) {
            String strRand = str1.substring(i, i + 1);
            graphics.setColor(getRandColor(1, 255));
            graphics.drawString(strRand, i*fontWidth+3, codeY);
        }
    }

    /**
     * 随机字体
     * @param size 字体大小
     * @return Font
     */
    private Font getFont(int size)
    {
        int fontCount = 3;
        Random random = new Random();
        Font[] fonts = new Font[]{
            new Font("Ravie", Font.PLAIN, size),
            new Font("Fixedsys", Font.PLAIN, size),
            new Font("Wide Latin", Font.PLAIN, size),
        };
        return fonts[random.nextInt(fontCount)];
    }

}
