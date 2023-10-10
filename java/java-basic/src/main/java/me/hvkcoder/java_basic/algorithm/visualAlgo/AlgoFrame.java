package me.hvkcoder.java_basic.algorithm.visualAlgo;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

/**
 * 可视化算法
 *
 * @author h_vk
 * @since 2023/10/9
 */
public class AlgoFrame extends JFrame {
	public AlgoFrame(String title, int canvasWidth, int canvasHeight, BiConsumer<Graphics2D, Dimension> render) {
		super(title);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AlgoCanvas algoCanvas = new AlgoCanvas(canvasWidth, canvasHeight, render);
		setContentPane(algoCanvas);
		pack();
		setVisible(true);
	}

	public AlgoFrame(String title, BiConsumer<Graphics2D, Dimension> render) {
		this(title, 1024, 768, render);
	}


	public void animation(Runnable runnable) {
		animation(-1, runnable);
	}

	public void animation(int delay, Runnable runnable) {
		new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					repaint();
					if (delay > 0) {
						Thread.sleep(delay);
					}
					runnable.run();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
	}

	private static class AlgoCanvas extends JPanel {
		private final BiConsumer<Graphics2D, Dimension> render;

		public AlgoCanvas(int canvasWidth, int canvasHeight, BiConsumer<Graphics2D, Dimension> render) {
			// 启用双缓存
			super(true);
			this.render = render;
			setPreferredSize(new Dimension(canvasWidth, canvasHeight));
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			// 抗锯齿
			RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHints(hints);
			this.render.accept(g2d, getPreferredSize());
		}
	}
}
