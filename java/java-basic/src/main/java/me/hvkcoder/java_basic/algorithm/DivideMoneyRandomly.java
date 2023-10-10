package me.hvkcoder.java_basic.algorithm;

import me.hvkcoder.java_basic.algorithm.visualAlgo.AlgoFrame;
import me.hvkcoder.java_basic.algorithm.visualAlgo.AlgoVisHelper;

import java.awt.*;
import java.util.Arrays;

/**
 * 随机分钱问题
 *
 * @author h_vk
 * @since 2023/10/10
 */
public class DivideMoneyRandomly {
	public static void main(String[] args) {
		int[] money = new int[100];
		Arrays.fill(money, 100);

		EventQueue.invokeLater(() -> {
			AlgoFrame frame = new AlgoFrame("随机分钱模拟", 1000, 800, (g2d, dimension) -> {
				int canvasWidth = (int) dimension.getWidth();
				int canvasHeight = (int) dimension.getHeight();
				int w = canvasWidth / money.length;
				for (int i = 0; i < money.length; i++) {
					if (money[i] > 0) {
						AlgoVisHelper.setColor(g2d, AlgoVisHelper.Blue);
						AlgoVisHelper.fillRectangle(g2d, i * w + 1, canvasHeight / 2 - money[i], w - 1, money[i]);
					} else if (money[i] < 0) {
						AlgoVisHelper.setColor(g2d, AlgoVisHelper.Red);
						AlgoVisHelper.fillRectangle(g2d, i * w + 1, canvasHeight / 2, w - 1, -money[i]);
					}
				}
			});

			frame.animation(40, () -> {
				Arrays.sort(money);
				for (int k = 0; k < 50; k++) {
					for (int i = 0; i < money.length; i++) {
						int j = (int) (Math.random() * money.length);
						money[i] -= 1;
						money[j] += 1;
					}
				}
			});
		});
	}
}
