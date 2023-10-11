package me.hvkcoder.java_basic.algorithm;

import lombok.Getter;
import me.hvkcoder.java_basic.algorithm.visualAlgo.AlgoFrame;
import me.hvkcoder.java_basic.algorithm.visualAlgo.AlgoVisHelper;

import javax.swing.*;
import java.awt.*;

/**
 * 冒泡排序
 *
 * @author h_vk
 * @since 2023/10/11
 */
public class SortBubble {

	public static void main(String[] args) {
		int DELAY = 40;
		SortData sortData = new SortData(50, 400);
		int[] data = sortData.getNumbers();

		EventQueue.invokeLater(() -> {
			AlgoFrame frame = new AlgoFrame("冒泡排序", (g2d, dimension) -> {
				int canvasWidth = (int) dimension.getWidth();
				int canvasHeight = (int) dimension.getHeight();

				int w = canvasWidth / sortData.getSize();
				for (int i = 0; i < sortData.getSize(); i++) {
					if (i == sortData.getCurrentCompareIndex() || i == sortData.getCurrentCompareNextIndex()) {
						AlgoVisHelper.setColor(g2d, AlgoVisHelper.Green);
					} else {
						AlgoVisHelper.setColor(g2d, AlgoVisHelper.Blue);
					}
					if (i >= sortData.getOrderedIndex()) {
						AlgoVisHelper.setColor(g2d, AlgoVisHelper.Orange);
					}
					AlgoVisHelper.fillRectangle(g2d, i * w + 1, canvasHeight - data[i], w - 1, data[i]);
				}
			});

			frame.animation(DELAY, () -> {
				int size = sortData.getSize();
				for (int i = 0; i < size; i++) {
					int sortedIndex = size - 1 - i;
					for (int j = 0; j < sortedIndex; j++) {
						sortData.setDataIndex(sortData.getOrderedIndex(), j, j + 1, frame, DELAY);
						if (data[j] > data[j + 1]) {
							int tmp = data[j];
							data[j] = data[j + 1];
							data[j + 1] = tmp;
						}
					}
					sortData.setDataIndex(sortedIndex, -1, -1, frame, DELAY);
				}
				sortData.setDataIndex(Integer.MAX_VALUE, -1, -1, frame, DELAY);
				Thread.currentThread().interrupt();
				System.out.println("排序完成");
			});
		});
	}

	@Getter
	private static class SortData {
		private int[] numbers;
		private final int size;
		private int orderedIndex; // 已排好序的索引区间
		private int currentCompareIndex; // 当前比较元素的索引
		private int currentCompareNextIndex; // 当前比较元素的下一个索引

		public SortData(int size, int maxValue) {
			this.size = size;
			this.generatorData(size, maxValue);
			this.orderedIndex = Integer.MAX_VALUE;
			this.currentCompareIndex = -1;
			this.currentCompareNextIndex = -1;
		}

		private void generatorData(int size, int maxValue) {
			this.numbers = new int[size];
			for (int i = 0; i < size; i++) {
				numbers[i] = (int) (Math.random() * maxValue) + 1;
			}
		}

		public void setDataIndex(int orderedIndex, int currentCompareIndex, int currentCompareNextIndex, JFrame frame, int delay) {
			try {
				this.orderedIndex = orderedIndex;
				this.currentCompareIndex = currentCompareIndex;
				this.currentCompareNextIndex = currentCompareNextIndex;
				frame.repaint();
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
