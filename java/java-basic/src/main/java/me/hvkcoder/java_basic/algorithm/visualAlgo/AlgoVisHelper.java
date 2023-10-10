package me.hvkcoder.java_basic.algorithm.visualAlgo;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * 设置绘图工具
 *
 * @author h_vk
 * @since 2023/10/9
 */
public class AlgoVisHelper {
	private AlgoVisHelper() {
	}

	public static final Color Red = new Color(0xF44336);
	public static final Color Pink = new Color(0xE91E63);
	public static final Color Purple = new Color(0x9C27B0);
	public static final Color DeepPurple = new Color(0x673AB7);
	public static final Color Indigo = new Color(0x3F51B5);
	public static final Color Blue = new Color(0x2196F3);
	public static final Color LightBlue = new Color(0x03A9F4);
	public static final Color Cyan = new Color(0x00BCD4);
	public static final Color Teal = new Color(0x009688);
	public static final Color Green = new Color(0x4CAF50);
	public static final Color LightGreen = new Color(0x8BC34A);
	public static final Color Lime = new Color(0xCDDC39);
	public static final Color Yellow = new Color(0xFFEB3B);
	public static final Color Amber = new Color(0xFFC107);
	public static final Color Orange = new Color(0xFF9800);
	public static final Color DeepOrange = new Color(0xFF5722);
	public static final Color Brown = new Color(0x795548);
	public static final Color Grey = new Color(0x9E9E9E);
	public static final Color BlueGrey = new Color(0x607D8B);
	public static final Color Black = new Color(0x000000);
	public static final Color White = new Color(0xFFFFFF);


	/**
	 * 设置线条宽度
	 *
	 * @param g
	 * @param strokeWidth
	 */
	public static void setStrokeWidth(Graphics2D g, int strokeWidth) {
		g.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	}

	/**
	 * 设置绘制颜色
	 *
	 * @param g
	 * @param color
	 */
	public static void setColor(Graphics2D g, Color color) {
		g.setColor(color);
	}

	/**
	 * 绘制一个空心圆
	 *
	 * @param g
	 * @param x
	 * @param y
	 * @param r
	 */
	public static void strokeCircle(Graphics2D g, int x, int y, int r) {
		Ellipse2D circle = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
		g.draw(circle);
	}

	/**
	 * 绘制一个实心圆
	 *
	 * @param g
	 * @param x
	 * @param y
	 * @param r
	 */
	public static void fillCircle(Graphics2D g, int x, int y, int r) {
		Ellipse2D circle = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
		g.fill(circle);
	}

	/**
	 * 绘制空心矩形
	 *
	 * @param g
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public static void strokeRectangle(Graphics2D g, int x, int y, int w, int h) {
		Rectangle2D rectangle = new Rectangle2D.Double(x, y, w, h);
		g.draw(rectangle);
	}

	/**
	 * 绘制实心矩形
	 *
	 * @param g
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public static void fillRectangle(Graphics2D g, int x, int y, int w, int h) {
		Rectangle2D rectangle = new Rectangle2D.Double(x, y, w, h);
		g.fill(rectangle);
	}
}
